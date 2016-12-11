package com.cyberspacelabs.openarena.service.impl;

import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.model.geoip.ProximityLevel;
import com.cyberspacelabs.openarena.service.GeoIpMappingService;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GeoIpMappingServiceImpl implements GeoIpMappingService {
    private final static String DIRECTORY_FILE = "_.json";
    @Value("${geoip.mapping.cache.path:./.geoip/mapping}")
    private String cachePath;
    private Map<String, Path<Set<OpenArenaServerRecord>>> reverseCache;
    private Set<Path<Set<OpenArenaServerRecord>>> cache;
    @Autowired
    private GeoIpResolutionService resolutionService;
    private ExecutorService cacheSaver;
    private AtomicLong threadCounter;


    public GeoIpMappingServiceImpl() {
        cachePath = "./.geoip/mapping";
        reverseCache = new ConcurrentHashMap<>();
        cache = new CopyOnWriteArraySet<>();
        threadCounter = new AtomicLong(0);
        cacheSaver = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread runner = new Thread(r);
                runner.setDaemon(true);
                runner.setName(GeoIpMappingServiceImpl.class.getSimpleName()
                        + "-Saver::"
                        + new SimpleDateFormat("yyyyMMddHHmmssXXX")
                        + "." + threadCounter.incrementAndGet());
                return runner;
            }
        });
    }

    public GeoIpMappingServiceImpl(GeoIpResolutionService resolver) {
        this();
        resolutionService = resolver;
    }

    @Override
    public Set<OpenArenaServerRecord> nearby(String ip, ProximityLevel proximityLevel) throws Exception {
        Set<OpenArenaServerRecord> result = new CopyOnWriteArraySet<>();
        cache.parallelStream().forEach(path -> {
            if (proximityLevel == null || proximityLevel == ProximityLevel.GLOBAL){
                result.addAll(path.getValue());
                return;
            }
            Path<String> address = null;
            try {
                address = resolutionService.resolve(ip);
                switch (proximityLevel){
                    case ZIP:
                        if (address.isSameLocation(path)){
                            result.addAll(path.getValue());
                        }
                        break;

                    case CITY:
                        if (    address.getCity().equals(path.getCity())
                                && address.getRegion().equals(path.getRegion())
                                && address.getCountry().equals(path.getCountry())
                                ){
                            result.addAll(path.getValue());
                        }
                        break;

                    case REGION:
                        if (    address.getRegion().equals(path.getRegion())
                                && address.getCountry().equals(path.getCountry())
                                ){
                            result.addAll(path.getValue());
                        }
                        break;

                    case COUNTRY:
                        if (address.getCountry().equals(path.getCountry())){
                            result.addAll(path.getValue());
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    @Override
    public Path<OpenArenaServerRecord> locate(OpenArenaServerRecord server) throws Exception {
        String ip = getHostAddress(server);
        Path<String> address = resolutionService.resolve(ip);
        String marker = address.getCity() + "/" +address.getZipCode();
        Path<Set<OpenArenaServerRecord>> geo;
        try {
            geo = cache.stream().filter(path -> path.isSameLocation(address)).findFirst().get();
        } catch (NoSuchElementException nsex){
            System.out.println("Locating " + server.getAddress() + "; is not in cache; resolving");
            geo = new Path<>(marker,
                    address.getCountry(),
                    address.getRegion(),
                    address.getCity(),
                    address.getZipCode(),
                    marker, new CopyOnWriteArraySet<>());
            geo.setCountryCode(address.getCountryCode());
            cache.add(geo);
        }

        geo.getValue(Set.class).add(server);
        reverseCache.put(ip, geo);
        saveCache();
        return new Path<OpenArenaServerRecord>(marker,
                address.getCountry(),
                address.getRegion(),
                address.getCity(),
                address.getZipCode(),
                marker, server);
    }

    @Override
    public void register(Path<OpenArenaServerRecord> entry) {
        OpenArenaServerRecord record = entry.getValue();
        String ip = getHostAddress(record);
        Path<Set<OpenArenaServerRecord>> geo = null;
        try {
            Path address = resolutionService.resolve(ip);
            try {
                geo = cache.stream().filter(path -> path.isSameLocation(address))
                        .findFirst().get();
            } catch (NoSuchElementException nsxe){
                System.out.println("Registering " + record.getAddress() + " is not in cache; resolving");
                String marker = address.getCity() + "/" +address.getZipCode();
                geo = new Path<>(marker,
                        address.getCountry(),
                        address.getRegion(),
                        address.getCity(),
                        address.getZipCode(),
                        marker, new CopyOnWriteArraySet<>());
                geo.setCountryCode(address.getCountryCode());
                cache.add(geo);
            }
            geo.getValue().add(record);
            reverseCache.put(ip, geo);
            saveCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Path createEmptyPath(Path<Set<OpenArenaServerRecord>> original){
        Path result = new Path(original.getName(), original.getCountry(), original.getRegion(), original.getCity(), original.getZipCode(), original.getLabel(), new CopyOnWriteArraySet<>());
        result.setCountryCode(original.getCountryCode());
        return result;
    }

    private void saveCache() {
        ObjectMapper json = new ObjectMapper();
        json.enable(SerializationFeature.INDENT_OUTPUT);
        cacheSaver.submit(() -> {
            ensureCacheDirectory();
            cache.parallelStream().forEach(path -> {
                String normpath = path.getPath().replace("//", "/_/");
                File target = new File(cachePath, normpath);
                target.mkdirs();
                File jsonFilePath = new File(target, DIRECTORY_FILE);
                try {
                    System.out.println(jsonFilePath.getAbsolutePath() + " <- " + path.getValue().size());
                    json.writeValue(jsonFilePath, createEmptyPath(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Set<OpenArenaServerRecord> records = path.getValue();
                records.parallelStream().forEach(record -> {
                    String normName = record.getAddress().replace(":", "-");
                    File jsonFile = new File(target, normName);
                    try {
                        System.out.println("  + " + jsonFile.getAbsolutePath() + " <- " + record);
                        json.writeValue(jsonFile, record);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
        });
    }

    private void ensureCacheDirectory() {
        new File(cachePath).mkdirs();
    }

    private String getHostAddress(OpenArenaServerRecord record) {
        String endpoint = record.getAddress();
        int port = endpoint.indexOf(":");
        if (port == -1) {
            return endpoint;
        } else {
            return endpoint.substring(0, port);
        }
    }



    @PostConstruct
    public void loadCache(){
        ObjectMapper mapper = new ObjectMapper();
        ensureCacheDirectory();
        for(File country : new File(cachePath).listFiles()){
            if (country.isDirectory()){
                for(File region : country.listFiles()){
                    if (region.isDirectory()){
                        File directory = new File(region, DIRECTORY_FILE);
                        if (directory.exists() && directory.isFile()){
                            try {
                                Path<Set<OpenArenaServerRecord>> path = mapper.readValue(directory, Path.class);
                                cache.add(path);
                                for(File city : region.listFiles()){
                                    if (city.isFile() && !city.getName().equals(DIRECTORY_FILE)){
                                        OpenArenaServerRecord record = mapper.readValue(city, OpenArenaServerRecord.class);
                                        path.getValue().add(record);
                                        reverseCache.put(getHostAddress(record), path);
                                        System.out.println("  " + city.getAbsolutePath() + " <- " + record);
                                    }
                                }
                                System.out.println(directory.getAbsolutePath() + " <- " + path.getValue().size());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        for(File city : region.listFiles()){
                            if (city.isDirectory()){
                                for (File zip : city.listFiles()){
                                    if (zip.isDirectory()){
                                        File zip_directory = new File(zip, DIRECTORY_FILE);
                                        if (zip_directory.exists() && zip_directory.isFile()){
                                            try {
                                                Path<Set<OpenArenaServerRecord>> path = mapper.readValue(zip_directory, Path.class);
                                                cache.add(path);
                                                for(File entry : zip.listFiles()){
                                                    if (entry.isFile() && !entry.getName().equals(DIRECTORY_FILE)){
                                                        OpenArenaServerRecord record = mapper.readValue(entry, OpenArenaServerRecord.class);
                                                        path.getValue().add(record);
                                                        reverseCache.put(getHostAddress(record), path);
                                                        System.out.println("  " + entry.getAbsolutePath() + " <- " + record);
                                                    }
                                                }
                                                System.out.println(zip_directory.getAbsolutePath() + " <- " + path.getValue().size());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        System.out.println("Mapping cache: " + cache.size());
        System.out.println("Reverse cache: " + reverseCache.size());
    }
}
