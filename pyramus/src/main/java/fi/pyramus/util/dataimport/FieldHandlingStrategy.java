package fi.pyramus.util.dataimport;

public interface FieldHandlingStrategy {

  void handleField(String fieldName, Object fieldValue, DataImportContext context) 
      throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException;
}
