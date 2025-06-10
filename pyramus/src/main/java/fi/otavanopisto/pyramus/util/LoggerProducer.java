package fi.otavanopisto.pyramus.util;

import java.util.logging.Logger;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoggerProducer {

  @Produces
  public Logger produceLog(InjectionPoint injectionPoint) {
    return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
  }

}