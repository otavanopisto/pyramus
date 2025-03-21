package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.ArrayList;
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
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.CreditStub;
import fi.otavanopisto.pyramus.koski.KoskiConsts;
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
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiNumeerinen;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiSanallinen;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;
import fi.otavanopisto.pyramus.koski.settings.StudyEndReasonMapping;

public class KoskiAikuistenPerusopetuksenStudentHandler extends AbstractAikuistenPerusopetuksenHandler {

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
    
    if (student.getStudyStartDate() == null) {
      koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.NO_STUDYSTARTDATE, new Date());
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
    
    OpintojenRahoitus opintojenRahoitus = opintojenRahoitus(student);
    StudyEndReasonMapping lopetusSyy = opiskelujaksot(student, opiskeluoikeus.getTila(), opintojenRahoitus);
    boolean laskeKeskiarvot = lopetusSyy != null ? lopetusSyy.getLaskeAinekeskiarvot() : false;
    boolean sisällytäVahvistus = lopetusSyy != null ? lopetusSyy.getSisällytäVahvistaja() : false;

    String departmentIdentifier = settings.getToimipisteOID(student.getStudyProgramme().getId(), academyIdentifier);

    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(departmentIdentifier);
    EducationType studentEducationType = student.getStudyProgramme() != null && student.getStudyProgramme().getCategory() != null ? 
        student.getStudyProgramme().getCategory().getEducationType() : null;
    List<AikuistenPerusopetuksenOppiaineenSuoritus> oppiaineet = assessmentsToModel(ops, student, studentEducationType, 
        studentSubjects, laskeKeskiarvot);

    AikuistenPerusopetuksenOppimaaranSuoritus suoritus = new AikuistenPerusopetuksenOppimaaranSuoritus(
        suoritusTapa(student), Kieli.FI, toimipiste);
    suoritus.setTodistuksellaNakyvatLisatiedot(getTodistuksellaNakyvatLisatiedot(student));
    suoritus.getKoulutusmoduuli().setPerusteenDiaarinumero(getDiaarinumero(student));
    if (sisällytäVahvistus) {
      suoritus.setVahvistus(getVahvistus(student, departmentIdentifier));
    }
    opiskeluoikeus.addSuoritus(suoritus);
    
    oppiaineet.forEach(oppiaine -> suoritus.addOsasuoritus(oppiaine));
    
