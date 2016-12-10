package com.cyberspacelabs.openarena.web.controller;

import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import com.cyberspacelabs.openarena.service.OpenArenaDirectoryService;
import com.cyberspacelabs.openarena.web.dto.DirectoryDTO;
import com.cyberspacelabs.openarena.web.transform.DiscoveryRecordToDirectoryDTO;
import com.cyberspacelabs.openarena.web.transform.ServerDtoRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mike on 10.12.16.
 */
@Controller
@RequestMapping("/directory")
public class DirectoryViewController {
    @Autowired
    private OpenArenaDirectoryService directoryService;

    @Autowired
    private GeoIpResolutionService resolutionService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) throws Exception{
        DirectoryDTO directory = new DiscoveryRecordToDirectoryDTO().apply(directoryService.enumerate());
        directory.getServers().parallelStream().forEach(server -> {
            try {
                Path<String> path = resolutionService.resolve(getHost(server.getAddress()));
                server.getLocation().setCode(path.getCountryCode());
                server.getLocation().setDomain(path.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ModelAndView result = new ModelAndView("directory");
        result.addObject("request", request);
        result.addObject("renderer", new ServerDtoRenderer());
        result.addObject("directory", directory);
        return result;
    }

    private String getHost(String endpoint) {
        int port = endpoint.indexOf(":");
        if (port == -1) {
            return endpoint;
        } else {
            return endpoint.substring(0, port);
        }
    }
}
