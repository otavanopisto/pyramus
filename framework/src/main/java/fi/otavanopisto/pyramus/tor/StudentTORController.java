package fi.otavanopisto.pyramus.tor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.PyramusConsts;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.students.StudentSubjectGradeDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditType;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade;
import fi.otavanopisto.pyramus.koski.KoskiConsts;
import fi.otavanopisto.pyramus.koski.StudentSubjectSelections;
import fi.otavanopisto.pyramus.tor.curriculum.TORCurriculum;
import fi.otavanopisto.pyramus.tor.curriculum.TORCurriculumSubject;

public class StudentTORController {

  public enum StudentTORFlags {
    /**
     * Use Curriculum and calculate summary fields based on it.
     */
    CURRICULUM,
    
    /**
     * Use Curriculum and calculate summary fields based on it,
     * if there are subjects that are included in other subjects,
     * the credits are moved under the included subjects. If the
     * subjects don't exist though, the subject is still included
     * as an individual subject. Use CURRICULUM with this option.
     */
    MOVE_INCLUDED,

    /**
     * Loads Student's subject selections and uses them to filter
     * the resulting TOR from subjects that should be excluded
     * from it. Every StudyProgramme doesn't necessarily have
     * subject selections or they are not specified for them so
     * results may vary.
     */
    USE_SUBJECT_SELECTIONS;
    
    // States when loading Student's Curriculum is required - although it might not exist
    public final static EnumSet<StudentTORFlags> LOAD_CURRICULUM = EnumSet.of(CURRICULUM, MOVE_INCLUDED);
  }
  
  /**
   * Constructs table of records for given student. If subject summary fields
   * (subject completed, mandatory course count and completed mandatory course count)
   * are needed, include a flag that requires Curriculum to be loaded. If they're not 
   * needed, omitting curriculum loading leads to less processing.
   * 
   * @param student Student
   * @param flags to specify how the TOR should be formed
   * @return Student's TOR
   * @throws Exception if something goes wrong
   */
  public static StudentTOR constructStudentTOR(Student student, EnumSet<StudentTORFlags> flags) throws Exception {
    TORCurriculum curriculum = !Collections.disjoint(StudentTORFlags.LOAD_CURRICULUM, flags) ? getCurriculum(student) : null;
    return constructStudentTOR(student, curriculum, flags);
  }

