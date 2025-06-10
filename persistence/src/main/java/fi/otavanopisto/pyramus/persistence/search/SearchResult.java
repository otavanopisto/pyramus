package fi.otavanopisto.pyramus.persistence.search;

import java.util.List;

public class SearchResult<E> {

  public SearchResult(long page, long pages, long totalHitCount, long firstResult, long lastResult, List<E> results) {
    this.page = page;
    this.pages = pages;
    this.totalHitCount = totalHitCount;
    this.firstResult = firstResult;
    this.lastResult = lastResult;
    this.results = results;
  }
  
  public long getPage() {
    return page;
  }
  
  public long getPages() {
    return pages;
  }
  
  public long getTotalHitCount() {
    return totalHitCount;
  }
  
  public List<E> getResults() {
    return results;
  }
  
  public long getFirstResult() {
    return firstResult;
  }
  
  public long getLastResult() {
    return lastResult;
  }
  
  private long page;
  private long pages;
  private long totalHitCount;
  private long firstResult;
  private long lastResult;
  private List<E> results;
}
