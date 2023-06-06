package fi.otavanopisto.pyramus.domainmodel.base;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceException;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;

import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.persistence.search.filters.ArchivedEntityFilterFactory;

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@FullTextFilterDefs (
  @FullTextFilterDef (
     name="ArchivedSchool",
     impl=ArchivedEntityFilterFactory.class
  )
)
public class School implements ArchivableEntity {
  
  /**
   * Returns internal unique id.
   * 
   * @return Internal unique id
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets user friendly name for this school.
   * 
   * @param name User friendly name for this school
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns user friendly name of this school.
   * 
   * @return User friendly name of this school
   */
  public String getName() {
    return name;
  }

  @Transient
  @Field(analyze = Analyze.NO, store = Store.NO)
  @SortableField
  public String getNameSortable() {
    return name;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setContactInfo(ContactInfo contactInfo) {
    this.contactInfo = contactInfo;
  }

  public ContactInfo getContactInfo() {
    return contactInfo;
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

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  /**
   * Sets the field of this school
   * 
   * @param field new field
   */
  public void setField(SchoolField field) {
    this.field = field;
  }

  /**
   * Returns the field of this school
   * 
   * @return field
   */
  public SchoolField getField() {
    return field;
  }

  public BillingDetails getBillingDetails() {
    return billingDetails;
  }

  public void setBillingDetails(BillingDetails billingDetails) {
    this.billingDetails = billingDetails;
  }

  public StudentGroup getStudentGroup() {
    return studentGroup;
  }

  public void setStudentGroup(StudentGroup studentGroup) {
    this.studentGroup = studentGroup;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="School")  
  @TableGenerator(name="School", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;
  
  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @Field
  private String code;
  
  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @Field
  private String name;
  
  @ManyToOne
  @JoinColumn (name = "studentGroup")
  private StudentGroup studentGroup;
  
  @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="contactInfo")
  @IndexedEmbedded
  private ContactInfo contactInfo;

  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;

  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name="__SchoolTags", joinColumns=@JoinColumn(name="school"), inverseJoinColumns=@JoinColumn(name="tag"))
  @IndexedEmbedded
  private Set<Tag> tags = new HashSet<>();

  @ManyToOne
  @JoinColumn (name = "field")
  private SchoolField field;
  
  @ManyToOne
  @JoinColumn (name = "billingDetails")
  private BillingDetails billingDetails;
  
  @Version
  @Column(nullable = false)
  private Long version;
}