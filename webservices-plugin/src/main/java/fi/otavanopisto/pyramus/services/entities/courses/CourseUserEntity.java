package fi.otavanopisto.pyramus.services.entities.courses;

import fi.otavanopisto.pyramus.services.entities.users.UserEntity;

public class CourseUserEntity {
  
  public CourseUserEntity() {
  }

  public CourseUserEntity(Long id, CourseEntity course, UserEntity user, CourseUserRoleEntity role) {
    super();
    this.id = id;
    this.course = course;
    this.user = user;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CourseEntity getCourse() {
    return course;
  }

  public void setCourse(CourseEntity course) {
    this.course = course;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public CourseUserRoleEntity getRole() {
    return role;
  }

  public void setRole(CourseUserRoleEntity role) {
    this.role = role;
  }

  private Long id;
  private CourseEntity course;
  private UserEntity user;
  private CourseUserRoleEntity role;
}
