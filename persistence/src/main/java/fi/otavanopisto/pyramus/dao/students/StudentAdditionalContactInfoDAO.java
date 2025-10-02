package fi.otavanopisto.pyramus.dao.students;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentAdditionalContactInfo;

@Stateless
public class StudentAdditionalContactInfoDAO extends PyramusEntityDAO<StudentAdditionalContactInfo> {

  public StudentAdditionalContactInfo create(ContactType contactType, boolean allowStudyDiscussions) {
    StudentAdditionalContactInfo contactInfo = new StudentAdditionalContactInfo();
    
    contactInfo.setContactType(contactType);
    contactInfo.setAllowStudyDiscussions(allowStudyDiscussions);
    
    return persist(contactInfo);
  }
  
  public StudentAdditionalContactInfo update(StudentAdditionalContactInfo contactInfo, ContactType contactType, boolean allowStudyDiscussions) {
    contactInfo.setContactType(contactType);
    contactInfo.setAllowStudyDiscussions(allowStudyDiscussions);
    return persist(contactInfo);
  }

}
