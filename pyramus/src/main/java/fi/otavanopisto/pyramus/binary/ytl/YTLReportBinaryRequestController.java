package fi.otavanopisto.pyramus.binary.ytl;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.DegreeType;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamTerm;
import fi.otavanopisto.pyramus.domainmodel.matriculation.SchoolType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.ytl.Kokelas;
import fi.otavanopisto.pyramus.ytl.Koulutustyyppi;
import fi.otavanopisto.pyramus.ytl.SuoritettuKurssi;
import fi.otavanopisto.pyramus.ytl.Tutkintotyyppi;
import fi.otavanopisto.pyramus.ytl.YTLAineKoodi;
import fi.otavanopisto.pyramus.ytl.YTLAineKoodiSuoritettuKurssi;
import fi.otavanopisto.pyramus.ytl.YTLSiirtotiedosto;

public class YTLReportBinaryRequestController extends BinaryRequestController {

  private static final Logger logger = Logger.getLogger(YTLReportBinaryRequestController.class.getName());
  private static final String KOSKI_HENKILO_OID = "koski.henkilo-oid";

  public void process(BinaryRequestContext binaryRequestContext) {
    List<YTLAineKoodi> m = readMapping();
    
    MatriculationExamEnrollmentDAO matriculationExamEnrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    
    Integer year = binaryRequestContext.getInteger("examYear");
    MatriculationExamTerm term = (MatriculationExamTerm) binaryRequestContext.getEnum("examTerm", MatriculationExamTerm.class);
    
    String tutkintokerta = String.valueOf(year) + (term == MatriculationExamTerm.SPRING ? "K" : "S");
    Integer koulunumero = binaryRequestContext.getInteger("schoolId");
    YTLSiirtotiedosto ytl = new YTLSiirtotiedosto(tutkintokerta, koulunumero);

    List<MatriculationExamEnrollment> enrollments = matriculationExamEnrollmentDAO.listDistinctByAttendanceTerms(
        MatriculationExamEnrollmentState.APPROVED, year, term, MatriculationExamAttendanceStatus.ENROLLED);
    enrollments = processCandidateNumbers(enrollments);
    enrollments.forEach(enrollment -> ytl.addKokelas(enrollmentToKokelas(enrollment, m)));
    
    ObjectMapper objectMapper = new ObjectMapper();
    
    try {
      String value = objectMapper.writeValueAsString(ytl);
      binaryRequestContext.setFileName("ytl_" + tutkintokerta + ".json");
      binaryRequestContext.setResponseContent(value.getBytes("UTF-8"), "application/json;charset=UTF-8");
    } catch (Exception e) {
      throw new SmvcRuntimeException(e);
    }
  }
  
  private List<MatriculationExamEnrollment> processCandidateNumbers(List<MatriculationExamEnrollment> enrollments) {
    MatriculationExamEnrollmentDAO matriculationExamEnrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    
    // In theory, if the enrollment with the max number was removed,
    // the max number might be reused for another entrant. Who knows
    // if this proves to be an issue though in real life.
    int nextFreeNumber = enrollments.stream()
        .map(MatriculationExamEnrollment::getCandidateNumber)
        .filter(Objects::nonNull)
        .mapToInt(Integer::valueOf)
        .max()
        .orElse(0)
        + 1;
    
    for (int i = 0; i < enrollments.size(); i++) {
      MatriculationExamEnrollment enrollment = enrollments.get(i);
      if (enrollment.getCandidateNumber() == null) {
        enrollment = matriculationExamEnrollmentDAO.updateCandidateNumber(enrollment, nextFreeNumber);
        enrollments.set(i, enrollment);
        nextFreeNumber++;
      }
    }
    
    return enrollments;
  }

  private Kokelas enrollmentToKokelas(MatriculationExamEnrollment enrollment, List<YTLAineKoodi> mapping) {
    MatriculationExamAttendanceDAO matriculationExamAttendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    PersonVariableDAO personVariableDAO = DAOFactory.getInstance().getPersonVariableDAO();
    
    Student student = enrollment.getStudent();
    if (student == null || student.getPerson() == null) {
      return null;
    }
    
    List<MatriculationExamAttendance> attendances = matriculationExamAttendanceDAO.listByEnrollmentAndStatus(
        enrollment, MatriculationExamAttendanceStatus.ENROLLED);

    Person person = student.getPerson();
    String hetu = enrollment.getSsn();
    
    if (hetu == null) {
      hetu = person.getSocialSecurityNumber();
      if (hetu == null && person.getBirthday() != null) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        hetu = format.format(person.getBirthday());
      }
    }
    
    List<String> etunimet = Arrays.asList(StringUtils.split(student.getFirstName(), " "));

    String oppijanumero = personVariableDAO.findByPersonAndKey(person, KOSKI_HENKILO_OID);
    oppijanumero = StringUtils.isNotBlank(oppijanumero) ? oppijanumero : null;
    
    int kokelasnumero = enrollment.getCandidateNumber();

    Koulutustyyppi koulutustyyppi = schoolTypeToKoulutustyyppi(enrollment.getEnrollAs());
    Tutkintotyyppi tutkintotyyppi = degreeTypeToTutkintoTyyppi(enrollment.getDegreeType());
    boolean uudelleenaloittaja = enrollment.isRestartExam();
    
    MatriculationExamAttendance äidinkieli = attendances.stream()
        .filter(attendance -> isÄidinkieli(attendance))
        .findAny()
        .orElse(null);
    
