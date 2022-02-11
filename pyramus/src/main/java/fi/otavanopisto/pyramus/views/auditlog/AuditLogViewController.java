package fi.otavanopisto.pyramus.views.auditlog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.envers.PyramusRevisionEntity;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class AuditLogViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {

    // Some crummy test page

    requestContext.setIncludeJSP("/templates/auditlog/auditlog.jsp");

    // Audit log person

    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    Long personId = NumberUtils.createLong(requestContext.getRequest().getParameter("person"));
    Person person = personDAO.findById(personId);

    // Results and other search objects

    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    List<AuditLogItem> items = new ArrayList<>();
    List<Number> revisions;
    List<Object[]> changes;
    AuditQuery query;

    // TODO Access to entity manager via a more sophisticated way

    EntityManager em = userDAO.getEntityManager();
    AuditReader reader = AuditReaderFactory.get(em);

    // Changes to person

    revisions = reader.getRevisions(Person.class, person.getId());
    if (!revisions.isEmpty()) {
      query = reader.createQuery().forRevisionsOfEntity(Person.class, false, true);
      query.add(AuditEntity.revisionNumber().in(revisions));
      changes = query.getResultList();
      for (Object[] change : changes) {
        Person p = (Person) change[0];
        PyramusRevisionEntity pre = (PyramusRevisionEntity) change[1];
        RevisionType revType = (RevisionType) change[2];
        AuditLogItem item = new AuditLogItem();
        item.setId(p.getId());
        item.setData(p.getSocialSecurityNumber());
        item.setDate(pre.getRevisionDate());
        item.setTarget("PERSON");
        item.setType(revType);
        User culprit = userDAO.findById(pre.getUserId());
        item.setUser(culprit.getFullName());
        items.add(item);
      }
    }

    // Changes to each student (TODO staff member tracking)

    for (User user : person.getStudents()) {
      revisions = reader.getRevisions(User.class, user.getId());
      if (!revisions.isEmpty()) {
        query = reader.createQuery().forRevisionsOfEntity(User.class, false, true);
        query.add(AuditEntity.revisionNumber().in(revisions));
        changes = query.getResultList();
        for (Object[] change : changes) {
          User u = (User) change[0];
          PyramusRevisionEntity pre = (PyramusRevisionEntity) change[1];
          RevisionType revType = (RevisionType) change[2];
          if (u.getArchived()) {
            revType = RevisionType.DEL;
          }
          AuditLogItem item = new AuditLogItem();
          item.setId(u.getId());
          item.setData(u.getFullName());
          item.setDate(pre.getRevisionDate());
          item.setTarget("USER");
          item.setType(revType);
          User culprit = userDAO.findById(pre.getUserId());
          item.setUser(culprit.getFullName());
          items.add(item);
        }
      }

      // Changes to contact information of each student

      List<Number> contactRevisions = reader.getRevisions(ContactInfo.class, user.getContactInfo().getId());
      if (!contactRevisions.isEmpty()) {

        // Addresses

        AuditQuery addressQuery = reader.createQuery().forRevisionsOfEntity(Address.class, false, true);
        addressQuery.add(AuditEntity.revisionNumber().in(contactRevisions)); 
        List<Object[]> addressChanges = addressQuery.getResultList();
        for (Object[] addressChange : addressChanges) {
          Address a = (Address) addressChange[0];
          PyramusRevisionEntity pre = (PyramusRevisionEntity) addressChange[1];
          RevisionType revType = (RevisionType) addressChange[2];
          AuditLogItem item = new AuditLogItem();
          item.setId(a.getId());
          item.setData(a.getName() + " " + a.getStreetAddress() + " " + a.getPostalCode() + " " + a.getCity() + " " + a.getCountry());
          item.setDate(pre.getRevisionDate());
          item.setTarget("ADDRESS");
          item.setType(revType);
          User culprit = userDAO.findById(pre.getUserId());
          item.setUser(culprit.getFullName());
          items.add(item);
        }

        // Emails

        AuditQuery emailQuery = reader.createQuery().forRevisionsOfEntity(Email.class, false, true);
        emailQuery.add(AuditEntity.revisionNumber().in(contactRevisions));
        List<Object[]> emailChanges = emailQuery.getResultList();
        for (Object[] emailChange : emailChanges) {
          Email e = (Email) emailChange[0];
          PyramusRevisionEntity pre = (PyramusRevisionEntity) emailChange[1];
          RevisionType revType = (RevisionType) emailChange[2];
          AuditLogItem item = new AuditLogItem();
          item.setId(e.getId());
          item.setData(e.getAddress());
          item.setDate(pre.getRevisionDate());
          item.setTarget("EMAIL");
          item.setType(revType);
          User culprit = userDAO.findById(pre.getUserId());
          item.setUser(culprit.getFullName());
          items.add(item);
        }

        // Phone numbers

        AuditQuery phoneQuery = reader.createQuery().forRevisionsOfEntity(PhoneNumber.class, false, true);
        phoneQuery.add(AuditEntity.revisionNumber().in(contactRevisions));
        List<Object[]> phoneChanges = phoneQuery.getResultList();
        for (Object[] phoneChange : phoneChanges) {
          PhoneNumber p = (PhoneNumber) phoneChange[0];
          PyramusRevisionEntity pre = (PyramusRevisionEntity) phoneChange[1];
          RevisionType revType = (RevisionType) phoneChange[2];
          AuditLogItem item = new AuditLogItem();
          item.setId(p.getId());
          item.setData(p.getNumber());
          item.setDate(pre.getRevisionDate());
          item.setTarget("PHONE");
          item.setType(revType);
          User culprit = userDAO.findById(pre.getUserId());
          item.setUser(culprit.getFullName());
          items.add(item);
        }
      }
    }

    items.sort(Comparator.comparing(AuditLogItem::getDate).reversed());

    requestContext.getRequest().setAttribute("items", items);

  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
