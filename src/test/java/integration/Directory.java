package integration;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;
import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.service.GeoIpMappingService;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import com.cyberspacelabs.openarena.service.OpenArenaDirectoryService;
import com.cyberspacelabs.openarena.web.controller.rest.DirectoryController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class Directory {
    @InjectMocks
    private DirectoryController controller;

    @Mock
    private OpenArenaDirectoryService directoryService;

    @Mock
    private GeoIpMappingService mappingService;

    @Mock
    private GeoIpResolutionService resolutionService;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(controller).build();

    }

    @Test
    public void directoryDiscoverySucceeded() throws Exception {
        when(directoryService.enumerate()).thenReturn(createTestDiscoverySet());
        this.mvc.perform(
                get("/api/directory")
        )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("cyberspacelabs.ru:")))
        .andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));
    }

    private Set<OpenArenaDiscoveryRecord> createTestDiscoverySet() {
        OpenArenaServerRecord record1 = new OpenArenaServerRecord("cyberspacelabs.ru:27960");
        record1.setDisplayName("OpenArena@Cyberspace Labs");
        record1.setGameType("baseoa");
        record1.setMap("oadm1");
        record1.setPing(32);
        record1.setPlayersPresent(2);
        record1.setQueryTimestamp(System.currentTimeMillis());
        record1.setRetries(0);
        record1.setSlotsAvailable(8);
        record1.setStatus(OpenArenaServerRecord.ServerStatus.UP);

        OpenArenaServerRecord record2 = new OpenArenaServerRecord("212.20.130.251:27960");
        record2.setDisplayName("Q3@Cyberspace Labs");
        record2.setGameType("DM");
        record2.setMap("q3dm1");
        record2.setPing(62);
        record2.setPlayersPresent(12);
        record2.setQueryTimestamp(System.currentTimeMillis());
        record2.setRetries(0);
        record2.setSlotsAvailable(80);
        record2.setStatus(OpenArenaServerRecord.ServerStatus.UP);
        record2.setServerType("Q3A");

        OpenArenaDiscoveryRecord discovery = new OpenArenaDiscoveryRecord("cyberspacelabs.ru:27950");
        discovery.setDirectoryName("Cyberspace Labs Directory");
        discovery.setLastQueried(System.currentTimeMillis());
        discovery.getRecords().add(record1);
        discovery.getRecords().add(record2);

        Set<OpenArenaDiscoveryRecord> result = new CopyOnWriteArraySet<>();
        result.add(discovery);
        return result;
    }
}
