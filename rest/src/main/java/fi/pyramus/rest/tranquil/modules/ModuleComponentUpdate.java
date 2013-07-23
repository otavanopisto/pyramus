package fi.pyramus.rest.tranquil.modules;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.modules.ModuleComponent.class, entityType = TranquilModelType.UPDATE)
public class ModuleComponentUpdate extends ModuleComponentComplete {

  public void setLength(EducationalLengthCompact length) {
    super.setLength(length);
  }

  public EducationalLengthCompact getLength() {
    return (EducationalLengthCompact)super.getLength();
  }

  public void setModule(ModuleCompact module) {
    super.setModule(module);
  }

  public ModuleCompact getModule() {
    return (ModuleCompact)super.getModule();
  }

  public final static String[] properties = {"length","module"};
}
