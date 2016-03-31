package fi.otavanopisto.pyramus.services.entities.modules;

import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.services.entities.EntityFactory;
import fi.otavanopisto.pyramus.services.entities.EntityFactoryVault;
import fi.otavanopisto.pyramus.services.entities.base.SubjectEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEducationTypeEntity;
import fi.otavanopisto.pyramus.services.entities.users.UserEntity;

public class ModuleEntityFactory implements EntityFactory<ModuleEntity> {

  public ModuleEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    Module module = (Module) domainObject;
    
    UserEntity creator = EntityFactoryVault.buildFromDomainObject((StaffMember) module.getCreator());
    UserEntity lastModifier = EntityFactoryVault.buildFromDomainObject((StaffMember) module.getLastModifier());
    SubjectEntity subject = EntityFactoryVault.buildFromDomainObject(module.getSubject());
    
    Double courseLengthUnits = null;
    Long courseLengthUnitId = null;
    if (module.getCourseLength() != null) {
      courseLengthUnits = module.getCourseLength().getUnits();
      if (module.getCourseLength().getUnit() != null)
        courseLengthUnitId = module.getCourseLength().getUnit().getId();
    }
    
    CourseEducationTypeEntity[] courseEducationTypes = (CourseEducationTypeEntity[]) EntityFactoryVault.buildFromDomainObjects(module.getCourseEducationTypes());
    ModuleComponentEntity[] moduleComponents = (ModuleComponentEntity[]) EntityFactoryVault.buildFromDomainObjects(module.getModuleComponents());

    int i = 0;
    String[] tags = new String[module.getTags().size()];
    for (Tag tag : module.getTags()) {
      tags[i++] = tag.getText();
    }  
    
    return new ModuleEntity(module.getId(), module.getName(), tags, creator, module.getCreated(), lastModifier, module.getLastModified(), 
        module.getDescription(), subject, module.getCourseNumber(), courseLengthUnits, courseLengthUnitId, courseEducationTypes, module.getArchived(), moduleComponents);
  }

}
