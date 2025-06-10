package fi.otavanopisto.pyramus.rest.model;

import java.util.List;

public class StudentContactLogEntryBatch {
  
  public StudentContactLogEntryBatch() {
    
  }

  public StudentContactLogEntryBatch(long firstResult, List<StudentContactLogEntry> results, long totalHitCount, Boolean allPrivileges) {
    this.totalHitCount= totalHitCount;
    this.results = results;
    this.firstResult = firstResult;
    this.allPrivileges = allPrivileges;
  }


  public long getTotalHitCount() {
    return totalHitCount;
  }
  public void setTotalHitCount(long totalHitCount) {
    this.totalHitCount = totalHitCount;
  }


  public long getFirstResult() {
    return firstResult;
  }


  public void setFirstResult(long firstResult) {
    this.firstResult = firstResult;
  }


  public List<StudentContactLogEntry> getResults() {
    return results;
  }


  public void setResults(List<StudentContactLogEntry> results) {
    this.results = results;
  }

  public Boolean getAllPrivileges() {
    return allPrivileges;
  }

  public void setAllPrivileges(Boolean allPrivileges) {
    this.allPrivileges = allPrivileges;
  }

  private long totalHitCount;
  private long firstResult;
  private List<StudentContactLogEntry> results;
  private Boolean allPrivileges;

}