package fi.otavanopisto.pyramus.koski.model.apa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class APAKoulutusmoduuli {

  public APAKoulutusmoduuli() {
  }
  
  public APAKoulutusmoduuli(SuorituksenTyyppi tunniste) {
    this.tunniste.setValue(tunniste);
  }
  
  public String getPerusteenDiaarinumero() {
    return perusteenDiaarinumero;
  }
  
  public void setPerusteenDiaarinumero(String perusteenDiaarinumero) {
    this.perusteenDiaarinumero = perusteenDiaarinumero;
  }
  
  public KoodistoViite<SuorituksenTyyppi> getTunniste() {
    return tunniste;
  }
  
  public void setTunniste(KoodistoViite<SuorituksenTyyppi> tunniste) {
    this.tunniste = tunniste;
  }

  private String perusteenDiaarinumero;
  private KoodistoViite<SuorituksenTyyppi> tunniste = new KoodistoViite<>();
}
