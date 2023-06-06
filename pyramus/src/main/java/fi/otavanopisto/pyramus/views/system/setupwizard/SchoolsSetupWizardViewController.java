package fi.otavanopisto.pyramus.views.system.setupwizard;

import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolFieldDAO;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolField;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;

public class SchoolsSetupWizardViewController extends SetupWizardController {
  
  public SchoolsSetupWizardViewController() {
    super("schools");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException{
    SchoolFieldDAO schoolFieldDAO = DAOFactory.getInstance().getSchoolFieldDAO();
    List<SchoolField> schoolFields = schoolFieldDAO.listUnarchived();
    setJsDataVariable(requestContext, "schoolFields", new JSONArrayExtractor("name", "id").extractString(schoolFields));
  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    SchoolFieldDAO schoolFieldDAO = DAOFactory.getInstance().getSchoolFieldDAO();
    
    int rowCount = requestContext.getInteger("schoolsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "schoolsTable." + i;
      String code = requestContext.getString(colPrefix + ".code");
      String name = requestContext.getString(colPrefix + ".name");
      Long fieldId = requestContext.getLong(colPrefix + ".field");
      
      if (fieldId == null) {
        throw new SetupWizardException("School field is missing");
      }
      
      SchoolField schoolField = schoolFieldDAO.findById(fieldId);
      if (schoolField == null) {
        throw new SetupWizardException("School field is missing");
      }
      
      BillingDetails billingDetails = null;
      StudentGroup studentGroup = null;
      schoolDAO.create(code, name, schoolField, studentGroup, billingDetails);
    }    
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    return !schoolDAO.listUnarchived().isEmpty();
  }
  
}
