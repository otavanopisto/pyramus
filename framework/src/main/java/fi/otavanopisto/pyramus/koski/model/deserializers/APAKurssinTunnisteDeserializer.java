package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.model.apa.APAKurssinTunniste;
import fi.otavanopisto.pyramus.koski.model.apa.APAKurssinTunnisteOPS2017;
import fi.otavanopisto.pyramus.koski.model.apa.APAKurssinTunnistePaikallinen;

public class APAKurssinTunnisteDeserializer extends JsonDeserializer<APAKurssinTunniste> {

  @Override
  public APAKurssinTunniste deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    if (tree.get("tunniste").has("koodistoUri")) {
      String koodistoUri = tree.get("tunniste").get("koodistoUri").asText();

      if (StringUtils.equals(koodistoUri, "aikuistenperusopetuksenalkuvaiheenkurssit2017")) {
        return codec.treeToValue(tree, APAKurssinTunnisteOPS2017.class);
      } 
      else {
        return codec.treeToValue(tree, APAKurssinTunnistePaikallinen.class);
      }
    } else {
      return codec.treeToValue(tree, APAKurssinTunnistePaikallinen.class);
    }
  }

}