  /**
   * Constructs table of records for given student. If subject summary fields
   * (subject completed, mandatory course count and completed mandatory course count)
   * are needed, pass the curriculum parameter. If they're not needed, curriculum can
   * be null for less processing.
   * 
   * @param student Student
   * @param curriculum set if subject summary fields are needed, can be null if not
   * @param flags to specify how the TOR should be formed
   * @return Student's TOR
   * @throws Exception if something goes wrong
   */
  public static StudentTOR constructStudentTOR(Student student, TORCurriculum curriculum, EnumSet<StudentTORFlags> flags) throws Exception {
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    CreditLinkDAO creditLinkDAO = DAOFactory.getInstance().getCreditLinkDAO();
    
    List<CourseAssessment> courseAssessmentsByStudent = courseAssessmentDAO.listByStudent(student);
    List<TransferCredit> transferCreditsByStudent = transferCreditDAO.listByStudent(student);
    List<CreditLink> linkedCourseAssessmentByStudent = creditLinkDAO.listByStudentAndType(student, CreditType.CourseAssessment);
    List<CreditLink> linkedTransferCreditsByStudent = creditLinkDAO.listByStudentAndType(student, CreditType.TransferCredit);

    StudentTOR tor = new StudentTOR();
    TORProblems problems = tor.getProblems();

    Map<String, Set<String>> subjectCodeTranslations = (flags.contains(StudentTORFlags.MOVE_INCLUDED) && curriculum != null) 
        ? getReverseIncludedSubjectsMapping(curriculum) : null;
    
    // Collect Subjects

    StudentSubjectSelections studentSubjectSelections = flags.contains(StudentTORFlags.USE_SUBJECT_SELECTIONS) ? loadStudentSubjectSelections(student) : null;
    
    for (CourseAssessment courseAssessment : courseAssessmentsByStudent) {
      if (courseAssessment.getCourseStudent() != null && courseAssessment.getCourseStudent().getCourse() != null) {
        Subject subject = courseAssessment.getSubject();
        
        if (studentSubjectSelections == null || studentSubjectSelections.isIncludedInStudies(subject.getCode())) {
          addTORSubject(tor, student, subject, curriculum);
        }
      }
    }
    
    for (TransferCredit transferCredit : transferCreditsByStudent) {
      Subject subject = transferCredit.getSubject();
      if (studentSubjectSelections == null || studentSubjectSelections.isIncludedInStudies(subject.getCode())) {
        addTORSubject(tor, student, subject, curriculum);
      }
    }
    
    for (CreditLink linkedCourseAssessment : linkedCourseAssessmentByStudent) {
      CourseAssessment courseAssessment = (CourseAssessment) linkedCourseAssessment.getCredit();
      if (courseAssessment != null && courseAssessment.getCourseStudent() != null && courseAssessment.getCourseStudent().getCourse() != null) {
        Subject subject = courseAssessment.getSubject();
        if (studentSubjectSelections == null || studentSubjectSelections.isIncludedInStudies(subject.getCode())) {
          addTORSubject(tor, student, subject, curriculum);
        }
      }
    }
    
    for (CreditLink linkedTransferCredit : linkedTransferCreditsByStudent) {
      TransferCredit transferCredit = (TransferCredit) linkedTransferCredit.getCredit();
      if (transferCredit != null) {
        Subject subject = transferCredit.getSubject();
        if (studentSubjectSelections == null || studentSubjectSelections.isIncludedInStudies(subject.getCode())) {
          addTORSubject(tor, student, subject, curriculum);
        }
      }
    }
    
    // Collect Credits
    
    for (CourseAssessment courseAssessment : courseAssessmentsByStudent) {
      if (courseAssessment.getCourseStudent() != null && courseAssessment.getCourseStudent().getCourse() != null) {
        Subject subject = courseAssessment.getSubject();
        Integer courseNumber = courseAssessment.getCourseNumber();
        Set<Curriculum> creditCurriculums = courseAssessment.getCourseStudent().getCourse().getCurriculums();
        
        EducationalLength courseEducationalLength = courseAssessment.getCourseLength();
        Double courseLength = courseEducationalLength != null ? courseEducationalLength.getUnits() : null;
        TORCourseLengthUnit courseLengthUnit = courseEducationalLength != null ? getCourseLengthUnit(courseEducationalLength.getUnit(), problems) : null;
        boolean mandatory = isMandatory(courseAssessment);
        if (studentSubjectSelections == null || studentSubjectSelections.isIncludedInStudies(subject.getCode())) {
          addTORCredit(tor, student, subject, courseAssessment, courseNumber, creditCurriculums, mandatory, courseLength, courseLengthUnit, subjectCodeTranslations, problems);
        }
      }
    }
    
    for (TransferCredit transferCredit : transferCreditsByStudent) {
      Subject subject = transferCredit.getSubject();
      Integer courseNumber = transferCredit.getCourseNumber();
      Set<Curriculum> creditCurriculums = transferCredit.getCurriculum() != null ? 
          new HashSet<>(Arrays.asList(transferCredit.getCurriculum())) : Collections.emptySet();

      EducationalLength courseEducationalLength = transferCredit.getCourseLength();
      Double courseLength = courseEducationalLength != null ? courseEducationalLength.getUnits() : null;
      TORCourseLengthUnit courseLengthUnit = courseEducationalLength != null ? getCourseLengthUnit(courseEducationalLength.getUnit(), problems) : null;
      boolean mandatory = transferCredit.getOptionality() == CourseOptionality.MANDATORY;
      if (studentSubjectSelections == null || studentSubjectSelections.isIncludedInStudies(subject.getCode())) {
        addTORCredit(tor, student, subject, transferCredit, courseNumber, creditCurriculums, mandatory, courseLength, courseLengthUnit, subjectCodeTranslations, problems);
      }
    }
    
    for (CreditLink linkedCourseAssessment : linkedCourseAssessmentByStudent) {
      CourseAssessment courseAssessment = (CourseAssessment) linkedCourseAssessment.getCredit();
      if (courseAssessment != null && courseAssessment.getCourseStudent() != null && courseAssessment.getCourseStudent().getCourse() != null) {
        Subject subject = courseAssessment.getSubject();
        Integer courseNumber = courseAssessment.getCourseNumber();
        Set<Curriculum> creditCurriculums = courseAssessment.getCourseStudent().getCourse().getCurriculums();

        EducationalLength courseEducationalLength = courseAssessment.getCourseLength();
        Double courseLength = courseEducationalLength != null ? courseEducationalLength.getUnits() : null;
        TORCourseLengthUnit courseLengthUnit = courseEducationalLength != null ? getCourseLengthUnit(courseEducationalLength.getUnit(), problems) : null;
        boolean mandatory = isMandatory(courseAssessment);
        if (studentSubjectSelections == null || studentSubjectSelections.isIncludedInStudies(subject.getCode())) {
          addTORCredit(tor, student, subject, courseAssessment, courseNumber, creditCurriculums, mandatory, courseLength, courseLengthUnit, subjectCodeTranslations, problems);
        }
      }
    }
    
    for (CreditLink linkedTransferCredit : linkedTransferCreditsByStudent) {
      TransferCredit transferCredit = (TransferCredit) linkedTransferCredit.getCredit();
      if (transferCredit != null) {
        Subject subject = transferCredit.getSubject();
        Integer courseNumber = transferCredit.getCourseNumber();
        Set<Curriculum> creditCurriculums = transferCredit.getCurriculum() != null ? 
            new HashSet<>(Arrays.asList(transferCredit.getCurriculum())) : Collections.emptySet();
        
        EducationalLength courseEducationalLength = transferCredit.getCourseLength();
        Double courseLength = courseEducationalLength != null ? courseEducationalLength.getUnits() : null;
        TORCourseLengthUnit courseLengthUnit = courseEducationalLength != null ? getCourseLengthUnit(courseEducationalLength.getUnit(), problems) : null;
        boolean mandatory = transferCredit.getOptionality() == CourseOptionality.MANDATORY;
        if (studentSubjectSelections == null || studentSubjectSelections.isIncludedInStudies(subject.getCode())) {
          addTORCredit(tor, student, subject, transferCredit, courseNumber, creditCurriculums, mandatory, courseLength, courseLengthUnit, subjectCodeTranslations, problems);
        }
      }
    }

    tor.postProcess(curriculum);
    return tor;
  }

