package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.CourseDescription.class, entityType = TranquilModelType.COMPACT)
public class CourseDescriptionEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getCourseBase_id() {
    return courseBase_id;
  }

  public void setCourseBase_id(Long courseBase_id) {
    this.courseBase_id = courseBase_id;
  }

  public Long getCategory_id() {
    return category_id;
  }

  public void setCategory_id(Long category_id) {
    this.category_id = category_id;
  }

  private Long id;

  private String description;

  private Long courseBase_id;

  private Long category_id;

  public final static String[] properties = {"id","description","courseBase","category"};
}
