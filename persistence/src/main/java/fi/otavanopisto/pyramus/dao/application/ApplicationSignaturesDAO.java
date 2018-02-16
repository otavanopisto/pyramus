package fi.otavanopisto.pyramus.dao.application;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatureState;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures_;

@Stateless
public class ApplicationSignaturesDAO extends PyramusEntityDAO<ApplicationSignatures> {

  public ApplicationSignatures create(Application application) {
    EntityManager entityManager = getEntityManager();
    ApplicationSignatures applicationSignatures = new ApplicationSignatures();
    applicationSignatures.setApplication(application);
    entityManager.persist(applicationSignatures);
    return applicationSignatures;
  }

  public ApplicationSignatures findByApplication(Application application) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationSignatures> criteria = criteriaBuilder.createQuery(ApplicationSignatures.class);
    Root<ApplicationSignatures> root = criteria.from(ApplicationSignatures.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(ApplicationSignatures_.application), application));
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public ApplicationSignatures findByStaffInvitationId(String invitationId) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationSignatures> criteria = criteriaBuilder.createQuery(ApplicationSignatures.class);
    Root<ApplicationSignatures> root = criteria.from(ApplicationSignatures.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(ApplicationSignatures_.staffInvitationId), invitationId));
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public ApplicationSignatures findByApplicantInvitationId(String invitationId) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationSignatures> criteria = criteriaBuilder.createQuery(ApplicationSignatures.class);
    Root<ApplicationSignatures> root = criteria.from(ApplicationSignatures.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(ApplicationSignatures_.applicantInvitationId), invitationId));
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public ApplicationSignatures updateStaffDocument(ApplicationSignatures signatures, String documentId,
      String invitationId, String invitationToken, ApplicationSignatureState state) {
    EntityManager entityManager = getEntityManager();
    signatures.setStaffDocumentId(documentId);
    signatures.setStaffDocumentState(state);
    signatures.setStaffInvitationId(invitationId);
    signatures.setStaffInvitationToken(invitationToken);
    signatures.setStaffDocumentModified(new Date());
    entityManager.persist(signatures);
    return signatures;
  }

  public ApplicationSignatures updateApplicantDocument(ApplicationSignatures signatures, String documentId,
      String invitationId, String invitationToken, ApplicationSignatureState state) {
    EntityManager entityManager = getEntityManager();
    signatures.setApplicantDocumentId(documentId);
    signatures.setApplicantDocumentState(state);
    signatures.setApplicantInvitationId(invitationId);
    signatures.setApplicantInvitationToken(invitationToken);
    signatures.setApplicantDocumentModified(new Date());
    entityManager.persist(signatures);
    return signatures;
  }

  public ApplicationSignatures updateStaffDocumentState(ApplicationSignatures signatures, ApplicationSignatureState state) {
    EntityManager entityManager = getEntityManager();
    signatures.setStaffDocumentState(state);
    signatures.setStaffDocumentModified(new Date());
    entityManager.persist(signatures);
    return signatures;
  }

  public ApplicationSignatures updateApplicantDocumentState(ApplicationSignatures signatures, ApplicationSignatureState state) {
    EntityManager entityManager = getEntityManager();
    signatures.setApplicantDocumentState(state);
    signatures.setApplicantDocumentModified(new Date());
    entityManager.persist(signatures);
    return signatures;
  }

}