package integration;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;
import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.model.geoip.ProximityLevel;
import com.cyberspacelabs.openarena.service.GeoIpMappingService;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import com.cyberspacelabs.openarena.service.OpenArenaDirectoryService;
import com.cyberspacelabs.openarena.web.controller.rest.DirectoryController;
import com.cyberspacelabs.openarena.web.dto.DirectoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
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

    private Path<String> decoratedPath = new Path<String>("Decoration", "Russia", "NVB", "Novosibirsk", "630099", "center", "0.0.0.0");

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(controller).build();
        decoratedPath.setCountryCode("RU");

        when(directoryService.enumerate()).thenReturn(createTestDiscoverySet());
        when(mappingService.nearby(anyString(), eq(ProximityLevel.GLOBAL))).thenReturn(createTestRecordSet());
        when(mappingService.nearby(anyString(), eq(ProximityLevel.COUNTRY))).thenReturn(
                createTestRecordSet()
                        .stream()
                        .filter(record -> !record.getAddress().equals("61.78.194.195:27960"))
                        .collect(Collectors.toSet())
        );
        when(resolutionService.resolve(anyString())).thenReturn(decoratedPath);
    }

    @Test
    public void directoryDiscoverySucceeded() throws Exception {

        this.mvc.perform(
                get("/api/directory")
        )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("cyberspacelabs.ru:")))
        .andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void nearbySearchSucceeded() throws Exception {
        when(mappingService.nearby(anyString(), eq(ProximityLevel.REGION)))
                .thenReturn(createTestRecordSet()
                        .stream()
                        .filter(record -> !record.getAddress().equals("61.78.194.195:27960"))
                        .filter( record -> !record.getAddress().contains("cyberspacelabs"))
                        .collect(Collectors.toSet())
                );
        when(mappingService.nearby(anyString(), eq(ProximityLevel.CITY)))
                .thenReturn(createTestRecordSet()
                        .stream()
                        .filter(record -> !record.getAddress().equals("61.78.194.195:27960"))
                        .filter( record -> !record.getAddress().contains("cyberspacelabs"))
                        .filter( record -> record.getAddress().endsWith("96:27960"))
                        .collect(Collectors.toSet())
                );
        when(mappingService.nearby(anyString(), eq(ProximityLevel.ZIP)))
                .thenReturn(createTestRecordSet()
                        .stream()
                        .filter(record -> !record.getAddress().equals("61.78.194.195:27960"))
                        .filter( record -> !record.getAddress().contains("cyberspacelabs"))
                        .filter( record -> record.getAddress().endsWith(".196:27960"))
                        .collect(Collectors.toSet())
                );

        this.mvc.perform(
                get("/api/directory/nearby/GLOBAL")
        )
        .andExpect(status().isOk())
        .andExpect(
            result -> {
                if (deserializeJson(result.getResponse().getContentAsString(), DirectoryDTO.class).getServers().size() != 5) {
                    throw new IllegalStateException("Not all records returned (expected 5): \r\n" + result.getResponse().getContentAsString());
                }
            }
        )
        .andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));

        this.mvc.perform(
                get("/api/directory/nearby/COUNTRY")
        )
        .andExpect(status().isOk())
        .andExpect(
            result -> {
                int expc = 4;
                int size = deserializeJson(result.getResponse().getContentAsString(), DirectoryDTO.class).getServers().size();
                if (size != expc) {
                    throw new IllegalStateException("Record count mismatch: " + size +" (expected " + expc +"): \r\n"
                        + result.getResponse().getContentAsString());
                }
            }
        )
        .andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));

        this.mvc.perform(
                get("/api/directory/nearby/REGION")
        )
        .andExpect(status().isOk())
        .andExpect(
            result -> {
                int expc = 3;
                int size = deserializeJson(result.getResponse().getContentAsString(), DirectoryDTO.class).getServers().size();
                if (size != expc) {
                    throw new IllegalStateException("Record count mismatch: " + size +" (expected " + expc +"): \r\n"
                        + result.getResponse().getContentAsString());
                }
            }
        )
        .andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));

        this.mvc.perform(
                get("/api/directory/nearby/CITY")
        )
        .andExpect(status().isOk())
        .andExpect(
            result -> {
                int expc = 2;
                int size = deserializeJson(result.getResponse().getContentAsString(), DirectoryDTO.class).getServers().size();
                if (size != expc) {
                    throw new IllegalStateException("Record count mismatch: " + size +" (expected " + expc +"): \r\n"
                        + result.getResponse().getContentAsString());
                }
            }
        )
        .andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));

        this.mvc.perform(
                get("/api/directory/nearby/ZIP")
        )
        .andExpect(status().isOk())
        .andExpect(
            result -> {
                int expc = 1;
                int size = deserializeJson(result.getResponse().getContentAsString(), DirectoryDTO.class).getServers().size();
                if (size != expc) {
                    throw new IllegalStateException("Record count mismatch: " + size +" (expected " + expc +"): \r\n"
                        + result.getResponse().getContentAsString());
                }
            }
        )
        .andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));
    }

    private <T> T deserializeJson(String json, Class<T> type) throws IOException {
        return new ObjectMapper().readValue(json, type);
    }

    private Set<OpenArenaDiscoveryRecord> createTestDiscoverySet() {

        OpenArenaDiscoveryRecord discovery = new OpenArenaDiscoveryRecord("cyberspacelabs.ru:27950");
        discovery.setDirectoryName("Cyberspace Labs Directory");
        discovery.setLastQueried(System.currentTimeMillis());
        discovery.getRecords().addAll(createTestRecordSet());

        Set<OpenArenaDiscoveryRecord> result = new CopyOnWriteArraySet<>();
        result.add(discovery);
        return result;
    }

    private Set<OpenArenaServerRecord> createTestRecordSet() {
        Set<OpenArenaServerRecord> result = new CopyOnWriteArraySet<>();
        result.add(createServerRecord("cyberspacelabs.ru:27960", "baseoa", "oadm1", "OpenArena FFA", 32, 2, 8));
        result.add(createServerRecord("212.20.130.251:27960", "DM", "aggressor", "OpenArena DM", 62, 12, 80));
        result.add(createServerRecord("109.81.225.96:27960", "CTF", "pxlrfan", "OpenArena CTF", 64, 16, 24));
        result.add(createServerRecord("61.78.194.196:27960", "baseoa", "lavaarena", "Deathmatch Arena", 112, 6, 16));
        result.add(createServerRecord("61.78.194.195:27960", "baseoa", "oasago2", "Deathmatch Arena 2", 112, 7, 10));
        return result;
    }

    private OpenArenaServerRecord createServerRecord(String address, String gametype, String map, String name,
                                                     long ping, int present, int available){
        OpenArenaServerRecord result = new OpenArenaServerRecord(address);
        result.setDisplayName(name);
        result.setGameType(gametype);
        result.setMap(map);
        result.setPing(ping);
        result.setPlayersPresent(present);
        result.setSlotsAvailable(available);
        result.setQueryTimestamp(System.currentTimeMillis());
        result.setStatus(OpenArenaServerRecord.ServerStatus.UP);
        return result;

    }
}
