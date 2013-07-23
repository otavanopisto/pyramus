package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.projects.Project.class, entityType = TranquilModelType.UPDATE)
public class ProjectUpdate extends ProjectComplete {

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

  public void setProjectModules(java.util.List<ProjectModuleCompact> projectModules) {
    super.setProjectModules(projectModules);
  }

  public java.util.List<ProjectModuleCompact> getProjectModules() {
    return (java.util.List<ProjectModuleCompact>)super.getProjectModules();
  }

  public void setTags(java.util.List<TagCompact> tags) {
    super.setTags(tags);
  }

  public java.util.List<TagCompact> getTags() {
    return (java.util.List<TagCompact>)super.getTags();
  }

  public final static String[] properties = {"creator","lastModifier","optionalStudiesLength","projectModules","tags"};
}
