package fi.otavanopisto.pyramus.views.matriculation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamSubjectSettingsDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubjectSettings;
import fi.otavanopisto.pyramus.framework.DateUtils;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MatriculationExamSettingsViewController extends PyramusViewController {
  
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
  
  private void doGet(PageRequestContext pageRequestContext) {
    MatriculationExamDAO dao = DAOFactory.getInstance().getMatriculationExamDAO();
    MatriculationExamSubjectSettingsDAO matriculationExamSubjectSettingsDAO = DAOFactory.getInstance().getMatriculationExamSubjectSettingsDAO();

    MatriculationExam exam;
    if ("new".equals(pageRequestContext.getString("examId"))) {
      pageRequestContext.getRequest().setAttribute("examId", pageRequestContext.getString("examId"));
      exam = null;
    } else {
      Long examId = pageRequestContext.getLong("examId");
      exam = dao.findById(examId);
      pageRequestContext.getRequest().setAttribute("examId", examId);
    }
    pageRequestContext.getRequest().setAttribute("exam", exam);
    
    JSONArray subjectsJSON = new JSONArray();
    List<MatriculationExamSubject> matriculationExamSubjects = Arrays.asList(MatriculationExamSubject.values());
    matriculationExamSubjects.sort(Comparator.comparing(MatriculationExamSubject::toString));
    
    for (MatriculationExamSubject subject : matriculationExamSubjects) {
      MatriculationExamSubjectSettings subset = exam != null ? matriculationExamSubjectSettingsDAO.findBy(exam, subject) : null;
      
      JSONObject subjectJSON = new JSONObject();
      subjectJSON.put("subjectCode", subject);
      subjectJSON.put("examDate", (subset != null && subset.getExamDate() != null) ? subset.getExamDate().getTime() : null);
      subjectsJSON.add(subjectJSON);
    }
    setJsDataVariable(pageRequestContext, "subjectData", subjectsJSON.toString());

    pageRequestContext.setIncludeJSP("/templates/matriculation/management-enrollment-settings.jsp");
  }
  
  private void doPost(PageRequestContext pageRequestContext) {
    MatriculationExamDAO dao = DAOFactory.getInstance().getMatriculationExamDAO();
    MatriculationExamSubjectSettingsDAO matriculationExamSubjectSettingsDAO = DAOFactory.getInstance().getMatriculationExamSubjectSettingsDAO();
    
    Date starts = DateUtils.startOfDay(pageRequestContext.getDate("starts"));
    Date ends = DateUtils.endOfDay(pageRequestContext.getDate("ends"));
    Date confirmationDate = DateUtils.endOfDay(pageRequestContext.getDate("confirmationDate"));
    boolean enrollmentActive = Boolean.TRUE.equals(pageRequestContext.getBoolean("enrollmentActive"));
    
    Integer examYear = pageRequestContext.getInteger("examYear");
    MatriculationExamTerm examTerm = StringUtils.isNotBlank(pageRequestContext.getString("examTerm")) ? 
        MatriculationExamTerm.valueOf(pageRequestContext.getString("examTerm")) : null;

    MatriculationExam exam;
    if ("new".equals(pageRequestContext.getString("examId"))) {
      // Create new
      exam = dao.create(starts, ends, confirmationDate, examYear, examTerm, enrollmentActive);
    } else {
      Long examId = pageRequestContext.getLong("examId");
      exam = dao.findById(examId);
      exam = dao.update(exam, starts, ends, confirmationDate, examYear, examTerm, enrollmentActive);
    }
    
    Long subjectTableRowCount = pageRequestContext.getLong("subjectSettingsTable.rowCount");
    for (int i = 0; i < subjectTableRowCount; i++) {
      String colPrefix = "subjectSettingsTable." + i;
      MatriculationExamSubject subject = MatriculationExamSubject.valueOf(pageRequestContext.getString(colPrefix + ".subjectCode"));
      Date examDate = pageRequestContext.getDate(colPrefix + ".examDate");
      
      MatriculationExamSubjectSettings subjectSettings = matriculationExamSubjectSettingsDAO.findBy(exam, subject);
      if (subjectSettings == null) {
        matriculationExamSubjectSettingsDAO.create(exam, subject, examDate);
      } else {
        matriculationExamSubjectSettingsDAO.update(subjectSettings, examDate);
      }
    }
    
    pageRequestContext.setRedirectURL(
        String.format("%s/matriculation/settings.page?examId=%d", pageRequestContext.getRequest().getContextPath(), exam.getId()));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
