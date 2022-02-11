package fi.otavanopisto.pyramus.views.auditlog;

import java.util.Date;

import org.hibernate.envers.RevisionType;

public class AuditLogItem {

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public RevisionType getType() {
    return type;
  }

  public void setType(RevisionType type) {
    this.type = type;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  private Long id;
  private String user;
  private String target;
  private RevisionType type;
  private Date date;
  private String data;

}
