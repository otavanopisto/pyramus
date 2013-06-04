package fi.pyramus;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.testng.Assert;
import org.testng.annotations.Test;

import fi.pyramus.breadcrumbs.BreadcrumbHandler;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.JSONRequestController;
import fi.pyramus.persistence.HibernateUtils;
import fi.pyramus.persistence.usertypes.Role;
import fi.pyramus.views.PyramusViewController;
import fi.testutils.DatabaseDependingTest;
import fi.testutils.TestDataGenerator;

public class ServletTest extends DatabaseDependingTest {
  private static final String CTXPATH = "/pyramus";
  

  /* Test if exception handling works correctly */
  
  
  
  @Test
  public void testUnauthorized() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ServletException, IOException, SecurityException, NoSuchMethodException {
    HttpServletRequest request = createNiceMock(HttpServletRequest.class);
    
    expect(request.getRequestURI()).andReturn(CTXPATH + "/authtest.page");
    expect(request.getContextPath()).andReturn(CTXPATH);
    expect(request.getSession(true)).andReturn(createMock(HttpSession.class));
    expect(request.getRequestURL()).andReturn(new StringBuffer("nonsence"));
    
    replay(request);
    
    HttpServletResponse response = createNiceMock(HttpServletResponse.class);
    replay(response);
    
    Properties pages = new Properties();
    pages.setProperty("authtest", AuthorizationTestController.class.getName());
    
    RequestControllerMapper.mapControllers(pages, ".page");
    
    /* Authorization should fail */
    
    AuthorizationTestController.reset();
    new Servlet().service(request, response);
    Assert.assertEquals(AuthorizationTestController.hasProcessed(), false);
    
    verify(request);
  }
  
  @Test
  public void testAuthorized() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ServletException, IOException, SecurityException, NoSuchMethodException {
    beginTransaction();
    User user = TestDataGenerator.createTestUsers(1, 0, "Test", "Test", "test", "tst.com", "internal", UserRole.USER).get(0);
    commit();
    
    BreadcrumbHandler breadcrumbHandler = createNiceMock(BreadcrumbHandler.class);

    HttpServletRequest request = createNiceMock(HttpServletRequest.class);
    
    HttpSession session = createNiceMock(HttpSession.class);
    expect(session.getAttribute("loggedUserId")).andReturn(user.getId()).times(2);
    expect(session.getAttribute("breadcrumbHandler")).andReturn(breadcrumbHandler);
    replay(session);
    
    expect(request.getRequestURI()).andReturn(CTXPATH + "/authtest.page");
    expect(request.getContextPath()).andReturn(CTXPATH);
    expect(request.getSession(false)).andReturn(session).times(2);
    expect(request.getSession(true)).andReturn(session);
    
    replay(request);
    
    HttpServletResponse response = createNiceMock(HttpServletResponse.class);
    replay(response);
    
    Properties pages = new Properties();
    pages.setProperty("authtest", AuthorizationTestController.class.getName());
    
    RequestControllerMapper.mapControllers(pages, ".page");
    
    /* Authorization should succeed */
    
    AuthorizationTestController.reset();
    new Servlet().service(request, response);
    Assert.assertEquals(AuthorizationTestController.hasProcessed(), true);
    
    verify(request);
    
    beginTransaction();
    
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    userDAO.deleteUser(userDAO.getUser(user.getId()));
    
    commit();
  }
  
  /* Test if page requests are handled correctly */
  @Test
  public void testPageDelegation() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ServletException, IOException, SecurityException, NoSuchMethodException {
    HttpSession httpSession = createNiceMock(HttpSession.class);
    BreadcrumbHandler breadcrumbHandler = createNiceMock(BreadcrumbHandler.class);
    expect(httpSession.getAttribute("breadcrumbHandler")).andReturn(breadcrumbHandler);
    replay(httpSession);

    HttpServletRequest request = createNiceMock(HttpServletRequest.class);
    expect(request.getRequestURI()).andReturn(CTXPATH + "/delegatetest.page");
    expect(request.getContextPath()).andReturn(CTXPATH);
    expect(request.getSession(true)).andReturn(httpSession);
    replay(request);
    
    Properties pages = new Properties();
    pages.setProperty("delegatetest", PageDelegationTestController.class.getName());
    
    RequestControllerMapper.mapControllers(pages, ".page");
    
    PageDelegationTestController.reset();
    new Servlet().service(request, createNiceMock(HttpServletResponse.class));
    Assert.assertEquals(PageDelegationTestController.isOk(), true);
    
    verify(request);
  }
  
