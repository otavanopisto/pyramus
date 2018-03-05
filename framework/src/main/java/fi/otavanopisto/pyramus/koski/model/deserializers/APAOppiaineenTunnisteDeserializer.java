package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenAlkuvaiheenOppiaineet;
import fi.otavanopisto.pyramus.koski.model.apa.APAOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.model.apa.APAOppiaineenTunnisteAidinkieli;
import fi.otavanopisto.pyramus.koski.model.apa.APAOppiaineenTunnisteMuu;
import fi.otavanopisto.pyramus.koski.model.apa.APAOppiaineenTunnistePaikallinen;
import fi.otavanopisto.pyramus.koski.model.apa.APAOppiaineenTunnisteVierasKieli;

public class APAOppiaineenTunnisteDeserializer extends JsonDeserializer<APAOppiaineenTunniste> {

  @Override
  public APAOppiaineenTunniste deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    if (tree.get("tunniste").has("koodistoUri")) {
      String koodistoUri = tree.get("tunniste").get("koodistoUri").asText();
      
      if (StringUtils.equals(koodistoUri, "aikuistenperusopetuksenalkuvaiheenoppiaineet")) {
        // National subject
        String subjectStr = tree.get("tunniste").get("koodiarvo").asText();
        
        AikuistenPerusopetuksenAlkuvaiheenOppiaineet subject = AikuistenPerusopetuksenAlkuvaiheenOppiaineet.valueOf(subjectStr);
        
        switch (subject) {
          case AI:
            return codec.treeToValue(tree, APAOppiaineenTunnisteAidinkieli.class);
          case A1:
            return codec.treeToValue(tree, APAOppiaineenTunnisteVierasKieli.class);
          default:
            return codec.treeToValue(tree, APAOppiaineenTunnisteMuu.class);
        }
      } else {
        // Local subject
        return codec.treeToValue(tree, APAOppiaineenTunnistePaikallinen.class);
      }
    } else {
      // Local subject (koodistoUri not mandatory)
      return codec.treeToValue(tree, APAOppiaineenTunnistePaikallinen.class);
    }
  }

}
