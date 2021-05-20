package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOpintojaksonTunniste2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOpintojaksonTunnisteMuuModuuli2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOpintojaksonTunnistePaikallinen2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOpintojaksonTunnisteVierasKieli2019;

public class LukionKurssinTunnisteDeserializer2019 extends JsonDeserializer<LukionOpintojaksonTunniste2019> {

  @Override
  public LukionOpintojaksonTunniste2019 deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    if (tree.get("tunniste").has("koodistoUri")) {
      String koodistoUri = tree.at("/tunniste/koodistoUri").asText();
      
      if (tree.has("kieli")) {
        // Paras arvaus, jos kieli-kenttä löytyy, on kyseessä vieras kieli
        return codec.treeToValue(tree, LukionOpintojaksonTunnisteVierasKieli2019.class);
      } 
      else if (StringUtils.equals(koodistoUri, "moduulikoodistolops2021")) {
        return codec.treeToValue(tree, LukionOpintojaksonTunnisteMuuModuuli2019.class);
      } 
      else {
        return codec.treeToValue(tree, LukionOpintojaksonTunnistePaikallinen2019.class);
      }
    } else {
      return codec.treeToValue(tree, LukionOpintojaksonTunnistePaikallinen2019.class);
    }
  }

}
