package com.cyberspacelabs.openarena.web.controller.rest;

import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/geoip")
public class GeoIpController {
    @Autowired
    GeoIpResolutionService resolutionService;

    @RequestMapping(value = "/resolve", method = RequestMethod.GET)
    public Path<String> resolve(HttpServletRequest request) throws Exception{
        return resolutionService.resolve(request.getRemoteAddr());
    }
}
