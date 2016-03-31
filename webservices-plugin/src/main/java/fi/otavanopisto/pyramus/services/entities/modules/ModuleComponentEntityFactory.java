package fi.otavanopisto.pyramus.services.entities.modules;

import fi.otavanopisto.pyramus.domainmodel.modules.ModuleComponent;
import fi.otavanopisto.pyramus.services.entities.EntityFactory;

public class ModuleComponentEntityFactory implements EntityFactory<ModuleComponentEntity> {

  public ModuleComponentEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    ModuleComponent moduleComponent = (ModuleComponent) domainObject;

    Double lengthUnits = null;
    Long lengthUnitId = null;
    if (moduleComponent.getLength() != null) {
      lengthUnits = moduleComponent.getLength().getUnits();
      if (moduleComponent.getLength().getUnit() != null)
        lengthUnitId = moduleComponent.getLength().getUnit().getId();
    }
    
    return new ModuleComponentEntity(moduleComponent.getId(), moduleComponent.getName(), moduleComponent.getDescription(), lengthUnits, lengthUnitId, moduleComponent.getId());
  }

}
