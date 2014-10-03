package fi.pyramus.domainmodel.users;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;

import fi.pyramus.domainmodel.base.BillingDetails;
import fi.pyramus.domainmodel.base.ContactInfo;
import fi.pyramus.domainmodel.base.Tag;

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Inheritance(strategy=InheritanceType.JOINED)
public class User {

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
  @Field(analyze = Analyze.NO, store = Store.YES)
  public String getFirstNameSortable() {
    return getFirstName();
  }

  @Transient
  @Field(analyze = Analyze.NO, store = Store.YES)
  public String getLastNameSortable() {
    return getLastName();
  }

  public String getAuthProvider() {
    return authProvider;
  }
  
  public void setAuthProvider(String authProvider) {
    this.authProvider = authProvider;
  }
  
  public String getExternalId() {
    return externalId;
  }
  
  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }
  
  @Transient 
  public String getFullName() {
    return getFirstName() + ' ' + getLastName();
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Role getRole() {
    return role;
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

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setBillingDetails(List<BillingDetails> billingDetails) {
    this.billingDetails = billingDetails;
  }

  public List<BillingDetails> getBillingDetails() {
    return billingDetails;
  }

  public void addBillingDetails(BillingDetails billingDetails) {
    if (!this.billingDetails.contains(billingDetails)) {
      this.billingDetails.add(billingDetails);
    } else {
      throw new PersistenceException("Entity already has this BillingDetails");
    }
  }
  
  public void removeBillingDetails(BillingDetails billingDetails) {
    if (this.billingDetails.contains(billingDetails)) {
      this.billingDetails.remove(billingDetails);
    } else {
      throw new PersistenceException("Entity does not have this BillingDetails");
    }
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="User")  
  @TableGenerator(name="User", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;
  
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

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String externalId;
  
  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String authProvider;  

  private String title;  
  
  @NotNull
  @Column (nullable = false)
  @Enumerated (EnumType.STRING)
  @Field (store = Store.NO)
  // TODO Some way to disallow Role.EVERYONE
  private Role role;

  @ManyToMany (fetch = FetchType.LAZY)
  @JoinTable (name="__UserBillingDetails", joinColumns=@JoinColumn(name="user"), inverseJoinColumns=@JoinColumn(name="billingDetails"))
  @IndexedEmbedded 
  private List<BillingDetails> billingDetails = new ArrayList<BillingDetails>();

  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name="__UserTags", joinColumns=@JoinColumn(name="user"), inverseJoinColumns=@JoinColumn(name="tag"))
  @IndexedEmbedded 
  private Set<Tag> tags = new HashSet<Tag>();
  
  @Version
  @Column(nullable = false)
  private Long version;
}
