package fi.otavanopisto.pyramus.domainmodel.base;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class StudyProgramme implements ArchivableEntity {

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the name of this education type.
   * 
   * @return The name of this education type
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this education type.
   * 
   * @param name The name of this education type
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * Returns the code of this education type
   * 
   * @return code of this education type
   */
  public String getCode() {
    return code;
  }
  
  /**
   * Sets the code of this education type.
   * 
   * @param name the code of this education type
   */
  public void setCode(String code) {
    this.code = code;
  }
  
  /**
   * Sets the archived flag of this object.
   * 
   * @param archived The archived flag of this object
   */
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  /**
   * Returns the archived flag of this object.
   * 
   * @return The archived flag of this object
   */
  public Boolean getArchived() {
    return archived;
  }
  
  public void setCategory(StudyProgrammeCategory category) {
    this.category = category;
  }

  public StudyProgrammeCategory getCategory() {
    return category;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  public Boolean getHasEvaluationFees() {
    return hasEvaluationFees;
  }

  public void setHasEvaluationFees(Boolean hasEvaluationFees) {
    this.hasEvaluationFees = hasEvaluationFees;
  }

  public String getOfficialEducationType() {
    return officialEducationType;
  }

  public void setOfficialEducationType(String officialEducationType) {
    this.officialEducationType = officialEducationType;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudyProgramme")  
  @TableGenerator(name="StudyProgramme", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;

  @ManyToOne
  @JoinColumn (name = "organization")
  private Organization organization;

  @NotNull
  @Column(nullable = false)
  @NotEmpty
  @FullTextField
  private String name;
  
  @KeywordField
  private String code;

  @KeywordField
  private String officialEducationType;
  
  @ManyToOne
  @JoinColumn (name = "category")
  private StudyProgrammeCategory category;

  @NotNull
  @Column(nullable = false)
  private Boolean hasEvaluationFees;

  @ElementCollection
  @MapKeyColumn (name = "name", length = 100)
  @Column (name = "value", length = 255)
  @CollectionTable (name = "StudyProgrammeProperties", joinColumns = @JoinColumn(name = "studyProgramme_id"))
  private Map<String, String> properties = new HashMap<String, String>();

  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;

  @Version
  @Column(nullable = false)
  private Long version;
}
