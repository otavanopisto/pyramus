package fi.pyramus.services.entities.base;

import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.pyramus.services.entities.EntityFactory;

public class LanguageEntityFactory implements EntityFactory<LanguageEntity> {

  public LanguageEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    Language language = (Language) domainObject;
    return new LanguageEntity(language.getId(), language.getCode(), language.getName(), language.getArchived());
  }
}
