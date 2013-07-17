package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.StudyProgrammeCategory.class, entityType = TranquilModelType.COMPACT)
public class StudyProgrammeCategoryCompact extends StudyProgrammeCategoryBase {

  public Long getEducationType_id() {
    return educationType_id;
  }

  public void setEducationType_id(Long educationType_id) {
    this.educationType_id = educationType_id;
  }

  private Long educationType_id;

  public final static String[] properties = {"educationType"};
}
