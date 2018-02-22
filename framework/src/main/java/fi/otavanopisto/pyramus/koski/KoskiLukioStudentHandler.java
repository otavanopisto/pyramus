package fi.otavanopisto.pyramus.koski;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod;
import fi.otavanopisto.pyramus.koski.CreditStubCredit.Type;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssit;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssitOPS2004Aikuiset;
import fi.otavanopisto.pyramus.koski.koodisto.LukionOppimaara;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineMatematiikka;
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
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunniste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnistePaikallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnisteValtakunnallinenOPS2004;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnisteValtakunnallinenOPS2015;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeudenLisatiedot;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenArviointi;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusAidinkieli;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusMatematiikka;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusMuuValtakunnallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusPaikallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusVierasKieli;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppimaaranSuoritus;

public class KoskiLukioStudentHandler extends KoskiStudentHandler {

  public static final String USERVARIABLE_EXTENDEDSTUDYTIME = "extendedStudyTime";
  public static final String USERVARIABLE_UNDER18STARTREASON = "under18studyStartReason";
  private static final KoskiStudyProgrammeHandler HANDLER_TYPE = KoskiStudyProgrammeHandler.lukio;

  @Inject
  private Logger logger;

  public Opiskeluoikeus studentToModel(Student student, String academyIdentifier, KoskiStudyProgrammeHandler handler) {
    if (handler != HANDLER_TYPE) {
      logger.log(Level.SEVERE, String.format("Wrong handler type %s, expected %s w/person %d.", handler, HANDLER_TYPE, student.getPerson().getId()));
      return null;
    }
    
    StudentSubjectSelections studentSubjects = loadStudentSubjectSelections(student, getDefaultSubjectSelections());
    String studyOid = userVariableDAO.findByUserAndKey(student, KOSKI_STUDYPERMISSION_ID);
    OpiskelijanOPS ops = resolveOPS(student);
    if (ops == null) {
      koskiPersonLogDAO.create(student.getPerson(), KoskiPersonState.NO_CURRICULUM, new Date());
      return null;
    }
    
    // Skip student if it is archived and the studyoid is blank
    if (Boolean.TRUE.equals(student.getArchived()) && StringUtils.isBlank(studyOid)) {
      return null;
    }
    
    LukionOpiskeluoikeus opiskeluoikeus = new LukionOpiskeluoikeus();
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

    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(academyIdentifier);
    EducationType studentEducationType = student.getStudyProgramme() != null && student.getStudyProgramme().getCategory() != null ? 
        student.getStudyProgramme().getCategory().getEducationType() : null;
    Set<LukionOppiaineenSuoritus> oppiaineet = assessmentsToModel(handler, ops, student, studentEducationType, studentSubjects, suorituksenTila == SuorituksenTila.VALMIS);

    LukionOppimaaranSuoritus suoritus = new LukionOppimaaranSuoritus(
        LukionOppimaara.aikuistenops, Kieli.FI, toimipiste, suorituksenTila);
    suoritus.getKoulutusmoduuli().setPerusteenDiaarinumero(getDiaarinumero(student));
    suoritus.setTodistuksellaNakyvatLisatiedot(getTodistuksellaNakyvatLisatiedot(student));
    if (suorituksenTila == SuorituksenTila.VALMIS)
      suoritus.setVahvistus(getVahvistus(student, academyIdentifier));
    opiskeluoikeus.addSuoritus(suoritus);
    
    oppiaineet.forEach(oppiaine -> suoritus.addOsasuoritus(oppiaine));
    
    return opiskeluoikeus;
  }
  
  private StudentSubjectSelections getDefaultSubjectSelections() {
    StudentSubjectSelections studentSubjects = new StudentSubjectSelections();
    studentSubjects.setMath("MAB");
    studentSubjects.setPrimaryLanguage("ÄI");
    studentSubjects.setReligion("UE");
    return studentSubjects;
  }

