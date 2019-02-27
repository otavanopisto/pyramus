package fi.otavanopisto.pyramus.framework;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.security.impl.Permissions;
import fi.otavanopisto.pyramus.security.impl.permissions.OrganizationPermissions;

public class UserUtils {

  private UserUtils() {
  }

  /**
   * Is email address allowed (not in use)
   * 
   * @param emailAddress address that is being used
   * @param contactType contacttype for emailAddress, if contacttype is non-unique, this methods returns always true
   * @return true if email is not in use
   */
  public static boolean isAllowedEmail(String emailAddress, ContactType contactType) {
    return isAllowedEmail(emailAddress, contactType, null);
  }

  /**
   * Is email address allowed (not in use by another person)
   * 
   * @param emailAddress address
   * @param contactType contacttype for emailAddress, if contacttype is non-unique, this methods returns always true
   * @param personId id of person receiving this address
   * @return true if email may be used by the provided person
   * @throws IllegalArgumentException if the given email is blank 
   */
  public static boolean isAllowedEmail(String emailAddress, ContactType contactType, Long personId) {
    if (StringUtils.isBlank(emailAddress)) {
      throw new IllegalArgumentException("Email address cannot be blank.");
    }
    
    // if Email is being put into non-unique field, it is always allowed    
    if (contactType.getNonUnique())
      return true;
    
    emailAddress = StringUtils.trim(emailAddress);

    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();

    StaffMember staffMember = staffMemberDAO.findByUniqueEmail(emailAddress);
    List<Student> students = studentDAO.listBy(null, emailAddress, null, null, null, null);

    if (personId != null) {
      // True, if found matches the person, or if not found at all 
      boolean allowed = true;
      
      allowed = allowed && (staffMember != null ? personId.equals(staffMember.getPerson().getId()) : true);
      
      for (Student student : students) {
        if (student.getContactInfo().getEmails() != null) {
          for (Email email : student.getContactInfo().getEmails()) {
            if (emailAddress.equalsIgnoreCase(email.getAddress())) {
              /**
               * allowed if
               *  - contacttype of the email in use is non-unique (ie. used as contact person etc)
               *  - person that owns the used email is the same as the person receiving the address 
               */
              
              allowed = allowed && (email.getContactType().getNonUnique() || (student != null ? personId.equals(student.getPerson().getId()) : true));
            }
          }
        }
      }
      
      return allowed;
    } else {
      // True, if email is not in use
      return staffMember == null && students.isEmpty();
    }
  }
  
  public static Role getHighestPersonRole(Person person) {
    List<StaffMember> staffMembers = person.getStaffMembers();
    
    Role role = Role.EVERYONE;
    
    for (StaffMember staffMember : staffMembers) {
      if (isHigherOrEqualRole(staffMember.getRole(), role))
        role = staffMember.getRole();
    }
    
    return role;
  }
  
  public static List<Role> getRoleOrder() {
    List<Role> list = new ArrayList<>();

    list.add(Role.ADMINISTRATOR);
    list.add(Role.MANAGER);
    list.add(Role.STUDY_PROGRAMME_LEADER);
    list.add(Role.STUDY_GUIDER);
    list.add(Role.TEACHER);
    list.add(Role.USER);
    list.add(Role.STUDENT);
    list.add(Role.TRUSTED_SYSTEM);
    list.add(Role.GUEST);
    list.add(Role.EVERYONE);
    
    return list;
  }
  
  /**
   * Tests if r >= test when it comes to roles.
   * 
   * @param r
   * @param test
   * @return
   */
  public static boolean isHigherOrEqualRole(Role r, Role test) {
    if (r == test)
      return true;

    List<Role> order = getRoleOrder();
    
    int r_index = order.indexOf(r);
    int t_index = order.indexOf(test);
    
    return r_index != -1 ? r_index <= t_index : false;
  }
  
  public static boolean allowEditCredentials(User editor, Person whose) {
    return
        (editor.getRole() == Role.ADMINISTRATOR) ||
        (editor.getPerson().getId().equals(whose.getId())) ||
        ((editor.getRole() == Role.STUDY_PROGRAMME_LEADER) && (UserUtils.getHighestPersonRole(whose) != Role.MANAGER && UserUtils.getHighestPersonRole(whose) != Role.ADMINISTRATOR)) ||
        ((editor.getRole() == Role.MANAGER) && (UserUtils.getHighestPersonRole(whose) != Role.ADMINISTRATOR));
  }

  /**
   * Checks if the user is member of an organization.
   * 
   * Checks only the users' organization against given organization. Does not check if the
   * users' person has an user that might have access to the organization. Should it?
   * 
   * @param user
   * @param organization
   * @return
   */
  public static boolean isMemberOf(User user, Organization organization) {
    if (user == null || organization == null)
      return false;
    
    Organization userOrganization = user.getOrganization();

    return organization != null && userOrganization != null && organization.getId().equals(userOrganization.getId());
  }
 
  public static boolean canAccessAllOrganizations(User user) {
    return Permissions.instance().hasEnvironmentPermission(user, OrganizationPermissions.ACCESS_ALL_ORGANIZATIONS);
  }

  /**
   * Returns true if user can access all organizations or is member of the given organization.
   */
  public static boolean canAccessOrganization(User user, Organization organization) {
    return canAccessAllOrganizations(user) || isMemberOf(user, organization);
  }
  
  public static boolean isAdmin(User user) {
    return user != null && user.getRole() == Role.ADMINISTRATOR;
  }

  public static boolean isOwnerOf(User user, Person person) {
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null");
    }
    
    if (person == null) {
      throw new IllegalArgumentException("Person cannot be null");
    }
    
    return user.getPerson().getId().equals(person.getId());
  }
  
}
