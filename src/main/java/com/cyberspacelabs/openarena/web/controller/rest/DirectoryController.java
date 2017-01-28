package com.cyberspacelabs.openarena.web.controller.rest;

import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.model.geoip.ProximityLevel;
import com.cyberspacelabs.openarena.service.GeoIpMappingService;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import com.cyberspacelabs.openarena.service.OpenArenaDirectoryService;
import com.cyberspacelabs.openarena.web.dto.Directory;
import com.cyberspacelabs.openarena.web.dto.DirectoryQuery;
import com.cyberspacelabs.openarena.web.dto.Server;
import com.cyberspacelabs.openarena.web.transform.DiscoveryRecordToDirectoryDTO;
import com.cyberspacelabs.openarena.web.transform.ServerLocationDecorator;
import com.cyberspacelabs.openarena.web.transform.ServerRecordSetToDirectoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
@RequestMapping("/api/directory")
public class DirectoryController {
    @Autowired
    private OpenArenaDirectoryService directoryService;

    @Autowired
    private GeoIpMappingService mappingService;

    @Autowired
    private GeoIpResolutionService resolutionService;

    @RequestMapping(method = RequestMethod.GET)
    public Directory all() throws Exception{
        Directory directory = new DiscoveryRecordToDirectoryDTO().apply(directoryService.refreshDiscovery());
        new ServerLocationDecorator().decorate(directory, resolutionService);
        return directory;
    }

    @RequestMapping(value = "/nearby/{level}",method = RequestMethod.GET)
    public Directory nearby(@PathVariable("level")ProximityLevel level, HttpServletRequest request) throws Exception{
        Directory directory = new ServerRecordSetToDirectoryDTO().apply(mappingService.nearby(request.getRemoteAddr(), level));
        new ServerLocationDecorator().decorate(directory, resolutionService);
        return  directory;
    }

    @RequestMapping(value = "/discovery/list", method = RequestMethod.GET)
    public List<String> listDiscoveryServers() throws Exception{
        return directoryService.enumerateDiscoveryServers();
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Directory queryDirectory(@RequestBody DirectoryQuery query, HttpServletRequest request) throws Exception {
        final Set<OpenArenaServerRecord> source = new CopyOnWriteArraySet<>();
                if (query.getLevel() == null || ProximityLevel.GLOBAL.equals(query.getLevel())){
                    directoryService.refreshDiscovery()
                            .parallelStream()
                            .filter(record ->
                                query.getServers().isEmpty() ||
                                query.getServers().contains(record.getServerHost() + ":" + record.getServerPort())
                            ).forEach(record -> source.addAll(record.getRecords()));

                } else {
                    source.addAll(directoryService.filterForDiscovery(mappingService.nearby(request.getRemoteAddr(), query.getLevel()), query.getServers()));
                }

        Directory directory = new ServerRecordSetToDirectoryDTO().apply(source);
        new ServerLocationDecorator().decorate(directory, resolutionService);
        return directory;
    }
}
