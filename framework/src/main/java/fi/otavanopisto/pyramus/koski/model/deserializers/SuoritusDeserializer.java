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
import fi.otavanopisto.pyramus.koski.model.Suoritus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.PerusopetuksenOppiaineenOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.model.apa.APASuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppimaaranSuoritus;

public class SuoritusDeserializer extends JsonDeserializer<Suoritus> {

  @Override
  public Suoritus deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
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
          
        case aikuistenperusopetuksenoppimaara:
          return codec.treeToValue(tree, AikuistenPerusopetuksenOppimaaranSuoritus.class);
        case aikuistenperusopetuksenoppimaaranalkuvaihe:
          return codec.treeToValue(tree, APASuoritus.class);
        case perusopetuksenoppiaineenoppimaara:
          return codec.treeToValue(tree, PerusopetuksenOppiaineenOppimaaranSuoritus.class);
          
        default:
          return null;
      }
    }
    
    return null;
  }

}
