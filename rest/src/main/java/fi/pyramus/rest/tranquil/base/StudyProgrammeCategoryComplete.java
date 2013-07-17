package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.StudyProgrammeCategory.class, entityType = TranquilModelType.COMPLETE)
public class StudyProgrammeCategoryComplete extends StudyProgrammeCategoryBase {

  public TranquilModelEntity getEducationType() {
    return educationType;
  }

  public void setEducationType(TranquilModelEntity educationType) {
    this.educationType = educationType;
  }

  private TranquilModelEntity educationType;

  public final static String[] properties = {"educationType"};
}
