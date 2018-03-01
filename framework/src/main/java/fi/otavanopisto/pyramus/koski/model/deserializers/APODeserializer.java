package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;

import org.apache.commons.lang3.EnumUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionSuoritus;

public class APODeserializer extends JsonDeserializer<LukionSuoritus> {

  @Override
  public LukionSuoritus deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    String tyyppi = tree.get("tyyppi").get("koodiarvo").asText();
    if (EnumUtils.isValidEnum(SuorituksenTyyppi.class, tyyppi)) {
      SuorituksenTyyppi tyyppiEnum = SuorituksenTyyppi.valueOf(tyyppi);
      
      switch (tyyppiEnum) {
        case lukionoppiaineenoppimaara:
          return codec.treeToValue(tree, LukionOppiaineenOppimaaranSuoritus.class);
        case lukionoppimaara:
          return codec.treeToValue(tree, LukionOppimaaranSuoritus.class);
          
        default:
          return null;
      }
    }
    
    return null;
  }

}
