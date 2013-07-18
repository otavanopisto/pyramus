package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.projects.Project.class, entityType = TranquilModelType.COMPACT)
public class ProjectCompact extends ProjectBase {

  public Long getCreator_id() {
    return creator_id;
  }

  public void setCreator_id(Long creator_id) {
    this.creator_id = creator_id;
  }

  public Long getLastModifier_id() {
    return lastModifier_id;
  }

  public void setLastModifier_id(Long lastModifier_id) {
    this.lastModifier_id = lastModifier_id;
  }

  public Long getOptionalStudiesLength_id() {
    return optionalStudiesLength_id;
  }

  public void setOptionalStudiesLength_id(Long optionalStudiesLength_id) {
    this.optionalStudiesLength_id = optionalStudiesLength_id;
  }

  public java.util.List<Long> getProjectModules_ids() {
    return projectModules_ids;
  }

  public void setProjectModules_ids(java.util.List<Long> projectModules_ids) {
    this.projectModules_ids = projectModules_ids;
  }

  public java.util.List<Long> getTags_ids() {
    return tags_ids;
  }

  public void setTags_ids(java.util.List<Long> tags_ids) {
    this.tags_ids = tags_ids;
  }

  private Long creator_id;

  private Long lastModifier_id;

  private Long optionalStudiesLength_id;

  private java.util.List<Long> projectModules_ids;

  private java.util.List<Long> tags_ids;

  public final static String[] properties = {"creator","lastModifier","optionalStudiesLength","projectModules","tags"};
}
