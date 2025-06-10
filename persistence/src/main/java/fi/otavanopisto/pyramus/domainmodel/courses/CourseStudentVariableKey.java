package fi.otavanopisto.pyramus.domainmodel.courses;

import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class CourseStudentVariableKey {

  public Long getId() {
    return id;
  }
  
  public void setVariableKey(String variableKey) {
    this.variableKey = variableKey;
  }
  
  public String getVariableKey() {
    return variableKey;
  }

  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }

  public String getVariableName() {
    return variableName;
  }

  public void setVariableType(VariableType variableType) {
    this.variableType = variableType;
  }

  public VariableType getVariableType() {
    return variableType;
  }
  
  public Boolean getUserEditable() {
    return userEditable;
  }
  
  public void setUserEditable(Boolean userEditable) {
    this.userEditable = userEditable;
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseStudentVariableKey")  
  @TableGenerator(name="CourseStudentVariableKey", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @Column (nullable = false)
  private Boolean userEditable = Boolean.FALSE;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String variableKey;
  
  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String variableName;

  @Column 
  @Enumerated (EnumType.STRING)  
  @KeywordField(projectable = Projectable.NO)
  private VariableType variableType;

  @Version
  @Column(nullable = false)
  private Long version;
}
