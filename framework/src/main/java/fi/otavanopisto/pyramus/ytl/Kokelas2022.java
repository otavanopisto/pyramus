package fi.otavanopisto.pyramus.ytl;

import java.util.ArrayList;
import java.util.List;

/**
 * Kokelas-tietue 2022 muutoksien jälkeen.
 */
public class Kokelas2022 extends AbstractKokelas {

  public void addKoe(Kokelas2022Koe koe) {
    kokeet.add(koe);
  }
  
  public List<Kokelas2022Koe> getKokeet() {
    return kokeet;
  }

  public void setKokeet(List<Kokelas2022Koe> kokeet) {
    this.kokeet = kokeet;
  }

  private List<Kokelas2022Koe> kokeet = new ArrayList<>();
}
