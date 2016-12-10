package com.cyberspacelabs.openarena.service;

import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.model.qstat.Qstat;

import java.util.Set;

public interface QStatConversionService {
    Set<OpenArenaServerRecord> convert(Qstat input) throws Exception;
}
