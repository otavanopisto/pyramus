package fi.otavanopisto.pyramus.rest.model;

import java.time.OffsetDateTime;
import java.util.List;

public class StudentContactLogEntry {

  public StudentContactLogEntry() {
    super();
  }

  public StudentContactLogEntry(Long id, String text, Long creatorId, String creatorName, OffsetDateTime entryDate, StudentContactLogEntryType type, List<StudentContactLogEntryCommentRestModel> comments, Boolean archived) {
    super();
    this.id = id;
    this.text = text;
    this.creatorId = creatorId;
    this.creatorName = creatorName;
    this.entryDate = entryDate;
    this.type = type;
    this.comments = comments;
    this.archived = archived;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Long getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  public OffsetDateTime getEntryDate() {
    return entryDate;
  }

  public void setEntryDate(OffsetDateTime entryDate) {
    this.entryDate = entryDate;
  }

  public StudentContactLogEntryType getType() {
    return type;
  }

  public void setType(StudentContactLogEntryType type) {
    this.type = type;
  }

  public List<StudentContactLogEntryCommentRestModel> getComments() {
    return comments;
  }

  public void setComments(List<StudentContactLogEntryCommentRestModel> comments) {
    this.comments = comments;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Boolean getHasImage() {
    return hasImage;
  }

  public void setHasImage(Boolean hasImage) {
    this.hasImage = hasImage;
  }

  public MessageRecipientList getRecipients() {
    return recipients;
  }

  public void setRecipients(MessageRecipientList recipients) {
    this.recipients = recipients;
  }

  private Long id;
  private String text;
  private Long creatorId;
  private String creatorName;
  private OffsetDateTime entryDate;
  private StudentContactLogEntryType type;
  private List<StudentContactLogEntryCommentRestModel> comments; 
  private Boolean archived;
  private Boolean hasImage;
  private MessageRecipientList recipients;
}