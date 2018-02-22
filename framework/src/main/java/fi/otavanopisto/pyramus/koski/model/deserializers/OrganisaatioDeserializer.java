package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.model.Organisaatio;
import fi.otavanopisto.pyramus.koski.model.OrganisaatioOID;

public class OrganisaatioDeserializer extends JsonDeserializer<Organisaatio> {

  @Override
  public Organisaatio deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    return codec.treeToValue(tree, OrganisaatioOID.class);
  }

}
