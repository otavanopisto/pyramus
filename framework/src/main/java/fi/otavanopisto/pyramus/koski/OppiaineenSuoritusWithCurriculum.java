package fi.otavanopisto.pyramus.koski;

public class OppiaineenSuoritusWithCurriculum<T> {

  public OppiaineenSuoritusWithCurriculum(OpiskelijanOPS ops, T oppiaineenSuoritus) {
    this.ops = ops;
    this.oppiaineenSuoritus = oppiaineenSuoritus;
  }
  
  public T getOppiaineenSuoritus() {
    return oppiaineenSuoritus;
  }

  public OpiskelijanOPS getOps() {
    return ops;
  }

  private final OpiskelijanOPS ops;
  private final T oppiaineenSuoritus;
}
