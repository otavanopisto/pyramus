package fi.pyramus.rest.tranquil.file;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.file.FileType.class, entityType = TranquilModelType.UPDATE)
public class FileTypeUpdate extends FileTypeComplete {

  public final static String[] properties = {};
}
