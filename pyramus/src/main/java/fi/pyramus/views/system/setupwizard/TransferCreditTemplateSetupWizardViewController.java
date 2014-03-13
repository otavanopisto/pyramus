package fi.pyramus.views.system.setupwizard;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AcademicTermDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.grading.TransferCreditTemplateCourseDAO;
import fi.pyramus.dao.grading.TransferCreditTemplateDAO;
import fi.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.grading.TransferCreditTemplate;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

public class TransferCreditTemplateSetupWizardViewController extends SetupWizardController {

  public TransferCreditTemplateSetupWizardViewController() {
    super("transfercredittemplates");
  }

  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();

    List<Subject> subjects = subjectDAO.listUnarchived();
    Collections.sort(subjects, new StringAttributeComparator("getName"));

    List<EducationalTimeUnit> timeUnits = educationalTimeUnitDAO.listUnarchived();
    Collections.sort(timeUnits, new StringAttributeComparator("getName"));

    List<School> schools = schoolDAO.listUnarchived();
    Collections.sort(schools, new StringAttributeComparator("getName"));
    
    String jsonSubjects = new JSONArrayExtractor("name", "id").extractString(subjects);
    String jsonTimeUnits = new JSONArrayExtractor("name", "id").extractString(timeUnits);

    this.setJsDataVariable(requestContext, "subjects", jsonSubjects);
    this.setJsDataVariable(requestContext, "timeUnits", jsonTimeUnits);
  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    TransferCreditTemplateDAO transferCreditTemplateDAO = DAOFactory.getInstance().getTransferCreditTemplateDAO();
    TransferCreditTemplateCourseDAO transferCreditTemplateCourseDAO = DAOFactory.getInstance().getTransferCreditTemplateCourseDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();

    String name = requestContext.getString("name");
    
    TransferCreditTemplate transferCreditTemplate = transferCreditTemplateDAO.create(name);
    
    int rowCount = requestContext.getInteger("coursesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "coursesTable." + i;
      
      String courseName = requestContext.getString(colPrefix + ".courseName"); 
      CourseOptionality courseOptionality = (CourseOptionality) requestContext.getEnum(colPrefix + ".courseOptionality", CourseOptionality.class);
      Integer courseNumber = requestContext.getInteger(colPrefix + ".courseNumber"); 
      Double courseLength = requestContext.getDouble(colPrefix + ".courseLength"); 
      Long subjectId = requestContext.getLong(colPrefix + ".subject"); 
      Long courseLengthUnitId = requestContext.getLong(colPrefix + ".courseLengthUnit"); 
      
      Subject subject = subjectDAO.findById(subjectId);
      EducationalTimeUnit courseLengthUnit = educationalTimeUnitDAO.findById(courseLengthUnitId);;
      
      transferCreditTemplateCourseDAO.create(transferCreditTemplate, courseName, courseNumber, courseOptionality, courseLength, courseLengthUnit, subject);
    }

  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    // TODO Auto-generated method stub
    return false;
  }

}
