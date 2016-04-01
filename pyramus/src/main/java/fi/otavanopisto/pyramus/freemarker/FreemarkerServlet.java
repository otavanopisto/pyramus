package fi.otavanopisto.pyramus.freemarker;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fi.otavanopisto.pyramus.plugin.PluginManager;
import freemarker.cache.TemplateLoader;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateModel;

public class FreemarkerServlet extends freemarker.ext.servlet.FreemarkerServlet {

  /**
   * SerialVersionUID
   */
  private static final long serialVersionUID = 2472454788723357007L;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
  
  @Override
  protected TemplateLoader createTemplateLoader(String templatePath) throws IOException {
    if (templatePath.startsWith("class://")) {
      return new ClassLoaderTemplateLoader(PluginManager.getInstance().getPluginsClassLoader(), templatePath.substring(7));
    }
    
    return super.createTemplateLoader(templatePath);
  }
  
  @Override
  protected boolean preTemplateProcess(HttpServletRequest request, HttpServletResponse response, Template template, TemplateModel data) throws ServletException, IOException {
    ((SimpleHash) data).put("contextPath", request.getContextPath());

    return super.preTemplateProcess(request, response, template, data);
  }  
  
  @Override
  protected Locale deduceLocale(String templatePath, HttpServletRequest request, HttpServletResponse response) {
    Locale locale = request.getLocale();
    if (locale != null) {
      return locale;  
    }
    
    return super.deduceLocale(templatePath, request, response);
  }
}
