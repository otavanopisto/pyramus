package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Majoitusjakso {

  public Majoitusjakso() {
  }

  public Majoitusjakso(Date alku) {
    this.alku = alku;
  }

  public Majoitusjakso(Date alku, Date loppu) {
    this.alku = alku;
    this.loppu = loppu;
  }

  public Date getAlku() {
    return alku;
  }
  
  public void setAlku(Date alku) {
    this.alku = alku;
  }
  
  public Date getLoppu() {
    return loppu;
  }

  public void setLoppu(Date loppu) {
    this.loppu = loppu;
  }

  private Date alku;
  private Date loppu;
}
