package fi.otavanopisto.pyramus.koski.model.internetix;

import java.util.ArrayList;
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

import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade;
import fi.otavanopisto.pyramus.koski.CreditStub;
import fi.otavanopisto.pyramus.koski.KoskiStudentId;
import fi.otavanopisto.pyramus.koski.KoskiStudyProgrammeHandler;
import fi.otavanopisto.pyramus.koski.OpiskelijanOPS;
import fi.otavanopisto.pyramus.koski.OppiaineenSuoritusWithCurriculum;
import fi.otavanopisto.pyramus.koski.StudentSubjectSelections;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenKurssit2015;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenPaattovaiheenKurssit2017;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiNumeerinen;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiSanallinen;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AbstractAikuistenPerusopetuksenHandler;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinSuoritus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunniste;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnisteOPS2015;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnistePV2017;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnistePaikallinen;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusEiTiedossa;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusMuu;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.PerusopetuksenOppiaineenOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.settings.KoskiStudyProgrammeHandlerParams;
import fi.otavanopisto.pyramus.koski.settings.StudyEndReasonMapping;

/**
 * Käsittelee Internetix-/aineopiskelijan peruskoulukurssien osuuden.
 */
public class KoskiInternetixPkStudentHandler extends AbstractAikuistenPerusopetuksenHandler {

  private static final KoskiStudyProgrammeHandler HANDLER_TYPE = KoskiStudyProgrammeHandler.aineopiskeluperusopetus;
  
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

    AikuistenPerusopetuksenOpiskeluoikeus opiskeluoikeus = new AikuistenPerusopetuksenOpiskeluoikeus();
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

    KoskiStudyProgrammeHandlerParams handlerParams = getHandlerParams(HANDLER_TYPE);

    // toimipiste-oid joko a) handlerParams:sta b) studyprogramme-asetuksista c) yleisesti kaikelle asetettu academyIdentifier
    String toimipisteOID = StringUtils.isNotBlank(handlerParams.getToimipisteOID()) ? handlerParams.getToimipisteOID() : 
      settings.getToimipisteOID(student.getStudyProgramme().getId(), academyIdentifier);

    assessmentsToModel(opiskeluoikeus, student, studentSubjects, laskeKeskiarvot, sisällytäVahvistus, toimipisteOID, defaultStudyProgramme);

    // Aineopiskelija

    boolean eiSuorituksia = CollectionUtils.isEmpty(opiskeluoikeus.getSuoritukset());
    
    if (eiSuorituksia) {
      AikuistenPerusopetuksenOppiaineenTunniste oppiaineenTunniste = new AikuistenPerusopetuksenOppiaineenSuoritusEiTiedossa();
      AikuistenPerusopetuksenOppiaineenSuoritus oppiaineenSuoritus = new AikuistenPerusopetuksenOppiaineenSuoritus(oppiaineenTunniste );

      PerusopetuksenOppiaineenOppimaaranSuoritus oppiaineenOppimaaranSuoritus = PerusopetuksenOppiaineenOppimaaranSuoritus.from(
          oppiaineenSuoritus, suoritusTapa(student), Kieli.FI, new OrganisaationToimipisteOID(toimipisteOID));
      opiskeluoikeus.addSuoritus(oppiaineenOppimaaranSuoritus);
    }
    
