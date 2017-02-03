package com.cyberspacelabs.openarena.web.controller;

import com.cyberspacelabs.openarena.model.geoip.ProximityLevel;
import com.cyberspacelabs.openarena.service.GeoIpMappingService;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import com.cyberspacelabs.openarena.service.OpenArenaDirectoryService;
import com.cyberspacelabs.openarena.web.dto.Directory;
import com.cyberspacelabs.openarena.web.transform.DiscoveryRecordToDirectoryDTO;
import com.cyberspacelabs.openarena.web.transform.ServerDtoRenderer;
import com.cyberspacelabs.openarena.web.transform.ServerLocationDecorator;
import com.cyberspacelabs.openarena.web.transform.ServerRecordSetToDirectoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private GeoIpMappingService mappingService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) throws Exception{
        Directory directory = new DiscoveryRecordToDirectoryDTO().apply(directoryService.refreshDiscovery());
        new ServerLocationDecorator().decorate(directory, resolutionService);
        return createDirectoryMV(request, directory);
    }

    @RequestMapping("/nearby/{level}")
    public ModelAndView nearby(HttpServletRequest request, @PathVariable("level")ProximityLevel level) throws Exception {
        Directory directory = new ServerRecordSetToDirectoryDTO().apply(mappingService.nearby(request.getRemoteAddr(), level));
        new ServerLocationDecorator().decorate(directory, resolutionService);
        return createDirectoryMV(request, directory);
    }

    private ModelAndView createDirectoryMV(HttpServletRequest request, Directory directory) throws Exception {
        ModelAndView result = new ModelAndView("directory");
        ServerDtoRenderer renderer=  new ServerDtoRenderer();
        renderer.setRequest(request);
        result.addObject("request", request);
        result.addObject("renderer", renderer);
        result.addObject("directory", directory);
        result.addObject("discovery", directoryService.enumerateDiscoveryServers());
        try {
            result.addObject("location", resolutionService.resolve(request.getRemoteAddr()).getPath());
        } catch (Exception e){
            result.addObject("location", "Unknown");
        }
        return result;
    }

}
