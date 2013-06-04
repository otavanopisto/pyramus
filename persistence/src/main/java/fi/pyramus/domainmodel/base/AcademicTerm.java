package fi.pyramus.domainmodel.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class AcademicTerm implements ArchivableEntity {

  public Long getId() {
    return id;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getEndDate() {
    return endDate;
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

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="AcademicTerm")  
  @TableGenerator(name="AcademicTerm", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @Column (nullable=false)
  @NotEmpty
  private String name;

  @Column (nullable=false)
  @Temporal (value=TemporalType.DATE)
  private Date startDate;
  
  @Column (nullable=false)
  @Temporal (value=TemporalType.DATE)
  private Date endDate;
  
  @NotNull
  @Column(nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;

  @Version
  @Column(nullable = false)
  private Long version;
}
