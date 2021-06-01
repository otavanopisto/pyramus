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
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusAidinkieli;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusEiTiedossa;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusMatematiikka;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusMuuValtakunnallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusPaikallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusVierasKieli;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenTunniste;

public class LukionOppiaineenTunnisteDeserializer extends JsonDeserializer<LukionOppiaineenTunniste> {

  @Override
  public LukionOppiaineenTunniste deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
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
            return codec.treeToValue(tree, LukionOppiaineenSuoritusEiTiedossa.class);
          case MA:
            return codec.treeToValue(tree, LukionOppiaineenSuoritusMatematiikka.class);
          case AI:
            return codec.treeToValue(tree, LukionOppiaineenSuoritusAidinkieli.class);
          case A1:
          case A2:
          case B1:
          case B2:
          case B3:
            return codec.treeToValue(tree, LukionOppiaineenSuoritusVierasKieli.class);
          default:
            return codec.treeToValue(tree, LukionOppiaineenSuoritusMuuValtakunnallinen.class);
        }
      } else {
        // Local subject
        return codec.treeToValue(tree, LukionOppiaineenSuoritusPaikallinen.class);
      }
    } else {
      // Local subject (koodistoUri not mandatory)
      return codec.treeToValue(tree, LukionOppiaineenSuoritusPaikallinen.class);
    }
  }

}
