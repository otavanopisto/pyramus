package fi.pyramus.domainmodel.students;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;

import fi.pyramus.domainmodel.base.ArchivableEntity;
import fi.pyramus.persistence.search.filters.ArchivedEntityFilterFactory;

/**
 * StudentContactLogEntry class defines a message bind to a student
 * when contact has been made between him/her and f.ex. a teacher.
 * 
 * Properties of StudentContactLogEntry
 * - internal id for internal linking
 * - student that the message is linked to
 * - text for textual message or description of the entry
 * - type for classifying the ways of contacts (phone, email etc)
 * - entry date to identify the date/time of message
 * - creatorName to tell who the student was in contact with
 * 
 * @author antti.viljakainen
 */

@Entity
@FullTextFilterDefs (
  @FullTextFilterDef (
     name="ArchivedContactLogEntry",
     impl=ArchivedEntityFilterFactory.class
  )
)
public class StudentContactLogEntry implements ArchivableEntity {

  /**
   * Returns internal unique id
   * 
   * @return unique id
   */
  public Long getId() {
    return id;
  }
  
  /**
   * Sets Student this log entry belongs to
   *  
   * @param student New student
   */
  public void setStudent(Student student) {
    this.student = student;
  }

  /**
   * Returns the student this log entry has been made for
   * 
   * @return The student
   */
  public Student getStudent() {
    return student;
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
   * Sets the type of the contact.
   * 
   * @param type Contact type
   */
  public void setType(StudentContactLogEntryType type) {
    this.type = type;
  }

  /**
   * Returns contact type of this entry.
   * 
   * @return Contact type of this entry
   */
  public StudentContactLogEntryType getType() {
    return type;
  }

  /**
   * Sets entry date for this entry.
   * 
   * @param entryDate New entry date
   */
  public void setEntryDate(Date entryDate) {
    this.entryDate = entryDate;
  }

  /**
   * Returns entry date of this entry.
   * 
   * @return Entry date
   */
  public Date getEntryDate() {
    return entryDate;
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

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentContactLogEntry")  
  @TableGenerator(name="StudentContactLogEntry", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne (optional = false)
  @JoinColumn (name = "student")
  private Student student;
  
  @Lob
  @Column
  private String text;
  
  private String creatorName;

  @Enumerated (EnumType.STRING)
  private StudentContactLogEntryType type;
  
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date entryDate;
  
  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;

  @Version
  @Column(nullable = false)
  private Long version;
}
