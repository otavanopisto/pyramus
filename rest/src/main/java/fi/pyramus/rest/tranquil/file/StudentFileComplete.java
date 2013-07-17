package fi.pyramus.rest.tranquil.file;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.file.StudentFile.class, entityType = TranquilModelType.COMPLETE)
public class StudentFileComplete extends StudentFileBase {

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

  public TranquilModelEntity getFileType() {
    return fileType;
  }

  public void setFileType(TranquilModelEntity fileType) {
    this.fileType = fileType;
  }

  public TranquilModelEntity getStudent() {
    return student;
  }

  public void setStudent(TranquilModelEntity student) {
    this.student = student;
  }

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  private TranquilModelEntity fileType;

  private TranquilModelEntity student;

  public final static String[] properties = {"creator","lastModifier","fileType","student"};
}
