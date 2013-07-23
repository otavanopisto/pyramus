package fi.pyramus.rest.tranquil.file;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.file.FileType.class, entityType = TranquilModelType.COMPLETE)
public class FileTypeComplete extends FileTypeBase {

  public final static String[] properties = {};
}
