package com.cyberspacelabs.openarena.service.impl;

import com.cyberspacelabs.openarena.model.QueryRequest;
import com.cyberspacelabs.openarena.service.DirectoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import okhttp3.*;
import ru.cyberspacelabs.gamebrowser.GameServer;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by mike on 07.09.17.
 */
public class DPMQueryDirectoryService implements DirectoryService {
    private String DPMQueryUrl;
    private OkHttpClient http;
    private ObjectMapper json;
    private QueryRequest requestParams;
    private String masterAddress;
    private String masterQuery;

    public DPMQueryDirectoryService(){
        http = new OkHttpClient();
        json = new ObjectMapper();
    }

    @Override
    public void setMasterAddress(String masterAddress) {
        this.masterAddress = masterAddress;
    }

    @Override
    public void setMasterQuery(String masterQuery) {
        this.masterQuery = masterQuery;
    }

    @Override
    public void setParams(QueryRequest params) {
        requestParams = params;
    }

    @Override
    public List<GameServer> queryMaster() throws Exception {
        Request request = null;
        if (requestParams == null){
            request = new Request.Builder()
                    .url(getUrlFor(masterAddress, masterQuery))
                    .get()
                    .build();
        } else {
            request = new Request.Builder()
                    .url(getUrlFor(masterAddress, masterQuery))
                    .post(
                            RequestBody.create(MediaType.parse("application/json"), json.writeValueAsString(requestParams))
                    )
                    .build();
        }
        Response response = http.newCall(request).execute();
        if (response == null){
            throw new IllegalStateException("Empty response on " + request.url().toString());
        }

        if (response.code() != 200){
            throw new IllegalStateException("Request failed: " + response.code() + " on " + request.url().toString());
        }

        return json.readValue(response.body().string(), TypeFactory.defaultInstance().constructCollectionType(List.class, GameServer.class));
    }

    private String getUrlFor(String masterAddress, String masterQuery) throws Exception {
        return DPMQueryUrl + "/api/v1/master/query/"
                + URLEncoder.encode(masterAddress, "UTF-8") + "/"
                + URLEncoder.encode(masterQuery, "UTF-8");
    }

    public void setDPMQueryUrl(String DPMQueryUrl) {
        this.DPMQueryUrl = DPMQueryUrl;
    }
}
