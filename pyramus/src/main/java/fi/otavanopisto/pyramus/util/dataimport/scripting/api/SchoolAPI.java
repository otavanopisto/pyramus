package fi.otavanopisto.pyramus.util.dataimport.scripting.api;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolField;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;

public class SchoolAPI {
  
  public SchoolAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long create(String code, String name, Long schoolField, Long studentGroupId) {
    SchoolField schoolFieldEntity = null;
    BillingDetails billingDetails = null;
    StudentGroup studentGroupEntity = null;
    
    if (schoolField != null) {
      schoolFieldEntity = DAOFactory.getInstance().getSchoolFieldDAO().findById(schoolField);
    }

    if (studentGroupId != null) {
      studentGroupEntity = DAOFactory.getInstance().getStudentGroupDAO().findById(studentGroupId);
    }
    
    return (DAOFactory.getInstance().getSchoolDAO().create(code, name, schoolFieldEntity, studentGroupEntity, billingDetails).getId());
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
