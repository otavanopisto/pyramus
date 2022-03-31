package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppiaineenSuoritus2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOsasuoritus2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.MuidenLukioOpintojenSuoritus2019;

public class LukionOsasuoritusDeserializer2019 extends JsonDeserializer<LukionOsasuoritus2019> {

  @Override
  public LukionOsasuoritus2019 deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    String tyyppiStr = tree.get("tyyppi").get("koodiarvo").asText();

    SuorituksenTyyppi tyyppi = SuorituksenTyyppi.valueOf(tyyppiStr);
    
    switch (tyyppi) {
      case lukionoppiaine:
        return codec.treeToValue(tree, LukionOppiaineenSuoritus2019.class);
      case lukionmuuopinto:
        return codec.treeToValue(tree, MuidenLukioOpintojenSuoritus2019.class);
        
      default:
        return null;
    }
  }

}
