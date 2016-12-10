package com.cyberspacelabs.openarena.web.transform;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;
import com.cyberspacelabs.openarena.web.dto.DirectoryDTO;
import com.google.common.base.Function;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class DiscoveryRecordToDirectoryDTO implements Function<Set<OpenArenaDiscoveryRecord>, DirectoryDTO> {
    @Override
    public DirectoryDTO apply(Set<OpenArenaDiscoveryRecord> input) {
        ServerRecordToServerDTO serverTransformer = new ServerRecordToServerDTO();
        DirectoryDTO result = new DirectoryDTO();
        input.forEach(discovery -> {
            result.setUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss XXX").format(new Date(discovery.getLastQueried())));
            discovery.getRecords().forEach(record -> result.getServers().add(serverTransformer.apply(record)));
        });
        return result;
    }
}
