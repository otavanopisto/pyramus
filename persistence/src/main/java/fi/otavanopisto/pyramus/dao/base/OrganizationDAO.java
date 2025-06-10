package fi.otavanopisto.pyramus.dao.base;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.events.OrganizationArchivedEvent;
import fi.otavanopisto.pyramus.events.OrganizationCreatedEvent;
import fi.otavanopisto.pyramus.events.OrganizationUpdatedEvent;

@Stateless
public class OrganizationDAO extends PyramusEntityDAO<Organization> {

  @Inject
  private Event<OrganizationCreatedEvent> organizationCreatedEvent;
  
  @Inject
  private Event<OrganizationUpdatedEvent> organizationUpdatedEvent;
  
  @Inject
  private Event<OrganizationArchivedEvent> organizationArchivedEvent;

  public Organization create(String name, EducationType educationType) {
    Organization organization = new Organization();
    
    organization.setName(name);
    organization.setEducationType(educationType);
    organization.setArchived(false);
    organization = persist(organization);
    organizationCreatedEvent.fire(new OrganizationCreatedEvent(organization.getId(), organization.getName()));
    return organization;
  }

  public Organization update(Organization organization, String name, EducationType educationType) {
    organization.setName(name);
    organization.setEducationType(educationType);
    organization = persist(organization);
    organizationUpdatedEvent.fire(new OrganizationUpdatedEvent(organization.getId(), organization.getName()));
    return organization;
  }

  public Organization updateBillingDetails(Organization organization, BillingDetails billingDetails) {
    organization.setBillingDetails(billingDetails);
    organization = persist(organization);
    organizationUpdatedEvent.fire(new OrganizationUpdatedEvent(organization.getId(), organization.getName()));
    return organization;
  }

  @Override
  public void archive(ArchivableEntity entity, User modifier) {
    super.archive(entity, modifier);
    if (entity instanceof Organization) {
      organizationArchivedEvent.fire(new OrganizationArchivedEvent(((Organization) entity).getId()));
    }
  }

}