package fi.otavanopisto.pyramus.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import fi.otavanopisto.pyramus.rest.util.PyramusDateDeserializer;
import fi.otavanopisto.pyramus.rest.util.PyramusDateSerializer;

@Provider
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {

  @Override
  public ObjectMapper getContext(Class<?> type) {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(OffsetDateTime.class, new PyramusDateDeserializer(OffsetDateTime.class));
    module.addSerializer(new PyramusDateSerializer(OffsetDateTime.class));
    objectMapper.registerModule(module);
    return objectMapper;
  }

}
