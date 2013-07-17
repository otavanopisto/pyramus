package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentGroup.class, entityType = TranquilModelType.COMPLETE)
public class StudentGroupComplete extends StudentGroupBase {

  public TranquilModelEntity getCreator() {
    return creator;
  }

  public void setCreator(TranquilModelEntity creator) {
    this.creator = creator;
  }

  public TranquilModelEntity getLastModifier() {
    return lastModifier;
  }

  public void setLastModifier(TranquilModelEntity lastModifier) {
    this.lastModifier = lastModifier;
  }

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  public final static String[] properties = {"creator","lastModifier"};
}
