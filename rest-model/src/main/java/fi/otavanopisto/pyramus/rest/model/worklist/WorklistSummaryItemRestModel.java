package fi.otavanopisto.pyramus.rest.model.worklist;

import java.time.OffsetDateTime;

/**
 * Single month statistics for worklist summary.
 */
public class WorklistSummaryItemRestModel {

  public OffsetDateTime getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(OffsetDateTime beginDate) {
    this.beginDate = beginDate;
  }

  public OffsetDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public void incCount() {
    count++;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  private String displayName;
  private OffsetDateTime beginDate;
  private OffsetDateTime endDate;
  private int count;

}
