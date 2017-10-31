package fi.otavanopisto.pyramus.koski;

import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;

public class KoskiException extends Exception {

  private static final long serialVersionUID = 3116054918556705370L;

  public KoskiException(String message, KoskiPersonState state) {
    super(message);
    this.state = state;
  }
  
  public KoskiPersonState getState() {
    return state;
  }

  private final KoskiPersonState state;
}
