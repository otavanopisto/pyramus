package fi.otavanopisto.pyramus.dao.application;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;

@Stateless
public class ApplicationDAO extends PyramusEntityDAO<ApplicationDAO> {

  public Application createApplication(
      String applicationId,
      StudyProgramme studyProgramme,
      String firstName,
      String lastName,
      String email,
      String referenceCode,
      String formData,
      ApplicationState state) {
    EntityManager entityManager = getEntityManager();

    Application application = new Application();
    
    application.setApplicationId(applicationId);
    application.setStudyProgramme(studyProgramme);
    application.setFirstName(firstName);
    application.setLastName(lastName);
    application.setEmail(email);
    application.setReferenceCode(referenceCode);
    application.setFormData(formData);
    application.setState(state);
    application.setCreated(new Date());
    application.setLastModified(new Date());
    application.setApplicantLastModified(new Date());
    application.setArchived(Boolean.FALSE);
   
    entityManager.persist(application);

    return application;
  }

}
