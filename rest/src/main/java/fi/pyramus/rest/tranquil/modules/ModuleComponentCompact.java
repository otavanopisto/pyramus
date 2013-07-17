package fi.pyramus.rest.tranquil.modules;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.modules.ModuleComponent.class, entityType = TranquilModelType.COMPACT)
public class ModuleComponentCompact extends ModuleComponentBase {

  public Long getLength_id() {
    return length_id;
  }

  public void setLength_id(Long length_id) {
    this.length_id = length_id;
  }

  public Long getModule_id() {
    return module_id;
  }

  public void setModule_id(Long module_id) {
    this.module_id = module_id;
  }

  private Long length_id;

  private Long module_id;

  public final static String[] properties = {"length","module"};
}
