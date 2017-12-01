package fi.otavanopisto.pyramus.domainmodel.koski;

public enum KoskiPersonState {

  PENDING,
  SUCCESS,
  SERVER_FAILURE,
  SERVER_UNAVAILABLE,
  UNKNOWN_FAILURE, 
  NO_CURRICULUM,
  LINKED_MISSING_VALUES;
  
  /* Pending states */
  public static KoskiPersonState[] PENDING_STATES = { PENDING };
  /* Success states */
  public static KoskiPersonState[] SUCCESS_STATES = { SUCCESS };
  /* Warning states which indicate succeeded update but with warnings */
  public static KoskiPersonState[] WARNING_STATES = { LINKED_MISSING_VALUES };
  /* Error states which prevent the update altogether */
  public static KoskiPersonState[] ERROR_STATES = { SERVER_FAILURE, SERVER_UNAVAILABLE, UNKNOWN_FAILURE, NO_CURRICULUM };
}
