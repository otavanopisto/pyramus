package fi.pyramus.json.projects;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AcademicTermDAO;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.dao.grading.GradeDAO;
import fi.pyramus.dao.grading.ProjectAssessmentDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.dao.projects.StudentProjectDAO;
import fi.pyramus.dao.projects.StudentProjectModuleDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.Defaults;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.grading.ProjectAssessment;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.projects.StudentProject;
import fi.pyramus.domainmodel.projects.StudentProjectModule;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.PyramusStatusCode;
import fi.pyramus.framework.UserRole;

public class EditStudentProjectJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    StudentProjectDAO studentProjectDAO = DAOFactory.getInstance().getStudentProjectDAO();
    StudentProjectModuleDAO studentProjectModuleDAO = DAOFactory.getInstance().getStudentProjectModuleDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    ProjectAssessmentDAO projectAssessmentDAO = DAOFactory.getInstance().getProjectAssessmentDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();

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
    User user = userDAO.findById(jsonRequestContext.getLoggedUserId());
    Long optionalStudiesLengthTimeUnitId = jsonRequestContext.getLong("optionalStudiesLengthTimeUnit");
    EducationalTimeUnit optionalStudiesLengthTimeUnit = educationalTimeUnitDAO.findById(optionalStudiesLengthTimeUnitId);
    Double optionalStudiesLength = jsonRequestContext.getDouble("optionalStudiesLength");
    String tagsText = jsonRequestContext.getString("tags");
    Long studentId = jsonRequestContext.getLong("student");
    CourseOptionality projectOptionality = (CourseOptionality) jsonRequestContext.getEnum("projectOptionality", CourseOptionality.class);
    
    Set<Tag> tagEntities = new HashSet<Tag>();
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
      studentProjectDAO.updateStudent(studentProject, student, user);
    }
    
    studentProjectDAO.update(studentProject, name, description, optionalStudiesLength,
        optionalStudiesLengthTimeUnit, projectOptionality, user);

    // Tags

    studentProjectDAO.updateTags(studentProject, tagEntities);

    // ProjectAssessments
    
    int rowCount = jsonRequestContext.getInteger("assessmentsTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "assessmentsTable." + i;
      
      Long assessmentModified = jsonRequestContext.getLong(colPrefix + ".modified");

      if ((assessmentModified != null) && (assessmentModified.intValue() == 1)) {
        Long assessmentId = jsonRequestContext.getLong(colPrefix + ".assessmentId");
        ProjectAssessment projectAssessment = ((assessmentId != null) && (assessmentId.intValue() != -1)) ? projectAssessmentDAO.findById(assessmentId) : null;
        Long assessmentArchived = jsonRequestContext.getLong(colPrefix + ".deleted");

        if ((assessmentArchived != null) && (assessmentArchived.intValue() == 1)) {
          if (projectAssessment != null)
            projectAssessmentDAO.archive(projectAssessment);
          else
            throw new SmvcRuntimeException(PyramusStatusCode.OK, "Assessment marked for delete does not exist.");
        } else {
          Date assessmentDate = jsonRequestContext.getDate(colPrefix + ".date");
          Long assessmentGradeId = jsonRequestContext.getLong(colPrefix + ".grade");
          Grade grade = assessmentGradeId != null ? gradeDAO.findById(assessmentGradeId) : null; 

          String verbalAssessment = projectAssessment != null ? projectAssessment.getVerbalAssessment() : null;
          Long verbalAssessmentModified = jsonRequestContext.getLong(colPrefix + ".verbalModified");
          if ((verbalAssessmentModified != null) && (verbalAssessmentModified.intValue() == 1))
            verbalAssessment = jsonRequestContext.getString(colPrefix + ".verbalAssessment");
          
          if (projectAssessment == null) {
            projectAssessmentDAO.create(studentProject, user, grade, assessmentDate, verbalAssessment);
          } else {
            projectAssessmentDAO.update(projectAssessment, user, grade, assessmentDate, verbalAssessment);
          }
        }
      }
    }
    
    // Student project modules

    Set<Long> existingModuleIds = new HashSet<Long>();
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
        courseStudent = courseStudentDAO.create(course, studentProject.getStudent(), courseEnrolmentType, participationType, enrolmentDate, lodging, optionality, null, Boolean.FALSE);
      } else {
        courseStudent = courseStudentDAO.update(courseStudent, studentProject.getStudent(), courseStudent.getCourseEnrolmentType(), courseStudent.getParticipationType(), courseStudent.getEnrolmentTime(), courseStudent.getLodging(), optionality);
      }
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
