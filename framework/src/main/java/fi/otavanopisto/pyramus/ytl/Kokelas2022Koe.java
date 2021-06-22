package fi.otavanopisto.pyramus.ytl;

public class Kokelas2022Koe {

  public Kokelas2022Koe() {
  }
  
  public Kokelas2022Koe(String koodi, boolean maksuton) {
    this.koodi = koodi;
    this.maksuton = maksuton;
  }
  
  public String getKoodi() {
    return koodi;
  }
  
  public void setKoodi(String koodi) {
    this.koodi = koodi;
  }
  
  public boolean isMaksuton() {
    return maksuton;
  }
  
  public void setMaksuton(boolean maksuton) {
    this.maksuton = maksuton;
  }

  private String koodi;
  private boolean maksuton;
}
