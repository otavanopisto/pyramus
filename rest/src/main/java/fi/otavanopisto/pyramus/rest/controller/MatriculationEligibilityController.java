package fi.otavanopisto.pyramus.rest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.PyramusConsts;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentChangeLogDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupStudentDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentChangeLog;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.framework.DateUtils;
import fi.otavanopisto.pyramus.framework.SettingUtils;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamListFilter;
import fi.otavanopisto.pyramus.rest.model.MatriculationExamStudentStatus;
import fi.otavanopisto.pyramus.tor.StudentTOR;
import fi.otavanopisto.pyramus.tor.StudentTORController;
import fi.otavanopisto.pyramus.tor.StudentTORController.StudentTORHandling;
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

  private static final String USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE = PyramusConsts.Matriculation.USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE;
  private static final String SETTING_ELIGIBLE_GROUPS = PyramusConsts.Matriculation.SETTING_ELIGIBLE_GROUPS;
  
  @Inject
  private Logger logger;

  @Inject
  private CommonController commonController;
  
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
     * Figure out the number of mandatory credit points in the subject. This is 
     * either explicitly set on the curriculum subject or it is calculated as the
     * sum of the length of the mandatory modules in the subject itself and the 
     * sum of the lengths of all the included subjects.
     */
    Double subjectMandatoryCreditPointCount = curriculumSubject.getMatriculationRequiredStudies() != null
        ? curriculumSubject.getMatriculationRequiredStudies()
        : (double) curriculumSubject.getMandatoryModuleLengthSumWithIncludedModules(torCurriculum);
    
    /* 
     * Constructing the whole TOR primarily for just one subject is a bit of a 
     * sledgehammer move but StudentTOR will handle the improved grades etc 
     * difficult stuff so we don't need to try replicate it here.
     */
    StudentTOR studentTOR = StudentTORController.constructStudentTOR(student, torCurriculum, StudentTORHandling.CURRICULUM_MOVE_INCLUDED);
    
    TORSubject torSubject = studentTOR.findSubject(subjectCode);

    if (torSubject != null) {
      /*
       * If the matriculationRequiredStudies is set, we take the total number of
       * completed credit points, otherwise take the number of credit points from
       * completed and mandatory courses.
       */
      Double creditPointsCompleted = curriculumSubject.getMatriculationRequiredStudies() != null
          ? torSubject.getTotalCreditPointsCompleted()
          : torSubject.getMandatoryCreditPointsCompleted();
  
      return new StudentMatriculationEligibilityResultOPS2021(
          subjectMandatoryCreditPointCount,
          creditPointsCompleted,
          creditPointsCompleted >= subjectMandatoryCreditPointCount);
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
  
  /**
   * Returns true if the student may enroll to given exam.
   * 
   * In order to be able to enroll
   * - The exam needs to have enrollment active
   * - The exam needs to have specified both the enrollment start and end dates
   * - The current date must be later than the exam's enrollment start date but 
   *   earlier than the either the end date or the student's end date override.
   * - The student needs to be part of a group that has matriculation set active.
   * 
   * @param student
   * @param matriculationExam
   * @return
   */
  public boolean isEnrollableByStudent(MatriculationExam matriculationExam, Student student) {
    if (student == null || matriculationExam == null || !matriculationExam.isEnrollmentActive() || matriculationExam.getStarts() == null || matriculationExam.getEnds() == null) {
      // If dates are not set, exam enrollment is not active
      return false;
    }

    // TODO: custom date affects all exams...

    UserVariableKey userVariableKey = userVariableKeyDAO.findByVariableKey(USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE);
    String personalExamEnrollmentExpiryStr = userVariableKey != null ? userVariableDAO.findByUserAndKey(student, USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE) : null;
    Date personalExamEnrollmentExpiry = personalExamEnrollmentExpiryStr != null ? DateUtils.endOfDay(new Date(Long.parseLong(personalExamEnrollmentExpiryStr))) : null;

    Date enrollmentStarts = matriculationExam.getStarts();
    Date enrollmentEnds = DateUtils.max(matriculationExam.getEnds(), personalExamEnrollmentExpiry);

    Date currentDate = new Date();
    if (currentDate.before(enrollmentStarts) || currentDate.after(enrollmentEnds)) {
      // Current date either before start or after end, return false
      return false;
    }

    // Everything fine so far, check for the group eligibility
    return hasGroupEligibility(student);
  }

  /**
   * Returns true if the student may update their exam enrollment.
   * 
   * In order to be able to update the enrollment
   * - The exam needs to have enrollment active
   * - The exam needs to have specified both the enrollment start and end dates
   * - The current date must be later than the exam's enrollment start date but 
   *   earlier than one of the following:
   *   - the enrollment end date on exam
   *   - the confirmationDate on exam
   *   - the student's end date override.
   * - The student needs to be part of a group that has matriculation set active.
   * 
   * Thus the check is quite similar to the check for being able to enroll to
   * an exam. The only difference is that the confirmationDate allows for 
   * modifications after the enrollment phase has concluded.
   * 
   * @param student
   * @param matriculationExam
   * @return
   */
  public boolean isUpdatableByStudent(MatriculationExamEnrollment enrollment) {
    MatriculationExam matriculationExam = enrollment != null ? enrollment.getExam() : null;
    Student student = enrollment != null ? enrollment.getStudent() : null;

    if (student == null || matriculationExam == null || !matriculationExam.isEnrollmentActive() || matriculationExam.getStarts() == null || matriculationExam.getEnds() == null) {
      // If dates are not set, exam enrollment is not active
      return false;
    }

    // TODO: custom date affects all exams...

    UserVariableKey userVariableKey = userVariableKeyDAO.findByVariableKey(USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE);
    String personalExamEnrollmentExpiryStr = userVariableKey != null ? userVariableDAO.findByUserAndKey(student, USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE) : null;
    Date personalExamEnrollmentExpiry = personalExamEnrollmentExpiryStr != null ? DateUtils.endOfDay(new Date(Long.parseLong(personalExamEnrollmentExpiryStr))) : null;

    Date enrollmentStarts = matriculationExam.getStarts();
    Date updatePeriodEnds = DateUtils.max(matriculationExam.getConfirmationDate(), DateUtils.max(matriculationExam.getEnds(), personalExamEnrollmentExpiry));

    Date currentDate = new Date();
    if (currentDate.before(enrollmentStarts) || currentDate.after(updatePeriodEnds)) {
      // Current date either before start or after end, return false
      return false;
    }

    // Everything fine so far, check for the group eligibility
    return hasGroupEligibility(student);
  }
  
  /**
   * Lists exams by given student based on the filter.
   * 
   * Filter types and effects of them:
   * - ELIGIBLE: Lists exams that are currently open for enrollments and the student is eligible to enroll to them
   * - PAST: Lists exams that the student has previously enrolled to excluding exams covered by ELIGIBLE filter
   * - ALL: Lists exams matching either of the previous filters
   * 
   * @param student
   * @param filter
   * @return
   */
  public List<MatriculationExam> listExamsByStudent(Student student, MatriculationExamListFilter filter) {
    List<MatriculationExam> allExams = matriculationExamDAO.listAll();
    List<MatriculationExam> examsByStudent = new ArrayList<>();
    
    for (MatriculationExam exam : allExams) {
      boolean isEligible = isEnrollableByStudent(exam, student);
      
      // Updatable has no separate state so we consider updatable enrollments as eligible too - fix later if needed
      if (!isEligible) {
        MatriculationExamEnrollment examEnrollment = matriculationExamEnrollmentDAO.findByExamAndStudent(exam, student);
        isEligible = examEnrollment != null && isUpdatableByStudent(examEnrollment);
      }

      // If the filter is asking for eligible exams to be listed and the student is eligible, add to list
      if ((filter == MatriculationExamListFilter.ALL || filter == MatriculationExamListFilter.ELIGIBLE) && isEligible) {
        examsByStudent.add(exam);
        continue;
      }

      boolean isEnrolled = matriculationExamEnrollmentDAO.findByExamAndStudent(exam, student) != null;
      
      // If the filter is asking for the past (aka enrolled) to be listed and the student is enrolled but is not eligible, add to list
      if ((filter == MatriculationExamListFilter.ALL || filter == MatriculationExamListFilter.PAST) && isEnrolled && !isEligible) {
        examsByStudent.add(exam);
        continue;
      }
    }
    
    return examsByStudent;
  }
  
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

