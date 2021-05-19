package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.Set;

import javax.inject.Inject;

import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.KoskiStudentHandler;
import fi.otavanopisto.pyramus.koski.KoskiStudentId;
import fi.otavanopisto.pyramus.koski.KoskiStudyProgrammeHandler;
import fi.otavanopisto.pyramus.koski.OpiskelijanOPS;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.KoskiLukioStudentHandler2019;

public class KoskiLukioStudentHandlerDelegator extends KoskiStudentHandler {

  @Inject
  private KoskiLukioStudentHandler lukioHandler;
  
  @Inject
  private KoskiLukioStudentHandler2019 lukioHandler2019;
  
  public Opiskeluoikeus studentToModel(Student student, String academyIdentifier, KoskiStudyProgrammeHandler handler) {
    OpiskelijanOPS opiskelijanOPS = settings.resolveOPS(student);
    return opiskelijanOPS == OpiskelijanOPS.ops2019 
        ? lukioHandler2019.oppimaaranOpiskeluoikeus(student, academyIdentifier, handler)
        : lukioHandler.studentToModel(student, academyIdentifier, handler);
  }
  
  @Override
  public void saveOrValidateOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    OpiskelijanOPS opiskelijanOPS = settings.resolveOPS(student);
    if (opiskelijanOPS == OpiskelijanOPS.ops2019) {
      lukioHandler2019.saveOrValidateOid(handler, student, oid);
    } else {
      lukioHandler.saveOrValidateOid(handler, student, oid);
    }
  }

  @Override
  public void removeOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    OpiskelijanOPS opiskelijanOPS = settings.resolveOPS(student);
    if (opiskelijanOPS == OpiskelijanOPS.ops2019) {
      lukioHandler2019.removeOid(handler, student, oid);
    } else {
      lukioHandler.removeOid(handler, student, oid);
    }
  }

  @Override
  public Set<KoskiStudentId> listOids(Student student) {
    OpiskelijanOPS opiskelijanOPS = settings.resolveOPS(student);
    return opiskelijanOPS == OpiskelijanOPS.ops2019 
        ? lukioHandler2019.listOids(student)
        : lukioHandler.listOids(student);
  }
  
}
