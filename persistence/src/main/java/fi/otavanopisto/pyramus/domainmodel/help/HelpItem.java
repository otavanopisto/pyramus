package fi.otavanopisto.pyramus.domainmodel.help;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceException;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Entity
@Inheritance (strategy=InheritanceType.JOINED)
@Indexed
public class HelpItem {

  public Long getId() {
    return id;
  }
  
  public HelpFolder getParent() {
    return parent;
  }
  
  public void setParent(HelpFolder parent) {
    this.parent = parent;
  }
  
  public Date getCreated() {
    return created;
  }
  
  public void setCreated(Date created) {
    this.created = created;
  }
  
  public User getCreator() {
    return creator;
  }
  
  public void setCreator(User creator) {
    this.creator = creator;
  }
  
  public Date getLastModified() {
    return lastModified;
  }
  
  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }
  
  public User getLastModifier() {
    return lastModifier;
  }
  
  public void setLastModifier(User lastModifier) {
    this.lastModifier = lastModifier;
  }
  
  public Integer getIndexColumn() {
    return indexColumn;
  }
  
  public void setIndexColumn(Integer indexColumn) {
    this.indexColumn = indexColumn;
  }
  
  public List<HelpItemTitle> getTitles() {
    return titles;
  }
  
  public void setTitles(List<HelpItemTitle> titles) {
    this.titles = titles;
  }
  
  @Transient
  public void addTitle(HelpItemTitle helpItemTitle) {
    if (helpItemTitle.getItem() != null) {
      helpItemTitle.getItem().removeTitle(helpItemTitle);
    }
      
    helpItemTitle.setItem(this);
    titles.add(helpItemTitle);
  }
  
  @Transient
  public void removeTitle(HelpItemTitle helpItemTitle) {
    helpItemTitle.setItem(null);
    titles.remove(helpItemTitle);
  }
  
  @Transient
  public HelpItemTitle getTitleByLocale(Locale locale) {
    for (HelpItemTitle title : titles) {
      if (title.getLocale().equals(locale))
        return title;
    }
    
    return null;
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
  
  @Transient
  @Field (analyze = Analyze.NO, store = Store.YES)
  public String getRecursiveIndex() {
    String result = StringUtils.leftPad(String.valueOf(getIndexColumn() + 1), 3, '0'); 
    
    HelpFolder parent = getParent();
    int depth = 0;
    
    while (depth <= 4) {
      depth++;
      
      int index = 0;
      if (parent != null) {
        index = parent.getIndexColumn() + 1; 
        parent = parent.getParent();
      } 

      result = StringUtils.leftPad(String.valueOf(index), 3, '0') + result; 
    }
    
    return result;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="HelpItem")  
  @TableGenerator(name="HelpItem", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;
  
  @ManyToOne 
  @JoinColumn (name="parent")
  private HelpFolder parent;
  
  @Column (nullable=false)
  @Field (analyze = Analyze.NO, store = Store.YES)
  @NotNull
  private Integer indexColumn;
  
  @Column (nullable=false)
  @NotNull
  @Temporal (TemporalType.TIMESTAMP)
  @Field (analyze = Analyze.NO, store=Store.YES)
  @DateBridge (resolution=Resolution.MILLISECOND)
  private Date lastModified;
  
  @Column (nullable=false)
  @Temporal (TemporalType.TIMESTAMP)
  @org.hibernate.search.annotations.Field (analyze = Analyze.NO, store=Store.YES)
  @DateBridge (resolution=Resolution.MILLISECOND)
  private Date created;

  @ManyToOne
  @JoinColumn (name="creator")
  private User creator;
  
  @ManyToOne
  @JoinColumn (name="lastModifier")
  private User lastModifier;
  
  @OneToMany (cascade = CascadeType.ALL, mappedBy="item")
  @IndexedEmbedded
  private List<HelpItemTitle> titles = new ArrayList<HelpItemTitle>();
  
  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name="__HelpItemTags", joinColumns=@JoinColumn(name="helpItem"), inverseJoinColumns=@JoinColumn(name="tag"))
  @IndexedEmbedded 
  private Set<Tag> tags = new HashSet<Tag>();
} 
