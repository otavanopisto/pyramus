package fi.pyramus.rest.tranquil.file;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.file.StudentFile.class, entityType = TranquilModelType.UPDATE)
public class StudentFileUpdate extends StudentFileComplete {

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

  public void setFileType(FileTypeCompact fileType) {
    super.setFileType(fileType);
  }

  public FileTypeCompact getFileType() {
    return (FileTypeCompact)super.getFileType();
  }

  public void setStudent(StudentCompact student) {
    super.setStudent(student);
  }

  public StudentCompact getStudent() {
    return (StudentCompact)super.getStudent();
  }

  public final static String[] properties = {"creator","lastModifier","fileType","student"};
}
