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
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenKurssit2015;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenPaattovaiheenKurssit2017;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
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
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinSuoritus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunniste;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnisteOPS2015;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnistePV2017;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnistePaikallinen;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusMuu;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppimaaranSuoritus;

public class KoskiAikuistenPerusopetuksenStudentHandler extends KoskiStudentHandler {

  @Inject
  private Logger logger;

  @Inject 
  private CourseAssessmentDAO courseAssessmentDAO;

  public Oppija studentToModel(Student student, String personOid, String studyOid, String academyIdentifier) {
    OpiskeluoikeudenTyyppi opiskeluoikeudenTyyppi = settings.getOpiskeluoikeudenTyyppi(student.getStudyProgramme().getId());
    StudentSubjectSelections studentSubjects = loadStudentSubjectSelections(student, opiskeluoikeudenTyyppi);
    List<CourseAssessment> courseAssessments = courseAssessmentDAO.listByStudent(student);

    AikuisOpiskelijanOPS ops = resoveOPS(student);
    
    Henkilo henkilo;
    if (StringUtils.isNotBlank(personOid))
      henkilo = new HenkiloOID(personOid);
    else
      henkilo = new HenkiloUusi(student.getPerson().getSocialSecurityNumber(), student.getFirstName(), student.getLastName(), student.getNickname());
    
    AikuistenPerusopetuksenOpiskeluoikeus opiskeluoikeus = new AikuistenPerusopetuksenOpiskeluoikeus();
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

      suorituksenTila  = ArrayUtils.contains(OpiskeluoikeudenTila.GRADUATED_STATES, opintojenLopetusTila) ? 
          SuorituksenTila.VALMIS : SuorituksenTila.KESKEYTYNYT;
    }
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(academyIdentifier);
    AikuistenPerusopetuksenOppimaaranSuoritus suoritus = new AikuistenPerusopetuksenOppimaaranSuoritus(
        suorituksenTila, PerusopetuksenSuoritusTapa.koulutus, Kieli.FI, toimipiste);
    suoritus.getKoulutusmoduuli().setPerusteenDiaarinumero(getDiaarinumero(student));
    opiskeluoikeus.addSuoritus(suoritus);
    
    Oppija oppija = new Oppija();
    oppija.setHenkilo(henkilo);
    oppija.addOpiskeluoikeus(opiskeluoikeus);
    
    EducationType studentEducationType = student.getStudyProgramme() != null && student.getStudyProgramme().getCategory() != null ? 
        student.getStudyProgramme().getCategory().getEducationType() : null;
    assessmentsToModel(ops, studentEducationType , studentSubjects, courseAssessments, suoritus);
    
