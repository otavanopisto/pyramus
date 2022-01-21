package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.koski.CreditStub;
import fi.otavanopisto.pyramus.koski.CreditStubCredit;
import fi.otavanopisto.pyramus.koski.KoskiConsts;
import fi.otavanopisto.pyramus.koski.KoskiStudentHandler;
import fi.otavanopisto.pyramus.koski.OpiskelijanOPS;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssit;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssitOPS2004Aikuiset;
import fi.otavanopisto.pyramus.koski.model.Laajuus;
import fi.otavanopisto.pyramus.koski.model.Majoitusjakso;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;

/**
 * Kaikille lukion linjoille yhteiset metodit.
 */
public abstract class AbstractKoskiLukioStudentHandler extends KoskiStudentHandler {

  public static final String USERVARIABLE_UNDER18START = KoskiConsts.UserVariables.STARTED_UNDER18;
  public static final String USERVARIABLE_UNDER18STARTREASON = KoskiConsts.UserVariables.UNDER18_STARTREASON;

  @Inject
  private Logger logger;
  
  @Inject
  private StudentStudyPeriodDAO studentStudyPeriodDAO;
  
  protected LukionOpiskeluoikeudenLisatiedot getLisatiedot(Student student) {
    List<StudentStudyPeriod> studyPeriods = studentStudyPeriodDAO.listByStudent(student);
    boolean pidennettyPaattymispaiva = studyPeriods.stream().anyMatch(studyPeriod -> studyPeriod.getPeriodType() == StudentStudyPeriodType.PROLONGED_STUDYENDDATE);
    boolean ulkomainenVaihtoopiskelija = false;
    boolean yksityisopiskelija = settings.isYksityisopiskelija(student.getStudyProgramme().getId());
    boolean oikeusMaksuttomaanAsuntolapaikkaan = settings.isFreeLodging(student.getStudyProgramme().getId());
    LukionOpiskeluoikeudenLisatiedot lisatiedot = new LukionOpiskeluoikeudenLisatiedot(
        pidennettyPaattymispaiva, ulkomainenVaihtoopiskelija, yksityisopiskelija, oikeusMaksuttomaanAsuntolapaikkaan);

    if (StringUtils.equals(userVariableDAO.findByUserAndKey(student, USERVARIABLE_UNDER18START), "1")) {
      String under18startReason = userVariableDAO.findByUserAndKey(student, USERVARIABLE_UNDER18STARTREASON);
      if (StringUtils.isNotBlank(under18startReason)) {
        lisatiedot.setAlle18vuotiaanAikuistenLukiokoulutuksenAloittamisenSyy(kuvaus(under18startReason));
      }
    }
    
    List<StudentLodgingPeriod> lodgingPeriods = lodgingPeriodDAO.listByStudent(student);
    for (StudentLodgingPeriod lodgingPeriod : lodgingPeriods) {
      Majoitusjakso jakso = new Majoitusjakso(lodgingPeriod.getBegin(), lodgingPeriod.getEnd());
      lisatiedot.addSisaoppilaitosmainenMajoitus(jakso);
    }
    
    studyPeriods.sort(Comparator.comparing(StudentStudyPeriod::getBegin));
    
    for (StudentStudyPeriod studyPeriod : studyPeriods) {
      if (studyPeriod.getPeriodType() == StudentStudyPeriodType.COMPULSORY_EDUCATION) {
        // Maksuttoman oppivelvollisuuden piirissä
        lisatiedot.addMaksuttomuus(new Maksuttomuus(studyPeriod.getBegin(), true));
      }
      
      if (studyPeriod.getPeriodType() == StudentStudyPeriodType.NON_COMPULSORY_EDUCATION) {
        // Ei maksuttoman oppivelvollisuuden piirissä
        lisatiedot.addMaksuttomuus(new Maksuttomuus(studyPeriod.getBegin(), false));
      }

      if (studyPeriod.getPeriodType() == StudentStudyPeriodType.EXTENDED_COMPULSORY_EDUCATION) {
        lisatiedot.addOikeuttaMaksuttomuuteenPidennetty(new OikeuttaMaksuttomuuteenPidennetty(studyPeriod.getBegin(), studyPeriod.getEnd()));
      }
    }
    
    return lisatiedot;
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
      
    Laajuus laajuus = kurssinLaajuus(student, courseCredit);
    tunniste.setLaajuus(laajuus);

    LukionKurssinSuoritus suoritus = new LukionKurssinSuoritus(tunniste);

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
