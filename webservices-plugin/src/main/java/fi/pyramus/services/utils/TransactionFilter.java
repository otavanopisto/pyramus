package fi.pyramus.services.utils;

import java.io.IOException;

import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
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