  /**
   * Returns TORCurriculum for student or null if one cannot be resolved.
   * 
   * @param student
   * @return
   * @throws Exception if parsing curriculum json fails
   */
  public static TORCurriculum getCurriculum(Student student) throws Exception {
    if (student.getCurriculum() != null) {
      String curriculumName = student.getCurriculum().getName();
      
      return getCurriculum(curriculumName);
    }
    
    return null;
  }
  
  /**
   * Returns TORCurriculum for curriculum name or null if one cannot be resolved.
   * 
   * @param student
   * @return
   * @throws Exception if parsing curriculum json fails
   */
  public static TORCurriculum getCurriculum(String curriculumName) throws Exception {
    if (StringUtils.isNotBlank(curriculumName)) {      
      String curriculumFile = null;
      switch (curriculumName) {
        case PyramusConsts.OPS_2018: curriculumFile = "curriculum_2018.json"; break;
        case PyramusConsts.OPS_2021: curriculumFile = "curriculum_2021.json"; break;
      }

      if (curriculumFile != null) {
        ObjectMapper objectMapper = new ObjectMapper();
        String curriculumJsonLocation = "fi/otavanopisto/pyramus/tor/" + curriculumFile;
        InputStream curriculumJson = StudentTORController.class.getClassLoader().getResourceAsStream(curriculumJsonLocation);
        return objectMapper.readValue(curriculumJson, TORCurriculum.class);
      }
    }
    
    return null;
  }
  
  /**
   * This is based on a hard-coded assumption that there is an EducationSubtype with a
   * specific code that can be interpreted as mandatory. In ideal situation this would
   * be a configurable setting or it should come from students' own curriculum choices.
   */
  private static boolean isMandatory(CourseAssessment courseAssessment) {
    return courseAssessment.getCourseModule().getCourse().getCourseEducationTypes().stream()
      .flatMap(courseEducationType -> courseEducationType.getCourseEducationSubtypes().stream())
      .map(CourseEducationSubtype::getEducationSubtype)
      .anyMatch(educationSubType -> StringUtils.equals(educationSubType.getCode(), "pakollinen"));
  }