    return oppija;
  }
  
  private AikuisOpiskelijanOPS resoveOPS(Student student) {
    Curriculum curriculum = student.getCurriculum();
    if (curriculum != null) {
      switch (curriculum.getId().intValue()) {
        case 1:
          return AikuisOpiskelijanOPS.ops2016;
        case 2:
          return AikuisOpiskelijanOPS.ops2005;
        case 3:
          return AikuisOpiskelijanOPS.ops2018;
      }
    }
    return null;
  }

  private void assessmentsToModel(AikuisOpiskelijanOPS ops, EducationType studentEducationType, StudentSubjectSelections studentSubjects, 
      List<CourseAssessment> courseAssessments, AikuistenPerusopetuksenOppimaaranSuoritus suoritus) {
    
    Map<Subject, AikuistenPerusopetuksenOppiaineenSuoritus> map = new HashMap<>();
    
    for (CourseAssessment ca : courseAssessments) {
      Subject subject = (ca.getCourseStudent() != null && ca.getCourseStudent().getCourse() != null) ?
          ca.getCourseStudent().getCourse().getSubject() : null;
      
      AikuistenPerusopetuksenOppiaineenSuoritus oppiaineenSuoritus = getSubject(studentEducationType, subject, studentSubjects, map);

      if (settings.isReportedCredit(ca) && oppiaineenSuoritus != null)
        oppiaineenSuoritus.addOsasuoritus(createKurssiSuoritus(ops, ca));
    }
    
    map.values().forEach(lukionOppiaineenSuoritus -> suoritus.addOsasuoritus(lukionOppiaineenSuoritus));
  }

  private AikuistenPerusopetuksenOppiaineenSuoritus getSubject(EducationType studentEducationType, Subject subject, StudentSubjectSelections studentSubjects, Map<Subject, AikuistenPerusopetuksenOppiaineenSuoritus> map) {
    if (map.containsKey(subject))
      return map.get(subject);
    
    String subjectCode = subject.getCode();
    
    boolean matchingEducationType = studentEducationType != null && subject.getEducationType() != null && 
        studentEducationType.getId().equals(subject.getEducationType().getId());
    
    if (matchingEducationType && (StringUtils.equals(subjectCode, "äi") || StringUtils.equals(subjectCode, "s2"))) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        OppiaineAidinkieliJaKirjallisuus aine = StringUtils.equals(subjectCode, "s2") ? 
            OppiaineAidinkieliJaKirjallisuus.AI7 :  // s2
              OppiaineAidinkieliJaKirjallisuus.AI1; // äi
        
        AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli(aine, true); // TODO pakollinen
        SuorituksenTila tila = SuorituksenTila.KESKEN; // TODO tila
        
        AikuistenPerusopetuksenOppiaineenSuoritus os = new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste, tila);
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
          
          AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli(valinta, kieli, true); // TODO pakollinen
          SuorituksenTila tila = SuorituksenTila.KESKEN; // TODO tila
          
          AikuistenPerusopetuksenOppiaineenSuoritus os = new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste, tila);
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
      AikuistenPerusopetuksenOppiaineenTunniste tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusMuu(kansallinenAine, true); // TODO pakollinen
      SuorituksenTila tila = SuorituksenTila.KESKEN; // TODO tila
      
      AikuistenPerusopetuksenOppiaineenSuoritus os = new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste, tila);
      map.put(subject, os);
      return os;
    } else {
      // Other local subject
      // TODO Skipped subjects ?? (MUU)
      
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(subject.getCode(), kuvaus(subject.getName()));
      AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen tunniste = new AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen(paikallinenKoodi, true, kuvaus(subject.getName())); // TODO pakollinen
      SuorituksenTila tila = SuorituksenTila.KESKEN; // TODO tila
      
      AikuistenPerusopetuksenOppiaineenSuoritus os = new AikuistenPerusopetuksenOppiaineenSuoritus(tunniste, tila);
      map.put(subject, os);
      return os;
    }
  }

  protected AikuistenPerusopetuksenKurssinSuoritus createKurssiSuoritus(AikuisOpiskelijanOPS ops, CourseAssessment ca) {
    Course course = (ca.getCourseStudent() != null) ?
        ca.getCourseStudent().getCourse() : null;
    Subject subject = (ca.getCourseStudent() != null && ca.getCourseStudent().getCourse() != null) ?
        ca.getCourseStudent().getCourse().getSubject() : null;
        
    String kurssiKoodi = StringUtils.upperCase(subject.getCode() + course.getCourseNumber().toString());
    ArviointiasteikkoYleissivistava arvosana = getArvosana(ca.getGrade());

    KurssinArviointi arviointi = new KurssinArviointiNumeerinen(arvosana, ca.getDate()); // TODO sanallinen arviointi
    AikuistenPerusopetuksenKurssinTunniste tunniste;
    
    if (ops == AikuisOpiskelijanOPS.ops2016 && EnumUtils.isValidEnum(AikuistenPerusopetuksenKurssit2015.class, kurssiKoodi)) {
      AikuistenPerusopetuksenKurssit2015 kurssi = AikuistenPerusopetuksenKurssit2015.valueOf(kurssiKoodi);
      tunniste = new AikuistenPerusopetuksenKurssinTunnisteOPS2015(kurssi);
    } else if (ops == AikuisOpiskelijanOPS.ops2018 && EnumUtils.isValidEnum(AikuistenPerusopetuksenPaattovaiheenKurssit2017.class, kurssiKoodi)) {
      AikuistenPerusopetuksenPaattovaiheenKurssit2017 kurssi = AikuistenPerusopetuksenPaattovaiheenKurssit2017.valueOf(kurssiKoodi);
      tunniste = new AikuistenPerusopetuksenKurssinTunnistePV2017(kurssi);
    } else {
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(kurssiKoodi, kuvaus(subject.getName()));
      tunniste = new AikuistenPerusopetuksenKurssinTunnistePaikallinen(paikallinenKoodi);
    }
      
    AikuistenPerusopetuksenKurssinSuoritus suoritus = new AikuistenPerusopetuksenKurssinSuoritus(tunniste, SuorituksenTila.VALMIS);
    suoritus.addArviointi(arviointi);
    return suoritus;
  }

  enum AikuisOpiskelijanOPS {
    ops2005,
    ops2016,
    ops2018
  }
  
}
