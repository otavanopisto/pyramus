package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunniste;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnisteOPS2015;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnistePV2017;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnistePaikallinen;

public class APOKurssinTunnisteDeserializer extends JsonDeserializer<AikuistenPerusopetuksenKurssinTunniste> {

  @Override
  public AikuistenPerusopetuksenKurssinTunniste deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    if (tree.get("tunniste").has("koodistoUri")) {
      String koodistoUri = tree.get("tunniste").get("koodistoUri").asText();

      if (StringUtils.equals(koodistoUri, "aikuistenperusopetuksenkurssit2015")) {
        return codec.treeToValue(tree, AikuistenPerusopetuksenKurssinTunnisteOPS2015.class);
      } 
      else if (StringUtils.equals(koodistoUri, "aikuistenperusopetuksenpaattovaiheenkurssit2017")) {
        return codec.treeToValue(tree, AikuistenPerusopetuksenKurssinTunnistePV2017.class);
      }
      else {
        return codec.treeToValue(tree, AikuistenPerusopetuksenKurssinTunnistePaikallinen.class);
      }
    } else {
      return codec.treeToValue(tree, AikuistenPerusopetuksenKurssinTunnistePaikallinen.class);
    }
  }

}
