package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusEiTiedossa;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusMuu;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenTunniste;

public class APOOppiaineenTunnisteDeserializer extends JsonDeserializer<AikuistenPerusopetuksenOppiaineenTunniste> {

  @Override
  public AikuistenPerusopetuksenOppiaineenTunniste deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    if (tree.get("tunniste").has("koodistoUri")) {
      String koodistoUri = tree.get("tunniste").get("koodistoUri").asText();
      
      if (StringUtils.equals(koodistoUri, "koskioppiaineetyleissivistava")) {
        // National subject
        String subjectStr = tree.get("tunniste").get("koodiarvo").asText();
        
        KoskiOppiaineetYleissivistava subject = KoskiOppiaineetYleissivistava.valueOf(subjectStr);
        
        switch (subject) {
          case XX:
            return codec.treeToValue(tree, AikuistenPerusopetuksenOppiaineenSuoritusEiTiedossa.class);
          case AI:
            return codec.treeToValue(tree, AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli.class);
          case A1:
          case A2:
          case B1:
          case B2:
          case B3:
            return codec.treeToValue(tree, AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli.class);
          default:
            return codec.treeToValue(tree, AikuistenPerusopetuksenOppiaineenSuoritusMuu.class);
        }
      } else {
        // Local subject
        return codec.treeToValue(tree, AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen.class);
      }
    } else {
      // Local subject (koodistoUri not mandatory)
      return codec.treeToValue(tree, AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen.class);
    }
  }

}
