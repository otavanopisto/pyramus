package fi.otavanopisto.pyramus.rest.model.worklist;

import java.time.LocalDate;

/**
 * Rest object for changing the state of all worklist item entries of the given user in the given timeframe.
 */
public class WorklistItemStateChangeRestModel {

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

  public String getUserIdentifier() {
    return userIdentifier;
  }

  public void setUserIdentifier(String userIdentifier) {
    this.userIdentifier = userIdentifier;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  private String userIdentifier;
  private LocalDate beginDate;
  private LocalDate endDate;
  private String state;

}
