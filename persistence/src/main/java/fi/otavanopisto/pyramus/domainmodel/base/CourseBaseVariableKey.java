package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;


@Entity
public class CourseBaseVariableKey {

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
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseBaseVariableKey")  
  @TableGenerator(name="CourseBaseVariableKey", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @Column (nullable = false)
  private Boolean userEditable = Boolean.FALSE;

  // TODO: Shouldn't this be unique?
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
  @Field (analyze = Analyze.NO, store = Store.NO)
  private VariableType variableType;

  @Version
  @Column(nullable = false)
  private Long version;
}