package fi.otavanopisto.pyramus.util;

import javax.servlet.http.HttpSession;

public class ThreadSessionContainer {

  private ThreadSessionContainer() {
  }

  public static HttpSession getSession() {
    return THREAD_LOCAL.get();
  }
  
  public static void setSession(HttpSession httpSession) {
    THREAD_LOCAL.set(httpSession);
  }
  
  private static final ThreadLocal<HttpSession> THREAD_LOCAL = new ThreadLocal<>();
}
