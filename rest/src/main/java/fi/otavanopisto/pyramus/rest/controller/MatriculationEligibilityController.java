package fi.otavanopisto.pyramus.rest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.dao.grading.ProjectAssessmentDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentChangeLogDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamSubjectSettingsDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectModuleDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectSubjectCourseDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupStudentDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.TSB;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.grading.ProjectAssessment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentChangeLog;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubjectSettings;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectModule;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectSubjectCourse;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.framework.DateUtils;
import fi.otavanopisto.pyramus.framework.SettingUtils;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentChangeLogType;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamListFilter;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.rest.matriculation.MatriculationEligibilityCurriculumMapping;
import fi.otavanopisto.pyramus.rest.matriculation.MatriculationEligibilityMapping;
import fi.otavanopisto.pyramus.rest.matriculation.MatriculationEligibilitySubjectMapping;
import fi.otavanopisto.pyramus.rest.model.MatriculationExamStudentStatus;
import fi.otavanopisto.pyramus.tor.StudentTOR;
import fi.otavanopisto.pyramus.tor.StudentTORController;
import fi.otavanopisto.pyramus.tor.TORSubject;
import fi.otavanopisto.pyramus.tor.curriculum.TORCurriculum;
import fi.otavanopisto.pyramus.tor.curriculum.TORCurriculumSubject;

