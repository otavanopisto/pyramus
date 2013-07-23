package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.StudyProgramme.class, entityType = TranquilModelType.COMPLETE)
public class StudyProgrammeComplete extends StudyProgrammeBase {

  public TranquilModelEntity getCategory() {
    return category;
  }

  public void setCategory(TranquilModelEntity category) {
    this.category = category;
  }

  private TranquilModelEntity category;

  public final static String[] properties = {"category"};
}