  private static TORCourseLengthUnit getCourseLengthUnit(EducationalTimeUnit unit, TORProblems problems) {
    TORCourseLengthUnit lengthUnit = TORCourseLengthUnit.valueOf(unit.getSymbol());
    
    if (lengthUnit == null) {
      problems.add(new TORProblem(TORProblemType.UNRESOLVABLE_LENGTHUNIT, String.format("%s (%s)", unit.getSymbol(), unit.getName())));
    }
    
    return lengthUnit;
  }

  private static void addTORSubject(StudentTOR tor, Student student, Subject subject, TORCurriculum curriculum) {
    TORSubject torSubject = tor.findSubject(subject.getId());
    if (torSubject == null) {
      Long educationTypeId = subject.getEducationType() != null ? subject.getEducationType().getId() : null;
      fi.otavanopisto.pyramus.tor.Subject subjectModel = new fi.otavanopisto.pyramus.tor.Subject(
          subject.getId(), subject.getCode(), subject.getName(), educationTypeId, subject.getArchived());
      torSubject = TORSubject.from(subjectModel);
      tor.addSubject(torSubject);
      
      StudentSubjectGradeDAO studentSubjectGradeDAO = DAOFactory.getInstance().getStudentSubjectGradeDAO();
      StudentSubjectGrade studentSubjectGrade = studentSubjectGradeDAO.findBy(student, subject);
      
      if (studentSubjectGrade != null && studentSubjectGrade.getGrade() != null) {
        fi.otavanopisto.pyramus.domainmodel.grading.Grade grade = studentSubjectGrade.getGrade();
        Grade gradeModel = new Grade(grade.getId(), grade.getName(), grade.getDescription(), grade.getGradingScale().getId(),
            grade.getPassingGrade(), grade.getQualification(), grade.getGPA(), grade.getArchived());
        torSubject.setMeanGrade(gradeModel);
      }
    }
  }

  private static void addTORCredit(StudentTOR tor, Student student, Subject subject, Credit credit, Integer courseNumber, Set<Curriculum> creditCurriculums, boolean mandatory, Double courseLength, TORCourseLengthUnit courseLengthUnit, Map<String, Set<String>> subjectCodeTranslations, TORProblems problems) {
    if (credit.getGrade() == null) {
      return;
    }
    
    if (student.getCurriculum() != null) {
      Long studentCurriculumId = student.getCurriculum().getId();
      
      if (!creditCurriculums.isEmpty()) {
        boolean matchingCurriculum = creditCurriculums.stream()
          .map(Curriculum::getId)
          .anyMatch(studentCurriculumId::equals);
        
        // Both student and credit have curriculums set, but they didn't match -> skip the credit
        if (!matchingCurriculum) {
          return;
        }
      }
    }
    
    Long educationTypeId = subject.getEducationType() != null ? subject.getEducationType().getId() : null;
    fi.otavanopisto.pyramus.tor.Subject subjectModel = new fi.otavanopisto.pyramus.tor.Subject(
        subject.getId(), subject.getCode(), subject.getName(), educationTypeId, subject.getArchived());

    // List of subjects the credit will be added to
    List<TORSubject> torSubjects = new ArrayList<>();
    // If the subject gets included into another subject, this is 
    // marked true and it's not included as an individual subject
    boolean handled = false;
    
    /*
     * If the translations map is set, the subjects there
     * will have their credits moved under the specified
     * other subjects. At some point there might be need
     * for an enum that still preserves them in the original
     * place too, so implement that with another enum.
     */
    if (subjectCodeTranslations != null && subjectCodeTranslations.containsKey(subject.getCode())) {
      Set<String> translatedSubjectCodes = subjectCodeTranslations.get(subject.getCode());
      
      for (String translatedSubjectCode : translatedSubjectCodes) {
        TORSubject torSubject = tor.findSubject(translatedSubjectCode);
        if (torSubject != null) {
          torSubjects.add(torSubject);
          handled = true;
        }
      }
    }
    
    if (!handled) {
      TORSubject torSubject = tor.findSubject(subject.getId());

      if (torSubject == null) {
        torSubject = TORSubject.from(subjectModel);
        tor.addSubject(torSubject);
        
        StudentSubjectGradeDAO studentSubjectGradeDAO = DAOFactory.getInstance().getStudentSubjectGradeDAO();
        StudentSubjectGrade studentSubjectGrade = studentSubjectGradeDAO.findBy(student, subject);
        if (studentSubjectGrade != null && studentSubjectGrade.getGrade() != null) {
          fi.otavanopisto.pyramus.domainmodel.grading.Grade grade = studentSubjectGrade.getGrade();
          Grade gradeModel = new Grade(grade.getId(), grade.getName(), grade.getDescription(), grade.getGradingScale().getId(),
              grade.getPassingGrade(), grade.getQualification(), grade.getGPA(), grade.getArchived());
          torSubject.setMeanGrade(gradeModel);
        }
      }
      
      torSubjects.add(torSubject);
    }

    for (TORSubject torSubject : torSubjects) {
      TORCourse torCourse = torSubject.findCourse(courseNumber);
      if (torCourse == null) {
        torCourse = new TORCourse(subjectModel, courseNumber, mandatory, courseLength, courseLengthUnit);
        torSubject.addCourse(torCourse);
      } else {
        // Validate the lengthUnit matches
        
        if (torCourse.getLengthUnit() != courseLengthUnit) {
          problems.add(new TORProblem(TORProblemType.INCOMPATIBLE_LENGTHUNITS, String.format("%s%d", subjectModel.getCode(), courseNumber)));
        }
        
        // Validate the mandatority matches
        
        if (torCourse.isMandatory() != mandatory) {
          problems.add(new TORProblem(TORProblemType.INCOMPATIBLE_MANDATORITIES, String.format("%s%d", subjectModel.getCode(), courseNumber)));
        }
      }
      
      String gradeName = credit.getGrade().getName();
      Double gpa = credit.getGrade().getGPA();
      torCourse.addCredit(new TORCredit(credit.getGrade().getId(), gradeName, gpa, credit.getDate(), 
          TORCreditType.COURSEASSESSMENT, credit.getGrade().getPassingGrade()));
    }
  }
  
