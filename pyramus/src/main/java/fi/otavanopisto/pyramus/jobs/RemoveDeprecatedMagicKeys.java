package fi.otavanopisto.pyramus.jobs;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.MagicKeyDAO;

/** A background job that removes deprecated magic keys
 *  from the repository periodically.
 */
@Stateless
public class RemoveDeprecatedMagicKeys {

  /** Removes the deprecated magic keys from the repository.
   * This method is called automatically every 5 minutes.
   */
  @Schedule(second = "0", minute = "0/5", hour = "*", persistent = false)
  public void removeDeprecatedMagicKeys() {
    MagicKeyDAO magicKeyDAO = DAOFactory.getInstance().getMagicKeyDAO();
    magicKeyDAO.deleteDeprecatedMagicKeys();
  }

}
