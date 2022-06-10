package fi.otavanopisto.pyramus.applications;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Application form data JSONObject decorator.
 */
public class ApplicationFormData {

  private ApplicationFormData() {
  }
  
  public static ApplicationFormData fromJSONObject(Object jsonObject) {
    ApplicationFormData applicationFormData = new ApplicationFormData();
    applicationFormData.formData = JSONObject.fromObject(jsonObject);
    return applicationFormData;
  }
  
  public String getFormData() {
    return formData.toString();
  }
  
  public String toString() {
    return formData.toString();
  }
  
  public boolean has(String key) {
    return formData.has(key);
  }

  /**
   * Returns a sanitized value of the given field.
   */
  public String getString(String key) {
    return formData.has(key) ? StringUtils.trim(formData.getString(key)) : null;
  }
  
  public Object get(String key) {
    return formData.get(key);
  }

  public JSONArray getJSONArray(String key) {
    return formData.getJSONArray(key);
  }
  
  private JSONObject formData;
}
