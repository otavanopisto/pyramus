package fi.otavanopisto.pyramus.rest.model;

import java.time.OffsetDateTime;
import java.util.List;

public class StudentContactLogWithRecipients extends StudentContactLogEntry {

  public StudentContactLogWithRecipients() {
  }

  public StudentContactLogWithRecipients(Long id, String text, Long creatorId, String creatorName, OffsetDateTime entryDate, StudentContactLogEntryType type, List<StudentContactLogEntryCommentRestModel> comments, Boolean archived, List<Long> recipients) {
    super(id, text, creatorId, creatorName, entryDate, type, comments, archived);
    this.recipients = recipients;
  }

  public List<Long> getRecipients() {
    return recipients;
  }

  public void setRecipients(List<Long> recipients) {
    this.recipients = recipients;
  }

  private List<Long> recipients;
}