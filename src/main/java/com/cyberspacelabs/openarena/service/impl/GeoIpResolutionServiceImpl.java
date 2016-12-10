package com.cyberspacelabs.openarena.service.impl;

import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.service.CountryFlagPictureService;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.UUID;

@PropertySources({
        @PropertySource(value = "application.properties", ignoreResourceNotFound = true),
        @PropertySource(value ="classpath:application.properties", ignoreResourceNotFound = true)
})
@Service
public class GeoIpResolutionServiceImpl implements GeoIpResolutionService, CountryFlagPictureService {
    @Value("${countries.flags.directory}")
    private String flagCachePath;

    public GeoIpResolutionServiceImpl(){
        flagCachePath = ".country-flags.png";
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
        InputStream in = new OkHttpClient().newCall(new Request.Builder().url("https://freegeoip.net/json/" + ip).build())
                .execute()
                .body().byteStream();
        FreeGeoIpResponse response = new ObjectMapper().readValue(in, FreeGeoIpResponse.class);
        ensureCountryFlagCache(response.country_code);
        Path result = new Path<>("IPv4:" + ip, response.country_name, response.region_name, response.city, response.zip_code, "IP", ip);
        result.setCountryCode(response.country_code);
        return result;
    }

    private void ensureCountryFlagCache(String countryCode) {
        Thread downloader = new Thread(() -> {
            File file = new File(flagCachePath, countryCode + ".png");
            if (!file.exists()){
                try {
                    downloadToCache(countryCode, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        downloader.setDaemon(true);
        downloader.setName(this.getClass().getSimpleName()+ "-Downloader::" + new SimpleDateFormat("yyyyMMddHHmmssSSSXXX") + "." + UUID.randomUUID());
        downloader.start();
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
}
