package fi.otavanopisto.pyramus.envers;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@Table(name = "REVINFO")
@RevisionEntity(PyramusRevisionEntityListener.class)
// TODO Could just extend org.hibernate.envers.DefaultRevisionEntity but hibernate_sequences...
public class PyramusRevisionEntity implements Serializable {

  private static final long serialVersionUID = -1383381894391915263L;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Transient
  public Date getRevisionDate() {
    return new Date(timestamp);
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PyramusRevisionEntity)) {
      return false;
    }

    final PyramusRevisionEntity that = (PyramusRevisionEntity) o;
    return id == that.id && timestamp == that.timestamp;
  }

  @Override
  public int hashCode() {
    int result;
    result = id;
    result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "PyramusRevisionEntity(id = " + id + ", revisionDate = " + DateFormat.getDateTimeInstance().format(getRevisionDate()) + ")";
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @RevisionNumber
  private int id;

  @RevisionTimestamp
  private long timestamp;

  private Long userId;

}