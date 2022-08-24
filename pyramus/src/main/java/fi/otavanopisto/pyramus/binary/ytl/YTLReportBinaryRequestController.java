package fi.otavanopisto.pyramus.binary.ytl;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.matriculation.DegreeType;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendanceFunding;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamTerm;
import fi.otavanopisto.pyramus.domainmodel.matriculation.SchoolType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.tor.StudentTOR;
import fi.otavanopisto.pyramus.tor.StudentTORController;
import fi.otavanopisto.pyramus.tor.TORSubject;
import fi.otavanopisto.pyramus.ytl.AbstractKokelas;
import fi.otavanopisto.pyramus.ytl.Kokelas;
import fi.otavanopisto.pyramus.ytl.Kokelas2022;
import fi.otavanopisto.pyramus.ytl.Kokelas2022Koe;
import fi.otavanopisto.pyramus.ytl.Koulutustyyppi;
import fi.otavanopisto.pyramus.ytl.SuoritettuKurssi;
import fi.otavanopisto.pyramus.ytl.Tutkintotyyppi;
import fi.otavanopisto.pyramus.ytl.YTLAineKoodi;
import fi.otavanopisto.pyramus.ytl.YTLAineKoodiSuoritettuKurssi;
import fi.otavanopisto.pyramus.ytl.YTLSiirtotiedosto;

/**
 * https://github.com/digabi/ilmoittautuminen/wiki/Ilmoittautumistiedot
 */
public class YTLReportBinaryRequestController extends BinaryRequestController {

  private static final Logger logger = Logger.getLogger(YTLReportBinaryRequestController.class.getName());
  private static final String KOSKI_HENKILO_OID = "koski.henkilo-oid";

