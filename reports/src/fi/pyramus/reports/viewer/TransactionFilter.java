package fi.pyramus.reports.viewer;

import java.io.IOException;

import javax.annotation.Resource;
import javax.persistence.PersistenceContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.transaction.UserTransaction;

@PersistenceContext(name="persistence/pyramusEntityManager", unitName="pyramusManager")
public class TransactionFilter implements Filter {
  
  @Resource private UserTransaction userTransaction;
  
  public void init(FilterConfig fc) throws ServletException {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    try {
      userTransaction.begin();
      chain.doFilter(request, response);
      userTransaction.commit();
    }
    catch (Exception e) {
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

}