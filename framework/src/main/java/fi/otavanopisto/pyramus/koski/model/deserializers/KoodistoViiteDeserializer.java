package fi.otavanopisto.pyramus.koski.model.deserializers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiKoodisto;

public class KoodistoViiteDeserializer extends JsonDeserializer<KoodistoViite<?>> {

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public KoodistoViite<?> deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
    ObjectCodec codec = parser.getCodec();
    JsonNode tree = codec.readTree(parser);
    
    String koodistoUri = tree.get("koodistoUri").asText();
    String koodiarvo = tree.get("koodiarvo").asText();

    if (StringUtils.isNotBlank(koodistoUri) && StringUtils.isNotBlank(koodiarvo)) {
      Class enumClass = KoskiKoodisto.getEnum(koodistoUri);
      if (enumClass != null) {
        Enum e = reverseLookup(enumClass, koodiarvo);
        if (e != null) {
          return new KoodistoViite(e);
        }
        else {
          if (EnumUtils.isValidEnum(enumClass, koodiarvo)) {
            e = EnumUtils.getEnum(enumClass, koodiarvo);
            return new KoodistoViite(e);
          }
        }
      }
    }
    
    return null;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private Enum reverseLookup(Class enumClass, String enumValue) {
    try {
      Method reverseLookupMethod = enumClass.getMethod("reverseLookup", String.class);

      if (reverseLookupMethod != null) {
        Enum e = (Enum) reverseLookupMethod.invoke(null, enumValue);
        return e;
      }
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
    }
    
    return null;
  }
  
}
