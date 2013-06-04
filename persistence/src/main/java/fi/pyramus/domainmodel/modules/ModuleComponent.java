package fi.pyramus.domainmodel.modules;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.search.annotations.Indexed;

import fi.pyramus.domainmodel.base.ComponentBase;

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
