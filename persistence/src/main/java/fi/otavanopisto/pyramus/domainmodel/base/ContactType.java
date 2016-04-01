package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Indexed
public class ContactType implements ArchivableEntity {

  public Long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Boolean getArchived() {
    return archived;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public Boolean getNonUnique() {
    return nonUnique;
  }

  public void setNonUnique(Boolean nonUnique) {
    this.nonUnique = nonUnique;
  }

  @Id 
  @DocumentId
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ContactType")  
  @TableGenerator(name="ContactType", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @Field
  private String name;

  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean nonUnique = Boolean.FALSE;

  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;

  @Version
  @Column(nullable = false)
  private Long version;
}
