package fi.otavanopisto.pyramus.koski.model.internetix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade;
import fi.otavanopisto.pyramus.koski.CreditStub;
import fi.otavanopisto.pyramus.koski.CreditStubCredit;
import fi.otavanopisto.pyramus.koski.KoskiStudentId;
import fi.otavanopisto.pyramus.koski.KoskiStudyProgrammeHandler;
import fi.otavanopisto.pyramus.koski.OpiskelijanOPS;
import fi.otavanopisto.pyramus.koski.OppiaineenSuoritusWithCurriculum;
import fi.otavanopisto.pyramus.koski.StudentSubjectSelections;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssit;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssitOPS2004Aikuiset;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineMatematiikka;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;
import fi.otavanopisto.pyramus.koski.model.lukio.AbstractKoskiLukioStudentHandler;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunniste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnistePaikallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnisteValtakunnallinenOPS2004;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnisteValtakunnallinenOPS2015;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenArviointi;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusAidinkieli;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusEiTiedossa;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusMatematiikka;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusMuuValtakunnallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusPaikallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusVierasKieli;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.settings.KoskiStudyProgrammeHandlerParams;
import fi.otavanopisto.pyramus.koski.settings.StudyEndReasonMapping;

public class KoskiInternetixLukioStudentHandler extends AbstractKoskiLukioStudentHandler {

  private static final KoskiStudyProgrammeHandler HANDLER_TYPE = KoskiStudyProgrammeHandler.aineopiskelulukio;

  @Inject
  private Logger logger;
  
  @Override
  protected boolean isPakollinenOppiaine(Student student, KoskiOppiaineetYleissivistava oppiaine) {
    KoskiStudyProgrammeHandlerParams handlerParams = getHandlerParams(HANDLER_TYPE);
    return handlerParams.isPakollinenOppiaine(oppiaine);
  }
  
  public OpiskeluoikeusInternetix studentToModel(Student student, String academyIdentifier) {
    StudentSubjectSelections studentSubjects = loadStudentSubjectSelections(student, getDefaultSubjectSelections());
    String studyOid = resolveInternetixOid(student, HANDLER_TYPE);
    
    // Skip student if it is archived and the studyoid is blank
    if (Boolean.TRUE.equals(student.getArchived()) && StringUtils.isBlank(studyOid)) {
      return null;
    }

    if (student.getStudyStartDate() == null) {
      koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.NO_STUDYSTARTDATE, new Date());
      return null;
    }

    boolean defaultStudyProgramme = 
        settings.getStudyProgrammeHandlerType(student.getStudyProgramme().getId()) == HANDLER_TYPE;

    LukionOpiskeluoikeus opiskeluoikeus = new LukionOpiskeluoikeus();
    opiskeluoikeus.setLahdejarjestelmanId(getLahdeJarjestelmaID(HANDLER_TYPE, student.getId()));
    opiskeluoikeus.setAlkamispaiva(student.getStudyStartDate());
    opiskeluoikeus.setPaattymispaiva(student.getStudyEndDate());
    if (StringUtils.isNotBlank(studyOid)) {
      opiskeluoikeus.setOid(studyOid);
    }

    opiskeluoikeus.setLisatiedot(getLisatiedot(student));

    OpintojenRahoitus opintojenRahoitus = opintojenRahoitus(student);
    StudyEndReasonMapping lopetusSyy = opiskelujaksot(student, opiskeluoikeus.getTila(), opintojenRahoitus);
    boolean laskeKeskiarvot = lopetusSyy != null ? lopetusSyy.getLaskeAinekeskiarvot() : false;
    boolean sisällytäVahvistus = lopetusSyy != null ? lopetusSyy.getSisällytäVahvistaja() : false;
    boolean lukionOppimääräSuoritettu = lopetusSyy != null ? lopetusSyy.isLukionOppimääräSuoritettu() : false;
    opiskeluoikeus.setOppimaaraSuoritettu(lukionOppimääräSuoritettu || sisällytäVahvistus);

