package fi.otavanopisto.pyramus.plugin.simple.hooks;

import fi.otavanopisto.pyramus.plugin.PageHookContext;
import fi.otavanopisto.pyramus.plugin.PageHookController;

/**
 * The controller responsible of the Edit Course tab hook of the application.
 * 
 */
public class EditCourseSimpleTabHook implements PageHookController {
  
  /**
   * Executes the tab hook.
   * 
   * @param pageHookContext Page hook context
   */
  public void execute(PageHookContext pageHookContext) {
    pageHookContext.setIncludeFtl("/plugin/simple/ftl/editcoursesimpletabhook.ftl");
  }
}
