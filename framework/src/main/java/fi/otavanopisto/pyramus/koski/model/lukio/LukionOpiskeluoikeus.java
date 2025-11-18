package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = JsonDeserializer.None.class)
public class LukionOpiskeluoikeus extends Opiskeluoikeus {

  public LukionOpiskeluoikeus() {
    super(OpiskeluoikeudenTyyppi.lukiokoulutus);
  }

  public void addSuoritus(LukionSuoritus suoritus) {
    suoritukset.add(suoritus);
  }
  
  public Set<LukionSuoritus> getSuoritukset() {
    return suoritukset;
  }

  @JsonProperty("lisätiedot")
  public LukionOpiskeluoikeudenLisatiedot getLisatiedot() {
    return lisatiedot;
  }

  public void setLisatiedot(LukionOpiskeluoikeudenLisatiedot lisatiedot) {
    this.lisatiedot = lisatiedot;
  }

  @JsonProperty("oppimääräSuoritettu")
  public boolean isOppimaaraSuoritettu() {
    return oppimaaraSuoritettu;
  }

  public void setOppimaaraSuoritettu(boolean oppimaaraSuoritettu) {
    this.oppimaaraSuoritettu = oppimaaraSuoritettu;
  }

  @JsonProperty("arvioituPäättymispäivä")
  public Date getArvioituPaattymispaiva() {
    return arvioituPaattymispaiva;
  }

  public void setArvioituPaattymispaiva(Date arvioituPaattymispaiva) {
    this.arvioituPaattymispaiva = arvioituPaattymispaiva;
  }

  private final Set<LukionSuoritus> suoritukset = new HashSet<>();
  private LukionOpiskeluoikeudenLisatiedot lisatiedot;
  private Date arvioituPaattymispaiva;
  private boolean oppimaaraSuoritettu;
}