  /**
   * Creates reverse subject code mapping from given TORCurriculum.
   * 
   * F.ex. if subjects MAA and MAB both have included subject MAY,
   * the resulting map is going to be MAY -> [MAA, MAB].
   * 
   * @param curriculum
   * @param handling
   * @param subjectCodeTranslations
   * @return
   */
  private static Map<String, Set<String>> getReverseIncludedSubjectsMapping(TORCurriculum curriculum) {
    Map<String, Set<String>> subjectCodeTranslations = new HashMap<>();

    for (TORCurriculumSubject torCurriculumSubject : curriculum.getSubjects()) {
      List<String> includedSubjects = torCurriculumSubject.getIncludedSubjects();
      if (CollectionUtils.isNotEmpty(includedSubjects)) {
        for (String includedSubject : includedSubjects) {
          Set<String> set = subjectCodeTranslations.get(includedSubject);
          if (set == null) {
            set = new HashSet<>();
            subjectCodeTranslations.put(includedSubject, set);
          }
          set.add(torCurriculumSubject.getCode());
        }
      }
    }

    return subjectCodeTranslations;
  }

  /**
   * Loads given Student's subject selections from user variables and returns an
   * helper object to deal with them. Attempts to load default values based on the
   * education type of the student and then overriding the subject choices from the
   * values in the database.
   * 
   * @param student Student for who the subject selections are being loaded for. Must not be null.
   * @return
   */
  public static StudentSubjectSelections loadStudentSubjectSelections(Student student) {
    if (student == null) {
      throw new IllegalArgumentException("Student must not be null");
    }
    
    Map<String, String> defaultSubjectSelectionsMap = null;
    Set<String> subjectChoices = null;

    // Attempt to figure out the values from the Student's education type
    
    String educationTypeCode = student.getEducationTypeCode();
    
    if (StringUtils.isNotBlank(educationTypeCode)) {
      switch (educationTypeCode) {
        case PyramusConsts.Lukio.EDUCATION_TYPE:
          defaultSubjectSelectionsMap = PyramusConsts.Lukio.DEFAULT_SUBJECT_CHOICES;
          subjectChoices = PyramusConsts.Lukio.CHOICE_SUBJECTS;
        break;
        
        case PyramusConsts.Perusopetus.EDUCATION_TYPE:
          defaultSubjectSelectionsMap = PyramusConsts.Perusopetus.DEFAULT_SUBJECT_CHOICES;
          subjectChoices = PyramusConsts.Perusopetus.CHOICE_SUBJECTS;
        break;
        
        case PyramusConsts.Apa.EDUCATION_TYPE:
          defaultSubjectSelectionsMap = PyramusConsts.Apa.DEFAULT_SUBJECT_CHOICES;
        break;
      }
    }
    
    return loadStudentSubjectSelections(student, subjectChoices, defaultSubjectSelectionsMap);
  }

