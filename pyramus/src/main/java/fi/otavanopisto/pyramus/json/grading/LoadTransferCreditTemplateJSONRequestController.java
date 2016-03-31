package fi.otavanopisto.pyramus.json.grading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditTemplate;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditTemplateCourse;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of loading transfer credit templates.
 */
public class LoadTransferCreditTemplateJSONRequestController extends JSONRequestController {

  
  /**
   * Processes the request to load a transfer credit template.
   * The request should contain the either following parameters:
   * <dl>
   *   <dt><code>transferCreditTemplateId</code></dt>
   *   <dd>The ID of the transfer credit to archive.</dd>
   * </dl>
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    TransferCreditTemplateDAO transferCreditTemplateDAO = DAOFactory.getInstance().getTransferCreditTemplateDAO();

    Long transferCreditTemplateId = jsonRequestContext.getLong("transferCreditTemplateId");
    TransferCreditTemplate transferCreditTemplate = transferCreditTemplateDAO.findById(transferCreditTemplateId);
    
    List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
    
    for (TransferCreditTemplateCourse templateCourse : transferCreditTemplate.getCourses()) {
      Map<String, Object> result = new HashMap<String, Object>();
      
      String subjectName = templateCourse.getSubject().getName();
      String subjectCode = templateCourse.getSubject().getCode();
      String subjectEducationType = templateCourse.getSubject().getEducationType() != null ? templateCourse.getSubject().getEducationType().getName() : null;
      
      String localizedSubject = subjectName;
      
      if ((subjectCode != null) && (subjectEducationType != null)) {
        localizedSubject = Messages.getInstance().getText(jsonRequestContext.getRequest().getLocale(), 
            "generic.subjectFormatterWithEducationType", new Object[] {
          subjectCode,
          subjectName,
          subjectEducationType
        });
      } else if (subjectEducationType != null) {
        localizedSubject = Messages.getInstance().getText(jsonRequestContext.getRequest().getLocale(), 
            "generic.subjectFormatterNoSubjectCode", new Object[] {
          subjectName,
          subjectEducationType
        });
      } else if (subjectCode != null) {
        localizedSubject = Messages.getInstance().getText(jsonRequestContext.getRequest().getLocale(), 
            "generic.subjectFormatterNoEducationType", new Object[] {
          subjectCode,
          subjectName
        });
      }
      
      result.put("courseId", templateCourse.getId());
      result.put("courseUnits", templateCourse.getCourseLength().getUnits());
      result.put("courseUnit", templateCourse.getCourseLength().getUnit().getId());
      result.put("courseName", templateCourse.getCourseName());
      result.put("courseNumber", templateCourse.getCourseNumber());
      result.put("courseOptionality", templateCourse.getOptionality().name());
      result.put("subjectId", templateCourse.getSubject().getId());
      result.put("subjectName", localizedSubject);

      results.add(result);
    }
    
    jsonRequestContext.addResponseParameter("results", results);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
