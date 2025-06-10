package fi.otavanopisto.pyramus.domainmodel.modules;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import fi.otavanopisto.pyramus.domainmodel.base.ComponentBase;

@Entity
@Indexed
@PrimaryKeyJoinColumn(name="id")
public class ModuleComponent extends ComponentBase {

  public Module getModule() {
    return module;
  }
  
  public void setModule(Module module) {
    this.module = module;
  }
  
  @ManyToOne
  @JoinColumn(name="module")
  private Module module;
}
