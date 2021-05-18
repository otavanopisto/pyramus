package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.koski.KoskiConsts;
import fi.otavanopisto.pyramus.koski.KoskiStudentHandler;
import fi.otavanopisto.pyramus.koski.model.Majoitusjakso;

/**
 * Kaikille lukion linjoille yhteiset metodit.
 */
public abstract class AbstractKoskiLukioStudentHandler extends KoskiStudentHandler {

  public static final String USERVARIABLE_UNDER18START = KoskiConsts.UserVariables.STARTED_UNDER18;
  public static final String USERVARIABLE_UNDER18STARTREASON = KoskiConsts.UserVariables.UNDER18_STARTREASON;

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
      if (studyPeriod.getPeriodType() == StudentStudyPeriodType.EXTENDED_COMPULSORY_EDUCATION) {
        lisatiedot.addOikeuttaMaksuttomuuteenPidennetty(new OikeuttaMaksuttomuuteenPidennetty(studyPeriod.getBegin(), studyPeriod.getEnd()));
      }
    }
    
    return lisatiedot;
  }
  
}
