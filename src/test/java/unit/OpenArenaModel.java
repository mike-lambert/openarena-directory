package unit;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;
import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class OpenArenaModel {
    @Test
    public void sameServerRecords() throws Exception{
        System.out.println("Server records equality");
        OpenArenaServerRecord rec1 = new OpenArenaServerRecord("cyberspacelabs.ru:27960");
        OpenArenaServerRecord rec2 = new OpenArenaServerRecord("cyberspacelabs.ru:27960");
        System.out.println(rec1);
        System.out.println(rec2);
        System.out.println("________________________________________________________");
        Assert.assertEquals(rec1, rec2);
    }

    @Test
    public void differentServerRecords() throws Exception{
        System.out.println("Server records inequality");
        OpenArenaServerRecord rec1 = new OpenArenaServerRecord("cyberspacelabs.ru:27960");
        OpenArenaServerRecord rec2 = new OpenArenaServerRecord("cyberspacelabs.ru:27961");
        System.out.println(rec1);
        System.out.println(rec2);
        System.out.println("________________________________________________________");
        Assert.assertNotEquals(rec1, rec2);
    }

    @Test
    public void defaultServerRecordIsInvalid() throws Exception{
        System.out.println("Server record invalidity by default");
        OpenArenaServerRecord rec1 = new OpenArenaServerRecord("cyberspacelabs.ru:27960");
        System.out.println(rec1);
        System.out.println("________________________________________________________");
        Assert.assertEquals(false, rec1.isValid());
    }

    @Test
    public void serverRecordPopulatedAndValid() throws Exception{
        System.out.println("Server record valid");
        OpenArenaServerRecord rec1 = new OpenArenaServerRecord("cyberspacelabs.ru:27960");
        rec1.setStatus(OpenArenaServerRecord.ServerStatus.UP);
        rec1.setDisplayName("Cyberspace Labs");
        rec1.setMap("q3dm1");
        rec1.setPing(32);
        rec1.setPlayersPresent(0);
        rec1.setSlotsAvailable(8);
        rec1.setGameType("baseoa");
        System.out.println(rec1);
        System.out.println("________________________________________________________");
        Assert.assertEquals(true, rec1.isValid());
    }

    @Test
    public void discoveryRecordEquals() {
        System.out.println("Discovery records equality");
        OpenArenaDiscoveryRecord rec1 = new OpenArenaDiscoveryRecord("cyberspacelabs.ru:27960");
        rec1.setLastQueried(System.currentTimeMillis());
        OpenArenaDiscoveryRecord rec2 = new OpenArenaDiscoveryRecord("cyberspacelabs.ru", 27960);
        rec2.setLastQueried(System.currentTimeMillis());
        System.out.println(rec1);
        System.out.println(rec2);
        System.out.println("________________________________________________________");
        Assert.assertEquals(rec1, rec2);
    }

    @Test
    public void discoveryRecordDiffers() {
        System.out.println("Discovery records inequality");
        OpenArenaDiscoveryRecord rec1 = new OpenArenaDiscoveryRecord("cyberspacelabs.ru:27960");
        rec1.setLastQueried(System.currentTimeMillis());
        OpenArenaDiscoveryRecord rec2 = new OpenArenaDiscoveryRecord("cyberspacelabs.ru", 27961);
        rec2.setLastQueried(System.currentTimeMillis());
        System.out.println(rec1);
        System.out.println(rec2);
        System.out.println("________________________________________________________");
        Assert.assertNotEquals(rec1, rec2);
    }

    @Test
    public void discoverySerialization() throws Exception {
        System.out.println("Discovery records serdes");
        ObjectMapper json = new ObjectMapper();
        json.enable(SerializationFeature.INDENT_OUTPUT);

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

        OpenArenaServerRecord record2 = new OpenArenaServerRecord("cyberspacelabs.ru:27961");
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

        String out = json.writeValueAsString(discovery);
        System.out.println(out);
        System.out.println("________________________________________________________");
        OpenArenaDiscoveryRecord deser = json.readValue(out, OpenArenaDiscoveryRecord.class);
        Assert.assertNotNull(deser);
        Assert.assertEquals(2, deser.getRecords().size());
        Assert.assertEquals(discovery, deser);
        Iterator<OpenArenaServerRecord> dri = deser.getRecords().iterator();
        Assert.assertEquals(record1, dri.next());
        Assert.assertEquals(record2, dri.next());
    }
}
