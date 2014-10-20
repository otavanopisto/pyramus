package fi.pyramus.json.students;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.students.StudentGroupDAO;
import fi.pyramus.dao.students.StudentGroupStudentDAO;
import fi.pyramus.dao.students.StudentGroupUserDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class CreateStudentGroupJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();
    StudentGroupStudentDAO studentGroupStudentDAO = DAOFactory.getInstance().getStudentGroupStudentDAO();
    StudentGroupUserDAO studentGroupUserDAO = DAOFactory.getInstance().getStudentGroupUserDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();

    // StudentGroup basic information

    String name = requestContext.getString("name");
    String description = requestContext.getString("description");
    Date beginDate = requestContext.getDate("beginDate");
    String tagsText = requestContext.getString("tags");
    
    Set<Tag> tagEntities = new HashSet<Tag>();
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

    User loggedUser = userDAO.findById(requestContext.getLoggedUserId());

    StudentGroup studentGroup = studentGroupDAO.create(name, description, beginDate, loggedUser);

    // Tags
    
    studentGroupDAO.setStudentGroupTags(studentGroup, tagEntities);
    
    // Personnel

    int rowCount = requestContext.getInteger("usersTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "usersTable." + i;
      Long userId = requestContext.getLong(colPrefix + ".userId");
      User user = userDAO.findById(userId);
      
      studentGroupUserDAO.create(studentGroup, user, loggedUser);
    }

    // Students

    int studentsTableRowCount = requestContext.getInteger("studentsTable.rowCount");
    for (int i = 0; i < studentsTableRowCount; i++) {
      String colPrefix = "studentsTable." + i;

      Long studentId = requestContext.getLong(colPrefix + ".studentId");
      Student student = studentDAO.findById(studentId);
      
      studentGroupStudentDAO.create(studentGroup, student, loggedUser);
    }
    
    String redirectURL = requestContext.getRequest().getContextPath() + "/students/editstudentgroup.page?studentgroup=" + studentGroup.getId();
    String refererAnchor = requestContext.getRefererAnchor();
    
    if (!StringUtils.isBlank(refererAnchor))
      redirectURL += "#" + refererAnchor;

    requestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}