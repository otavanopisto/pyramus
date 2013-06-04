package fi.pyramus.services.entities.courses;

public class CourseEducationSubtypeEntity {

  public CourseEducationSubtypeEntity() {
  }
  
  public CourseEducationSubtypeEntity(Long id, String name, String code, Long courseEducationTypeId) {
    super();
    this.id = id;
    this.name = name;
    this.code = code;
    this.courseEducationTypeId = courseEducationTypeId;
  }

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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Long getCourseEducationTypeId() {
    return courseEducationTypeId;
  }

  public void setCourseEducationTypeId(Long courseEducationTypeId) {
    this.courseEducationTypeId = courseEducationTypeId;
  }

  private Long id;
  private String name;
  private String code;
  private Long courseEducationTypeId;
}
