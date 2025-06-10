package fi.otavanopisto.pyramus.domainmodel.help;

import java.util.Date;
import java.util.Locale;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.engine.backend.types.Projectable;

import fi.otavanopisto.pyramus.domainmodel.users.User;

@Entity
@Indexed
public class HelpItemTitle {

  public Long getId() {
    return id;
  }
  
  public Locale getLocale() {
    return locale;
  }
  
  public void setLocale(Locale locale) {
    this.locale = locale;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public HelpItem getItem() {
    return item;
  }
  
  protected void setItem(HelpItem item) {
    this.item = item;
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

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="HelpItemTitle")  
  @TableGenerator(name="HelpItemTitle", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;
  
  @ManyToOne 
  @JoinColumn (name="item")
  private HelpItem item;
  
  @Column (nullable=false)
  @NotNull
  private Locale locale;
  
  @Column (nullable=false)
  @NotNull
  @NotEmpty  
  @FullTextField
  private String title;
  
  @Column (nullable=false)
  @NotNull
  @Temporal (TemporalType.TIMESTAMP)
  @GenericField (projectable = Projectable.YES)
  private Date lastModified;
  
  @Column (nullable=false)
  @Temporal (TemporalType.TIMESTAMP)
  @GenericField (projectable = Projectable.YES)
  private Date created;

  @ManyToOne
  @JoinColumn (name="creator")
  private User creator;
  
  @ManyToOne
  @JoinColumn (name="lastModifier")
  private User lastModifier;
}
