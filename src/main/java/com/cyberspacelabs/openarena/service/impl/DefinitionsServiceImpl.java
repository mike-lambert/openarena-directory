package com.cyberspacelabs.openarena.service.impl;

import com.cyberspacelabs.openarena.model.DiscoveryConfiguration;
import com.cyberspacelabs.openarena.model.QueryDefinition;
import com.cyberspacelabs.openarena.service.DefinitionsService;
import com.cyberspacelabs.openarena.service.DirectoryService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by mike on 08.09.17.
 */
@Service
public class DefinitionsServiceImpl implements DefinitionsService {
    @Value("${discovery.service.cache.size:10000}")
    private long cacheMaxSize;

    @Value("${discovery.service.cache.expires:300000}")
    private long cacheLifeTime;

    @Autowired
    private DiscoveryConfiguration discoveryConfiguration;

    private Cache<UUID, DirectoryService> directoryServiceCache;

    public DefinitionsServiceImpl(){
        directoryServiceCache = CacheBuilder.newBuilder()
                .expireAfterAccess(cacheLifeTime, TimeUnit.MILLISECONDS)
                .expireAfterWrite(cacheLifeTime, TimeUnit.MILLISECONDS)
                .maximumSize(cacheMaxSize)
                .build();
    }

    @Override
    public List<QueryDefinition> enumerateDefinitions() throws Exception {
        return Collections.unmodifiableList(discoveryConfiguration.getDefinitions());
    }

    @Override
    public DirectoryService getDirectoryServiceForDefinition(UUID definitionId) throws Exception {
        return directoryServiceCache.get(definitionId, () -> {
            if (definitionId == null){
                throw new IllegalArgumentException("Directory service ID cannot be null");
            }
            Optional<QueryDefinition> defopt = discoveryConfiguration.getDefinitions()
                    .stream()
                    .filter(def -> def.getId().equals(definitionId))
                    .findFirst();
            if (!defopt.isPresent()){
                throw new IllegalArgumentException("Directory service definition " + definitionId + " not found");
            }
            QueryDefinition def = defopt.get();
            DPMQueryDirectoryService result = new DPMQueryDirectoryService();
            result.setDPMQueryUrl(discoveryConfiguration.getBaseURL());
            result.setMasterAddress(def.getMaster());
            result.setMasterQuery(def.getQuery());
            result.setParams(def.getParams());
            return result;
        });
    }

    @Override
    public DirectoryService getDefaultDirectoryService() throws Exception {
        QueryDefinition first = discoveryConfiguration.getDefinitions().get(0);
        Optional<QueryDefinition> defopt = discoveryConfiguration.getDefinitions()
                .stream()
                .filter(def -> def.isDefaultDefintion())
                .findFirst();
        if (!defopt.isPresent() && first == null){
            throw new IllegalArgumentException("Default service definition not found");
        }
        if (defopt.isPresent()){
            first = defopt.get();
        }
        return getDirectoryServiceForDefinition(first.getId());
    }
}
