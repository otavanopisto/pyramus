package fi.otavanopisto.pyramus.framework;

import java.util.List;
import java.util.Set;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationPersonFlagDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationPersonFlag;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentFlag;

public class MatriculationUtils {

  private MatriculationUtils() {
  }

  /**
   * Synchronizes given MatriculationExamEnrollmentFlags for given Person as
   * MatriculationPersonFlag entities.
   * 
   * @param person
   * @param flags
   */
  public static void syncronizePersonFlags(Person person, Set<MatriculationExamEnrollmentFlag> flags) {
    MatriculationPersonFlagDAO matriculationPersonFlagDAO = DAOFactory.getInstance().getMatriculationPersonFlagDAO();

    // List of what's stored at the moment
    List<MatriculationPersonFlag> existing = matriculationPersonFlagDAO.listByPerson(person);
    
    for (MatriculationExamEnrollmentFlag flag : flags) {
      if (!existing.removeIf(flagEntity -> flagEntity.getFlag() == flag)) {
        // removeIf == false -> didn't exist -> create
        matriculationPersonFlagDAO.create(person, flag);
      }
    }

    // At this point only the removed remain in the list
    for (MatriculationPersonFlag removedFlag : existing) {
      matriculationPersonFlagDAO.delete(removedFlag);
    }
  }
  
}
