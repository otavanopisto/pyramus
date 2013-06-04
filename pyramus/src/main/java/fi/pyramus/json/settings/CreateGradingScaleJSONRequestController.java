package fi.pyramus.json.settings;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.grading.GradeDAO;
import fi.pyramus.dao.grading.GradingScaleDAO;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of creating a new grading scale. 
 * 
 * @see fi.pyramus.views.settings.CreateGradingScaleViewController
 */
public class CreateGradingScaleJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to create a new grading scale.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    
    String name = jsonRequestContext.getRequest().getParameter("name");
    String description = jsonRequestContext.getRequest().getParameter("description");
    GradingScale gradingScale = gradingScaleDAO.create(name, description);

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("gradesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "gradesTable." + i;
      
      String gradeName = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");
      String gradeQualification = jsonRequestContext.getRequest().getParameter(colPrefix + ".qualification");
      Double gradeGPA = NumberUtils.createDouble(jsonRequestContext.getRequest().getParameter(colPrefix + ".GPA"));
      String gradeDescription = jsonRequestContext.getRequest().getParameter(colPrefix + ".description");
      Boolean passingGrade = "1".equals(jsonRequestContext.getRequest().getParameter(colPrefix + ".passingGrade"));
      
      gradeDAO.create(gradingScale, gradeName, gradeDescription, passingGrade, gradeGPA, gradeQualification);
    }
    
    String redirectURL = jsonRequestContext.getRequest().getContextPath() + "/settings/editgradingscale.page?gradingScaleId=" + gradingScale.getId();
    String refererAnchor = jsonRequestContext.getRefererAnchor();
    
    if (!StringUtils.isBlank(refererAnchor))
      redirectURL += "#" + refererAnchor;

    jsonRequestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
