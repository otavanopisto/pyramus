package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.koski.KoskiStudentHandler;
import fi.otavanopisto.pyramus.koski.model.Majoitusjakso;
import fi.otavanopisto.pyramus.koski.model.lukio.Maksuttomuus;
import fi.otavanopisto.pyramus.koski.model.lukio.OikeuttaMaksuttomuuteenPidennetty;

public abstract class AbstractAikuistenPerusopetuksenHandler extends KoskiStudentHandler {

  @Inject
  private StudentStudyPeriodDAO studentStudyPeriodDAO;

  protected AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot getLisatiedot(Student student) {
    AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot lisatiedot = new AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot();
    
    lisatiedot.setVuosiluokkiinSitoutumatonOpetus(true);
    
    List<StudentLodgingPeriod> lodgingPeriods = lodgingPeriodDAO.listByStudent(student);
    for (StudentLodgingPeriod lodgingPeriod : lodgingPeriods) {
      lisatiedot.addSisaoppilaitosmainenMajoitus(new Majoitusjakso(lodgingPeriod.getBegin(), lodgingPeriod.getEnd()));
    }
    
    if (!lodgingPeriods.isEmpty()) {
      Date minBeginDate = lodgingPeriods.stream().map(StudentLodgingPeriod::getBegin).filter(Objects::nonNull)
          .min(Comparator.nullsLast(Date::compareTo)).orElse(null);
      
      if (minBeginDate != null) {
        Date maxEndDate = lodgingPeriods.stream().map(StudentLodgingPeriod::getEnd).filter(Objects::nonNull)
            .max(Comparator.nullsLast(Date::compareTo)).orElse(null);
        
        lisatiedot.setOikeusMaksuttomaanAsuntolapaikkaan(new Majoitusjakso(minBeginDate, maxEndDate));
      }
    }
    
    List<StudentStudyPeriod> studyPeriods = studentStudyPeriodDAO.listByStudent(student);

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
