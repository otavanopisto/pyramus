package fi.pyramus.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import fi.pyramus.plugin.PageHookContext;
import fi.pyramus.plugin.PageHookController;
import fi.pyramus.plugin.PageHookVault;

/** This class adds support for extension hooks in JSP pages.
 * Extension hooks allow plugins to add their own content to
 * already existing pages. Usage:
 * <br/>
 * <ul>
 *   <li>Add an <code>ix:extensionhook</code> tag to the page you want extended. The tag
 *   will be substituted with content provided by the plugin:<br/>
 *   <code>&lt;!-- ... contents of the existing page --&gt;</code><br/>
 *   <code>&lt;!-- The plugin-provided content will be added after this line --&gt;</code><br/>
 *   <code>&lt;ix:extensionHook name="<i>the name of the hook</i>"/&gt;</code><br/>
 *   <code>&lt;!-- ... contents of the existing page --&gt;</code><br/> 
 *   </li>
 *   <li>Add a subclass of <code>PageHookController</code> to the
 *   plugin that extends an existing page.</li>
 *   <li>Add an entry of that <code>PageHookController</code> to the
 *   <code>getPageHookControllers</code> method of the
 *   plugin's <code>PluginDescriptor</code>.</li>
 * </ul>
 */
public class ExtensionHookTag extends TagSupport {

  private static final long serialVersionUID = 5603128082864676937L;
  /** The content substitution is done here.
   *  @returns SKIP_BODY
   */
  public int doEndTag() throws javax.servlet.jsp.JspTagException {
    List<PageHookController> hookControllers = PageHookVault.getInstance().getPageHooks(getName());
    if (hookControllers != null) {
      for (PageHookController hookController : hookControllers) {
        PageHookContext pageHookContext = new PageHookContext();

        hookController.execute(pageHookContext);

        if (!StringUtils.isBlank(pageHookContext.getIncludeFtl())) {
          try {
            String includePath = pageHookContext.getIncludeFtl(); 
            pageContext.include(includePath);
          } catch (ServletException e) {
            throw new javax.servlet.jsp.JspTagException(e);
          } catch (IOException e) {
            throw new javax.servlet.jsp.JspTagException(e);
          }
        }
      }
    }

    return SKIP_BODY;
  }

  /** Returns the name of the tag.
   * 
   * @return The name of the tag.
   */
  public String getName() {
    return name;
  }

  /** Sets the name of the tag.
   * 
   * @param name The new name of the tag
   */
  public void setName(String name) {
    this.name = name;
  }

  private String name;

}
