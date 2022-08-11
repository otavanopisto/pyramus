package fi.otavanopisto.pyramus.json.projects;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.ProjectAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.ProjectAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectModule;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectSubjectCourse;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ListStudentProjectsJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    StudentProjectDAO studentProjectDAO = DAOFactory.getInstance().getStudentProjectDAO();
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    ProjectAssessmentDAO projectAssessmentDAO = DAOFactory.getInstance().getProjectAssessmentDAO();
    
    Long projectId = requestContext.getLong("project");
    Project project = projectDAO.findById(projectId);

    Integer page = requestContext.getInteger("page");
    page = page != null ? page : 0;
    Integer maxResults = requestContext.getInteger("maxResults");
    maxResults = maxResults != null ? maxResults : 25;

    int firstResult = page * maxResults;
    long numStudentProjectsTotal = studentProjectDAO.countByProject(project);
    int numPages = (int) Math.ceil((double) numStudentProjectsTotal / maxResults);
    
    List<StudentProject> studentProjectsByProjectPage = studentProjectDAO.listByProject(project, firstResult, maxResults);
    int resultsCount = studentProjectsByProjectPage.size();
    
    JSONArray studentProjectsJSON = new JSONArray();
    for (StudentProject sp : studentProjectsByProjectPage) {
      StudentProjectBean bean = beanify(sp, courseDAO, courseStudentDAO, transferCreditDAO, courseAssessmentDAO, projectAssessmentDAO);
      
      JSONObject spJSON = new JSONObject();
      JSONArray spDates = new JSONArray();
      
      StringBuilder gradeText = new StringBuilder();
      
      for (ProjectAssessment assessment : bean.getAssessments()) {
        spDates.add(assessment.getDate().getTime());
        
        if (gradeText.length() > 0)
          gradeText.append(", ");
        
        if (assessment.getGrade() != null)
          gradeText.append(assessment.getGrade().getName());
        else
          gradeText.append("-");
      }
      
      spJSON.put("personId", bean.getStudentProject().getStudent().getPerson().getId());
      spJSON.put("studentProjectId", bean.getStudentProject().getId());
      spJSON.put("studentName", bean.getStudentProject().getStudent().getLastName() + ", " + bean.getStudentProject().getStudent().getFirstName());
      spJSON.put("studyProgrammeName", bean.getStudentProject().getStudent().getStudyProgramme().getName());
      spJSON.put("optionality", bean.getStudentProject().getOptionality());
      spJSON.put("dates", spDates);
      spJSON.put("grade", gradeText.toString());
      spJSON.put("passedMandatory", bean.getPassedMandatoryModuleCount() + "/" + bean.getMandatoryModuleCount());
      spJSON.put("passedOptional", bean.getPassedOptionalModuleCount() + "/" + bean.getOptionalModuleCount());
      
      studentProjectsJSON.add(spJSON);
    }

    String statusMessage;
    Locale locale = requestContext.getRequest().getLocale();
    if (studentProjectsByProjectPage.size() > 0) {
      statusMessage = Messages.getInstance().getText(
          locale,
          "projects.viewProject.studentProjects.searchStatus",
          new Object[] { firstResult + 1, firstResult + resultsCount,
              numStudentProjectsTotal });
    }
    else {
      statusMessage = Messages.getInstance().getText(locale, "projects.viewProject.studentProjects.searchStatusNoMatches");
    }
    
    requestContext.addResponseParameter("studentProjects", studentProjectsJSON);
    requestContext.addResponseParameter("page", page);
    requestContext.addResponseParameter("pages", numPages);
    requestContext.addResponseParameter("statusMessage", statusMessage);
  }

  private StudentProjectBean beanify(StudentProject studentProject, CourseDAO courseDAO,
      CourseStudentDAO courseStudentDAO,
      TransferCreditDAO transferCreditDAO,
      CourseAssessmentDAO courseAssessmentDAO,
      ProjectAssessmentDAO projectAssessmentDAO) {
    int mandatoryModuleCount = 0;
    int optionalModuleCount = 0;
    int passedMandatoryModuleCount = 0;
    int passedOptionalModuleCount = 0;
    
    /**
     * Go through project modules to
     *  a) count mandatory/optional modules
     *  b) count mandatory/optional modules that have passing grade on them
     *  c) create beans to be passed to jsp
     */

    List<CourseAssessment> courseAssessmentsByStudent = courseAssessmentDAO.listByStudent(studentProject.getStudent());
    List<TransferCredit> transferCreditsByStudent = transferCreditDAO.listByStudent(studentProject.getStudent());
    
    for (StudentProjectModule studentProjectModule : studentProject.getStudentProjectModules()) {
      boolean hasPassingGrade = false;
      List<CourseStudent> courseStudentList = courseStudentDAO.listByModuleAndStudent(studentProjectModule.getModule(), studentProject.getStudent());

      // Find out if there is a course that has passing grade for the module
      if (courseStudentList != null) {
        for (CourseStudent cs : courseStudentList) {
          CourseAssessment ca = courseAssessmentDAO.findLatestByCourseStudentAndArchived(cs, Boolean.FALSE); 
          if (ca != null && ca.getGrade() != null && ca.getGrade().getPassingGrade()) {
            hasPassingGrade = true; 
            break;
          }
        }
      }

      if (studentProjectModule.getOptionality() == CourseOptionality.MANDATORY) {
        mandatoryModuleCount++;
        if (hasPassingGrade)
          passedMandatoryModuleCount++;
      } else if (studentProjectModule.getOptionality() == CourseOptionality.OPTIONAL) {
        optionalModuleCount++;
        if (hasPassingGrade)
          passedOptionalModuleCount++;
      }
    }
    
    for (StudentProjectSubjectCourse studentProjectsubjectCourse : studentProject.getStudentProjectSubjectCourses()) {
      boolean hasPassingGrade = false;

      if ((studentProjectsubjectCourse.getCourseNumber() != null) && (studentProjectsubjectCourse.getCourseNumber() != -1) && (studentProjectsubjectCourse.getSubject() != null)) {
        for (CourseAssessment courseAssessment : courseAssessmentsByStudent) {
          if ((courseAssessment.getCourseNumber() != null) && (courseAssessment.getCourseNumber() != -1) && (courseAssessment.getSubject() != null)) {
            if (courseAssessment.getCourseNumber().equals(studentProjectsubjectCourse.getCourseNumber()) && courseAssessment.getSubject().equals(studentProjectsubjectCourse.getSubject())) {
              if (courseAssessment.getGrade() != null && courseAssessment.getGrade().getPassingGrade()) {
                hasPassingGrade = true; 
                break;
              }
            }
          }
        }

        if (!hasPassingGrade) {
          if ((studentProjectsubjectCourse.getCourseNumber() != null) && (studentProjectsubjectCourse.getCourseNumber() != -1) && (studentProjectsubjectCourse.getSubject() != null)) {
            for (TransferCredit tc : transferCreditsByStudent) {
              if ((tc.getCourseNumber() != null) && (tc.getCourseNumber() != -1) && (tc.getSubject() != null)) {
                if (tc.getCourseNumber().equals(studentProjectsubjectCourse.getCourseNumber()) && tc.getSubject().equals(studentProjectsubjectCourse.getSubject())) {
                  if (tc.getGrade() != null && tc.getGrade().getPassingGrade()) {
                    hasPassingGrade = true;
                    break;
                  }
                }
              }
            }
          }
        }
      }
      
      if (studentProjectsubjectCourse.getOptionality() == CourseOptionality.MANDATORY) {
        mandatoryModuleCount++;
        if (hasPassingGrade)
          passedMandatoryModuleCount++;
      } else if (studentProjectsubjectCourse.getOptionality() == CourseOptionality.OPTIONAL) {
        optionalModuleCount++;
        if (hasPassingGrade)
          passedOptionalModuleCount++;
      }
    }
    
    List<ProjectAssessment> projectAssessments = projectAssessmentDAO.listByProjectAndArchived(studentProject, Boolean.FALSE);
    
    Collections.sort(projectAssessments, new Comparator<ProjectAssessment>() {
      @Override
      public int compare(ProjectAssessment o1, ProjectAssessment o2) {
        return o2.getDate().compareTo(o1.getDate());
      }
    });
    
    return new StudentProjectBean(studentProject, mandatoryModuleCount, optionalModuleCount, passedMandatoryModuleCount, passedOptionalModuleCount, projectAssessments);
  }
  
  public class StudentProjectBean {
    private final StudentProject studentProject;
    private final int passedOptionalModuleCount;
    private final int mandatoryModuleCount;
    private final int optionalModuleCount;
    private final int passedMandatoryModuleCount;
    private final List<ProjectAssessment> assessments;

    public StudentProjectBean(StudentProject studentProject, int mandatoryModuleCount, int optionalModuleCount,
        int passedMandatoryModuleCount, int passedOptionalModuleCount, List<ProjectAssessment> assessments) {
      this.studentProject = studentProject;
      this.mandatoryModuleCount = mandatoryModuleCount;
      this.optionalModuleCount = optionalModuleCount;
      this.passedOptionalModuleCount = passedOptionalModuleCount;
      this.passedMandatoryModuleCount = passedMandatoryModuleCount;
      this.assessments = assessments;
    }

    public StudentProject getStudentProject() {
      return studentProject;
    }

    public int getPassedOptionalModuleCount() {
      return passedOptionalModuleCount;
    }

    public int getMandatoryModuleCount() {
      return mandatoryModuleCount;
    }

    public int getOptionalModuleCount() {
      return optionalModuleCount;
    }

    public int getPassedMandatoryModuleCount() {
      return passedMandatoryModuleCount;
    }

    public List<ProjectAssessment> getAssessments() {
      return assessments;
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
