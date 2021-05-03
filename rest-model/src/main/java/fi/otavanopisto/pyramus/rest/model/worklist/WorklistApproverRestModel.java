package fi.otavanopisto.pyramus.rest.model.worklist;

public class WorklistApproverRestModel {

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  private String name;
  private String email;

}
