package fi.otavanopisto.pyramus.views.settings;

import java.util.ArrayList;
import java.util.List;

public class ManageChangeLogViewEntityBean {

  public ManageChangeLogViewEntityBean(String name, String displayName, List<ManageChangeLogViewEntityPropertyBean> properties) {
    this.name = name;
    this.properties = properties;
    this.displayName = displayName;
  }
  
  public String getName() {
    return name;
  }
  
  public String getDisplayName() {
    return displayName;
  }
  
  public List<ManageChangeLogViewEntityPropertyBean> getProperties() {
    return properties;
  }
  
  private String displayName;
  private String name;
  private List<ManageChangeLogViewEntityPropertyBean> properties = new ArrayList<ManageChangeLogViewEntityPropertyBean>();
}
