package fi.otavanopisto.pyramus.applications;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.mailer.MailErrorHandler;

public class ApplicationMailErrorHandler implements MailErrorHandler {
  
  public ApplicationMailErrorHandler(Application application) {
    this.application = application;
  }
  
  public void report(String subject, String errorMsg) {
    ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
    applicationLogDAO.create(application,
        ApplicationLogType.HTML,
        String.format("<p>Sähköpostin <b>%s</b> lähetys epäonnistui:</p><p>%s</p>", subject, errorMsg),
        null);
  }
  
  private Application application;

}
