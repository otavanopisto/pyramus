package fi.otavanopisto.pyramus.koski;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.students.StudentLodgingPeriodDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kunta;
import fi.otavanopisto.pyramus.koski.koodisto.Lahdejarjestelma;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.model.HenkilovahvistusPaikkakunnalla;
import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.LahdeJarjestelmaID;
import fi.otavanopisto.pyramus.koski.model.Organisaatio;
import fi.otavanopisto.pyramus.koski.model.OrganisaatioHenkilo;
import fi.otavanopisto.pyramus.koski.model.OrganisaatioOID;

public class KoskiStudentHandler {

  public static final String KOSKI_STUDYPERMISSION_ID = "koski.studypermission-id";
  
  @Inject
  protected KoskiSettings settings;
  
  @Inject 
  protected UserVariableDAO userVariableDAO;

  @Inject
  protected StudentLodgingPeriodDAO lodgingPeriodDAO;
  
  @Inject 
  private CourseAssessmentDAO courseAssessmentDAO;

  @Inject
  private TransferCreditDAO transferCreditDAO;
  
  @Inject
  private CreditLinkDAO creditLinkDAO;
  
  protected Kuvaus kuvaus(String fiKuvaus) {
    Kuvaus kuvaus = new Kuvaus();
    kuvaus.setFi(fiKuvaus);
    return kuvaus;
  }

  protected HenkilovahvistusPaikkakunnalla getVahvistus(Student student, String academyOid) {
    Organisaatio henkilonOrganisaatio = new OrganisaatioOID(academyOid);
    String nimi = settings.getVahvistaja(student.getStudyProgramme().getId());
    String titteli = settings.getVahvistajanTitteli(student.getStudyProgramme().getId());
    OrganisaatioHenkilo henkilo = new OrganisaatioHenkilo(nimi, kuvaus(titteli), henkilonOrganisaatio);

    Organisaatio myontajaOrganisaatio = new OrganisaatioOID(academyOid);
    HenkilovahvistusPaikkakunnalla vahvistus = new HenkilovahvistusPaikkakunnalla(
        student.getStudyEndDate(), Kunta.K491, myontajaOrganisaatio);
    vahvistus.addMyontajaHenkilo(henkilo);
    return vahvistus;
  }

  protected LahdeJarjestelmaID getLahdeJarjestelmaID(Long id) {
    return new LahdeJarjestelmaID(String.valueOf(id), Lahdejarjestelma.pyramus);
  }

  protected String getDiaarinumero(Student student) {
    Long studyProgrammeId = student.getStudyProgramme() != null ? student.getStudyProgramme().getId() : null;
    Long curriculumId = student.getCurriculum() != null ? student.getCurriculum().getId() : null;
    
    if ((studyProgrammeId != null) && (curriculumId != null)) {
      return settings.getDiaariNumero(studyProgrammeId, curriculumId);
    } else {
      return null;
    }
  }

  protected ArviointiasteikkoYleissivistava getArvosana(Grade grade) {
    switch (grade.getName()) {
      case "4":
        return ArviointiasteikkoYleissivistava.GRADE_4;
      case "5":
        return ArviointiasteikkoYleissivistava.GRADE_5;
      case "6":
        return ArviointiasteikkoYleissivistava.GRADE_6;
      case "7":
        return ArviointiasteikkoYleissivistava.GRADE_7;
      case "8":
        return ArviointiasteikkoYleissivistava.GRADE_8;
      case "9":
        return ArviointiasteikkoYleissivistava.GRADE_9;
      case "10":
        return ArviointiasteikkoYleissivistava.GRADE_10;
      case "H":
        return ArviointiasteikkoYleissivistava.GRADE_H;
      case "S":
        return ArviointiasteikkoYleissivistava.GRADE_S;
    }
    
    return null;
  }

  protected boolean isPakollinenOppiaine(Student student, KoskiOppiaineetYleissivistava oppiaine) {
    Set<KoskiOppiaineetYleissivistava> pakollisetOppiaineet = settings.getPakollisetOppiaineet(student.getStudyProgramme().getId());
    if (pakollisetOppiaineet != null) {
      return pakollisetOppiaineet.contains(oppiaine);
    }
    return false; 
  }
  
