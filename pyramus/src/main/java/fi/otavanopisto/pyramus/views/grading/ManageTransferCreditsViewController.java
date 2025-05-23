package fi.otavanopisto.pyramus.views.grading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.PyramusConsts;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.grading.GradingScaleDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditTemplateDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditTemplate;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;
import net.sf.json.JSONObject;

/**
 * The controller responsible of the Manage Transfer Credits view of the application.
 */
public class ManageTransferCreditsViewController extends PyramusViewController implements Breadcrumbable {

  private static final Logger logger = Logger.getLogger(ManageTransferCreditsViewController.class.getName());
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    TransferCreditTemplateDAO transferCreditTemplateDAO = DAOFactory.getInstance().getTransferCreditTemplateDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
    
    Long studentId = pageRequestContext.getLong("studentId");
    
    Student student = studentDAO.findById(studentId);
    List<TransferCredit> transferCredits = transferCreditDAO.listByStudent(student);
    List<GradingScale> gradingScales = gradingScaleDAO.listUnarchived();
    String studentSSNHash = (student.getPerson() != null && StringUtils.isNotBlank(student.getPerson().getSocialSecurityNumber()))
        ? DigestUtils.sha256Hex(student.getPerson().getSocialSecurityNumber()) : null;
    
    List<EducationalTimeUnit> timeUnits = educationalTimeUnitDAO.listUnarchived();
    Collections.sort(timeUnits, new StringAttributeComparator("getName"));

    List<TransferCreditTemplate> transferCreditTemplates = transferCreditTemplateDAO.listAll();

    Collections.sort(transferCredits, new StringAttributeComparator("getCourseName", true));

    List<Curriculum> curriculums = curriculumDAO.listUnarchived();
    Collections.sort(curriculums, new StringAttributeComparator("getName"));
    String jsonCurriculums = new JSONArrayExtractor("name", "id").extractString(curriculums);

    setJsDataVariable(pageRequestContext, "curriculums", jsonCurriculums);
    
    // Grading scales used for importing credits from a json file - subset of all the grading scales

    List<GradingScale> importGradingScales = getImportGradingScales();

    JSONObject importGradeMapping = new JSONObject();
    importGradingScales.stream()
      .flatMap(gradingScale -> gradingScale.getGrades().stream())
      .forEach(grade -> importGradeMapping.put(grade.getName(), grade.getId()));
    setJsDataVariable(pageRequestContext, "importGradeMapping", importGradeMapping.toString());

    // TimeUnit mapping for importing credits
    
    JSONObject importTimeUnitMapping = new JSONObject();
    timeUnits.forEach(timeUnit -> importTimeUnitMapping.put(timeUnit.getSymbol(), timeUnit.getId()));
    setJsDataVariable(pageRequestContext, "importTimeUnitMapping", importTimeUnitMapping.toString());

    // Outbound variables
    
    pageRequestContext.getRequest().setAttribute("student", student);
    pageRequestContext.getRequest().setAttribute("transferCredits", transferCredits);
    pageRequestContext.getRequest().setAttribute("gradingScales", gradingScales);
    pageRequestContext.getRequest().setAttribute("timeUnits", timeUnits);
    pageRequestContext.getRequest().setAttribute("transferCreditTemplates", transferCreditTemplates);
    pageRequestContext.getRequest().setAttribute("studentSSNHash", studentSSNHash);
    
    pageRequestContext.setIncludeJSP("/templates/grading/managetransfercredits.jsp");
  }

  /**
   * Returns list of grading scales used for transfer credit import function.
   * 
   * @return list of grading scales used for transfer credit import function
   */
  private List<GradingScale> getImportGradingScales() {
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    
    SettingKey settingKey = settingKeyDAO.findByName(PyramusConsts.Setting.TRANSFERCREDITIMPORTS_GRADINGSCALES_LUKIO);
    if (settingKey != null) {
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting != null && StringUtils.isNotBlank(setting.getValue())) {
        try {
          Set<Long> gradingScaleIds = Arrays.stream(setting.getValue().split(","))
              .map(String::trim)
              .map(Long::parseLong)
              .collect(Collectors.toSet());
          
          if (!gradingScaleIds.isEmpty()) {
            List<GradingScale> importGradingScales = new ArrayList<>();
            for (Long gradingScaleId : gradingScaleIds) {
              importGradingScales.add(gradingScaleDAO.findById(gradingScaleId));
            }
            return importGradingScales;
          }
        } catch (NumberFormatException nfe) {
          logger.warning(String.format("Variable (%s) for grading scales used for transfer credit import contains invalid numbers.", 
              PyramusConsts.Setting.TRANSFERCREDITIMPORTS_GRADINGSCALES_LUKIO));
        }
      }
    }
    else {
      logger.warning(String.format("Variable (%s) for grading scales used for transfer credit import not defined.", 
          PyramusConsts.Setting.TRANSFERCREDITIMPORTS_GRADINGSCALES_LUKIO));
    }
    
    // Fallback to all unarchived
    return gradingScaleDAO.listUnarchived();
  }
  
  /**
   * Returns the roles allowed to access this page.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "grading.manageTransferCredits.pageTitle");
  }

}
