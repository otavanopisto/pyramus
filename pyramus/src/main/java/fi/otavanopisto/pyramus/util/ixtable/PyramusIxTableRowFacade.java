package fi.otavanopisto.pyramus.util.ixtable;

import java.util.Date;

import fi.internetix.smvc.controllers.RequestContext;

public class PyramusIxTableRowFacade {
  
  protected PyramusIxTableRowFacade(PyramusIxTableFacade table, int row) {
    this.table = table;
    this.row = row;
  }
  
  public Double getDouble(String field) {
    return getRequestContext().getDouble(getRowFieldName(row, field));
  }

  public Integer getInteger(String field) {
    return getRequestContext().getInteger(getRowFieldName(row, field));
  }

  public Long getLong(String field) {
    return getRequestContext().getLong(getRowFieldName(row, field));
  }

  public String getString(String field) {
    return getRequestContext().getString(getRowFieldName(row, field));
  }

  public Date getDate(String field) {
    return getRequestContext().getDate(getRowFieldName(row, field));
  }

  private String getRowFieldName(int row, String field) {
    return String.format("%s.%d.%s", table.getName(), row, field);
  }

  private RequestContext getRequestContext() {
    return table.getRequestContext();
  }
  
  private PyramusIxTableFacade table;
  private int row;
}