  private LukionOpiskeluoikeudenLisatiedot getLisatiedot(Student student) {
    boolean pidennettyPaattymispaiva = Boolean.valueOf(userVariableDAO.findByUserAndKey(student, USERVARIABLE_EXTENDEDSTUDYTIME));
    boolean ulkomainenVaihtoopiskelija = false;
    boolean yksityisopiskelija = false;
    boolean oikeusMaksuttomaanAsuntolapaikkaan = settings.isFreeLodging(student.getStudyProgramme().getId());
    LukionOpiskeluoikeudenLisatiedot lisatiedot = new LukionOpiskeluoikeudenLisatiedot(
        pidennettyPaattymispaiva, ulkomainenVaihtoopiskelija, yksityisopiskelija, oikeusMaksuttomaanAsuntolapaikkaan);

    String under18startReason = userVariableDAO.findByUserAndKey(student, USERVARIABLE_UNDER18STARTREASON);
    if (StringUtils.isNotBlank(under18startReason))
      lisatiedot.setAlle18vuotiaanAikuistenLukiokoulutuksenAloittamisenSyy(kuvaus(under18startReason));
    
    List<StudentLodgingPeriod> lodgingPeriods = lodgingPeriodDAO.listByStudent(student);
    for (StudentLodgingPeriod lodgingPeriod : lodgingPeriods) {
      Majoitusjakso jakso = new Majoitusjakso(lodgingPeriod.getBegin(), lodgingPeriod.getEnd());
      lisatiedot.addSisaoppilaitosmainenMajoitus(jakso);
    }
    
    return lisatiedot;
  }

  private Set<LukionOppiaineenSuoritus> assessmentsToModel(KoskiStudyProgrammeHandler handler, OpiskelijanOPS ops, Student student, EducationType studentEducationType, StudentSubjectSelections studentSubjects, boolean calculateMeanGrades) {
    Collection<CreditStub> credits = listCredits(student, true, true, ops, credit -> matchingCurriculumFilter(student, credit));
    Set<LukionOppiaineenSuoritus> results = new HashSet<>();
    
    Map<String, LukionOppiaineenSuoritus> map = new HashMap<>();
    
    for (CreditStub credit : credits) {
      LukionOppiaineenSuoritus oppiaineenSuoritus = getSubject(student, studentEducationType, credit.getSubject(), studentSubjects, map);

      if (settings.isReportedCredit(credit) && oppiaineenSuoritus != null) {
        LukionKurssinSuoritus kurssiSuoritus = createKurssiSuoritus(student, ops, credit);
        if (kurssiSuoritus != null) {
          oppiaineenSuoritus.addOsasuoritus(kurssiSuoritus);
        } else {
          logger.warning(String.format("Course %s not reported for student %d due to unresolvable credit.", credit.getCourseCode(), student.getId()));
          koskiPersonLogDAO.create(student.getPerson(), KoskiPersonState.UNREPORTED_CREDIT, new Date());
        }
      }
    }
    
    for (LukionOppiaineenSuoritus lukionOppiaineenSuoritus : map.values()) {
      if (CollectionUtils.isEmpty(lukionOppiaineenSuoritus.getOsasuoritukset())) {
        // Skip empty subjects
        continue;
      }
      
      // Valmiille oppiaineelle on rustattava kokonaisarviointi
      if (calculateMeanGrades) {
        ArviointiasteikkoYleissivistava aineKeskiarvo = getSubjectMeanGrade(lukionOppiaineenSuoritus);
        
        if (aineKeskiarvo != null) {
          LukionOppiaineenArviointi arviointi = new LukionOppiaineenArviointi(aineKeskiarvo, student.getStudyEndDate());
          lukionOppiaineenSuoritus.addArviointi(arviointi);
        } else {
          logger.warning(String.format("Unresolved mean grade for person %d.", student.getPerson().getId()));
        }
      }
      
      results.add(lukionOppiaineenSuoritus);
    }
    
    return results;
  }

  private ArviointiasteikkoYleissivistava getSubjectMeanGrade(LukionOppiaineenSuoritus oppiaineenSuoritus) {
    Set<ArviointiasteikkoYleissivistava> kurssiarvosanat = new HashSet<>();
    for (LukionKurssinSuoritus kurssinSuoritus : oppiaineenSuoritus.getOsasuoritukset()) {
      Set<KurssinArviointi> arvioinnit = kurssinSuoritus.getArviointi();
      Set<ArviointiasteikkoYleissivistava> arvosanat = arvioinnit.stream().map(arviointi -> arviointi.getArvosana().getValue()).collect(Collectors.toSet());
      
      kurssiarvosanat.add(ArviointiasteikkoYleissivistava.bestGrade(arvosanat));
    }
    
    return meanGrade(kurssiarvosanat);
  }
  
