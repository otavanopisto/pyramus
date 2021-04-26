package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenArviointi;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class LukionOppiaineenSuoritus2019 extends LukionOsasuoritus2019 {

  public LukionOppiaineenSuoritus2019() {
  }
  
  public LukionOppiaineenSuoritus2019(LukionOppiaineenTunniste2019 koulutusmoduuli, boolean suoritettuErityisenäTutkintona) {
    this.koulutusmoduuli = koulutusmoduuli;
    this.suoritettuErityisenäTutkintona = suoritettuErityisenäTutkintona;
  }
  
  public LukionOppiaineenTunniste2019 getKoulutusmoduuli() {
    return koulutusmoduuli;
  }
  
  public KoodistoViite<SuorituksenTyyppi> getTyyppi() {
    return tyyppi;
  }
  
  public void addArviointi(LukionOppiaineenArviointi arviointi) {
    this.arviointi.add(arviointi);
  }
  
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<LukionOppiaineenArviointi> getArviointi() {
    return arviointi;
  }

  public void setKoulutusmoduuli(LukionOppiaineenTunniste2019 koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }

  public boolean isSuoritettuErityisenäTutkintona() {
    return suoritettuErityisenäTutkintona;
  }

  public void setSuoritettuErityisenäTutkintona(boolean suoritettuErityisenäTutkintona) {
    this.suoritettuErityisenäTutkintona = suoritettuErityisenäTutkintona;
  }

  private boolean suoritettuErityisenäTutkintona;
  private LukionOppiaineenTunniste2019 koulutusmoduuli;
  private final List<LukionOppiaineenArviointi> arviointi = new ArrayList<>();
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.lukionoppiaine);
}
