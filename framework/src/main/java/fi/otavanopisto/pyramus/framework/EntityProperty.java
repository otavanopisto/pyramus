package fi.otavanopisto.pyramus.framework;

import fi.otavanopisto.pyramus.domainmodel.base.VariableType;

public class EntityProperty {
  
  public EntityProperty(String key, VariableType type, String localeKey) {
    this.key = key;
    this.type = type;
    this.localeKey = localeKey;
  }
  
  public String getKey() {
    return key;
  }
  
  public void setKey(String key) {
    this.key = key;
  }
  
  public String getLocaleKey() {
    return localeKey;
  }
  
  public void setLocaleKey(String localeKey) {
    this.localeKey = localeKey;
  }

  public VariableType getType() {
    return type;
  }

  public void setType(VariableType type) {
    this.type = type;
  }

  private String key;
  private String localeKey;
  private VariableType type;
}
