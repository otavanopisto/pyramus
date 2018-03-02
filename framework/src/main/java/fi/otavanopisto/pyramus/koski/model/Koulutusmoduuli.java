package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Koulutus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Koulutusmoduuli {

  public Koulutusmoduuli() {
  }
  
  public Koulutusmoduuli(Koulutus koulutus) {
    this.tunniste.setValue(koulutus);
  }
  
  public String getPerusteenDiaarinumero() {
    return perusteenDiaarinumero;
  }
  
  public void setPerusteenDiaarinumero(String perusteenDiaarinumero) {
    this.perusteenDiaarinumero = perusteenDiaarinumero;
  }
  
  public KoodistoViite<Koulutus> getTunniste() {
    return tunniste;
  }
  
  public void setTunniste(KoodistoViite<Koulutus> tunniste) {
    this.tunniste = tunniste;
  }

  private String perusteenDiaarinumero;
  private KoodistoViite<Koulutus> tunniste = new KoodistoViite<>();
}
