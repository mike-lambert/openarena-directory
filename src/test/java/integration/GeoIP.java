package integration;

import com.cyberspacelabs.openarena.service.CountryFlagPictureService;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import com.cyberspacelabs.openarena.service.impl.GeoIpResolutionServiceImpl;
import com.cyberspacelabs.openarena.web.controller.rest.GeoIpController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mike on 21.12.16.
 */
@Configuration
@EnableWebMvc
public class GeoIP {
    private static RequestPostProcessor remoteAddr(final String remoteAddr) {
        return new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setRemoteAddr(remoteAddr);
                return request;
            }
        };
    }
    private MockMvc mvc;

    @InjectMocks
    private GeoIpController controller;

    @Mock
    private GeoIpResolutionService resolutionService;

    @Mock
    private CountryFlagPictureService pictureService;

    private GeoIpResolutionServiceImpl impl;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(controller).build();
        initResolutionService();
    }

    private void initResolutionService() {
        if (impl == null){
            impl = new GeoIpResolutionServiceImpl();
            impl.loadCache();
        }
    }

    @Test
    public void testFlags() throws Exception {
        when(pictureService.getPNG(anyString())).thenReturn(impl.getPNG("RU"));
        this.mvc.perform(
                get("/api/geoip/flag/RU")
        )
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.IMAGE_PNG_VALUE))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Test
    public void testLocate() throws Exception {
        when(resolutionService.resolve(anyString())).thenReturn(impl.resolve("212.164.234.91"));
        this.mvc.perform(
                get("/api/geoip/resolve")
                .with(remoteAddr("212.164.234.91"))
        )
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }
}
