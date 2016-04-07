package fi.otavanopisto.pyramus.persistence.events;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class EntityStateData {

  public Map<String, Object> addUpdateBatch(String entity, Object id) {
    Map<String, Object> updateBatch = new HashMap<>();

    Map<Object, Map<String, Object>> entityUpdateBatch = updateBatches.get(entity);
    if (entityUpdateBatch == null) {
      entityUpdateBatch = new HashMap<>();
      updateBatches.put(entity, entityUpdateBatch);
    }
    
    entityUpdateBatch.put(id, updateBatch);

    return updateBatch;
  }

  public Map<String, Object> getUpdateBatch(String entity, Object id) {
    Map<Object, Map<String, Object>> entityUpdateBatch = updateBatches.get(entity);

    return entityUpdateBatch != null ? entityUpdateBatch.get(id) : null;
  }

  public void removeUpdateBatch(String entity, Object id) {
    Map<Object, Map<String, Object>> entityUpdateBatch = updateBatches.get(entity);
    entityUpdateBatch.remove(id);
  }

  private Map<String, Map<Object, Map<String, Object>>> updateBatches = new Hashtable<>();
}