  /**
   * Loads given Student's subject selections from user variables and returns an
   * helper object to deal with them. Populates the subject selections from given
   * default values which are then overridden if set in database.
   * 
   * @param student Student for who the subject selections are being loaded for. Must not be null.
   * @param subjectChoices Set of subject codes that require an explicit choice to be made
   * @param defaultSubjectSelectionsMap Default values to be applied before the choices are loaded from database
   * @return
   */
  public static StudentSubjectSelections loadStudentSubjectSelections(Student student, Set<String> subjectChoices, Map<String, String> defaultSubjectSelectionsMap) {
    if (student == null) {
      throw new IllegalArgumentException("Student must not be null");
    }
    
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();

    StudentSubjectSelections studentSubjects = new StudentSubjectSelections();
    studentSubjects.setSubjectSelectionRequired(subjectChoices);

    if (defaultSubjectSelectionsMap != null) {
      for (Entry<String, String> defaultSelection : defaultSubjectSelectionsMap.entrySet()) {
        studentSubjects.setFieldValueByUserVariableName(defaultSelection.getKey(), defaultSelection.getValue());
      }
    }
    
    // Get the subject selections from database and set or replace the subject selections
    
    String math = userVariableDAO.findByUserAndKey(student, KoskiConsts.SubjectSelections.MATH);
    String lang = userVariableDAO.findByUserAndKey(student, KoskiConsts.SubjectSelections.NATIVE_LANGUAGE);
    String aLang = userVariableDAO.findByUserAndKey(student, KoskiConsts.SubjectSelections.LANG_A);
    String a1Lang = userVariableDAO.findByUserAndKey(student, KoskiConsts.SubjectSelections.LANG_A1);
    String a2Lang = userVariableDAO.findByUserAndKey(student, KoskiConsts.SubjectSelections.LANG_A2);
    String b1Lang = userVariableDAO.findByUserAndKey(student, KoskiConsts.SubjectSelections.LANG_B1);
    String b2Lang = userVariableDAO.findByUserAndKey(student, KoskiConsts.SubjectSelections.LANG_B2);
    String b3Lang = userVariableDAO.findByUserAndKey(student, KoskiConsts.SubjectSelections.LANG_B3);
    String religion = userVariableDAO.findByUserAndKey(student, KoskiConsts.SubjectSelections.RELIGION);
    String accomplishmentsStr = userVariableDAO.findByUserAndKey(student, KoskiConsts.SubjectSelections.COMPLETION_MARKS);

    if (StringUtils.isNotBlank(math)) {
      studentSubjects.setMath(math);
    }
    if (StringUtils.isNotBlank(lang)) {
      studentSubjects.setPrimaryLanguage(lang);
    }

    if (StringUtils.isNotBlank(aLang)) {
      studentSubjects.setALanguages(aLang);
    }
    if (StringUtils.isNotBlank(a1Lang)) {
      studentSubjects.setA1Languages(a1Lang);
    }
    if (StringUtils.isNotBlank(a2Lang)) {
      studentSubjects.setA2Languages(a2Lang);
    }
    
    if (StringUtils.isNotBlank(b1Lang)) {
      studentSubjects.setB1Languages(b1Lang);
    }
    if (StringUtils.isNotBlank(b2Lang)) {
      studentSubjects.setB2Languages(b2Lang);
    }
    if (StringUtils.isNotBlank(b3Lang)) {
      studentSubjects.setB3Languages(b3Lang);
    }
    if (StringUtils.isNotBlank(religion)) {
      studentSubjects.setReligion(religion);
    }

    if (StringUtils.isNotBlank(accomplishmentsStr)) {
      studentSubjects.setAccomplishments(accomplishmentsStr);
    }
    
    return studentSubjects;
  }

}
