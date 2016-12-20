package com.cyberspacelabs.openarena.service.impl;

import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.service.CountryFlagPictureService;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

@PropertySources({
        @PropertySource(value = "application.properties", ignoreResourceNotFound = true),
        @PropertySource(value ="classpath:application.properties", ignoreResourceNotFound = true)
})
@Service
public class GeoIpResolutionServiceImpl implements GeoIpResolutionService, CountryFlagPictureService {
    @Value("${countries.flags.directory:./.geoip/flags}")
    private String flagCachePath;

    @Value("${geoip.resolving.cache.path:./.geoip/v4}")
    private String cachePath;
    private Map<String, Path<String>> cache;
    private ExecutorService cacheSaver;
    private AtomicLong threadCounter;

    public GeoIpResolutionServiceImpl(){
        flagCachePath = "./.geoip/flags";
        cachePath = "./.geoip/v4";
        cache = new ConcurrentHashMap<>();
        threadCounter = new AtomicLong(0);
        cacheSaver = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread runner = new Thread(r);
                runner.setDaemon(true);
                runner.setName(GeoIpResolutionServiceImpl.class.getSimpleName()
                        + "-Saver::"
                        + new SimpleDateFormat("yyyyMMddHHmmssXXX")
                        + "." + threadCounter.incrementAndGet());
                return runner;
            }
        });
    }

    public static class FreeGeoIpResponse {
        public String ip;
        public String country_code;
        public String country_name;
        public String region_code;
        public String region_name;
        public String city;
        public String zip_code;
        public String time_zone;
        public double latitude;
        public double longitude;
        public int metro_code;
    }

    @Override
    public Path<String> resolve(String ip) throws Exception {
        if (cache.get(ip) == null){
            InputStream in = new OkHttpClient().newCall(new Request.Builder().url("https://freegeoip.net/json/" + ip).build())
                    .execute()
                    .body().byteStream();
            FreeGeoIpResponse response = new ObjectMapper().readValue(in, FreeGeoIpResponse.class);
            ensureCountryFlagCache(response.country_code);
            Path result = new Path<>("IPv4:" + ip, response.country_name, response.region_name, response.city, response.zip_code, "IP", ip);
            result.setCountryCode(response.country_code);
            cache.put(ip, result);
            saveCache();
        }
        return cache.get(ip);
    }

    private void saveCache() {
        cacheSaver.submit(() -> {
            File root = new File(cachePath);
            root.mkdirs();
            cache.entrySet().parallelStream().forEach(entry -> {
                String path = entry.getKey().replace(".", "/");
                File folder = new File(root, path);
                folder.mkdirs();
                ObjectMapper json = new ObjectMapper();
                json.enable(SerializationFeature.INDENT_OUTPUT);
                File target = new File(folder, entry.getValue().getValue());
                try {
                    json.writeValue(target, entry.getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void ensureCountryFlagCache(String countryCode) {
        cacheSaver.submit(() -> {
            ensureFlagCacheDirectory();
            File file = new File(flagCachePath, countryCode + ".png");
            if (!file.exists()){
                try {
                    downloadToCache(countryCode, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void ensureFlagCacheDirectory() {
        new File(flagCachePath).mkdirs();
    }

    @Override
    public InputStream getPNG(String countryCode) throws Exception{
        return bufferizePNG(countryCode);
    }

    private InputStream bufferizePNG(String countryCode) throws IOException {
        File file = new File(flagCachePath, countryCode + ".png");
        if (file.exists() && file.isFile()){
            return new ByteArrayInputStream(readAll(file));
        }
        downloadToCache(countryCode, file);
        return bufferizePNG(countryCode);
    }

    private void downloadToCache(String countryCode, File file) throws IOException {
        InputStream in = new OkHttpClient().newCall(new Request.Builder().url("http://www.geognos.com/api/en/countries/flag/" + countryCode + ".png").build())
                .execute()
                .body().byteStream();
        saveTo(in, file);
    }

    private void saveTo(InputStream in, File file) throws IOException{
        byte[] buffer = new byte[4096];
        int counter = -1;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            do {
                counter = in.read(buffer);
                if (counter > 0){ fos.write(buffer, 0, counter);}
            }while (counter > 0);
        } finally {
            if (fos != null){ fos.close();}
        }
    }

    private byte[] readAll(File file) throws IOException{
        byte[] buffer = new byte[4096];
        int counter = -1;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            do{
                counter = in.read(buffer);
                if (counter > 0){ out.write(buffer, 0, counter);}
            }while(counter > 0);
            return out.toByteArray();
        } finally {
            if (in != null){ in.close(); }
        }
    }

    public String getFlagCachePath() {
        return flagCachePath;
    }

    public void setFlagCachePath(String flagCachePath) {
        this.flagCachePath = flagCachePath;
    }

    public int cacheSize(){
        return cache.size();
    }

    @PostConstruct
    public void loadCache(){
        ObjectMapper json = new ObjectMapper();
        File root = new File(cachePath);
        root.mkdirs();
        for(File level1 : root.listFiles()){
            if (level1.isDirectory()){
                for(File level2 : level1.listFiles()){
                    if (level2.isDirectory()){
                        for(File level3 : level2.listFiles()){
                            if (level3.isDirectory()){
                                for (File level4 : level3.listFiles()){
                                    if (level4.isDirectory()){
                                        for(File entry : level4.listFiles()){
                                            if (entry.isFile()){
                                                Path<String> path = null;
                                                try {
                                                    System.out.println(this.getClass().getSimpleName() + ".loadCache: reading " + entry.getAbsolutePath());
                                                    path = json.readValue(entry, Path.class);
                                                    String ip = path.getValue();
                                                    System.out.println(this.getClass().getSimpleName() + ".loadCache: " + ip + " <- " + path.getPath() + " [" + path.getCountryCode() + "]");
                                                    cache.put(ip, path);
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
        }
        System.out.println("GeoIP cache: " + cache.size());
    }
}
