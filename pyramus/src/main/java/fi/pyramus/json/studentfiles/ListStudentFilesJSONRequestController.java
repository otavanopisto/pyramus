package fi.pyramus.json.studentfiles;

import java.util.Collections;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.file.StudentFileDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.file.StudentFile;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of modifying an existing student group.
 * 
 * @see fi.pyramus.views.students.EditStudentGroupViewController
 */
public class ListStudentFilesJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to edit a student group.
   * 
   * @param requestContext
   *          The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();

    Long studentId = requestContext.getLong("studentId");
    Student student = studentDAO.findById(studentId);
    List<StudentFile> studentFiles = studentFileDAO.listByStudent(student);
    Collections.sort(studentFiles, new StringAttributeComparator("getName", true));
    
    JSONArray filesJSON = new JSONArray();
    
    for (StudentFile file : studentFiles) {
      JSONObject fileJSON = new JSONObject();
      fileJSON.put("id", file.getId());
      fileJSON.put("name", file.getName());
      fileJSON.put("fileTypeName", file.getFileType() != null ? file.getFileType().getName() : "");
      fileJSON.put("created", file.getCreated().getTime());
      fileJSON.put("lastModified", file.getLastModified().getTime());
      filesJSON.add(fileJSON);
    }
    
    requestContext.addResponseParameter("files", filesJSON.toString());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
