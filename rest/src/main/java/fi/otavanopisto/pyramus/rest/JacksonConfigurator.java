package fi.otavanopisto.pyramus.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.threeten.bp.ZonedDateTime;

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
    module.addDeserializer(ZonedDateTime.class, new PyramusDateDeserializer(ZonedDateTime.class));
    module.addSerializer(new PyramusDateSerializer(ZonedDateTime.class));
    objectMapper.registerModule(module);
    return objectMapper;
  }

}
