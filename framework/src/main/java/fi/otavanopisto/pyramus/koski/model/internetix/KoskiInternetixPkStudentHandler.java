package fi.otavanopisto.pyramus.koski.model.internetix;

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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod;
import fi.otavanopisto.pyramus.koski.CreditStub;
import fi.otavanopisto.pyramus.koski.CreditStubCredit;
import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.KoskiStudentHandler;
import fi.otavanopisto.pyramus.koski.KoskiStudentId;
import fi.otavanopisto.pyramus.koski.KoskiStudyProgrammeHandler;
import fi.otavanopisto.pyramus.koski.OpiskelijanOPS;
import fi.otavanopisto.pyramus.koski.OppiaineenSuoritusWithCurriculum;
import fi.otavanopisto.pyramus.koski.StudentSubjectSelections;
import fi.otavanopisto.pyramus.koski.CreditStubCredit.Type;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenKurssit2015;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenPaattovaiheenKurssit2017;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiNumeerinen;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiSanallinen;
import fi.otavanopisto.pyramus.koski.model.Majoitusjakso;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusJakso;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.OsaamisenTunnustaminen;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinSuoritus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunniste;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnisteOPS2015;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnistePV2017;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnistePaikallinen;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusMuu;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.PerusopetuksenOppiaineenOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.settings.KoskiStudyProgrammeHandlerParams;

/**
 * Käsittelee Internetix-/aineopiskelijan peruskoulukurssien osuuden.
 */
public class KoskiInternetixPkStudentHandler extends KoskiStudentHandler {

  private static final KoskiStudyProgrammeHandler HANDLER_TYPE = KoskiStudyProgrammeHandler.aineopiskeluperusopetus;
  
  @Inject
  private Logger logger;

  public Opiskeluoikeus studentToModel(Student student, String academyIdentifier) {
    StudentSubjectSelections studentSubjects = loadStudentSubjectSelections(student, getDefaultSubjectSelections());
    String studyOid = resolveInternetixOid(student, HANDLER_TYPE);

    // Skip student if it is archived and the studyoid is blank
    if (Boolean.TRUE.equals(student.getArchived()) && StringUtils.isBlank(studyOid)) {
      return null;
    }
    
    boolean defaultStudyProgramme = 
        settings.getStudyProgrammeHandlerType(student.getStudyProgramme().getId()) == HANDLER_TYPE;

    AikuistenPerusopetuksenOpiskeluoikeus opiskeluoikeus = new AikuistenPerusopetuksenOpiskeluoikeus();
    opiskeluoikeus.setLahdejarjestelmanId(getLahdeJarjestelmaID(HANDLER_TYPE, student.getId()));
    opiskeluoikeus.setAlkamispaiva(student.getStudyStartDate());
    opiskeluoikeus.setPaattymispaiva(student.getStudyEndDate());
    if (StringUtils.isNotBlank(studyOid)) {
      opiskeluoikeus.setOid(studyOid);
    }

    opiskeluoikeus.setLisatiedot(getLisatiedot(student));
    
    if (!handleLinkedStudyOID(student, opiskeluoikeus)) {
      koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.LINKED_MISSING_VALUES, new Date());
      return null;
    }
    
    OpiskeluoikeudenTila jaksonTila = !Boolean.TRUE.equals(student.getArchived()) ? OpiskeluoikeudenTila.lasna : OpiskeluoikeudenTila.mitatoity;
    OpiskeluoikeusJakso jakso = new OpiskeluoikeusJakso(student.getStudyStartDate(), jaksonTila);
    jakso.setOpintojenRahoitus(new KoodistoViite<>(student.getSchool() == null ? OpintojenRahoitus.K1 : OpintojenRahoitus.K6));
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(jakso);

    SuorituksenTila suorituksenTila = SuorituksenTila.KESKEN;

    if (student.getStudyEndDate() != null) {
      OpiskeluoikeudenTila opintojenLopetusTila = settings.getStudentState(student, OpiskeluoikeudenTila.eronnut);
      opiskeluoikeus.getTila().addOpiskeluoikeusJakso(
          new OpiskeluoikeusJakso(student.getStudyEndDate(), opintojenLopetusTila));

      suorituksenTila = ArrayUtils.contains(OpiskeluoikeudenTila.GRADUATED_STATES, opintojenLopetusTila) ? 
          SuorituksenTila.VALMIS : SuorituksenTila.KESKEYTYNYT;
    }

    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(academyIdentifier);
    Set<OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus>> oppiaineet = assessmentsToModel(student, studentSubjects, suorituksenTila == SuorituksenTila.VALMIS, defaultStudyProgramme);

    // Aineopiskelija

