package fi.otavanopisto.pyramus.views.applications;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;

@Stateless
public class RemoveOldApplicationsScheduler {
  
  @Schedule(dayOfWeek = "1", hour = "1", persistent = false)
  public void removeOldApplications() {
    Logger logger = Logger.getLogger(RemoveOldApplicationsScheduler.class.getName());
    logger.info("Running remove old applications scheduler");
    // Threshold date
    Calendar c = Calendar.getInstance();
    c.add(Calendar.YEAR, -1);
    Date date = c.getTime();
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    // #4226: aineopiskelu, mk, aikuislukio: year after registration
    List<Application> applications = applicationDAO.listByOlderAndLine(date, "aineopiskelu");
    for (Application application : applications) {
      ApplicationUtils.deleteApplication(application);
    }
    applications = applicationDAO.listByOlderAndLine(date, "mk");
    for (Application application : applications) {
      ApplicationUtils.deleteApplication(application);
    }
    applications = applicationDAO.listByOlderAndLine(date, "aikuislukio");
    for (Application application : applications) {
      ApplicationUtils.deleteApplication(application);
    }
    // #4226: nettilukio, nettipk: year after registration if not transferred as student
    applications = applicationDAO.listByOlderAndLineAndNullStudent(date, "nettilukio");
    for (Application application : applications) {
      ApplicationUtils.deleteApplication(application);
    }
    applications = applicationDAO.listByOlderAndLineAndNullStudent(date, "nettipk");
    for (Application application : applications) {
      ApplicationUtils.deleteApplication(application);
    }
  }
  
}
