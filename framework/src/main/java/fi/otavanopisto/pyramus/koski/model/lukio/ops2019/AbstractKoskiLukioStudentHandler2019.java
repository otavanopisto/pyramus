package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
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
import fi.otavanopisto.pyramus.koski.CreditStub;
import fi.otavanopisto.pyramus.koski.CreditStubCredit;
import fi.otavanopisto.pyramus.koski.OpiskelijanOPS;
import fi.otavanopisto.pyramus.koski.OppiaineenSuoritusWithSubject;
import fi.otavanopisto.pyramus.koski.StudentSubjectSelections;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.LukionMuutOpinnot;
import fi.otavanopisto.pyramus.koski.koodisto.ModuuliKoodistoLOPS2021;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenLaajuusYksikko;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineMatematiikka;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.Laajuus;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;
import fi.otavanopisto.pyramus.koski.model.lukio.AbstractKoskiLukioStudentHandler;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenArviointi;

public abstract class AbstractKoskiLukioStudentHandler2019 extends AbstractKoskiLukioStudentHandler {

  // KU, LI, MU, TE kuuluisivat listaan, mutta ilmoitetaan paikallisina
  private static final EnumSet<KoskiOppiaineetYleissivistava> VALTAKUNNALLISETOPPIAINEET2019 = EnumSet.of(
      KoskiOppiaineetYleissivistava.BI,
      KoskiOppiaineetYleissivistava.ET,
      KoskiOppiaineetYleissivistava.FI,
      KoskiOppiaineetYleissivistava.FY,
      KoskiOppiaineetYleissivistava.GE,
      KoskiOppiaineetYleissivistava.HI,
      KoskiOppiaineetYleissivistava.KE,
      KoskiOppiaineetYleissivistava.OP,
      KoskiOppiaineetYleissivistava.PS,
      KoskiOppiaineetYleissivistava.YH
  );

  @Inject
  private Logger logger;

  protected StudentSubjectSelections getDefaultSubjectSelections() {
    StudentSubjectSelections studentSubjects = new StudentSubjectSelections();
    studentSubjects.setMath("MAB");
    studentSubjects.setPrimaryLanguage("ÄI");
    studentSubjects.setReligion("UE");
    return studentSubjects;
  }

  protected Set<LukionOsasuoritus2019> assessmentsToModel(OpiskelijanOPS ops, Student student, EducationType studentEducationType, StudentSubjectSelections studentSubjects, boolean calculateMeanGrades) {
    Collection<CreditStub> credits = listCredits(student, true, true, ops, credit -> matchingCurriculumFilter(student, credit));
    Set<LukionOsasuoritus2019> results = new HashSet<>();
    
    Map<String, OppiaineenSuoritusWithSubject<LukionOsasuoritus2019>> map = new HashMap<>();
    Set<OppiaineenSuoritusWithSubject<LukionOsasuoritus2019>> accomplished = new HashSet<>();
    
    for (CreditStub credit : credits) {
      OppiaineenSuoritusWithSubject<LukionOsasuoritus2019> oppiaineenSuoritusWSubject = getSubject(student, ops, studentEducationType, credit.getSubject(), studentSubjects, map);
      collectAccomplishedMarks(credit.getSubject(), oppiaineenSuoritusWSubject, studentSubjects, accomplished);
      
      if (settings.isReportedCredit(credit) && oppiaineenSuoritusWSubject != null) {
        LukionOpintojaksonSuoritus2019 kurssiSuoritus = createKurssiSuoritus(student, studentSubjects, oppiaineenSuoritusWSubject.isPaikallinenOppiaine(), ops, credit);
        if (kurssiSuoritus != null) {
          oppiaineenSuoritusWSubject.getOppiaineenSuoritus().addOsasuoritus(kurssiSuoritus);
        } else {
          logger.warning(String.format("Course %s not reported for student %d due to unresolvable credit.", credit.getCourseCode(), student.getId()));
          koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNREPORTED_CREDIT, new Date(), credit.getCourseCode());
        }
      }
    }
    