    for (OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus> oppiaine : oppiaineet) {
      PerusopetuksenOppiaineenOppimaaranSuoritus oppiaineenOppimaaranSuoritus = PerusopetuksenOppiaineenOppimaaranSuoritus.from(
          oppiaine.getOppiaineenSuoritus(), PerusopetuksenSuoritusTapa.koulutus, Kieli.FI, toimipiste);
      oppiaineenOppimaaranSuoritus.setTodistuksellaNakyvatLisatiedot(getTodistuksellaNakyvatLisatiedot(student));
      oppiaineenOppimaaranSuoritus.getKoulutusmoduuli().setPerusteenDiaarinumero(getDiaarinumero(HANDLER_TYPE, oppiaine.getOps()));
      if (suorituksenTila == SuorituksenTila.VALMIS)
        oppiaineenOppimaaranSuoritus.setVahvistus(getVahvistus(student, academyIdentifier));
      opiskeluoikeus.addSuoritus(oppiaineenOppimaaranSuoritus);
    }
    
    if (CollectionUtils.isEmpty(opiskeluoikeus.getSuoritukset())) {
      koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.NO_RESOLVABLE_SUBJECTS, new Date());
      return null;
    }
    
    return opiskeluoikeus;
  }
  
  private AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot getLisatiedot(Student student) {
    AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot lisatiedot = new AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot();
    
    lisatiedot.setVuosiluokkiinSitoutumatonOpetus(true);
    
    List<StudentLodgingPeriod> lodgingPeriods = lodgingPeriodDAO.listByStudent(student);
    for (StudentLodgingPeriod lodgingPeriod : lodgingPeriods) {
      lisatiedot.addSisaoppilaitosmainenMajoitus(new Majoitusjakso(lodgingPeriod.getBegin(), lodgingPeriod.getEnd()));
    }
    
    if (!lodgingPeriods.isEmpty()) {
      Date minBeginDate = lodgingPeriods.stream().map(StudentLodgingPeriod::getBegin).filter(Objects::nonNull)
          .min(Comparator.nullsLast(Date::compareTo)).orElse(null);
      
      if (minBeginDate != null) {
        Date maxEndDate = lodgingPeriods.stream().map(StudentLodgingPeriod::getEnd).filter(Objects::nonNull)
            .max(Comparator.nullsLast(Date::compareTo)).orElse(null);
        
        lisatiedot.setOikeusMaksuttomaanAsuntolapaikkaan(new Majoitusjakso(minBeginDate, maxEndDate));
      }
    }
    
    return lisatiedot;
  }

  private StudentSubjectSelections getDefaultSubjectSelections() {
    StudentSubjectSelections studentSubjects = new StudentSubjectSelections();
    studentSubjects.setPrimaryLanguage("äi");
    studentSubjects.setReligion("ue");
    return studentSubjects;
  }
  
  private Set<OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus>> assessmentsToModel(
      Student student, StudentSubjectSelections studentSubjects, 
      boolean calculateMeanGrades, boolean defaultStudyProgramme) {
    List<OpiskelijanOPS> opsList = new ArrayList<>();
    opsList.add(OpiskelijanOPS.ops2018);
    opsList.add(OpiskelijanOPS.ops2016);
    opsList.add(OpiskelijanOPS.ops2005);
    
    KoskiStudyProgrammeHandlerParams handlerParams = settings.getSettings().getKoski().getHandlerParams().get(HANDLER_TYPE);
    
    if (handlerParams == null) {
      throw new RuntimeException("HandlerParams not set for InternetixPk");
    }
    
    // If this is default study programme of the student, we exclude the incompatible education type (ie list all other edutypes)
    // Otherwise, only list credits from one education type
    Predicate<Credit> predicate = defaultStudyProgramme ?
        credit -> !matchingEducationTypeFilter(credit, handlerParams.getExcludedEducationTypes()) :
        credit -> matchingEducationTypeFilter(credit, handlerParams.getEducationTypes());
    Map<OpiskelijanOPS, List<CreditStub>> opsCredits = listCredits(student, true, true, opsList, OpiskelijanOPS.ops2018, predicate);

    Map<String, OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus>> map = new HashMap<>();
    Set<OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus>> accomplished = new HashSet<>();
    
    for (OpiskelijanOPS ops : opsCredits.keySet()) {
      List<CreditStub> credits = opsCredits.get(ops);
      
      for (CreditStub credit : credits) {
        OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus> oppiaineenSuoritus = getSubject(
            ops, student, handlerParams.getEducationTypes(), studentSubjects, credit.getSubject(), map);
        collectAccomplishedMarks(credit.getSubject(), oppiaineenSuoritus, studentSubjects, accomplished);
  
        if (settings.isReportedCredit(credit) && oppiaineenSuoritus != null) {
          AikuistenPerusopetuksenKurssinSuoritus kurssiSuoritus = createKurssiSuoritus(ops, credit);
          if (kurssiSuoritus != null) {
            oppiaineenSuoritus.getOppiaineenSuoritus().addOsasuoritus(kurssiSuoritus);
          } else {
            logger.warning(String.format("Course %s not reported for student %d due to unresolvable credit.", credit.getCourseCode(), student.getId()));
            koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNREPORTED_CREDIT, new Date(), credit.getCourseCode());
          }
        }
      }
    }
    
    Set<OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus>> results = new HashSet<>();

    for (OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus> oppiaineenSuoritus : map.values()) {
      if (CollectionUtils.isEmpty(oppiaineenSuoritus.getOppiaineenSuoritus().getOsasuoritukset())) {
        // Skip empty subjects
        continue;
      }
      
      // Valmiille oppiaineelle on rustattava kokonaisarviointi
      if (calculateMeanGrades) {
        ArviointiasteikkoYleissivistava aineKeskiarvo = accomplished.contains(oppiaineenSuoritus) ? 
            ArviointiasteikkoYleissivistava.GRADE_S : getSubjectMeanGrade(oppiaineenSuoritus.getOppiaineenSuoritus());
        
        if (ArviointiasteikkoYleissivistava.isNumeric(aineKeskiarvo)) {
          KurssinArviointi arviointi = new KurssinArviointiNumeerinen(aineKeskiarvo, student.getStudyEndDate());
          oppiaineenSuoritus.getOppiaineenSuoritus().addArviointi(arviointi);
        } else if (ArviointiasteikkoYleissivistava.isLiteral(aineKeskiarvo)) {
          KurssinArviointi arviointi = new KurssinArviointiSanallinen(aineKeskiarvo, student.getStudyEndDate(), kuvaus("Suoritettu/Hylätty"));
          oppiaineenSuoritus.getOppiaineenSuoritus().addArviointi(arviointi);
        }
      }
      
      results.add(oppiaineenSuoritus);
    }
    
    return results;
  }

  private OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus> map(
      Map<String, OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus>> map, String mapKey, 
      OpiskelijanOPS creditOPS, AikuistenPerusopetuksenOppiaineenTunniste tunniste) {
    OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus> oswc = 
        new OppiaineenSuoritusWithCurriculum<>(creditOPS, new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste));
    map.put(mapKey, oswc);
    return oswc;
  }
  
  private OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus> getSubject(
      OpiskelijanOPS creditOPS, Student student, List<Long> educationTypes, 
      StudentSubjectSelections studentSubjects, Subject subject, 
      Map<String, OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus>> map) {
    String subjectCode = subjectCode(subject);

    String mapKey = String.valueOf(creditOPS) + subjectCode;
    if (map.containsKey(mapKey))
      return map.get(mapKey);
    
    boolean matchingEducationType = educationTypes != null && subject.getEducationType() != null && 
        educationTypes.contains(subject.getEducationType().getId());
    
    if (matchingEducationType && (StringUtils.equals(subjectCode, "äi") || StringUtils.equals(subjectCode, "s2"))) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        OppiaineAidinkieliJaKirjallisuus aine = StringUtils.equals(subjectCode, "s2") ? 
            OppiaineAidinkieliJaKirjallisuus.AI7 :  // s2
              OppiaineAidinkieliJaKirjallisuus.AI1; // äi
        
        AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli(
            aine, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.AI));
        return map(map, mapKey, creditOPS, tunniste);
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
          
          AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli(
              valinta, kieli, isPakollinenOppiaine(student, valinta));
          return map(map, mapKey, creditOPS, tunniste);
        } else {
          logger.log(Level.SEVERE, String.format("Koski: Language code %s could not be converted to an enum.", langCode));
          koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNKNOWN_LANGUAGE, new Date(), langCode);
          return null;
        }
      }
    }

    String[] religionSubjects = new String[] { "ue", "uo" };
    
    if (matchingEducationType && ArrayUtils.contains(religionSubjects, subjectCode)) {
      // Only the religion that student has selected is reported
      if (StringUtils.equals(subjectCode, studentSubjects.getReligion())) {
        mapKey = String.valueOf(creditOPS) + "KT";
        if (map.containsKey(mapKey))
          return map.get(mapKey);
        
        KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.KT;
        AikuistenPerusopetuksenOppiaineenTunniste tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusMuu(
            kansallinenAine, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.KT));
        return map(map, mapKey, creditOPS, tunniste);
      } else
        return null;
    }
    
    if (matchingEducationType && EnumUtils.isValidEnum(KoskiOppiaineetYleissivistava.class, StringUtils.upperCase(subjectCode))) {
      // Common national subject
      
      KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.valueOf(StringUtils.upperCase(subjectCode));
      AikuistenPerusopetuksenOppiaineenTunniste tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusMuu(
          kansallinenAine, isPakollinenOppiaine(student, kansallinenAine));
      return map(map, mapKey, creditOPS, tunniste);
    } else {
      // Other local subject
      // TODO Skipped subjects ?? (MUU)
      
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(subjectCode, kuvaus(subject.getName()));
      AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen(
          paikallinenKoodi, false, kuvaus(subject.getName()));
      return map(map, mapKey, creditOPS, tunniste);
    }
  }

  protected AikuistenPerusopetuksenKurssinSuoritus createKurssiSuoritus(OpiskelijanOPS ops, CreditStub courseCredit) {
    String kurssiKoodi = StringUtils.upperCase(courseCredit.getCourseCode());
    AikuistenPerusopetuksenKurssinTunniste tunniste;
    
    if (ops == OpiskelijanOPS.ops2016 && EnumUtils.isValidEnum(AikuistenPerusopetuksenKurssit2015.class, kurssiKoodi)) {
      AikuistenPerusopetuksenKurssit2015 kurssi = AikuistenPerusopetuksenKurssit2015.valueOf(kurssiKoodi);
      tunniste = new AikuistenPerusopetuksenKurssinTunnisteOPS2015(kurssi);
    } else if (ops == OpiskelijanOPS.ops2018 && EnumUtils.isValidEnum(AikuistenPerusopetuksenPaattovaiheenKurssit2017.class, kurssiKoodi)) {
      AikuistenPerusopetuksenPaattovaiheenKurssit2017 kurssi = AikuistenPerusopetuksenPaattovaiheenKurssit2017.valueOf(kurssiKoodi);
      tunniste = new AikuistenPerusopetuksenKurssinTunnistePV2017(kurssi);
    } else {
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(kurssiKoodi, kuvaus(courseCredit.getSubject().getName()));
      tunniste = new AikuistenPerusopetuksenKurssinTunnistePaikallinen(paikallinenKoodi);
    }
      
    AikuistenPerusopetuksenKurssinSuoritus suoritus = new AikuistenPerusopetuksenKurssinSuoritus(tunniste);

    // Hyväksilukutieto on hölmössä paikassa; jos kaikki arvosanat ovat hyväksilukuja, tallennetaan 
    // tieto hyväksilukuna - ongelmallista, jos hyväksiluettua kurssia on korotettu 
    if (courseCredit.getCredits().stream().allMatch(credit -> credit.getType() == Type.RECOGNIZED)) {
      OsaamisenTunnustaminen tunnustettu = new OsaamisenTunnustaminen(kuvaus("Hyväksiluku"));
      suoritus.setTunnustettu(tunnustettu);
    }

    for (CreditStubCredit credit : courseCredit.getCredits()) {
      ArviointiasteikkoYleissivistava arvosana = getArvosana(credit.getGrade());

      KurssinArviointi arviointi = null;
      if (ArviointiasteikkoYleissivistava.isNumeric(arvosana)) {
        arviointi =  new KurssinArviointiNumeerinen(arvosana, credit.getDate());
      } else if (ArviointiasteikkoYleissivistava.isLiteral(arvosana)) {
        arviointi = new KurssinArviointiSanallinen(arvosana, credit.getDate(), kuvaus(credit.getGrade().getName()));
      }
      
      if (arviointi != null) {
        suoritus.addArviointi(arviointi);
      }
    }
    
    // Don't report the course if there's no credits
    if (CollectionUtils.isEmpty(suoritus.getArviointi())) {
      return null;
    }
    
    return suoritus;
  }

  private ArviointiasteikkoYleissivistava getSubjectMeanGrade(AikuistenPerusopetuksenOppiaineenSuoritus oppiaineenSuoritus) {
    List<ArviointiasteikkoYleissivistava> kurssiarvosanat = new ArrayList<>();
    for (AikuistenPerusopetuksenKurssinSuoritus kurssinSuoritus : oppiaineenSuoritus.getOsasuoritukset()) {
      Set<KurssinArviointi> arvioinnit = kurssinSuoritus.getArviointi();
      Set<ArviointiasteikkoYleissivistava> arvosanat = arvioinnit.stream().map(arviointi -> arviointi.getArvosana().getValue()).collect(Collectors.toSet());
      
      kurssiarvosanat.add(ArviointiasteikkoYleissivistava.bestGrade(arvosanat));
    }
    
    return ArviointiasteikkoYleissivistava.meanGrade(kurssiarvosanat);
  }

  @Override
  public void saveOrValidateOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    saveOrValidateInternetixOid(handler, student, oid);
  }
  
  @Override
  public Set<KoskiStudentId> listOids(Student student) {
    return loadInternetixOids(student);
  }

}