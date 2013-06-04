package fi.pyramus.services.entities.courses;

public class CourseParticipationTypeEntity {
  
  public CourseParticipationTypeEntity() {
  }

  public CourseParticipationTypeEntity(Long id, String name) {
    super();
    this.id = id;
    this.name = name;
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

  private Long id;
  private String name;
}
