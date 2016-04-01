package fi.otavanopisto.pyramus.json.courses;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStateDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import fi.otavanopisto.pyramus.persistence.search.SearchTimeFilterMode;

/**
 * The controller responsible of searching courses.
 * 
 * @see fi.otavanopisto.pyramus.views.modules.SearchCoursesViewController
 */
public class SearchCoursesJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to search courses.
   * The request should contain the either following parameters (for simple search):
   * <dl>
   *   <dt><code>text</code></dt>
   *   <dd>The text to search for</dd>
   * </dl>
   * or the following parameters (for advanced search):
   * <dl>
   *   <dt><code>name</code></dt>
   *   <dd>Course name to find.</dd>
   *   <dt><code>tags</code></dt>
   *   <dd>Tags to find.</dd>
   *   <dt><code>nameExtension</code></dt>
   *   <dd>The name extension to find.</dd>
   *   <dt><code>description</code></dt>
   *   <dd>The description to find.</dd>
   *   <dt><code>state</code></dt>
   *   <dd>The ID of the course state to find.</dd>
   *   <dt><code>subject</code></dt>
   *   <dd>The ID of the subject to find.</dd>
   *   <dt><code>timeframeStart</code></dt>
   *   <dd>The start of the timeframe to find.</dd>
   *   <dt><code>timeframeEnd</code></dt>
   *   <dd>The end of the timeframe to find.</dd>
   *   <dt><code>educationType</code></dt>
   *   <dd>The education type to find.</dd>
   *   <dt><code>educationSubtype</code></dt>
   *   <dd>The education subtype to find.</dd>
   *   <dt><code>timeframeMode</code></dt>
   *   <dd>The mode of the timeframe. Can be <code>INCLUSIVE</code>
   *   or <code>EXCLUSIVE</code>.</dd>
   * </dl>
   * 
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();    

    // Determine the number of results shown per page. If not defined, default to ten results per page

    Integer resultsPerPage = NumberUtils.createInteger(requestContext.getRequest().getParameter("maxResults"));
    if (resultsPerPage == null)
      resultsPerPage = 10;

    // Determine the result page to be shown. If not defined, default to the first page

    Integer page = NumberUtils.createInteger(requestContext.getRequest().getParameter("page"));
    if (page == null) {
      page = 0;
    }

    SearchResult<Course> searchResult;
    if ("advanced".equals(requestContext.getRequest().getParameter("activeTab"))) {
      
      String name = requestContext.getString("name");
      String tags = requestContext.getString("tags");
      if (!StringUtils.isBlank(tags))
        tags = tags.replace(',', ' ');
        
      String nameExtension = requestContext.getString("nameExtension");
      String description = requestContext.getString("description");

      CourseState courseState = null;
      Long courseStateId = requestContext.getLong("state");
      if (courseStateId != null) {
        courseState = courseStateDAO.findById(courseStateId);
      }

      Subject subject = null;
      Long subjectId = requestContext.getLong("subject");
      if (subjectId != null) {
        subject = subjectDAO.findById(subjectId);
      }

      Date timeframeStart = null;
      String value = requestContext.getString("timeframeStart");
      if (NumberUtils.isNumber(value)) {
        timeframeStart = new Date(NumberUtils.createLong(value));
      }

      Date timeframeEnd = null;
      value = requestContext.getString("timeframeEnd");
      if (NumberUtils.isNumber(value)) {
        timeframeEnd = new Date(NumberUtils.createLong(value));
      }
      
      EducationType educationType = null;
      Long educationTypeId = requestContext.getLong("educationType");
      if (educationTypeId != null) {
        educationType = educationTypeDAO.findById(educationTypeId);
      }

      EducationSubtype educationSubtype = null;
      Long educationSubtypeId = requestContext.getLong("educationSubtype");
      if (educationSubtypeId != null) {
        educationSubtype = educationSubtypeDAO.findById(educationSubtypeId);
      }
      
      SearchTimeFilterMode timeFilterMode = (SearchTimeFilterMode) requestContext.getEnum("timeframeMode", SearchTimeFilterMode.class);
      
      searchResult = courseDAO.searchCourses(resultsPerPage, page, name, tags, nameExtension, description, courseState,
          subject, timeFilterMode, timeframeStart, timeframeEnd, educationType, educationSubtype, true);
    }
    else {
      String text = requestContext.getRequest().getParameter("text");
      searchResult = courseDAO.searchCoursesBasic(resultsPerPage, page, text, true);
    }

    List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

    List<Course> courses = searchResult.getResults();
    for (Course course : courses) {
      Map<String, Object> courseInfo = new HashMap<String, Object>();
      courseInfo.put("id", course.getId());
      courseInfo.put("name", course.getName());
      courseInfo.put("nameExtension", course.getNameExtension());
      if (course.getBeginDate() != null) {
        courseInfo.put("beginDate", course.getBeginDate().getTime());
      }
      if (course.getEndDate() != null) {
        courseInfo.put("endDate", course.getEndDate().getTime());
      }
      results.add(courseInfo);
    }

    String statusMessage = "";
    Locale locale = requestContext.getRequest().getLocale();
    if (searchResult.getTotalHitCount() > 0) {
      statusMessage = Messages.getInstance().getText(
          locale,
          "courses.searchCourses.searchStatus",
          new Object[] { searchResult.getFirstResult() + 1, searchResult.getLastResult() + 1,
              searchResult.getTotalHitCount() });
    }
    else {
      statusMessage = Messages.getInstance().getText(locale, "courses.searchCourses.searchStatusNoMatches");
    }

    requestContext.addResponseParameter("results", results);
    requestContext.addResponseParameter("statusMessage", statusMessage);
    requestContext.addResponseParameter("pages", searchResult.getPages());
    requestContext.addResponseParameter("page", searchResult.getPage());
  }

  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
