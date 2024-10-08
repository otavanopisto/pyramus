package fi.otavanopisto.pyramus.rest.model;

import java.util.Date;

public class StudentContactLogEntryCommentRestModel{
  
  public StudentContactLogEntryCommentRestModel() {
    super();
  }

  public StudentContactLogEntryCommentRestModel(Long id, String text, Long creatorId, String creatorName, Date commentDate, Long entryId) {
    super();
    this.id = id;
    this.text = text;
    this.setCreatorId(creatorId);
    this.creatorName = creatorName;
    this.commentDate = commentDate;
    this.entryId = entryId;
  }


  public Long getId() {
    return id;
  }
  public void setText(String text) {
    this.text = text;
  }
  
  public String getText() {
    return text;
  }

  public Long getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
  }

  public void setCommentDate(Date commentDate) {
    this.commentDate = commentDate;
  }

  public Date getCommentDate() {
    return commentDate;
  }

  public void setCreatorName(String creator) {
    this.creatorName = creator;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setEntry(Long entry) {
    this.entryId = entry;
  }

  public Long getEntry() {
    return entryId;
  }

  public Boolean getHasImage() {
    return hasImage;
  }

  public void setHasImage(Boolean hasImage) {
    this.hasImage = hasImage;
  }

  private Long id;
  private Long entryId;
  private String text;
  private Long creatorId;
  private String creatorName;
  private Date commentDate;
  private Boolean hasImage;
}