package fi.otavanopisto.pyramus.json.projects;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.Test;
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
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectModule;
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
    Project project = projectDAO.findById(projectId );
    
//    List<StudentProject> studentProjectsByProject = studentProjectDAO.listByProject(project);
    Test<StudentProject> studentProjectsByProject = studentProjectDAO.listByProject2(project);
//    Collections.sort(studentProjectsByProject, new Comparator<StudentProject>() {
//      @Override
//      public int compare(StudentProject o1, StudentProject o2) {
//        int v = o1.getStudent().getLastName().compareToIgnoreCase(o2.getStudent().getLastName());
//        
//        return v != 0 ? v : o1.getStudent().getFirstName().compareToIgnoreCase(o2.getStudent().getFirstName());
//      }
//    });
    
    JSONArray studentProjectsJSON = new JSONArray();
    int count = 0;
    for (StudentProject sp : studentProjectsByProject) {
      count++;
      System.out.println("Student #" + count);
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

    requestContext.addResponseParameter("studentProjects", studentProjectsJSON);
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
    
    courseDAO.flush();
    courseDAO.clear();

    transferCreditDAO.flush();
    transferCreditDAO.clear();

    courseAssessmentDAO.flush();
    courseAssessmentDAO.clear();
    
    projectAssessmentDAO.flush();
    projectAssessmentDAO.clear();
    
    /**
     * Go through project modules to
     *  a) count mandatory/optional modules
     *  b) count mandatory/optional modules that have passing grade on them
     *  c) create beans to be passed to jsp
     */

//    List<TransferCredit> transferCreditsByStudent = transferCreditDAO.listByStudent(studentProject.getStudent());
//    List<CourseAssessment> courseAssessmentsByStudent = courseAssessmentDAO.listByStudent(studentProject.getStudent());
    
//    System.out.println("Student: " + studentProject.getStudent().getId());
    printHeap();
    
    for (StudentProjectModule studentProjectModule : studentProject.getStudentProjectModules()) {
      boolean hasPassingGrade = false;
      List<CourseStudent> courseStudentList = courseStudentDAO.listByModuleAndStudent(studentProjectModule.getModule(), studentProject.getStudent());

      // Find out if there is a course that has passing grade for the module
      if (courseStudentList != null) {
        for (CourseStudent cs : courseStudentList) {
          CourseAssessment ca = courseAssessmentDAO.findByCourseStudentAndArchived(cs, Boolean.FALSE); 
          if (ca != null && ca.getGrade() != null && ca.getGrade().getPassingGrade()) {
            hasPassingGrade = true; 
            break;
          }
        }
      }

//      for (CourseAssessment courseAssessment : courseAssessmentsByStudent) {
//        if (courseAssessment.getCourseStudent() != null) {
//          if (courseAssessment.getCourseStudent().getCourse() != null) {
//            if (courseAssessment.getCourseStudent().getCourse().getModule() != null) {
//              Module module = courseAssessment.getCourseStudent().getCourse().getModule();
//              
//              if (module.getId().equals(studentProjectModule.getModule().getId())) {
//                if (courseAssessment.getGrade() != null && courseAssessment.getGrade().getPassingGrade()) {
////                  System.out.println("found module - passing grade");
//                  hasPassingGrade = true; 
//                  break;
//                }
////                System.out.println("found module - no passing grade");
//              }
//            }
//          }
//        }
//      }      
      
      if (!hasPassingGrade) {
        if ((studentProjectModule.getModule().getCourseNumber() != null) && (studentProjectModule.getModule().getCourseNumber() != -1) && (studentProjectModule.getModule().getSubject() != null)) {
          List<TransferCredit> tcs = transferCreditDAO.findBy(studentProject.getStudent(), studentProjectModule.getModule().getSubject(), studentProjectModule.getModule().getCourseNumber());
          for (TransferCredit tc : tcs) { // transferCreditsByStudent) {
            if ((tc.getCourseNumber() != null) && (tc.getCourseNumber() != -1) && (tc.getSubject() != null)) {
              if (tc.getCourseNumber().equals(studentProjectModule.getModule().getCourseNumber()) && tc.getSubject().equals(studentProjectModule.getModule().getSubject())) {
                if (tc.getGrade() != null && tc.getGrade().getPassingGrade()) {
                  hasPassingGrade = true;
                  break;
                }
              }
            }
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
    
    List<ProjectAssessment> projectAssessments = projectAssessmentDAO.listByProjectAndArchived(studentProject, Boolean.FALSE);
    
    Collections.sort(projectAssessments, new Comparator<ProjectAssessment>() {
      @Override
      public int compare(ProjectAssessment o1, ProjectAssessment o2) {
        return o2.getDate().compareTo(o1.getDate());
      }
    });
    
    return new StudentProjectBean(studentProject, mandatoryModuleCount, optionalModuleCount, passedMandatoryModuleCount, passedOptionalModuleCount, projectAssessments);
  }
  
  public void printHeap() {
//    int mb = 1024*1024;
    int mb = 1024;
    
    //Getting the runtime reference from system
    Runtime runtime = Runtime.getRuntime();
    
//    System.out.println("##### Heap utilization statistics [MB] #####");
    
    long used = (runtime.totalMemory() - runtime.freeMemory()) / mb;
    long free = runtime.freeMemory() / mb;
    long total = runtime.totalMemory() / mb;
    long max = runtime.maxMemory() / mb;
    
    System.out.println(String.format("Used: %d\tFree: %d", used, free));
    
//    //Print used memory
//    System.out.println("Used Memory:" 
//      + (runtime.totalMemory() - runtime.freeMemory()) / mb);
//
//    //Print free memory
//    System.out.println("Free Memory:" 
//      + runtime.freeMemory() / mb);
//    
//    //Print total available memory
//    System.out.println("Total Memory:" + runtime.totalMemory() / mb);
//
//    //Print Maximum available memory
//    System.out.println("Max Memory:" + runtime.maxMemory() / mb);
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
