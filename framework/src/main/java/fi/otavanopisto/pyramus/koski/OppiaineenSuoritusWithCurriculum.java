package fi.otavanopisto.pyramus.koski;

import fi.otavanopisto.pyramus.koski.model.OppiaineenSuoritus;

public class OppiaineenSuoritusWithCurriculum<T extends OppiaineenSuoritus> {

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
