package fi.otavanopisto.pyramus.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.otavanopisto.pyramus.util.ReflectionApiUtils;
import net.sf.json.*;

/** A selective Java collection to <code>JSONArray</code> converter.
 * <code>JSONArrayExtractor</code> converts a <code>List</code>
 * of beans containing a given set of properties into
 * a <code>JSONArray</code> that contains <code>JSONObject</code>s
 * with the same properties, converted appropriately, using <code>JSONObject</code>'s
 * conversion rules, with the exception that properties containing <code>null</code>s
 * are skipped and so don't end up in the converted object.
 * Usage:
 * <pre>
 *   List&lt;Address&gt; addresses = school.getContactInfo().getAddresses();
 *   JSONArray jaAddresses = new JSONArrayExtractor("id",
 *                                                  "name",
 *                                                  "streetAddress",
 *                                                  "postalCode",
 *                                                  "city",
 *                                                  "country").extract(addresses); 
 * </pre> 
 * @author ilmo.euro@otavanopisto.fi
 */
public class JSONArrayExtractor {
  
  
  /** Creates a new Java collection to <code>JSONArray</code> converter
   * that converts no properties, just emits empty <code>JSONObject</code>s.
   */
  public JSONArrayExtractor() {
    attributeNames = new ArrayList<>();
  }
  
  /** Creates a new Java collection to <code>JSONArray</code> converter
   * that converts a given set of properties of each object.
   * 
   * @param args The properties to convert.
   */
  public JSONArrayExtractor(String... args) {
    attributeNames = Arrays.asList(args);
  }
  
  /** Returns <code>sourceObjects</code> converted to a string containing JSON.
   * 
   * @param sourceObjects The objects to convert.
   * @return <code>sourceObject</code>, converted to a JSON string.
   */
  public <T> String extractString(List<T> sourceObjects) {
    return extract(sourceObjects).toString();
  }
  
  /** Returns <code>sourceObjects</code> converted to a JSON array.
   * 
   * @param sourceObjects The objects to convert.
   * @return <code>sourceObject</code>, converted to a JSON array.
   */
  public <T> JSONArray extract(List<T> sourceObjects) {
    JSONArray destObjects = new JSONArray();
    for (Object sourceObject : sourceObjects) {
      JSONObject destObject = new JSONObject();
      for (String attributeName : attributeNames) {
        Object attributeValue;
        // Nulls are deliberately skipped so that they are undefined in JS
        try {
          attributeValue = ReflectionApiUtils.getObjectFieldValue(sourceObject, attributeName, true);
        } catch (NullPointerException e) {
          throw new SmvcRuntimeException(StatusCode.UNDEFINED, e.getMessage());
        } catch (IllegalAccessException e) {
          throw new SmvcRuntimeException(StatusCode.UNDEFINED, e.getMessage());
        } catch (IllegalArgumentException e) {
          throw new SmvcRuntimeException(StatusCode.UNDEFINED, e.getMessage());
        } catch (InvocationTargetException e) {
          throw new SmvcRuntimeException(StatusCode.UNDEFINED, e.getMessage());
        }
        if (attributeValue != null) {
          destObject.put(attributeName, attributeValue);
        }
      }
      destObjects.add(destObject);
    }
    
    return destObjects;
  }
  
  private List<String> attributeNames;
}
