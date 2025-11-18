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

import fi.internetix.smvc.SmvcRuntimeException;
import fi.otavanopisto.pyramus.PyramusConsts;
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
  
  private static final String SUBJECTS_CHOICE = "hops.choiceSubjects";
  private static final String SUBJECTS_HIDDEN = "hops.hiddenSubjects";
  
  public HopsCourseMatrix getCourseMatrix(Student student) {
    HopsCourseMatrix matrix = new HopsCourseMatrix();
    
    // Listat ainevalinta-aineista ja oletuksena piilotettavista aineista
    
    String choiceStr = SettingUtils.getSettingValue(SUBJECTS_CHOICE);
    Set<String> choiceSubjects = new HashSet<>();
    StringTokenizer st = new StringTokenizer(choiceStr, ",");
    while (st.hasMoreTokens()) {
      choiceSubjects.add(st.nextToken());
    }
    String hiddenStr = SettingUtils.getSettingValue(SUBJECTS_HIDDEN);
    Set<String> hiddenSubjects = new HashSet<>();
    st = new StringTokenizer(hiddenStr, ",");
    while (st.hasMoreTokens()) {
      hiddenSubjects.add(st.nextToken());
    }
    
    // Onko opiskelijalla opetussuunnitelma ja onko se joko OPS 2021 tai OPS 2018

    String ops = student.getCurriculum().getName();
    if (StringUtils.isEmpty(ops) || !StringUtils.equalsAny(ops, PyramusConsts.OPS_2021, PyramusConsts.OPS_2018)) {
      matrix.addProblem(HopsCourseMatrixProblem.INCOMPATIBLE_STUDENT);
      return matrix;
    }
    
    // Onko opiskelijan koulutusaste lukio tai peruskoulu
    
    String type = student.getStudyProgramme().getCategory().getEducationType().getCode();
    if (!StringUtils.equalsAny(type, PyramusConsts.EDUCATION_TYPE_LUKIO, PyramusConsts.EDUCATION_TYPE_PK)) {
      matrix.addProblem(HopsCourseMatrixProblem.INCOMPATIBLE_STUDENT);
      return matrix;
    }

    // Matriisin tyyppi on nyt tiedossa
    
    if (StringUtils.equals(type, PyramusConsts.EDUCATION_TYPE_LUKIO)) {
      matrix.setType(HopsCourseMatrixType.UPPER_SECONDARY);
    }
    else {
      matrix.setType(HopsCourseMatrixType.COMPULSORY);
    }
    
    // Onko lukiolaisen opetussuunnitelma 2021
    
    if (StringUtils.equals(type, PyramusConsts.EDUCATION_TYPE_LUKIO) && !StringUtils.equals(ops, PyramusConsts.OPS_2021)) {
      matrix.addProblem(HopsCourseMatrixProblem.INCOMPATIBLE_STUDENT);
      return matrix;
    }

    // Onko perusopetuksessa opiskelevan opetussuunnitelma 2018
    
    if (StringUtils.equals(type, PyramusConsts.EDUCATION_TYPE_PK) && !StringUtils.equals(ops, PyramusConsts.OPS_2018)) {
      matrix.addProblem(HopsCourseMatrixProblem.INCOMPATIBLE_STUDENT);
      return matrix;
    }
    
    // Pohjaksi paikallinen opetussuunnitelma koulutusasteen mukaan

    try {
      ObjectMapper mapper = new ObjectMapper();
      String jsonSrc = StringUtils.equals(type, PyramusConsts.EDUCATION_TYPE_LUKIO)
          ? "fi/otavanopisto/pyramus/tor/curriculum_2021.json"
          : "fi/otavanopisto/pyramus/tor/curriculum_2018.json";
      InputStream json = HopsController.class.getClassLoader().getResourceAsStream(jsonSrc);
      matrix = mapper.readValue(json, HopsCourseMatrix.class);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error parsing OPS", e);
      throw new SmvcRuntimeException(e);
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
    //
    // Jatkokehitysideana olisi parempi, jos opiskelijalla olisi vain asetus chosenSubjects
    // (pilkkueroteltu lista valituista ainekoodeista), jonka lisäksi sekä lukiolle että
    // perusopetukselle erilliset ainevalintakonffit about tyyliin
    //
    // nativeLanguageChoices: ÄIM,ÄI,S2
    // mathChoices: MAA,MAB
    
    boolean hasNativeLanguage = false;
    boolean hasMath = false;
    boolean hasReligion = false;
    boolean hasPrimaryForeignLangauge = false;
    boolean hasSecondaryForeignLanguage = false;
    Set<String> chosenSubjects = new HashSet<>();
    String s = userVariableDAO.findByUserAndKey(student, "lukioAidinkieli");
    if (s != null) {
      hasNativeLanguage = true;
      chosenSubjects.add(s);
    }
    s = userVariableDAO.findByUserAndKey(student, "lukioMatematiikka");
    if (s != null) {
      hasMath = true;
      chosenSubjects.add(s);
    }
    s = userVariableDAO.findByUserAndKey(student, "lukioUskonto");
    if (s != null) {
      hasReligion = true;
      chosenSubjects.add(s);
    }
    s = userVariableDAO.findByUserAndKey(student, "lukioKieliA");
    if (s != null) {
      hasPrimaryForeignLangauge = true;
      chosenSubjects.add(s);
    }
    s = userVariableDAO.findByUserAndKey(student, "lukioKieliB1");
    if (s != null) {
      hasSecondaryForeignLanguage = true;
      chosenSubjects.add(s);
    }
    s = userVariableDAO.findByUserAndKey(student, "lukioKieliB2");
    if (s != null) {
      chosenSubjects.add(s);
    }
    s = userVariableDAO.findByUserAndKey(student, "lukioKieliB3");
    if (s != null) {
      chosenSubjects.add(s);
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
      
      if (choiceSubjects.contains(subject) && !chosenSubjects.contains(subject)) {
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
