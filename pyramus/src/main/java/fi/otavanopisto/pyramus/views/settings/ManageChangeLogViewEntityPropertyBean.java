package fi.otavanopisto.pyramus.views.settings;

public class ManageChangeLogViewEntityPropertyBean {
  
  public ManageChangeLogViewEntityPropertyBean(String name, String displayName, Boolean track) {
    this.name = name;
    this.track = track;
    this.displayName = displayName;
  }

  public Boolean getTrack() {
    return track;
  }
  
  public String getName() {
    return name;
  }
  
  public String getDisplayName() {
    return displayName;
  }
  
  private String displayName;
  private String name;
  private Boolean track;
}
