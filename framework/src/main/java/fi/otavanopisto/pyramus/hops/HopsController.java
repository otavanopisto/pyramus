package fi.otavanopisto.pyramus.hops;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.framework.SettingUtils;

@ApplicationScoped
public class HopsController {
  
  @Inject
  private Logger logger;
  
  @Inject
  private TransferCreditDAO transferCreditDAO;
  
  @Inject
  private CourseAssessmentDAO courseAssessmentDAO;
  
  @Inject
  private CreditLinkDAO creditLinkDAO;
  
  @Inject
  private UserVariableDAO userVariableDAO;
  
  private static final String LUKIO = "lukio";
  private static final String PK = "peruskoulu";
  private static final String OPS_LUKIO = "OPS 2021";
  private static final String OPS_PK = "OPS 2018";
  private static final String SUBJECTS_CHOSEN = "hops.chosenSubjects";
  private static final String SUBJECTS_HIDDEN = "hops.hiddenSubjects";
  
  public HopsCourseMatrix getCourseMatrix(Student student) {
    HopsCourseMatrix matrix = new HopsCourseMatrix();
    
    // Listat ainevalinta-aineista ja oletuksena piilotettavista aineista
    
    String chosenStr = SettingUtils.getSettingValue(SUBJECTS_CHOSEN);
    Set<String> chosenSubjects = new HashSet<>();
    StringTokenizer st = new StringTokenizer(chosenStr, ",");
    while (st.hasMoreTokens()) {
      chosenSubjects.add(st.nextToken());
    }
    String hiddenStr = SettingUtils.getSettingValue(SUBJECTS_HIDDEN);
    Set<String> hiddenSubjects = new HashSet<>();
    st = new StringTokenizer(hiddenStr, ",");
    while (st.hasMoreTokens()) {
      hiddenSubjects.add(st.nextToken());
    }
    
    // Onko opiskelijalla opetussuunnitelma ja onko se joko OPS 2021 tai OPS 2018

    String ops = student.getCurriculum().getName();
    if (StringUtils.isEmpty(ops) || !StringUtils.equalsAny(ops, OPS_LUKIO, OPS_PK)) {
      matrix.addProblem(HopsCourseMatrixProblem.INCOMPATIBLE_STUDENT);
      return matrix;
    }
    
    // Onko opiskelijan koulutusaste lukio tai peruskoulu
    
    String type = student.getStudyProgramme().getCategory().getEducationType().getCode();
    if (!StringUtils.equalsAny(type, LUKIO, PK)) {
      matrix.addProblem(HopsCourseMatrixProblem.INCOMPATIBLE_STUDENT);
      return matrix;
    }
    
    // Onko lukiolaisen opetussuunnitelma 2021
    
    if (StringUtils.equals(type, LUKIO) && !StringUtils.equals(ops, OPS_LUKIO)) {
      matrix.addProblem(HopsCourseMatrixProblem.INCOMPATIBLE_STUDENT);
      return matrix;
    }

    // Onko perusopetuksessa opiskelevan opetussuunnitelma 2018
    
    if (StringUtils.equals(type, PK) && !StringUtils.equals(ops, OPS_PK)) {
      matrix.addProblem(HopsCourseMatrixProblem.INCOMPATIBLE_STUDENT);
      return matrix;
    }
    
    // Pohjaksi paikallinen opetussuunnitelma koulutusasteen mukaan

    try {
      ObjectMapper mapper = new ObjectMapper();
      String jsonSrc = StringUtils.equals(type, LUKIO)
          ? "fi/otavanopisto/pyramus/tor/curriculum_2021.json"
          : "fi/otavanopisto/pyramus/tor/curriculum_2018.json";
      InputStream json = HopsController.class.getClassLoader().getResourceAsStream(jsonSrc);
      matrix = mapper.readValue(json, HopsCourseMatrix.class);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error parsing OPS", e);
      matrix = new HopsCourseMatrix();
      matrix.addProblem(HopsCourseMatrixProblem.INTERNAL_ERROR);
    }
    
    // Opiskelijan kaikki mahdolliset suoritukset
    
    List<TransferCredit> transferCredits = transferCreditDAO.listByStudent(student);
    List<CourseAssessment> courseAssessments = courseAssessmentDAO.listByStudent(student);
    List<CreditLink> creditLinks = creditLinkDAO.listByStudent(student);
    for (CreditLink creditLink : creditLinks) {
      if (creditLink.getCredit() instanceof TransferCredit) {
        transferCredits.add((TransferCredit) creditLink.getCredit());
      }
      else if (creditLink.getCredit() instanceof CourseAssessment) {
        courseAssessments.add((CourseAssessment) creditLink.getCredit());
      }
    }
    
    // Opiskelijan ainevalinnat
    
    boolean hasNativeLanguage = false;
    boolean hasMath = false;
    boolean hasReligion = false;
    boolean hasPrimaryForeignLangauge = false;
    boolean hasSecondaryForeignLanguage = false;
    Set<String> studentChosenSubjects = new HashSet<>();
    String s = userVariableDAO.findByUserAndKey(student, "lukioAidinkieli");
    if (s != null) {
      hasNativeLanguage = true;
      studentChosenSubjects.add(s);
    }
    s = userVariableDAO.findByUserAndKey(student, "lukioMatematiikka");
    if (s != null) {
      hasMath = true;
      studentChosenSubjects.add(s);
    }
    s = userVariableDAO.findByUserAndKey(student, "lukioUskonto");
    if (s != null) {
      hasReligion = true;
      studentChosenSubjects.add(s);
    }
    s = userVariableDAO.findByUserAndKey(student, "lukioKieliA");
    if (s != null) {
      hasPrimaryForeignLangauge = true;
      studentChosenSubjects.add(s);
    }
    s = userVariableDAO.findByUserAndKey(student, "lukioKieliB1");
    if (s != null) {
      hasSecondaryForeignLanguage = true;
      studentChosenSubjects.add(s);
    }
    s = userVariableDAO.findByUserAndKey(student, "lukioKieliB2");
    if (s != null) {
      studentChosenSubjects.add(s);
    }
    s = userVariableDAO.findByUserAndKey(student, "lukioKieliB3");
    if (s != null) {
      studentChosenSubjects.add(s);
    }
    
    // Nillitystä puuttuvista ainevalinnoista
    
    if (!hasNativeLanguage) {
      matrix.addProblem(HopsCourseMatrixProblem.NO_NATIVE_LANGUAGE);
    }
    if (!hasMath) {
      matrix.addProblem(HopsCourseMatrixProblem.NO_MATH);
    }
    if (!hasReligion) {
      matrix.addProblem(HopsCourseMatrixProblem.NO_RELIGION);
    }
    if (!hasPrimaryForeignLangauge) {
      matrix.addProblem(HopsCourseMatrixProblem.NO_PRIMARY_FOREIGN_LANGUAGE);
    }
    if (!hasSecondaryForeignLanguage) {
      matrix.addProblem(HopsCourseMatrixProblem.NO_SECONDARY_FOREIGN_LANGUAGE);
    }
    
    // Aletaan käymään läpi aineita
    
    Set<String> subjects = matrix.listSubjectCodes();
    for (String subject : subjects) {
      Set<Integer> courseNumbers = matrix.listCourseNumbers(subject);
      
      // Onko opiskelijalla suorituksia tästä aineesta:
      // - oikea aine
      // - opetussuunnitelman mukainen kurssinumero
      // - kurssin tai hyväksiluvun opetussuunnitelman pitää olla joko tyhjä tai vastata opiskelijan opetussuunnitelmaa
      
      boolean hasCredit = transferCredits.stream().anyMatch(tc ->
        StringUtils.equals(subject, tc.getSubject().getCode()) &&
        courseNumbers.contains(tc.getCourseNumber()) &&
        (tc.getCurriculum() == null || StringUtils.equals(tc.getCurriculum().getName(), ops)));
      if (!hasCredit) {
        hasCredit = courseAssessments.stream().anyMatch(ca ->
          StringUtils.equals(subject, ca.getSubject().getCode()) &&
          courseNumbers.contains(ca.getCourseNumber()) &&
          (ca.getCourseModule().getCourse().getCurriculums().isEmpty() ||
            ca.getCourseModule().getCourse().getCurriculums().stream().anyMatch(c -> StringUtils.equals(c.getName(), ops))));
      }
      
      // Jos suorituksia on, aine jää aina matriisiin
      
      if (hasCredit) {
        continue;
      }
      
      // Jos aine on ainevalinta-aine mutta ei opiskelijan valitsema, ei näytetä
      
      if (chosenSubjects.contains(subject) && !studentChosenSubjects.contains(subject)) {
        matrix.removeSubject(subject);
      }
      
      // Jos ainetta ei haluta näyttää niin ei näytetä
      
      if (hiddenSubjects.contains(subject)) {
        matrix.removeSubject(subject);
      }
    }
    
    // Johan oli
    
    return matrix;
  }

}
