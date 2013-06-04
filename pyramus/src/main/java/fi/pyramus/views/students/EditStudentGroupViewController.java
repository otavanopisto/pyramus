package fi.pyramus.views.students;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentGroupDAO;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.students.StudentGroupStudent;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the Edit Student Group view of the application.
 * 
 * @see fi.pyramus.json.students.EditStudentGroupJSONRequestController
 */
public class EditStudentGroupViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();

    // The student group to be edited
    
    StudentGroup studentGroup = studentGroupDAO.findById(NumberUtils.createLong(pageRequestContext.getRequest().getParameter("studentgroup")));
    pageRequestContext.getRequest().setAttribute("studentGroup", studentGroup);

    List<StudentGroupStudent> studentGroupStudents = new ArrayList<StudentGroupStudent>(studentGroup.getStudents());
    Collections.sort(studentGroupStudents, new Comparator<StudentGroupStudent>() {
      @Override
      public int compare(StudentGroupStudent o1, StudentGroupStudent o2) {
        int cmp = o1.getStudent().getLastName().compareToIgnoreCase(o2.getStudent().getLastName());
        if (cmp == 0)
          cmp = o1.getStudent().getFirstName().compareToIgnoreCase(o2.getStudent().getFirstName());
        return cmp;
      }
    });
    
    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = studentGroup.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }
    
    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString());
    pageRequestContext.getRequest().setAttribute("studentGroupStudents", studentGroupStudents);
    
    pageRequestContext.setIncludeJSP("/templates/students/editstudentgroup.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Editing student groups is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "students.editStudentGroup.breadcrumb");
  }

}
