package fi.otavanopisto.pyramus.views.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.framework.PyramusFormViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.CSVImporter;
import fi.otavanopisto.pyramus.util.dataimport.EntityImportStrategy;

public class ImportCSVViewController extends PyramusFormViewController {


  @Override
  public void processForm(PageRequestContext requestContext) {
    requestContext.setIncludeJSP("/templates/system/importcsv.jsp");
  }
  
  @SuppressWarnings("rawtypes")
  @Override
  public void processSend(PageRequestContext requestContext) {
    EntityImportStrategy importStrategy = (EntityImportStrategy) requestContext.getEnum("importStrategy", EntityImportStrategy.class);
    
    CSVImporter dataImporter = new CSVImporter();
    Class entityClass = dataImporter.getEntityClassForStrategy(importStrategy);

    List<Object> list;
    try {
      list = dataImporter.importDataFromStream(requestContext.getFile("file").getInputStream(), 
          importStrategy, requestContext.getLoggedUserId(), requestContext.getRequest().getLocale());
    } catch (IOException e) {
      throw new SmvcRuntimeException(e);
    }
    
    List<Object> entityList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      Object o = list.get(i);
      
      if ((o != null) && (o.getClass().equals(entityClass)))
        entityList.add(o);
    }
    
    requestContext.setIncludeJSP("/templates/system/importcsv.jsp");
    requestContext.getRequest().setAttribute("fields", dataImporter.getHeaderFields());
    requestContext.getRequest().setAttribute("entities", list);
    requestContext.getRequest().setAttribute("entityClassEntities", entityList);
    requestContext.getRequest().setAttribute("importStrategy", importStrategy);
    
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }
 
  
}