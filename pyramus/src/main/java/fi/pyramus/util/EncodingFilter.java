package fi.pyramus.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter implements Filter {

  /**
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig fc) throws ServletException {
    this.filterConfig = fc;
    this.encoding = filterConfig.getInitParameter("encoding");
  }

  /**
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
   *      javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
      ServletException {
    req.setCharacterEncoding(encoding);
    resp.setCharacterEncoding(encoding);
    chain.doFilter(req, resp);
  }

  /**
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
  }

  private String encoding;
  private FilterConfig filterConfig;

}