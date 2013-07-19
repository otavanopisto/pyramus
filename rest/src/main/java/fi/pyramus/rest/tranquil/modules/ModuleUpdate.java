package fi.pyramus.rest.tranquil.modules;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.modules.Module.class, entityType = TranquilModelType.UPDATE)
public class ModuleUpdate extends ModuleComplete {

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

  public void setSubject(SubjectCompact subject) {
    super.setSubject(subject);
  }

  public SubjectCompact getSubject() {
    return (SubjectCompact)super.getSubject();
  }

  public void setCourseEducationTypeByEducationTypeId(CourseEducationTypeCompact courseEducationTypeByEducationTypeId) {
    super.setCourseEducationTypeByEducationTypeId(courseEducationTypeByEducationTypeId);
  }

  public CourseEducationTypeCompact getCourseEducationTypeByEducationTypeId() {
    return (CourseEducationTypeCompact)super.getCourseEducationTypeByEducationTypeId();
  }

  public void setCourseLength(EducationalLengthCompact courseLength) {
    super.setCourseLength(courseLength);
  }

  public EducationalLengthCompact getCourseLength() {
    return (EducationalLengthCompact)super.getCourseLength();
  }

  public void setCourseEducationTypes(java.util.List<CourseEducationTypeCompact> courseEducationTypes) {
    super.setCourseEducationTypes(courseEducationTypes);
  }

  public java.util.List<CourseEducationTypeCompact> getCourseEducationTypes() {
    return (java.util.List<CourseEducationTypeCompact>)super.getCourseEducationTypes();
  }

  public void setVariables(java.util.List<CourseBaseVariableCompact> variables) {
    super.setVariables(variables);
  }

  public java.util.List<CourseBaseVariableCompact> getVariables() {
    return (java.util.List<CourseBaseVariableCompact>)super.getVariables();
  }

  public void setModuleComponents(java.util.List<ModuleComponentCompact> moduleComponents) {
    super.setModuleComponents(moduleComponents);
  }

  public java.util.List<ModuleComponentCompact> getModuleComponents() {
    return (java.util.List<ModuleComponentCompact>)super.getModuleComponents();
  }

  public void setTags(java.util.List<TagCompact> tags) {
    super.setTags(tags);
  }

  public java.util.List<TagCompact> getTags() {
    return (java.util.List<TagCompact>)super.getTags();
  }

  public final static String[] properties = {"creator","lastModifier","subject","courseEducationTypeByEducationTypeId","courseLength","courseEducationTypes","variables","moduleComponents","tags"};
}
