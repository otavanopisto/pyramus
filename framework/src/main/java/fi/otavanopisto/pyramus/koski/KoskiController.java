package fi.otavanopisto.pyramus.koski;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.KoskiAikuistenPerusopetuksenStudentHandler;
import fi.otavanopisto.pyramus.koski.model.apa.KoskiAPAStudentHandler;
import fi.otavanopisto.pyramus.koski.model.internetix.KoskiInternetixStudentHandler;
import fi.otavanopisto.pyramus.koski.model.lukio.KoskiLukioStudentHandlerDelegator;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.KoskiInternetixLukioStudentHandler2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.KoskiLukioStudentHandler2019;

@ApplicationScoped
public class KoskiController {

  @Inject
  private Logger logger;
  
  @Inject
  private KoskiSettings settings;
  
  @Inject
  private KoskiAPAStudentHandler apaHandler;

  @Inject
  private KoskiLukioStudentHandlerDelegator lukioHandler;
  
  @Inject
  private KoskiInternetixStudentHandler internetixHandler;

  @Inject
  private KoskiAikuistenPerusopetuksenStudentHandler aikuistenPerusopetuksenHandler;

  @Inject
  private KoskiLukioStudentHandler2019 lukioHandler2019;
  
  @Inject
  private KoskiInternetixLukioStudentHandler2019 internetixLukioHandler2019;
  
  /**
   * Lists student OIDs with the currently active handler for it (i.e. the ones that should currently be in use)
   */
  public Set<String> listStudentOIDs(Student student) {
    KoskiStudyProgrammeHandler handlerType = settings.getStudyProgrammeHandlerType(student.getStudyProgramme().getId());
    KoskiStudentHandler studentHandler = handlerType != null ? getStudentHandler(handlerType) : null;
    if (studentHandler != null) {
      Set<KoskiStudentId> koskiStudentIds = studentHandler.listOids(student);
      return koskiStudentIds.stream()
          .map(koskiStudentId -> koskiStudentId.getOid())
          .collect(Collectors.toSet());
    } else {
      return Collections.emptySet();
    }
  }
  
  public KoskiStudentHandler getStudentHandler(KoskiStudyProgrammeHandler handlerType) {
    switch (handlerType) {
      case aikuistenperusopetus:
        return aikuistenPerusopetuksenHandler;
      case lukio:
        return lukioHandler;
      case aineopiskeluperusopetus:
      case aineopiskelulukio:
        return internetixHandler;
      case aikuistenperusopetuksenalkuvaihe:
        return apaHandler;
      case lukio2019:
        return lukioHandler2019;
      case aineopiskelulukio2019:
        return internetixLukioHandler2019;

      default:
        logger.severe(String.format("Handler for type %s couldn't be determined.", handlerType));
        return null;
    }
  }

  public List<Opiskeluoikeus> studentToOpiskeluoikeus(Student student) {
    KoskiStudyProgrammeHandler handler = settings.getStudyProgrammeHandlerType(student.getStudyProgramme().getId());
    switch (handler) {
      case aikuistenperusopetus:
        return asList(aikuistenPerusopetuksenHandler.studentToModel(student, settings.getAcademyIdentifier(), KoskiStudyProgrammeHandler.aikuistenperusopetus));
      case lukio:
        return asList(lukioHandler.studentToModel(student, settings.getAcademyIdentifier(), KoskiStudyProgrammeHandler.lukio));
      case aineopiskeluperusopetus:
      case aineopiskelulukio:
        return internetixHandler.studentToModel(student, settings.getAcademyIdentifier());
      case aikuistenperusopetuksenalkuvaihe:
        return asList(apaHandler.studentToModel(student, settings.getAcademyIdentifier()));

      default:
        logger.log(Level.WARNING, String.format("Student %d with studyprogramme %s was not reported to Koski because no handler was specified.", 
            student.getId(), student.getStudyProgramme().getName()));
        return null;
    }
  }

  private <T> List<T> asList(T o) {
    List<T> list = new ArrayList<T>();
    list.add(o);
    return list;
  }

}
