package com.cyberspacelabs.openarena.web.controller;


import com.cyberspacelabs.openarena.dto.Server;
import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.model.geoip.ProximityLevel;
import com.cyberspacelabs.openarena.service.DefinitionsService;
import com.cyberspacelabs.openarena.service.DirectoryService;
import com.cyberspacelabs.openarena.service.GeoIpMappingService;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import com.cyberspacelabs.openarena.transform.GameServerTransformer;
import com.cyberspacelabs.openarena.transform.ServerLocationDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.cyberspacelabs.gamebrowser.GameServer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 10.12.16.
 */
@Controller
@RequestMapping("/directory")
public class DirectoryViewController {
    @Autowired
    private DefinitionsService discoveryService;

    @Autowired
    private GeoIpResolutionService resolutionService;

    @Autowired
    private GeoIpMappingService mappingService;

    @Autowired
    private GameServerTransformer transformer;

    @Autowired
    private ServerLocationDecorator decorator;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) throws Exception{
        List<GameServer> retrieved = discoveryService.getDefaultDirectoryService().queryMaster();
        List<Server> servers = new ArrayList<>();
        retrieved.forEach(server -> {
            Server s = transformer.apply(server);
            decorator.apply(resolutionService, s);
            servers.add(s);
        });
        ModelAndView result = new ModelAndView("directory");
        result.addObject("request", request);
        result.addObject("servers", servers);
        return result;
    }

}
