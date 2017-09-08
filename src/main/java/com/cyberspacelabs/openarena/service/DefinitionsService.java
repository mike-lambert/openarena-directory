package com.cyberspacelabs.openarena.service;

import com.cyberspacelabs.openarena.model.QueryDefinition;

import java.util.List;
import java.util.UUID;

/**
 * Created by mike on 07.09.17.
 */
public interface DefinitionsService {
    List<QueryDefinition> enumerateDefinitions() throws Exception;
    DirectoryService getDirectoryServiceForDefinition(UUID definitionId) throws Exception;
    DirectoryService getDefaultDirectoryService() throws Exception;
}
