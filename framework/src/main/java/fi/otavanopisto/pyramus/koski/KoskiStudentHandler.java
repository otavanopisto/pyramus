package fi.otavanopisto.pyramus.koski;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.base.SchoolVariableDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.koski.KoskiPersonLogDAO;
import fi.otavanopisto.pyramus.dao.students.StudentLodgingPeriodDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.CreditStubCredit.Type;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kunta;
import fi.otavanopisto.pyramus.koski.koodisto.Lahdejarjestelma;
import fi.otavanopisto.pyramus.koski.model.HenkilovahvistusPaikkakunnalla;
import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.LahdeJarjestelmaID;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.Oppilaitos;
import fi.otavanopisto.pyramus.koski.model.Organisaatio;
import fi.otavanopisto.pyramus.koski.model.OrganisaatioHenkilo;
import fi.otavanopisto.pyramus.koski.model.OrganisaatioOID;
import fi.otavanopisto.pyramus.koski.model.SisaltavaOpiskeluoikeus;

public class KoskiStudentHandler {

  public static final String KOSKI_STUDYPERMISSION_ID = "koski.studypermission-id";
  public static final String KOSKI_LINKED_STUDYPERMISSION_ID = "koski.linked-to-studypermission-id";
  public static final String KOSKI_SCHOOL_OID = "koski.schooloid";
  
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
  
  @Inject
  protected KoskiPersonLogDAO koskiPersonLogDAO;
  
  @Inject
  protected SchoolVariableDAO schoolVariableDAO;
  
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

  /**
   * Fills studentSubjects based on selections. Default values can be provided in studentSubjects, 
   * they are only overwritten if values exist in database.
   * 
   * @param student
   * @param studentSubjects
   * @return
   */
  protected StudentSubjectSelections loadStudentSubjectSelections(Student student, StudentSubjectSelections studentSubjects) {
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

    return studentSubjects;
  }
 
  protected List<CreditStub> listCredits(Student student) {
    List<CourseAssessment> courseAssessments = courseAssessmentDAO.listByStudent(student);
    List<TransferCredit> transferCredits = transferCreditDAO.listByStudent(student);
    List<CreditLink> creditLinks = creditLinkDAO.listByStudent(student);
    
    Map<String, CreditStub> stubs = new HashMap<>();
    
    for (CourseAssessment ca : courseAssessments) {
      Course course = ca.getCourseStudent() != null ? ca.getCourseStudent().getCourse() : null;
      Subject subject = course != null ? course.getSubject() : null;
      
      if (matchingCurriculum(student, course)) {
        String courseCode = courseCode(subject, course.getCourseNumber(), ca.getId());
        CreditStub stub;
        if (!stubs.containsKey(courseCode)) {
          stub = new CreditStub(courseCode, course.getCourseNumber(), course.getName(), subject);
          stubs.put(courseCode, stub);
        } else {
          stub = stubs.get(courseCode);
        }
        
        stub.addCredit(new CreditStubCredit(ca, Type.CREDIT));
      }
    }
    
    for (TransferCredit tc : transferCredits) {
      if (matchingCurriculum(student, tc)) {
        String courseCode = courseCode(tc.getSubject(), tc.getCourseNumber(), tc.getId());
        CreditStub stub;
        if (!stubs.containsKey(courseCode)) {
          stub = new CreditStub(courseCode, tc.getCourseNumber(), tc.getCourseName(), tc.getSubject());
          stubs.put(courseCode, stub);
        } else {
          stub = stubs.get(courseCode);
        }
        
        stub.addCredit(new CreditStubCredit(tc, Type.RECOGNIZED));
      }
    }
    
    for (CreditLink cl : creditLinks) {
      Subject subject = null;
      Integer courseNumber = null;
      String courseName = null;
      
      switch (cl.getCredit().getCreditType()) {
        case CourseAssessment:
          CourseAssessment ca = (CourseAssessment) cl.getCredit();
          Course course = ca.getCourseStudent() != null ? ca.getCourseStudent().getCourse() : null;

          if (!matchingCurriculum(student, course)) {
            continue;
          }
          
          subject = course != null ? course.getSubject() : null;
          courseNumber = course.getCourseNumber();
          courseName = course.getName();
        break;
        case TransferCredit:
          TransferCredit tc = (TransferCredit) cl.getCredit();
          
          if (!matchingCurriculum(student, tc)) {
            continue;
          }
          
          subject = tc.getSubject();
          courseNumber = tc.getCourseNumber();
          courseName = tc.getCourseName();
        break;
        case ProjectAssessment:
          continue;
      }
      
      if (allNotNull(subject, courseNumber, courseName)) {
        String courseCode = courseCode(subject, courseNumber, cl.getCredit().getId());
        CreditStub stub;
        if (!stubs.containsKey(courseCode)) {
          stub = new CreditStub(courseCode, courseNumber, courseName, subject);
          stubs.put(courseCode, stub);
        } else {
          stub = stubs.get(courseCode);
        }
        
        stub.addCredit(new CreditStubCredit(cl.getCredit(), Type.RECOGNIZED));
      }
    }
    
    List<CreditStub> stubList = new ArrayList<>(stubs.values());
    stubList.sort((a, b) -> ObjectUtils.compare(a.getCourseNumber(), b.getCourseNumber()));
    return stubList;
  }

