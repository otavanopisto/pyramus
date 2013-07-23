package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.projects.StudentProject.class, entityType = TranquilModelType.UPDATE)
public class StudentProjectUpdate extends StudentProjectComplete {

  public void setCreator(UserCompact creator) {
    super.setCreator(creator);
  }

  public UserCompact getCreator() {
    return (UserCompact)super.getCreator();
  }

  public void setLastModifier(UserCompact lastModifier) {
    super.setLastModifier(lastModifier);
  }

  public UserCompact getLastModifier() {
    return (UserCompact)super.getLastModifier();
  }

  public void setOptionalStudiesLength(EducationalLengthCompact optionalStudiesLength) {
    super.setOptionalStudiesLength(optionalStudiesLength);
  }

  public EducationalLengthCompact getOptionalStudiesLength() {
    return (EducationalLengthCompact)super.getOptionalStudiesLength();
  }

  public void setStudent(StudentCompact student) {
    super.setStudent(student);
  }

  public StudentCompact getStudent() {
    return (StudentCompact)super.getStudent();
  }

  public void setProject(ProjectCompact project) {
    super.setProject(project);
  }

  public ProjectCompact getProject() {
    return (ProjectCompact)super.getProject();
  }

  public void setStudentProjectModules(java.util.List<StudentProjectModuleCompact> studentProjectModules) {
    super.setStudentProjectModules(studentProjectModules);
  }

  public java.util.List<StudentProjectModuleCompact> getStudentProjectModules() {
    return (java.util.List<StudentProjectModuleCompact>)super.getStudentProjectModules();
  }

  public void setTags(java.util.List<TagCompact> tags) {
    super.setTags(tags);
  }

  public java.util.List<TagCompact> getTags() {
    return (java.util.List<TagCompact>)super.getTags();
  }

  public final static String[] properties = {"creator","lastModifier","optionalStudiesLength","student","project","studentProjectModules","tags"};
}
