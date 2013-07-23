package fi.pyramus.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import fi.pyramus.rest.tranquil.BaseLookup;
import fi.pyramus.rest.tranquil.CompactLookup;
import fi.pyramus.rest.tranquil.CompleteLookup;
import fi.tranquil.TranquilityBuilderFactory;
import fi.tranquil.processing.PropertyAccessor;
import fi.tranquil.processing.TranquilityEntityFactory;

public class TranquilityBuilderFactoryProvider {
  @Produces
  @ApplicationScoped
  public TranquilityBuilderFactory produceTranquilityFactory() {
    return new TranquilityBuilderFactory(new PropertyAccessor(), new TranquilityEntityFactory(new BaseLookup(), new CompactLookup(), new CompleteLookup()));
  }
}
