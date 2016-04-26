package fi.pyramus.services.entities.courses;

public class CourseEducationTypeEntity {
  
  public CourseEducationTypeEntity() {
  }

  public CourseEducationTypeEntity(Long id, String name, String code, CourseEducationSubtypeEntity[] subtypes) {
    super();
    this.id = id;
    this.name = name;
    this.code = code;
    this.subtypes = subtypes;
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

  public CourseEducationSubtypeEntity[] getSubtypes() {
    return subtypes;
  }

  public void setSubtypes(CourseEducationSubtypeEntity[] subtypes) {
    this.subtypes = subtypes;
  }

  private Long id;
  private String name;
  private String code;
  private CourseEducationSubtypeEntity subtypes[];
}
