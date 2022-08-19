package fi.internetix.smvc.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.Severity;
import fi.internetix.smvc.SmvcMessage;
import fi.internetix.smvc.dispatcher.RequestDispatchContext;

/**
 * An abstract request context associated with each request made to the application.
 * <p/>
 * Wraps in both the HTTP servlet request and response objects, as well as packs some logic for
 * error handling and tracking the currently logged in user.
 */
public abstract class RequestContext {

  /**
   * The constructor specifying the HTTP servlet request and response objects associated with the
   * context.
   * 
   * @param servletRequest The HTTP servlet request
   * @param servletResponse The HTTP servlet response
   * @throws FileUploadException 
   */
  protected RequestContext(RequestDispatchContext requestDispatchContext, HttpServletRequest servletRequest, HttpServletResponse servletResponse, ServletContext servletContext) {
    this.servletRequest = servletRequest;
    this.servletResponse = servletResponse;
    this.requestDispatchContext = requestDispatchContext;
    this.servletContext = servletContext;
  }

  /**
   * Returns the HTTP servlet request associated with this context.
   * 
   * @return The HTTP servlet request associated with this context
   */
  public HttpServletRequest getRequest() {
    return servletRequest;
  }

  /**
   * Returns the HTTP servlet response associated with this context.
   * 
   * @return The HTTP servlet response associated with this context
   */
  public HttpServletResponse getResponse() {
    return servletResponse;
  }
  
  public ServletContext getServletContext() {
    return servletContext;
  }

  /**
   * Returns whether this context is associated with a logged in user.
   * 
   * @return <code>true</code> if a logged in user exists in this context, otherwise
   * <code>false/code>
   */
  public boolean isLoggedIn() {
    return getLoggedUserId() != null;
  }
  
  /**
   * Returns the identifier of the logged in user. If the user hasn't logged in, returns
   * <code>null</code>.
   * 
   * @return The identifier of the logged in user, or <code>null</code> if the user hasn't logged in
   */
  public Long getLoggedUserId() {
    HttpSession session = getRequest().getSession(false);
    return session == null ? null : (Long) session.getAttribute("loggedUserId");
  }
  
  /**
   * Returns the redirect URL stored to this context. If one hasn't been specified, returns
   * <code>null</code>.
   * 
   * @return The redirect URL stored to this context, or <code>null</code> if one hasn't been set
   */
  public String getRedirectURL() {
    return redirectURL;
  }

  /**
   * Sets the redirect URL of this context.
   * 
   * @param redirectURL The redirect URL of this context
   */
  public void setRedirectURL(String redirectURL) {
    this.redirectURL = redirectURL;
  }
  
  public String getReferer(boolean returnAnchor) {
    if (StringUtils.isEmpty(servletRequest.getHeader("Referer"))) {
      return null;
    }
    StringBuilder refererBuilder = new StringBuilder(servletRequest.getHeader("Referer"));
    
    if (returnAnchor) {
      String refererAnchor = getRefererAnchor();
      if (!StringUtils.isBlank(refererAnchor)) {
        refererBuilder.append('#').append(refererAnchor);
      }
    }
    
    return refererBuilder.toString();
  }
  
  public String getRefererAnchor() {
    return getString("__refererAnchor"); 
  }

  public void addMessage(Severity severity, String message) {
    messages.add(new SmvcMessage(severity, message));
  }

  public void addMessage(SmvcMessage message) {
    messages.add(message);
  }

  /**
   * Returns the messages of this context.
   * 
   * @return The messages of this context
   */
  protected List<SmvcMessage> getMessages() {
    return messages;
  }

  public Boolean getBoolean(String paramName) {
    return Boolean.valueOf(getString(paramName));
  }
  
  public Date getDate(String paramName) {
    Long value = getLong(paramName);
    return value == null ? null : new Date(value);
  }
  
