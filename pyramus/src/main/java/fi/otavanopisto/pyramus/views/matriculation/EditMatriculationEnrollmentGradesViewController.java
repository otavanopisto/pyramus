package fi.otavanopisto.pyramus.views.matriculation;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fi.internetix.smvc.Severity;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamSubjectSettingsDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationGradeDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubjectSettings;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationGrade;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.DateUtils;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;
import fi.otavanopisto.pyramus.util.ixtable.PyramusIxTableFacade;
import fi.otavanopisto.pyramus.util.ixtable.PyramusIxTableRowFacade;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EditMatriculationEnrollmentGradesViewController extends PyramusViewController {
  
  private static final Long NEW_ROW_ID = Long.valueOf(-1); 

  public void process(PageRequestContext pageRequestContext) {
    switch (pageRequestContext.getRequest().getMethod()) {
    case "GET":
      doGet(pageRequestContext);
      break;
    case "POST":
      doPost(pageRequestContext);
      break;
    }
  }
  
  public void doGet(PageRequestContext pageRequestContext) {
    MatriculationExamAttendanceDAO attendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    MatriculationExamSubjectSettingsDAO matriculationExamSubjectSettingsDAO = DAOFactory.getInstance().getMatriculationExamSubjectSettingsDAO();
    MatriculationGradeDAO matriculationGradeDAO = DAOFactory.getInstance().getMatriculationGradeDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();

    Long personId = pageRequestContext.getLong("person");
    Person person = personId != null ? personDAO.findById(personId) : null;

    if (personId == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Person not found");
      return;
    }

    MatriculationExamTerm term = MatriculationExamTerm.valueOf(pageRequestContext.getString("term"));
    int year = Integer.parseInt(pageRequestContext.getString("year"));

    // List the enrolled attendances by year+term
    List<MatriculationExamAttendance> enrolledAttendances = attendanceDAO.listBy(person, year, term, MatriculationExamAttendanceStatus.ENROLLED);

    // List given Grades by year+term
    List<MatriculationGrade> grades = matriculationGradeDAO.listBy(person, year, term);

    // Set of subjects from both lists
    Set<MatriculationExamSubject> subjects = Stream.concat(
        enrolledAttendances.stream().map(MatriculationExamAttendance::getSubject), 
        grades.stream().map(MatriculationGrade::getSubject))
      .filter(Objects::nonNull)
      .collect(Collectors.toSet());
    
    JSONArray jsonAttendances = new JSONArray();
    for (MatriculationExamSubject subject : subjects) {
      Date gradeDate = new Date();

      MatriculationGrade grade = matriculationGradeDAO.findBy(person, year, term, subject);
      if (grade != null) {
        // Grade exists, use the date from there
        gradeDate = DateUtils.toDate(grade.getGradeDate());
      }
      else {
        // No grade, so there should be an attendance - try to find the exam date from the subject's settings
        MatriculationExamAttendance enrolledAttendance = enrolledAttendances.stream()
          .filter(attendance -> attendance.getSubject() == subject)
          .findFirst()
          .orElse(null);

        if (enrolledAttendance != null && enrolledAttendance.getEnrollment() != null && enrolledAttendance.getEnrollment().getExam() != null) {
          MatriculationExamSubjectSettings subjectSettings = matriculationExamSubjectSettingsDAO.findBy(enrolledAttendance.getEnrollment().getExam(), subject);
          if (subjectSettings != null && subjectSettings.getExamDate() != null) {
            // If subject has a date set in settings, use that instead
            gradeDate = subjectSettings.getExamDate();
          }
        }
      }
      
      JSONObject obj = new JSONObject();
      obj.put("subject", subject.name());
      obj.put("gradeId", grade != null ? grade.getId() : NEW_ROW_ID);
      obj.put("grade", grade != null ? grade.getGrade().name() : null);
      obj.put("gradeDate", gradeDate != null ? gradeDate.getTime() : null);
      jsonAttendances.add(obj);
    }
    setJsDataVariable(pageRequestContext, "grades", jsonAttendances.toString());

    pageRequestContext.getRequest().setAttribute("person", person);
    pageRequestContext.getRequest().setAttribute("term", term);
    pageRequestContext.getRequest().setAttribute("year", year);
    
    pageRequestContext.setIncludeJSP("/templates/matriculation/matriculation-edit-grades.jsp");
  }

  private void doPost(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/matriculation/matriculation-edit-grades.jsp");

    Long personId = pageRequestContext.getLong("person");
    if (personId == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Person not found");
      return;
    }

    MatriculationGradeDAO matriculationGradeDAO = DAOFactory.getInstance().getMatriculationGradeDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    
    Person person = personDAO.findById(personId);
    StaffMember loggedUser = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());

    MatriculationExamTerm term = MatriculationExamTerm.valueOf(pageRequestContext.getString("term"));
    int year = Integer.parseInt(pageRequestContext.getString("year"));

    PyramusIxTableFacade gradesTable = PyramusIxTableFacade.from(pageRequestContext, "gradesTable");
    for (PyramusIxTableRowFacade gradeRow : gradesTable.rows()) {
      Long gradeId = gradeRow.getLong("gradeId");
      MatriculationExamSubject subject = MatriculationExamSubject.valueOf(gradeRow.getString("subject"));
      MatriculationExamGrade grade = gradeRow.getString("grade") != null ? MatriculationExamGrade.valueOf(gradeRow.getString("grade")) : null;
      LocalDate gradeDate = DateUtils.toLocalDate(gradeRow.getDate("gradeDate"));
      
      if (NEW_ROW_ID.equals(gradeId)) {
        if (grade != null) {
          matriculationGradeDAO.create(person, subject, year, term, grade, gradeDate, loggedUser);
        }
      }
      else {
        MatriculationGrade matriculationGrade = matriculationGradeDAO.findById(gradeId);
        if (matriculationGrade != null) {
          if (grade != null) {
            matriculationGradeDAO.update(matriculationGrade, grade, gradeDate, loggedUser);
          }
          else {
            matriculationGradeDAO.delete(matriculationGrade);
          }
        }
      }
    }

    pageRequestContext.setRedirectURL(pageRequestContext.getRequest().getRequestURI() + "?person=" + personId + "&term=" + term.name() + "&year=" + year);
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
