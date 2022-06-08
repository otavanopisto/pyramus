package fi.otavanopisto.pyramus.json.lukio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class SaveSubjectChoicesJSONRequestController extends JSONRequestController {

  @Override
  public void process(JSONRequestContext jsonRequestContext) {
    Map<Long, List<String> > choices = new HashMap< Long, List<String> >();
    Long studentId = new Long(-1);
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    UserVariableDAO studentVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    UserVariableKeyDAO studentVariableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    int rowCount = jsonRequestContext.getInteger("subjectChoicesTable.rowCount");
    
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "subjectChoicesTable." + i;
      Long subjectChoiceKeyId = jsonRequestContext.getLong(colPrefix + ".subject");
      String subjectChoiceValue = jsonRequestContext.getString(colPrefix + ".choice");
      studentId = jsonRequestContext.getLong(colPrefix + ".studentId");
      
      if (!choices.containsKey(subjectChoiceKeyId)) {
        choices.put(subjectChoiceKeyId, new ArrayList<String>());
      }
      
      choices.get(subjectChoiceKeyId).add(subjectChoiceValue);
    }
    
    for (Long subjectChoiceKeyId : choices.keySet()) {
      String value = StringUtils.join(choices.get(subjectChoiceKeyId), ",");
      Student student = studentDAO.findById(studentId);
      UserVariableKey variableKey = studentVariableKeyDAO.findById(subjectChoiceKeyId);
      UserVariable studentVariable = studentVariableDAO.findByUserAndVariableKey(student, variableKey);
      if (studentVariable == null) {
        studentVariable = studentVariableDAO.create(student, variableKey, value);
      } else {
        studentVariable.setValue(value);
      }
    }
    
    List<Long> passFailGradeOptions = new ArrayList<Long>();
    rowCount = jsonRequestContext.getInteger("passFailGradeOptionsTable.rowCount");
    for (int i=0; i < rowCount; i++) {
      String colPrefix = "passFailGradeOptionsTable." + i;
      Long subject = jsonRequestContext.getLong(colPrefix + ".subject"); 
      studentId = jsonRequestContext.getLong(colPrefix + ".studentId");
      
      passFailGradeOptions.add(subject);
    }
    
    if (passFailGradeOptions.size() > 0) {
      String value = StringUtils.join(passFailGradeOptions, ",");
      Student student = studentDAO.findById(studentId);
      UserVariableKey passFailGradeOptionsKey = studentVariableKeyDAO.findByVariableKey("lukioSmerkinta");
      UserVariable passFailGradeOptionsVariable = studentVariableDAO.findByUserAndVariableKey(student, passFailGradeOptionsKey);
      if (passFailGradeOptionsVariable == null) {
        passFailGradeOptionsVariable = studentVariableDAO.create(student, passFailGradeOptionsKey, value);
      } else {
        passFailGradeOptionsVariable.setValue(value);
      }
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  @Override
  public UserRole[] getAllowedRoles() {
    // TODO Auto-generated method stub
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
