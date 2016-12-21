package com.cyberspacelabs.openarena.web.controller.rest;

import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.service.CountryFlagPictureService;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/geoip")
public class GeoIpController {
    @Autowired
    private GeoIpResolutionService resolutionService;

    @Autowired
    private CountryFlagPictureService countryFlagPictureService;

    @RequestMapping(value = "/resolve", method = RequestMethod.GET)
    public Path<String> resolve(HttpServletRequest request) throws Exception{
        return resolutionService.resolve(request.getRemoteAddr());
    }

    @RequestMapping(value = "/flag/{code}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getFlagPicture(@PathVariable("code") String countryCode) throws Exception {
        return StreamUtils.copyToByteArray(countryFlagPictureService.getPNG(countryCode));
    }
}
