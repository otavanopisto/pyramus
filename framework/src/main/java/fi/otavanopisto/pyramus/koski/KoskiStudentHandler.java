package fi.otavanopisto.pyramus.koski;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.dao.base.SchoolVariableDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.koski.KoskiPersonLogDAO;
import fi.otavanopisto.pyramus.dao.students.StudentLodgingPeriodDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.dao.students.StudentSubjectGradeDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditFunding;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.koski.CreditStubCredit.Type;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kunta;
import fi.otavanopisto.pyramus.koski.koodisto.Lahdejarjestelma;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.model.HenkilovahvistusPaikkakunnalla;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiNumeerinen;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiSanallinen;
import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.LahdeJarjestelmaID;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusJakso;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusTila;
import fi.otavanopisto.pyramus.koski.model.Oppilaitos;
import fi.otavanopisto.pyramus.koski.model.Organisaatio;
import fi.otavanopisto.pyramus.koski.model.OrganisaatioHenkilo;
import fi.otavanopisto.pyramus.koski.model.OrganisaatioOID;
import fi.otavanopisto.pyramus.koski.model.OsaamisenTunnustaminen;
import fi.otavanopisto.pyramus.koski.model.SisaltavaOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.settings.KoskiStudyProgrammeHandlerParams;
import fi.otavanopisto.pyramus.koski.settings.StudyEndReasonMapping;

public abstract class KoskiStudentHandler {

  public static final String KOSKI_STUDYPERMISSION_ID = "koski.studypermission-id";
  public static final String KOSKI_INTERNETIX_STUDYPERMISSION_ID = "koski.internetix-studypermission-id";
  public static final String KOSKI_LINKED_STUDYPERMISSION_ID = "koski.linked-to-studypermission-id";
  public static final String KOSKI_SCHOOL_OID = "koski.schooloid";

  @Inject
  private Logger logger;
  
  @Inject
  protected KoskiSettings settings;
  
  @Inject 
  protected UserVariableDAO userVariableDAO;

  @Inject 
  protected UserVariableKeyDAO userVariableKeyDAO;

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

  @Inject
  private StudentSubjectGradeDAO studentSubjectGradeDAO;
  
  @Inject
  private StudentStudyPeriodDAO studentStudyPeriodDAO;
  
  public abstract void saveOrValidateOid(KoskiStudyProgrammeHandler handler, Student student, String oid);
  public abstract void removeOid(KoskiStudyProgrammeHandler handler, Student student, String oid);
  public abstract Set<KoskiStudentId> listOids(Student student);
  
  protected void saveOrValidateOid(Student student, String oid) {
    String studyOid = userVariableDAO.findByUserAndKey(student, KOSKI_STUDYPERMISSION_ID);

    if (StringUtils.isBlank(studyOid)) {
      userVariableDAO.setUserVariable(student, KOSKI_STUDYPERMISSION_ID, oid);
    } else {
      // Validate the oid is the same
      if (!StringUtils.equals(studyOid, oid))
        throw new RuntimeException(String.format("Returned study permit oid %s doesn't match the saved oid %s.", oid, studyOid));
    }
  }

  protected void removeOid(Student student, String oid) {
    String storedStudyOid = userVariableDAO.findByUserAndKey(student, KOSKI_STUDYPERMISSION_ID);
    
    if (StringUtils.equals(storedStudyOid, oid)) {
      userVariableDAO.setUserVariable(student, KOSKI_STUDYPERMISSION_ID, "");
    } else {
      logger.severe(String.format("removeOid failed for student %d with oid %s", student.getId(), oid));
    }
  }

  protected void saveOrValidateInternetixOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    Set<KoskiStudentId> oids = loadInternetixOids(student);

    String studentIdentifier = getStudentIdentifier(handler, student.getId());
    KoskiStudentId koskiStudentId = oids.stream().filter(koskiId -> StringUtils.equals(koskiId.getStudentIdentifier(), studentIdentifier)).findFirst().orElse(null);
    
