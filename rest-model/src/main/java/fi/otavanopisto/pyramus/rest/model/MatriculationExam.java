package fi.otavanopisto.pyramus.rest.model;

public class MatriculationExam {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getStarts() {
    return starts;
  }

  public void setStarts(Long starts) {
    this.starts = starts;
  }

  public Long getEnds() {
    return ends;
  }

  public void setEnds(Long ends) {
    this.ends = ends;
  }

  private Long id;
  private Long starts;
  private Long ends;

}

