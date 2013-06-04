package fi.pyramus.services.entities.base;

import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.services.entities.EntityFactory;

public class NationalityEntityFactory implements EntityFactory<NationalityEntity> {

  public NationalityEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    Nationality nationality = (Nationality) domainObject;
    return new NationalityEntity(nationality.getId(), nationality.getCode(), nationality.getName(),
            nationality.getArchived());
  }

}
