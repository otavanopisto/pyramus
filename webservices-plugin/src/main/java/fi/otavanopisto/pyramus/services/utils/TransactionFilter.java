package fi.otavanopisto.pyramus.services.utils;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

public class TransactionFilter implements Filter {

  public void init(FilterConfig fc) throws ServletException {
  }

  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
      ServletException {
    try {
      userTransaction.begin();
      chain.doFilter(req, resp);
      userTransaction.commit();
    }
    catch (Exception e) {
      if (resp instanceof HttpServletResponse) {
        ((HttpServletResponse) resp).setStatus(500);
      }

      try {
        userTransaction.rollback();
        throw new ServletException(e);
      }
      catch (Exception e1) {
        throw new ServletException(e1);
      }
    }
  }

  public void destroy() {
  }

  @Resource
  private UserTransaction userTransaction;
}