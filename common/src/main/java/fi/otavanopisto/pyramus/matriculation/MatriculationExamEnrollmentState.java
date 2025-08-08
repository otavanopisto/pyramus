package fi.otavanopisto.pyramus.matriculation;

import java.util.Collections;
import java.util.Set;

public enum MatriculationExamEnrollmentState {
  // Form has been saved by the student and is pending review
  PENDING,
  // Guider has requested supplementation
  SUPPLEMENTATION_REQUEST,
  // Student has supplemented the form
  SUPPLEMENTED,
  // Form has been approved by the guider
  APPROVED,
  // Form has been rejected by the guider
  REJECTED,
  // Form has been confirmed by the student
  CONFIRMED,
  // Form has been filled on behalf of the student
  FILLED_ON_BEHALF;

  // Terminal states
  public static final Set<MatriculationExamEnrollmentState> TERMINAL_STATES = Collections.unmodifiableSet(Set.of(REJECTED, CONFIRMED, FILLED_ON_BEHALF));
}
