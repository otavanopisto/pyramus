package fi.otavanopisto.pyramus.views.matriculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.internetix.smvc.Severity;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.ProjectAssessmentDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamSubjectSettingsDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectModuleDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.TSB;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.grading.ProjectAssessment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.DegreeType;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubjectSettings;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamTerm;
import fi.otavanopisto.pyramus.domainmodel.matriculation.SchoolType;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectModule;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditEnrollmentViewController extends PyramusViewController {
  
  private static final Long NEW_ROW_ID = new Long(-1); 
  
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
  
  private void doPost(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/matriculation/management-edit-enrollment.jsp");
    Long id = pageRequestContext.getLong("enrollment");
    if (id == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    MatriculationExamDAO examDAO = DAOFactory.getInstance().getMatriculationExamDAO();
    MatriculationExamEnrollmentDAO enrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamAttendanceDAO attendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    MatriculationExamEnrollment enrollment = enrollmentDAO.findById(id);
    if (enrollment == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }
    
    StaffMember loggedUser = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());

    MatriculationExamEnrollmentState enrollmentState = MatriculationExamEnrollmentState.valueOf(
        pageRequestContext.getString("state"));
    
    enrollment = enrollmentDAO.update(
      enrollment,
      ObjectUtils.firstNonNull(pageRequestContext.getString("name"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("ssn"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("email"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("phone"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("address"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("postalCode"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("postalOffice"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("guidanceCounselor"), ""),
      SchoolType.valueOf(pageRequestContext.getString("enrollAs")),
      DegreeType.valueOf(pageRequestContext.getString("degreeType")),
      ObjectUtils.firstNonNull(pageRequestContext.getInteger("numMandatoryCourses"), 0),
      pageRequestContext.getBoolean("restartExam"),
      ObjectUtils.firstNonNull(pageRequestContext.getString("location"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("message"), ""),
      pageRequestContext.getBoolean("canPublishName"),
      enrollment.getStudent(),
      enrollmentState 
    );
    
    Integer enrolledAttendances = pageRequestContext.getInteger("enrolledAttendances.rowCount");
    if (enrolledAttendances == null) {
      enrolledAttendances = 0;
    }
    for (int i=0; i<enrolledAttendances; i++) {
      Long attendanceId = pageRequestContext.getLong("enrolledAttendances." + i + ".attendanceId");
      attendanceId = attendanceId != null ? attendanceId : -1;
      
      MatriculationExamSubject subject =
        MatriculationExamSubject.valueOf(pageRequestContext.getString("enrolledAttendances." + i + ".subject"));
      boolean mandatory =
        "MANDATORY".equals(pageRequestContext.getString("enrolledAttendances." + i + ".mandatority"));
      boolean repeat =
        "REPEAT".equals(pageRequestContext.getString("enrolledAttendances." + i + ".repeat"));
      
      MatriculationExamAttendance examAttendance;
      if (NEW_ROW_ID.equals(attendanceId)) {
        // NOTE: new ENROLLED attendances are tied to the year and term defined in the exam properties
        MatriculationExam matriculationExam = examDAO.get();
        Integer year = matriculationExam.getExamYear();
        MatriculationExamTerm term = matriculationExam.getExamTerm();
        
        examAttendance = attendanceDAO.create(enrollment, subject, mandatory, repeat,
            year, term, MatriculationExamAttendanceStatus.ENROLLED, null);
      } else {
        examAttendance = attendanceDAO.findById(attendanceId);
        examAttendance = attendanceDAO.update(examAttendance, enrollment, subject, mandatory, repeat,
            examAttendance.getYear(), examAttendance.getTerm(), examAttendance.getStatus(), examAttendance.getGrade());
      }
      
      if (enrollmentState == MatriculationExamEnrollmentState.APPROVED) {
        createOrUpdateStudentProject(examAttendance, enrollment.getStudent(), subject, mandatory, loggedUser);
      }
    }
    
    Integer finishedAttendances = pageRequestContext.getInteger("finishedAttendances.rowCount");
    if (finishedAttendances == null) {
      finishedAttendances = 0;
    }
    for (int i=0; i<finishedAttendances; i++) {
      Long attendanceId = pageRequestContext.getLong("finishedAttendances." + i + ".attendanceId");
      attendanceId = attendanceId != null ? attendanceId : -1;

      String termString = pageRequestContext.getString("finishedAttendances." + i + ".term");
      MatriculationExamTerm term = MatriculationExamTerm.valueOf(termString.substring(0, 6));
      int year = Integer.parseInt(termString.substring(6));
      MatriculationExamSubject subject =
        MatriculationExamSubject.valueOf(pageRequestContext.getString("finishedAttendances." + i + ".subject"));
      boolean mandatory =
        "MANDATORY".equals(pageRequestContext.getString("finishedAttendances." + i + ".mandatority"));
      MatriculationExamGrade grade =
        MatriculationExamGrade.valueOf(pageRequestContext.getString("finishedAttendances." + i + ".grade"));
      
      if (NEW_ROW_ID.equals(attendanceId)) {
        attendanceDAO.create(enrollment, subject, mandatory, null, year, term,
          MatriculationExamAttendanceStatus.FINISHED, grade);
      } else {
        MatriculationExamAttendance examAttendance = attendanceDAO.findById(attendanceId);
        attendanceDAO.update(examAttendance, enrollment, subject, mandatory, null, year, term,
            MatriculationExamAttendanceStatus.FINISHED, grade);
      }
    }
    
    Integer plannedAttendances = pageRequestContext.getInteger("plannedAttendances.rowCount");
    if (plannedAttendances == null) {
      plannedAttendances = 0;
    }
    for (int i=0; i<plannedAttendances; i++) {
      Long attendanceId = pageRequestContext.getLong("plannedAttendances." + i + ".attendanceId");
      attendanceId = attendanceId != null ? attendanceId : -1;

      String termString = pageRequestContext.getString("plannedAttendances." + i + ".term");
      MatriculationExamTerm term = MatriculationExamTerm.valueOf(termString.substring(0, 6));
      int year = Integer.parseInt(termString.substring(6));
      MatriculationExamSubject subject =
        MatriculationExamSubject.valueOf(pageRequestContext.getString("plannedAttendances." + i + ".subject"));
      boolean mandatory =
        "MANDATORY".equals(pageRequestContext.getString("plannedAttendances." + i + ".mandatority"));
      if (NEW_ROW_ID.equals(attendanceId)) {
        attendanceDAO.create(enrollment, subject, mandatory, null, year, term,
          MatriculationExamAttendanceStatus.PLANNED, null);
      } else {
        MatriculationExamAttendance examAttendance = attendanceDAO.findById(attendanceId);
        attendanceDAO.update(examAttendance, enrollment, subject, mandatory, null, year, term,
            MatriculationExamAttendanceStatus.PLANNED, null);
      }
    }
    
    pageRequestContext.setRedirectURL(pageRequestContext.getRequest().getRequestURI() + "?enrollment=" + id);
  }

  private void createOrUpdateStudentProject(MatriculationExamAttendance examAttendance, Student student, MatriculationExamSubject subject, boolean mandatory, StaffMember loggedUser) {
    ProjectAssessmentDAO projectAssessmentDAO = DAOFactory.getInstance().getProjectAssessmentDAO();
    StudentProjectDAO studentProjectDAO = DAOFactory.getInstance().getStudentProjectDAO();
    StudentProjectModuleDAO studentProjectModuleDAO = DAOFactory.getInstance().getStudentProjectModuleDAO();
    MatriculationExamDAO matriculationExamDAO = DAOFactory.getInstance().getMatriculationExamDAO();
    MatriculationExamSubjectSettingsDAO matriculationExamSubjectSettingsDAO = DAOFactory.getInstance().getMatriculationExamSubjectSettingsDAO();
    MatriculationExamAttendanceDAO matriculationExamAttendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();

    MatriculationExamSubjectSettings subjectSettings = matriculationExamSubjectSettingsDAO.findBy(subject);
    if (subjectSettings == null || subjectSettings.getProject() == null) {
      // We cannot really do anything if the settings aren't in place
      return;
    }
    
    CourseOptionality projectOptionality = mandatory ? CourseOptionality.MANDATORY : CourseOptionality.OPTIONAL;
    
    Project project = subjectSettings.getProject();
    StudentProject studentProject;
    
    if (examAttendance != null && 
        examAttendance.getProjectAssessment() != null && 
        BooleanUtils.isFalse(examAttendance.getProjectAssessment().getArchived()) &&
        examAttendance.getProjectAssessment().getStudentProject() != null &&
        BooleanUtils.isFalse(examAttendance.getProjectAssessment().getStudentProject().getArchived())) {
      // Use the studentproject from the projectassessment if it exists
      studentProject = examAttendance.getProjectAssessment().getStudentProject();
    } else {
      // Resolve studentProject from the project in the settings
      List<StudentProject> studentProjects = studentProjectDAO.listBy(student, project, TSB.IGNORE);

      // Find first non-archived project
      studentProject = studentProjects.stream()
          .filter(studentProject1 -> BooleanUtils.isFalse(studentProject1.getArchived()))
          .findFirst()
          .orElse(null);
      
      if (studentProject == null) {
        // No unarchived student project was found so try to use any other
        studentProject = studentProjects.isEmpty() ? null : studentProjects.get(0);
        
        if (studentProject != null && BooleanUtils.isTrue(studentProject.getArchived())) {
          studentProjectDAO.unarchive(studentProject);
        }
      }
    }
    
    if (studentProject == null) {
      // No matching student project was found so create a new one
      studentProject = studentProjectDAO.create(student, project.getName(), project.getDescription(), 
          project.getOptionalStudiesLength().getUnits(), project.getOptionalStudiesLength().getUnit(), projectOptionality, loggedUser, project);
      
      Set<Tag> tags = new HashSet<>();
      for (Tag tag : project.getTags()) {
        tags.add(tag);
      }
      studentProjectDAO.updateTags(studentProject, tags);
      
      List<ProjectModule> projectModules = project.getProjectModules();
      for (ProjectModule projectModule : projectModules) {
        studentProjectModuleDAO.create(studentProject, projectModule.getModule(), null,
            CourseOptionality.getOptionality(projectModule.getOptionality().getValue()));
      }
    } else {
      studentProject = studentProjectDAO.updateOptionality(studentProject, projectOptionality);
    }

    MatriculationExam matriculationExam = matriculationExamDAO.get();
    
    if (matriculationExam != null && matriculationExam.getSignupGrade() != null && subjectSettings.getExamDate() != null && examAttendance.getProjectAssessment() == null) {
      // Add the exam date
      ProjectAssessment projectAssessment = projectAssessmentDAO.create(studentProject, loggedUser, matriculationExam.getSignupGrade(), subjectSettings.getExamDate(), "");
      // Link the project assessment to this exam atten dance
      matriculationExamAttendanceDAO.updateProjectAssessment(examAttendance, projectAssessment);
    }
  }

  private void doGet(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/matriculation/management-edit-enrollment.jsp");
    
    Long id = pageRequestContext.getLong("enrollment");
    if (id == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }
    MatriculationExamEnrollmentDAO enrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamAttendanceDAO attendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();

    MatriculationExamEnrollment enrollment = enrollmentDAO.findById(id);
    if (enrollment == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }
    
    pageRequestContext.getRequest().setAttribute("enrollment", enrollment);
    pageRequestContext.getRequest().setAttribute("name", enrollment.getName());
    pageRequestContext.getRequest().setAttribute("ssn", enrollment.getSsn());
    pageRequestContext.getRequest().setAttribute("email", enrollment.getEmail());
    pageRequestContext.getRequest().setAttribute("phone", enrollment.getPhone());
    pageRequestContext.getRequest().setAttribute("address", enrollment.getAddress());
    pageRequestContext.getRequest().setAttribute("postalCode", enrollment.getPostalCode());
    pageRequestContext.getRequest().setAttribute("postalOffice", enrollment.getCity());
    pageRequestContext.getRequest().setAttribute("nationalStudentNumber", enrollment.getNationalStudentNumber());
    pageRequestContext.getRequest().setAttribute("guidanceCounselor", enrollment.getGuider());
    pageRequestContext.getRequest().setAttribute("enrollAs", enrollment.getEnrollAs().name());
    pageRequestContext.getRequest().setAttribute("numMandatoryCourses", enrollment.getNumMandatoryCourses());
    pageRequestContext.getRequest().setAttribute("restartExam", enrollment.isRestartExam());
    pageRequestContext.getRequest().setAttribute("location", enrollment.getLocation());
    pageRequestContext.getRequest().setAttribute("message", enrollment.getMessage());
    pageRequestContext.getRequest().setAttribute("canPublishName", enrollment.isCanPublishName());
    pageRequestContext.getRequest().setAttribute("state", enrollment.getState().name());
    
    List<MatriculationExamAttendance> attendances = attendanceDAO.listByEnrollment(enrollment);
    
    List<List<Object>> enrolledAttendances = new ArrayList<>();
    List<List<Object>> finishedAttendances = new ArrayList<>();
    List<List<Object>> plannedAttendances = new ArrayList<>();
    
    for (MatriculationExamAttendance attendance : attendances) {
      switch (attendance.getStatus()) {
      case ENROLLED:
        enrolledAttendances.add(
            Arrays.asList(
                attendance.getId(),
                attendance.getTerm().name() + attendance.getYear(),
                attendance.getSubject().name(),
                attendance.isMandatory() ? "MANDATORY" : "OPTIONAL",
                attendance.isRetry() ? "REPEAT" : "FIRST_TIME",
                (attendance.getProjectAssessment() != null && attendance.getProjectAssessment().getDate() != null) ? 
                    attendance.getProjectAssessment().getDate().getTime() : null,
                ""));
        break;
      case FINISHED:
        finishedAttendances.add(
            Arrays.asList(
                attendance.getId(),
                attendance.getTerm().name() + attendance.getYear(),
                attendance.getSubject().name(),
                attendance.isMandatory() ? "MANDATORY" : "OPTIONAL",
                attendance.getGrade().name(),
                ""));
        break;
      case PLANNED:
        plannedAttendances.add(
            Arrays.asList(
                attendance.getId(),
                attendance.getTerm().name() + attendance.getYear(),
                attendance.getSubject().name(),
                attendance.isMandatory() ? "MANDATORY" : "OPTIONAL",
                ""));
        break;
      default:
        break;
      }
      
      ObjectMapper om = new ObjectMapper();
      
      try {
        setJsDataVariable(
            pageRequestContext,
            "enrolledAttendances",
            om.writeValueAsString(enrolledAttendances));
        setJsDataVariable(
            pageRequestContext,
            "finishedAttendances",
            om.writeValueAsString(finishedAttendances));
        setJsDataVariable(
            pageRequestContext,
            "plannedAttendances",
            om.writeValueAsString(plannedAttendances));
      } catch (JsonProcessingException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
