package fi.pyramus.framework;

import java.util.List;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.domainmodel.users.User;

public class UserUtils {

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
   */
  public static boolean isAllowedEmail(String emailAddress, ContactType contactType, Long personId) {
    // if Email is being put into non-unique field, it is always allowed    
    if (contactType.getNonUnique())
      return true;
    
    emailAddress = emailAddress != null ? emailAddress.trim() : null;
    
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();

    StaffMember staffMember = staffMemberDAO.findByUniqueEmail(emailAddress);
    List<Student> students = studentDAO.listByEmail(emailAddress);

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
      return (staffMember == null) && (students.size() == 0);
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
  
  public static boolean isHigherOrEqualRole(Role r, Role test) {
    // Administrator --> Manager --> User --> Student --> Trusted System --> Guest --> Everyone
    
    if (r == test)
      return true;
    
    switch (test) {
      case ADMINISTRATOR:
        return false; // Only true if role is administrator too, and checked before so we will never end up here

      case MANAGER:
        return (r == Role.ADMINISTRATOR);
      
      case USER:
        return (r == Role.ADMINISTRATOR) || (r == Role.MANAGER);
        
      case STUDENT:
        return (r == Role.ADMINISTRATOR) || (r == Role.MANAGER) || (r == Role.USER);
      
      case TRUSTED_SYSTEM:
        return (r == Role.ADMINISTRATOR) || (r == Role.MANAGER) || (r == Role.USER) || (r == Role.STUDENT);

      case GUEST:
        return (r == Role.ADMINISTRATOR) || (r == Role.MANAGER) || (r == Role.USER) || (r == Role.STUDENT) || (r == Role.TRUSTED_SYSTEM);
      
      case EVERYONE:
        return true; // Any other role is higher or equal to everyone
    }
    
    return false;
  }
  
  public static boolean allowEditCredentials(User editor, Person whose) {
    return
        (editor.getRole() == Role.ADMINISTRATOR) ||
        (editor.getPerson().getId().equals(whose.getId())) ||
        ((editor.getRole() == Role.MANAGER) && (UserUtils.getHighestPersonRole(whose) != Role.ADMINISTRATOR));
  }
  
}
