package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod;
import fi.otavanopisto.pyramus.koski.CreditStub;
import fi.otavanopisto.pyramus.koski.CreditStubCredit;
import fi.otavanopisto.pyramus.koski.CreditStubCredit.Type;
import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.KoskiConsts;
import fi.otavanopisto.pyramus.koski.KoskiStudentHandler;
import fi.otavanopisto.pyramus.koski.KoskiStudentId;
import fi.otavanopisto.pyramus.koski.KoskiStudyProgrammeHandler;
import fi.otavanopisto.pyramus.koski.OpiskelijanOPS;
import fi.otavanopisto.pyramus.koski.OppiaineenSuoritusWithSubject;
import fi.otavanopisto.pyramus.koski.StudentSubjectSelections;
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

public class KoskiAikuistenPerusopetuksenStudentHandler extends KoskiStudentHandler {

  private static final KoskiStudyProgrammeHandler HANDLER_TYPE = KoskiStudyProgrammeHandler.aikuistenperusopetus;
  
  @Inject
  private Logger logger;

  public Opiskeluoikeus studentToModel(Student student, String academyIdentifier, KoskiStudyProgrammeHandler handler) {
    if (handler != HANDLER_TYPE) {
      logger.log(Level.SEVERE, String.format("Wrong handler type %s expected %s w/person %d.", handler, HANDLER_TYPE, student.getPerson().getId()));
      return null;
    }
    
    StudentSubjectSelections studentSubjects = loadStudentSubjectSelections(student, getDefaultSubjectSelections());
    String studyOid = userVariableDAO.findByUserAndKey(student, KOSKI_STUDYPERMISSION_ID);

    // Skip student if it is archived and the studyoid is blank
    if (Boolean.TRUE.equals(student.getArchived()) && StringUtils.isBlank(studyOid)) {
      return null;
    }
    
    OpiskelijanOPS ops = resolveOPS(student);
    if (ops == null) {
      koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.NO_CURRICULUM, new Date());
      return null;
    }
    
    AikuistenPerusopetuksenOpiskeluoikeus opiskeluoikeus = new AikuistenPerusopetuksenOpiskeluoikeus();
    opiskeluoikeus.setLahdejarjestelmanId(getLahdeJarjestelmaID(handler, student.getId()));
    opiskeluoikeus.setAlkamispaiva(student.getStudyStartDate());
    opiskeluoikeus.setPaattymispaiva(student.getStudyEndDate());
    if (StringUtils.isNotBlank(studyOid)) {
      opiskeluoikeus.setOid(studyOid);
    }

    opiskeluoikeus.setLisatiedot(getLisatiedot(student));
    
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

    String departmentIdentifier = settings.getToimipisteOID(student.getStudyProgramme().getId(), academyIdentifier);

    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(departmentIdentifier);
    EducationType studentEducationType = student.getStudyProgramme() != null && student.getStudyProgramme().getCategory() != null ? 
        student.getStudyProgramme().getCategory().getEducationType() : null;
    Set<AikuistenPerusopetuksenOppiaineenSuoritus> oppiaineet = assessmentsToModel(ops, student, studentEducationType, studentSubjects, suorituksenTila == SuorituksenTila.VALMIS);

    AikuistenPerusopetuksenOppimaaranSuoritus suoritus = new AikuistenPerusopetuksenOppimaaranSuoritus(
        PerusopetuksenSuoritusTapa.koulutus, Kieli.FI, toimipiste);
    suoritus.setTodistuksellaNakyvatLisatiedot(getTodistuksellaNakyvatLisatiedot(student));
    suoritus.getKoulutusmoduuli().setPerusteenDiaarinumero(getDiaarinumero(student));
    if (suorituksenTila == SuorituksenTila.VALMIS)
      suoritus.setVahvistus(getVahvistus(student, departmentIdentifier));
    opiskeluoikeus.addSuoritus(suoritus);
    
    oppiaineet.forEach(oppiaine -> suoritus.addOsasuoritus(oppiaine));
    
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
  
