package fi.otavanopisto.pyramus.domainmodel.users;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Transient;

@Entity
@Indexed
@PrimaryKeyJoinColumn(name="id")
public class StaffMember extends User implements ArchivableEntity {
  
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

  @Override
  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
  
  @Transient
  @Override
  public boolean isAccountEnabled() {
    return isEnabled() && CollectionUtils.isNotEmpty(getRoles());
  }

  @GenericField (name = "enabled", projectable = Projectable.NO)
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  private boolean enabled;
  
  @FullTextField
  private String title;  
  
  // TODO fix the environment to not need EAGER here
  @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
  @Enumerated (EnumType.STRING)
  @Column (name = "role")
  @CollectionTable (name = "StaffMemberRoles", joinColumns = @JoinColumn(name = "staffMember_id"))
  private Set<Role> roles;

  @ElementCollection
  @MapKeyColumn (name = "name", length = 100)
  @Column (name = "value", length = 255)
  @CollectionTable (name = "StaffMemberProperties", joinColumns = @JoinColumn(name = "staffMember_id"))
  private Map<String, String> properties = new HashMap<String, String>();

  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name = "StaffMemberStudyProgrammes", joinColumns = @JoinColumn(name = "staffMember"), inverseJoinColumns = @JoinColumn(name = "studyProgramme"))
  private Set<StudyProgramme> studyProgrammes = new HashSet<>();

}
