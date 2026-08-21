package fi.otavanopisto.pyramus.util.dataimport.scripting.api;

import java.util.Collections;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;

public class ClientApplicationAPI {
  
  public ClientApplicationAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(String clientName, String clientId, String clientSecret)
  {
    ClientApplication app = DAOFactory.getInstance().getClientApplicationDAO().create(clientName, false, clientId, clientSecret, false, Collections.emptySet(), false, Collections.emptySet());
    if (app == null) {
      return null;
    } else {
      return app.getId();
    }
  }
  
  @SuppressWarnings("unused")
  private Long loggedUserId;

}
