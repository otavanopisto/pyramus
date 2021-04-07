package fi.otavanopisto.pyramus.rest.model;

import java.time.LocalDate;

public class OrganizationContractPeriod {

  public OrganizationContractPeriod() {
  }
  
  public OrganizationContractPeriod(Long id, LocalDate begin, LocalDate end) {
    this.id = id;
    this.begin = begin;
    this.end = end;
  }
  
  public LocalDate getBegin() {
    return begin;
  }
  
  public void setBegin(LocalDate begin) {
    this.begin = begin;
  }
  
  public LocalDate getEnd() {
    return end;
  }
  
  public void setEnd(LocalDate end) {
    this.end = end;
  }
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  private Long id;
  private LocalDate begin;
  private LocalDate end;
}
