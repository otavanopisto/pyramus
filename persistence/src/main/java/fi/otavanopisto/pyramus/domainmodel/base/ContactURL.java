package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
public class ContactURL {
  
  public Long getId() {
    return id;
  }

  public void setURL(String url) {
    this.url = url;
  }

  public String getURL() {
    return url;
  }

  public void setContactURLType(ContactURLType contactURLType) {
    this.contactURLType = contactURLType;
  }

  public ContactURLType getContactURLType() {
    return contactURLType;
  }

  public void setContactInfo(ContactInfo contactInfo) {
    this.contactInfo = contactInfo;
  }

  public ContactInfo getContactInfo() {
    return contactInfo;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ContactURL")  
  @TableGenerator(name="ContactURL", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;
  
  @ManyToOne
  @JoinColumn (name = "contactURLType")
  private ContactURLType contactURLType;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @Field (store = Store.NO) 
  private String url;

  @ManyToOne
  @JoinColumn(name="contactInfo", insertable=false, updatable=false)
  private ContactInfo contactInfo;

  @Version
  @Column(nullable = false)
  private Long version;
}
