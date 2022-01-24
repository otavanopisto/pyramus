package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

public class PainotettuArvosana {

  public PainotettuArvosana(double kurssinLaajuus, ArviointiasteikkoYleissivistava arvosana) {
    this.kurssinLaajuus = kurssinLaajuus;
    this.arvosana = arvosana;
  }
  
  public double getKurssinLaajuus() {
    return kurssinLaajuus;
  }

  public ArviointiasteikkoYleissivistava getArvosana() {
    return arvosana;
  }

  private final double kurssinLaajuus;
  private final ArviointiasteikkoYleissivistava arvosana;
}
