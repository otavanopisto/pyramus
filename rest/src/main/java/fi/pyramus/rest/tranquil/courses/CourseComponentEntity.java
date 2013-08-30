package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.CourseComponent.class, entityType = TranquilModelType.COMPACT)
public class CourseComponentEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
  
  public Double getLength() {
    return length;
  }
  
  public void setLength(Double length) {
    this.length = length;
  }

  public Long getLength_id() {
    return length_id;
  }

  public void setLength_id(Long length_id) {
    this.length_id = length_id;
  }

  public Long getCourse_id() {
    return course_id;
  }

  public void setCourse_id(Long course_id) {
    this.course_id = course_id;
  }

  public java.util.List<Long> getResources_ids() {
    return resources_ids;
  }

  public void setResources_ids(java.util.List<Long> resources_ids) {
    this.resources_ids = resources_ids;
  }

  private Long id;

  private String name;

  private String description;

  private Boolean archived;

  private Long version;
  
  private Double length;

  private Long length_id;

  private Long course_id;

  private java.util.List<Long> resources_ids;

  public final static String[] properties = {"id","name","description","archived","version","length","course","resources"};
}
