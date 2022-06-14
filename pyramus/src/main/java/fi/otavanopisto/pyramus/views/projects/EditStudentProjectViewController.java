package fi.otavanopisto.pyramus.views.projects;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.AcademicTermDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditVariableDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.grading.GradingScaleDAO;
import fi.otavanopisto.pyramus.dao.grading.ProjectAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectModuleDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.AcademicTerm;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditVariableKey;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.domainmodel.grading.ProjectAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectModule;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectSubjectCourse;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * The controller responsible of the Edit Student Project view of the application.
 */
public class EditStudentProjectViewController extends PyramusViewController implements Breadcrumbable {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    StudentProjectDAO studentProjectDAO = DAOFactory.getInstance().getStudentProjectDAO();
    StudentProjectModuleDAO studentProjectModuleDAO = DAOFactory.getInstance().getStudentProjectModuleDAO();
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    ProjectAssessmentDAO projectAssessmentDAO = DAOFactory.getInstance().getProjectAssessmentDAO();
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    CreditLinkDAO creditLinkDAO = DAOFactory.getInstance().getCreditLinkDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    CreditVariableDAO creditVariableDAO = DAOFactory.getInstance().getCreditVariableDAO();
    CreditVariableKeyDAO creditVariableKeyDAO = DAOFactory.getInstance().getCreditVariableKeyDAO();

    Long studentProjectId = pageRequestContext.getLong("studentproject");
    List<GradingScale> gradingScales = gradingScaleDAO.listUnarchived();

    StudentProject studentProject = studentProjectDAO.findById(studentProjectId);
    List<StudentProjectModule> studentProjectModules = studentProjectModuleDAO.listByStudentProject(studentProject);
    List<CourseStudent> courseStudents = courseStudentDAO.listByStudent(studentProject.getStudent());
    
    List<CourseAssessment> allStudentCourseAssessments = courseAssessmentDAO.listByStudent(studentProject.getStudent());
    List<TransferCredit> allStudentTransferCredits = transferCreditDAO.listByStudent(studentProject.getStudent());
    List<CreditLink> allStudentCreditLinks = creditLinkDAO.listByStudent(studentProject.getStudent());
    
    JSONArray studentProjectModulesJSON = new JSONArray();
    JSONArray studentProjectSubjectCoursesJSON = new JSONArray();
    JSONArray courseStudentsJSON = new JSONArray();

    for (CreditLink creditLink : allStudentCreditLinks) {
      switch (creditLink.getCredit().getCreditType()) {
        case CourseAssessment:
          allStudentCourseAssessments.add((CourseAssessment) creditLink.getCredit());
        break;

        case TransferCredit:
          allStudentTransferCredits.add((TransferCredit) creditLink.getCredit());
        break;
        
        case ProjectAssessment:
        break;
      }
    }

    Collections.sort(studentProjectModules, new Comparator<StudentProjectModule>() {
      @Override
      public int compare(StudentProjectModule o1, StudentProjectModule o2) {
        return o1.getModule().getName().compareTo(o2.getModule().getName());
      }
    });
    
