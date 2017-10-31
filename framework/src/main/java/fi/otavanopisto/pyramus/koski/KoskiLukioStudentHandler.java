package fi.otavanopisto.pyramus.koski;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssit;
import fi.otavanopisto.pyramus.koski.koodisto.LukionOppimaara;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineMatematiikka;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiNumeerinen;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiSanallinen;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusJakso;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.OsaamisenTunnustaminen;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunniste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnistePaikallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnisteValtakunnallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusAidinkieli;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusMatematiikka;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusMuuValtakunnallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusPaikallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusVierasKieli;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppimaaranSuoritus;

public class KoskiLukioStudentHandler extends KoskiStudentHandler {

  @Inject
  private Logger logger;

  public Opiskeluoikeus studentToModel(Student student, String academyIdentifier) {
    OpiskeluoikeudenTyyppi opiskeluoikeudenTyyppi = settings.getOpiskeluoikeudenTyyppi(student.getStudyProgramme().getId());
    StudentSubjectSelections studentSubjects = loadStudentSubjectSelections(student, opiskeluoikeudenTyyppi);
    String studyOid = userVariableDAO.findByUserAndKey(student, KOSKI_STUDYPERMISSION_ID);
    
    LukionOpiskeluoikeus opiskeluoikeus = new LukionOpiskeluoikeus();
    opiskeluoikeus.setLahdejarjestelmanId(getLahdeJarjestelmaID(student.getId()));
    opiskeluoikeus.setAlkamispaiva(student.getStudyStartDate());
    opiskeluoikeus.setPaattymispaiva(student.getStudyEndDate());
    if (studyOid != null)
      opiskeluoikeus.setOid(studyOid);

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
    LukionOppimaaranSuoritus suoritus = new LukionOppimaaranSuoritus(
        LukionOppimaara.aikuistenops, Kieli.FI, toimipiste, suorituksenTila);
    suoritus.getKoulutusmoduuli().setPerusteenDiaarinumero(getDiaarinumero(student));
    if (suorituksenTila == SuorituksenTila.VALMIS)
      suoritus.setVahvistus(getVahvistus(student, academyIdentifier));
    opiskeluoikeus.addSuoritus(suoritus);
    

    EducationType studentEducationType = student.getStudyProgramme() != null && student.getStudyProgramme().getCategory() != null ? 
        student.getStudyProgramme().getCategory().getEducationType() : null;
    assessmentsToModel(student, studentEducationType, studentSubjects, suoritus);
    
    return opiskeluoikeus;
  }
  
