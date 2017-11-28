package fi.otavanopisto.pyramus.koski.model.apa;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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

import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.CreditStub;
import fi.otavanopisto.pyramus.koski.KoskiException;
import fi.otavanopisto.pyramus.koski.KoskiStudentHandler;
import fi.otavanopisto.pyramus.koski.OpiskelijanOPS;
import fi.otavanopisto.pyramus.koski.StudentSubjectSelections;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenAlkuvaiheenKurssit2017;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenAlkuvaiheenOppiaineet;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiNumeerinen;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiSanallinen;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusJakso;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOpiskeluoikeus;

public class KoskiAPAStudentHandler extends KoskiStudentHandler {

  @Inject
  private Logger logger;

  public Opiskeluoikeus studentToModel(Student student, String academyIdentifier) throws KoskiException {
    OpiskelijanOPS ops = resolveOPS(student);
    if (ops == null)
      throw new KoskiException(String.format("Cannot report student %d without curriculum.", student.getId()), KoskiPersonState.NO_CURRICULUM);
    
    StudentSubjectSelections studentSubjects = loadStudentSubjectSelections(student, getDefaultStudentSubjectSelections());
    String studyOid = userVariableDAO.findByUserAndKey(student, KOSKI_STUDYPERMISSION_ID);

    AikuistenPerusopetuksenOpiskeluoikeus opiskeluoikeus = new AikuistenPerusopetuksenOpiskeluoikeus();
    opiskeluoikeus.setLahdejarjestelmanId(getLahdeJarjestelmaID(student.getId()));
    opiskeluoikeus.setAlkamispaiva(student.getStudyStartDate());
    opiskeluoikeus.setPaattymispaiva(student.getStudyEndDate());
    if (studyOid != null) {
      opiskeluoikeus.setOid(studyOid);
    }
    handleLinkedStudyOID(student, opiskeluoikeus);
    
    OpiskeluoikeusJakso jakso = new OpiskeluoikeusJakso(student.getStudyStartDate(), OpiskeluoikeudenTila.lasna);
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(jakso);

    SuorituksenTila suorituksenTila = SuorituksenTila.KESKEN;

    if (student.getStudyEndDate() != null) {
      OpiskeluoikeudenTila opintojenLopetusTila = settings.getStudentState(student);
      opiskeluoikeus.getTila().addOpiskeluoikeusJakso(
          new OpiskeluoikeusJakso(student.getStudyEndDate(), opintojenLopetusTila));

      suorituksenTila = ArrayUtils.contains(OpiskeluoikeudenTila.GRADUATED_STATES, opintojenLopetusTila) ? 
          SuorituksenTila.VALMIS : SuorituksenTila.KESKEYTYNYT;
    }
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(academyIdentifier);
    APASuoritus suoritus = new APASuoritus(
        PerusopetuksenSuoritusTapa.koulutus, Kieli.FI, toimipiste, suorituksenTila);
    suoritus.getKoulutusmoduuli().setPerusteenDiaarinumero(getDiaarinumero(student));
    if (suorituksenTila == SuorituksenTila.VALMIS)
      suoritus.setVahvistus(getVahvistus(student, academyIdentifier));
    opiskeluoikeus.addSuoritus(suoritus);
    
    EducationType studentEducationType = student.getStudyProgramme() != null && student.getStudyProgramme().getCategory() != null ? 
        student.getStudyProgramme().getCategory().getEducationType() : null;
    assessmentsToModel(ops, student, studentEducationType, studentSubjects, suoritus);
    
    return opiskeluoikeus;
  }
  
  private StudentSubjectSelections getDefaultStudentSubjectSelections() {
    StudentSubjectSelections studentSubjects = new StudentSubjectSelections();
    studentSubjects.setA1Languages("aena");
    return studentSubjects;
  }

  private void assessmentsToModel(OpiskelijanOPS ops, Student student, EducationType studentEducationType, StudentSubjectSelections studentSubjects,
      APASuoritus oppimaaranSuoritus) {
    Collection<CreditStub> credits = listCredits(student);
    
    Map<String, APAOppiaineenSuoritus> map = new HashMap<>();
    
    for (CreditStub credit : credits) {
      APAOppiaineenSuoritus oppiaineenSuoritus = getSubject(student, studentEducationType, studentSubjects, credit.getSubject(), map);

      if (settings.isReportedCredit(credit) && oppiaineenSuoritus != null) {
        APAKurssinSuoritus kurssiSuoritus = createKurssiSuoritus(ops, credit);
        if (kurssiSuoritus != null) {
          oppiaineenSuoritus.addOsasuoritus(kurssiSuoritus);
        } else {
          logger.warning(String.format("Course %s not reported for student %d due to unresolvable credit.", credit.getCourseCode(), student.getId()));
        }
      }
    }
    
    for (APAOppiaineenSuoritus oppiaineenSuoritus : map.values()) {
      if (CollectionUtils.isEmpty(oppiaineenSuoritus.getOsasuoritukset())) {
        // Skip empty subjects
        continue;
      }
      
      // Valmiille oppiaineelle on rustattava kokonaisarviointi
      if (oppimaaranSuoritus.getTila().getValue() == SuorituksenTila.VALMIS) {
        ArviointiasteikkoYleissivistava aineKeskiarvo = getSubjectMeanGrade(oppiaineenSuoritus);
        
        if (ArviointiasteikkoYleissivistava.isNumeric(aineKeskiarvo)) {
          KurssinArviointi arviointi = new KurssinArviointiNumeerinen(aineKeskiarvo, student.getStudyEndDate());
          oppiaineenSuoritus.addArviointi(arviointi);
        } else if (ArviointiasteikkoYleissivistava.isNumeric(aineKeskiarvo)) {
          KurssinArviointi arviointi = new KurssinArviointiSanallinen(aineKeskiarvo, student.getStudyEndDate(), kuvaus("Suoritettu/Hylätty"));
          oppiaineenSuoritus.addArviointi(arviointi);
        }
      }
      
      oppimaaranSuoritus.addOsasuoritus(oppiaineenSuoritus);
    }
  }

