package fi.otavanopisto.pyramus.views.system;

import java.util.ArrayList;
import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.plugins.PluginDAO;
import fi.otavanopisto.pyramus.dao.plugins.PluginRepositoryDAO;
import fi.otavanopisto.pyramus.domainmodel.plugins.Plugin;
import fi.otavanopisto.pyramus.domainmodel.plugins.PluginRepository;
import fi.otavanopisto.pyramus.framework.PyramusFormViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.plugin.PluginManager;

public class PluginsViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    PluginRepositoryDAO pluginRepositoryDAO = DAOFactory.getInstance().getPluginRepositoryDAO();
    PluginDAO pluginDAO = DAOFactory.getInstance().getPluginDAO();
    
    PluginManager pluginManager = PluginManager.getInstance();

    List<PluginRepository> pluginRepositories = pluginRepositoryDAO.listAll();
    List<Plugin> plugins = pluginDAO.listAll();
    List<PluginBean> pluginBeans = new ArrayList<PluginsViewController.PluginBean>(plugins.size());
    for (Plugin plugin : plugins) {
      String status;
      boolean loaded = pluginManager.isLoaded(plugin.getGroupId(), plugin.getArtifactId(), plugin.getVersion());
      boolean deletable = false;
      
      if (loaded) {
        if (plugin.getEnabled())
          status = Messages.getInstance().getText(requestContext.getRequest().getLocale(), "system.plugins.pluginsTableStatusLoaded");
        else
          status = Messages.getInstance().getText(requestContext.getRequest().getLocale(), "system.plugins.pluginsTableStatusUnloadOnRestart");
      } else {
        if (plugin.getEnabled())
          status = Messages.getInstance().getText(requestContext.getRequest().getLocale(), "system.plugins.pluginsTableStatusLoadOnRestart");
        else
          status = Messages.getInstance().getText(requestContext.getRequest().getLocale(), "system.plugins.pluginsTableStatusNotLoaded");
        deletable = true;
      }
      
      PluginBean pluginBean = new PluginBean(plugin.getId(), plugin.getGroupId(), plugin.getArtifactId(), plugin.getVersion(), plugin.getEnabled(), deletable, status);
      pluginBeans.add(pluginBean);
    }

    requestContext.getRequest().setAttribute("repositories", pluginRepositories);
    requestContext.getRequest().setAttribute("plugins", pluginBeans);
/**
    Messages.getInstance().getText(requestContext.getRequest().getLocale(), "system.plugins.pluginsTableStatusNotLoaded");
    Messages.getInstance().getText(requestContext.getRequest().getLocale(), "system.plugins.pluginsTableStatusUnloadOnRestart");
    Messages.getInstance().getText(requestContext.getRequest().getLocale(), "system.plugins.pluginsTableStatusLoaded");
**/
    requestContext.setIncludeJSP("/templates/system/plugins.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
    PluginDAO pluginDAO = DAOFactory.getInstance().getPluginDAO();
    PluginRepositoryDAO pluginRepositoryDAO = DAOFactory.getInstance().getPluginRepositoryDAO();

    Long pluginsRowCount = requestContext.getLong("pluginsTable.rowCount");
    for (int i = 0; i < pluginsRowCount; i++) {
      String colPrefix = "pluginsTable." + i;

      Long id = requestContext.getLong(colPrefix + ".id");
      Boolean enabled = "1".equals(requestContext.getString(colPrefix + ".enabled"));
      Boolean remove = "1".equals(requestContext.getString(colPrefix + ".remove"));
      String groupId = requestContext.getString(colPrefix + ".groupId");
      String artifactId = requestContext.getString(colPrefix + ".artifactId");
      String version = requestContext.getString(colPrefix + ".version");

      if (id == null) {
        pluginDAO.create(artifactId, groupId, version, enabled);
      } else {
        Plugin plugin = pluginDAO.findById(id);
        if (remove == true) {
          pluginDAO.delete(plugin);
        } else {
          pluginDAO.updateEnabled(plugin, enabled);
          pluginDAO.updateArtifactId(plugin, artifactId);
          pluginDAO.updateGroupId(plugin, groupId);
          pluginDAO.updateVersion(plugin, version);
        }
      }
    }

    Long repositoriesRowCount = requestContext.getLong("repositoriesTable.rowCount");
    for (int i = 0; i < repositoriesRowCount; i++) {
      String colPrefix = "repositoriesTable." + i;

      Long id = requestContext.getLong(colPrefix + ".id");
      String url = requestContext.getString(colPrefix + ".url");
      String repositoryId = requestContext.getString(colPrefix + ".repositoryId");
      Boolean remove = "1".equals(requestContext.getString(colPrefix + ".remove"));

      if (id == null) {
        pluginRepositoryDAO.create(url, repositoryId);
      } else {
        PluginRepository repository = pluginRepositoryDAO.findById(id);
        if (remove == true) {
          pluginRepositoryDAO.delete(repository);
        } else {
          pluginRepositoryDAO.update(repository, url, repositoryId);
        }
      }
    }

    processForm(requestContext);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

  public class PluginBean {

    public PluginBean(Long id, String groupId, String artifactId, String version, Boolean enabled, Boolean deletable, String status) {
      this.id = id;
      this.groupId = groupId;
      this.artifactId = artifactId;
      this.version = version;
      this.enabled = enabled;
      this.deletable = deletable;
      this.status = status;
    }

    public Long getId() {
      return id;
    }

    public String getGroupId() {
      return groupId;
    }

    public String getArtifactId() {
      return artifactId;
    }

    public String getVersion() {
      return version;
    }

    public Boolean getEnabled() {
      return enabled;
    }
    
    public Boolean getDeletable() {
      return deletable;
    }

    public String getStatus() {
      return status;
    }

    private Long id;
    private String groupId;
    private String artifactId;
    private String version;
    private Boolean enabled;
    private Boolean deletable;
    private String status;
  }
}
