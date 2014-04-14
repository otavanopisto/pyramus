package fi.pyramus.util;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

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
