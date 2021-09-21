package fi.otavanopisto.pyramus.koski;

import fi.otavanopisto.pyramus.domainmodel.base.Subject;

public class OppiaineenSuoritusWithSubject<T> {

  public OppiaineenSuoritusWithSubject(Subject subject, boolean paikallinenOppiaine, T oppiaineenSuoritus) {
    this.subject = subject;
    this.oppiaineenSuoritus = oppiaineenSuoritus;
    this.paikallinenOppiaine = paikallinenOppiaine;
  }
  
  public T getOppiaineenSuoritus() {
    return oppiaineenSuoritus;
  }

  public Subject getSubject() {
    return subject;
  }

  public boolean isPaikallinenOppiaine() {
    return paikallinenOppiaine;
  }

  private final boolean paikallinenOppiaine;
  private final Subject subject;
  private final T oppiaineenSuoritus;
}
