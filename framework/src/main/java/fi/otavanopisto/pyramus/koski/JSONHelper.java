package fi.otavanopisto.pyramus.koski;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONHelper {
  
  public static String getString(JSONObject object, String path) {
    String[] split = path.split("\\.");

    JSONObject element = object;
    for (int i = 0; i < split.length - 1; i++) {
      element = element.getJSONObject(split[i]);
      if (element == null)
        return null;
    }
    
    return element.getString(split[split.length - 1]);
  }

  public static JSONArray getArray(JSONObject object, String path) {
    String[] split = path.split("\\.");

    JSONObject element = object;
    for (int i = 0; i < split.length - 1; i++) {
      element = element.getJSONObject(split[i]);
      if (element == null)
        return null;
    }
    
    return element.getJSONArray(split[split.length - 1]);
  }
  
}