  private Set<AikuistenPerusopetuksenOppiaineenSuoritus> assessmentsToModel(OpiskelijanOPS ops, Student student, EducationType studentEducationType, StudentSubjectSelections studentSubjects, boolean calculateMeanGrades) {
    Collection<CreditStub> credits = listCredits(student, true, true, ops, credit -> matchingCurriculumFilter(student, credit));
    Set<AikuistenPerusopetuksenOppiaineenSuoritus> results = new HashSet<>();
    
    Map<String, OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus>> map = new HashMap<>();
    Set<AikuistenPerusopetuksenOppiaineenSuoritus> accomplished = new HashSet<>();
    
    for (CreditStub credit : credits) {
      OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus> oppiaineenSuoritusWSubject = getSubject(student, studentEducationType, studentSubjects, credit.getSubject(), map);
      AikuistenPerusopetuksenOppiaineenSuoritus oppiaineenSuoritus = oppiaineenSuoritusWSubject.getOppiaineenSuoritus();
      collectAccomplishedMarks(credit.getSubject(), oppiaineenSuoritus, studentSubjects, accomplished);

      if (settings.isReportedCredit(credit) && oppiaineenSuoritus != null) {
        AikuistenPerusopetuksenKurssinSuoritus kurssiSuoritus = createKurssiSuoritus(ops, credit);
        if (kurssiSuoritus != null) {
          oppiaineenSuoritus.addOsasuoritus(kurssiSuoritus);
        } else {
          logger.warning(String.format("Course %s not reported for student %d due to unresolvable credit.", credit.getCourseCode(), student.getId()));
          koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNREPORTED_CREDIT, new Date(), credit.getCourseCode());
        }
      }
    }
    
    for (OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus> oppiaineenSuoritusWSubject : map.values()) {
      AikuistenPerusopetuksenOppiaineenSuoritus oppiaineenSuoritus = oppiaineenSuoritusWSubject.getOppiaineenSuoritus();
      if (CollectionUtils.isEmpty(oppiaineenSuoritus.getOsasuoritukset())) {
        // Skip empty subjects
        continue;
      }
      
      // Valmiille oppiaineelle on rustattava kokonaisarviointi
      if (calculateMeanGrades) {
        ArviointiasteikkoYleissivistava aineKeskiarvo = accomplished.contains(oppiaineenSuoritus) ? 
            ArviointiasteikkoYleissivistava.GRADE_S : getSubjectMeanGrade(student, oppiaineenSuoritusWSubject.getSubject(), oppiaineenSuoritus);
        
        if (ArviointiasteikkoYleissivistava.isNumeric(aineKeskiarvo)) {
          KurssinArviointi arviointi = new KurssinArviointiNumeerinen(aineKeskiarvo, student.getStudyEndDate());
          oppiaineenSuoritus.addArviointi(arviointi);
        } else if (ArviointiasteikkoYleissivistava.isLiteral(aineKeskiarvo)) {
          KurssinArviointi arviointi = new KurssinArviointiSanallinen(aineKeskiarvo, student.getStudyEndDate(), kuvaus("Suoritettu/Hylätty"));
          oppiaineenSuoritus.addArviointi(arviointi);
        }
      }
      
      results.add(oppiaineenSuoritus);
    }
    
