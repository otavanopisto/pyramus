package fi.pyramus.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.PageNotFoundException;
import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.dispatcher.PlatformErrorListener;

public class PyramusErrorListener implements PlatformErrorListener {

  @Override
  public void onLoginRequiredException(HttpServletRequest request,
      HttpServletResponse response, LoginRequiredException e) {
    
  }

  @Override
  public void onPageNotFoundException(HttpServletRequest request,
      HttpServletResponse response, PageNotFoundException e) {
    
  }

  @Override
  public void onAccessDeniedException(HttpServletRequest request,
      HttpServletResponse response, AccessDeniedException e) {
    try {
      response.sendRedirect("/accessdenied.page");
    } catch (IOException e1) {
    }
  }

  @Override
  public void onSmvcRuntimeException(HttpServletRequest request,
      HttpServletResponse response, SmvcRuntimeException e) {
    
  }

  @Override
  public void onUncontrolledException(HttpServletRequest request,
      HttpServletResponse response, Exception e) {
    
  }

  @Override
  public void onTransactionCommitException(HttpServletRequest request,
      HttpServletResponse response, Exception e) {
    
  }

}
