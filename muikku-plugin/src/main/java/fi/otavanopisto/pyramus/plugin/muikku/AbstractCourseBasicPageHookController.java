package fi.otavanopisto.pyramus.plugin.muikku;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.plugin.PageHookContext;
import fi.otavanopisto.pyramus.plugin.PageHookController;

public abstract class AbstractCourseBasicPageHookController implements PageHookController {

  @Override
  public void execute(PageHookContext pageHookContext) {
    String muikkuHost = MuikkuPluginTools.getMuikkuHost();
    if (StringUtils.isNotBlank(muikkuHost)) {
      PageContext pageContext = pageHookContext.getPageContext();
      Object courseObject = pageContext.getRequest().getAttribute("course");
      if (courseObject instanceof Course) {
        Long courseId = ((Course) courseObject).getId();
        pageContext.getRequest().setAttribute("muikkuWorkspaceUrl", String.format("http://%s/pyramusWorkspaceRedirect?courseId=%d", muikkuHost, courseId));
        pageHookContext.setIncludeFtl("/plugin/muikku/editcoursebasichook.ftl");
      }
    }
  }
  
}
