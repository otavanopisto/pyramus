package fi.otavanopisto.pyramus.rest.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
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
  private CourseController courseController;

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
      curriculum = null;
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
    
    int acceptedCourseCount = getAcceptedCourseCount(student, subject, educationType, educationSubtype, curriculum);
    int acceptedTransferCreditCount = getAcceptedTransferCreditCount(student, subject, mapping.getTransferCreditOnlyMandatory(), curriculum);
    return new StudentMatriculationEligibilityResult(requirePassingGrades, acceptedCourseCount, acceptedTransferCreditCount, acceptedCourseCount + acceptedTransferCreditCount >= requirePassingGrades);
  }
  
  /**
   * Counts how many courses with passing grade student has with given subject, education type, education subtype and curriculum.
   * 
   * if educationType, educationSubtype or curriculum is defined as null, all courses are accepted
   * 
   * @param student student entity
   * @param subject subject entity
   * @param educationType education type entity
   * @param educationSubtype education subtype entity
   * @param curriculum curriculum entity
   * @return student passing grade count in matching courses
   */
  private int getAcceptedCourseCount(Student student, Subject subject, EducationType educationType, EducationSubtype educationSubtype, Curriculum curriculum) {
    int result = 0;
    Set<Course> courses = getAcceptedCourses(student, subject, educationType, educationSubtype, curriculum);
    
    for (Course course : courses) {
      boolean hasPassedGrade = assessmentController.listByCourseAndStudent(course, student).stream().map(CourseAssessment::getGrade).filter(Grade::getPassingGrade).count() > 0;
      
      if (hasPassedGrade) {
        result++;
      }
    }
    
    return result;
  }
  
  /**
   * Counts how many transfer credits with passing grade student has with given curriculum, subject and mandatority
   * 
   * if curriculum or subject is defined as null filters are ignored. 
   * If transferCreditOnlyMandatory is defined as true, only transfer credits with CourseOptionality MANDATORY are accepted
   * 
   * @param student student entity
   * @param subject subject entity
   * @param transferCreditOnlyMandatory 
   * @param curriculum curriculum entity
   * @return student passing grade count in matching transfer credits
   */
  private int getAcceptedTransferCreditCount(Student student, Subject subject, Boolean transferCreditOnlyMandatory, Curriculum curriculum) {
    CourseOptionality transferCreditOptionality = transferCreditOnlyMandatory ? CourseOptionality.MANDATORY : null;
    
    return (int) assessmentController.listTransferCreditsByStudentAndSubjectAndCurriculumAndOptionality(student, subject, curriculum, transferCreditOptionality).stream()
      .filter(transferCredit -> transferCredit.getGrade().getPassingGrade())
      .count();
  }
  
  /**
   * Returns set of student courses matching subject, educationType, educationSubtype and curriculum.
   * 
   * if educationType, educationSubtype or curriculum is defined as null, filters are ignored
   * 
   * @param student student entity
   * @param subject subject entity
   * @param educationType education type entity
   * @param educationSubtype education subtype entity
   * @param curriculum curriculum entity
   * @return set of matched courses
   */
  private Set<Course> getAcceptedCourses(Student student, Subject subject, EducationType educationType, EducationSubtype educationSubtype, Curriculum curriculum) {
    List<CourseStudent> courseStudents = null;
    
    if (curriculum != null) {      
      courseStudents = courseController.listByStudentAndCourseSubjectAndCourseCurriculum(student, subject, curriculum);
    } else {
      courseStudents = courseController.listByStudentAndCourseSubject(student, subject);
    }
    
    return courseStudents.stream()
      .filter(courseStudent -> filterCourseStudentByEducationType(educationType, educationSubtype, courseStudent))
      .map(CourseStudent::getCourse)
      .distinct()
      .collect(Collectors.toSet());
  }

  /**
   * Returns whether course student course has appropriate educationType and educationSubtype
   * 
   * if educationType is null, all education types are accepted, if educationSubtype is null all subtypes are accepted
   * 
   * @param educationType education type or null for all education types
   * @param educationSubtype education subtype or null for all education subtypes
   * @param courseStudent course student
   * @return whether course student course has appropriate educationType and educationSubtype
   */
  private boolean filterCourseStudentByEducationType(EducationType educationType, EducationSubtype educationSubtype, CourseStudent courseStudent) {
    Course course = courseStudent.getCourse();

     if (educationType != null) {
       CourseEducationType courseEducationType = course.getCourseEducationTypes().stream()
         .filter(filterEducationType -> {
           return filterEducationType.getEducationType().getId().equals(educationType.getId());
         })
         .findFirst()
         .orElse(null);
       
       if (courseEducationType == null) {
         return false;
       }
       
       if (educationSubtype != null) {
         Set<Long> courseEducationSubtypes = courseEducationType.getCourseEducationSubtypes().stream().map(CourseEducationSubtype::getEducationSubtype).map(EducationSubtype::getId).collect(Collectors.toSet());
         return courseEducationSubtypes.contains(educationSubtype.getId());
       }
     }

     return true;
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

