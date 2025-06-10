package fi.otavanopisto.pyramus.domainmodel.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Transient;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Entity
@Indexed
@PrimaryKeyJoinColumn (name="id")
public class HelpPage extends HelpItem {
  
  public List<HelpPageContent> getContents() {
    return contents;
  }
  
  public void setContents(List<HelpPageContent> contents) {
    this.contents = contents;
  }
  
  @Transient
  public void addContent(HelpPageContent helpPageContent) {
    if (helpPageContent.getPage() != null) {
      helpPageContent.getPage().removeContent(helpPageContent);
    }
      
    helpPageContent.setPage(this);
    contents.add(helpPageContent);
  }
  
  @Transient
  public void removeContent(HelpPageContent helpPageContent) {
    helpPageContent.setPage(null);
    contents.remove(helpPageContent);
  }
  
  @Transient
  public HelpPageContent getContentByLocale(Locale locale) {
    for (HelpPageContent content : contents) {
      if (content.getLocale().equals(locale))
        return content;
    }
    
    return null;
  }
  
  @OneToMany (cascade = CascadeType.ALL, mappedBy="page")
  @IndexedEmbedded
  private List<HelpPageContent> contents = new ArrayList<>();
}