  private void assessmentsToModel(Student student, EducationType studentEducationType, StudentSubjectSelections studentSubjects, LukionOppimaaranSuoritus oppimaaranSuoritus) {
    Collection<CreditStub> credits = listCredits(student);
    
    Map<String, LukionOppiaineenSuoritus> map = new HashMap<>();
    
    for (CreditStub credit : credits) {
      LukionOppiaineenSuoritus oppiaineenSuoritus = getSubject(student, studentEducationType, credit.getSubject(), studentSubjects, map);

      if (settings.isReportedCredit(credit) && oppiaineenSuoritus != null) {
        LukionKurssinSuoritus kurssiSuoritus = createKurssiSuoritus(credit);
        if (kurssiSuoritus != null)
          oppiaineenSuoritus.addOsasuoritus(kurssiSuoritus);
      }
    }
    
    for (LukionOppiaineenSuoritus lukionOppiaineenSuoritus : map.values()) {
      // Valmiille oppiaineelle on rustattava kokonaisarviointi
      if (oppimaaranSuoritus.getTila().getValue() == SuorituksenTila.VALMIS) {
        ArviointiasteikkoYleissivistava aineKeskiarvo = getSubjectMeanGrade(lukionOppiaineenSuoritus);
        
        if (ArviointiasteikkoYleissivistava.isNumeric(aineKeskiarvo)) {
          KurssinArviointi arviointi = new KurssinArviointiNumeerinen(aineKeskiarvo, student.getStudyEndDate());
          lukionOppiaineenSuoritus.addArviointi(arviointi);
        } else if (ArviointiasteikkoYleissivistava.isNumeric(aineKeskiarvo)) {
          KurssinArviointi arviointi = new KurssinArviointiSanallinen(aineKeskiarvo, student.getStudyEndDate(), kuvaus("Suoritettu/Hylätty"));
          lukionOppiaineenSuoritus.addArviointi(arviointi);
        }
      }
      
      oppimaaranSuoritus.addOsasuoritus(lukionOppiaineenSuoritus);
    }
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
  
  private LukionOppiaineenSuoritus getSubject(Student student, EducationType studentEducationType, Subject subject, StudentSubjectSelections studentSubjects, Map<String, LukionOppiaineenSuoritus> map) {
    String subjectCode = subject.getCode();

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
        String langCode = subjectCode.substring(0, 2);
        Kielivalikoima kieli = Kielivalikoima.valueOf(langCode); // TODO MAPPING! RU=russian, RU=Ruotsi...
        
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
          return null;
        }
      }
    }

    String[] religionSubjects = new String[] { "UE", "UO", "ET" };
    
    if (ArrayUtils.contains(religionSubjects, subjectCode)) {
      if (StringUtils.equals(subjectCode, studentSubjects.getReligion()) || StringUtils.equals(subjectCode, "ET")) {
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
      // TODO Skipped subjects ?? (MUU)
      
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(subjectCode, kuvaus(subject.getName()));
      LukionOppiaineenSuoritusPaikallinen tunniste = new LukionOppiaineenSuoritusPaikallinen(paikallinenKoodi, false, kuvaus(subject.getName()));
      LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste);
      map.put(subjectCode, os);
      return os;
    }
  }

  protected LukionKurssinSuoritus createKurssiSuoritus(CreditStub courseCredit) {
    String kurssiKoodi = courseCredit.getCourseCode();
    LukionKurssinTunniste tunniste;
    
    if (EnumUtils.isValidEnum(LukionKurssit.class, kurssiKoodi)) {
      LukionKurssit kurssi = LukionKurssit.valueOf(kurssiKoodi);
      LukionKurssinTyyppi kurssinTyyppi = LukionKurssinTyyppi.pakollinen; // TODO pakollinen/syventava -> rukseista
      tunniste = new LukionKurssinTunnisteValtakunnallinen(kurssi, kurssinTyyppi);
    } else {
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(kurssiKoodi, kuvaus(courseCredit.getSubject().getName()));
      LukionKurssinTyyppi kurssinTyyppi = LukionKurssinTyyppi.syventava; // TODO paikallinen syventava/soveltava
      tunniste = new LukionKurssinTunnistePaikallinen(paikallinenKoodi , kurssinTyyppi, kuvaus(courseCredit.getCourseName()));
    }
      
    LukionKurssinSuoritus suoritus = new LukionKurssinSuoritus(tunniste, SuorituksenTila.VALMIS);

    // Hyväksilukutieto on hölmössä paikassa; jos kaikki arvosanat ovat hyväksilukuja, tallennetaan 
    // tieto hyväksilukuna - ongelmallista, jos hyväksiluettua kurssia on korotettu 
    if (courseCredit.getCredits().stream().allMatch(credit -> credit.getCreditType() == CreditType.TransferCredit)) {
      OsaamisenTunnustaminen tunnustettu = new OsaamisenTunnustaminen(kuvaus("Hyväksiluku"));
      suoritus.setTunnustettu(tunnustettu);
    }

    for (Credit credit : courseCredit.getCredits()) {
      ArviointiasteikkoYleissivistava arvosana = getArvosana(credit.getGrade());
  
      KurssinArviointi arviointi = null;
      if (ArviointiasteikkoYleissivistava.isNumeric(arvosana)) {
        arviointi =  new KurssinArviointiNumeerinen(arvosana, credit.getDate());
      } else if (ArviointiasteikkoYleissivistava.isLiteral(arvosana)) {
        arviointi = new KurssinArviointiSanallinen(arvosana, credit.getDate(), kuvaus(credit.getGrade().getName()));
      }

      if (arviointi != null)
        suoritus.addArviointi(arviointi);
    }
    
    return suoritus;
  }

}
