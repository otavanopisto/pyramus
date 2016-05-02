package fi.otavanopisto.pyramus.json.projects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

public class SearchStudentProjectCoursesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();

    Integer resultsPerPage = NumberUtils.createInteger(requestContext.getRequest().getParameter("maxResults"));
    if (resultsPerPage == null) {
      resultsPerPage = 10;
    }

    Integer page = NumberUtils.createInteger(requestContext.getRequest().getParameter("page"));
    if (page == null) {
      page = 0;
    }

    String name = requestContext.getString("name");
    String tags = requestContext.getString("tags");
    
    SearchResult<Course> searchResult = courseDAO.searchCourses(resultsPerPage, page, name, tags, null, null, null, null, null, null, null, true);

    List<Map<String, Object>> results = new ArrayList<>();
    List<Course> courses = searchResult.getResults();
    for (Course course : courses) {
      Long studentCount = courseStudentDAO.countByCourse(course);
      Long maxStudentCount = course.getMaxParticipantCount();
      
      Map<String, Object> courseInfo = new HashMap<>();
      courseInfo.put("id", course.getId());
      courseInfo.put("name", course.getName());
      courseInfo.put("nameExtension", course.getNameExtension());
      courseInfo.put("moduleId", course.getModule().getId());
      if (course.getBeginDate() != null)
        courseInfo.put("beginDate", course.getBeginDate().getTime());
      if (course.getEndDate() != null)
        courseInfo.put("endDate", course.getEndDate().getTime());

      courseInfo.put("studentCount", studentCount);
      courseInfo.put("maxStudentCount", maxStudentCount);
      
      results.add(courseInfo);
    }

    String statusMessage;
    Locale locale = requestContext.getRequest().getLocale();
    if (searchResult.getTotalHitCount() > 0) {
      statusMessage = Messages.getInstance().getText(
          locale,
          "projects.searchStudentProjectCoursesDialog.searchStatus",
          new Object[] { searchResult.getFirstResult() + 1, searchResult.getLastResult() + 1,
              searchResult.getTotalHitCount() });
    }
    else {
      statusMessage = Messages.getInstance().getText(locale, "projects.searchStudentProjectCoursesDialog.searchStatusNoMatches");
    }

    requestContext.addResponseParameter("results", results);
    requestContext.addResponseParameter("statusMessage", statusMessage);
    requestContext.addResponseParameter("pages", searchResult.getPages());
    requestContext.addResponseParameter("page", searchResult.getPage());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
