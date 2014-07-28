package fi.internetix.smvc.controllers;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.dispatcher.RequestDispatchContext;

import net.sf.json.JSONObject;

/**
 * Request context for all JSON requests in the application.
 */
public class JSONRequestContext extends RequestContext {

  /** Create a new JSON request context.
   * 
   * @param dispatchContext The dispatch context, passed to RequestContext constructor
   * @param servletRequest The servlet request, passed to RequestContext constructor
   * @param servletResponse The servlet response, passed to RequestContext constructor
   * @param servletContext The servlet context, passed to RequestContext constructor
   */
  public JSONRequestContext(RequestDispatchContext dispatchContext, HttpServletRequest servletRequest, HttpServletResponse servletResponse, ServletContext servletContext) {
    super(dispatchContext, servletRequest, servletResponse, servletContext);
  }

  /**
   * Adds a response parameter to the context.
   * 
   * @param key The unique name of the response parameter
   * @param value The value of the response parameter
   */
  public void addResponseParameter(String key, Object value) {
    this.jsonResponse.put(key, value);
  }
  
  @Override
  public void writePreCommitResponse(int statusCode) throws Exception {
  }
  
  /**
   * Writes the response to the JSON request.
   * <p/>
   * In addition to all response parameters added to the context by the JSON request controller, the
   * following parameters also exist in the response:
   * <p/>
   * <code>statusCode</code> identifying whether the call was successful or not<br/>
   * <code>errorLevel</code>, if the status code is not {@link StatusCode#OK}<br/>
   * <code>errorMessage</code>, if the status code is not {@link StatusCode#OK} <br/>
   * <code>redirectURL</code>, if a redirect URL has been set to the context<br/>
   * 
   * @throws Exception If writing the response fails for any reason
   */
  @Override
  public void writePostCommitResponse(int statusCode) throws Exception {
    addResponseParameter("statusCode", statusCode);
    addResponseParameter("messages", getMessages());
    if (statusCode == StatusCode.OK && !StringUtils.isBlank(getRedirectURL())) {
      addResponseParameter("redirectURL", getRedirectURL());
    }

    // Write the JSON response

    getResponse().setContentType("text/javascript");
    PrintWriter responseWriter = getResponse().getWriter();
    try {
      jsonResponse.write(responseWriter);
    }
    finally {
      responseWriter.close();
    }
  }

  /**
   * The JSON response object.
   */
  private JSONObject jsonResponse = new JSONObject();

}
