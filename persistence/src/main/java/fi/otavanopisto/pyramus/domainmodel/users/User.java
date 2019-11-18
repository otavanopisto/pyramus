package fi.otavanopisto.pyramus.domainmodel.users;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceException;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;

import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.security.ContextReference;

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
  public Email getPrimaryEmail() {
    for (Email email : getContactInfo().getEmails()) {
      if (email.getDefaultAddress())
        return email;
    }
    return null;
  }
  
  @Transient
  @Field(analyze = Analyze.NO, store = Store.NO)
  @SortableField
  public String getFirstNameSortable() {
    return getFirstName();
  }

  @Transient
  @Field(analyze = Analyze.NO, store = Store.NO)
  @SortableField
  public String getLastNameSortable() {
    return getLastName();
  }
  
  @Transient 
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
  public Role getRole() {
    return Role.EVERYONE;
  }

  @Transient
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
  
  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="User")  
  @TableGenerator(name="User", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id; 
  
  @ManyToOne
  private Person person;
  
  @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="contactInfo")
  @IndexedEmbedded
  private ContactInfo contactInfo;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @Field
  private String firstName;
  
  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @Field
  private String lastName;

  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name="__UserTags", joinColumns=@JoinColumn(name="user"), inverseJoinColumns=@JoinColumn(name="tag"))
  @IndexedEmbedded 
  private Set<Tag> tags = new HashSet<>();
  
  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived;
  
  @Version
  @Column(nullable = false)
  private Long version;
}
