package fi.otavanopisto.pyramus.domainmodel.help;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
@PrimaryKeyJoinColumn (name="id")
public class HelpFolder extends HelpItem {

  public List<HelpItem> getChildren() {
    return children;
  }
  
  @SuppressWarnings("unused")
  private void setChildren(List<HelpItem> children) {
    this.children = children;
  }
  
  public void addChild(HelpItem helpItem) {
    if (helpItem.getParent() != null)
      helpItem.getParent().removeChild(helpItem);
    
    helpItem.setParent(this);
    children.add(helpItem);
  }
  
  public void removeChild(HelpItem helpItem) {
    if (!helpItem.getParent().getId().equals(this.getId())) 
      throw new RuntimeException("item is not child of this folder");
    
    helpItem.setParent(null);
    children.remove(helpItem);
  }
  
  @OneToMany (cascade = CascadeType.ALL, mappedBy="parent")
  private List<HelpItem> children = new ArrayList<>();
}
