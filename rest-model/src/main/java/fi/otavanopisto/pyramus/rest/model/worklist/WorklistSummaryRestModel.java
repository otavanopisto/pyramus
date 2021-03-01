package fi.otavanopisto.pyramus.rest.model.worklist;

import java.util.List;

/**
 * Object representing the summary of one person's worklist items; includes item
 * counts per month and year.
 */
public class WorklistSummaryRestModel {

  public List<WorklistSummaryItemRestModel> getItems() {
    return items;
  }

  public void setItems(List<WorklistSummaryItemRestModel> items) {
    this.items = items;
  }

  private List<WorklistSummaryItemRestModel> items;

}
