package fi.otavanopisto.pyramus.views.matriculation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.Severity;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamSubjectSettingsDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubjectSettings;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EditMatriculationEnrollmentGradesViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    MatriculationExamAttendanceDAO attendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    MatriculationExamEnrollmentDAO enrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamSubjectSettingsDAO matriculationExamSubjectSettingsDAO = DAOFactory.getInstance().getMatriculationExamSubjectSettingsDAO();
    
    Long id = pageRequestContext.getLong("enrollment");
    if (id == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }

    MatriculationExamEnrollment enrollment = enrollmentDAO.findById(id);
    if (enrollment == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }

    List<MatriculationExamAttendance> attendances = attendanceDAO.listByEnrollmentAndStatus(enrollment, MatriculationExamAttendanceStatus.ENROLLED);
    
    JSONArray jsonAttendances = new JSONArray();
    for (MatriculationExamAttendance attendance : attendances) {
      Date gradeDate = new Date();
      
      if (attendance.getSubject() != null) {
        MatriculationExamSubjectSettings subjectSettings = matriculationExamSubjectSettingsDAO.findBy(enrollment.getExam(), attendance.getSubject());
        if (subjectSettings != null && subjectSettings.getExamDate() != null) {
          // If subject has a date set in settings, use that instead of current date
          gradeDate = subjectSettings.getExamDate();
        }
      }
      
      JSONObject obj = new JSONObject();
      obj.put("attendanceId", attendance.getId());
      obj.put("subject", attendance.getSubject() != null ? attendance.getSubject().name() : null);
      obj.put("grade", attendance.getGrade() != null ? attendance.getGrade().name() : null);
      obj.put("gradeDate", gradeDate != null ? gradeDate.getTime() : null);
      jsonAttendances.add(obj);
    }
    setJsDataVariable(pageRequestContext, "attendances", jsonAttendances.toString());

    Messages messages = Messages.getInstance();
    Locale locale = pageRequestContext.getRequest().getLocale();
    
    List<TermOption> termOptions = new ArrayList<>();
    for (int year = Calendar.getInstance().get(Calendar.YEAR) + 4; year >= 2016; year--) {
      termOptions.add(new TermOption(MatriculationExamTerm.AUTUMN.name() + String.valueOf(year), messages.getText(locale, "terms.seasons.autumn") + " " + String.valueOf(year)));
      termOptions.add(new TermOption(MatriculationExamTerm.SPRING.name() + String.valueOf(year), messages.getText(locale, "terms.seasons.spring") + " " + String.valueOf(year)));
    }
    
    pageRequestContext.getRequest().setAttribute("enrollment", enrollment);
    pageRequestContext.getRequest().setAttribute("termOptions", termOptions);
    
    pageRequestContext.setIncludeJSP("/templates/matriculation/matriculation-edit-grades.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

  public class TermOption {
    public TermOption(String value, String displayText) {
      this.value = value;
      this.displayText = displayText;
    }
    
    public String getValue() {
      return value;
    }

    public String getDisplayText() {
      return displayText;
    }

    private final String value;
    private final String displayText;
  }
}
