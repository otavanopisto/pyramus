package fi.pyramus.jobs;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.drafts.DraftDAO;

/** A background job that removes deprecated drafts
 *  from the draft repository periodically.
 */
@Stateless
public class RemoveDeprecatedDrafts {

  /** Removes the deprecated drafts from the repository.
   * This method is called automatically every 5 minutes.
   */
  @Schedule(second = "0", minute = "0/5", hour = "*", persistent = false)
  public void removeDeprecatedDrafts(){
    DraftDAO draftDAO = DAOFactory.getInstance().getDraftDAO();
    draftDAO.removeDeprecatedDrafts();
  }
  
}
