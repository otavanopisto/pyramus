package fi.otavanopisto.pyramus.domainmodel.users;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@PrimaryKeyJoinColumn(name="id")
public class StaffMember extends User implements ArchivableEntity {
  
  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public Role getRole() {
    return role;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  @ManyToOne
  @JoinColumn (name = "organization")
  private Organization organization;
  
  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  public Set<StudyProgramme> getStudyProgrammes() {
    return studyProgrammes;
  }

  public void setStudyProgrammes(Set<StudyProgramme> studyProgrammes) {
    this.studyProgrammes = studyProgrammes;
  }

  private String title;  
  
  @NotNull
  @Column (nullable = false)
  @Enumerated (EnumType.STRING)
  @Field (store = Store.NO)
  // TODO Some way to disallow Role.EVERYONE
  private Role role;

  @ElementCollection
  @MapKeyColumn (name = "name", length = 100)
  @Column (name = "value", length = 255)
  @CollectionTable (name = "StaffMemberProperties", joinColumns = @JoinColumn(name = "staffMember_id"))
  private Map<String, String> properties = new HashMap<String, String>();

  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name = "StaffMemberStudyProgrammes", joinColumns = @JoinColumn(name = "staffMember"), inverseJoinColumns = @JoinColumn(name = "studyProgramme"))
  private Set<StudyProgramme> studyProgrammes = new HashSet<>();

}