  private APAOppiaineenSuoritus getSubject(Student student, EducationType studentEducationType, 
      StudentSubjectSelections studentSubjects, Subject subject, Map<String, APAOppiaineenSuoritus> map) {
    String subjectCode = subject.getCode();

    if (map.containsKey(subjectCode)) {
      return map.get(subjectCode);
    }
    
    boolean matchingEducationType = studentEducationType != null && subject.getEducationType() != null && 
        studentEducationType.getId().equals(subject.getEducationType().getId());
    
    if (matchingEducationType && StringUtils.equals(subjectCode, "as2")) {
      OppiaineAidinkieliJaKirjallisuus aine = OppiaineAidinkieliJaKirjallisuus.AI7; // s2
      
      APAOppiaineenTunniste tunniste = new APAOppiaineenTunnisteAidinkieli(aine);
      APAOppiaineenSuoritus os = new APAOppiaineenSuoritus(tunniste);
      map.put(subjectCode, os);
      return os;
    }
    
    // APA has only A1 foreign language
    if (matchingEducationType && studentSubjects.isA1Language(subjectCode)) {
      if (subjectCode.length() >= 3) {
        // APA Subject codes are of form a + code so substring without the a
        String langCode = settings.getSubjectToLanguageMapping(subjectCode.substring(1, 3).toUpperCase());
        Kielivalikoima kieli = Kielivalikoima.valueOf(langCode);
        
        if (kieli != null) {
          APAOppiaineenTunniste tunniste = new APAOppiaineenTunnisteVierasKieli(
              AikuistenPerusopetuksenAlkuvaiheenOppiaineet.A1, kieli);
          APAOppiaineenSuoritus os = new APAOppiaineenSuoritus(tunniste);
          map.put(subjectCode, os);
          return os;
        } else {
          logger.log(Level.SEVERE, String.format("Koski: Language code %s could not be converted to an enum.", langCode));
          return null;
        }
      }
    }

    AikuistenPerusopetuksenAlkuvaiheenOppiaineet nationalSubject = getNationalSubject(subjectCode);
    
    if (nationalSubject != null) {
      // Common national subject
      
      APAOppiaineenTunniste tunniste = new APAOppiaineenTunnisteMuu(nationalSubject);
      APAOppiaineenSuoritus os = new APAOppiaineenSuoritus(tunniste);
      map.put(subjectCode, os);
      return os;
    } else {
      // Other local subject
      
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(subjectCode, kuvaus(subject.getName()));
      APAOppiaineenTunniste tunniste = new APAOppiaineenTunnistePaikallinen(paikallinenKoodi, kuvaus(subject.getName()));
      APAOppiaineenSuoritus os = new APAOppiaineenSuoritus(tunniste);
      map.put(subjectCode, os);
      return os;
    }
  }
  
  private AikuistenPerusopetuksenAlkuvaiheenOppiaineet getNationalSubject(String subjectCode) {
    subjectCode = subjectCode.toUpperCase();
    
    for (AikuistenPerusopetuksenAlkuvaiheenOppiaineet subject : AikuistenPerusopetuksenAlkuvaiheenOppiaineet.values()) {
      if (subjectCode.equals("A" + subject.toString())) {
        return subject;
      }
    }
    
    return null;
  }

  protected APAKurssinSuoritus createKurssiSuoritus(OpiskelijanOPS ops, CreditStub courseCredit) {
    String kurssiKoodi = StringUtils.upperCase(courseCredit.getCourseCode());
    APAKurssinTunniste tunniste;
    
    if (ops == OpiskelijanOPS.ops2018 && EnumUtils.isValidEnum(AikuistenPerusopetuksenAlkuvaiheenKurssit2017.class, kurssiKoodi)) {
      AikuistenPerusopetuksenAlkuvaiheenKurssit2017 kurssi = AikuistenPerusopetuksenAlkuvaiheenKurssit2017.valueOf(kurssiKoodi);
      tunniste = new APAKurssinTunnisteOPS2017(kurssi);
    } else {
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(kurssiKoodi, kuvaus(courseCredit.getSubject().getName()));
      tunniste = new APAKurssinTunnistePaikallinen(paikallinenKoodi);
    }
      
    APAKurssinSuoritus suoritus = new APAKurssinSuoritus(tunniste);

    for (Credit credit : courseCredit.getCredits()) {
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

  private ArviointiasteikkoYleissivistava getSubjectMeanGrade(APAOppiaineenSuoritus oppiaineenSuoritus) {
    Set<ArviointiasteikkoYleissivistava> kurssiarvosanat = new HashSet<>();
    for (APAKurssinSuoritus kurssinSuoritus : oppiaineenSuoritus.getOsasuoritukset()) {
      Set<KurssinArviointi> arvioinnit = kurssinSuoritus.getArviointi();
      Set<ArviointiasteikkoYleissivistava> arvosanat = arvioinnit.stream().map(arviointi -> arviointi.getArvosana().getValue()).collect(Collectors.toSet());
      
      kurssiarvosanat.add(ArviointiasteikkoYleissivistava.bestGrade(arvosanat));
    }
    
    return meanGrade(kurssiarvosanat);
  }
  
}
