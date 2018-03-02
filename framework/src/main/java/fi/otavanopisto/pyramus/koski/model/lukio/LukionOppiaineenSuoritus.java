package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class LukionOppiaineenSuoritus extends LukionOsasuoritus {

  public LukionOppiaineenSuoritus() {
  }
  
  public LukionOppiaineenSuoritus(LukionOppiaineenTunniste koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }
  
  public LukionOppiaineenTunniste getKoulutusmoduuli() {
    return koulutusmoduuli;
  }
  
  public KoodistoViite<SuorituksenTyyppi> getTyyppi() {
    return tyyppi;
  }
  
  public void addOsasuoritus(LukionKurssinSuoritus lukionKurssinSuoritus) {
    osasuoritukset.add(lukionKurssinSuoritus);
  }
  
  public List<LukionKurssinSuoritus> getOsasuoritukset() {
    return osasuoritukset;
  }

  public void addArviointi(LukionOppiaineenArviointi arviointi) {
    this.arviointi.add(arviointi);
  }
  
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<LukionOppiaineenArviointi> getArviointi() {
    return arviointi;
  }

  public void setKoulutusmoduuli(LukionOppiaineenTunniste koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }

  private LukionOppiaineenTunniste koulutusmoduuli;
  private final List<LukionOppiaineenArviointi> arviointi = new ArrayList<>();
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.lukionoppiaine);
  private final List<LukionKurssinSuoritus> osasuoritukset = new ArrayList<>();
}
