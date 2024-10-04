package fi.otavanopisto.pyramus.json.students;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentVariableDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudentVariable;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudentVariableKey;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveCourseStudentVariablesJSONRequestController extends JSONRequestController {

  private static final String TODISTUSKEY = "todistusTehty";
  private static final String LASKUTUSKEY = "laskutus";

  @Override
  public void process(JSONRequestContext requestContext) {
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    CourseStudentVariableDAO courseStudentVariableDAO = DAOFactory.getInstance().getCourseStudentVariableDAO();
    CourseStudentVariableKeyDAO courseStudentVariableKeyDAO = DAOFactory.getInstance().getCourseStudentVariableKeyDAO();

    CourseStudentVariableKey todistusKey = courseStudentVariableKeyDAO.findByKey(TODISTUSKEY);
    if (todistusKey == null)
      todistusKey = courseStudentVariableKeyDAO.create(true, TODISTUSKEY, "Todistus tehty", VariableType.BOOLEAN);
    CourseStudentVariableKey laskutusKey = courseStudentVariableKeyDAO.findByKey(LASKUTUSKEY);
    if (laskutusKey == null)
      laskutusKey = courseStudentVariableKeyDAO.create(true, LASKUTUSKEY, "Laskutuksen kohde", VariableType.TEXT);
    
    Long rowCount = requestContext.getLong("internetixMarkerTable.rowCount");

    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "internetixMarkerTable." + i;
      
      Long courseStudentId = requestContext.getLong(colPrefix + ".courseStudentId");
      CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
      
      boolean todistusTehty = "1".equals(requestContext.getString(colPrefix + ".todistus"));
      String laskutusValue = requestContext.getString(colPrefix + ".laskutusmaarays");

      CourseStudentVariable todistus = courseStudentVariableDAO.findByCourseStudentAndKey(courseStudent, todistusKey);
      CourseStudentVariable laskutus = courseStudentVariableDAO.findByCourseStudentAndKey(courseStudent, laskutusKey);
      
      boolean todistusCurrent = todistus != null ? Boolean.valueOf(todistus.getValue()) : false;
      String laskutusCurrent = laskutus != null ? laskutus.getValue() : null;
      
      if (todistusTehty != todistusCurrent)
        courseStudentVariableDAO.setCourseStudentVariable(courseStudent, TODISTUSKEY, String.valueOf(todistusTehty));

      if (!StringUtils.equals(laskutusValue, laskutusCurrent))
        courseStudentVariableDAO.setCourseStudentVariable(courseStudent, LASKUTUSKEY, laskutusValue);
    }
    
    Date startDate = requestContext.getDate("startDate");
    startDate = startDate != null ? startDate : new Date();
    Date endDate = requestContext.getDate("endDate");
    endDate = endDate != null ? endDate : new Date();
    
    String redirect = requestContext.getReferer(false);
    
    if (redirect.contains("?")) {
      redirect = redirect.substring(0, redirect.indexOf('?'));
    }
    
    redirect += 
        "?startDate=" + startDate.getTime() +
        "&endDate=" + endDate.getTime();
    
    requestContext.setRedirectURL(redirect);
  }

  @Override
  public UserRole[] getAllowedRoles() {
   return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
