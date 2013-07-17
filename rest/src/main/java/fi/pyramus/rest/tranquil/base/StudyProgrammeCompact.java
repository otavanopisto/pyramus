package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.StudyProgramme.class, entityType = TranquilModelType.COMPACT)
public class StudyProgrammeCompact extends StudyProgrammeBase {

  public Long getCategory_id() {
    return category_id;
  }

  public void setCategory_id(Long category_id) {
    this.category_id = category_id;
  }

  private Long category_id;

  public final static String[] properties = {"category"};
}
