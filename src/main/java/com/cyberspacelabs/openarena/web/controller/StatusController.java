package com.cyberspacelabs.openarena.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/status")
public class StatusController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request){
        ModelAndView result = new ModelAndView("status");
        result.addObject("ip", request.getRemoteAddr());
        result.addObject("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss XXX").format(new Date()));
        return result;
    }
}
