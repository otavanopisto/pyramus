package fi.otavanopisto.pyramus.domainmodel.modules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.PrimaryKeyJoinColumn;

import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;

@Entity
@Indexed
@PrimaryKeyJoinColumn(name="id")
public class Module extends CourseBase implements ArchivableEntity {

  public List<ModuleComponent> getModuleComponents() {
    return moduleComponents;
  }
  
  @SuppressWarnings("unused")
  private void setModuleComponents(List<ModuleComponent> moduleComponents) {
    this.moduleComponents = moduleComponents;
  }
  
  public void addModuleComponent(ModuleComponent moduleComponent) {
    if (moduleComponent.getModule() != null)
      moduleComponent.getModule().getModuleComponents().remove(moduleComponent);
    moduleComponent.setModule(this);
    this.moduleComponents.add(moduleComponent);
  }
  
  public void removeModuleComponent(ModuleComponent moduleComponent) {
    moduleComponent.setModule(null);
    this.moduleComponents.remove(moduleComponent);
  } 

  public Set<Tag> getTags() {
    return tags;
  }
  
  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }
  
  public void addTag(Tag tag) {
    if (!tags.contains(tag)) {
      tags.add(tag);
    } else {
      throw new PersistenceException("Entity already has this tag");
    }
  }
  
  public void removeTag(Tag tag) {
    if (tags.contains(tag)) {
      tags.remove(tag);
    } else {
      throw new PersistenceException("Entity does not have this tag");
    }
  }

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn (name = "indexColumn")
  @JoinColumn (name="module")
  @IndexedEmbedded
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private List<ModuleComponent> moduleComponents = new Vector<>();

  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name="__ModuleTags", joinColumns=@JoinColumn(name="module"), inverseJoinColumns=@JoinColumn(name="tag"))
  @IndexedEmbedded 
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private Set<Tag> tags = new HashSet<>();
}
