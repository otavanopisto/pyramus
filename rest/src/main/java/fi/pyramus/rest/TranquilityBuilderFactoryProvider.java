package fi.pyramus.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;


import fi.pyramus.rest.tranquil.EntityLookup;
import fi.tranquil.TranquilityBuilderFactory;
import fi.tranquil.processing.PropertyAccessor;
import fi.tranquil.processing.TranquilityEntityFactory;

public class TranquilityBuilderFactoryProvider {
  @Produces
  @ApplicationScoped
  public TranquilityBuilderFactory produceTranquilityFactory() {
    EntityLookup lookup = new EntityLookup();
    return new TranquilityBuilderFactory(new PropertyAccessor(), new TranquilityEntityFactory(lookup, lookup, lookup));
  }
}
