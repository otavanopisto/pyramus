package fi.otavanopisto.pyramus.domainmodel.students;

import java.util.Date;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

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
 */
@Entity
public class StudentGroupContactLogEntry implements ArchivableEntity {

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

  public StudentGroup getStudentGroup() {
    return studentGroup;
  }

  public void setStudentGroup(StudentGroup studentGroup) {
    this.studentGroup = studentGroup;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentGroupContactLogEntry")  
  @TableGenerator(name="StudentGroupContactLogEntry", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne (optional = false)
  @JoinColumn (name = "studentGroup")
  private StudentGroup studentGroup;
  
  @Lob
  private String text;
  
  private String creatorName;

  @Enumerated (EnumType.STRING)
  private StudentContactLogEntryType type;
  
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date entryDate;
  
  @NotNull
  @Column (nullable = false)
  @GenericField
  private Boolean archived;

  @Version
  @Column(nullable = false)
  private Long version;
}