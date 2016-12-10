package unit;

import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.model.qstat.Qstat;
import com.cyberspacelabs.openarena.service.QStatConversionService;
import com.cyberspacelabs.openarena.service.impl.QStatConversionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.Set;

public class QStatModel {
    @Test
    public void qstatXMLtoOpenArenaJSON() throws Exception {
        System.out.println("Transform QStat XML output to OpenArena JSON");

        QStatConversionService conversionService = new QStatConversionServiceImpl();

        ObjectMapper json = new ObjectMapper();
        json.enable(SerializationFeature.INDENT_OUTPUT);

        JAXBContext ctx = JAXBContext.newInstance(Qstat.class);
        Unmarshaller deserializer = ctx.createUnmarshaller();

        Qstat source = (Qstat) deserializer.unmarshal(this.getClass().getClassLoader().getResourceAsStream("deathmask.xml"));
        Assert.assertNotNull(source);

        Set<OpenArenaServerRecord> results = conversionService.convert(source);
        String out = json.writeValueAsString(results);
        System.out.println(out);
        System.out.println("________________________________________________________");
        Assert.assertNotNull(results);
        Assert.assertNotEquals(true, results.isEmpty());

    }
}
