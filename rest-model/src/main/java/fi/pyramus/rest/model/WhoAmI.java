package fi.pyramus.rest.model;

import java.util.List;

public class WhoAmI {

  public WhoAmI() {
  }
  
  public WhoAmI(Long id, String firstName, String lastName, List<String> emails) {
    super();
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.emails = emails;
  }

  public Long getId() {
    return id;
  }
  
  public String getFirstName() {
    return firstName;
  }
  
  public String getLastName() {
    return lastName;
  }

  public List<String> getEmails() {
    return emails;
  }
  
  private Long id;
  private String firstName;
  private String lastName;
  private List<String> emails;
}