  protected StudentSubjectSelections loadStudentSubjectSelections(Student student, OpiskeluoikeudenTyyppi opiskeluoikeudenTyyppi) {
    StudentSubjectSelections studentSubjects = new StudentSubjectSelections();
    
    String math = userVariableDAO.findByUserAndKey(student, "lukioMatematiikka");
    String lang = userVariableDAO.findByUserAndKey(student, "lukioAidinkieli");
    String aLang = userVariableDAO.findByUserAndKey(student, "lukioKieliA");
    String a1Lang = userVariableDAO.findByUserAndKey(student, "lukioKieliA1");
    String a2Lang = userVariableDAO.findByUserAndKey(student, "lukioKieliA2");
    String b1Lang = userVariableDAO.findByUserAndKey(student, "lukioKieliB1");
    String b2Lang = userVariableDAO.findByUserAndKey(student, "lukioKieliB2");
    String b3Lang = userVariableDAO.findByUserAndKey(student, "lukioKieliB3");
    String religion = userVariableDAO.findByUserAndKey(student, "lukioUskonto");

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

    if (StringUtils.isBlank(studentSubjects.getMath())) {
      switch (opiskeluoikeudenTyyppi) {
        case lukiokoulutus:
          studentSubjects.setMath("MAB");
        break;
        
        default:
        break;
      }
    }
    
    if (StringUtils.isBlank(studentSubjects.getPrimaryLanguage())) {
      switch (opiskeluoikeudenTyyppi) {
        case lukiokoulutus:
          studentSubjects.setPrimaryLanguage("ÄI");
        break;
        
        case aikuistenperusopetus:
          studentSubjects.setPrimaryLanguage("äi");
        break;

        default:
        break;
      }
    }

    if (StringUtils.isBlank(studentSubjects.getReligion())) {
      switch (opiskeluoikeudenTyyppi) {
        case lukiokoulutus:
          studentSubjects.setReligion("UE");
        break;
        
        case aikuistenperusopetus:
          studentSubjects.setReligion("ue");
        break;

        default:
        break;
      }
    }
    
    return studentSubjects;
  }
 
  protected Collection<CreditStub> listCredits(Student student) {
    List<CourseAssessment> courseAssessments = courseAssessmentDAO.listByStudent(student);
    List<TransferCredit> transferCredits = transferCreditDAO.listByStudent(student);
    List<CreditLink> creditLinks = creditLinkDAO.listByStudent(student);
    
    Map<String, CreditStub> stubs = new HashMap<>();
    
    for (CourseAssessment ca : courseAssessments) {
      Course course = ca.getCourseStudent() != null ? ca.getCourseStudent().getCourse() : null;
      Subject subject = course != null ? course.getSubject() : null;
      
      String courseCode = subject.getCode() + course.getCourseNumber();
      CreditStub stub;
      if (!stubs.containsKey(courseCode)) {
        stub = new CreditStub(courseCode, course.getName(), subject);
        stubs.put(courseCode, stub);
      } else
        stub = stubs.get(courseCode);
      
      stub.addCredit(ca);
    }
    
    for (TransferCredit tc : transferCredits) {
      String courseCode = tc.getSubject().getCode() + tc.getCourseNumber();
      CreditStub stub;
      if (!stubs.containsKey(courseCode)) {
        stub = new CreditStub(courseCode, tc.getCourseName(), tc.getSubject());
        stubs.put(courseCode, stub);
      } else
        stub = stubs.get(courseCode);
      
      stub.addCredit(tc);
    }
    
    for (CreditLink cl : creditLinks) {
      Subject subject = null;
      Integer courseNumber = null;
      String courseName = null;
      
      switch (cl.getCredit().getCreditType()) {
        case CourseAssessment:
          CourseAssessment ca = (CourseAssessment) cl.getCredit();
          Course course = ca.getCourseStudent() != null ? ca.getCourseStudent().getCourse() : null;
          subject = course != null ? course.getSubject() : null;
          courseNumber = course.getCourseNumber();
          courseName = course.getName();
        break;
        case TransferCredit:
          TransferCredit tc = (TransferCredit) cl.getCredit();
          subject = tc.getSubject();
          courseNumber = tc.getCourseNumber();
          courseName = tc.getCourseName();
        break;
        case ProjectAssessment:
          continue;
      }
      
      if (allNotNull(subject, courseNumber, courseName)) {
        String courseCode = subject.getCode() + courseNumber;
        CreditStub stub;
        if (!stubs.containsKey(courseCode)) {
          stub = new CreditStub(courseCode, courseName, subject);
          stubs.put(courseCode, stub);
        } else
          stub = stubs.get(courseCode);
        
        stub.addCredit(cl.getCredit());
      }
    }
    
    return stubs.values();
  }
  
  protected boolean allNotNull(final Object ... objects) {
    for (Object o : objects) {
      if (o == null) {
        return false;
      }
    }
    return true;
  }
  
  protected ArviointiasteikkoYleissivistava meanGrade(Collection<ArviointiasteikkoYleissivistava> grades) {
    if (grades.stream().anyMatch(grade -> ArviointiasteikkoYleissivistava.isNumeric(grade))) {
      // Numeric grade
      
      int gradeSum = 0;
      int gradeCount = 0;
      
      for (ArviointiasteikkoYleissivistava grade : grades) {
        if (ArviointiasteikkoYleissivistava.isNumeric(grade)) {
          gradeSum += Integer.valueOf(grade.toString());
          gradeCount++;
        }
      }
      
      if (gradeCount > 0) {
        return ArviointiasteikkoYleissivistava.get(String.valueOf(Math.round((double) gradeSum / gradeCount)));
      } else {
        return null;
      }
    } else {
      if (grades.stream().anyMatch(grade -> ArviointiasteikkoYleissivistava.isLiteral(grade))) {
        // Literal grade S/H
        
        if (grades.stream().anyMatch(grade -> ArviointiasteikkoYleissivistava.GRADE_S == grade)) {
          return ArviointiasteikkoYleissivistava.GRADE_S;
        } else {
          return ArviointiasteikkoYleissivistava.GRADE_H;
        }
      } else {
        return null;
      }
    }
  }

}
