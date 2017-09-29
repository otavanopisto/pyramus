package fi.otavanopisto.pyramus.koski;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
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
import fi.otavanopisto.pyramus.koski.model.Henkilo;
import fi.otavanopisto.pyramus.koski.model.HenkiloOID;
import fi.otavanopisto.pyramus.koski.model.HenkiloUusi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiNumeerinen;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusJakso;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
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

  @Inject 
  private CourseAssessmentDAO courseAssessmentDAO;

  public Oppija studentToModel(Student student, String personOid, String studyOid, String academyIdentifier) {
    OpiskeluoikeudenTyyppi opiskeluoikeudenTyyppi = settings.getOpiskeluoikeudenTyyppi(student.getStudyProgramme().getId());
    StudentSubjectSelections studentSubjects = loadStudentSubjectSelections(student, opiskeluoikeudenTyyppi);
    List<CourseAssessment> courseAssessments = courseAssessmentDAO.listByStudent(student);
    
    Henkilo henkilo;
    if (StringUtils.isNotBlank(personOid))
      henkilo = new HenkiloOID(personOid);
    else
      henkilo = new HenkiloUusi(student.getPerson().getSocialSecurityNumber(), student.getFirstName(), student.getLastName(), student.getNickname());

    LukionOpiskeluoikeus opiskeluoikeus = new LukionOpiskeluoikeus();
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
        suorituksenTila, LukionOppimaara.aikuistenops, Kieli.FI, toimipiste);
    suoritus.getKoulutusmoduuli().setPerusteenDiaarinumero(getDiaarinumero(student));
    opiskeluoikeus.addSuoritus(suoritus);
    
    Oppija oppija = new Oppija();
    oppija.setHenkilo(henkilo);
    oppija.addOpiskeluoikeus(opiskeluoikeus);
    
    EducationType studentEducationType = student.getStudyProgramme() != null && student.getStudyProgramme().getCategory() != null ? 
        student.getStudyProgramme().getCategory().getEducationType() : null;
    assessmentsToModel(studentEducationType, studentSubjects, courseAssessments, suoritus);
    
    return oppija;
  }
  
  private void assessmentsToModel(EducationType studentEducationType, StudentSubjectSelections studentSubjects, List<CourseAssessment> courseAssessments,
      LukionOppimaaranSuoritus suoritus) {
    
    Map<Subject, LukionOppiaineenSuoritus> map = new HashMap<>();
    
    for (CourseAssessment ca : courseAssessments) {
      Subject subject = (ca.getCourseStudent() != null && ca.getCourseStudent().getCourse() != null) ?
          ca.getCourseStudent().getCourse().getSubject() : null;
      
      LukionOppiaineenSuoritus oppiaineenSuoritus = getSubject(studentEducationType, subject, studentSubjects, map);

      if (settings.isReportedCredit(ca) && oppiaineenSuoritus != null)
        oppiaineenSuoritus.addOsasuoritus(createKurssiSuoritus(ca));
    }
    
    map.values().forEach(lukionOppiaineenSuoritus -> suoritus.addOsasuoritus(lukionOppiaineenSuoritus));
  }

  private LukionOppiaineenSuoritus getSubject(EducationType studentEducationType, Subject subject, StudentSubjectSelections studentSubjects, Map<Subject, LukionOppiaineenSuoritus> map) {
    if (map.containsKey(subject))
      return map.get(subject);
    
    boolean matchingEducationType = studentEducationType != null && subject.getEducationType() != null && 
        studentEducationType.getId().equals(subject.getEducationType().getId());
    String subjectCode = subject.getCode();
    
    if (matchingEducationType && (StringUtils.equals(subjectCode, "MAA") || StringUtils.equals(subjectCode, "MAB"))) {
      if (StringUtils.equals(subjectCode, studentSubjects.getMath())) {
        LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusMatematiikka(OppiaineMatematiikka.valueOf(subjectCode), true); // TODO pakollinen
        SuorituksenTila tila = SuorituksenTila.KESKEN; // TODO tila
        
        LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste, tila);
        map.put(subject, os);
        return os;
      } else
        return null;
    }
    if (matchingEducationType && StringUtils.equals(subjectCode, "MAY")) {
      LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusMatematiikka(OppiaineMatematiikka.MAY, true); // TODO pakollinen
      SuorituksenTila tila = SuorituksenTila.KESKEN; // TODO tila
      
      LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste, tila);
      map.put(subject, os);
      return os;
    }
    
    
    if (matchingEducationType && StringUtils.equals(subjectCode, "ÄI")) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusAidinkieli(OppiaineAidinkieliJaKirjallisuus.AI1, true); // TODO pakollinen
        SuorituksenTila tila = SuorituksenTila.KESKEN; // TODO tila
        
        LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste, tila);
        map.put(subject, os);
        return os;
      } else
        return null;
    }
    if (matchingEducationType && StringUtils.equals(subjectCode, "S2")) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusAidinkieli(OppiaineAidinkieliJaKirjallisuus.AI7, true); // TODO pakollinen
        SuorituksenTila tila = SuorituksenTila.KESKEN; // TODO tila
        
        LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste, tila);
        map.put(subject, os);
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
          
          LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusVierasKieli(valinta, kieli, true); // TODO pakollinen
          SuorituksenTila tila = SuorituksenTila.KESKEN; // TODO tila
          
          LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste, tila);
          map.put(subject, os);
          return os;
        } else {
          logger.log(Level.SEVERE, String.format("Koski: Language code %s could not be converted to an enum.", langCode));
          return null;
        }
      }
    }

    if (matchingEducationType && EnumUtils.isValidEnum(KoskiOppiaineetYleissivistava.class, StringUtils.upperCase(subjectCode))) {
      // Common national subject
      
      KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.valueOf(StringUtils.upperCase(subject.getCode()));
      LukionOppiaineenTunniste tunniste = new LukionOppiaineenSuoritusMuuValtakunnallinen(kansallinenAine, true); // TODO pakollinen
      SuorituksenTila tila = SuorituksenTila.KESKEN; // TODO tila
      
      LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste, tila);
      map.put(subject, os);
      return os;
    } else {
      // Other local subject
      // TODO Skipped subjects ?? (MUU)
      
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(subject.getCode(), kuvaus(subject.getName()));
      LukionOppiaineenSuoritusPaikallinen tunniste = new LukionOppiaineenSuoritusPaikallinen(paikallinenKoodi, true, kuvaus(subject.getName())); // TODO pakollinen
      SuorituksenTila tila = SuorituksenTila.KESKEN; // TODO tila
      
      LukionOppiaineenSuoritus os = new LukionOppiaineenSuoritus(tunniste, tila);
      map.put(subject, os);
      return os;
    }
  }

  protected LukionKurssinSuoritus createKurssiSuoritus(CourseAssessment ca) {
    Course course = (ca.getCourseStudent() != null) ?
        ca.getCourseStudent().getCourse() : null;
    Subject subject = (ca.getCourseStudent() != null && ca.getCourseStudent().getCourse() != null) ?
        ca.getCourseStudent().getCourse().getSubject() : null;
        
    String kurssiKoodi = subject.getCode() + course.getCourseNumber().toString();
    ArviointiasteikkoYleissivistava arvosana = getArvosana(ca.getGrade());

    KurssinArviointi arviointi = new KurssinArviointiNumeerinen(arvosana, ca.getDate()); // TODO sanallinen arviointi
    LukionKurssinTunniste tunniste;
    
    if (EnumUtils.isValidEnum(LukionKurssit.class, kurssiKoodi)) {
      LukionKurssit kurssi = LukionKurssit.valueOf(kurssiKoodi);
      LukionKurssinTyyppi kurssinTyyppi = LukionKurssinTyyppi.pakollinen; // TODO pakollinen/syventava
      tunniste = new LukionKurssinTunnisteValtakunnallinen(kurssi, kurssinTyyppi); // TODO vk/paikallinen
    } else {
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(kurssiKoodi, kuvaus(subject.getName()));
      LukionKurssinTyyppi kurssinTyyppi = LukionKurssinTyyppi.soveltava; // TODO syventava/soveltava
      tunniste = new LukionKurssinTunnistePaikallinen(paikallinenKoodi , kurssinTyyppi, kuvaus(course.getName()));
    }
      
    LukionKurssinSuoritus suoritus = new LukionKurssinSuoritus(tunniste, SuorituksenTila.VALMIS);
    suoritus.addArviointi(arviointi);
    return suoritus;
  }

}
