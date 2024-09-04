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
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.framework.DateUtils;
import fi.otavanopisto.pyramus.framework.SettingUtils;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamListFilter;
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
     * Figure out the number of mandatory credit points in the subject. This is a
     * sum of the length of the mandatory modules in the subject itself and the 
     * sum of the lengths of all the included subjects.
     */
    Double subjectMandatoryCreditPointCount = (double) curriculumSubject.getMandatoryModuleLengthSumWithIncludedModules(torCurriculum);
    
    /* 
     * Constructing the whole TOR primarily for just one subject is a bit of a 
     * sledgehammer move but StudentTOR will handle the improved grades etc 
     * difficult stuff so we don't need to try replicate it here.
     */
    StudentTOR studentTOR = StudentTORController.constructStudentTOR(student, torCurriculum);
    
    TORSubject torSubject = studentTOR.findSubject(subjectCode);

    if (torSubject != null) {
      Double mandatoryCreditPointsCompleted = torSubject.getMandatoryCreditPointsCompletedWithIncludedSubjects(studentTOR, torCurriculum);
  
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