    KoskiStudyProgrammeHandlerParams handlerParams = getHandlerParams(HANDLER_TYPE);

    // toimipiste-oid joko a) handlerParams:sta b) studyprogramme-asetuksista c) yleisesti kaikelle asetettu academyIdentifier
    String toimipisteOID = StringUtils.isNotBlank(handlerParams.getToimipisteOID()) ? handlerParams.getToimipisteOID() : 
      settings.getToimipisteOID(student.getStudyProgramme().getId(), academyIdentifier);

    assessmentsToModel(opiskeluoikeus, student, studentSubjects, laskeKeskiarvot, sisällytäVahvistus, toimipisteOID, defaultStudyProgramme);

    // Aineopiskelija
    
    boolean eiSuorituksia = CollectionUtils.isEmpty(opiskeluoikeus.getSuoritukset());
    
    if (eiSuorituksia) {
      LukionOppiaineenTunniste oppiaineenTunniste = new LukionOppiaineenSuoritusEiTiedossa();
      LukionOppiaineenSuoritus oppiaineenSuoritus = new LukionOppiaineenSuoritus(oppiaineenTunniste);
      
      LukionOppiaineenOppimaaranSuoritus oppiaineenOppimaaranSuoritus = LukionOppiaineenOppimaaranSuoritus.from(
          oppiaineenSuoritus, Kieli.FI, new OrganisaationToimipisteOID(toimipisteOID));
      opiskeluoikeus.addSuoritus(oppiaineenOppimaaranSuoritus);
    }
    
