package fi.pyramus.dao.help;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.help.HelpFolder;
import fi.pyramus.domainmodel.users.User;

@Stateless
public class HelpFolderDAO extends PyramusEntityDAO<HelpFolder> {

  public HelpFolder create(HelpFolder parent, Integer indexColum, User creatingUser) {
    EntityManager entityManager = getEntityManager();
    
    Date now = new Date(System.currentTimeMillis());
    
    HelpFolder helpFolder = new HelpFolder();
    helpFolder.setCreated(now);
    helpFolder.setLastModified(now);
    helpFolder.setCreator(creatingUser);
    helpFolder.setLastModifier(creatingUser);
    helpFolder.setParent(parent);
    helpFolder.setIndexColumn(indexColum);
    
    entityManager.persist(helpFolder);
    
    return helpFolder;
  }
  
  @Override
  public void delete(HelpFolder helpFolder) {
    EntityManager entityManager = getEntityManager();
    
    if (helpFolder.getParent() != null) {
      HelpFolder parentFolder = helpFolder.getParent();
      parentFolder.removeChild(helpFolder);
      entityManager.persist(parentFolder);
    }
    
    super.delete(helpFolder);
  }
  
}
