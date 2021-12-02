package fi.otavanopisto.pyramus.util.ixtable;

import fi.internetix.smvc.controllers.RequestContext;

/**
 * Facade to ease handling of data transferred by ixtable.js
 */
public class PyramusIxTableFacade {
  
  private PyramusIxTableFacade(RequestContext requestContext, String ixTableName) {
    this.requestContext = requestContext;
    this.tableName = ixTableName;
    this.rows = new PyramusIxTableRowsFacade(this);
  }
  
  public static PyramusIxTableFacade from(RequestContext requestContext, String ixTableName) {
    return new PyramusIxTableFacade(requestContext, ixTableName);
  }
  
  public int getRowCount() {
    return requestContext.getInteger(tableName + ".rowCount").intValue();
  }

  public PyramusIxTableRowsFacade rows() {
    return rows;
  }

  public String getName() {
    return tableName;
  }
  
  protected RequestContext getRequestContext() {
    return requestContext;
  }

  private RequestContext requestContext;
  private String tableName;
  private PyramusIxTableRowsFacade rows;
}
