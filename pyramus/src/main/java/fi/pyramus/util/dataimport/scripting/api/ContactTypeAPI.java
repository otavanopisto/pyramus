package fi.pyramus.util.dataimport.scripting.api;

import java.util.ArrayList;
import java.util.List;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.domainmodel.base.ContactType;

public class ContactTypeAPI {
  
  public ContactTypeAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(String name) {
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    return contactTypeDAO.create(name).getId();
  }
  
  public Long[] listIdsByName(String name) {
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    
    List<Long> result = new ArrayList<>();
    
    List<ContactType> contactTypes = contactTypeDAO.listByName(name);
    for (ContactType contactType : contactTypes) {
      result.add(contactType.getId());
    }
    
    return result.toArray(new Long[0]); 
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