    if (koskiStudentId == null || StringUtils.isBlank(koskiStudentId.getOid())) {
      if (koskiStudentId != null) {
        koskiStudentId.setOid(oid);
      } else {
        oids.add(new KoskiStudentId(studentIdentifier, oid));
      }
      
      ObjectMapper mapper = new ObjectMapper();
      try {
        String variableValue = CollectionUtils.isNotEmpty(oids) ? mapper.writeValueAsString(oids) : "";
        userVariableDAO.setUserVariable(student, KOSKI_INTERNETIX_STUDYPERMISSION_ID, variableValue);
      } catch (Exception ex) {
        logger.severe(String.format("Serialization failed for student %s", student.getId()));
      }
    } else {
      // Validate the oid is the same
      if (!StringUtils.equals(koskiStudentId.getOid(), oid))
        throw new RuntimeException(String.format("Returned study permit oid %s doesn't match the saved oid %s.", oid, koskiStudentId.getOid()));
    }
  }

  protected void removeInternetixOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    String studentIdentifier = getStudentIdentifier(handler, student.getId());
    
    Set<KoskiStudentId> oids = loadInternetixOids(student);
    oids.removeIf(koskiStudentId -> StringUtils.equals(koskiStudentId.getStudentIdentifier(), studentIdentifier) && StringUtils.equals(koskiStudentId.getOid(), oid));
    
    ObjectMapper mapper = new ObjectMapper();
    try {
      String variableValue = CollectionUtils.isNotEmpty(oids) ? mapper.writeValueAsString(oids) : "";
      userVariableDAO.setUserVariable(student, KOSKI_INTERNETIX_STUDYPERMISSION_ID, variableValue);
    } catch (Exception ex) {
      logger.severe(String.format("Serialization failed for student %s", student.getId()));
    }
  }

  protected Set<KoskiStudentId> loadInternetixOids(Student student) {
    UserVariableKey userVariableKey = userVariableKeyDAO.findByVariableKey(KOSKI_INTERNETIX_STUDYPERMISSION_ID);
    
    if (userVariableKey != null) {
      UserVariable userVariable = userVariableDAO.findByUserAndVariableKey(student, userVariableKey);
      if (userVariable != null && StringUtils.isNotBlank(userVariable.getValue())) {
        ObjectMapper mapper = new ObjectMapper();
        
        TypeReference<Set<KoskiStudentId>> typeRef = new TypeReference<Set<KoskiStudentId>>() {};
        try {
          return mapper.readValue(userVariable.getValue(), typeRef);
        } catch (Exception ex) {
          logger.log(Level.SEVERE, String.format("Couldn't parse internetix Oids for student %d", student.getId()), ex);
        }
      }
    }
    
    return new HashSet<>();
  }
  
  protected String resolveInternetixOid(Student student, KoskiStudyProgrammeHandler handlerType) {
    Set<KoskiStudentId> oids = loadInternetixOids(student);
    String studentIdentifier = getStudentIdentifier(handlerType, student.getId());
    KoskiStudentId koskiStudentId = oids.stream().filter(koskiId -> StringUtils.equals(koskiId.getStudentIdentifier(), studentIdentifier)).findFirst().orElse(null);
    return koskiStudentId != null ? koskiStudentId.getOid() : null;
  }

  protected StudyEndReasonMapping opiskelujaksot(Student student, OpiskeluoikeusTila tila, OpintojenRahoitus rahoitus) {
    if (!Boolean.TRUE.equals(student.getArchived())) {
      OpiskeluoikeusJakso opintojenAlkamisjakso = new OpiskeluoikeusJakso(student.getStudyStartDate(), OpiskeluoikeudenTila.lasna);
      opintojenAlkamisjakso.setOpintojenRahoitus(new KoodistoViite<>(rahoitus));
      tila.addOpiskeluoikeusJakso(opintojenAlkamisjakso);
  
      List<StudentStudyPeriod> studyPeriods = studentStudyPeriodDAO.listByStudent(student);
      studyPeriods.sort(Comparator.comparing(StudentStudyPeriod::getBegin));
      
      for (StudentStudyPeriod period : studyPeriods) {
        switch (period.getPeriodType()) {
          case TEMPORARILY_SUSPENDED:
            tila.addOpiskeluoikeusJakso(new OpiskeluoikeusJakso(period.getBegin(), OpiskeluoikeudenTila.valiaikaisestikeskeytynyt));
  
            if (period.getEnd() != null) {
              OpiskeluoikeusJakso väliaikaisenKeskeytymisenPäättymisenJälkeinenLäsnäoloJakso = new OpiskeluoikeusJakso(period.getEnd(), OpiskeluoikeudenTila.lasna);
              väliaikaisenKeskeytymisenPäättymisenJälkeinenLäsnäoloJakso.setOpintojenRahoitus(new KoodistoViite<>(rahoitus));
              tila.addOpiskeluoikeusJakso(väliaikaisenKeskeytymisenPäättymisenJälkeinenLäsnäoloJakso);
            }
          break; 
          case PROLONGED_STUDYENDDATE:
            // Pidennetty päättymispäivä - päättymispäivää ei raportoida Koskeen / rahoitus sama kuin normaalitilanteessa
            OpiskeluoikeusJakso pidennettyPäättymispäiväjakso = new OpiskeluoikeusJakso(period.getBegin(), OpiskeluoikeudenTila.lasna);
            pidennettyPäättymispäiväjakso.setOpintojenRahoitus(new KoodistoViite<>(rahoitus));
            tila.addOpiskeluoikeusJakso(pidennettyPäättymispäiväjakso);
          break;
        }
      }
      
      if (student.getStudyEndDate() != null) {
        OpiskeluoikeudenTila opintojenLopetusTila = OpiskeluoikeudenTila.eronnut;
        StudyEndReasonMapping studyEndReasonMapping = student.getStudyEndReason() != null ? 
            settings.getStudyEndReasonMapping(student.getStudyEndReason()) : null;
  
        if (studyEndReasonMapping != null) {
          opintojenLopetusTila = studyEndReasonMapping.getOpiskeluoikeudenTila();
        } else {
          koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.MISSING_STUDYENDREASONMAPPING, new Date());
        }
        
        OpiskeluoikeusJakso opintojenPäättymisjakso = new OpiskeluoikeusJakso(student.getStudyEndDate(), opintojenLopetusTila);
        opintojenPäättymisjakso.setOpintojenRahoitus(new KoodistoViite<>(rahoitus));
        tila.addOpiskeluoikeusJakso(opintojenPäättymisjakso);
        
        return studyEndReasonMapping;
      }
    } else {
      // Student.archived=true -> mitätöity
      tila.addOpiskeluoikeusJakso(new OpiskeluoikeusJakso(new Date(), OpiskeluoikeudenTila.mitatoity));
    }
    
    return null;
  }
  
  protected Kuvaus kuvaus(String fiKuvaus) {
    Kuvaus kuvaus = new Kuvaus();
    kuvaus.setFi(fiKuvaus);
    return kuvaus;
  }

  protected HenkilovahvistusPaikkakunnalla getVahvistus(Student student, StaffMember approver, Date approvalDate, String academyOid) {
    if (approver != null) {
      String nimi = approver.getFirstName() + " " + approver.getLastName();
      String titteli = approver.getTitle();
      
      if (StringUtils.isAnyBlank(nimi, titteli)) {
        koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.MISSING_STUDYAPPROVER, new Date());
        return null;
      }

      if (approvalDate == null) {
        koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.MISSING_STUDYAPPROVEDATE, new Date());
        return null;
      }
      
      Organisaatio henkilonOrganisaatio = new OrganisaatioOID(academyOid);
      OrganisaatioHenkilo henkilo = new OrganisaatioHenkilo(nimi, kuvaus(titteli), henkilonOrganisaatio);

      Organisaatio myontajaOrganisaatio = new OrganisaatioOID(academyOid);
      HenkilovahvistusPaikkakunnalla vahvistus = new HenkilovahvistusPaikkakunnalla(
          approvalDate, Kunta.K491, myontajaOrganisaatio);
      vahvistus.addMyontajaHenkilo(henkilo);
      return vahvistus;
    } else {
      koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.MISSING_STUDYAPPROVER, new Date());
      return null;
    }
  }

  protected HenkilovahvistusPaikkakunnalla getVahvistus(Student student, String academyOid) {
    return getVahvistus(student, student.getStudyApprover(), student.getStudyEndDate(), academyOid);
  }

  protected String getStudentIdentifier(KoskiStudyProgrammeHandler handler, Long studentId) {
    return KoskiConsts.getStudentIdentifier(handler, studentId);
  }
  
  protected LahdeJarjestelmaID getLahdeJarjestelmaID(KoskiStudyProgrammeHandler handler, Long studentId) {
    return new LahdeJarjestelmaID(getStudentIdentifier(handler, studentId), Lahdejarjestelma.pyramus);
  }

  protected String getDiaarinumero(KoskiStudyProgrammeHandler handler, OpiskelijanOPS ops) {
    return settings.getSettings().getKoski().getHandlerParams(handler).getDiaariNumero(ops);
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
    if (grade != null) {
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
    String accomplishmentsStr = userVariableDAO.findByUserAndKey(student, "lukioSmerkinta");

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
 
  /**
   * Returns credits for one ops
   */
  protected List<CreditStub> listCredits(Student student, boolean listTransferCredits, boolean listCreditLinks,
      OpiskelijanOPS ops, Predicate<Credit> filter) {
    List<OpiskelijanOPS> opsList = new ArrayList<>();
    opsList.add(ops);
    
    Map<OpiskelijanOPS, List<CreditStub>> credits = listCredits(student, listTransferCredits, listCreditLinks, opsList, ops, filter);
    return credits.get(ops) != null ? credits.get(ops) : new ArrayList<>();
  }
  
  protected Map<OpiskelijanOPS, List<CreditStub>> listCredits(Student student, boolean listTransferCredits, boolean listCreditLinks,
      List<OpiskelijanOPS> orderedOPSs, OpiskelijanOPS defaultOPS, Predicate<Credit> filter) {

    // Map of OPS -> CourseCode -> CreditStub
    Map<OpiskelijanOPS, Map<String, CreditStub>> map = new HashMap<>();
    
    Set<Long> filteredGrades = settings.getSettings().getKoski().getFilteredGrades();

    List<CourseAssessment> courseAssessments = courseAssessmentDAO.listByStudent(student);
    courseAssessments.stream().filter(filter).filter(credit -> !isFilteredGrade(credit, filteredGrades)).forEach(ca -> {
      Course course = ca.getCourseStudent() != null ? ca.getCourseStudent().getCourse() : null;
      Subject subject = course != null ? course.getSubject() : null;
      OpiskelijanOPS creditOPS = resolveSingleOPSFromCredit(ca, orderedOPSs, defaultOPS);
      
      if (creditOPS != null) {
        Map<String, CreditStub> stubs = map.get(creditOPS);
        if (stubs == null) {
          map.put(creditOPS, stubs = new HashMap<>());
        }
        
        String courseCode = courseCode(subject, course.getCourseNumber(), ca.getId());
        CreditStub stub;
        if (!stubs.containsKey(courseCode)) {
          stub = new CreditStub(courseCode, course.getCourseNumber(), course.getName(), subject);
          stubs.put(courseCode, stub);
        } else {
          stub = stubs.get(courseCode);
        }
        
        stub.addCredit(new CreditStubCredit(ca, Type.CREDIT));
      } else {
        logger.log(Level.WARNING, String.format("Couldn't resolve OPS for CourseAssessment %d", ca.getId()));
        koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNRESOLVED_CREDIT_CURRICULUM, new Date(), course.getName());
      }
    });
    
    if (listTransferCredits) {
      List<TransferCredit> transferCredits = transferCreditDAO.listByStudent(student);
      transferCredits.stream().filter(filter).filter(credit -> !isFilteredGrade(credit, filteredGrades)).forEach(tc -> {
        OpiskelijanOPS creditOPS = resolveSingleOPSFromCredit(tc, orderedOPSs, defaultOPS);
        
        if (creditOPS != null) {
          Map<String, CreditStub> stubs = map.get(creditOPS);
          if (stubs == null) {
            map.put(creditOPS, stubs = new HashMap<>());
          }
          
          String courseCode = courseCode(tc.getSubject(), tc.getCourseNumber(), tc.getId());
          CreditStub stub;
          if (!stubs.containsKey(courseCode)) {
            stub = new CreditStub(courseCode, tc.getCourseNumber(), tc.getCourseName(), tc.getSubject());
            stubs.put(courseCode, stub);
          } else {
            stub = stubs.get(courseCode);
          }
          
          stub.addCredit(new CreditStubCredit(tc, Type.RECOGNIZED));
        } else {
          logger.log(Level.WARNING, String.format("Couldn't resolve OPS for TransferCredit %d", tc.getId()));
          koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNRESOLVED_CREDIT_CURRICULUM, new Date(), tc.getCourseName());
        }
      });
    }

    if (listCreditLinks) {
      List<CreditLink> creditLinks = creditLinkDAO.listByStudent(student);
      
      creditLinks.stream().map(creditLink -> creditLink.getCredit()).filter(filter).filter(credit -> !isFilteredGrade(credit, filteredGrades)).forEach(credit -> {
        Subject subject = null;
        Integer courseNumber = null;
        String courseName = null;
        OpiskelijanOPS creditOPS = null;
        
        switch (credit.getCreditType()) {
          case CourseAssessment:
            CourseAssessment ca = (CourseAssessment) credit;
            Course course = ca.getCourseStudent() != null ? ca.getCourseStudent().getCourse() : null;
  
            subject = course != null ? course.getSubject() : null;
            courseNumber = course.getCourseNumber();
            courseName = course.getName();

            creditOPS = resolveSingleOPSFromCredit(ca, orderedOPSs, defaultOPS);
          break;
          case TransferCredit:
            TransferCredit tc = (TransferCredit) credit;
            
            subject = tc.getSubject();
            courseNumber = tc.getCourseNumber();
            courseName = tc.getCourseName();

            creditOPS = resolveSingleOPSFromCredit(tc, orderedOPSs, defaultOPS);
          break;
          case ProjectAssessment:
          break;
        }
        
        if (allNotNull(subject, courseNumber, courseName, creditOPS)) {
          String courseCode = courseCode(subject, courseNumber, credit.getId());
          CreditStub stub;
          Map<String, CreditStub> stubs = map.get(creditOPS);
          if (stubs == null) {
            map.put(creditOPS, stubs = new HashMap<>());
          }

          if (!stubs.containsKey(courseCode)) {
            stub = new CreditStub(courseCode, courseNumber, courseName, subject);
            stubs.put(courseCode, stub);
          } else {
            stub = stubs.get(courseCode);
          }
          
          stub.addCredit(new CreditStubCredit(credit, Type.RECOGNIZED));
        }
      });
    }

    Map<OpiskelijanOPS, List<CreditStub>> result = new HashMap<>();
    for (OpiskelijanOPS ops : map.keySet()) {
      Map<String, CreditStub> stubs = map.get(ops);
      
      List<CreditStub> stubList = new ArrayList<>(stubs.values());
      stubList.sort((a, b) -> ObjectUtils.compare(a.getCourseNumber(), b.getCourseNumber()));
      
      result.put(ops, stubList);
    }
    
    return result;
  }

  private boolean isFilteredGrade(Credit credit, Set<Long> filteredGrades) {
    if (credit != null && credit.getGrade() != null) {
      Grade grade = credit.getGrade();
      
      return filteredGrades.contains(grade.getId());
    }
    
    return false;
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
  
  
  /**
   * Returns true if credits' education type matches the given education type 
   */
  protected boolean matchingEducationTypeFilter(Credit credit, List<Long> educationTypes) {
    if (credit instanceof CourseAssessment) {
      CourseAssessment courseAssessment = (CourseAssessment) credit;
      if (courseAssessment.getCourseStudent() != null && 
          courseAssessment.getCourseStudent().getCourse() != null && 
          courseAssessment.getCourseStudent().getCourse().getSubject() != null &&
          courseAssessment.getCourseStudent().getCourse().getSubject().getEducationType() != null) {
        return educationTypes.contains(courseAssessment.getCourseStudent().getCourse().getSubject().getEducationType().getId());
      }
    } else if (credit instanceof TransferCredit) {
      TransferCredit transferCredit = (TransferCredit) credit;
      if (transferCredit.getSubject() != null &&
          transferCredit.getSubject().getEducationType() != null) {
        return educationTypes.contains(transferCredit.getSubject().getEducationType().getId());
      }
    }
    
    return false;
  }
  
  protected boolean matchingCurriculumFilter(Student student, Credit credit) {
    if (credit instanceof CourseAssessment) {
      return matchingCurriculum(student, ((CourseAssessment) credit).getCourseStudent().getCourse());
    } else if (credit instanceof TransferCredit) {
      return matchingCurriculum(student, (TransferCredit) credit);
    } else {
      logger.severe(String.format("Credit %d instance not recognized.", credit.getId()));
    }
    
    return false;
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
  
  protected OpiskelijanOPS resolveOPS(Student student) {
    Curriculum curriculum = student.getCurriculum();
    if (curriculum != null) {
      return resolveOPS(curriculum.getId().intValue());
    }
    return null;
  }
  
  protected OpiskelijanOPS resolveOPS(Long curriculumId) {
    return resolveOPS(curriculumId.intValue());
  }
  
  protected OpiskelijanOPS resolveOPS(Curriculum curriculum) {
    if (curriculum != null) {
      return resolveOPS(curriculum.getId().intValue());
    } else {
      return null;
    }
  }
  
  protected OpiskelijanOPS resolveOPS(int curriculumId) {
    switch (curriculumId) {
      case 1:
        return OpiskelijanOPS.ops2016;
      case 2:
        return OpiskelijanOPS.ops2005;
      case 3:
        return OpiskelijanOPS.ops2018;
    }
    
    return null;
  }

  /**
   * Handles the situation when Student's studies are linked to other studies in another
   * school. Returns false if some of the needed variables for linking is missing, true otherwise.
   */
  protected boolean handleLinkedStudyOID(Student student, Opiskeluoikeus opiskeluoikeus) {
    if (student.getSchool() != null) {
      String linkedStudyOID = userVariableDAO.findByUserAndKey(student, KOSKI_LINKED_STUDYPERMISSION_ID);
      String schoolOID = schoolVariableDAO.findValueBySchoolAndKey(student.getSchool(), KOSKI_SCHOOL_OID);
      if (StringUtils.isNotBlank(linkedStudyOID) && StringUtils.isNotBlank(schoolOID)) {
        Oppilaitos oppilaitos = new Oppilaitos(schoolOID);
        opiskeluoikeus.setSisaltyyOpiskeluoikeuteen(new SisaltavaOpiskeluoikeus(oppilaitos, linkedStudyOID));
        return true;
      } else {
        return false;
      }
    }
    return true;
  }

  protected Kuvaus getTodistuksellaNakyvatLisatiedot(Student student) {
    StringBuilder sb = new StringBuilder();
    
    for (int i = 1; i <= 5; i++) {
      UserVariableKey key = userVariableKeyDAO.findByVariableKey("todistus.lisatiedot" + i);
      if (key != null) {
        UserVariable variable = userVariableDAO.findByUserAndVariableKey(student, key);
        
        if (variable != null) {
          if (StringUtils.isNotBlank(variable.getValue())) {
            if (sb.length() > 0) {
              sb.append(" ");
            }
            
            sb.append(variable.getValue().trim());
          }
        }
      }
    }
    
    return sb.length() > 0 ? kuvaus(sb.toString()) : null;
  }

  /**
   * Returns preferred OPS for CourseAssessment.
   * If CourseAssessments' Course has Curriculums
   *  - returns first matching curriculum from orderedOPSs
   *  - if no matching curriculums are found returns null
   * If CourseAssessments' Course has no Curriculums
   *  - returns defaultOPS
   * If anything is null from CourseAssessment to Course:Curriculums, returns null
   */
  protected OpiskelijanOPS resolveSingleOPSFromCredit(CourseAssessment courseAssessment, List<OpiskelijanOPS> orderedOPSs, OpiskelijanOPS defaultOPS) {
    if (courseAssessment.getCourseStudent() != null && courseAssessment.getCourseStudent().getCourse() != null && courseAssessment.getCourseStudent().getCourse().getCurriculums() != null) {
      Set<OpiskelijanOPS> creditOPSs = courseAssessment.getCourseStudent().getCourse().getCurriculums().stream()
          .map(curriculum -> resolveOPS(curriculum))
          .filter(Objects::nonNull)
          .collect(Collectors.toSet());

      // No curriculums set in the credit -> return default
      if (CollectionUtils.isEmpty(creditOPSs)) {
        return defaultOPS;
      }

      // Find the first common curriculum
      for (OpiskelijanOPS ops : orderedOPSs) {
        if (creditOPSs.contains(ops)) {
          return ops;
        }
      }
    }
    
    return null;
  }

  /**
   * Returns preferred OPS for TransferCredit.
   * - If TransferCredit has curriculum
   *   - and it exists in orderedOPSs, returns TransferCredit:Curriculum
   *   - and it doesn't exist in orderedOPSs, returns null (mismatch)
   * - If TransferCredit doesn't have curriculum, returns defaultOPS 
   */
  protected OpiskelijanOPS resolveSingleOPSFromCredit(TransferCredit transferCredit, List<OpiskelijanOPS> orderedOPSs, OpiskelijanOPS defaultOPS) {
    if (transferCredit.getCurriculum() != null) {
      OpiskelijanOPS ops = resolveOPS(transferCredit.getCurriculum());
      
      // If the curriculum is in the list, return it
      if (orderedOPSs.contains(ops)) {
        return ops;
      } else {
        return null;
      }
    } else {
      return defaultOPS;
    }
  }

  protected <T> void collectAccomplishedMarks(Subject subject, T subjectCounterpart,
      StudentSubjectSelections studentSubjects, Set<T> accomplished) {
    if (subject != null && subjectCounterpart != null) {
      if (studentSubjects.isAccomplishment(subject.getId())) {
        accomplished.add(subjectCounterpart);
      }
    }
  }

  protected KoskiStudyProgrammeHandlerParams getHandlerParams(KoskiStudyProgrammeHandler handlerType) {
    KoskiStudyProgrammeHandlerParams handlerParams = settings.getSettings().getKoski().getHandlerParams().get(handlerType);

    if (handlerParams == null) {
      throw new RuntimeException(String.format("HandlerParams not set for %s", handlerType));
    }

    return handlerParams;
  }
  
  protected ArviointiasteikkoYleissivistava getSubjectGrade(Student student, Subject subject) {
    StudentSubjectGrade studentSubjectGrade = studentSubjectGradeDAO.findBy(student, subject);
    
    if (studentSubjectGrade != null && studentSubjectGrade.getGrade() != null) {
      return getArvosana(studentSubjectGrade.getGrade());
    }
    
    return null;
  }

  protected StudentSubjectGrade findStudentSubjectGrade(Student student, Subject subject) {
    return studentSubjectGradeDAO.findBy(student, subject);
  }

  protected <T extends KurssinSuoritus> T luoKurssiSuoritus(T suoritus, CreditStub courseCredit) {
    CreditStubCredit parasArvosana = courseCredit.getCredits().getBestCredit();
    
    if (parasArvosana != null) {
      if (parasArvosana.getType() == Type.RECOGNIZED) {
        OsaamisenTunnustaminen tunnustettu = new OsaamisenTunnustaminen(kuvaus("Hyväksiluku"));
        
        if (parasArvosana.getTransferCreditFunding() == TransferCreditFunding.GOVERNMENT_FUNDING) {
          tunnustettu.setRahoituksenPiirissä(true);
        }
        
        suoritus.setTunnustettu(tunnustettu);
      }
      
      ArviointiasteikkoYleissivistava arvosana = getArvosana(parasArvosana.getGrade());
      
      KurssinArviointi arviointi = null;
      if (ArviointiasteikkoYleissivistava.isNumeric(arvosana)) {
        arviointi =  new KurssinArviointiNumeerinen(arvosana, parasArvosana.getDate());
      } else if (ArviointiasteikkoYleissivistava.isLiteral(arvosana)) {
        arviointi = new KurssinArviointiSanallinen(arvosana, parasArvosana.getDate(), kuvaus(parasArvosana.getGrade().getName()));
      } else {
        logger.log(Level.SEVERE, String.format("Grade %s is neither numeric nor literal", arvosana));
        return null;
      }

      suoritus.addArviointi(arviointi);
      return suoritus;
    } else {
      // Kurssisuoritus ohitetaan jos arvosanaa ei ole
      return null;
    }
  }
  
  protected OpintojenRahoitus opintojenRahoitus(Student student) {
    if (student != null) {
      if (student.getFunding() != null) {
        switch (student.getFunding()) {
          case GOVERNMENT_FUNDING:
            return OpintojenRahoitus.K1;
          case OTHER_FUNDING:
            return OpintojenRahoitus.K6;
        }
      }

      // Rahoitus; jos määritetty jokin kiinteä arvo, käytetään sitä
      OpintojenRahoitus opintojenRahoitus = settings.getOpintojenRahoitus(student.getStudyProgramme().getId());
      if (opintojenRahoitus == null) {
        // Jos kiinteää rahoitusarvoa ei ole määritetty, rahoitus määräytyy oppilaitoksen mukaan
        opintojenRahoitus = student.getSchool() == null ? OpintojenRahoitus.K1 : OpintojenRahoitus.K6;
      }
    
      return opintojenRahoitus;
    } else {
      return null;
    }
  }

}
