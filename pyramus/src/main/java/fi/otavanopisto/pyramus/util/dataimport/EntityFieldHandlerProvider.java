package fi.otavanopisto.pyramus.util.dataimport;

import java.util.HashMap;
import java.util.Map;

public class EntityFieldHandlerProvider {
  
  public void registerFieldHandler(String fieldName, FieldHandlingStrategy strategy) {
    this.fieldHandlers.put(fieldName, strategy);
  }
  
  public FieldHandlingStrategy getFieldHandlingStrategy(String fieldName) {
    return fieldHandlers.get(fieldName);
  }
  
  private Map<String, FieldHandlingStrategy> fieldHandlers = new HashMap<String, FieldHandlingStrategy>();  
}