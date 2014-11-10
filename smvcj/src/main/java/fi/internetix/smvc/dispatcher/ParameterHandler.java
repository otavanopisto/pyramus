package fi.internetix.smvc.dispatcher;

public interface ParameterHandler {

  public String getParameter(String name);
  public String[] getParameters(String name);
}
