package fi.otavanopisto.pyramus.json.projects;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.AcademicTermDAO;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditVariableDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.grading.GradeDAO;
import fi.otavanopisto.pyramus.dao.grading.ProjectAssessmentDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectModuleDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectSubjectCourseDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.accommodation.Room;
import fi.otavanopisto.pyramus.domainmodel.base.AcademicTerm;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Defaults;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditVariableKey;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.ProjectAssessment;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectModule;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectSubjectCourse;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.exception.DuplicateCourseStudentException;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditStudentProjectJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    StudentProjectDAO studentProjectDAO = DAOFactory.getInstance().getStudentProjectDAO();
    StudentProjectModuleDAO studentProjectModuleDAO = DAOFactory.getInstance().getStudentProjectModuleDAO();
    StudentProjectSubjectCourseDAO studentProjectSubjectCourseDAO = DAOFactory.getInstance().getStudentProjectSubjectCourseDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    ProjectAssessmentDAO projectAssessmentDAO = DAOFactory.getInstance().getProjectAssessmentDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();

    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    Defaults defaults = defaultsDAO.getDefaults();

    // Project

    Long studentProjectId = jsonRequestContext.getLong("studentProject");
    StudentProject studentProject = studentProjectDAO.findById(studentProjectId);
    
    // Version check
    Long version = jsonRequestContext.getLong("version"); 
    if (!studentProject.getVersion().equals(version))
      throw new StaleObjectStateException(StudentProject.class.getName(), studentProject.getId());
    
    String name = jsonRequestContext.getString("name");
    String description = jsonRequestContext.getString("description");
    StaffMember staffMember = staffMemberDAO.findById(jsonRequestContext.getLoggedUserId());
    Long optionalStudiesLengthTimeUnitId = jsonRequestContext.getLong("optionalStudiesLengthTimeUnit");
    EducationalTimeUnit optionalStudiesLengthTimeUnit = educationalTimeUnitDAO.findById(optionalStudiesLengthTimeUnitId);
    Double optionalStudiesLength = jsonRequestContext.getDouble("optionalStudiesLength");
    String tagsText = jsonRequestContext.getString("tags");
    Long studentId = jsonRequestContext.getLong("student");
    CourseOptionality projectOptionality = (CourseOptionality) jsonRequestContext.getEnum("projectOptionality", CourseOptionality.class);
    
    Set<Tag> tagEntities = new HashSet<>();
    if (!StringUtils.isBlank(tagsText)) {
      List<String> tags = Arrays.asList(tagsText.split("[\\ ,]"));
      for (String tag : tags) {
        if (!StringUtils.isBlank(tag)) {
          Tag tagEntity = tagDAO.findByText(tag.trim());
          if (tagEntity == null)
            tagEntity = tagDAO.create(tag);
          tagEntities.add(tagEntity);
        }
      }
    }
    
    Student student = studentDAO.findById(studentId);
    
    // Student
    
    if (!studentProject.getStudent().equals(student)) {
      studentProjectDAO.updateStudent(studentProject, student, staffMember);
    }
    
    studentProjectDAO.update(studentProject, name, description, optionalStudiesLength,
        optionalStudiesLengthTimeUnit, projectOptionality, staffMember);

    // Tags

    studentProjectDAO.updateTags(studentProject, tagEntities);

    // ProjectAssessments
    CreditVariableKeyDAO creditVariableKeyDAO = DAOFactory.getInstance().getCreditVariableKeyDAO();
    CreditVariableKey creditVariableKey = creditVariableKeyDAO.findByKey("lukioKokelaslaji");
    if (creditVariableKey == null) {
      creditVariableKey = creditVariableKeyDAO.create(false, "lukioKokelaslaji", "Ylioppilaskokeen kokelaslaji", VariableType.TEXT);
    }
    CreditVariableDAO creditVariableDAO = DAOFactory.getInstance().getCreditVariableDAO();
    
    int rowCount = jsonRequestContext.getInteger("assessmentsTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "assessmentsTable." + i;
      
      Long assessmentModified = jsonRequestContext.getLong(colPrefix + ".modified");

      if ((assessmentModified != null) && (assessmentModified.intValue() == 1)) {
        Long assessmentId = jsonRequestContext.getLong(colPrefix + ".assessmentId");
        ProjectAssessment projectAssessment = ((assessmentId != null) && (assessmentId.intValue() != -1)) ? projectAssessmentDAO.findById(assessmentId) : null;
        Long assessmentArchived = jsonRequestContext.getLong(colPrefix + ".deleted");
        String examinationType = jsonRequestContext.getString(colPrefix + ".examinationType");

        if ((assessmentArchived != null) && (assessmentArchived.intValue() == 1)) {
          if (projectAssessment != null) {
            projectAssessmentDAO.archive(projectAssessment);
            creditVariableDAO.setCreditVariable(projectAssessment, "lukioKokelaslaji", "");
          } else {
            throw new SmvcRuntimeException(PyramusStatusCode.OK, "Assessment marked for delete does not exist.");
          }
        } else {
          Date assessmentDate = jsonRequestContext.getDate(colPrefix + ".date");
          Long assessmentGradeId = jsonRequestContext.getLong(colPrefix + ".grade");
          Grade grade = assessmentGradeId != null ? gradeDAO.findById(assessmentGradeId) : null; 

          String verbalAssessment = projectAssessment != null ? projectAssessment.getVerbalAssessment() : null;
          Long verbalAssessmentModified = jsonRequestContext.getLong(colPrefix + ".verbalModified");
          if ((verbalAssessmentModified != null) && (verbalAssessmentModified.intValue() == 1))
            verbalAssessment = jsonRequestContext.getString(colPrefix + ".verbalAssessment");
          
          if (projectAssessment == null) {
            projectAssessment = projectAssessmentDAO.create(studentProject, staffMember, grade, assessmentDate, verbalAssessment);
          } else {
            projectAssessment = projectAssessmentDAO.update(projectAssessment, staffMember, grade, assessmentDate, verbalAssessment);
          }
          creditVariableDAO.setCreditVariable(projectAssessment, "lukioKokelaslaji", examinationType);
        }
      }
    }
    
    // Student project modules

    Set<Long> existingModuleIds = new HashSet<>();
    rowCount = jsonRequestContext.getInteger("modulesTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "modulesTable." + i;
      
      Long studentProjectModuleId = jsonRequestContext.getLong(colPrefix + ".studentProjectModuleId");
      CourseOptionality optionality = (CourseOptionality) jsonRequestContext.getEnum(colPrefix + ".optionality", CourseOptionality.class);
      Long studyTermId = jsonRequestContext.getLong(colPrefix + ".academicTerm");
      AcademicTerm academicTerm = studyTermId == null ? null : academicTermDAO.findById(studyTermId);
      
      if (studentProjectModuleId == -1) {
        Long moduleId = jsonRequestContext.getLong(colPrefix + ".moduleId");
        Module module = moduleDAO.findById(moduleId);
        studentProjectModuleId = studentProjectModuleDAO.create(studentProject, module, academicTerm, optionality).getId();
      } else {
        studentProjectModuleDAO.update(studentProjectModuleDAO.findById(studentProjectModuleId), academicTerm, optionality);
      }
      
      existingModuleIds.add(studentProjectModuleId);
    }
    
    // Removed Student project modules 
    
    List<StudentProjectModule> studentProjectModules = studentProjectModuleDAO.listByStudentProject(studentProject);
    for (StudentProjectModule studentProjectModule : studentProjectModules) {
      if (!existingModuleIds.contains(studentProjectModule.getId())) {
        studentProjectModuleDAO.delete(studentProjectModule);
      }
    }
    
    // Student subject courses

    Set<Long> existingSubjectCourseIds = new HashSet<>();
    rowCount = jsonRequestContext.getInteger("subjectCoursesTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "subjectCoursesTable." + i;
      
      Long studentProjectSubjectCourseId = jsonRequestContext.getLong(colPrefix + ".projectSubjectCourseId");
      int optionalityInt = new Integer(jsonRequestContext.getRequest().getParameter(colPrefix + ".optionality")).intValue();
      CourseOptionality optionality = CourseOptionality.getOptionality(optionalityInt);
      Long studyTermId = jsonRequestContext.getLong(colPrefix + ".academicTerm");
      AcademicTerm academicTerm = studyTermId == null ? null : academicTermDAO.findById(studyTermId);
      
      if (studentProjectSubjectCourseId == -1) {
        Long subjectId = jsonRequestContext.getLong(colPrefix + ".subjectId");
        Subject subject = subjectDAO.findById(subjectId);
        Integer courseNumber = jsonRequestContext.getInteger(colPrefix + ".courseNumber");
        studentProjectSubjectCourseId = studentProjectSubjectCourseDAO.create(studentProject, subject, courseNumber , academicTerm, optionality).getId();
      } else {
        studentProjectSubjectCourseDAO.update(studentProjectSubjectCourseDAO.findById(studentProjectSubjectCourseId), academicTerm, optionality);
      }
      
      existingSubjectCourseIds.add(studentProjectSubjectCourseId);
    }
    
    // Removed Student project subject courses 
    
    List<StudentProjectSubjectCourse> studentProjectSubjectCourses = studentProjectSubjectCourseDAO.listByStudentProject(studentProject);
    for (StudentProjectSubjectCourse studentProjectSubjectCourse : studentProjectSubjectCourses) {
      if (!existingSubjectCourseIds.contains(studentProjectSubjectCourse.getId())) {
        studentProjectSubjectCourseDAO.delete(studentProjectSubjectCourse);
      }
    }
    
    // Student project courses

    rowCount = jsonRequestContext.getInteger("coursesTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "coursesTable." + i;
      
      Long courseId = jsonRequestContext.getLong(colPrefix + ".courseId");
      CourseOptionality optionality = (CourseOptionality) jsonRequestContext.getEnum(colPrefix + ".optionality", CourseOptionality.class);
      
      Course course = courseId == -1 ? null : courseDAO.findById(courseId);
      CourseStudent courseStudent = courseStudentDAO.findByCourseAndStudent(course, studentProject.getStudent());
      if (courseStudent == null) {
        CourseEnrolmentType courseEnrolmentType = defaults.getInitialCourseEnrolmentType();
        CourseParticipationType participationType = defaults.getInitialCourseParticipationType();
        Date enrolmentDate = new Date(System.currentTimeMillis());
        Boolean lodging = Boolean.FALSE;
        String organization = null;
        String additionalInfo = null;
        Room room = null;
        BigDecimal lodgingFee = null;
        Currency lodgingFeeCurrency = null;
        BigDecimal reservationFee = null;
        Currency reservationFeeCurrency = null;
        
        try {
          courseStudent = courseStudentDAO.create(course, studentProject.getStudent(), courseEnrolmentType, participationType, 
              enrolmentDate, lodging, optionality, null, organization, additionalInfo, room, lodgingFee, lodgingFeeCurrency, 
              reservationFee, reservationFeeCurrency, Boolean.FALSE);
        } catch (DuplicateCourseStudentException dcse) {
          Locale locale = jsonRequestContext.getRequest().getLocale();
          throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, 
              Messages.getInstance().getText(locale, "generic.errors.duplicateCourseStudent", new Object[] { student.getFullName() }));
        }
      } else {
        courseStudentDAO.updateOptionality(courseStudent, optionality);
      }
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
