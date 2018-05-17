package fi.otavanopisto.pyramus.dao.base;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;

@Stateless
public class OrganizationDAO extends PyramusEntityDAO<Organization> {

  public Organization create(String name) {
    Organization organization = new Organization();
    
    organization.setName(name);
    organization.setArchived(false);
    
    return persist(organization);
  }

  public Organization update(Organization organization, String name) {
    organization.setName(name);
    
    return persist(organization);
  }

}