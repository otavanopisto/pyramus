package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.School.class, entityType = TranquilModelType.COMPACT)
public class SchoolCompact extends SchoolBase {

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

  public java.util.List<Long> getVariables_ids() {
    return variables_ids;
  }

  public void setVariables_ids(java.util.List<Long> variables_ids) {
    this.variables_ids = variables_ids;
  }

  public java.util.List<Long> getTags_ids() {
    return tags_ids;
  }

  public void setTags_ids(java.util.List<Long> tags_ids) {
    this.tags_ids = tags_ids;
  }

  private Long contactInfo_id;

  private Long field_id;

  private java.util.List<Long> variables_ids;

  private java.util.List<Long> tags_ids;

  public final static String[] properties = {"contactInfo","field","variables","tags"};
}
