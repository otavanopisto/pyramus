package fi.pyramus.rest.tranquil.plugins;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.plugins.PluginRepository.class, entityType = TranquilModelType.COMPACT)
public class PluginRepositoryEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  private Long id;

  private String url;

  public final static String[] properties = {"id","url"};
}
