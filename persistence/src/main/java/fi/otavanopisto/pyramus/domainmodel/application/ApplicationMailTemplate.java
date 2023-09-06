package fi.otavanopisto.pyramus.domainmodel.application;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

@Entity
public class ApplicationMailTemplate implements ArchivableEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public StaffMember getStaffMember() {
    return staffMember;
  }

  public void setStaffMember(StaffMember staffMember) {
    this.staffMember = staffMember;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ApplicationMailTemplate")
  @TableGenerator(name="ApplicationMailTemplate", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  private String line;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String name;

  private String subject;

  @Lob
  @NotNull
  @Column (nullable=false)
  private String content;

  @ManyToOne  
  @JoinColumn(name="staffMember")
  private StaffMember staffMember;

  @NotNull
  @Column (nullable = false)
  private Boolean archived;

}