package unit;

import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.model.geoip.ProximityLevel;
import com.cyberspacelabs.openarena.model.geoip.ValueNode;
import com.cyberspacelabs.openarena.model.geoip.Node;
import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.service.impl.GeoIpMappingServiceImpl;
import com.cyberspacelabs.openarena.service.impl.GeoIpResolutionServiceImpl;
import com.cyberspacelabs.openarena.service.impl.QStatDiscoveryServiceFactoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class GeoIpModel {

    @Test
    public void pathsEquals() throws Exception {
        System.out.println("Paths equality");
        Path path1 = new Path("MSK-1", new Node("Russia", new Node("MSO", new Node("Moscow", new Node("127000", new ValueNode<String>("Arena-2", "212.100.110.1:27960"))))));
        Path path2 = new Path("MSK-2", new Node("Russia", new Node("MSO", new Node("Moscow", new Node("127000", new ValueNode<String>("Arena-2", "212.100.110.1:27960"))))));
        System.out.println(path1);
        System.out.println(path2);
        System.out.println("__________________________________________________________________________________________");
        Assert.assertEquals(path1, path2);
    }

    @Test
    public void pathsDiffers() throws Exception {
        System.out.println("Paths difference");
        Path path1 = new Path("!", new Node("Russia", new Node("MSO", new Node("Moscow", new Node("127000", new ValueNode<String>("Arena-2", "212.100.110.1:27960"))))));
        Path path2 = new Path("!", new Node("Russia", new Node("NSO", new Node("Novosibirsk", new Node("630058", new ValueNode<String>("Cyberspace Labs", "cyberspacelabs.ru:27960"))))));
        System.out.println(path1);
        System.out.println(path2);
        System.out.println("__________________________________________________________________________________________");
        Assert.assertNotEquals(path1, path2);
    }

    @Test
    public void leafValuesDiffers() throws Exception {
        System.out.println("Value indifference on same path");
        Path path1 = new Path("NSK1", new Node("Russia", new Node("NSO", new Node("Novosibirsk", new Node("630058", new ValueNode<String>("Cyberspace Labs", "cyberspacelabs.ru:27950"))))));
        Path path2 = new Path("NSK2", new Node("Russia", new Node("NSO", new Node("Novosibirsk", new Node("630058", new ValueNode<String>("Cyberspace Labs", "cyberspacelabs.ru:27960"))))));
        System.out.println(path1);
        System.out.println(path2);
        System.out.println("__________________________________________________________________________________________");
        Assert.assertEquals(path1, path2);
    }

    @Test
    public void leafsDiffers() throws Exception {
        System.out.println("Leafs difference");
        Path path1 = new Path("NSK", new Node("Russia", new Node("NSO", new Node("Novosibirsk", new Node("630058", new ValueNode<String>("Cyberspace Labs", "cyberspacelabs.ru:27950"))))));
        Path path2 = new Path("NSK", new Node("Russia", new Node("NSO", new Node("Novosibirsk", new Node("630058", new ValueNode<String>("Cyberspace Labs 2", "cyberspacelabs.ru:27950"))))));
        System.out.println(path1);
        System.out.println(path2);
        System.out.println("__________________________________________________________________________________________");
        Assert.assertNotEquals(path1, path2);
    }

    @Test
    public void samePathLocations() throws Exception {
        System.out.println("Paths are same but content differs");
        Path path1 = new Path("NSK", new Node("Russia", new Node("NSO", new Node("Novosibirsk", new Node("630058", new ValueNode<String>("Cyberspace Labs", "cyberspacelabs.ru:27950"))))));
        Path path2 = new Path("NSK", new Node("Russia", new Node("NSO", new Node("Novosibirsk", new Node("630058", new ValueNode<String>("Cyberspace Labs 2", "cyberspacelabs.ru:27951"))))));
        System.out.println(path1.getPath());
        System.out.println(path2.getPath());
        System.out.println("__________________________________________________________________________________________");
        Assert.assertEquals(true, path1.isSameLocation(path2));
    }

    @Test
    public void pathComponentsNotNull() throws Exception {
        System.out.println("Path components not null");
        Path path1 = new Path("NSK", new Node("Russia", new Node("NSO", new Node("Novosibirsk", new Node("630058", new ValueNode<String>("Cyberspace Labs", "cyberspacelabs.ru:27950"))))));
        System.out.println(path1);
        System.out.println("__________________________________________________________________________________________");
        Assert.assertNotNull(path1.getCountry());
        Assert.assertNotNull(path1.getRegion());
        Assert.assertNotNull(path1.getCity());
        Assert.assertNotNull(path1.getZipCode());
    }

    @Test
    public void entryEquals() throws Exception {
        System.out.println("Path value as intended");
        String value = "cyberspacelabs.ru:27950";
        Path path1 = new Path("NSK", new Node("Russia", new Node("NSO", new Node("Novosibirsk", new Node("630058", new ValueNode<String>("Cyberspace Labs", value))))));
        System.out.println(path1);
        System.out.println("__________________________________________________________________________________________");
        Assert.assertNotNull(path1.getValue(String.class));
        Assert.assertEquals(value, path1.getValue(String.class));
    }

    @Test
    public void FreeGeoIpResponseDeserialized() throws Exception {
        System.out.println("FreeGeoIP.net response deserialized");
        String json = "{\"ip\":\"82.146.35.230\",\"country_code\":\"RU\",\"country_name\":\"Russia\",\"region_code\":\"MOW\",\"region_name\":\"Moscow\",\"city\":\"Moscow\",\"zip_code\":\"101194\",\"time_zone\":\"Europe/Moscow\",\"latitude\":55.7485,\"longitude\":37.6184,\"metro_code\":0}";
        GeoIpResolutionServiceImpl.FreeGeoIpResponse result = new ObjectMapper().readValue(json, GeoIpResolutionServiceImpl.FreeGeoIpResponse.class);
        System.out.println("__________________________________________________________________________________________");
        Assert.assertNotNull(result);
    }

    @Test
    public void serverPathSerialization() throws Exception {
        System.out.println("Path with server record serdes");
        ObjectMapper json = new ObjectMapper();
        json.enable(SerializationFeature.INDENT_OUTPUT);
        OpenArenaServerRecord rec1 = new OpenArenaServerRecord("cyberspacelabs.ru:27960");
        rec1.setStatus(OpenArenaServerRecord.ServerStatus.UP);
        rec1.setDisplayName("Cyberspace Labs");
        rec1.setMap("q3dm1");
        rec1.setPing(32);
        rec1.setPlayersPresent(0);
        rec1.setSlotsAvailable(8);
        rec1.setGameType("baseoa");
        rec1.setQueryTimestamp(System.currentTimeMillis());
        Path<OpenArenaServerRecord> geo = new Path<>("Novosibirsk/Cyberspace Labs", "Russia", "Novosibirsk", "Novosibirsk", "630117", "Cyberspace Labs", rec1);
        String out = json.writeValueAsString(geo);
        System.out.println(out);
        System.out.println("__________________________________________________________________________________________");

        Path<OpenArenaServerRecord> deser = json.readValue(out, Path.class);
        Assert.assertNotNull(deser);
        Assert.assertEquals(geo, deser);
        Assert.assertEquals(rec1, deser.getValue());
    }

    @Test
    public void nearbyAll() throws Exception {
        GeoIpResolutionServiceImpl resolver = new GeoIpResolutionServiceImpl();
        resolver.loadCache();
        GeoIpMappingServiceImpl mapper = new GeoIpMappingServiceImpl(resolver);
        mapper.loadCache();
        QStatDiscoveryServiceFactoryImpl discoveryServiceFactory = new QStatDiscoveryServiceFactoryImpl();
        discoveryServiceFactory.instantiate()
            .forEach(instance -> {
                instance.getLatestDiscoveryResults().getRecords().forEach(record -> {
                    try {
                        System.out.println(mapper.locate(record));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Assert.fail();
                    }
                });
            });

        Set<OpenArenaServerRecord> records = mapper.nearby("212.164.234.91", ProximityLevel.GLOBAL);
        System.out.println("All: " + records.size());
        Assert.assertNotEquals(true, records.isEmpty());
    }

    @Test
    public void nearbyCountry() throws Exception {
        GeoIpResolutionServiceImpl resolver = new GeoIpResolutionServiceImpl();
        resolver.loadCache();
        GeoIpMappingServiceImpl mapper = new GeoIpMappingServiceImpl(resolver);
        mapper.loadCache();
        QStatDiscoveryServiceFactoryImpl discoveryServiceFactory = new QStatDiscoveryServiceFactoryImpl();
        discoveryServiceFactory.instantiate()
                .forEach(instance -> {
                    instance.getLatestDiscoveryResults().getRecords().forEach(record -> {
                        try {
                            System.out.println(mapper.locate(record));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Assert.fail();
                        }
                    });
                });

        Set<OpenArenaServerRecord> records = mapper.nearby("212.164.234.91", ProximityLevel.COUNTRY);
        System.out.println("Russia: " + records.size());
        Assert.assertNotEquals(true, records.isEmpty());
    }
}