/**
 * Controller for determining 
 * 
 * @author Antti Leppä
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class MatriculationEligibilityController {

  private static final String USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE = "matriculation.examEnrollmentExpiryDate";
  private static final String SETTING_ELIGIBLE_GROUPS = "matriculation.eligibleGroups";
  private static final String ANY_CURRICULUM = "any";
  
  @Inject
  private Logger logger;

  @Inject
  private CommonController commonController;
  
  @Inject
  private AssessmentController assessmentController;
  
  private MatriculationEligibilityMapping matriculationEligibilityMapping; 
  
  @Inject
  private MatriculationExamEnrollmentDAO matriculationExamEnrollmentDAO;
  
  @Inject
  private MatriculationExamEnrollmentChangeLogDAO matriculationExamEnrollmentChangeLogDAO;
  
  @Inject
  private MatriculationExamDAO matriculationExamDAO;
  
  @Inject
  private StudentGroupDAO studentGroupDAO;
  
  @Inject
  private StudentGroupStudentDAO studentGroupStudentDAO;
  
  @Inject
  private UserVariableDAO userVariableDAO;

  @Inject
  private UserVariableKeyDAO userVariableKeyDAO;

  @Inject
  private ProjectAssessmentDAO projectAssessmentDAO;

  @Inject
  private StudentProjectDAO studentProjectDAO;
  
  @Inject
  private StudentProjectModuleDAO studentProjectModuleDAO;
  
  @Inject
  private StudentProjectSubjectCourseDAO studentProjectSubjectCourseDAO;
  
  @Inject
  private MatriculationExamSubjectSettingsDAO matriculationExamSubjectSettingsDAO;
  
  @Inject
  private MatriculationExamAttendanceDAO matriculationExamAttendanceDAO;

  
  /**
   * Post construct method. 
   *  
   * Initializes matriculation eligibility mapping
   */
  @PostConstruct
  public void init() {
    ObjectMapper objectMapper = new ObjectMapper();
    
    try {
      matriculationEligibilityMapping = objectMapper.readValue(getClass().getClassLoader().getResourceAsStream("fi/otavanopisto/pyramus/rest/matriculation-eligibility-mapping.json"), MatriculationEligibilityMapping.class);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Failed to read matriculation eligibility mapping", e);
    }    
  }
  
  /**
   * Resolves matriculation eligibility into given subject for a student
   * 
   * @param student student
   * @param subjectCode subject code
   * @return matriculation eligibility into given subject for a student
   */
  @Deprecated
  public StudentMatriculationEligibilityResult getStudentMatriculationEligible(Student student, String subjectCode) {
    Curriculum curriculum = student.getCurriculum();
    if (curriculum == null) {
      if (logger.isLoggable(Level.SEVERE)) {
        logger.log(Level.SEVERE, String.format("Failed to resolve matriculation eligibility, student %d is missing curriculum", student.getId()));
      }
      
      return null;
    }
    
    Subject subject = commonController.findSubjectByCode(subjectCode);
    if (subject == null) {
      if (logger.isLoggable(Level.SEVERE)) {
        logger.log(Level.SEVERE, String.format("Failed to resolve matriculation eligibility, subject %s is missing", subjectCode));
      }
      
      return null;
    }
    
    MatriculationEligibilitySubjectMapping mapping = getMatriculationMapping(curriculum.getName(), subject);
    if (mapping == null) {
      mapping = getMatriculationMapping(ANY_CURRICULUM, subject);
    }
    
    if (mapping == null) {
      if (logger.isLoggable(Level.SEVERE)) {
        logger.log(Level.SEVERE, String.format("Failed to resolve matriculation eligibility, no mapping for curriculum %s, subjectCode %s", curriculum == null ? "NULL" : curriculum.getName(), subjectCode));
      }
      
      return null;
    }
    
    String educationTypeCode = mapping.getEducationType();
    String educationSubtypeCode = mapping.getEducationSubtype();
    Integer requirePassingGrades = mapping.getPassingGrades();
    
    EducationType educationType = null;
    EducationSubtype educationSubtype = null;
    
    if (StringUtils.isNotBlank(educationTypeCode)) {
      educationType = commonController.findEducationTypeByCode(educationTypeCode);
      if (educationType == null) {
        if (logger.isLoggable(Level.SEVERE)) {
          logger.log(Level.SEVERE, String.format("Failed to resolve matriculation eligibility, could not find educationType %s", educationTypeCode));
        }
        
        return null;
      }
    }
    
    if (StringUtils.isNotBlank(educationSubtypeCode)) {
      educationSubtype = commonController.findEducationSubtypeByCode(educationType, educationSubtypeCode);
      if (educationSubtype == null) {
        if (logger.isLoggable(Level.SEVERE)) {
          logger.log(Level.SEVERE, String.format("Failed to resolve matriculation eligibility, could not find educationSubtype %s", educationSubtypeCode));
        }

        return null;
      }
    }
    
    int acceptedCourseCount = assessmentController.getAcceptedCourseCount(student, subject, educationType, educationSubtype, curriculum);
    int acceptedTransferCreditCount = assessmentController.getAcceptedTransferCreditCount(student, subject, mapping.getTransferCreditOnlyMandatory(), curriculum);

    if (CollectionUtils.isNotEmpty(mapping.getIncludedSubjects())) {
      for (String includedSubjectCode : mapping.getIncludedSubjects()) {
        Subject includedSubject = commonController.findSubjectByCode(includedSubjectCode);

        if (includedSubject != null) {
          acceptedCourseCount += assessmentController.getAcceptedCourseCount(student, includedSubject, educationType, educationSubtype, curriculum);
          acceptedTransferCreditCount += assessmentController.getAcceptedTransferCreditCount(student, includedSubject, mapping.getTransferCreditOnlyMandatory(), curriculum);
        } else {
          logger.log(Level.SEVERE, String.format("Failed to resolve includedSubject %s for subject %s", includedSubjectCode, subjectCode));
        }
      }
    }
    
    return new StudentMatriculationEligibilityResult(requirePassingGrades, acceptedCourseCount, acceptedTransferCreditCount, acceptedCourseCount + acceptedTransferCreditCount >= requirePassingGrades);
  }
  
  /**
   * Resolves matriculation eligibility into given subject for a student
   * 
   * @param student student
   * @param subjectCode subject code
   * @return matriculation eligibility into given subject for a student
   */
  public StudentMatriculationEligibilityResultOPS2021 getStudentMatriculationEligibleOPS2021(Student student, String subjectCode) throws Exception {
    if (subjectCode == null) {
      if (logger.isLoggable(Level.SEVERE)) {
        logger.log(Level.SEVERE, String.format("Failed to resolve matriculation eligibility, subjectCode was null"));
      }
      
      return null;
    }
    
    if (student.getCurriculum() == null) {
      if (logger.isLoggable(Level.SEVERE)) {
        logger.log(Level.SEVERE, String.format("Failed to resolve matriculation eligibility, student %d is missing curriculum", student.getId()));
      }
      
      return null;
    }
    
    if (commonController.findSubjectByCode(subjectCode) == null) {
      if (logger.isLoggable(Level.SEVERE)) {
        logger.log(Level.SEVERE, String.format("Failed to resolve matriculation eligibility, subject %s is missing", subjectCode));
      }
      
      return null;
    }
    
    /*
     * TORCurriculum is used to fetch the number of mandatory credit points in
     * the subject of interest here.
     */
    TORCurriculum torCurriculum = StudentTORController.getCurriculum(student);
    TORCurriculumSubject curriculumSubject = torCurriculum != null ? torCurriculum.getSubjectByCode(subjectCode) : null;

    if (curriculumSubject == null) {
      if (logger.isLoggable(Level.SEVERE)) {
        logger.log(Level.SEVERE, String.format("Failed to resolve matriculation eligibility, couldn't find subject %s from curriculum", subjectCode));
      }
      
      return null;
    }

    /*
     * Figure out the number of mandatory credit points in the subject. This is a
     * sum of the length of the mandatory modules in the subject itself and the 
     * sum of the lengths of all the included subjects.
     */
    Double subjectMandatoryCreditPointCount = (double) curriculumSubject.getMandatoryModuleLengthSumWithIncludedModules(torCurriculum);
    
//    if (CollectionUtils.isNotEmpty(curriculumSubject.getIncludedSubjects())) {
//      for (String includedSubjectCode : curriculumSubject.getIncludedSubjects()) {
//        TORCurriculumSubject includedCurriculumSubject = torCurriculum.getSubjectByCode(includedSubjectCode);
//        if (includedCurriculumSubject != null) {
//          subjectMandatoryCreditPointCount += includedCurriculumSubject.getMandatoryModuleLengthSum();
//        } else {
//          logger.log(Level.SEVERE, String.format("Failed to resolve includedSubject %s for subject %s", includedSubjectCode, subjectCode));
//        }
//      }
//    }
    
    /* 
     * Constructing the whole TOR primarily for just one subject is a bit of a 
     * sledgehammer move but StudentTOR will handle the improved grades etc 
     * difficult stuff so we don't need to try replicate it here.
     */
    StudentTOR studentTOR = StudentTORController.constructStudentTOR(student, torCurriculum);
    
    TORSubject torSubject = studentTOR.findSubject(subjectCode);

    if (torSubject != null) {
      Double mandatoryCreditPointsCompleted = torSubject.getMandatoryCreditPointsCompletedWithIncludedSubjects(studentTOR, torCurriculum);
  
//      // There's a single case of included subjects atm MAA/MAB include MAY
//      if (CollectionUtils.isNotEmpty(curriculumSubject.getIncludedSubjects())) {
//        for (String includedSubjectCode : curriculumSubject.getIncludedSubjects()) {
//          /*
//           * If StudentTOR has the included subject, add the completed mandatory credit points to the tally.
//           * If it doesn't, then the student probably just has no completed credits of the subject.
//           */
//          TORSubject includedSubject = studentTOR.findSubject(includedSubjectCode);
//          if (includedSubject != null) {
//            mandatoryCreditPointsCompleted += includedSubject.getMandatoryCreditPointsCompleted();
//          }
//        }
//      }
//      
      return new StudentMatriculationEligibilityResultOPS2021(
          subjectMandatoryCreditPointCount,
          mandatoryCreditPointsCompleted,
          mandatoryCreditPointsCompleted >= subjectMandatoryCreditPointCount);
    }
    else {
      // TORSubject was null - it likely has no completed credits -> return zero
      return new StudentMatriculationEligibilityResultOPS2021(
          subjectMandatoryCreditPointCount,
          0d,
          false);
    }
  }
  
  /**
   * Finds a matriculation eligibility subject mapping by curriculum and subject
   * 
   * @param curriculum name of the curriculum
   * @param subject subject entity
   * @return Found matriculation eligibility subject mapping or null if not found
   */
  private MatriculationEligibilitySubjectMapping getMatriculationMapping(String curriculum, Subject subject) {
    MatriculationEligibilityMapping matriculationMapping = getMatriculationEligibilityMapping();    
    MatriculationEligibilityCurriculumMapping curriculumMapping = matriculationMapping.get(curriculum);
    if (curriculumMapping != null) {
      return curriculumMapping.get(subject.getCode());
    }
    
    return null;
  }
  
  /**
   * Returns matriculation eligibility mapping
   * 
   * @return matriculation eligibility mapping
   */
  private MatriculationEligibilityMapping getMatriculationEligibilityMapping() {
    return matriculationEligibilityMapping;
  }
  
  /**
   * Returns true if student is in one of the groups mentioned in the setting.
   * 
   * @param student
   * @return
   */
  public boolean hasGroupEligibility(Student student) {
    if (student != null) {
      String eligibleGroupsStr = SettingUtils.getSettingValue(SETTING_ELIGIBLE_GROUPS);

      if (StringUtils.isNotBlank(eligibleGroupsStr)) {
        String[] split = StringUtils.split(eligibleGroupsStr, ",");

        for (String groupIdentifier : split) {
          if (groupIdentifier.startsWith("STUDYPROGRAMME:")) {
            Long studyProgrammeId = Long.parseLong(groupIdentifier.substring(15));
            if (student.getStudyProgramme() != null && Objects.equals(student.getStudyProgramme().getId(), studyProgrammeId)) {
              return true;
            }
          } else if (groupIdentifier.startsWith("STUDENTGROUP:")) {
            Long studentGroupId = Long.parseLong(groupIdentifier.substring(13));
            StudentGroup studentGroup = studentGroupDAO.findById(studentGroupId);

            if (studentGroupStudentDAO.findByStudentGroupAndStudent(studentGroup, student) != null) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  public List<MatriculationExamEnrollmentChangeLog> listEnrollmentChangeLog(MatriculationExamEnrollment enrollment) {
    return matriculationExamEnrollmentChangeLogDAO.listByEnrollment(enrollment);
  }
  
  public boolean isVisible(MatriculationExam matriculationExam, User user) {
    if (matriculationExam == null || matriculationExam.getStarts() == null || matriculationExam.getEnds() == null || !matriculationExam.isEnrollmentActive()) {
      // If dates are not set, exam enrollment is not active
      return false;
    }

    // TODO: custom date affects all exams...

    UserVariableKey userVariableKey = userVariableKeyDAO.findByVariableKey(USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE);
    String personalExamEnrollmentExpiryStr = userVariableKey != null ? userVariableDAO.findByUserAndKey(user, USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE) : null;
    Date personalExamEnrollmentExpiry = personalExamEnrollmentExpiryStr != null ? DateUtils.endOfDay(new Date(Long.parseLong(personalExamEnrollmentExpiryStr))) : null;

    Date enrollmentStarts = matriculationExam.getStarts();
    Date enrollmentEnds = personalExamEnrollmentExpiry == null ? matriculationExam.getEnds() : 
      new Date(Math.max(matriculationExam.getEnds().getTime(), personalExamEnrollmentExpiry.getTime()));

    Date currentDate = new Date();
    return currentDate.after(enrollmentStarts) && currentDate.before(enrollmentEnds);
  }

  public boolean isEligible(Student student, MatriculationExam matriculationExam) {
    return student == null ? false :
      isVisible(matriculationExam, student) && hasGroupEligibility(student);
  }

  public List<MatriculationExam> listExamsByStudent(Student student, MatriculationExamListFilter filter) {
    List<MatriculationExam> allExams = matriculationExamDAO.listAll();
    List<MatriculationExam> examsByStudent = new ArrayList<>();
    
    for (MatriculationExam exam : allExams) {
      boolean isEnrolled = matriculationExamEnrollmentDAO.findByExamAndStudent(exam, student) != null;

      // If the filter is asking for the past (aka enrolled) to be listed and the student is enrolled, add to list
      if ((filter == MatriculationExamListFilter.ALL || filter == MatriculationExamListFilter.PAST) && isEnrolled) {
        examsByStudent.add(exam);
        continue;
      }      
      
      boolean isEligible = isEligible(student, exam);
      
      // If the filter is asking for eligible exams to be listed and the student is eligible but not enrolled, add to list
      if ((filter == MatriculationExamListFilter.ALL || filter == MatriculationExamListFilter.ELIGIBLE) && isEligible && !isEnrolled) {
        examsByStudent.add(exam);
        continue;
      }
    }
    
    return examsByStudent;
  }
  
//  /**
//   * Sets the state for enrollment and creates relevant change log entry.
//   * 
//   * If the enrollment is being set to CONFIRMED, also sets up the student projects.
//   * 
//   * MAKE SURE MatriculationExamAttendances are persisted prior as they may need to 
//   * be read if the state is CONFIRMED.
//   * 
//   * @param enrollment
//   * @param state
//   * @param modifier
//   * @return
//   */
//  public MatriculationExamEnrollment setEnrollmentState(MatriculationExamEnrollment enrollment, MatriculationExamEnrollmentState state, User modifier) {
//    // If state is being set to CONFIRMED, set up the student projects
//    if (state == MatriculationExamEnrollmentState.CONFIRMED) {
//      List<MatriculationExamAttendance> attendances = matriculationExamAttendanceDAO.listByEnrollment(enrollment);
//      for (MatriculationExamAttendance attendance : attendances) {
//        
//        // STUDENT CANNOT CREATE THESE
//        
//        // createOrUpdateStudentProject(attendance, enrollment.getStudent(), attendance.getSubject(), attendance.isMandatory(), modifier);
//      }
//    }
//    
//    // Update state
//    enrollment = matriculationExamEnrollmentDAO.updateState(enrollment, state);
//
//    // Make a log entry for state change with new state
//    matriculationExamEnrollmentChangeLogDAO.create(enrollment, modifier, MatriculationExamEnrollmentChangeLogType.STATE_CHANGED, state, null);
//    
//    return enrollment;
//  }
//
//  /**
//   * Creates or updates StudentProject and ProjectAssessment for given 
//   * MatriculationExamAttendance.
//   * 
//   * This should be used when the MatriculationExamEnrollment reaches
//   * a state where it is finalized and ready to be sent forwards.
//   * 
//   * @param examAttendance
//   * @param student
//   * @param subject
//   * @param mandatory
//   * @param loggedUser
//   */
//  public void createOrUpdateStudentProject(MatriculationExamAttendance examAttendance, Student student, MatriculationExamSubject subject, boolean mandatory, StaffMember loggedUser) {
//    MatriculationExamSubjectSettings subjectSettings = matriculationExamSubjectSettingsDAO.findBy(examAttendance.getEnrollment().getExam(), subject);
//    if (subjectSettings == null || subjectSettings.getProject() == null) {
//      // We cannot really do anything if the settings aren't in place
//      return;
//    }
//    
//    CourseOptionality projectOptionality = mandatory ? CourseOptionality.MANDATORY : CourseOptionality.OPTIONAL;
//    
//    Project project = subjectSettings.getProject();
//    StudentProject studentProject;
//    
//    if (examAttendance != null && 
//        examAttendance.getProjectAssessment() != null && 
//        BooleanUtils.isFalse(examAttendance.getProjectAssessment().getArchived()) &&
//        examAttendance.getProjectAssessment().getStudentProject() != null &&
//        BooleanUtils.isFalse(examAttendance.getProjectAssessment().getStudentProject().getArchived())) {
//      // Use the studentproject from the projectassessment if it exists
//      studentProject = examAttendance.getProjectAssessment().getStudentProject();
//    } else {
//      // Resolve studentProject from the project in the settings
//      List<StudentProject> studentProjects = studentProjectDAO.listBy(student, project, TSB.IGNORE);
//
//      // Find first non-archived project
//      studentProject = studentProjects.stream()
//          .filter(studentProject1 -> BooleanUtils.isFalse(studentProject1.getArchived()))
//          .findFirst()
//          .orElse(null);
//      
//      if (studentProject == null) {
//        // No unarchived student project was found so try to use any other
//        studentProject = studentProjects.isEmpty() ? null : studentProjects.get(0);
//        
//        if (studentProject != null && BooleanUtils.isTrue(studentProject.getArchived())) {
//          studentProjectDAO.unarchive(studentProject);
//        }
//      }
//    }
//    
//    if (studentProject == null) {
//      // No matching student project was found so create a new one
//      studentProject = studentProjectDAO.create(student, project.getName(), project.getDescription(), 
//          project.getOptionalStudiesLength().getUnits(), project.getOptionalStudiesLength().getUnit(), projectOptionality, loggedUser, project);
//      
//      Set<Tag> tags = new HashSet<>();
//      for (Tag tag : project.getTags()) {
//        tags.add(tag);
//      }
//      studentProjectDAO.updateTags(studentProject, tags);
//      
//      List<ProjectModule> projectModules = project.getProjectModules();
//      for (ProjectModule projectModule : projectModules) {
//        studentProjectModuleDAO.create(studentProject, projectModule.getModule(), null,
//            CourseOptionality.getOptionality(projectModule.getOptionality().getValue()));
//      }
//
//      List<ProjectSubjectCourse> projectSubjectCourses = project.getProjectSubjectCourses();
//      for (ProjectSubjectCourse projectSubjectCourse : projectSubjectCourses) {
//        studentProjectSubjectCourseDAO.create(studentProject, projectSubjectCourse.getSubject(), projectSubjectCourse.getCourseNumber(), null,
//            CourseOptionality.getOptionality(projectSubjectCourse.getOptionality().getValue()));
//      }
//    } else {
//      studentProject = studentProjectDAO.updateOptionality(studentProject, projectOptionality);
//    }
//
//    MatriculationExam matriculationExam = examAttendance.getEnrollment().getExam();
//    
//    if (matriculationExam != null && matriculationExam.getSignupGrade() != null && subjectSettings.getExamDate() != null && examAttendance.getProjectAssessment() == null) {
//      // Add the exam date
//      ProjectAssessment projectAssessment = projectAssessmentDAO.create(studentProject, loggedUser, matriculationExam.getSignupGrade(), subjectSettings.getExamDate(), "");
//      // Link the project assessment to this exam atten dance
//      matriculationExamAttendanceDAO.updateProjectAssessment(examAttendance, projectAssessment);
//    }
//  }

  public MatriculationExamStudentStatus translateState(MatriculationExamEnrollmentState state) {
    switch (state) {
      case PENDING:
        return MatriculationExamStudentStatus.PENDING;
      case SUPPLEMENTATION_REQUEST:
        return MatriculationExamStudentStatus.SUPPLEMENTATION_REQUEST;
      case SUPPLEMENTED:
        return MatriculationExamStudentStatus.SUPPLEMENTED;
      case APPROVED:
        return MatriculationExamStudentStatus.APPROVED;
      case REJECTED:
        return MatriculationExamStudentStatus.REJECTED;
      case CONFIRMED:
        return MatriculationExamStudentStatus.CONFIRMED;
      default:
        throw new IllegalArgumentException();
    }
  }

  public MatriculationExamEnrollmentState translateState(MatriculationExamStudentStatus state) {
    switch (state) {
      case PENDING:
        return MatriculationExamEnrollmentState.PENDING;
      case SUPPLEMENTATION_REQUEST:
        return MatriculationExamEnrollmentState.SUPPLEMENTATION_REQUEST;
      case SUPPLEMENTED:
        return MatriculationExamEnrollmentState.SUPPLEMENTED;
      case APPROVED:
        return MatriculationExamEnrollmentState.APPROVED;
      case REJECTED:
        return MatriculationExamEnrollmentState.REJECTED;
      case CONFIRMED:
        return MatriculationExamEnrollmentState.CONFIRMED;
      case ELIGIBLE:
      case NOT_ELIGIBLE:
      default:
        throw new IllegalArgumentException();
    }
  }

}

