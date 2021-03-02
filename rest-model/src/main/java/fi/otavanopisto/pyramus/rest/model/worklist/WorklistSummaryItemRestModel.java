package fi.otavanopisto.pyramus.rest.model.worklist;

import java.time.LocalDate;

/**
 * Single month statistics for worklist summary.
 */
public class WorklistSummaryItemRestModel {

  public LocalDate getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(LocalDate beginDate) {
    this.beginDate = beginDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
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
  private LocalDate beginDate;
  private LocalDate endDate;
  private int count;

}
