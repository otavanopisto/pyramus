package fi.pyramus.views.students;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.AbstractStudentDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.students.StudentImageDAO;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * ViewController for viewing student information within popup dialog.
 * 
 * @author antti.leppa
 */
public class StudentInfoPopupViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Returns allowed roles for this page. Everyone is allowed to use this view.
   * 
   * @return allowed roles
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Processes the page request.
   * 
   * In parameters
   * - student
   * 
   * Page parameters
   * - student - Student object
   * - studentImage - url to student's image
   * - studentNationality - students nationality
   * - studentLanguage - students native language
   * - studentPhoneNumber - student phone number
   * 
   * @param pageRequestContext pageRequestContext
   */
  public void process(PageRequestContext pageRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    StudentImageDAO imageDAO = DAOFactory.getInstance().getStudentImageDAO();

    Long studentId = NumberUtils.createLong(pageRequestContext.getRequest().getParameter("student"));
    Long abstractStudentId = NumberUtils.createLong(pageRequestContext.getRequest().getParameter("abstractStudent"));
    
    AbstractStudent abstractStudent;

    if (abstractStudentId != null) {
    	abstractStudent = abstractStudentDAO.findById(abstractStudentId);
    } else {
      Student student = studentDAO.findById(studentId);
      abstractStudent = student.getAbstractStudent();
    }
  
    List<Student> students = studentDAO.listByAbstractStudent(abstractStudent);
    Collections.sort(students, new Comparator<Student>() {
      @Override
      public int compare(Student o1, Student o2) {
        /**
         * Ordering study programmes as follows
         *  1. studies that have start date but no end date (ongoing)
         *  2. studies that have no start nor end date
         *  3. studies that have ended
         *  4. studies that are archived
         */
        
        int o1class =
          (o1.getArchived()) ? 4:
            (o1.getStudyStartDate() != null && o1.getStudyEndDate() == null) ? 1:
              (o1.getStudyStartDate() == null && o1.getStudyEndDate() == null) ? 2:
                (o1.getStudyEndDate() != null) ? 3:
                  5;
        int o2class =
          (o2.getArchived()) ? 4:
            (o2.getStudyStartDate() != null && o2.getStudyEndDate() == null) ? 1:
              (o2.getStudyStartDate() == null && o2.getStudyEndDate() == null) ? 2:
                (o2.getStudyEndDate() != null) ? 3:
                  5;

        if (o1class == o2class) {
          // classes are the same, we try to do last comparison from the start dates
          return ((o1.getStudyStartDate() != null) && (o2.getStudyStartDate() != null)) ? 
              o1.getStudyStartDate().compareTo(o2.getStudyStartDate()) : 0; 
        } else
          return o1class < o2class ? -1 : o1class == o2class ? 0 : 1;
      }
    });
    
    String studentImage = pageRequestContext.getRequest().getContextPath() + "/gfx/default-user-image.png";
    
		pageRequestContext.getRequest().setAttribute("abstractStudent", abstractStudent);
    pageRequestContext.getRequest().setAttribute("students", students);
    pageRequestContext.getRequest().setAttribute("studentImage", studentImage);
    pageRequestContext.getRequest().setAttribute("latestStudentHasImage", abstractStudent.getLatestStudent() != null ? imageDAO.findStudentHasImage(abstractStudent.getLatestStudent()) : Boolean.FALSE);
  
    pageRequestContext.setIncludeJSP("/templates/students/studentinfopopup.jsp");
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * This view does not need a name because it's used as a content to popup dialog
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return "";
  }

}

