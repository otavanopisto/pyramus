package fi.otavanopisto.pyramus.persistence.search;

import java.util.List;

public class SearchResult<E> {

  public SearchResult(int page, int pages, int totalHitCount, int firstResult, int lastResult, List<E> results) {
    this.page = page;
    this.pages = pages;
    this.totalHitCount = totalHitCount;
    this.firstResult = firstResult;
    this.lastResult = lastResult;
    this.results = results;
  }
  
  public int getPage() {
    return page;
  }
  
  public int getPages() {
    return pages;
  }
  
  public int getTotalHitCount() {
    return totalHitCount;
  }
  
  public List<E> getResults() {
    return results;
  }
  
  public int getFirstResult() {
    return firstResult;
  }
  
  public int getLastResult() {
    return lastResult;
  }
  
  private int page;
  private int pages;
  private int totalHitCount;
  private int firstResult;
  private int lastResult;
  private List<E> results;
}
