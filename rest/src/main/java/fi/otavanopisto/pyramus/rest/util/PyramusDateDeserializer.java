package fi.otavanopisto.pyramus.rest.util;

import java.io.IOException;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class PyramusDateDeserializer extends StdDeserializer<OffsetDateTime> {

  private static final long serialVersionUID = -4746289317645203051L;

  public PyramusDateDeserializer(Class<OffsetDateTime> t) {
    super(t);
  }

  @Override
  public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext context)
      throws IOException, JsonProcessingException {
    return OffsetDateTime.parse(jsonParser.getText());
  }

}
