package com.cyberspacelabs.openarena.web.controller.rest;

import com.cyberspacelabs.openarena.model.geoip.ProximityLevel;
import com.cyberspacelabs.openarena.service.GeoIpMappingService;
import com.cyberspacelabs.openarena.service.OpenArenaDirectoryService;
import com.cyberspacelabs.openarena.web.dto.DirectoryDTO;
import com.cyberspacelabs.openarena.web.transform.DiscoveryRecordToDirectoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/directory")
public class DirectoryController {
    @Autowired
    private OpenArenaDirectoryService directoryService;

    @Autowired
    private GeoIpMappingService mappingService;

    @RequestMapping(method = RequestMethod.GET)
    public DirectoryDTO all() throws Exception{
        return new DiscoveryRecordToDirectoryDTO().apply(directoryService.enumerate());
    }

    @RequestMapping(value = "/nearby/{level}",method = RequestMethod.GET)
    public DirectoryDTO nearby(@PathVariable("level")ProximityLevel level, HttpServletRequest request) throws Exception{
        return new DiscoveryRecordToDirectoryDTO().apply(directoryService.enumerate());
    }
}
