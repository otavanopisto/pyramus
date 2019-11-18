package fi.otavanopisto.pyramus.util.dataimport.scripting.api;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.util.dataimport.scripting.InvalidScriptException;

public class StaffMemberAPI {
  
  public Long create(String firstName, String lastName, String role, Long personId, Long organizationId)
                         throws InvalidScriptException {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();

    Person person = personDAO.findById(personId);
    if (person == null) {
      throw new InvalidScriptException("Person not found");
    }

    Organization organization = organizationId != null ? organizationDAO.findById(organizationId) : null;
    if (organization == null) {
      throw new InvalidScriptException("Organization not found");
    }
    
    StaffMember staffMember = staffMemberDAO.create(organization, firstName, lastName, Role.valueOf(role), person, false);
    if (staffMember == null) {
      throw new InvalidScriptException("Failed to create new staff member");
    } else {
      if (person.getDefaultUser() == null) {
        personDAO.updateDefaultUser(person, staffMember);
      }
      
      return staffMember.getId();
    }
  }
  
  public void addEmail(Long staffMemberId, Long contactTypeId, String address, Boolean defaultAddress) throws InvalidScriptException {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    address = address != null ? address.trim() : null;
    
    ContactType contactType = contactTypeDAO.findById(contactTypeId);
    if (contactType == null) {
      throw new InvalidScriptException("ContactType could not be found");
    }
    
    StaffMember staffMember = staffMemberDAO.findById(staffMemberId);
    if (staffMember == null) {
      throw new InvalidScriptException("StaffMember could not be found");
    }

    Person person = personDAO.findByUniqueEmail(address);
    if (person != null) {
      if (!staffMember.getPerson().getId().equals(person.getId())) {
        throw new InvalidScriptException("Email is already defined for another user");
      }
      
      StaffMember emailStaffMember = staffMemberDAO.findByUniqueEmail(address);
      if (emailStaffMember != null && emailStaffMember.getId().equals(staffMemberId)) {
        throw new InvalidScriptException("Email is already defined for this staff member");
      }
    }
    
    emailDAO.create(staffMember.getContactInfo(), contactType, defaultAddress, address);
  }

}
