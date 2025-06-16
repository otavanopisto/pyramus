package fi.otavanopisto.pyramus.domainmodel.users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;

import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.security.ContextReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Inheritance(strategy=InheritanceType.JOINED)
public class User implements fi.otavanopisto.security.User, ContextReference {

  public Long getId() {
    return id;
  }
  
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  @Transient
  public Long getPersonId() {
    return person == null ? null : person.getId();
  }

  @Transient  
  public Email getPrimaryEmail() {
    for (Email email : getContactInfo().getEmails()) {
      if (email.getDefaultAddress())
        return email;
    }
    return null;
  }
  
  @Transient  
  public PhoneNumber getPrimaryPhoneNumber() {
    for (PhoneNumber phoneNumber : getContactInfo().getPhoneNumbers()) {
      if (phoneNumber.getDefaultNumber())
        return phoneNumber;
    }
    return null;
  }
  
  @Transient
  @FullTextField (projectable = Projectable.NO)
  @IndexingDependency(derivedFrom = {
      @ObjectPath({ @PropertyValue(propertyName = "firstName") }),
      @ObjectPath({ @PropertyValue(propertyName = "lastName") })
  })
  public String getFullName() {
    return getFirstName() + ' ' + getLastName();
  }
  
  public Set<Tag> getTags() {
    return tags;
  }
  
  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }
  
  public void addTag(Tag tag) {
    if (!tags.contains(tag)) {
      tags.add(tag);
    } else {
      throw new PersistenceException("Entity already has this tag");
    }
  }
  
  public void removeTag(Tag tag) {
    if (tags.contains(tag)) {
      tags.remove(tag);
    } else {
      throw new PersistenceException("Entity does not have this tag");
    }
  }

  public void setContactInfo(ContactInfo contactInfo) {
    this.contactInfo = contactInfo;
  }
  public ContactInfo getContactInfo() {
    return contactInfo;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Transient
  @IndexedEmbedded(includeEmbeddedObjectId = true)
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO)
  public Organization getOrganization() {
    return null;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public Boolean getArchived() {
    return archived;
  }
  
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  @Transient
  @GenericField
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO)
  public Set<Role> getRoles() {
    return Collections.emptySet();
  }
  
  @Transient
  public final boolean hasRole(Role role) {
    Set<Role> roles = getRoles();
    return roles != null ? roles.contains(role) : false;
  }
  
  @Transient
  public final boolean hasAnyRole(Role ... roles) {
    for (Role role : roles) {
      if (hasRole(role)) {
        return true;
      }
    }
    
    return false;
  }
  
  @Transient
  public boolean isAccountEnabled() {
    return false;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="User")  
  @TableGenerator(name="User", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id; 
  
  @ManyToOne
  @IndexedEmbedded(includeEmbeddedObjectId = true, includeDepth = 1)
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO)
  private Person person;
  
  @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="contactInfo")
  @IndexedEmbedded
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private ContactInfo contactInfo;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @FullTextField
  @KeywordField (name = "firstName_sort", projectable = Projectable.NO, sortable = Sortable.YES)
  private String firstName;
  
  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @FullTextField
  @KeywordField (name = "lastName_sort", projectable = Projectable.NO, sortable = Sortable.YES)
  private String lastName;

  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name="__UserTags", joinColumns=@JoinColumn(name="user"), inverseJoinColumns=@JoinColumn(name="tag"))
  @IndexedEmbedded 
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private Set<Tag> tags = new HashSet<>();
  
  @NotNull
  @Column (nullable = false)
  @GenericField
  private Boolean archived;
  
  @Version
  @Column(nullable = false)
  private Long version;

}
