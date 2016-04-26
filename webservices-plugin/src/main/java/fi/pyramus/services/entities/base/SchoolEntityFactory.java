package fi.pyramus.services.entities.base;

import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.pyramus.services.entities.EntityFactory;

public class SchoolEntityFactory implements EntityFactory<SchoolEntity> {

  public SchoolEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    School school = (School) domainObject;
    
    int i = 0;
    String[] tags = new String[school.getTags().size()];
    for (Tag tag : school.getTags()) {
      tags[i++] = tag.getText();
    }
    
    return new SchoolEntity(school.getId(), school.getCode(), school.getName(), tags, school.getArchived());
  }

}