    for (StudentProjectModule studentProjectModule : studentProjectModules) {
      JSONArray moduleCourseStudents = new JSONArray();
      JSONArray moduleCredits = new JSONArray();
      boolean hasPassingGrade = false;

      List<CourseStudent> projectCourseCourseStudents = courseStudentDAO.listByModuleAndStudent(studentProjectModule.getModule(), studentProject.getStudent());
      
      for (CourseStudent courseStudent : projectCourseCourseStudents) {
        JSONObject courseStudentJson = new JSONObject();
        courseStudentJson.put("courseStudentParticipationType", courseStudent.getParticipationType().getName());
//        courseStudents.remove(courseStudent);
        moduleCourseStudents.add(courseStudentJson);
      }
      
      for (CourseAssessment assessment : allStudentCourseAssessments) {
        if (assessment.getCourseStudent().getCourse().getModule().getId().equals(studentProjectModule.getModule().getId())) {
          if (assessment.getGrade() != null) {
            JSONObject courseAssessment = new JSONObject();

            courseAssessment.put("creditType", assessment.getCreditType().toString());
            courseAssessment.put("courseName", assessment.getCourseStudent().getCourse().getName());
            courseAssessment.put("gradeName", assessment.getGrade().getName());

            moduleCredits.add(courseAssessment);
            hasPassingGrade = hasPassingGrade || assessment.getGrade().getPassingGrade();
          }
        }
      }

      JSONObject obj = new JSONObject();
      
      obj.put("projectModuleId", studentProjectModule.getId().toString());
      obj.put("projectModuleOptionality", studentProjectModule.getOptionality().toString());
      obj.put("projectModuleAcademicTermId", studentProjectModule.getAcademicTerm() != null ? studentProjectModule.getAcademicTerm().getId().toString() : "");
      obj.put("projectModuleHasPassingGrade", hasPassingGrade ? "1" : "0");
      obj.put("moduleId", studentProjectModule.getModule().getId());
      obj.put("moduleName", studentProjectModule.getModule().getName());
      
      obj.put("moduleCourseStudents", moduleCourseStudents);
      obj.put("moduleCredits", moduleCredits);
      
      studentProjectModulesJSON.add(obj);
    }

    for (StudentProjectSubjectCourse studentProjectSubjectCourse : studentProject.getStudentProjectSubjectCourses()) {
      JSONArray moduleCredits = new JSONArray();
      boolean hasPassingGrade = false;

      if ((studentProjectSubjectCourse.getCourseNumber() != null) && (studentProjectSubjectCourse.getCourseNumber() != -1) && (studentProjectSubjectCourse.getSubject() != null)) {
        for (CourseAssessment assessment : allStudentCourseAssessments) {
          if ((assessment.getCourseNumber() != null) && (assessment.getCourseNumber() != -1) && (assessment.getSubject() != null)) {
            if (assessment.getCourseNumber().equals(studentProjectSubjectCourse.getCourseNumber()) && assessment.getSubject().equals(studentProjectSubjectCourse.getSubject())) {
              if (assessment.getGrade() != null) {
                JSONObject courseAssessment = new JSONObject();
    
                courseAssessment.put("creditType", assessment.getCreditType().toString());
                courseAssessment.put("courseName", assessment.getCourseStudent().getCourse().getName());
                courseAssessment.put("gradeName", assessment.getGrade().getName());
    
                moduleCredits.add(courseAssessment);
                hasPassingGrade = hasPassingGrade || assessment.getGrade().getPassingGrade();
              }
            }
          }
        }
      
        for (TransferCredit tc : allStudentTransferCredits) {
          if ((tc.getCourseNumber() != null) && (tc.getCourseNumber() != -1) && (tc.getSubject() != null)) {
            if (tc.getCourseNumber().equals(studentProjectSubjectCourse.getCourseNumber()) && tc.getSubject().equals(studentProjectSubjectCourse.getSubject())) {
              if (tc.getGrade() != null) {
                JSONObject transferCredit = new JSONObject();
                
                transferCredit.put("creditType", tc.getCreditType().toString());
                transferCredit.put("courseName", tc.getCourseName());
                transferCredit.put("gradeName", tc.getGrade().getName());
                
                moduleCredits.add(transferCredit);
                hasPassingGrade = hasPassingGrade || tc.getGrade().getPassingGrade();
              }
            }
          }
        }
      }
      
      
      JSONObject obj = new JSONObject();
      
      obj.put("id", studentProjectSubjectCourse.getId());
      obj.put("subjectId", studentProjectSubjectCourse.getSubject().getId());
      obj.put("courseNumber", studentProjectSubjectCourse.getCourseNumber());
      obj.put("optionality", studentProjectSubjectCourse.getOptionality().getValue());
      obj.put("projectModuleAcademicTermId", studentProjectSubjectCourse.getAcademicTerm() != null ? studentProjectSubjectCourse.getAcademicTerm().getId().toString() : "");
      obj.put("projectModuleHasPassingGrade", hasPassingGrade ? "1" : "0");

      obj.put("moduleCredits", moduleCredits);
      
      studentProjectSubjectCoursesJSON.add(obj);
    }
    
