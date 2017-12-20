package fi.otavanopisto.pyramus.koski;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class KoskiJSONValidatorTestsIT extends AbstractKoskiTest {

  @Test
  public void testSchemaMatching() throws IOException {
    URL testFile = this.getClass().getResource("test-student.json");
    String testJSON = IOUtils.toString(testFile, "UTF-8");
    
    assertThat(testJSON, getSchemaValidator());
  }

}