  protected String courseCode(Subject subject, Integer courseNumber, Long creditId) {
    if (subject != null && StringUtils.isNotBlank(subject.getCode()) && courseNumber != null) {
      return subject.getCode() + courseNumber;
    } else {
      return String.valueOf(creditId);
    }
  }
  
  protected String subjectCode(Subject subject) {
    if (subject != null) {
      if (StringUtils.isNotBlank(subject.getCode())) {
        return subject.getCode();
      } else {
        return subject.getId().toString();
      }
    } else {
      return null;
    }
  }
  
  protected boolean matchingCurriculum(Student student, Course course) {
    return 
        student.getCurriculum() == null || 
        course.getCurriculums().isEmpty() || 
        course.getCurriculums().contains(student.getCurriculum());
  }
  
  protected boolean matchingCurriculum(Student student, TransferCredit transferCredit) {
    return 
        student.getCurriculum() == null || 
        transferCredit.getCurriculum() == null || 
        Objects.equals(student.getCurriculum(), transferCredit.getCurriculum());
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

  protected OpiskelijanOPS resolveOPS(Student student) {
    Curriculum curriculum = student.getCurriculum();
    if (curriculum != null) {
      switch (curriculum.getId().intValue()) {
        case 1:
          return OpiskelijanOPS.ops2016;
        case 2:
          return OpiskelijanOPS.ops2005;
        case 3:
          return OpiskelijanOPS.ops2018;
      }
    }
    return null;
  }

  protected void handleLinkedStudyOID(Student student, Opiskeluoikeus opiskeluoikeus) {
    if (student.getSchool() != null) {
      String linkedStudyOID = userVariableDAO.findByUserAndKey(student, KOSKI_LINKED_STUDYPERMISSION_ID);
      String schoolOID = schoolVariableDAO.findValueBySchoolAndKey(student.getSchool(), KOSKI_SCHOOL_OID);
      if (StringUtils.isNotBlank(linkedStudyOID) && StringUtils.isNotBlank(schoolOID)) {
        Oppilaitos oppilaitos = new Oppilaitos(schoolOID);
        opiskeluoikeus.setSisaltyyOpiskeluoikeuteen(new SisaltavaOpiskeluoikeus(oppilaitos, linkedStudyOID));
      } else {
        koskiPersonLogDAO.create(student.getPerson(), KoskiPersonState.LINKED_MISSING_VALUES, new Date());
      }
    }
  }
  
}
