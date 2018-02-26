package fi.otavanopisto.pyramus.koski.model.internetix;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;

import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.KoskiException;
import fi.otavanopisto.pyramus.koski.KoskiStudentHandler;
import fi.otavanopisto.pyramus.koski.KoskiStudentId;
import fi.otavanopisto.pyramus.koski.KoskiStudyProgrammeHandler;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;

public class KoskiInternetixStudentHandler extends KoskiStudentHandler {

  @Inject
  private Logger logger;

  @Inject
  private KoskiInternetixPkStudentHandler pkHandler;
  
  @Inject
  private KoskiInternetixLukioStudentHandler lukioHandler;
  
  public List<Opiskeluoikeus> studentToModel(Student student, String academyIdentifier) throws KoskiException {
    List<Opiskeluoikeus> oos = new ArrayList<>();
    
    Opiskeluoikeus pk = pkHandler.studentToModel(student, academyIdentifier);
    
    Opiskeluoikeus lukio = lukioHandler.studentToModel(student, academyIdentifier);

    if (pk != null) {
      oos.add(pk);
    }
    
    if (lukio != null) {
      oos.add(lukio);
    }

    return oos;
  }

  @Override
  public void saveOrValidateOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    switch (handler) {
      case aineopiskeluperusopetus:
        pkHandler.saveOrValidateOid(handler, student, oid);
      break;
      case aineopiskelulukio:
        lukioHandler.saveOrValidateOid(handler, student, oid);
      break;
      
      default:
        logger.warning("Unknown handler type.");
      break;
    }
  }

  @Override
  public Set<KoskiStudentId> listOids(Student student) {
    return loadInternetixOids(student);
  }

}
