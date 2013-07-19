package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.StudyProgramme.class, entityType = TranquilModelType.UPDATE)
public class StudyProgrammeUpdate extends StudyProgrammeComplete {

  public void setCategory(StudyProgrammeCategoryCompact category) {
    super.setCategory(category);
  }

  public StudyProgrammeCategoryCompact getCategory() {
    return (StudyProgrammeCategoryCompact)super.getCategory();
  }

  public final static String[] properties = {"category"};
}