    String äidinkielenKoe = null;
    if (äidinkieli != null) {
      YTLAineKoodi ytlÄidinkieli = examSubjectToYTLAineKoodi(äidinkieli.getSubject(), mapping);
      if (ytlÄidinkieli != null) {
        äidinkielenKoe = ytlÄidinkieli.getYtlAine();
      } else {
        logger.warning(String.format("No äidinkieli mapping found for %s", äidinkieli.getSubject()));
      }
    }
    
    Kokelas kokelas = new Kokelas();
    kokelas.getEtunimet().addAll(etunimet);
    kokelas.setSukunimi(student.getLastName());
    kokelas.setHetu(hetu);
    kokelas.setKokelasnumero(kokelasnumero);
    kokelas.setOppijanumero(oppijanumero);
    kokelas.setKoulutustyyppi(koulutustyyppi);
    kokelas.setTutkintotyyppi(tutkintotyyppi);
    kokelas.setUudelleenaloittaja(uudelleenaloittaja);
    
    kokelas.setÄidinkielenKoe(äidinkielenKoe);
    
    List<SuoritettuKurssi> suoritetutKurssit = new ArrayList<>();
    
    for (MatriculationExamAttendance attendance : attendances) {
      MatriculationExamSubject examSubject = attendance.getSubject();
      
      YTLAineKoodi ytlAineKoodi = examSubjectToYTLAineKoodi(examSubject, mapping);
      if (ytlAineKoodi != null) {
        if (attendance.isMandatory()) {
          kokelas.addPakollinenKoe(ytlAineKoodi.getYhdistettyAineKoodi());
        } else {
          kokelas.addYlimääräinenKoe(ytlAineKoodi.getYhdistettyAineKoodi());
        }
        
        lataaSuoritetutKurssit(student, ytlAineKoodi, suoritetutKurssit);
      } else {
        logger.warning(String.format("No subject mapping found for %s", examSubject));
      }
    }

    kokelas.setSuoritetutKurssit(suoritetutKurssit);
    
    return kokelas;
  }

  private void lataaSuoritetutKurssit(Student student, YTLAineKoodi ytlAineKoodi, List<SuoritettuKurssi> suoritetutKurssit) {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();
    
    long kurssiLukumäärä = 0;
    
    for (YTLAineKoodiSuoritettuKurssi ytlAKSK : ytlAineKoodi.getSuoritetutKurssit()) {
      EducationType educationType = educationTypeDAO.findById(ytlAKSK.getEducationType());
      if (educationType != null) {
        Subject subject = subjectDAO.findBy(educationType, ytlAKSK.getSubjectCode());
        if (subject != null) {
          List<CourseAssessment> courseAssessments = courseAssessmentDAO.listByStudentAndSubject(student, subject);
      
          kurssiLukumäärä += courseAssessments.stream()
            .filter(courseAssessment -> courseAssessment.getGrade() != null ? courseAssessment.getGrade().getPassingGrade() : false)
            .count();
        } else {
          logger.warning(String.format("Specified subjectcode %s for educationtype %d was not found", ytlAKSK.getSubjectCode(), ytlAKSK.getEducationType()));
        }
      } else {
        logger.warning(String.format("Specified educationtype %d was not found", ytlAKSK.getEducationType()));
      }
    }
    
    SuoritettuKurssi suoritettuKurssi = new SuoritettuKurssi(ytlAineKoodi.getYtlAine(), ytlAineKoodi.getYtlOppimäärä(), (int) kurssiLukumäärä);
    suoritetutKurssit.add(suoritettuKurssi);
  }

  private Tutkintotyyppi degreeTypeToTutkintoTyyppi(DegreeType degreeType) {
    if (degreeType != null) {
      switch (degreeType) {
        case MATRICULATIONEXAMINATION:
          return Tutkintotyyppi.yoTutkinto;
        case MATRICULATIONEXAMINATIONSUPPLEMENT:
          return Tutkintotyyppi.korottaja;
        case SEPARATEEXAM:
          return Tutkintotyyppi.erillinenKoe;
      }
    }
    
    return null;
  }

  private Koulutustyyppi schoolTypeToKoulutustyyppi(SchoolType schoolType) {
    if (schoolType != null) {
      switch (schoolType) {
        case UPPERSECONDARY:
          return Koulutustyyppi.lukio;
        case UPPERSECONDARYANDVOCATIONAL:
          return Koulutustyyppi.lukioJaAmmatillinen;
        case VOCATIONAL:
          return Koulutustyyppi.ammatillinen;
        case UNKNOWN:
          return Koulutustyyppi.tuntematon;
      }
    }
    
    return null;
  }

  private YTLAineKoodi examSubjectToYTLAineKoodi(MatriculationExamSubject examSubject, List<YTLAineKoodi> mapping) {
    return mapping.stream()
      .filter(ainekoodi -> (ainekoodi.getMatriculationExamSubject() == examSubject))
      .findFirst()
      .orElse(null);
  }
  
  private boolean isÄidinkieli(MatriculationExamAttendance attendance) {
    MatriculationExamSubject subject = attendance.getSubject();
    return 
        subject == MatriculationExamSubject.AI ||
        subject == MatriculationExamSubject.S2;
  }

  private List<YTLAineKoodi> readMapping() {
    ObjectMapper objectMapper = new ObjectMapper();
    
    try {
      InputStream json = getClass().getClassLoader().getResourceAsStream("fi/otavanopisto/pyramus/ytl/ytl_ainekoodit.json");
      return objectMapper.readValue(json, new TypeReference<List<YTLAineKoodi>>(){});
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Could not read YTL Mapping file", e);
    }
    
    return new ArrayList<>();
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }
  
}
