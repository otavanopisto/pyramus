package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiNumeerinen;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiSanallinen;

public class KurssinArviointiDeserializer extends JsonDeserializer<KurssinArviointi> {

  @Override
  public KurssinArviointi deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    String koodiarvo = tree.get("arvosana").get("koodiarvo").asText();
    
    ArviointiasteikkoYleissivistava arvosana = ArviointiasteikkoYleissivistava.get(koodiarvo);
    
    if (ArviointiasteikkoYleissivistava.isNumeric(arvosana)) {
      return codec.treeToValue(tree, KurssinArviointiNumeerinen.class);
    } else {
      return codec.treeToValue(tree, KurssinArviointiSanallinen.class);
    }
  }

}