    return new OpiskeluoikeusInternetix(opiskeluoikeus, eiSuorituksia);
  }
  
  private StudentSubjectSelections getDefaultSubjectSelections() {
    StudentSubjectSelections studentSubjects = new StudentSubjectSelections();
    studentSubjects.setPrimaryLanguage("äi");
    studentSubjects.setReligion("ue");
    return studentSubjects;
  }
  
  private void assessmentsToModel(
      AikuistenPerusopetuksenOpiskeluoikeus opiskeluoikeus, Student student, StudentSubjectSelections studentSubjects, 
      boolean calculateMeanGrades, boolean sisällytäVahvistus, String toimipisteOID, boolean defaultStudyProgramme) {
    List<OpiskelijanOPS> opsList = new ArrayList<>();
    opsList.add(OpiskelijanOPS.ops2018);
    opsList.add(OpiskelijanOPS.ops2016);
    opsList.add(OpiskelijanOPS.ops2005);
    
    KoskiStudyProgrammeHandlerParams handlerParams = getHandlerParams(HANDLER_TYPE);
    
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
    
    PerusopetuksenSuoritusTapa perusopetuksenSuoritusTapa = suoritusTapa(student);

    List<String> sortedSubjects = getSortedKeys(map);
    for (String subjectCode : sortedSubjects) {
      OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus> oppiaineenSuoritus = map.get(subjectCode);
      
      if (CollectionUtils.isEmpty(oppiaineenSuoritus.getOppiaineenSuoritus().getOsasuoritukset())) {
        // Skip empty subjects
        continue;
      }

      StudentSubjectGrade studentSubjectGrade = findStudentSubjectGrade(student, oppiaineenSuoritus.getSubject());
      boolean hasStudentSubjectGrade = studentSubjectGrade != null;

      // Valmiille oppiaineelle on rustattava kokonaisarviointi
      if (calculateMeanGrades || hasStudentSubjectGrade) {
        ArviointiasteikkoYleissivistava aineKeskiarvo = accomplished.contains(oppiaineenSuoritus) ? 
            ArviointiasteikkoYleissivistava.GRADE_S : getSubjectMeanGrade(student, oppiaineenSuoritus.getSubject(), oppiaineenSuoritus.getOppiaineenSuoritus());
        Date arviointiPvm = (studentSubjectGrade != null && studentSubjectGrade.getGradeDate() != null) ? studentSubjectGrade.getGradeDate() : student.getStudyEndDate();
        
        if (ArviointiasteikkoYleissivistava.isNumeric(aineKeskiarvo)) {
          KurssinArviointi arviointi = new KurssinArviointiNumeerinen(aineKeskiarvo, arviointiPvm);
          oppiaineenSuoritus.getOppiaineenSuoritus().addArviointi(arviointi);
        } else if (ArviointiasteikkoYleissivistava.isLiteral(aineKeskiarvo)) {
          KurssinArviointi arviointi = new KurssinArviointiSanallinen(aineKeskiarvo, arviointiPvm, kuvaus("Suoritettu/Hylätty"));
          oppiaineenSuoritus.getOppiaineenSuoritus().addArviointi(arviointi);
        }
      }
      
      PerusopetuksenOppiaineenOppimaaranSuoritus oppiaineenOppimaaranSuoritus = PerusopetuksenOppiaineenOppimaaranSuoritus.from(
          oppiaineenSuoritus.getOppiaineenSuoritus(), perusopetuksenSuoritusTapa, Kieli.FI, new OrganisaationToimipisteOID(toimipisteOID));
      oppiaineenOppimaaranSuoritus.getKoulutusmoduuli().setPerusteenDiaarinumero(getDiaarinumero(HANDLER_TYPE, oppiaineenSuoritus.getOps()));
      oppiaineenOppimaaranSuoritus.setTodistuksellaNakyvatLisatiedot(getTodistuksellaNakyvatLisatiedot(student));
      
      if (hasStudentSubjectGrade) {
        oppiaineenOppimaaranSuoritus.setVahvistus(getVahvistus(student, studentSubjectGrade.getGradeApprover(), studentSubjectGrade.getGradeDate(), toimipisteOID));
      } else if (sisällytäVahvistus) {
        oppiaineenOppimaaranSuoritus.setVahvistus(getVahvistus(student, toimipisteOID));
      }
      
      opiskeluoikeus.addSuoritus(oppiaineenOppimaaranSuoritus);
    }
    
  }

  private OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus> map(
      Map<String, OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus>> map, String mapKey, 
      OpiskelijanOPS creditOPS, AikuistenPerusopetuksenOppiaineenTunniste tunniste, Subject subject) {
    OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus> oswc = 
        new OppiaineenSuoritusWithCurriculum<>(subject, creditOPS, new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste));
    map.put(mapKey, oswc);
    return oswc;
  }
  
  private OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus> getSubject(
      OpiskelijanOPS creditOPS, Student student, List<Long> educationTypes, 
      StudentSubjectSelections studentSubjects, Subject subject, 
      Map<String, OppiaineenSuoritusWithCurriculum<AikuistenPerusopetuksenOppiaineenSuoritus>> map) {
    String subjectCode = subjectCode(subject);

    String mapKey = String.valueOf(creditOPS) + subjectCode;
    if (map.containsKey(mapKey)) {
      return map.get(mapKey);
    }
    
    boolean matchingEducationType = educationTypes != null && subject.getEducationType() != null && 
        educationTypes.contains(subject.getEducationType().getId());
    if (matchingEducationType && (StringUtils.equals(subjectCode, "äi") || StringUtils.equals(subjectCode, "s2"))) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        OppiaineAidinkieliJaKirjallisuus aine = StringUtils.equals(subjectCode, "s2") ? 
            OppiaineAidinkieliJaKirjallisuus.AI7 :  // s2
              OppiaineAidinkieliJaKirjallisuus.AI1; // äi
        
        AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli(
            aine, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.AI));
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
          
          AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli(
              valinta, kieli, isPakollinenOppiaine(student, valinta));
          return map(map, mapKey, creditOPS, tunniste, subject);
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
        return map(map, mapKey, creditOPS, tunniste, subject);
      } else
        return null;
    }
    
    if (matchingEducationType && EnumUtils.isValidEnum(KoskiOppiaineetYleissivistava.class, StringUtils.upperCase(subjectCode))) {
      // Common national subject
      
      KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.valueOf(StringUtils.upperCase(subjectCode));
      AikuistenPerusopetuksenOppiaineenTunniste tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusMuu(
          kansallinenAine, isPakollinenOppiaine(student, kansallinenAine));
      return map(map, mapKey, creditOPS, tunniste, subject);
    } else {
      // Other local subject
      // TODO Skipped subjects ?? (MUU)
      
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(subjectCode, kuvaus(subject.getName()));
      AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen(
          paikallinenKoodi, false, kuvaus(subject.getName()));
      return map(map, mapKey, creditOPS, tunniste, subject);
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
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(kurssiKoodi, kuvaus(courseCredit.getCourseName()));
      tunniste = new AikuistenPerusopetuksenKurssinTunnistePaikallinen(paikallinenKoodi);
    }
      
    AikuistenPerusopetuksenKurssinSuoritus suoritus = new AikuistenPerusopetuksenKurssinSuoritus(tunniste);

    return luoKurssiSuoritus(suoritus, courseCredit);
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