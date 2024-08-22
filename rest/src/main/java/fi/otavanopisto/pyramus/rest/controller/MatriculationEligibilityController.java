package fi.otavanopisto.pyramus.rest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentChangeLogDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupStudentDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
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
import fi.otavanopisto.pyramus.rest.matriculation.MatriculationEligibilityCurriculumMapping;
import fi.otavanopisto.pyramus.rest.matriculation.MatriculationEligibilityMapping;
import fi.otavanopisto.pyramus.rest.matriculation.MatriculationEligibilitySubjectMapping;
import fi.otavanopisto.pyramus.rest.model.MatriculationExamStudentStatus;
import fi.otavanopisto.pyramus.tor.StudentTOR;
import fi.otavanopisto.pyramus.tor.StudentTORController;
import fi.otavanopisto.pyramus.tor.TORSubject;

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

    /* 
     * Constructing the whole TOR primarily for just one subject is a bit of a 
     * sledgehammer move but StudentTOR will handle the improved grades etc 
     * difficult stuff so we don't need to try replicate it here.
     */
    StudentTOR studentTOR = StudentTORController.constructStudentTOR(student, true);
    
    TORSubject torSubject = studentTOR.findSubject(subjectCode);
    
    Double mandatoryCreditPointCount = torSubject.getMandatoryCreditPointCount();
    Double mandatoryCreditPointsCompleted = torSubject.getMandatoryCreditPointsCompleted();

    // There's a single case of included subjects atm MAA/MAB include MAY
    if (CollectionUtils.isNotEmpty(mapping.getIncludedSubjects())) {
      for (String includedSubjectCode : mapping.getIncludedSubjects()) {
        TORSubject includedSubject = studentTOR.findSubject(includedSubjectCode);
        
        if (includedSubject != null) {
          mandatoryCreditPointCount += includedSubject.getMandatoryCreditPointCount();
          mandatoryCreditPointsCompleted += includedSubject.getMandatoryCreditPointsCompleted();
        } else {
          logger.log(Level.SEVERE, String.format("Failed to resolve includedSubject %s for subject %s", includedSubjectCode, subjectCode));
        }
      }
    }
    
    return new StudentMatriculationEligibilityResultOPS2021(
        mandatoryCreditPointCount,
        mandatoryCreditPointsCompleted,
        mandatoryCreditPointsCompleted >= mandatoryCreditPointCount);
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
      boolean isEnrolled = matriculationExamEnrollmentDAO.findLatestByExamAndStudent(exam, student) != null;

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

