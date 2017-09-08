package com.cyberspacelabs.openarena.service;

import com.cyberspacelabs.openarena.model.QueryRequest;
import ru.cyberspacelabs.gamebrowser.GameServer;

import java.util.List;

/**
 * Created by mike on 07.09.17.
 */
public interface DirectoryService {
    void setMasterAddress(String masterAddress);
    void setMasterQuery(String masterQuery);
    void setParams(QueryRequest params);
    List<GameServer> queryMaster() throws Exception;
}
