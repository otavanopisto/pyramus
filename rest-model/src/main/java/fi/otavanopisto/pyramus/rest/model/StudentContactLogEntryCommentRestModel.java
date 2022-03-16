package fi.otavanopisto.pyramus.rest.model;

import java.util.Date;

public class StudentContactLogEntryCommentRestModel{
  
  public StudentContactLogEntryCommentRestModel() {
    super();
  }

  public StudentContactLogEntryCommentRestModel(Long id, String text, String creatorName, Date commentDate, Long entryId) {
    super();
    this.id = id;
    this.text = text;
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

  private Long id;
  private Long entryId;
  private String text;
  private String creatorName;
  private Date commentDate;
}