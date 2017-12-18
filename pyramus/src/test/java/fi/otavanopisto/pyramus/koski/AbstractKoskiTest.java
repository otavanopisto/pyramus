package fi.otavanopisto.pyramus.koski;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.koski.model.Oppija;
import io.restassured.module.jsv.JsonSchemaValidator;

public abstract class AbstractKoskiTest {

  public void assertOppija(Oppija oppija) throws JsonGenerationException, JsonMappingException, IOException {
    String oppijaStr = oppijaToString(oppija);
    
    System.out.println("GENEROITU: " + oppijaStr);
    
    assertThat(oppijaStr, getSchemaValidator());
  }
  
  protected String oppijaToString(Oppija oppija) throws JsonGenerationException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    StringWriter writer = new StringWriter();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    mapper.writeValue(writer, oppija);
    
    return writer.toString();
  }
  
  protected JsonSchemaValidator getSchemaValidator() throws IOException {
    InputStream resource = AbstractKoskiTest.class.getResourceAsStream("koski-oppija-schema.json");
    
//    return matchesJsonSchema(new InputStreamReader(resource, Charset.defaultCharset()));
    
    String s = IOUtils.toString(resource, "UTF-8");
    
    System.out.println("SCHEMA: " + s);
    
    return matchesJsonSchema(s);
  }
}
