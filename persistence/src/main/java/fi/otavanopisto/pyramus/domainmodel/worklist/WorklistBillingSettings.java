package fi.otavanopisto.pyramus.domainmodel.worklist;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
public class WorklistBillingSettings {

  public Long getId() {
    return id;
  }

  public String getSettings() {
    return settings;
  }

  public void setSettings(String settings) {
    this.settings = settings;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Lob
  @Basic (fetch = FetchType.LAZY)
  @Column
  private String settings;

}