    for (OppiaineenSuoritusWithSubject<LukionOsasuoritus2019> lukionOppiaineenSuoritusWSubject : map.values()) {
      LukionOsasuoritus2019 lukionOsaSuoritus = lukionOppiaineenSuoritusWSubject.getOppiaineenSuoritus();
      if (CollectionUtils.isEmpty(lukionOsaSuoritus.getOsasuoritukset())) {
        // Skip empty subjects
        continue;
      }
      
      Subject subject = lukionOppiaineenSuoritusWSubject.getSubject();
      
      // Valmiille oppiaineelle on rustattava kokonaisarviointi

      if (lasketaankoAineKeskiarvo(calculateMeanGrades, student, subject) && (lukionOsaSuoritus instanceof LukionOppiaineenSuoritus2019)) {
        LukionOppiaineenSuoritus2019 lukionOppiaineenSuoritus = (LukionOppiaineenSuoritus2019) lukionOsaSuoritus;
        ArviointiasteikkoYleissivistava aineKeskiarvo = accomplished.contains(lukionOppiaineenSuoritusWSubject) ? 
            ArviointiasteikkoYleissivistava.GRADE_S : getSubjectMeanGrade(student, subject, lukionOsaSuoritus);
        
        if (aineKeskiarvo != null) {
          LukionOppiaineenArviointi arviointi = new LukionOppiaineenArviointi(aineKeskiarvo, student.getStudyEndDate());
          lukionOppiaineenSuoritus.addArviointi(arviointi);
        } else {
          logger.warning(String.format("Unresolved mean grade for person %d.", student.getPerson().getId()));
        }
      }
      
      results.add(lukionOsaSuoritus);
    }
    