    return opiskeluoikeus;
  }
  
  private StudentSubjectSelections getDefaultSubjectSelections() {
    StudentSubjectSelections studentSubjects = new StudentSubjectSelections();
    studentSubjects.setPrimaryLanguage("äi");
    studentSubjects.setReligion("ue");
    return studentSubjects;
  }
  
  private List<AikuistenPerusopetuksenOppiaineenSuoritus> assessmentsToModel(OpiskelijanOPS ops, Student student, EducationType studentEducationType, StudentSubjectSelections studentSubjects, boolean calculateMeanGrades) {
    Collection<CreditStub> credits = listCredits(student, true, true, ops, credit -> matchingCurriculumFilter(student, credit));
    List<AikuistenPerusopetuksenOppiaineenSuoritus> results = new ArrayList<>();
    
    Map<String, OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus>> map = new HashMap<>();
    Set<OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus>> accomplished = new HashSet<>();
    
    for (CreditStub credit : credits) {
      OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus> oppiaineenSuoritusWSubject = getSubject(student, ops, studentEducationType, studentSubjects, credit.getSubject(), map);
      collectAccomplishedMarks(credit.getSubject(), oppiaineenSuoritusWSubject, studentSubjects, accomplished);

      if (settings.isReportedCredit(credit) && oppiaineenSuoritusWSubject != null) {
        AikuistenPerusopetuksenKurssinSuoritus kurssiSuoritus = createKurssiSuoritus(ops, credit);
        if (kurssiSuoritus != null) {
          oppiaineenSuoritusWSubject.getOppiaineenSuoritus().addOsasuoritus(kurssiSuoritus);
        } else {
          logger.warning(String.format("Course %s not reported for student %d due to unresolvable credit.", credit.getCourseCode(), student.getId()));
          koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNREPORTED_CREDIT, new Date(), credit.getCourseCode());
        }
      }
    }
    
    List<String> sortedSubjects = getSortedKeys(map);
    for (String subjectCode : sortedSubjects) {
      OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus> oppiaineenSuoritusWSubject = map.get(subjectCode);
      
      AikuistenPerusopetuksenOppiaineenSuoritus oppiaineenSuoritus = oppiaineenSuoritusWSubject.getOppiaineenSuoritus();
      if (CollectionUtils.isEmpty(oppiaineenSuoritus.getOsasuoritukset())) {
        // Skip empty subjects
        continue;
      }
      
      // Valmiille oppiaineelle on rustattava kokonaisarviointi
      if (calculateMeanGrades) {
        ArviointiasteikkoYleissivistava aineKeskiarvo = accomplished.contains(oppiaineenSuoritusWSubject) ? 
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

  private OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus> getSubject(Student student, OpiskelijanOPS ops, EducationType studentEducationType, 
      StudentSubjectSelections studentSubjects, Subject subject, Map<String, OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus>> map) {
    String subjectCode = subjectCode(subject);

    // ot1, ot2 etc have subject OPA
    if (StringUtils.equals(subjectCode, "ot")) {
      subjectCode = "OPA";
    }

    /*
     * Uskonnon ainevalinta. Uskonnosta voi olla valittuna aina vain 
     * yksi aine. Jos opiskelija opiskelee useampaa samaan aikaan,
     * toissijainen hyväksiluetaan osaksi ensisijaista uskonnon ainetta.
     */
    if (KoskiConsts.Perusopetus.USKONTO_JA_ET.contains(subjectCode)) {
      // Vain ainevalinnoissa olevat uskonnon aineet ilmoitetaan
      if (!studentSubjects.isReligion(subjectCode)) {
        return null;
      }
      
      // Elämänkatsomustiedolla (et) on oma oppiaineensa, Muille
      // uskonnon oppiaineille käytetään ainekoodia KT
      if (KoskiConsts.Perusopetus.USKONTO.contains(subjectCode)) {
        subjectCode = "KT";
      }
    }

    if (map.containsKey(subjectCode)) {
      return map.get(subjectCode);
    }
    
    boolean matchingEducationType = studentEducationType != null && subject.getEducationType() != null && 
        studentEducationType.getId().equals(subject.getEducationType().getId());
    
    if (matchingEducationType && (StringUtils.equals(subjectCode, "äi") || StringUtils.equals(subjectCode, "s2"))) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        OppiaineAidinkieliJaKirjallisuus aine = StringUtils.equals(subjectCode, "s2") ? 
            OppiaineAidinkieliJaKirjallisuus.AI7 :  // s2
              OppiaineAidinkieliJaKirjallisuus.AI1; // äi
        
        AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli(
            aine, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.AI));
        return mapSubject(subject, subjectCode, false, tunniste, map);
      } else
        return null;
    }
    
    if (matchingEducationType && studentSubjects.isAdditionalLanguage(subjectCode)) {
      if (subjectCode.length() > 2) {
        String langCode = settings.getSubjectToLanguageMapping(subjectCode.substring(0, 2).toUpperCase());
        Kielivalikoima kieli = langCode != null ? Kielivalikoima.valueOf(langCode) : null;
        
        if (kieli != null) {
          KoskiOppiaineetYleissivistava valinta = studentSubjects.koskiKoodi(ops, subjectCode);
          AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli(
              valinta, kieli, isPakollinenOppiaine(student, valinta));
          return mapSubject(subject, subjectCode, false, tunniste, map);
        } else {
          logger.log(Level.SEVERE, String.format("Koski: Language code %s could not be converted to an enum. Subject code was %s.", langCode, subjectCode));
          koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNKNOWN_LANGUAGE, new Date(), langCode);
          return null;
        }
      }
    }

    if (matchingEducationType && EnumUtils.isValidEnum(KoskiOppiaineetYleissivistava.class, StringUtils.upperCase(subjectCode))) {
      // Common national subject
      
      KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.valueOf(StringUtils.upperCase(subjectCode));
      AikuistenPerusopetuksenOppiaineenTunniste tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusMuu(
          kansallinenAine, isPakollinenOppiaine(student, kansallinenAine));
      return mapSubject(subject, subjectCode, false, tunniste, map);
    } else {
      // Other local subject
      // TODO Skipped subjects ?? (MUU)
      
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(subjectCode, kuvaus(subject.getName()));
      AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen(
          paikallinenKoodi, false, kuvaus(subject.getName()));
      return mapSubject(subject, subjectCode, true, tunniste, map);
    }
  }

  private OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus> mapSubject(
      Subject subject, String subjectCode, boolean paikallinenOppiaine, AikuistenPerusopetuksenOppiaineenTunniste tunniste,
      Map<String, OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus>> map) {
    OppiaineenSuoritusWithSubject<AikuistenPerusopetuksenOppiaineenSuoritus> os = new OppiaineenSuoritusWithSubject<>(subject, paikallinenOppiaine, new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste));
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
    if (handler == HANDLER_TYPE) {
      saveOrValidateOid(student, oid);
    } else {
      logger.severe(String.format("saveOrValidateOid called with wrong handler %s, expected %s ", handler, HANDLER_TYPE));
    }
  }

  @Override
  public void removeOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    if (handler == HANDLER_TYPE) {
      removeOid(student, oid);
    } else {
      logger.severe(String.format("removeOid called with wrong handler %s, expected %s ", handler, HANDLER_TYPE));
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
