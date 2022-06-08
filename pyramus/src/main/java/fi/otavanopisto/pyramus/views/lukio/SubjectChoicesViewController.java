package fi.otavanopisto.pyramus.views.lukio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageController;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * The controller responsible of the Logged User Info view of the application.
 * 
 */
public class SubjectChoicesViewController extends PyramusViewController implements PageController {

  private static final Map<String, String> VARIABLE_KEYS;
  
  static {
    VARIABLE_KEYS = new LinkedHashMap<String, String>();
    VARIABLE_KEYS.put("lukioMatematiikka", "Matematiikka");
    VARIABLE_KEYS.put("lukioAidinkieli",   "Äidinkieli");
    VARIABLE_KEYS.put("lukioUskonto",      "Uskonto");
    VARIABLE_KEYS.put("lukioKieliA",       "A-kieli");
//    VARIABLE_KEYS.put("lukioKieliA1",      "A1-kieli");
//    VARIABLE_KEYS.put("lukioKieliA2",      "A2-kieli");
    VARIABLE_KEYS.put("lukioKieliB1",      "B1-kieli");
    VARIABLE_KEYS.put("lukioKieliB2",      "B2-kieli");
    VARIABLE_KEYS.put("lukioKieliB3",      "B3-kieli");
  }
  
  /**
   * Processes the page request.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    Long studentId = new Long(0);
    if (pageRequestContext.getRequest().getParameter("studentId") != null) {
      studentId = Long.parseLong(pageRequestContext.getRequest().getParameter("studentId").toString());
    }
    
    JSONArray jsonSubjectChoices = new JSONArray();
    JSONArray jsonPassFailGradeOptions = new JSONArray();
    JSONArray jsonSubjectChoiceKeys = new JSONArray();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    UserVariableDAO studentVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    UserVariableKeyDAO studentVariableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    Student student = studentDAO.findById(studentId);
    String educationType;
    try {
      educationType = student.getStudyProgramme().getCategory().getEducationType().getCode();
    } catch (NullPointerException nullPointerException) {
      educationType = "";
    }
    List<UserVariableKey> studentVariableKeys = new ArrayList<UserVariableKey>();
    List<Subject> subjects = subjectDAO.listByEducationType(
        student.getStudyProgramme().getCategory().getEducationType());
    
    JSONArray jsonSubjects = new JSONArray();
    for (Subject subject : subjects) {
      String subjectName = subject.getName();
      if (!StringUtils.isEmpty(subject.getCode()))
        subjectName = subjectName + " (" + subject.getCode() + ")";
      
      JSONObject jsonSubject = new JSONObject();
      jsonSubject.put("id", subject.getId());
      jsonSubject.put("name", subjectName);
      jsonSubjects.add(jsonSubject);
    }
    
    for (String variableKey : VARIABLE_KEYS.keySet()) {
      UserVariableKey studentVariableKey = studentVariableKeyDAO.findByVariableKey(variableKey);
      if (studentVariableKey == null) {
        studentVariableKey = studentVariableKeyDAO.create(variableKey,
                                                          VARIABLE_KEYS.get(variableKey),
                                                          VariableType.TEXT,
                                                          false);
      }
      studentVariableKeys.add(studentVariableKey);
    }
    
    for (UserVariableKey studentVariableKey : studentVariableKeys) {
      // Key first
      JSONObject jsonSubjectChoiceKey = new JSONObject();
      jsonSubjectChoiceKey.put("id", studentVariableKey.getId());
      jsonSubjectChoiceKey.put("name", studentVariableKey.getVariableName());
      jsonSubjectChoiceKey.put("key", studentVariableKey.getVariableKey());
      jsonSubjectChoiceKeys.add(jsonSubjectChoiceKey);
      // Then variable
      UserVariable studentVariable = studentVariableDAO.findByUserAndVariableKey(student, studentVariableKey);
      if (studentVariable != null && !"".equals(studentVariable.getValue())) {
        for (String value : studentVariable.getValue().split(",")) {
          JSONObject jsonSubjectChoice = new JSONObject();
          jsonSubjectChoice.put("keyId", studentVariableKey.getId());
          jsonSubjectChoice.put("value", value);
          jsonSubjectChoice.put("id", studentVariable.getId());
          jsonSubjectChoices.add(jsonSubjectChoice);
        }
      }
    }
    
    UserVariableKey passFailGradeOptionKey = studentVariableKeyDAO.findByVariableKey("lukioSmerkinta");
    if (passFailGradeOptionKey == null) {
      passFailGradeOptionKey = studentVariableKeyDAO.create("lukioSmerkinta", "S-merkintä", VariableType.TEXT, false);
    }
    UserVariable passFailGradeOptionVariable = studentVariableDAO.findByUserAndVariableKey(student, passFailGradeOptionKey);
    if (passFailGradeOptionVariable != null && !"".equals(passFailGradeOptionVariable.getValue())) {
      for (String value : passFailGradeOptionVariable.getValue().split(",")) {
        JSONObject jsonPassFailGradeOption = new JSONObject();
        jsonPassFailGradeOption.put("keyId", passFailGradeOptionKey.getId());
        jsonPassFailGradeOption.put("value", value);
        jsonPassFailGradeOption.put("id", passFailGradeOptionVariable.getId());
        jsonPassFailGradeOptions.add(jsonPassFailGradeOption);
      }
    }
    
    this.setJsDataVariable(pageRequestContext, "studentName", student.getFullName());
    this.setJsDataVariable(pageRequestContext, "studentId", studentId.toString());
    this.setJsDataVariable(pageRequestContext, "educationType", educationType);
    this.setJsDataVariable(pageRequestContext, "subjects", jsonSubjects.toString());
    this.setJsDataVariable(pageRequestContext, "subjectChoices", jsonSubjectChoices.toString());
    this.setJsDataVariable(pageRequestContext, "subjectChoiceKeys", jsonSubjectChoiceKeys.toString());
    this.setJsDataVariable(pageRequestContext, "passFailGradeOptions", jsonPassFailGradeOptions.toString());
    pageRequestContext.setIncludeFtl("/plugin/lukio/ftl/subjectchoices.ftl");
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR, UserRole.STUDY_PROGRAMME_LEADER };
  }
  
}
