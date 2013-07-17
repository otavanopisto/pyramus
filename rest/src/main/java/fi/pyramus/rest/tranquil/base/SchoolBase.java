package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.School.class, entityType = TranquilModelType.BASE)
public class SchoolBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNameSortable() {
    return nameSortable;
  }

  public void setNameSortable(String nameSortable) {
    this.nameSortable = nameSortable;
  }

  public java.util.Map<java.lang.String,java.lang.String> getVariablesAsStringMap() {
    return variablesAsStringMap;
  }

  public void setVariablesAsStringMap(java.util.Map<java.lang.String,java.lang.String> variablesAsStringMap) {
    this.variablesAsStringMap = variablesAsStringMap;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public java.util.Set<fi.pyramus.domainmodel.base.Tag> getTags() {
    return tags;
  }

  public void setTags(java.util.Set<fi.pyramus.domainmodel.base.Tag> tags) {
    this.tags = tags;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private String name;

  private String nameSortable;

  private java.util.Map<java.lang.String,java.lang.String> variablesAsStringMap;

  private String code;

  private Boolean archived;

  private java.util.Set<fi.pyramus.domainmodel.base.Tag> tags;

  private Long version;

  public final static String[] properties = {"id","name","nameSortable","variablesAsStringMap","code","archived","tags","version"};
}
