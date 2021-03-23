package fi.otavanopisto.pyramus.views.worklist;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class AssessmentInfoViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    WorklistItemDAO worklistItemDAO = DAOFactory.getInstance().getWorklistItemDAO();
    String itemIdStr = requestContext.getRequest().getParameter("itemId");
    Long itemId = NumberUtils.isNumber(itemIdStr) ? NumberUtils.createLong(itemIdStr) : null;
    WorklistItem worklistItem = worklistItemDAO.findById(itemId);
    if (worklistItem.getCourseAssessment() != null) {
      CourseAssessment courseAssessment = worklistItem.getCourseAssessment();
      
      // Student display name
      
      StringBuilder sb = new StringBuilder();
      Student student = courseAssessment.getStudent();
      sb.append(student.getFirstName());
      if (!StringUtils.isEmpty(student.getNickname())) {
        sb.append(String.format(" \"%s\"", student.getNickname()));
      }
      if (!StringUtils.isEmpty(student.getLastName())) {
        sb.append(String.format(" %s", student.getLastName()));
      }
      if (student.getStudyProgramme() != null) {
        sb.append(String.format(" (%s)", student.getStudyProgramme().getName()));
      }
      requestContext.getRequest().setAttribute("student", sb.toString());
      
      // Course display name
      
      sb = new StringBuilder();
      Course course = courseAssessment.getCourseStudent().getCourse();
      sb.append(course.getName());
      if (!StringUtils.isEmpty(course.getNameExtension())) {
        sb.append(String.format(" (%s)", course.getNameExtension()));
      }
      requestContext.getRequest().setAttribute("course", sb.toString());
      
      // Grade information
      
      requestContext.getRequest().setAttribute("grade", courseAssessment.getGrade().getName());
      List<CourseAssessment> assessments = courseAssessmentDAO.listByStudentAndCourse(student, course);
      assessments.sort(Comparator.comparing(CourseAssessment::getDate));
      boolean isRaise = assessments.size() > 1 && !Objects.equals(courseAssessment.getId(), assessments.get(0).getId()); 
      requestContext.getRequest().setAttribute("raise", Messages.getInstance().getText(
          requestContext.getRequest().getLocale(), isRaise ? "terms.yes" : "terms.no", null));
    }
    requestContext.setIncludeJSP("/templates/worklist/assessmentinfo.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
