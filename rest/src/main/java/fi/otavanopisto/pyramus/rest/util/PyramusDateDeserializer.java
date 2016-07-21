package fi.otavanopisto.pyramus.rest.util;

import java.io.IOException;
import java.util.Date;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class PyramusDateDeserializer extends StdDeserializer<ZonedDateTime> {

  private static final long serialVersionUID = -4746289317645203051L;

  public PyramusDateDeserializer(Class<ZonedDateTime> t) {
    super(t);
  }

  @Override
  public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext context)
      throws IOException, JsonProcessingException {
    return ZonedDateTime.parse(jsonParser.getText());
  }

}
