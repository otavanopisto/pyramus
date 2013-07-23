package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.School.class, entityType = TranquilModelType.COMPACT)
public class SchoolEntity implements fi.tranquil.TranquilModelEntity {

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

  public Long getContactInfo_id() {
    return contactInfo_id;
  }

  public void setContactInfo_id(Long contactInfo_id) {
    this.contactInfo_id = contactInfo_id;
  }

  public Long getField_id() {
    return field_id;
  }

  public void setField_id(Long field_id) {
    this.field_id = field_id;
  }

  private Long id;

  private String name;

  private String code;

  private Boolean archived;

  private Long version;

  private Long contactInfo_id;

  private Long field_id;

  public final static String[] properties = {"id","name","code","archived","version","contactInfo","field"};
}
