package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.model.Henkilo;
import fi.otavanopisto.pyramus.koski.model.HenkiloOID;
import fi.otavanopisto.pyramus.koski.model.HenkiloTiedotJaOID;

public class HenkiloDeserializer extends JsonDeserializer<Henkilo> {

  @Override
  public Henkilo deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    if (tree.has("etunimet") && tree.has("sukunimi")) {
      return codec.treeToValue(tree, HenkiloTiedotJaOID.class);
    } else {
      return codec.treeToValue(tree, HenkiloOID.class);
    }
  }

}
