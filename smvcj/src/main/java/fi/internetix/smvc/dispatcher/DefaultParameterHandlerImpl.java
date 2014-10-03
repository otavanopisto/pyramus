package fi.internetix.smvc.dispatcher;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

public class DefaultParameterHandlerImpl implements ParameterHandler {

  public DefaultParameterHandlerImpl(HttpServletRequest request, boolean decodeGETUtf) {
    this.request = request;
    this.decodeGETUtf = decodeGETUtf;
  }

  @Override
  public String getParameter(String name) {
    String value = request.getParameter(name);

    if (decodableRequest())
      value = decodeValueEncoding(value);
    
    return value;
  }
  
  @Override
  public String[] getParameters(String name) {
    String[] values = request.getParameterValues(name);
    
    if (decodableRequest()) {
      for (int i = 0; i < values.length; i++) {
        values[i] = decodeValueEncoding(values[i]);
      }
    }
    
    return values; 
  }

  private boolean decodableRequest() {
    return (decodeGETUtf && "GET".equals(request.getMethod()));
  }
  
  private String decodeValueEncoding(String value) {
    if (value == null)
      return null;
    
    try {
      byte[] bytes = value.getBytes("ISO-8859-1");
      value = new String(bytes, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return value;
  }
  
  private final boolean decodeGETUtf;
  private final HttpServletRequest request;
}
