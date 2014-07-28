package fi.pyramus.plugin;

public class DynamicAttribute {

  public DynamicAttribute(String name, Object value2, String uri) {
    this.name = name;
    this.value = value2;
    this.uri = uri;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Object getValue() {
    return value;
  }
  
  public void setValue(Object value) {
    this.value = value;
  }
  
  public String getUri() {
    return uri;
  }
  
  public void setUri(String uri) {
    this.uri = uri;
  }

  public long valueAsLong() {
    return (Long) value;
  }
  
  private String name;
  private Object value;
  private String uri;
}
