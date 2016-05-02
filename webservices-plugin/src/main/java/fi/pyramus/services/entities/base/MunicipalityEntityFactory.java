package fi.pyramus.services.entities.base;

import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.pyramus.services.entities.EntityFactory;

public class MunicipalityEntityFactory implements EntityFactory<MunicipalityEntity> {

  public MunicipalityEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    Municipality municipality = (Municipality) domainObject;
    return new MunicipalityEntity(municipality.getId(), municipality.getCode().toString(), municipality.getName(), municipality.getArchived());
  }

}
