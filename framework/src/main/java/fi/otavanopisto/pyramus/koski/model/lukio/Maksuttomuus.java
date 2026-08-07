package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.Date;

public class Maksuttomuus {

  public Maksuttomuus() {
  }

  public Maksuttomuus(Date alku, boolean maksuton) {
    this.alku = alku;
    this.maksuton = maksuton;
  }
  
  public Date getAlku() {
    return alku;
  }
  
  public void setAlku(Date alku) {
    this.alku = alku;
  }

  public boolean isMaksuton() {
    return maksuton;
  }

  public void setMaksuton(boolean maksuton) {
    this.maksuton = maksuton;
  }

  public Date getLoppu() {
    return loppu;
  }

  public void setLoppu(Date loppu) {
    this.loppu = loppu;
  }

  private Date alku;
  private Date loppu;
  private boolean maksuton;
}
