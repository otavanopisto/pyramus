package fi.otavanopisto.pyramus.domainmodel.modules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceException;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.persistence.search.filters.ArchivedEntityFilterFactory;

@Entity
@Indexed
@PrimaryKeyJoinColumn(name="id")
@FullTextFilterDefs (
  @FullTextFilterDef (
     name="ArchivedModule",
     impl=ArchivedEntityFilterFactory.class
  )
)
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
  @IndexColumn (name = "indexColumn")
  @JoinColumn (name="module")
  @IndexedEmbedded
  private List<ModuleComponent> moduleComponents = new Vector<ModuleComponent>();

  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name="__ModuleTags", joinColumns=@JoinColumn(name="module"), inverseJoinColumns=@JoinColumn(name="tag"))
  @IndexedEmbedded 
  private Set<Tag> tags = new HashSet<Tag>();
}
