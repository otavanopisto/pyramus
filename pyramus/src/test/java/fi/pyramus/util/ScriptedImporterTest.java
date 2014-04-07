package fi.pyramus.util;

import static org.junit.Assert.*;

import java.io.StringReader;

import javax.script.ScriptException;

import org.junit.Test;

import fi.pyramus.util.dataimport.scripting.InvalidScriptException;
import fi.pyramus.util.dataimport.scripting.ScriptedImporter;

public class ScriptedImporterTest {

  @Test
  public void testApiLog() throws InvalidScriptException {
    ScriptedImporter si = new ScriptedImporter();
    si.importDataFromReader(
        new StringReader
        (
            "({" +
            "  'run': function(api) {" +
            "     api.log(api.faker.name());" +
            "     api.log(api.faker.streetAddress(false));" +
            "  }" +
            "})"
    ));
  }

}
