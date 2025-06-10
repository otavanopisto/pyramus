package fi.otavanopisto.pyramus.rest.controller;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;

@Dependent
@Stateless
public class OrganizationController {
  
  @Inject
  private OrganizationDAO organizationDAO;
  
  public Organization findById(Long id) {
    return organizationDAO.findById(id);
  }

}