    List<Student> students = studentDAO.listByPerson(studentProject.getStudent().getPerson());
    Collections.sort(students, new Comparator<Student>() {
      @Override
      public int compare(Student o1, Student o2) {
        /**
         * Ordering study programmes as follows
         *  1. studies that have start date but no end date (ongoing)
         *  2. studies that have no start nor end date
         *  3. studies that have ended
         *  4. studies that are archived
         *  5. other
         */
        
        int o1class =
          (o1.getArchived()) ? 4:
            (o1.getStudyStartDate() != null && o1.getStudyEndDate() == null) ? 1:
              (o1.getStudyStartDate() == null && o1.getStudyEndDate() == null) ? 2:
                (o1.getStudyEndDate() != null) ? 3:
                  5;
        int o2class =
          (o2.getArchived()) ? 4:
            (o2.getStudyStartDate() != null && o2.getStudyEndDate() == null) ? 1:
              (o2.getStudyStartDate() == null && o2.getStudyEndDate() == null) ? 2:
                (o2.getStudyEndDate() != null) ? 3:
                  5;

        if (o1class == o2class) {
          // classes are the same, we try to do last comparison from the start dates
          return ((o1.getStudyStartDate() != null) && (o2.getStudyStartDate() != null)) ? 
              o2.getStudyStartDate().compareTo(o1.getStudyStartDate()) : 0; 
        } else
          return o1class < o2class ? -1 : o1class == o2class ? 0 : 1;
      }
    });

    List<EducationalTimeUnit> educationalTimeUnits = educationalTimeUnitDAO.listUnarchived();
    Collections.sort(educationalTimeUnits, new StringAttributeComparator("getName"));

    List<AcademicTerm> academicTerms = academicTermDAO.listUnarchived();
    Collections.sort(academicTerms, new Comparator<AcademicTerm>() {
      public int compare(AcademicTerm o1, AcademicTerm o2) {
        return o1.getStartDate() == null ? -1 : o2.getStartDate() == null ? 1 : o1.getStartDate().compareTo(o2.getStartDate());
      }
    });
    
    List<ProjectAssessment> assessments = projectAssessmentDAO.listByProjectAndArchived(studentProject, Boolean.FALSE);
    Collections.sort(assessments, new Comparator<ProjectAssessment>() {
      @Override
      public int compare(ProjectAssessment o1, ProjectAssessment o2) {
        return o2.getDate().compareTo(o1.getDate());
      }
    });

    /* Lukio-plugin */
    
    Map<Long, String> assessmentExaminationTypes = new HashMap<>();
    
    CreditVariableKey creditVariableKey = creditVariableKeyDAO.findByKey("lukioKokelaslaji");
    if (creditVariableKey != null) {
      for (ProjectAssessment projectAssessment : assessments) {
        String creditVariable = creditVariableDAO.findByCreditAndKey(projectAssessment, "lukioKokelaslaji");
        if (creditVariable != null) {
          assessmentExaminationTypes.put(projectAssessment.getId(), creditVariable);
        }
      }
    }
    
    Collections.sort(courseStudents, new Comparator<CourseStudent>() {
      @Override
      public int compare(CourseStudent o1, CourseStudent o2) {
        return o1.getCourse().getName().compareTo(o2.getCourse().getName());
      }
    });

