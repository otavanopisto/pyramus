package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.Subject.class, entityType = TranquilModelType.COMPACT)
public class SubjectEntity implements fi.tranquil.TranquilModelEntity {

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

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getEducationType_id() {
    return educationType_id;
  }

  public void setEducationType_id(Long educationType_id) {
    this.educationType_id = educationType_id;
  }

  private Long id;

  private String name;

  private String code;

  private Boolean archived;

  private Long version;

  private Long educationType_id;

  public final static String[] properties = {"id","name","code","archived","version","educationType"};
}
