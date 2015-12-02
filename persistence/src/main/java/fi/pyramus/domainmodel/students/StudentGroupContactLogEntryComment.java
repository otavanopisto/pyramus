package fi.pyramus.domainmodel.students;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;

import fi.pyramus.domainmodel.base.ArchivableEntity;

/**
 * StudentContactLogEntryComment class defines a message bind to a contact log entry
 * when contact has been made between him/her and f.ex. a teacher.
 * 
 * Properties of StudentContactLogEntryComment
 * - internal id for internal linking
 * - entry that the message is linked to
 * - text for textual message or description of the entry
 * - entry date to identify the date/time of message
 * - creatorName to tell who the student was in contact with
 */
@Entity
public class StudentGroupContactLogEntryComment implements ArchivableEntity {

  /**
   * Returns internal unique id
   * 
   * @return unique id
   */
  public Long getId() {
    return id;
  }
  
  /**
   * Sets textual message or description associated with this entry
   * 
   * @param text Textual message or description
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Returns textual message or description associated with this entry.
   * 
   * @return Textual mesasge or description
   */
  public String getText() {
    return text;
  }

  /**
   * Sets date for this comment.
   * 
   * @param entryDate New date
   */
  public void setCommentDate(Date commentDate) {
    this.commentDate = commentDate;
  }

  /**
   * Returns date of this comment.
   * 
   * @return Entry date
   */
  public Date getCommentDate() {
    return commentDate;
  }

  /**
   * Sets creator for this entry.
   * 
   * @param creator New creator
   */
  public void setCreatorName(String creator) {
    this.creatorName = creator;
  }

  /**
   * Returns the creator of this entry.
   * 
   * @return The creator
   */
  public String getCreatorName() {
    return creatorName;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public void setEntry(StudentGroupContactLogEntry entry) {
    this.entry = entry;
  }

  public StudentGroupContactLogEntry getEntry() {
    return entry;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentGroupContactLogEntryComment")  
  @TableGenerator(name="StudentGroupContactLogEntryComment", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne
  @JoinColumn (name = "entry")
  private StudentGroupContactLogEntry entry;
  
  @Lob
  @Column
  private String text;
  
  private String creatorName;

  @Temporal (value=TemporalType.TIMESTAMP)
  private Date commentDate;
  
  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived;

  @Version
  @Column(nullable = false)
  private Long version;
}