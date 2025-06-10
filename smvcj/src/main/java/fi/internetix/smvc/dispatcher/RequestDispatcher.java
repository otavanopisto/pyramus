package fi.internetix.smvc.dispatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RequestDispatcher {
  
  public boolean canHandle(HttpServletRequest request, HttpServletResponse response);

  public RequestDispatchContext getContext(HttpServletRequest request, HttpServletResponse response);
  
}
