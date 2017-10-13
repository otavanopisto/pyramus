package fi.otavanopisto.pyramus.dao.application;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationMailTemplate;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

@Stateless
public class ApplicationMailTemplateDAO extends PyramusEntityDAO<ApplicationMailTemplate> {

  public ApplicationMailTemplate create(String line, String name, String subject, String content, StaffMember user) {
   EntityManager entityManager = getEntityManager();
   
   ApplicationMailTemplate applicationMailTemplate = new ApplicationMailTemplate();
   
   applicationMailTemplate.setLine(line);
   applicationMailTemplate.setName(name);
   applicationMailTemplate.setSubject(subject);
   applicationMailTemplate.setContent(content);
   applicationMailTemplate.setStaffMember(user);
   applicationMailTemplate.setArchived(Boolean.FALSE);
   
   entityManager.persist(applicationMailTemplate);

   return applicationMailTemplate;
  }
  
  public ApplicationMailTemplate update(ApplicationMailTemplate applicationMailTemplate, String line, String name, String subject, String content) {
    EntityManager entityManager = getEntityManager();

    applicationMailTemplate.setLine(line);
    applicationMailTemplate.setName(name);
    applicationMailTemplate.setSubject(subject);
    applicationMailTemplate.setContent(content);
   
    entityManager.persist(applicationMailTemplate);

    return applicationMailTemplate;
  }

}