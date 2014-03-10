package fi.pyramus.views.system.setupwizard;

import java.util.HashMap;
import java.util.Map;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;

public enum SetupWizardHandlerFactory {
  INSTANCE;

  {
    handlers = new HashMap<String, SetupWizardHandler>();
    addHandler(new EducationTypesSetupWizardHandler());
  }
  
  private void addHandler(SetupWizardHandler handler) {
    handlers.put(handler.getPhaseName(), handler);
  }
  
  public SetupWizardHandler getHandlerFor(String setupPhase) {
    if (!handlers.containsKey(setupPhase)) {
      throw new SmvcRuntimeException(StatusCode.UNDEFINED, "Invalid setup wizard phase");
    } else {
      return handlers.get(setupPhase);
    }
  }
  
  private Map<String, SetupWizardHandler> handlers;
}
