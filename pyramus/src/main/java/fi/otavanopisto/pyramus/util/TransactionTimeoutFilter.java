package fi.otavanopisto.pyramus.util;

import java.io.IOException;
import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

public class TransactionTimeoutFilter implements Filter {
  
  @Resource
  private UserTransaction userTransaction;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    timeout = Integer.parseInt(filterConfig.getInitParameter("timeout"), 10);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    try {
      userTransaction.setTransactionTimeout(timeout);
    } catch (SystemException e) {
      throw new ServletException(e);
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }
  
  private int timeout = -1;

}
