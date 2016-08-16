package fi.otavanopisto.pyramus.rest.util;

import java.io.IOException;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class PyramusDateSerializer extends StdSerializer<OffsetDateTime> {

  private static final long serialVersionUID = -2454365657467413722L;
  
  public PyramusDateSerializer(Class<OffsetDateTime> t) {
    super(t);
  }

  @Override
  public void serialize(OffsetDateTime OffsetDateTime, JsonGenerator jsonGenerator, SerializerProvider sp) throws IOException {
    jsonGenerator.writeString(OffsetDateTime.toString());
  }

}