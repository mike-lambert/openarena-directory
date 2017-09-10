package com.cyberspacelabs.openarena.web.controller.rest;

import com.cyberspacelabs.openarena.dto.Definition;
import com.cyberspacelabs.openarena.service.DefinitionsService;
import com.cyberspacelabs.openarena.transform.QueryDefinitionToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 07.09.17.
 */
@RestController
@RequestMapping("/api/definitions")
public class DefinitionsController {
    @Autowired
    DefinitionsService definitionsService;

    @Autowired
    QueryDefinitionToDTO transformer;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Definition> getDefinitions() throws Exception {
        List<Definition> result = new ArrayList<>();
        definitionsService.enumerateDefinitions().forEach(def -> result.add(transformer.apply(def)));
        return result;
    }
}
