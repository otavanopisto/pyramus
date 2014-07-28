package fi.pyramus.util.dataimport;


public interface EntityHandlingStrategy {

  void handleValue(String fieldName, Object value, DataImportContext context) 
      throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException;
  
  void saveEntities(DataImportContext context);
  
  void initializeContext(DataImportContext context);

  @SuppressWarnings("rawtypes")
  Class getMainEntityClass();
}
