package fi.otavanopisto.pyramus.koski;

import fi.otavanopisto.pyramus.domainmodel.base.Subject;

public class OppiaineenSuoritusWithCurriculum<T> {

  public OppiaineenSuoritusWithCurriculum(Subject subject, OpiskelijanOPS ops, T oppiaineenSuoritus) {
    this.subject = subject;
    this.ops = ops;
    this.oppiaineenSuoritus = oppiaineenSuoritus;
  }
  
  public T getOppiaineenSuoritus() {
    return oppiaineenSuoritus;
  }

  public OpiskelijanOPS getOps() {
    return ops;
  }

  public Subject getSubject() {
    return subject;
  }

  private final Subject subject;
  private final OpiskelijanOPS ops;
  private final T oppiaineenSuoritus;
}
