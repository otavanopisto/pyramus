package fi.otavanopisto.pyramus.ytl;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.module.jsv.JsonSchemaValidator;

public class YTLJSONTestsIT {

  @Test
  public void testOldTestCase() throws IOException {
    assertYTL(YTLTestCaseData.getOldTestCase());
  }

  @Test
  public void test2022TestCase() throws IOException {
    assertYTL(YTLTestCaseData.get2022TestCase());
  }

  @Test
  public void testCombinedTestCase() throws IOException {
    assertYTL(YTLTestCaseData.getCombinedTestCase());
  }
  
  protected void assertYTL(YTLSiirtotiedosto ytl) throws JsonGenerationException, JsonMappingException, IOException {
    String oppijaStr = ytlToString(ytl);
    
    assertThat(oppijaStr, getSchemaValidator());
  }
  
  protected String ytlToString(YTLSiirtotiedosto ytl) throws JsonGenerationException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    StringWriter writer = new StringWriter();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    mapper.writeValue(writer, ytl);
    
    return writer.toString();
  }
  
  protected JsonSchemaValidator getSchemaValidator() throws IOException {
    InputStream resource = getClass().getResourceAsStream("ytl-json-schema.json");
    
    return matchesJsonSchema(new InputStreamReader(resource, "UTF-8"));
  }

}