    for (CourseStudent courseStudent : courseStudents) {
      CourseAssessment courseAssessment = courseAssessmentDAO.findLatestByCourseStudentAndArchived(courseStudent, Boolean.FALSE);
      Grade grade = courseAssessment != null ? courseAssessment.getGrade() : null;
      
      JSONObject obj = new JSONObject();

      String courseName = courseStudent.getCourse().getName();
      if (!StringUtils.isEmpty(courseStudent.getCourse().getNameExtension()))
        courseName = courseName + " (" + courseStudent.getCourse().getNameExtension() + ")";
      
      obj.put("courseName", courseName);
      
      if (courseStudent.getParticipationType() != null) {
        obj.put("participationType", courseStudent.getParticipationType().getName());
      }
      if (courseStudent.getCourse().getBeginDate() != null) {
        obj.put("courseBeginDate", courseStudent.getCourse().getBeginDate().getTime());
      }
      if (courseStudent.getCourse().getEndDate() != null) {
        obj.put("courseEndDate", courseStudent.getCourse().getEndDate().getTime());
      }

      obj.put("gradeName", grade != null ? grade.getName() : "");
      
      if (courseStudent.getOptionality() != null) {
        obj.put("optionality", courseStudent.getOptionality().toString());
      }
      obj.put("moduleId", courseStudent.getCourse().getModule().getId().toString());
      obj.put("courseId", courseStudent.getCourse().getId().toString());
      obj.put("courseStudentId", courseStudent.getId().toString());
      
      courseStudentsJSON.add(obj);
    }
    
    Map<Long, String> verbalAssessments = new HashMap<>();

    for (ProjectAssessment pAss : assessments) {
      // Shortened descriptions
      String description = pAss.getVerbalAssessment();
      if (description != null) {
        description = StringEscapeUtils.unescapeHtml(description.replaceAll("\\<.*?>",""));
        description = description.replaceAll("\\n", "");
        
        verbalAssessments.put(pAss.getId(), description);
      }
    }

    List<Subject> subjects = subjectDAO.listUnarchived();
    JSONArray subjectsJSON = new JSONArray();
    for (Subject subject : subjects) {
      JSONObject subjectJSON = new JSONObject();
      subjectJSON.put("id", subject.getId());
      subjectJSON.put("name", subject.getName());
      subjectJSON.put("code", subject.getCode());
      subjectJSON.put("educationTypeId", subject.getEducationType() != null ? subject.getEducationType().getId() : null);
      subjectJSON.put("educationTypeCode", subject.getEducationType() != null ? subject.getEducationType().getCode() : null);
      subjectJSON.put("educationTypeName", subject.getEducationType() != null ? subject.getEducationType().getName() : null);
      subjectsJSON.add(subjectJSON);
    }
    setJsDataVariable(pageRequestContext, "subjects", subjectsJSON.toString());
    
    /* Tags */
    
    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = studentProject.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }

    pageRequestContext.getRequest().setAttribute("assessmentExaminationTypes", assessmentExaminationTypes);
    pageRequestContext.getRequest().setAttribute("projectAssessments", assessments);
    pageRequestContext.getRequest().setAttribute("verbalAssessments", verbalAssessments);
    pageRequestContext.getRequest().setAttribute("studentProject", studentProject);
    pageRequestContext.getRequest().setAttribute("students", students);
    pageRequestContext.getRequest().setAttribute("optionalStudiesLengthTimeUnits", educationalTimeUnits);
    pageRequestContext.getRequest().setAttribute("academicTerms", academicTerms);
    pageRequestContext.getRequest().setAttribute("users", userDAO.listAll());
    pageRequestContext.getRequest().setAttribute("gradingScales", gradingScales);
    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString());

    setJsDataVariable(pageRequestContext, "studentProjectModules", studentProjectModulesJSON.toString());
    setJsDataVariable(pageRequestContext, "studentProjectSubjectCourses", studentProjectSubjectCoursesJSON.toString());
    setJsDataVariable(pageRequestContext, "courseStudents", courseStudentsJSON.toString());
    
    pageRequestContext.setIncludeJSP("/templates/projects/editstudentproject.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Editing student projects is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "projects.editStudentProject.breadcrumb");
  }
}
