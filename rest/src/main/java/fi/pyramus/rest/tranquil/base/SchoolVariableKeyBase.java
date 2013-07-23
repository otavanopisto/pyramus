package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.SchoolVariableKey.class, entityType = TranquilModelType.BASE)
public class SchoolVariableKeyBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getVariableKey() {
    return variableKey;
  }

  public void setVariableKey(String variableKey) {
    this.variableKey = variableKey;
  }

  public String getVariableName() {
    return variableName;
  }

  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }

  public fi.pyramus.domainmodel.base.VariableType getVariableType() {
    return variableType;
  }

  public void setVariableType(fi.pyramus.domainmodel.base.VariableType variableType) {
    this.variableType = variableType;
  }

  public Boolean getUserEditable() {
    return userEditable;
  }

  public void setUserEditable(Boolean userEditable) {
    this.userEditable = userEditable;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private String variableKey;

  private String variableName;

  private fi.pyramus.domainmodel.base.VariableType variableType;

  private Boolean userEditable;

  private Long version;

  public final static String[] properties = {"id","variableKey","variableName","variableType","userEditable","version"};
}
