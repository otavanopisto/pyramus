package fi.pyramus.services.entities.students;

import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.PhoneNumber;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;
import fi.pyramus.services.entities.base.AddressEntity;
import fi.pyramus.services.entities.base.LanguageEntity;
import fi.pyramus.services.entities.base.MunicipalityEntity;
import fi.pyramus.services.entities.base.NationalityEntity;
import fi.pyramus.services.entities.base.SchoolEntity;
import fi.pyramus.services.entities.base.StudyProgrammeEntity;

public class StudentEntityFactory implements EntityFactory<StudentEntity> {
  
  public StudentEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    Student student = (Student) domainObject;
    
    AbstractStudentEntity personEntity = EntityFactoryVault.buildFromDomainObject(student.getPerson());
    NationalityEntity nationalityEntity = EntityFactoryVault.buildFromDomainObject(student.getNationality());
    LanguageEntity languageEntity = EntityFactoryVault.buildFromDomainObject(student.getLanguage()); 
    MunicipalityEntity municipalityEntity = EntityFactoryVault.buildFromDomainObject(student.getMunicipality()); 
    SchoolEntity schoolEntity = EntityFactoryVault.buildFromDomainObject(student.getSchool());
    StudyProgrammeEntity studyProgramme = EntityFactoryVault.buildFromDomainObject(student.getStudyProgramme());
    
    String[] emails = new String[student.getContactInfo().getEmails().size()];
    int i = 0;
    for (Email email : student.getContactInfo().getEmails()) {
      emails[i++] = email.getAddress();
    }
    
    AddressEntity[] addresses = new AddressEntity[student.getContactInfo().getAddresses().size()];
    i = 0;
    for (Address address : student.getContactInfo().getAddresses()) {
      addresses[i++] = EntityFactoryVault.buildFromDomainObject(address);
    }
    
    // TODO phone number, parental info...
    
    String phoneNumberStr = null;
    for (PhoneNumber phoneNumber : student.getContactInfo().getPhoneNumbers()) {
      if (phoneNumber.getDefaultNumber()) {
        phoneNumberStr = phoneNumber.getNumber();
        break;
      }
    }
    
    String parentalInfo = student.getContactInfo().getAdditionalInfo();
    
    i = 0;
    String[] tags = new String[student.getTags().size()];
    for (Tag tag : student.getTags()) {
      tags[i++] = tag.getText();
    }
    
    return new StudentEntity(student.getId(), personEntity, emails, student.getFirstName(), student.getLastName(),
        tags, addresses, phoneNumberStr, student.getAdditionalInfo(), parentalInfo, student.getStudyTimeEnd(), 
        nationalityEntity, languageEntity, municipalityEntity, schoolEntity, studyProgramme, student.getArchived(), 
        student.getStudyStartDate(), student.getStudyEndDate());
  }
}
