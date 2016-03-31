package fi.otavanopisto.pyramus.services.entities.courses;

public class CourseEntitySearchResult {

  public CourseEntitySearchResult() {
  }
  
  public CourseEntitySearchResult(int page, int pages, int totalHitCount, CourseEntity[] results) {
    super();
    this.page = page;
    this.pages = pages;
    this.totalHitCount = totalHitCount;
    this.results = results;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getPages() {
    return pages;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

  public int getTotalHitCount() {
    return totalHitCount;
  }

  public void setTotalHitCount(int totalHitCount) {
    this.totalHitCount = totalHitCount;
  }

  public CourseEntity[] getResults() {
    return results;
  }

  public void setResults(CourseEntity[] results) {
    this.results = results;
  }

  private int page;
  private int pages;
  private int totalHitCount;
  private CourseEntity[] results;
}
