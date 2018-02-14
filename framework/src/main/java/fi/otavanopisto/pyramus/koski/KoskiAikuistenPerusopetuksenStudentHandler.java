package fi.otavanopisto.pyramus.koski;

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
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppimaaranSuoritus;

public class KoskiAikuistenPerusopetuksenStudentHandler extends KoskiStudentHandler {

  private static final KoskiStudyProgrammeHandler HANDLER_TYPE = KoskiStudyProgrammeHandler.aikuistenperusopetus;
  
  @Inject
  private Logger logger;

  public Opiskeluoikeus studentToModel(Student student, String academyIdentifier, KoskiStudyProgrammeHandler handler) throws KoskiException {
    if (handler != HANDLER_TYPE) {
      logger.log(Level.SEVERE, String.format("Wrong handler type %s expected %s w/person %d.", handler, HANDLER_TYPE, student.getPerson().getId()));
      return null;
    }
    
    OpiskelijanOPS ops = resolveOPS(student);
    if (ops == null) {
      koskiPersonLogDAO.create(student.getPerson(), KoskiPersonState.NO_CURRICULUM, new Date());
      return null;
    }
    
    StudentSubjectSelections studentSubjects = loadStudentSubjectSelections(student, getDefaultSubjectSelections());
    String studyOid = userVariableDAO.findByUserAndKey(student, KOSKI_STUDYPERMISSION_ID);

    // Skip student if it is archived and the studyoid is blank
    if (Boolean.TRUE.equals(student.getArchived()) && StringUtils.isBlank(studyOid)) {
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
    
    handleLinkedStudyOID(student, opiskeluoikeus);
    
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
    EducationType studentEducationType = student.getStudyProgramme() != null && student.getStudyProgramme().getCategory() != null ? 
        student.getStudyProgramme().getCategory().getEducationType() : null;
    Set<AikuistenPerusopetuksenOppiaineenSuoritus> oppiaineet = assessmentsToModel(ops, student, studentEducationType, studentSubjects, suorituksenTila == SuorituksenTila.VALMIS);

    AikuistenPerusopetuksenOppimaaranSuoritus suoritus = new AikuistenPerusopetuksenOppimaaranSuoritus(
        PerusopetuksenSuoritusTapa.koulutus, Kieli.FI, toimipiste, suorituksenTila);
    suoritus.setTodistuksellaNakyvatLisatiedot(getTodistuksellaNakyvatLisatiedot(student));
    suoritus.getKoulutusmoduuli().setPerusteenDiaarinumero(getDiaarinumero(student));
    if (suorituksenTila == SuorituksenTila.VALMIS)
      suoritus.setVahvistus(getVahvistus(student, academyIdentifier));
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
    Collection<CreditStub> credits = listCredits(student, false, false, ops, credit -> matchingCurriculumFilter(student, credit));
    Set<AikuistenPerusopetuksenOppiaineenSuoritus> results = new HashSet<>();
    
    Map<String, AikuistenPerusopetuksenOppiaineenSuoritus> map = new HashMap<>();
    
    for (CreditStub credit : credits) {
      AikuistenPerusopetuksenOppiaineenSuoritus oppiaineenSuoritus = getSubject(student, studentEducationType, studentSubjects, credit.getSubject(), map);

      if (settings.isReportedCredit(credit) && oppiaineenSuoritus != null) {
        AikuistenPerusopetuksenKurssinSuoritus kurssiSuoritus = createKurssiSuoritus(ops, credit);
        if (kurssiSuoritus != null) {
          oppiaineenSuoritus.addOsasuoritus(kurssiSuoritus);
        } else {
          logger.warning(String.format("Course %s not reported for student %d due to unresolvable credit.", credit.getCourseCode(), student.getId()));
        }
      }
    }
    
    for (AikuistenPerusopetuksenOppiaineenSuoritus oppiaineenSuoritus : map.values()) {
      if (CollectionUtils.isEmpty(oppiaineenSuoritus.getOsasuoritukset())) {
        // Skip empty subjects
        continue;
      }
      
      // Valmiille oppiaineelle on rustattava kokonaisarviointi
      if (calculateMeanGrades) {
        ArviointiasteikkoYleissivistava aineKeskiarvo = getSubjectMeanGrade(oppiaineenSuoritus);
        
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

  private AikuistenPerusopetuksenOppiaineenSuoritus getSubject(Student student, EducationType studentEducationType, 
      StudentSubjectSelections studentSubjects, Subject subject, Map<String, AikuistenPerusopetuksenOppiaineenSuoritus> map) {
    String subjectCode = subjectCode(subject);

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
        AikuistenPerusopetuksenOppiaineenSuoritus os = new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste);
        map.put(subjectCode, os);
        return os;
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
          AikuistenPerusopetuksenOppiaineenSuoritus os = new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste);
          map.put(subjectCode, os);
          return os;
        } else {
          logger.log(Level.SEVERE, String.format("Koski: Language code %s could not be converted to an enum.", langCode));
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
        AikuistenPerusopetuksenOppiaineenSuoritus os = new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste);
        map.put("KT", os);
        return os;
      } else
        return null;
    }
    
    if (matchingEducationType && EnumUtils.isValidEnum(KoskiOppiaineetYleissivistava.class, StringUtils.upperCase(subjectCode))) {
      // Common national subject
      
      KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.valueOf(StringUtils.upperCase(subjectCode));
      AikuistenPerusopetuksenOppiaineenTunniste tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusMuu(
          kansallinenAine, isPakollinenOppiaine(student, kansallinenAine));
      AikuistenPerusopetuksenOppiaineenSuoritus os = new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste);
      map.put(subjectCode, os);
      return os;
    } else {
      // Other local subject
      // TODO Skipped subjects ?? (MUU)
      
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(subjectCode, kuvaus(subject.getName()));
      AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen(
          paikallinenKoodi, false, kuvaus(subject.getName()));
      AikuistenPerusopetuksenOppiaineenSuoritus os = new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste);
      map.put(subjectCode, os);
      return os;
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
      
    AikuistenPerusopetuksenKurssinSuoritus suoritus = new AikuistenPerusopetuksenKurssinSuoritus(tunniste, SuorituksenTila.VALMIS);

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
    Set<ArviointiasteikkoYleissivistava> kurssiarvosanat = new HashSet<>();
    for (AikuistenPerusopetuksenKurssinSuoritus kurssinSuoritus : oppiaineenSuoritus.getOsasuoritukset()) {
      Set<KurssinArviointi> arvioinnit = kurssinSuoritus.getArviointi();
      Set<ArviointiasteikkoYleissivistava> arvosanat = arvioinnit.stream().map(arviointi -> arviointi.getArvosana().getValue()).collect(Collectors.toSet());
      
      kurssiarvosanat.add(ArviointiasteikkoYleissivistava.bestGrade(arvosanat));
    }
    
    return meanGrade(kurssiarvosanat);
  }

  @Override
  public void saveOrValidateOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    if (handler == HANDLER_TYPE) {
      saveOrValidateOid(student, oid);
    } else {
      logger.severe(String.format("saveOrValidateOid called with wrong handler %s, expected %s ", handler, HANDLER_TYPE));
    }
  }
  
}
