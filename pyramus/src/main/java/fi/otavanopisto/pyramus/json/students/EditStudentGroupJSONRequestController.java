package fi.otavanopisto.pyramus.json.students;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupStudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupUserDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController2;
import fi.otavanopisto.pyramus.framework.PyramusRequestControllerAccess;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.security.impl.Permissions;
import fi.otavanopisto.pyramus.views.PyramusViewPermissions;

/**
 * The controller responsible of modifying an existing student group.
 * 
 * @see fi.otavanopisto.pyramus.views.students.EditStudentGroupViewController
 */
public class EditStudentGroupJSONRequestController extends JSONRequestController2 {

  public EditStudentGroupJSONRequestController() {
    super(
        PyramusRequestControllerAccess.REQUIRELOGIN // access
    );
  }

  @Override
  protected boolean checkAccess(RequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();

    StaffMember loggedUser = staffMemberDAO.findById(requestContext.getLoggedUserId());
    StudentGroup studentGroup = studentGroupDAO.findById(requestContext.getLong("studentGroupId"));

    return 
        Permissions.instance().hasEnvironmentPermission(loggedUser, PyramusViewPermissions.EDIT_STUDENTGROUP) &&
        UserUtils.canAccessOrganization(loggedUser, studentGroup.getOrganization());
  }
  
  /**
   * Processes the request to edit a student group.
   * 
   * @param requestContext
   *          The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();
    StudentGroupStudentDAO studentGroupStudentDAO = DAOFactory.getInstance().getStudentGroupStudentDAO();
    StudentGroupUserDAO studentGroupUserDAO = DAOFactory.getInstance().getStudentGroupUserDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();

    // StudentGroup basic information

    String name = requestContext.getString("name");
    String description = requestContext.getString("description");
    Date beginDate = requestContext.getDate("beginDate");
    String tagsText = requestContext.getString("tags");
    Boolean guidanceGroup = requestContext.getBoolean("guidanceGroup");
    
    Set<Tag> tagEntities = new HashSet<>();
    if (!StringUtils.isBlank(tagsText)) {
      List<String> tags = Arrays.asList(tagsText.split("[\\ ,]"));
      for (String tag : tags) {
        if (!StringUtils.isBlank(tag)) {
          Tag tagEntity = tagDAO.findByText(tag.trim());
          if (tagEntity == null)
            tagEntity = tagDAO.create(tag);
          tagEntities.add(tagEntity);
        }
      }
    }

    StudentGroup studentGroup = studentGroupDAO.findById(requestContext.getLong("studentGroupId"));
    User loggedUser = staffMemberDAO.findById(requestContext.getLoggedUserId());

    if (!UserUtils.canAccessOrganization(loggedUser, studentGroup.getOrganization())) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Can not access student group from another organization.");
    }

    // Version check
    Long version = requestContext.getLong("version"); 
    if (!studentGroup.getVersion().equals(version))
      throw new StaleObjectStateException(StudentGroup.class.getName(), studentGroup.getId());
    
    Organization organization = organizationDAO.findById(requestContext.getLong("organizationId"));

    if (!UserUtils.canAccessOrganization(loggedUser, organization)) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Invalid organization.");
    }
    
    studentGroupDAO.update(studentGroup, organization, name, description, beginDate, guidanceGroup, loggedUser);

    // Tags

    studentGroupDAO.setStudentGroupTags(studentGroup, tagEntities);
    
    // Personnel

    StudentGroupUser[] users = studentGroup.getUsers().toArray(new StudentGroupUser[0]);
    StudentGroupStudent[] students = studentGroup.getStudents().toArray(new StudentGroupStudent[0]);

    Set<Long> removables = studentGroup.getUsers().stream()
        .map(StudentGroupUser::getId)
        .collect(Collectors.toSet());
    
    int rowCount = requestContext.getInteger("usersTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "usersTable." + i;
      Long userId = requestContext.getLong(colPrefix + ".userId");
      Long studentGroupUserId = requestContext.getLong(colPrefix + ".studentGroupUserId");
      StaffMember staffMember = staffMemberDAO.findById(userId);

      if (!UserUtils.canAccessOrganization(loggedUser, staffMember.getOrganization())) {
        throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Can not access student group from another organization.");
      }
      
      if (studentGroupUserId == null) {
        // New User
        studentGroupUserDAO.create(studentGroup, staffMember, loggedUser);
      } else {
        // Old User, still in list
        removables.remove(studentGroupUserId);
      }
    }

    // Remove the ones that were deleted from group
    for (int i = 0; i < users.length; i++) {
      if (removables.contains(users[i].getId())) {
        studentGroupUserDAO.remove(studentGroup, users[i], loggedUser);
      }
    }

    // Students

    removables = studentGroup.getStudents().stream()
        .map(StudentGroupStudent::getId)
        .collect(Collectors.toSet());

    rowCount = requestContext.getInteger("studentsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studentsTable." + i;

      Long studentId = requestContext.getLong(colPrefix + ".studentId");
      Long studentGroupStudentId = requestContext.getLong(colPrefix + ".studentGroupStudentId");
      Student student = studentDAO.findById(studentId);

      if (!UserUtils.canAccessOrganization(loggedUser, student.getOrganization())) {
        throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Can not access student from another organization.");
      }
      
      if (studentGroupStudentId == null) {
        // New Student
        studentGroupStudentDAO.create(studentGroup, student, loggedUser);
      } else {
        // Old User, still in list, we'll update if the student has changed student group
        removables.remove(studentGroupStudentId);
        
        StudentGroupStudent sgStudent = studentGroupStudentDAO.findById(studentGroupStudentId);
        if (!sgStudent.getStudent().getId().equals(studentId)) {
          studentGroupStudentDAO.update(sgStudent, studentDAO.findById(studentId), loggedUser);
        }
      }
    }

    // Remove the ones that were deleted from group
    for (int i = 0; i < students.length; i++) {
      if (removables.contains(students[i].getId())) {
        studentGroupStudentDAO.remove(studentGroup, students[i], loggedUser);
      }
    }
    
    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

}
