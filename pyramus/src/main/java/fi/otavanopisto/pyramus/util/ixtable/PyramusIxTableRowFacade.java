package fi.otavanopisto.pyramus.util.ixtable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.apache.commons.lang3.EnumUtils;

import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.framework.DateUtils;

public class PyramusIxTableRowFacade {
  
  protected PyramusIxTableRowFacade(PyramusIxTableFacade table, int row) {
    this.table = table;
    this.row = row;
  }
  
  public Boolean getBoolean(String field) {
    return getRequestContext().getBoolean(getRowFieldName(row, field));
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

  /**
   * Get named parameter value as long. If it doesn't exist or
   * cannot be parsed to long, returns default value.
   * 
   * @param field name of the parameter
   * @param defaultValue default value if the parameter doesn't exist or cannot be parsed to long
   * @return long value of the parameter or default value in case the parameter value doesn't exist
   */
  public long getLong(String field, long defaultValue) {
    return getRequestContext().getLong(getRowFieldName(row, field), defaultValue);
  }

  public String getString(String field) {
    return getRequestContext().getString(getRowFieldName(row, field));
  }

  public Date getDate(String field) {
    return getRequestContext().getDate(getRowFieldName(row, field));
  }
  
  /**
   * Returns fields value as LocalDate.
   * 
   * @param field field name
   * @return LocalDate or null if value cannot be parsed.
   */
  public LocalDate getLocalDate(String field) {
    return DateUtils.toLocalDate(getDate(field));
  }

  /**
   * Returns fields value as LocalDateTime.
   * 
   * @param field field name
   * @return LocalDateTime or null if value cannot be parsed.
   */
  public LocalDateTime getLocalDateTime(String field) {
    return DateUtils.toLocalDateTime(getDate(field));
  }
  
  /**
   * Returns enum from the value the given field contains. If the field 
   * value doesn't match any enum value, returns null.
   * 
   * @param field field name
   * @param enumClass class of the enum
   * @return enum
   */
  public <E extends Enum<E>> E getEnum(String field, Class<E> enumClass) {
    return EnumUtils.getEnum(enumClass, getString(field));
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