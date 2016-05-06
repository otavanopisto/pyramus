package fi.otavanopisto.pyramus.views.students;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentGroupContactLogEntryCommentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupContactLogEntryDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupContactLogEntryComment;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * ViewController for managing student group contact log entries.
 */
public class ManageStudentGroupContactEntriesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Returns allowed roles for this page. Allowed are UserRole.MANAGER and UserRole.ADMINISTRATOR.
   * 
   * @return allowed roles
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  /**
   * Processes the page request.
   * 
   * In parameters
   * - person
   * 
   * Page parameters
   * - person - Person object
   * - contactEntries - List of StudentContactLogEntry objects
   * 
   * @param pageRequestContext pageRequestContext
   */
  public void process(PageRequestContext pageRequestContext) {
    StudentGroupContactLogEntryDAO logEntryDAO = DAOFactory.getInstance().getStudentGroupContactLogEntryDAO();
    StudentGroupContactLogEntryCommentDAO entryCommentDAO = DAOFactory.getInstance().getStudentGroupContactLogEntryCommentDAO();
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();

    Long studentGroupId = pageRequestContext.getLong("studentgroup");
    
    StudentGroup studentGroup = studentGroupDAO.findById(studentGroupId);
    
    pageRequestContext.getRequest().setAttribute("studentGroup", studentGroup);

    final Map<Long, List<StudentGroupContactLogEntryComment>> contactEntryComments = new HashMap<>();
    List<StudentGroupContactLogEntry> contactLogEntries = logEntryDAO.listByStudentGroup(studentGroup);

    // Populate comments for each entry to lists
    
    for (int j = 0; j < contactLogEntries.size(); j++) {
      StudentGroupContactLogEntry entry = contactLogEntries.get(j);
      
      List<StudentGroupContactLogEntryComment> listComments = entryCommentDAO.listByEntry(entry);
        
      Collections.sort(listComments, new Comparator<StudentGroupContactLogEntryComment>() {
        public int compare(StudentGroupContactLogEntryComment o1, StudentGroupContactLogEntryComment o2) {
          Date d1 = o1.getCommentDate();
          Date d2 = o2.getCommentDate();
          
          int val = d1 == null ? 
              d2 == null ? 0 : 1 :
                d2 == null ? -1 : d1.compareTo(d2);
          
          if (val == 0)
            return o1.getId().compareTo(o2.getId());
          else
            return val;
        }
      });
      
      contactEntryComments.put(entry.getId(), listComments);
    }
      
    // Now we can sort entries based on date of entry and/or dates of the comments on the entry
    
    Collections.sort(contactLogEntries, new Comparator<StudentGroupContactLogEntry>() {

      private Date getDateForEntry(StudentGroupContactLogEntry entry) {
        Date d = entry.getEntryDate();
        
        List<StudentGroupContactLogEntryComment> comments = contactEntryComments.get(entry.getId());
        
        for (int i = 0; i < comments.size(); i++) {
          StudentGroupContactLogEntryComment comment = comments.get(i);
          
          if (d == null) {
            d = comment.getCommentDate();
          } else {
            if (d.before(comment.getCommentDate()))
              d = comment.getCommentDate();
          }
        }
        
        return d;
      }
      
      public int compare(StudentGroupContactLogEntry o1, StudentGroupContactLogEntry o2) {
        Date d1 = getDateForEntry(o1);
        Date d2 = getDateForEntry(o2);

        int val = d1 == null ? 
            d2 == null ? 0 : 1 :
              d2 == null ? -1 : d2.compareTo(d1);

        if (val == 0)
          return o2.getId().compareTo(o1.getId());
        else
          return val;
      }
    });
      
    pageRequestContext.getRequest().setAttribute("contactEntries", contactLogEntries);
    pageRequestContext.getRequest().setAttribute("contactEntryComments", contactEntryComments);

    pageRequestContext.setIncludeJSP("/templates/students/managestudentgroupcontactentries.jsp");
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "students.manageStudentGroupContactEntries.pageTitle");
  }

}

