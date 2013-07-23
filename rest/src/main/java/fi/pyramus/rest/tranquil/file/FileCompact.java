package fi.pyramus.rest.tranquil.file;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.file.File.class, entityType = TranquilModelType.COMPACT)
public class FileCompact extends FileBase {

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

  public Long getFileType_id() {
    return fileType_id;
  }

  public void setFileType_id(Long fileType_id) {
    this.fileType_id = fileType_id;
  }

  private Long creator_id;

  private Long lastModifier_id;

  private Long fileType_id;

  public final static String[] properties = {"creator","lastModifier","fileType"};
}
