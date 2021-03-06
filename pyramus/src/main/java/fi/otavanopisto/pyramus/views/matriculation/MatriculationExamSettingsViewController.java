package fi.otavanopisto.pyramus.views.matriculation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.GradeDAO;
import fi.otavanopisto.pyramus.dao.grading.GradingScaleDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamSubjectSettingsDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubjectSettings;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamTerm;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.framework.DateUtils;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
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
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();

    List<GradingScale> gradingScales = gradingScaleDAO.listUnarchived();
    pageRequestContext.getRequest().setAttribute("gradingScales", gradingScales);

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
      subjectJSON.put("projectId", (subset != null && subset.getProject() != null) ? subset.getProject().getId() : null);
      subjectJSON.put("examDate", (subset != null && subset.getExamDate() != null) ? subset.getExamDate().getTime() : null);
      subjectsJSON.add(subjectJSON);
    }
    setJsDataVariable(pageRequestContext, "subjectData", subjectsJSON.toString());

    List<Project> projects = projectDAO.listUnarchived();
    projects.sort(Comparator.comparing(Project::getName));
    
    JSONArray projectsJSON = new JSONArray();
    for (Project project : projects) {
      JSONObject projectJSON = new JSONObject();
      projectJSON.put("id", project.getId());
      projectJSON.put("name", project.getName());
      projectsJSON.add(projectJSON);
    }
    setJsDataVariable(pageRequestContext, "projectData", projectsJSON.toString());
    
    pageRequestContext.setIncludeJSP("/templates/matriculation/management-enrollment-settings.jsp");
  }
  
  private void doPost(PageRequestContext pageRequestContext) {
    MatriculationExamDAO dao = DAOFactory.getInstance().getMatriculationExamDAO();
    MatriculationExamSubjectSettingsDAO matriculationExamSubjectSettingsDAO = DAOFactory.getInstance().getMatriculationExamSubjectSettingsDAO();
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    
    Date starts = DateUtils.startOfDay(pageRequestContext.getDate("starts"));
    Date ends = DateUtils.endOfDay(pageRequestContext.getDate("ends"));
    boolean enrollmentActive = Boolean.TRUE.equals(pageRequestContext.getBoolean("enrollmentActive"));
    
    Long signupGradeId = pageRequestContext.getLong("signupGradeId");
    Grade signupGrade = signupGradeId != null ? gradeDAO.findById(signupGradeId) : null;
    Integer examYear = pageRequestContext.getInteger("examYear");
    MatriculationExamTerm examTerm = StringUtils.isNotBlank(pageRequestContext.getString("examTerm")) ? 
        MatriculationExamTerm.valueOf(pageRequestContext.getString("examTerm")) : null;

    MatriculationExam exam;
    if ("new".equals(pageRequestContext.getString("examId"))) {
      // Create new
      exam = dao.create(starts, ends, signupGrade, examYear, examTerm, enrollmentActive);
    } else {
      Long examId = pageRequestContext.getLong("examId");
      exam = dao.findById(examId);
      exam = dao.update(exam, starts, ends, signupGrade, examYear, examTerm, enrollmentActive);
    }
    
    Long subjectTableRowCount = pageRequestContext.getLong("subjectSettingsTable.rowCount");
    for (int i = 0; i < subjectTableRowCount; i++) {
      String colPrefix = "subjectSettingsTable." + i;
      MatriculationExamSubject subject = MatriculationExamSubject.valueOf(pageRequestContext.getString(colPrefix + ".subjectCode"));
      Long projectId = pageRequestContext.getLong(colPrefix + ".projectId");
      Project project = projectId != null ? projectDAO.findById(projectId) : null;
      Date examDate = pageRequestContext.getDate(colPrefix + ".examDate");
      
      MatriculationExamSubjectSettings subjectSettings = matriculationExamSubjectSettingsDAO.findBy(exam, subject);
      if (subjectSettings == null) {
        matriculationExamSubjectSettingsDAO.create(exam, subject, project, examDate);
      } else {
        matriculationExamSubjectSettingsDAO.update(subjectSettings, project, examDate);
      }
    }
    
    pageRequestContext.setRedirectURL(
        String.format("%s/matriculation/settings.page?examId=%d", pageRequestContext.getRequest().getContextPath(), exam.getId()));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