  public Date getDateByFormat(String paramName, String format) {
  	Date date = null;
  	SimpleDateFormat sdf = new SimpleDateFormat(format);
  	
  	String dateStr = getString(paramName, true);
  	
  	if (dateStr != null) {
	  	try {
		    date = sdf.parse(dateStr);
	    } catch (ParseException e) {
		    e.printStackTrace();
	    }
  	}
    
    return date;
  }

  public Double getDouble(String paramName) {
    String value = getString(paramName);
    // Since '.' is a universal decimal separator in Java, let's be lenient with ',' as well
    if (value != null && value.indexOf(',') >= 0) {
      value = value.replace(',', '.');
    }
    // TODO Invalid value as a runtime exception?
    return NumberUtils.isNumber(value) ? NumberUtils.toDouble(value) : null;  
  }

  public Integer getInteger(String paramName) {
    String value = getString(paramName);
    return NumberUtils.isNumber(value) ? NumberUtils.toInt(value) : null;  
  }

  public Long getLong(String paramName) {
    String value = getString(paramName);
    return NumberUtils.isNumber(value) ? NumberUtils.toLong(value) : null;  
  }

  public BigDecimal getBigDecimal(String paramName) {
    String value = getString(paramName);
    // Since '.' is a universal decimal separator in Java, let's be lenient with ',' as well
    if (value != null && value.indexOf(',') >= 0) {
      value = value.replace(',', '.');
    }
    
    return NumberUtils.isNumber(value) ? NumberUtils.createBigDecimal(value) : null;  
  }
  
  public Currency getCurrency(String paramName) {
    String value = getString(paramName);
    if (StringUtils.isNotBlank(value)) {
      return Currency.getInstance(value);
    }
    
    return null;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Enum getEnum(String paramName, Class enumClass) {
    String name = getString(paramName);
    if (name != null)
      return Enum.valueOf(enumClass, name);
    else
      return null;
  }
  
  public String getLowercaseString(String paramName) {
    return getLowercaseString(paramName, true);
  }

  public String getLowercaseString(String paramName, boolean emptyAsNull) {
    String str = getString(paramName, emptyAsNull);
    return str == null ? null : str.toLowerCase();
  }

  public String getString(String paramName) {
    return getString(paramName, true);
  }
  
  public String[] getStrings(String paramName) {
    return requestDispatchContext.getParameterHandler().getParameters(paramName);
  }

  public String getString(String paramName, boolean emptyAsNull) {
    String value = getRequest().getParameter(paramName);
    return emptyAsNull && StringUtils.isBlank(value) ? null : value;
  }
  
  public Part getFile(String paramName) {
    try {
      return getRequest().getPart(paramName);
    } catch (IOException | ServletException e) {
      return null;
    }
  }  
  
  /**
   * Once a page request has been processed, the context is responsible of writing it back to the
   * user via this method. This response is written BEFORE committing (or rolling back) the database
   * session associated with request.
   * 
   * @param statusCode the status code of the request
   * 
   * @throws Exception If writing the response fails for any reason
   */
  public abstract void writePreCommitResponse(int statusCode) throws Exception;

  /**
   * Once a page request has been processed, the context is responsible of writing it back to the
   * user via this method. This response is written AFTER committing (or rolling back) the database
   * session associated with the request.
   * 
   * @param statusCode the status code of the request
   * 
   * @throws Exception If writing the response fails for any reason
   */
  public abstract void writePostCommitResponse(int statusCode) throws Exception;

  private RequestDispatchContext requestDispatchContext;
  
  /**
   * The HTTP servlet request of this context.
   */
  private HttpServletRequest servletRequest;

  /**
   * The HTTP servlet response of this context.
   */
  private HttpServletResponse servletResponse;

  private final ServletContext servletContext;
  
  /**
   * The redirect URL of this context.
   */
  private String redirectURL;

  /**
   * The messages of this context.
   */
  private List<SmvcMessage> messages = new ArrayList<>();

}
