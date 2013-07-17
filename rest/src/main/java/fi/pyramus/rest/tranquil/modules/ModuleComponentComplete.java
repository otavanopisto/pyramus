package fi.pyramus.rest.tranquil.modules;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.modules.ModuleComponent.class, entityType = TranquilModelType.COMPLETE)
public class ModuleComponentComplete extends ModuleComponentBase {

  public TranquilModelEntity getLength() {
    return length;
  }

  public void setLength(TranquilModelEntity length) {
    this.length = length;
  }

  public TranquilModelEntity getModule() {
    return module;
  }

  public void setModule(TranquilModelEntity module) {
    this.module = module;
  }

  private TranquilModelEntity length;

  private TranquilModelEntity module;

  public final static String[] properties = {"length","module"};
}
