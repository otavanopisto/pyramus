package fi.otavanopisto.pyramus.services.entities.grading;

import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.services.entities.EntityFactory;
import fi.otavanopisto.pyramus.services.entities.EntityFactoryVault;
import fi.otavanopisto.pyramus.services.entities.users.UserEntity;

public class TransferCreditEntityFactory implements EntityFactory<TransferCreditEntity> {

  public TransferCreditEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    TransferCredit transferCredit = (TransferCredit) domainObject;
    GradeEntity grade = EntityFactoryVault.buildFromDomainObject(transferCredit.getGrade());
    UserEntity assessingUser = EntityFactoryVault.buildFromDomainObject(transferCredit.getAssessor());
    
    return new TransferCreditEntity(transferCredit.getId(), transferCredit.getStudent().getId(), transferCredit.getDate(), 
        grade, transferCredit.getVerbalAssessment(), assessingUser, transferCredit.getArchived(),
        transferCredit.getCourseName(), transferCredit.getCourseNumber(), transferCredit.getCourseLength().getUnits(),
        transferCredit.getCourseLength().getUnit().getId(), transferCredit.getSchool().getId(), transferCredit.getSubject().getId(),
        transferCredit.getOptionality().toString());
  }
}
