package fi.internetix.smvc.dispatcher;

import fi.internetix.smvc.controllers.RequestController;

public class RequestDispatchContext {
  
  public RequestDispatchContext(RequestController requestController, ParameterHandler parameterHandler) {
    this.requestController = requestController;
    this.parameterHandler = parameterHandler;
  }

  public RequestController getRequestController() {
    return requestController;
  }
  
  public ParameterHandler getParameterHandler() {
    return parameterHandler;
  }
  
  private RequestController requestController;
  private ParameterHandler parameterHandler; 
  
}
