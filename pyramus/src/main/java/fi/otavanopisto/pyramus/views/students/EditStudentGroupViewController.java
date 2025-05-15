package fi.otavanopisto.pyramus.views.students;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusRequestControllerAccess;
import fi.otavanopisto.pyramus.framework.PyramusViewController2;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.security.impl.Permissions;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;
import fi.otavanopisto.pyramus.views.PyramusViewPermissions;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * The controller responsible of the Edit Student Group view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.students.EditStudentGroupJSONRequestController
 */
public class EditStudentGroupViewController extends PyramusViewController2 implements Breadcrumbable {

  public EditStudentGroupViewController() {
    super(
        PyramusRequestControllerAccess.REQUIRELOGIN // access
    );
  }

  @Override
  protected boolean checkAccess(RequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();

    StaffMember loggedUser = staffMemberDAO.findById(requestContext.getLoggedUserId());
    StudentGroup studentGroup = studentGroupDAO.findById(requestContext.getLong("studentgroup"));

    return 
        Permissions.instance().hasEnvironmentPermission(loggedUser, PyramusViewPermissions.EDIT_STUDENTGROUP) &&
        UserUtils.canAccessOrganization(loggedUser, studentGroup.getOrganization());
  }
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();

    // The student group to be edited
    
    StudentGroup studentGroup = studentGroupDAO.findById(pageRequestContext.getLong("studentgroup"));
    pageRequestContext.getRequest().setAttribute("studentGroup", studentGroup);

    List<StudentGroupStudent> studentGroupStudents = new ArrayList<>(studentGroup.getStudents());
    
    // Filter archived students from the list
    List<StudentGroupStudent> removableStudentGroupStudents = new ArrayList<>();
    if (!studentGroupStudents.isEmpty()) {
      for (StudentGroupStudent studentGroupStudent : studentGroupStudents) {
        Student student = studentGroupStudent.getStudent();
        
        if (student.getArchived() || student == null) {
          removableStudentGroupStudents.add(studentGroupStudent);
        }
      }
      studentGroupStudents.removeAll(removableStudentGroupStudents);
    }
    
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
    
    JSONArray studentGroupStudentsJSON = new JSONArray();
    
    for (StudentGroupStudent studentGroupStudent : studentGroupStudents) {
      if (studentGroupStudent.getStudent() != null) {
        JSONObject obj = new JSONObject();
        obj.put("id", studentGroupStudent.getId().toString());
        obj.put("studentId", studentGroupStudent.getStudent().getId().toString());
        obj.put("personId", studentGroupStudent.getStudent().getPerson().getId().toString());
        obj.put("firstName", studentGroupStudent.getStudent().getFirstName());
        obj.put("lastName", studentGroupStudent.getStudent().getLastName());
        
        List<Student> studyProgrammes = getPersonStudyProgrammes(studentGroupStudent.getStudent().getPerson());
  
        JSONArray studentStudyProgrammeJSON = new JSONArray();
        
        for (Student student : studyProgrammes) {
          JSONObject spJSON = new JSONObject();
          String studyProgrammeName;
          
          if (student.getStudyProgramme() != null)
            studyProgrammeName = student.getStudyProgramme().getName();
          else
            studyProgrammeName = Messages.getInstance().getText(pageRequestContext.getRequest().getLocale(), "students.editStudent.noStudyProgrammeDropDownItemLabel");
  
          if (student.getStudyEndDate() != null) {
            studyProgrammeName += " *";
          }
          
          spJSON.put("studentId", student.getId());
          spJSON.put("studyProgrammeName", studyProgrammeName);
          
          studentStudyProgrammeJSON.add(spJSON);
        }
      
        obj.put("studyProgrammes", studentStudyProgrammeJSON.toString());
        
        studentGroupStudentsJSON.add(obj);
      }
    }
    
    setJsDataVariable(pageRequestContext, "studentGroupStudents", studentGroupStudentsJSON.toString());
    
    Long loggedUserId = pageRequestContext.getLoggedUserId();
    StaffMember user = staffMemberDAO.findById(loggedUserId);
    
    List<Organization> organizations;
    if (UserUtils.canAccessAllOrganizations(user)) {
      organizations = organizationDAO.listUnarchived();
    } else {
      organizations = Arrays.asList(user.getOrganization());
    }
    
    Collections.sort(organizations, new StringAttributeComparator("getName"));
    pageRequestContext.getRequest().setAttribute("organizations", organizations);

    pageRequestContext.setIncludeJSP("/templates/students/editstudentgroup.jsp");
  }

  private List<Student> getPersonStudyProgrammes(Person person) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    
    List<Student> students = studentDAO.listByPerson(person);
    Collections.sort(students, new Comparator<Student>() {
      @Override
      public int compare(Student o1, Student o2) {
        /**
         * Ordering study programmes as follows
         *  1. studies that have start date but no end date (ongoing)
         *  2. studies that have no start nor end date
         *  3. studies that have ended
         *  4. studies that are archived
         *  5. other
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
              o2.getStudyStartDate().compareTo(o1.getStudyStartDate()) : 0; 
        } else
          return o1class < o2class ? -1 : o1class == o2class ? 0 : 1;
      }
    });
    
    return students;
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
