package fi.otavanopisto.pyramus.domainmodel.application;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class ApplicationAttachment {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ApplicationAttachment")
  @TableGenerator(name="ApplicationAttachment", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String applicationId;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String name;
  
  private String description;

  @NotNull
  @Column (nullable = false)
  private Integer size;

}