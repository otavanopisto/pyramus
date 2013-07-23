package fi.pyramus.rest.tranquil.file;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.file.File.class, entityType = TranquilModelType.UPDATE)
public class FileUpdate extends FileComplete {

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

  public final static String[] properties = {"creator","lastModifier","fileType"};
}
