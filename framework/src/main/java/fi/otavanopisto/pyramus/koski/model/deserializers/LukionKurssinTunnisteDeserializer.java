package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunniste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnistePaikallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnisteValtakunnallinenOPS2004;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnisteValtakunnallinenOPS2015;

public class LukionKurssinTunnisteDeserializer extends JsonDeserializer<LukionKurssinTunniste> {

  @Override
  public LukionKurssinTunniste deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    if (tree.get("tunniste").has("koodistoUri")) {
      String koodistoUri = tree.get("tunniste").get("koodistoUri").asText();

      if (StringUtils.equals(koodistoUri, "lukionkurssit")) {
        return codec.treeToValue(tree, LukionKurssinTunnisteValtakunnallinenOPS2015.class);
      } 
      else if (StringUtils.equals(koodistoUri, "lukionkurssitops2004aikuiset")) {
        return codec.treeToValue(tree, LukionKurssinTunnisteValtakunnallinenOPS2004.class);
      }
      else {
        return codec.treeToValue(tree, LukionKurssinTunnistePaikallinen.class);
      }
    } else {
      return codec.treeToValue(tree, LukionKurssinTunnistePaikallinen.class);
    }
  }

}