    return new OpiskeluoikeusInternetix(opiskeluoikeus, eiSuorituksia);
  }
  
  private StudentSubjectSelections getDefaultSubjectSelections() {
    StudentSubjectSelections studentSubjects = new StudentSubjectSelections();
    studentSubjects.setMath("MAB");
    studentSubjects.setPrimaryLanguage("ÄI");
    studentSubjects.setReligion("UE");
    return studentSubjects;
  }

  private void assessmentsToModel(LukionOpiskeluoikeus opiskeluoikeus, 
      Student student, StudentSubjectSelections studentSubjects, boolean calculateMeanGrades, boolean sisällytäVahvistus, 
      String toimipisteOID, boolean defaultStudyProgramme) {
    List<OpiskelijanOPS> opsList = new ArrayList<>();
    opsList.add(OpiskelijanOPS.ops2016);
    opsList.add(OpiskelijanOPS.ops2005);
    
    KoskiStudyProgrammeHandlerParams handlerParams = getHandlerParams(HANDLER_TYPE);

    // If this is default study programme of the student, we exclude the incompatible education type (ie list all other edutypes)
    // Otherwise, only list credits from one education type
    Predicate<Credit> predicate = defaultStudyProgramme ?
        credit -> !matchingEducationTypeFilter(credit, handlerParams.getExcludedEducationTypes()) :
        credit -> matchingEducationTypeFilter(credit, handlerParams.getEducationTypes());
    Map<OpiskelijanOPS, List<CreditStub>> opsCredits = listCredits(student, true, true, opsList, OpiskelijanOPS.ops2016, predicate);
    
    Map<String, OppiaineenSuoritusWithCurriculum<LukionOppiaineenSuoritus>> map = new HashMap<>();
    Set<OppiaineenSuoritusWithCurriculum<LukionOppiaineenSuoritus>> accomplished = new HashSet<>();

    for (OpiskelijanOPS ops : opsCredits.keySet()) {
      List<CreditStub> credits = opsCredits.get(ops);
      
      for (CreditStub credit : credits) {
        OppiaineenSuoritusWithCurriculum<LukionOppiaineenSuoritus> oppiaineenSuoritus = getSubject(ops, student, handlerParams.getEducationTypes(), credit.getSubject(), studentSubjects, map);
        collectAccomplishedMarks(credit.getSubject(), oppiaineenSuoritus, studentSubjects, accomplished);
  
        if (settings.isReportedCredit(credit) && oppiaineenSuoritus != null) {
          LukionKurssinSuoritus kurssiSuoritus = createKurssiSuoritus(student, ops, credit);
          if (kurssiSuoritus != null) {
            oppiaineenSuoritus.getOppiaineenSuoritus().addOsasuoritus(kurssiSuoritus);
          } else {
            logger.warning(String.format("Course %s not reported for student %d due to unresolvable credit.", credit.getCourseCode(), student.getId()));
            koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNREPORTED_CREDIT, new Date(), credit.getCourseCode());
          }
        }
      }
    }
    
    List<String> sortedSubjects = getSortedKeys(map);
    for (String subjectCode : sortedSubjects) {
      OppiaineenSuoritusWithCurriculum<LukionOppiaineenSuoritus> lukionOppiaineenSuoritus = map.get(subjectCode);
      
      if (CollectionUtils.isEmpty(lukionOppiaineenSuoritus.getOppiaineenSuoritus().getOsasuoritukset())) {
        // Skip empty subjects
        continue;
      }
      
      StudentSubjectGrade studentSubjectGrade = findStudentSubjectGrade(student, lukionOppiaineenSuoritus.getSubject());
      boolean hasStudentSubjectGrade = studentSubjectGrade != null;
      
      // Valmiille oppiaineelle on rustattava kokonaisarviointi
      if (calculateMeanGrades || hasStudentSubjectGrade) {
        ArviointiasteikkoYleissivistava aineKeskiarvo = accomplished.contains(lukionOppiaineenSuoritus) ? 
            ArviointiasteikkoYleissivistava.GRADE_S : getSubjectMeanGrade(student, lukionOppiaineenSuoritus.getSubject(), lukionOppiaineenSuoritus.getOppiaineenSuoritus());
        
        if (aineKeskiarvo != null) {
          Date arviointiPvm = (studentSubjectGrade != null && studentSubjectGrade.getGradeDate() != null) ? studentSubjectGrade.getGradeDate() : student.getStudyEndDate();
          LukionOppiaineenArviointi arviointi = new LukionOppiaineenArviointi(aineKeskiarvo, arviointiPvm);
          lukionOppiaineenSuoritus.getOppiaineenSuoritus().addArviointi(arviointi);
        } else {
          logger.warning(String.format("Unresolved mean grade for person %d.", student.getPerson().getId()));
        }
      }
      
      LukionOppiaineenSuoritus oppiaineenSuoritus = lukionOppiaineenSuoritus.getOppiaineenSuoritus();
      
      LukionOppiaineenOppimaaranSuoritus oppiaineenOppimaaranSuoritus = LukionOppiaineenOppimaaranSuoritus.from(
          oppiaineenSuoritus, Kieli.FI, new OrganisaationToimipisteOID(toimipisteOID));
      oppiaineenOppimaaranSuoritus.getKoulutusmoduuli().setPerusteenDiaarinumero(getDiaarinumero(HANDLER_TYPE, lukionOppiaineenSuoritus.getOps()));
      oppiaineenOppimaaranSuoritus.setTodistuksellaNakyvatLisatiedot(getTodistuksellaNakyvatLisatiedot(student));

      if (hasStudentSubjectGrade) {
        oppiaineenOppimaaranSuoritus.setVahvistus(getVahvistus(student, studentSubjectGrade.getGradeApprover(), studentSubjectGrade.getGradeDate(), toimipisteOID));
      } else if (sisällytäVahvistus) {
        oppiaineenOppimaaranSuoritus.setVahvistus(getVahvistus(student, toimipisteOID));
      }
      
      opiskeluoikeus.addSuoritus(oppiaineenOppimaaranSuoritus);
    }
  }

  private ArviointiasteikkoYleissivistava getSubjectMeanGrade(Student student, Subject subject, LukionOppiaineenSuoritus oppiaineenSuoritus) {
    // Jos aineesta on annettu korotettu arvosana, käytetään automaattisesti sitä
    ArviointiasteikkoYleissivistava korotettuArvosana = getSubjectGrade(student, subject);
    if (korotettuArvosana != null) {
      return korotettuArvosana;
    } else {
      List<ArviointiasteikkoYleissivistava> kurssiarvosanat = new ArrayList<>();
      for (LukionKurssinSuoritus kurssinSuoritus : oppiaineenSuoritus.getOsasuoritukset()) {
        List<KurssinArviointi> arvioinnit = kurssinSuoritus.getArviointi();
        Set<ArviointiasteikkoYleissivistava> arvosanat = arvioinnit.stream().map(arviointi -> arviointi.getArvosana().getValue()).collect(Collectors.toSet());
        
        kurssiarvosanat.add(ArviointiasteikkoYleissivistava.bestGrade(arvosanat));
      }
      
      return ArviointiasteikkoYleissivistava.meanGrade(kurssiarvosanat);
    }
  }
  
  private boolean isMathSubject(String subjectCode) {
    return StringUtils.equals(subjectCode, "MAA") || StringUtils.equals(subjectCode, "MAB") || StringUtils.equals(subjectCode, "MAY");
  }
  
  private OppiaineenSuoritusWithCurriculum<LukionOppiaineenSuoritus> getSubject(
      OpiskelijanOPS creditOPS, Student student, List<Long> educationTypes, Subject subject, 
      StudentSubjectSelections studentSubjects, Map<String, OppiaineenSuoritusWithCurriculum<LukionOppiaineenSuoritus>> map) {
    String subjectCode = subjectCode(subject);
    String mapKey = String.valueOf(creditOPS) + subjectCode;
    if (map.containsKey(mapKey)) {
      return map.get(mapKey);
    }
    
    boolean matchingEducationType = educationTypes != null && subject.getEducationType() != null && 
        educationTypes.contains(subject.getEducationType().getId());
    
    // MATHEMATICS
    if (matchingEducationType && isMathSubject(subjectCode)) {
      if (StringUtils.equals(subjectCode, "MAY") && isMathSubject(studentSubjects.getMath())) {
        // MAY is mapped to either MAB/MAA unless neither is specified
        subjectCode = studentSubjects.getMath();
        mapKey = String.valueOf(creditOPS) + subjectCode;
        if (map.containsKey(mapKey)) {
          return map.get(mapKey);
        }
      }
      
      if (StringUtils.equals(subjectCode, studentSubjects.getMath())) {
        LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusMatematiikka(
            OppiaineMatematiikka.valueOf(subjectCode), isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.MA));
        return map(map, mapKey, creditOPS, tunniste, subject);
      } else
        return null;
    }
    
    if (matchingEducationType && StringUtils.equals(subjectCode, "ÄI")) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusAidinkieli(
            OppiaineAidinkieliJaKirjallisuus.AI1, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.AI));
        return map(map, mapKey, creditOPS, tunniste, subject);
      } else
        return null;
    }
    if (matchingEducationType && StringUtils.equals(subjectCode, "S2")) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusAidinkieli(
            OppiaineAidinkieliJaKirjallisuus.AI7, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.AI));
        return map(map, mapKey, creditOPS, tunniste, subject);
      } else
        return null;
    }
    
    if (matchingEducationType && studentSubjects.isAdditionalLanguage(subjectCode)) {
      if (subjectCode.length() > 2) {
        String langCode = settings.getSubjectToLanguageMapping(subjectCode.substring(0, 2).toUpperCase());
        Kielivalikoima kieli = Kielivalikoima.valueOf(langCode);
        
        if (kieli != null) {
          KoskiOppiaineetYleissivistava valinta = 
              studentSubjects.isALanguage(subjectCode) ? KoskiOppiaineetYleissivistava.A1 :
                studentSubjects.isA1Language(subjectCode) ? KoskiOppiaineetYleissivistava.A1 :
                  studentSubjects.isA2Language(subjectCode) ? KoskiOppiaineetYleissivistava.A2 :
                    studentSubjects.isB1Language(subjectCode) ? KoskiOppiaineetYleissivistava.B1 :
                      studentSubjects.isB2Language(subjectCode) ? KoskiOppiaineetYleissivistava.B2 :
                        studentSubjects.isB3Language(subjectCode) ? KoskiOppiaineetYleissivistava.B3 : null;
          
          LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusVierasKieli(valinta, kieli, 
              isPakollinenOppiaine(student, valinta));
          return map(map, mapKey, creditOPS, tunniste, subject);
        } else {
          logger.log(Level.SEVERE, String.format("Koski: Language code %s could not be converted to an enum.", langCode));
          koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNKNOWN_LANGUAGE, new Date(), langCode);
          return null;
        }
      }
    }

    String[] religionSubjects = new String[] { "UE", "UO" };
    
    if (matchingEducationType && ArrayUtils.contains(religionSubjects, subjectCode)) {
      // Only the religion that student has selected is reported
      if (StringUtils.equals(subjectCode, studentSubjects.getReligion())) {
        mapKey = String.valueOf(creditOPS) + "KT";
        if (map.containsKey(mapKey))
          return map.get(mapKey);
        
        KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.KT;
        LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusMuuValtakunnallinen(kansallinenAine, isPakollinenOppiaine(student, kansallinenAine));
        return map(map, mapKey, creditOPS, tunniste, subject);
      } else
        return null;
    }
    
    if (matchingEducationType && EnumUtils.isValidEnum(KoskiOppiaineetYleissivistava.class, StringUtils.upperCase(subjectCode))) {
      // Common national subject
      
      KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.valueOf(StringUtils.upperCase(subjectCode));
      LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusMuuValtakunnallinen(kansallinenAine, isPakollinenOppiaine(student, kansallinenAine));
      return map(map, mapKey, creditOPS, tunniste, subject);
    } else {
      // Other local subject
      
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(subjectCode, kuvaus(subject.getName()));
      LukionOppiaineenSuoritusPaikallinen tunniste = new LukionOppiaineenSuoritusPaikallinen(paikallinenKoodi, false, kuvaus(subject.getName()));
      return map(map, mapKey, creditOPS, tunniste, subject);
    }
  }

  private OppiaineenSuoritusWithCurriculum<LukionOppiaineenSuoritus> map(
      Map<String, OppiaineenSuoritusWithCurriculum<LukionOppiaineenSuoritus>> map, String mapKey,
      OpiskelijanOPS creditOPS, LukionOppiaineenTunniste tunniste, Subject subject) {
    OppiaineenSuoritusWithCurriculum<LukionOppiaineenSuoritus> oswc = 
        new OppiaineenSuoritusWithCurriculum<LukionOppiaineenSuoritus>(subject, creditOPS, new LukionOppiaineenSuoritus(tunniste));
    map.put(mapKey, oswc);
    return oswc;
  }

  protected LukionKurssinSuoritus createKurssiSuoritus(Student student, OpiskelijanOPS ops, CreditStub courseCredit) {
    String kurssiKoodi = courseCredit.getCourseCode();
    LukionKurssinTunniste tunniste;
    
    if (ops == OpiskelijanOPS.ops2016 && EnumUtils.isValidEnum(LukionKurssit.class, kurssiKoodi)) {
      // OPS 2016 (2015)
      LukionKurssit kurssi = LukionKurssit.valueOf(kurssiKoodi);
      LukionKurssinTyyppi kurssinTyyppi = findCourseType(student, courseCredit, true, LukionKurssinTyyppi.pakollinen, LukionKurssinTyyppi.syventava);
      tunniste = new LukionKurssinTunnisteValtakunnallinenOPS2015(kurssi, kurssinTyyppi);
    } else if (ops == OpiskelijanOPS.ops2005 && EnumUtils.isValidEnum(LukionKurssitOPS2004Aikuiset.class, kurssiKoodi)) {
      // OPS 2005 (2004)
      LukionKurssitOPS2004Aikuiset kurssi = LukionKurssitOPS2004Aikuiset.valueOf(kurssiKoodi);
      LukionKurssinTyyppi kurssinTyyppi = findCourseType(student, courseCredit, true, LukionKurssinTyyppi.pakollinen, LukionKurssinTyyppi.syventava);
      tunniste = new LukionKurssinTunnisteValtakunnallinenOPS2004(kurssi, kurssinTyyppi);
    } else {
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(kurssiKoodi, kuvaus(courseCredit.getCourseName()));
      LukionKurssinTyyppi kurssinTyyppi = findCourseType(student, courseCredit, true, LukionKurssinTyyppi.syventava, LukionKurssinTyyppi.soveltava);
      tunniste = new LukionKurssinTunnistePaikallinen(paikallinenKoodi , kurssinTyyppi, kuvaus(courseCredit.getCourseName()));
    }
      
    LukionKurssinSuoritus suoritus = new LukionKurssinSuoritus(tunniste);
    
    return luoKurssiSuoritus(suoritus, courseCredit);
  }

  private LukionKurssinTyyppi findCourseType(Student student, CreditStub courseCredit, boolean national, LukionKurssinTyyppi ... allowedValues) {
    Set<LukionKurssinTyyppi> resolvedTypes = new HashSet<>();
    
    for (CreditStubCredit credit : courseCredit.getCredits()) {
      if (credit.getCredit() instanceof CourseAssessment) {
        CourseAssessment courseAssessment = (CourseAssessment) credit.getCredit();
        if (courseAssessment.getCourseStudent() != null && courseAssessment.getCourseStudent().getCourse() != null) {
          Course course = courseAssessment.getCourseStudent().getCourse();
          Set<Long> educationSubTypeIds = course.getCourseEducationTypes().stream().flatMap(
              educationType -> educationType.getCourseEducationSubtypes().stream().map(subType -> subType.getEducationSubtype().getId())).collect(Collectors.toSet());
          for (Long educationSubTypeId : educationSubTypeIds) {
            String mappedValue = settings.getCourseTypeMapping(educationSubTypeId);
            if (mappedValue != null && EnumUtils.isValidEnum(LukionKurssinTyyppi.class, mappedValue)) {
              resolvedTypes.add(LukionKurssinTyyppi.valueOf(mappedValue));
            }
          }
        } else
          logger.warning(String.format("CourseAssessment %d has no courseStudent or Course", courseAssessment.getId()));
      } else if (credit.getCredit() instanceof TransferCredit) {
        TransferCredit transferCredit = (TransferCredit) credit.getCredit();
        if (national && transferCredit.getOptionality() == CourseOptionality.MANDATORY) {
          resolvedTypes.add(LukionKurssinTyyppi.pakollinen);
        } else {
          resolvedTypes.add(LukionKurssinTyyppi.syventava);
        }
      } else {
        logger.warning(String.format("Unknown credit type %s", credit.getClass().getSimpleName()));
      }
    }
    
    Set<LukionKurssinTyyppi> allowedSet = new HashSet<>(Arrays.asList(allowedValues));
    allowedSet.removeIf(element -> !resolvedTypes.contains(element));
    
    if (allowedSet.size() == 0) {
      logger.warning(String.format("Course %s has no feasible subtypes.", courseCredit.getCourseCode()));
      koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNRESOLVABLE_SUBTYPES, new Date(), courseCredit.getCourseCode());
      return allowedValues[0];
    } else if (allowedSet.size() == 1) {
      return allowedSet.iterator().next();
    } else {
      for (LukionKurssinTyyppi type : allowedValues) {
        if (allowedSet.contains(type)) {
          logger.warning(String.format("Course %s has several matching subtypes.", courseCredit.getCourseCode()));
          koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNRESOLVABLE_SUBTYPES, new Date(), courseCredit.getCourseCode());
          return type;
        }
      }
    }
    
    return allowedValues[0];
  }

  @Override
  public void saveOrValidateOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    saveOrValidateInternetixOid(handler, student, oid);
  }
  
  @Override
  public void removeOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    removeInternetixOid(handler, student, oid);
  }
  
  @Override
  public Set<KoskiStudentId> listOids(Student student) {
    return loadInternetixOids(student);
  }

}
