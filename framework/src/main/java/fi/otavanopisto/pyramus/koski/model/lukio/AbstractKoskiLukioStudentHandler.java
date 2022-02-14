package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.koski.KoskiStudentHandler;
import fi.otavanopisto.pyramus.koski.model.Majoitusjakso;

/**
 * Kaikille lukion linjoille yhteiset metodit.
 */
public abstract class AbstractKoskiLukioStudentHandler extends KoskiStudentHandler {

  @Inject
  private StudentStudyPeriodDAO studentStudyPeriodDAO;
  
  protected LukionOpiskeluoikeudenLisatiedot getLisatiedot(Student student) {
    List<StudentStudyPeriod> studyPeriods = studentStudyPeriodDAO.listByStudent(student);
    boolean pidennettyPaattymispaiva = studyPeriods.stream().anyMatch(studyPeriod -> studyPeriod.getPeriodType() == StudentStudyPeriodType.PROLONGED_STUDYENDDATE);
    boolean ulkomainenVaihtoopiskelija = false;
    LukionOpiskeluoikeudenLisatiedot lisatiedot = new LukionOpiskeluoikeudenLisatiedot(
        pidennettyPaattymispaiva, ulkomainenVaihtoopiskelija);

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
  
}
