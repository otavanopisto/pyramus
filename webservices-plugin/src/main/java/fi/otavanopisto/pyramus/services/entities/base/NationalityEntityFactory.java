package fi.otavanopisto.pyramus.services.entities.base;

import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.services.entities.EntityFactory;

public class NationalityEntityFactory implements EntityFactory<NationalityEntity> {

  public NationalityEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    Nationality nationality = (Nationality) domainObject;
    return new NationalityEntity(nationality.getId(), nationality.getCode(), nationality.getName(),
            nationality.getArchived());
  }

}
