package fi.otavanopisto.pyramus.json.lukio;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class DeletePassFailGradeOptionJSONRequestController extends JSONRequestController {

  @Override
  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    Student student = studentDAO.findById(requestContext.getLong("studentId"));
    UserVariableKeyDAO studentVariableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    UserVariableKey studentVariableKey = studentVariableKeyDAO.findByVariableKey("lukioSmerkinta");
    UserVariableDAO studentVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    UserVariable studentVariable = studentVariableDAO.findByUserAndVariableKey(student, studentVariableKey);
    String value = requestContext.getString("subject");

    if (studentVariable != null) {
      String[] choices = studentVariable.getValue().split(",");
      List<String> newChoices = new ArrayList<String>();
      for (String choice : choices) {
        if (!choice.equals(value)) {
          newChoices.add(choice);
        }
      }
      if (newChoices.size() == 0) {
        studentVariableDAO.delete(studentVariable);
      } else {
        studentVariable.setValue(StringUtils.join(newChoices, ","));
      }
    }
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
