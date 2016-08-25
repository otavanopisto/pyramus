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
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditTemplateCourse;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditTransferCreditTemplateJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    TransferCreditTemplateDAO transferCreditTemplateDAO = DAOFactory.getInstance().getTransferCreditTemplateDAO();
    TransferCreditTemplateCourseDAO transferCreditTemplateCourseDAO = DAOFactory.getInstance().getTransferCreditTemplateCourseDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();

    Long transferCreditTemplateId = jsonRequestContext.getLong("transferCreditTemplateId");
    Long templateCurriculumId = jsonRequestContext.getLong("curriculum");
    String name = jsonRequestContext.getString("name");
    
    Curriculum templateCurriculum = templateCurriculumId != null ? curriculumDAO.findById(templateCurriculumId) : null;
    
    TransferCreditTemplate transferCreditTemplate;
    
    if (transferCreditTemplateId != null && transferCreditTemplateId >= 0) {
      transferCreditTemplate = transferCreditTemplateDAO.findById(transferCreditTemplateId);
      transferCreditTemplateDAO.update(transferCreditTemplate, name, templateCurriculum);
    } else {
      transferCreditTemplate = transferCreditTemplateDAO.create(name, templateCurriculum);
    }
    
    int rowCount = jsonRequestContext.getInteger("coursesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "coursesTable." + i;
      
      Long courseId = jsonRequestContext.getLong(colPrefix + ".courseId"); 
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
      
      TransferCreditTemplateCourse course;
      
      if (courseId != null && courseId > 0) {
        course = transferCreditTemplateCourseDAO.findById(courseId);
        transferCreditTemplateCourseDAO.update(course, courseName, courseNumber, courseOptionality, courseLength, courseLengthUnit, subject, curriculum);
      } else {
        course = transferCreditTemplateCourseDAO.create(transferCreditTemplate, courseName, courseNumber, courseOptionality, courseLength, courseLengthUnit, subject, curriculum);
      }
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
