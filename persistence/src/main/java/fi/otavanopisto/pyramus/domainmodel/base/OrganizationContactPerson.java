package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;

@Entity
public class OrganizationContactPerson {
  
  public Long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  public OrganizationContactPersonType getType() {
    return type;
  }

  public void setType(OrganizationContactPersonType type) {
    this.type = type;
  }

  @Id 
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne (optional = false)
  @JoinColumn (name = "organization")
  private Organization organization;
  
  @Column
  @Enumerated (EnumType.STRING)
  private OrganizationContactPersonType type;
  
  @Column
  private String name;

  @Column
  @Email
  private String email;

  @Column
  private String phone;

  @Column
  private String title;
}