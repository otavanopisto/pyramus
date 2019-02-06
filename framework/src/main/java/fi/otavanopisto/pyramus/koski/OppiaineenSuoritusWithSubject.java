package fi.otavanopisto.pyramus.koski;

import fi.otavanopisto.pyramus.domainmodel.base.Subject;

public class OppiaineenSuoritusWithSubject<T> {

  public OppiaineenSuoritusWithSubject(Subject subject, T oppiaineenSuoritus) {
    this.subject = subject;
    this.oppiaineenSuoritus = oppiaineenSuoritus;
  }
  
  public T getOppiaineenSuoritus() {
    return oppiaineenSuoritus;
  }

  public Subject getSubject() {
    return subject;
  }

  private final Subject subject;
  private final T oppiaineenSuoritus;
}
