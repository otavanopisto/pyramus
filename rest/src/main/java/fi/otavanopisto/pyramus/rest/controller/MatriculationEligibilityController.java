package fi.otavanopisto.pyramus.rest.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.rest.matriculation.MatriculationEligibilityCurriculumMapping;
import fi.otavanopisto.pyramus.rest.matriculation.MatriculationEligibilityMapping;
import fi.otavanopisto.pyramus.rest.matriculation.MatriculationEligibilitySubjectMapping;

/**
 * Controller for determining 
 * 
 * @author Antti Leppä
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class MatriculationEligibilityController {
  
  private static final String ANY_CURRICULUM = "any";
  
  @Inject
  private Logger logger;

  @Inject
  private CommonController commonController;
  
  @Inject
  private AssessmentController assessmentController;
  
  private MatriculationEligibilityMapping matriculationEligibilityMapping; 
  
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
  
}