  public void process(BinaryRequestContext binaryRequestContext) {
    List<YTLAineKoodi> m = readMapping();
    
    MatriculationExamEnrollmentDAO matriculationExamEnrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamDAO matriculationExamDAO = DAOFactory.getInstance().getMatriculationExamDAO();
    
    Long examId = binaryRequestContext.getLong("examId");
    MatriculationExam exam = matriculationExamDAO.findById(examId);
    
    String tutkintokerta = String.valueOf(exam.getExamYear()) + (exam.getExamTerm() == MatriculationExamTerm.SPRING ? "K" : "S");
    Integer koulunumero = binaryRequestContext.getInteger("schoolId");
    YTLSiirtotiedosto ytl = new YTLSiirtotiedosto(tutkintokerta, koulunumero);

    List<MatriculationExamEnrollment> enrollments = matriculationExamEnrollmentDAO.listDistinctByAttendanceTerms(exam,
        MatriculationExamEnrollmentState.APPROVED, exam.getExamYear(), exam.getExamTerm(), MatriculationExamAttendanceStatus.ENROLLED);
    enrollments = processCandidateNumbers(enrollments);
    enrollments.forEach(enrollment -> ytl.addKokelas(enrollmentToKokelas(enrollment, m)));
    
    ObjectMapper objectMapper = new ObjectMapper();
    
    try {
      String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ytl);
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

  private AbstractKokelas enrollmentToKokelas(MatriculationExamEnrollment enrollment, List<YTLAineKoodi> mapping) {
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
    
    AbstractKokelas kokelas;
    
    switch (enrollment.getDegreeStructure()) {
      case PRE2022:
        kokelas = vanhaKokelas(student, attendances, mapping);
      break;
      
      case POST2022:
        kokelas = uusiKokelas(student, attendances, mapping);
      break;
      
      default:
        throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Invalid degree structure.");
    }

    kokelas.getEtunimet().addAll(etunimet);
    kokelas.setSukunimi(student.getLastName());
    kokelas.setHetu(hetu);
    kokelas.setKokelasnumero(kokelasnumero);
    kokelas.setOppijanumero(oppijanumero);
    kokelas.setKoulutustyyppi(koulutustyyppi);
    kokelas.setTutkintotyyppi(tutkintotyyppi);
    kokelas.setUudelleenaloittaja(uudelleenaloittaja);
    
    return kokelas;
  }

  /**
   * Muodostaa kokelas-tietueen 2022 keväällä tai sen jälkeen tutkinnon aloittaneille.
   */
  private Kokelas2022 uusiKokelas(Student student, List<MatriculationExamAttendance> attendances,
      List<YTLAineKoodi> mapping) {
    Kokelas2022 kokelas = new Kokelas2022();
    
    final EnumSet<MatriculationExamAttendanceFunding> MAKSUTTOMAT = EnumSet.of(
        MatriculationExamAttendanceFunding.COMPULSORYEDUCATION_FREE, 
        MatriculationExamAttendanceFunding.COMPULSORYEDUCATION_FREE_RETRY);
    
    attendances.forEach(attendance -> {
      MatriculationExamSubject examSubject = attendance.getSubject();
      YTLAineKoodi ytlAineKoodi = examSubjectToYTLAineKoodi(examSubject, mapping);

      String aineKoodi = ytlAineKoodi.getYhdistettyAineKoodi();
      boolean maksuton = MAKSUTTOMAT.contains(attendance.getFunding());
      kokelas.addKoe(new Kokelas2022Koe(aineKoodi, maksuton));
    });
    
    return kokelas;
  }

  /**
   * Muodostaa vanhantyylisen kokelas-tietueen. Käytetään opiskelijoille,
   * jotka ovat aloittaneet tutkinnon ennen kevättä 2022.
   */
  private Kokelas vanhaKokelas(Student student, List<MatriculationExamAttendance> attendances, List<YTLAineKoodi> mapping) {
    Kokelas kokelas = new Kokelas();

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
    
    kokelas.setÄidinkielenKoe(äidinkielenKoe);

    StudentTOR tor;
    try {
      tor = StudentTORController.constructStudentTOR(student);
    } catch (Exception ex) {
      tor = new StudentTOR();
      logger.log(Level.SEVERE, String.format("Failed to construct TOR for Student %d", student.getId()), ex);
    }

    Set<String> suoritetutKurssitAineet = new HashSet<>();
    
    for (MatriculationExamAttendance attendance : attendances) {
      MatriculationExamSubject examSubject = attendance.getSubject();
      
      YTLAineKoodi ytlAineKoodi = examSubjectToYTLAineKoodi(examSubject, mapping);
      if (ytlAineKoodi != null) {
        // Äidinkielelle on oma lokeronsa, ei lisätä pakolliseksi/ylimääräiseksi
        if (!isÄidinkieli(attendance)) {
          if (attendance.isMandatory()) {
            kokelas.addPakollinenKoe(ytlAineKoodi.getYhdistettyAineKoodi());
          } else {
            kokelas.addYlimääräinenKoe(ytlAineKoodi.getYhdistettyAineKoodi());
          }
        }
        
        suoritetutKurssitAineet.add(ytlAineKoodi.getYtlAine());
        if (CollectionUtils.isNotEmpty(ytlAineKoodi.getSuoritetutKurssitLisäaineet())) {
          suoritetutKurssitAineet.addAll(ytlAineKoodi.getSuoritetutKurssitLisäaineet());
        }
      } else {
        logger.warning(String.format("No subject mapping found for %s", examSubject));
      }
    }

    List<SuoritettuKurssi> suoritetutKurssit = new ArrayList<>();
    
    for (String suoritetutKurssitAine : suoritetutKurssitAineet) {
      List<YTLAineKoodi> ytlAineKoodit = ytlSubjectToYTLAineKoodi(suoritetutKurssitAine, mapping);
      for (YTLAineKoodi ytlAineKoodi : ytlAineKoodit) {
        lataaSuoritetutKurssit(student, tor, ytlAineKoodi, suoritetutKurssit);
      }
    }

    kokelas.setSuoritetutKurssit(suoritetutKurssit);
    
    return kokelas;
  }

  private void lataaSuoritetutKurssit(Student student, StudentTOR tor, YTLAineKoodi ytlAineKoodi, List<SuoritettuKurssi> suoritetutKurssit) {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();
    
    long kurssiLukumäärä = 0;
    
    for (YTLAineKoodiSuoritettuKurssi ytlAKSK : ytlAineKoodi.getSuoritetutKurssit()) {
      EducationType educationType = educationTypeDAO.findById(ytlAKSK.getEducationType());
      if (educationType != null) {
        Subject subject = subjectDAO.findByEducationTypeAndCode(educationType, ytlAKSK.getSubjectCode());
        if (subject != null) {
          TORSubject torSubject = tor.findSubject(subject.getCode());
          if (torSubject != null) {
            kurssiLukumäärä += torSubject.getPassedCoursesCount();
          }
        } else {
          logger.warning(String.format("Specified subjectcode %s for educationtype %d was not found", ytlAKSK.getSubjectCode(), ytlAKSK.getEducationType()));
        }
      } else {
        logger.warning(String.format("Specified educationtype %d was not found", ytlAKSK.getEducationType()));
      }
    }

    /**
     * MAY lisätään ainevalinnan mukaan joko pitkän tai lyhyen matematiikan kurssilukumäärään
     */
    if (StringUtils.equals(ytlAineKoodi.getYtlAine(), "M") || StringUtils.equals(ytlAineKoodi.getYtlAine(), "N")) {
      UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
      String valittuMatematiikka = userVariableDAO.findByUserAndKey(student, "lukioMatematiikka");
      if ((StringUtils.equals(ytlAineKoodi.getYtlAine(), "M") && StringUtils.equals(valittuMatematiikka, "MAA")) || 
          (StringUtils.equals(ytlAineKoodi.getYtlAine(), "N") && StringUtils.equals(valittuMatematiikka, "MAB"))) {
        TORSubject torSubject = tor.findSubject("MAY");
        if (torSubject != null) {
          kurssiLukumäärä += torSubject.getPassedCoursesCount();
        }
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
  
  private List<YTLAineKoodi> ytlSubjectToYTLAineKoodi(String ytlSubject, List<YTLAineKoodi> mapping) {
    return mapping.stream()
      .filter(ainekoodi -> StringUtils.equals(ainekoodi.getYtlAine(), ytlSubject))
      .collect(Collectors.toList());
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
