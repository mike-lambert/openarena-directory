package com.cyberspacelabs.openarena.web.transform;

import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.web.dto.Directory;
import com.google.common.base.Function;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Created by mike on 11.12.16.
 */
public class ServerRecordSetToDirectoryDTO implements Function<Set<OpenArenaServerRecord>, Directory> {
    @Override
    public Directory apply(Set<OpenArenaServerRecord> input) {
        ServerRecordToServerDTO serverTransformer = new ServerRecordToServerDTO();
        Directory result = new Directory();
        input.forEach(server -> {
            result.setUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss XXX").format(new Date(server.getQueryTimestamp())));
            result.getServers().add(serverTransformer.apply(server));
        });
        return result;
    }
}
