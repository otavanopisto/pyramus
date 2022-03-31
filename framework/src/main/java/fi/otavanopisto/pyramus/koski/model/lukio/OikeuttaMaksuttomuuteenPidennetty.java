package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.Date;

public class OikeuttaMaksuttomuuteenPidennetty {

  public OikeuttaMaksuttomuuteenPidennetty() {
  }
  
  public OikeuttaMaksuttomuuteenPidennetty(Date alku, Date loppu) {
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
