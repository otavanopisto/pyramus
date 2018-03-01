package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;

import org.apache.commons.lang3.EnumUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;

public class OpiskeluoikeusDeserializer extends JsonDeserializer<Opiskeluoikeus> {

  @Override
  public Opiskeluoikeus deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    String tyyppi = tree.get("tyyppi").get("koodiarvo").asText();
    if (EnumUtils.isValidEnum(OpiskeluoikeudenTyyppi.class, tyyppi)) {
      OpiskeluoikeudenTyyppi tyyppiEnum = OpiskeluoikeudenTyyppi.valueOf(tyyppi);
      
      switch (tyyppiEnum) {
        case aikuistenperusopetus:
          return codec.treeToValue(tree, AikuistenPerusopetuksenOpiskeluoikeus.class);
        case lukiokoulutus:
          return codec.treeToValue(tree, LukionOpiskeluoikeus.class);
          
        default:
          return null;
      }
    }
    
    return null;
  }

}
