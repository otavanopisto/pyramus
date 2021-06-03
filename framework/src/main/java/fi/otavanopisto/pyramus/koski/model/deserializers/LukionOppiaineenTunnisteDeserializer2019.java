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
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppiaineenSuoritusAidinkieli2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppiaineenSuoritusMatematiikka2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppiaineenSuoritusMuuValtakunnallinen2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppiaineenSuoritusPaikallinen2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppiaineenSuoritusVierasKieli2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppiaineenTunniste2019;

public class LukionOppiaineenTunnisteDeserializer2019 extends JsonDeserializer<LukionOppiaineenTunniste2019> {

  @Override
  public LukionOppiaineenTunniste2019 deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    if (tree.get("tunniste").has("koodistoUri")) {
      String koodistoUri = tree.get("tunniste").get("koodistoUri").asText();
      
      if (StringUtils.equals(koodistoUri, "koskioppiaineetyleissivistava")) {
        // National subject
        String subjectStr = tree.get("tunniste").get("koodiarvo").asText();
        
        KoskiOppiaineetYleissivistava subject = KoskiOppiaineetYleissivistava.valueOf(subjectStr);
        
        switch (subject) {
          case MA:
            return codec.treeToValue(tree, LukionOppiaineenSuoritusMatematiikka2019.class);
          case AI:
            return codec.treeToValue(tree, LukionOppiaineenSuoritusAidinkieli2019.class);
          case A1:
          case A2:
          case B1:
          case B2:
          case B3:
            return codec.treeToValue(tree, LukionOppiaineenSuoritusVierasKieli2019.class);
          default:
            return codec.treeToValue(tree, LukionOppiaineenSuoritusMuuValtakunnallinen2019.class);
        }
      } else {
        // Local subject
        return codec.treeToValue(tree, LukionOppiaineenSuoritusPaikallinen2019.class);
      }
    } else {
      // Local subject (koodistoUri not mandatory)
      return codec.treeToValue(tree, LukionOppiaineenSuoritusPaikallinen2019.class);
    }
  }

}
