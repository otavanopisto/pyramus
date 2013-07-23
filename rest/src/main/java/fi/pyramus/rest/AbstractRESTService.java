package fi.pyramus.rest;

import java.util.Collection;

import fi.tranquil.TranquilModelEntity;
import fi.tranquil.TranquilityBuilderFactory;

public abstract class AbstractRESTService {

  protected abstract TranquilityBuilderFactory getTranquilityBuilderFactory();

  protected TranquilModelEntity tranqualise(Object object) {
    return getTranquilityBuilderFactory()
        .createBuilder()
        .createTranquility()
        .entity(object);
  }

  protected Collection<TranquilModelEntity> tranqualise(Collection<?> collection) {
    return getTranquilityBuilderFactory()
        .createBuilder()
        .createTranquility()
        .entities(collection);
  }

  protected Long getUser() {
    Long id = (long) 1;
    return id;
  }

}