  private LukionOppiaineenSuoritus getSubject(Student student, EducationType studentEducationType,
      Subject subject, StudentSubjectSelections studentSubjects, Map<String, LukionOppiaineenSuoritus> map) {
    String subjectCode = subjectCode(subject);

    if (map.containsKey(subjectCode))
      return map.get(subjectCode);
    
    boolean matchingEducationType = studentEducationType != null && subject.getEducationType() != null && 
        studentEducationType.getId().equals(subject.getEducationType().getId());
    
    if (matchingEducationType && (StringUtils.equals(subjectCode, "MAA") || StringUtils.equals(subjectCode, "MAB"))) {
      if (StringUtils.equals(subjectCode, studentSubjects.getMath())) {
        LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusMatematiikka(
            OppiaineMatematiikka.valueOf(subjectCode), isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.MA));
        LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste);
        map.put(subjectCode, os);
        return os;
      } else
        return null;
    }
    if (matchingEducationType && StringUtils.equals(subjectCode, "MAY")) {
      LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusMatematiikka(
          OppiaineMatematiikka.MAY, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.MA));
      LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste);
      map.put(subjectCode, os);
      return os;
    }
    
    
    if (matchingEducationType && StringUtils.equals(subjectCode, "ÄI")) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusAidinkieli(
            OppiaineAidinkieliJaKirjallisuus.AI1, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.AI));
        LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste);
        map.put(subjectCode, os);
        return os;
      } else
        return null;
    }
    if (matchingEducationType && StringUtils.equals(subjectCode, "S2")) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusAidinkieli(
            OppiaineAidinkieliJaKirjallisuus.AI7, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.AI));
        LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste);
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
          
          LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusVierasKieli(valinta, kieli, 
              isPakollinenOppiaine(student, valinta));
          LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste);
          map.put(subjectCode, os);
          return os;
        } else {
          logger.log(Level.SEVERE, String.format("Koski: Language code %s could not be converted to an enum.", langCode));
          koskiPersonLogDAO.create(student.getPerson(), KoskiPersonState.UNKNOWN_LANGUAGE, new Date());
          return null;
        }
      }
    }

    String[] religionSubjects = new String[] { "UE", "UO" };
    
    if (matchingEducationType && ArrayUtils.contains(religionSubjects, subjectCode)) {
      // Only the religion that student has selected is reported
      if (StringUtils.equals(subjectCode, studentSubjects.getReligion())) {
        if (map.containsKey("KT"))
          return map.get("KT");
        
        KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.KT;
        LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusMuuValtakunnallinen(kansallinenAine, isPakollinenOppiaine(student, kansallinenAine));
        LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste);
        map.put("KT", os);
        return os;
      } else
        return null;
    }
    
    if (matchingEducationType && EnumUtils.isValidEnum(KoskiOppiaineetYleissivistava.class, StringUtils.upperCase(subjectCode))) {
      // Common national subject
      
      KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.valueOf(StringUtils.upperCase(subjectCode));
      LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusMuuValtakunnallinen(kansallinenAine, isPakollinenOppiaine(student, kansallinenAine));
      LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste);
      map.put(subjectCode, os);
      return os;
    } else {
      // Other local subject
      
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(subjectCode, kuvaus(subject.getName()));
      LukionOppiaineenSuoritusPaikallinen tunniste = new LukionOppiaineenSuoritusPaikallinen(paikallinenKoodi, false, kuvaus(subject.getName()));
      LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste);
      map.put(subjectCode, os);
      return os;
    }
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
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(kurssiKoodi, kuvaus(courseCredit.getSubject().getName()));
      LukionKurssinTyyppi kurssinTyyppi = findCourseType(student, courseCredit, true, LukionKurssinTyyppi.syventava, LukionKurssinTyyppi.soveltava);
      tunniste = new LukionKurssinTunnistePaikallinen(paikallinenKoodi , kurssinTyyppi, kuvaus(courseCredit.getCourseName()));
    }
      
    LukionKurssinSuoritus suoritus = new LukionKurssinSuoritus(tunniste, SuorituksenTila.VALMIS);

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
      koskiPersonLogDAO.create(student.getPerson(), KoskiPersonState.UNRESOLVABLE_SUBTYPES, new Date());
      return allowedValues[0];
    } else if (allowedSet.size() == 1) {
      return allowedSet.iterator().next();
    } else {
      for (LukionKurssinTyyppi type : allowedValues) {
        if (allowedSet.contains(type)) {
          logger.warning(String.format("Course %s has several matching subtypes.", courseCredit.getCourseCode()));
          koskiPersonLogDAO.create(student.getPerson(), KoskiPersonState.UNRESOLVABLE_SUBTYPES, new Date());
          return type;
        }
      }
    }
    
    return allowedValues[0];
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