  /* Test if json requests are handled correctly */
  @Test
  public void testJSONDelegation() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ServletException, IOException, SecurityException, NoSuchMethodException {
    HttpServletRequest request = createNiceMock(HttpServletRequest.class);
    expect(request.getRequestURI()).andReturn(CTXPATH + "/delegatetest.json");
    expect(request.getContextPath()).andReturn(CTXPATH);
    replay(request);
    
    HttpServletResponse response = createNiceMock(HttpServletResponse.class);
    expect(response.getWriter()).andReturn(createMock(PrintWriter.class));
    replay(response);
    
    Properties json = new Properties();
    json.setProperty("delegatetest", JSONDelegationTestController.class.getName());
    
    RequestControllerMapper.mapControllers(json, ".json");
    
    JSONDelegationTestController.reset();
    new Servlet().service(request, response);
    Assert.assertEquals(JSONDelegationTestController.isOk(), true);
    
    verify(request);
    verify(response);
  }
  
  @Test
  public void testHibernate() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ServletException, IOException {
    HttpSession httpSession = createNiceMock(HttpSession.class);
    BreadcrumbHandler breadcrumbHandler = createNiceMock(BreadcrumbHandler.class);
    expect(httpSession.getAttribute("breadcrumbHandler")).andReturn(breadcrumbHandler);
    replay(httpSession);

    Properties pages = new Properties();
    pages.setProperty("dbtest", DBTestPageController.class.getName());
    
    RequestControllerMapper.mapControllers(pages, ".page");
    
    HttpServletRequest request = createMock(HttpServletRequest.class);
    
    expect(request.getRequestURI()).andReturn(CTXPATH + "/dbtest.page");
    expect(request.getContextPath()).andReturn(CTXPATH);
    expect(request.getSession(true)).andReturn(httpSession);
    
    replay(request);
    
    new Servlet().service(request, createMock(HttpServletResponse.class));
  }

  public static class DBTestPageController implements PyramusViewController {
    public void process(PageRequestContext pageRequestContext) {
      Assert.assertEquals(HibernateUtils.getSessionFactory().isClosed(), false);
      Assert.assertEquals(HibernateUtils.getSessionFactory().getCurrentSession().isOpen(), true);
      Assert.assertEquals(HibernateUtils.getSessionFactory().getCurrentSession().getTransaction().isActive(), true);
    }
    public UserRole[] getAllowedRoles() {
      return new UserRole[] { UserRole.EVERYONE };
    }
    public String getName() {
      // TODO Auto-generated method stub
      return null;
    }
  }
  
  public static class PageDelegationTestController implements PyramusViewController {
    public void process(PageRequestContext pageRequestContext) {
      ok = true;
    }
    public static boolean isOk() {
      return ok;
    }
    public static void reset() {
      ok = false;
    }
    public UserRole[] getAllowedRoles() {
      return new UserRole[] { UserRole.EVERYONE };
    }
    private static boolean ok = false;
    public String getName() {
      // TODO Auto-generated method stub
      return null;
    }
  }
  
  public static class JSONDelegationTestController extends JSONRequestController {
    public void process(JSONRequestContext jsonRequestContext) {
      ok = true;
    }
    public static boolean isOk() {
      return ok;
    }
    public static void reset() {
      ok = false;
    }
    public UserRole[] getAllowedRoles() {
      return new UserRole[] { UserRole.EVERYONE };
    }
    private static boolean ok = false;
  }
  
  public static class AuthorizationTestController implements PyramusViewController {
    public void process(PageRequestContext pageRequestContext) {
      processed = true;
    }
    public static boolean hasProcessed() {
      return processed;
    }
    public static void reset() {
      processed = false;
    }
    public UserRole[] getAllowedRoles() {
      return new UserRole[] { UserRole.USER };
    }
    private static boolean processed = false;
    public String getName() {
      // TODO Auto-generated method stub
      return null;
    }
  }
}