    return results;
  }

  private ArviointiasteikkoYleissivistava getSubjectMeanGrade(Student student, Subject subject, LukionOsasuoritus2019 oppiaineenSuoritus) {
    // Jos aineesta on annettu korotettu arvosana, käytetään automaattisesti sitä
    ArviointiasteikkoYleissivistava korotettuArvosana = getSubjectGrade(student, subject);
    if (korotettuArvosana != null) {
      return korotettuArvosana;
    } else {
      List<PainotettuArvosana> kurssiarvosanat = new ArrayList<>();
      EnumSet<OpintojenLaajuusYksikko> yksikot = EnumSet.noneOf(OpintojenLaajuusYksikko.class);
      
      for (LukionOpintojaksonSuoritus2019 kurssinSuoritus : oppiaineenSuoritus.getOsasuoritukset()) {
        if (kurssinSuoritus.getKoulutusmoduuli() != null && kurssinSuoritus.getKoulutusmoduuli().getLaajuus() != null) {
          List<KurssinArviointi> arvioinnit = kurssinSuoritus.getArviointi();
          Laajuus laajuus = kurssinSuoritus.getKoulutusmoduuli().getLaajuus();

          OpintojenLaajuusYksikko laajuusYksikko = laajuus.getYksikko().getValue();
          if (yksikot.isEmpty()) {
            yksikot.add(laajuusYksikko);
          } else {
            if (!yksikot.contains(laajuusYksikko)) {
              koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.CONFLICTING_COURSELENGTH, new Date(), subject != null ? subject.getCode() : "?");
            }
          }
          
          int kurssinLaajuus = laajuus.getArvo();
          
          Set<ArviointiasteikkoYleissivistava> arvosanat = arvioinnit.stream().map(arviointi -> arviointi.getArvosana().getValue()).collect(Collectors.toSet());
          ArviointiasteikkoYleissivistava kurssiArvosana = ArviointiasteikkoYleissivistava.bestGrade(arvosanat);
          kurssiarvosanat.add(new PainotettuArvosana(kurssinLaajuus, kurssiArvosana));
        } else {
          // Yleisvirhe, jonka ei pitäisi koskaan toteutua tai jotain on mennyt valmistelussa pieleen
          koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.GENERIC_ERROR, new Date(), "1001");
        }
      }
      
      return ArviointiasteikkoYleissivistava.weightedMeanGrade(kurssiarvosanat);
    }
  }

  private boolean isMathSubject(String subjectCode) {
    return StringUtils.equals(subjectCode, "MAA") || StringUtils.equals(subjectCode, "MAB") || StringUtils.equals(subjectCode, "MAY");
  }
  
  private OppiaineenSuoritusWithSubject<LukionOsasuoritus2019> getSubject(Student student, OpiskelijanOPS ops, EducationType studentEducationType,
      Subject subject, StudentSubjectSelections studentSubjects, Map<String, OppiaineenSuoritusWithSubject<LukionOsasuoritus2019>> map) {
    String subjectCode = subjectCode(subject);

    if (map.containsKey(subjectCode))
      return map.get(subjectCode);
    
    boolean matchingEducationType = studentEducationType != null && subject.getEducationType() != null && 
        studentEducationType.getId().equals(subject.getEducationType().getId());
    
    // Aineet, joista lukiodiplomit voi suorittaa
    List<String> lukioDiplomit = Arrays.asList(new String[] { "KOLD", "KULD", "KÄLD", "LILD", "MELD", "MULD", "TALD", "TELD" });

    if (lukioDiplomit.contains(subjectCode)) {
      if (map.containsKey("LD")) {
        return map.get("LD");
      }

      LukionMuidenOpintojenTunniste2019 tunniste = new LukionMuidenOpintojenTunniste2019(LukionMuutOpinnot.LD);
      LukionOsasuoritus2019 mo = new MuidenLukioOpintojenSuoritus2019(tunniste);
      OppiaineenSuoritusWithSubject<LukionOsasuoritus2019> os = new OppiaineenSuoritusWithSubject<>(subject, false, mo);
      map.put("LD", os);
      return os;
    }
    
    // MATHEMATICS
    if (matchingEducationType && isMathSubject(subjectCode)) {
      if (StringUtils.equals(subjectCode, "MAY") && isMathSubject(studentSubjects.getMath())) {
        // MAY is mapped to either MAB/MAA unless neither is specified
        subjectCode = studentSubjects.getMath();
        if (map.containsKey(subjectCode)) {
          return map.get(subjectCode);
        }
      }
      
      if (StringUtils.equals(subjectCode, studentSubjects.getMath())) {
        LukionOppiaineenTunniste2019 tunniste = new LukionOppiaineenSuoritusMatematiikka2019(
            OppiaineMatematiikka.valueOf(subjectCode), isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.MA));
        return mapSubject(subject, subjectCode, false, tunniste, map);
      } else
        return null;
    }
    
    if (matchingEducationType && StringUtils.equals(subjectCode, "ÄI")) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        LukionOppiaineenTunniste2019 tunniste = new LukionOppiaineenSuoritusAidinkieli2019(
            OppiaineAidinkieliJaKirjallisuus.AI1, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.AI));
        return mapSubject(subject, subjectCode, false, tunniste, map);
      } else
        return null;
    }
    if (matchingEducationType && StringUtils.equals(subjectCode, "S2")) {
      if (StringUtils.equals(subjectCode, studentSubjects.getPrimaryLanguage())) {
        LukionOppiaineenTunniste2019 tunniste = new LukionOppiaineenSuoritusAidinkieli2019(
            OppiaineAidinkieliJaKirjallisuus.AI7, isPakollinenOppiaine(student, KoskiOppiaineetYleissivistava.AI));
        return mapSubject(subject, subjectCode, false, tunniste, map);
      } else
        return null;
    }
    
    if (matchingEducationType && studentSubjects.isAdditionalLanguage(subjectCode)) {
      if (subjectCode.length() > 2) {
        String langCode = settings.getSubjectToLanguageMapping(subjectCode.substring(0, 2).toUpperCase());
        Kielivalikoima kieli = Kielivalikoima.valueOf(langCode);
        
        if (kieli != null) {
          KoskiOppiaineetYleissivistava valinta = studentSubjects.koskiKoodi(ops, subjectCode);          
          LukionOppiaineenTunniste2019 tunniste = new LukionOppiaineenSuoritusVierasKieli2019(valinta, kieli, 
              isPakollinenOppiaine(student, valinta));
          return mapSubject(subject, subjectCode, false, tunniste, map);
        } else {
          logger.log(Level.SEVERE, String.format("Koski: Language code %s could not be converted to an enum.", langCode));
          koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNKNOWN_LANGUAGE, new Date(), langCode);
          return null;
        }
      }
    }

    String[] religionSubjects = new String[] { "UE", "UO", "UI" };
    
    if (matchingEducationType && ArrayUtils.contains(religionSubjects, subjectCode)) {
      // Only the religion that student has selected is reported
      if (StringUtils.equals(subjectCode, studentSubjects.getReligion())) {
        if (map.containsKey("KT"))
          return map.get("KT");
        
        KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.KT;
        LukionOppiaineenTunniste2019 tunniste = new LukionOppiaineenSuoritusMuuValtakunnallinen2019(kansallinenAine, isPakollinenOppiaine(student, kansallinenAine));
        return mapSubject(subject, "KT", false, tunniste, map);
      } else
        return null;
    }
    
    if (matchingEducationType && isValtakunnallinenOppiaine2019(subjectCode)) {
      // Common national subject

      KoskiOppiaineetYleissivistava kansallinenAine = KoskiOppiaineetYleissivistava.valueOf(StringUtils.upperCase(subjectCode));
      LukionOppiaineenTunniste2019 tunniste = new LukionOppiaineenSuoritusMuuValtakunnallinen2019(kansallinenAine, isPakollinenOppiaine(student, kansallinenAine));
      return mapSubject(subject, subjectCode, false, tunniste, map);
    } else {
      // Other local subject
      
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(subjectCode, kuvaus(subject.getName()));
      LukionOppiaineenSuoritusPaikallinen2019 tunniste = new LukionOppiaineenSuoritusPaikallinen2019(paikallinenKoodi, false, kuvaus(subject.getName()));
      return mapSubject(subject, subjectCode, true, tunniste, map);
    }
  }

  protected boolean isValtakunnallinenOppiaine2019(String subjectCode) {
    String subjectCodeUpper = StringUtils.upperCase(subjectCode);
    
    return EnumUtils.isValidEnum(KoskiOppiaineetYleissivistava.class, subjectCodeUpper)
      ? VALTAKUNNALLISETOPPIAINEET2019.contains(KoskiOppiaineetYleissivistava.valueOf(subjectCodeUpper))
      : false;
  }

  private OppiaineenSuoritusWithSubject<LukionOsasuoritus2019> mapSubject(Subject subject, String subjectCode, boolean paikallinenOppiaine, LukionOppiaineenTunniste2019 tunniste, Map<String, OppiaineenSuoritusWithSubject<LukionOsasuoritus2019>> map) {
    // TODO
    boolean suoritettuErityisenäTutkintona = false;
    LukionOppiaineenSuoritus2019 lukionOppiaineenSuoritus = new LukionOppiaineenSuoritus2019(tunniste, suoritettuErityisenäTutkintona);
    OppiaineenSuoritusWithSubject<LukionOsasuoritus2019> os = new OppiaineenSuoritusWithSubject<>(subject, paikallinenOppiaine, lukionOppiaineenSuoritus);
    map.put(subjectCode, os);
    return os;
  }
  
  protected LukionOpintojaksonSuoritus2019 createKurssiSuoritus(Student student, StudentSubjectSelections studentSubjects, boolean paikallinenOppiaine, OpiskelijanOPS ops, CreditStub courseCredit) {
    String kurssiKoodi = courseCredit.getCourseCode();
    LukionOpintojaksonTunniste2019 tunniste;

    String subjectCode = subjectCode(courseCredit.getSubject());

    Laajuus laajuus = kurssinLaajuus(student, courseCredit);
    
    if (laajuus != null && laajuus.getYksikko() != null && laajuus.getYksikko().getValue() != OpintojenLaajuusYksikko.op) {
      koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.LUKIO2019_CREDIT_NOT_IN_POINTS, new Date(), kurssiKoodi);
    }
    
    SuorituksenTyyppi suorituksenTyyppi;

    if (!paikallinenOppiaine && EnumUtils.isValidEnum(ModuuliKoodistoLOPS2021.class, kurssiKoodi)) {
      /**
       * Tässä yhteydessä:
       * * pakollinen => valtakunnallinen pakollinen
       * * syventava => valtakunnallinen valinnainen
       */
      LukionKurssinTyyppi kurssinTyyppi = findCourseType(student, courseCredit, true, LukionKurssinTyyppi.pakollinen, LukionKurssinTyyppi.syventava);
      boolean pakollinen = kurssinTyyppi == LukionKurssinTyyppi.pakollinen;

      suorituksenTyyppi = SuorituksenTyyppi.lukionvaltakunnallinenmoduuli;
      ModuuliKoodistoLOPS2021 kurssi = ModuuliKoodistoLOPS2021.valueOf(kurssiKoodi);

      if (studentSubjects.isAdditionalLanguage(subjectCode)) {
        tunniste = new LukionOpintojaksonTunnisteVierasKieli2019(kurssi, laajuus, pakollinen);
      } else {
        tunniste = new LukionOpintojaksonTunnisteMuuModuuli2019(kurssi, laajuus, pakollinen);
      }
    } else {
      /**
       * Tässä yhteydessä:
       * * syventava => paikallinen pakollinen
       * * soveltava => paikallinen valinnainen
       */
      LukionKurssinTyyppi kurssinTyyppi = findCourseType(student, courseCredit, false, LukionKurssinTyyppi.syventava, LukionKurssinTyyppi.soveltava);
      boolean pakollinen = kurssinTyyppi == LukionKurssinTyyppi.pakollinen;

      suorituksenTyyppi = SuorituksenTyyppi.lukionpaikallinenopintojakso;
      PaikallinenKoodi paikallinenKoodi = new PaikallinenKoodi(kurssiKoodi, kuvaus(courseCredit.getSubject().getName()));
      tunniste = new LukionOpintojaksonTunnistePaikallinen2019(paikallinenKoodi, laajuus, pakollinen, kuvaus(courseCredit.getCourseName()));
    }
      
    LukionOpintojaksonSuoritus2019 suoritus = new LukionOpintojaksonSuoritus2019(tunniste, suorituksenTyyppi);

    return luoKurssiSuoritus(suoritus, courseCredit);
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
            String mappedValue = settings.getCourseTypeMapping2019(educationSubTypeId);
            if (mappedValue != null && EnumUtils.isValidEnum(LukionKurssinTyyppi.class, mappedValue)) {
              resolvedTypes.add(LukionKurssinTyyppi.valueOf(mappedValue));
            }
          }
        } else {
          logger.warning(String.format("CourseAssessment %d has no courseStudent or Course", courseAssessment.getId()));
        }
      } else if (credit.getCredit() instanceof TransferCredit) {
        TransferCredit transferCredit = (TransferCredit) credit.getCredit();
        if (national && transferCredit.getOptionality() == CourseOptionality.MANDATORY) {
          resolvedTypes.add(LukionKurssinTyyppi.pakollinen);
        } else {
          // TODO Is this correct?
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
      koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNRESOLVABLE_SUBTYPES, new Date(), courseCredit.getCourseCode());
      return allowedValues[0];
    } else if (allowedSet.size() == 1) {
      return allowedSet.iterator().next();
    } else {
      for (LukionKurssinTyyppi type : allowedValues) {
        if (allowedSet.contains(type)) {
          logger.warning(String.format("Course %s has several matching subtypes.", courseCredit.getCourseCode()));
          koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.UNRESOLVABLE_SUBTYPES, new Date(), courseCredit.getCourseCode());
          return type;
        }
      }
    }
    
    return allowedValues[0];
  }

}