    return results;
  }

  private OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus> getSubject(Student student, EducationType studentEducationType, 
      StudentSubjectSelections studentSubjects, Subject subject, Map<String, OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus>> map) {
    String subjectCode = subjectCode(subject);

    // ot1, ot2 etc have subject OPA
    if (StringUtils.equals(subjectCode, "ot")) {
      subjectCode = "OPA";
    }
    
    // SubjectCode for religious subjects is different
    if (StringUtils.equals(subjectCode, "ue") || StringUtils.equals(subjectCode, "uo") || StringUtils.equals(subjectCode, "et"))
      subjectCode = "KT";

    if (map.containsKey(subjectCode))
      return map.get(subjectCode);
    
    boolean matchingEducationType = studentEducationType != null && subject.getEducationType() != null && 
        studentEducationType.getId().equals(subject.getEducationType().getId());
    
    if (matchingEducationType && (StringUtils.equals(subjectCode, "äi") || StringUtils.equals(subjectCode, "s2"))) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        OppiaineAidinkieliJaKirjallisuus aine = StringUtils.equals(subjectCode, "s2") ? 
            OppiaineAidinkieliJaKirjallisuus.AI7 :  // s2
              OppiaineAidinkieliJaKirjallisuus.AI1; // äi
        
        AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli(
            aine, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.AI));
        return mapSubject(subject, subjectCode, tunniste, map);
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
          return mapSubject(subject, subjectCode, tunniste, map);
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
        if (map.containsKey("KT"))
          return map.get("KT");
        
        KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.KT;
        AikuistenPerusopetuksenOppiaineenTunniste tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusMuu(
            kansallinenAine, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.KT));
        return mapSubject(subject, "KT", tunniste, map);
      } else
        return null;
    }
    
    if (matchingEducationType && EnumUtils.isValidEnum(KoskiOppiaineetYleissivistava.class, StringUtils.upperCase(subjectCode))) {
      // Common national subject
      
      KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.valueOf(StringUtils.upperCase(subjectCode));
      AikuistenPerusopetuksenOppiaineenTunniste tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusMuu(
          kansallinenAine, isPakollinenOppiaine(student, kansallinenAine));
      return mapSubject(subject, subjectCode, tunniste, map);
    } else {
      // Other local subject
      // TODO Skipped subjects ?? (MUU)
      
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(subjectCode, kuvaus(subject.getName()));
      AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen(
          paikallinenKoodi, false, kuvaus(subject.getName()));
      return mapSubject(subject, subjectCode, tunniste, map);
    }
  }

  private OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus> mapSubject(
      Subject subject, String subjectCode, AikuistenPerusopetuksenOppiaineenTunniste tunniste,
      Map<String, OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus>> map) {
    OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus> os = new OppiaineenSuoritusWithSubject<>(subject, new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste));
    map.put(subjectCode, os);
    return os;
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

  private ArviointiasteikkoYleissivistava getSubjectMeanGrade(Student student, Subject subject, AikuistenPerusopetuksenOppiaineenSuoritus oppiaineenSuoritus) {
    // Jos aineesta on annettu korotettu arvosana, käytetään automaattisesti sitä
    ArviointiasteikkoYleissivistava korotettuArvosana = getSubjectGrade(student, subject);
    if (korotettuArvosana != null) {
      return korotettuArvosana;
    } else {
      List<ArviointiasteikkoYleissivistava> kurssiarvosanat = new ArrayList<>();
      for (AikuistenPerusopetuksenKurssinSuoritus kurssinSuoritus : oppiaineenSuoritus.getOsasuoritukset()) {
        List<KurssinArviointi> arvioinnit = kurssinSuoritus.getArviointi();
        Set<ArviointiasteikkoYleissivistava> arvosanat = arvioinnit.stream().map(arviointi -> arviointi.getArvosana().getValue()).collect(Collectors.toSet());
        
        kurssiarvosanat.add(ArviointiasteikkoYleissivistava.bestGrade(arvosanat));
      }
      
      return ArviointiasteikkoYleissivistava.meanGrade(kurssiarvosanat);
    }
  }

  @Override
  public void saveOrValidateOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    if (handler == HANDLER_TYPE) {
      saveOrValidateOid(student, oid);
    } else {
      logger.severe(String.format("saveOrValidateOid called with wrong handler %s, expected %s ", handler, HANDLER_TYPE));
    }
  }

  @Override
  public Set<KoskiStudentId> listOids(Student student) {
    String oid = userVariableDAO.findByUserAndKey(student, KoskiConsts.VariableNames.KOSKI_STUDYPERMISSION_ID);
    if (StringUtils.isNotBlank(oid)) {
      return new HashSet<>(Arrays.asList(new KoskiStudentId(getStudentIdentifier(HANDLER_TYPE, student.getId()), oid)));
    } else {
      return new HashSet<>();
    }
  }
  
}
