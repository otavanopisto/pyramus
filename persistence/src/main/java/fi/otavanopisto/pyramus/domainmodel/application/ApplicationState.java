package fi.otavanopisto.pyramus.domainmodel.application;

public enum ApplicationState {
  
  PENDING,
  PROCESSING,
  WAITING_STAFF_SIGNATURE,
  STAFF_SIGNED,
  APPROVED_BY_SCHOOL,
  APPROVED_BY_APPLICANT,
  TRANSFERRED_AS_STUDENT,
  REGISTERED_AS_STUDENT,
  REJECTED

}
