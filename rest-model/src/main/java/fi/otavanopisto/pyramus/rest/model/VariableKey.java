package fi.otavanopisto.pyramus.rest.model;

public class VariableKey {

  public VariableKey() {
    super();
  }

  public VariableKey(String key, String name, Boolean userEditable, VariableType type) {
    this();
    this.key = key;
    this.name = name;
    this.userEditable = userEditable;
    this.type = type;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getUserEditable() {
    return userEditable;
  }

  public void setUserEditable(Boolean userEditable) {
    this.userEditable = userEditable;
  }
  
  public VariableType getType() {
    return type;
  }
  
  public void setType(VariableType type) {
    this.type = type;
  }

  private String key;
  private String name;
  private Boolean userEditable;
  private VariableType type;
}
