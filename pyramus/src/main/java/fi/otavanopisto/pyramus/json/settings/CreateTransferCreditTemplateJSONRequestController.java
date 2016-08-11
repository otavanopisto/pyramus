package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditTemplateCourseDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditTemplate;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class CreateTransferCreditTemplateJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    TransferCreditTemplateDAO transferCreditTemplateDAO = DAOFactory.getInstance().getTransferCreditTemplateDAO();
    TransferCreditTemplateCourseDAO transferCreditTemplateCourseDAO = DAOFactory.getInstance().getTransferCreditTemplateCourseDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();

    String name = jsonRequestContext.getString("name");

    Long templateCurriculumId = jsonRequestContext.getLong("curriculum");
    Curriculum templateCurriculum = templateCurriculumId != null ? curriculumDAO.findById(templateCurriculumId) : null;
    
    TransferCreditTemplate transferCreditTemplate = transferCreditTemplateDAO.create(name, templateCurriculum);
    
    int rowCount = jsonRequestContext.getInteger("coursesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "coursesTable." + i;
      
      String courseName = jsonRequestContext.getString(colPrefix + ".courseName"); 
      CourseOptionality courseOptionality = (CourseOptionality) jsonRequestContext.getEnum(colPrefix + ".courseOptionality", CourseOptionality.class);
      Integer courseNumber = jsonRequestContext.getInteger(colPrefix + ".courseNumber"); 
      Double courseLength = jsonRequestContext.getDouble(colPrefix + ".courseLength"); 
      Long subjectId = jsonRequestContext.getLong(colPrefix + ".subject"); 
      Long courseLengthUnitId = jsonRequestContext.getLong(colPrefix + ".courseLengthUnit"); 
      Long curriculumId = jsonRequestContext.getLong(colPrefix + ".curriculum"); 
      
      Subject subject = subjectDAO.findById(subjectId);
      EducationalTimeUnit courseLengthUnit = educationalTimeUnitDAO.findById(courseLengthUnitId);
      Curriculum curriculum = curriculumId != null ? curriculumDAO.findById(curriculumId) : null;
      
      transferCreditTemplateCourseDAO.create(transferCreditTemplate, courseName, courseNumber, courseOptionality, courseLength, courseLengthUnit, subject, curriculum);
    }
    
    String redirectURL = jsonRequestContext.getRequest().getContextPath() + "/settings/edittransfercredittemplate.page?transferCreditTemplate=" + transferCreditTemplate.getId();
    
    jsonRequestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
