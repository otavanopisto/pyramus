package fi.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.grading.TransferCreditTemplateCourseDAO;
import fi.pyramus.dao.grading.TransferCreditTemplateDAO;
import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.grading.TransferCreditTemplate;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class CreateTransferCreditTemplateJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    TransferCreditTemplateDAO transferCreditTemplateDAO = DAOFactory.getInstance().getTransferCreditTemplateDAO();
    TransferCreditTemplateCourseDAO transferCreditTemplateCourseDAO = DAOFactory.getInstance().getTransferCreditTemplateCourseDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();

    String name = jsonRequestContext.getString("name");
    
    TransferCreditTemplate transferCreditTemplate = transferCreditTemplateDAO.create(name);
    
    int rowCount = jsonRequestContext.getInteger("coursesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "coursesTable." + i;
      
      String courseName = jsonRequestContext.getString(colPrefix + ".courseName"); 
      CourseOptionality courseOptionality = (CourseOptionality) jsonRequestContext.getEnum(colPrefix + ".courseOptionality", CourseOptionality.class);
      Integer courseNumber = jsonRequestContext.getInteger(colPrefix + ".courseNumber"); 
      Double courseLength = jsonRequestContext.getDouble(colPrefix + ".courseLength"); 
      Long subjectId = jsonRequestContext.getLong(colPrefix + ".subject"); 
      Long courseLengthUnitId = jsonRequestContext.getLong(colPrefix + ".courseLengthUnit"); 
      
      Subject subject = subjectDAO.findById(subjectId);
      EducationalTimeUnit courseLengthUnit = educationalTimeUnitDAO.findById(courseLengthUnitId);;
      
      transferCreditTemplateCourseDAO.create(transferCreditTemplate, courseName, courseNumber, courseOptionality, courseLength, courseLengthUnit, subject);
    }
    
    String redirectURL = jsonRequestContext.getRequest().getContextPath() + "/settings/edittransfercredittemplate.page?transferCreditTemplate=" + transferCreditTemplate.getId();
    
    jsonRequestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
