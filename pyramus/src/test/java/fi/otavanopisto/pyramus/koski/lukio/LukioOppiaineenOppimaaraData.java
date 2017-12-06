package fi.otavanopisto.pyramus.koski.lukio;

import java.util.Date;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.model.HenkiloUusi;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusJakso;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusMuuValtakunnallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionSuoritus;

public class LukioOppiaineenOppimaaraData {
  
  public static Oppija getTestStudentMinimal() {
    Oppija oppija = new Oppija();
    oppija.setHenkilo(new HenkiloUusi("111111A111C", "Sally", "Student", "Sally"));
    
    LukionOpiskeluoikeus opiskeluoikeus = new LukionOpiskeluoikeus();
    oppija.addOpiskeluoikeus(opiskeluoikeus);
    
    OpiskeluoikeusJakso jakso = new OpiskeluoikeusJakso(new Date(), OpiskeluoikeudenTila.lasna);
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(jakso );
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID("1.2.246.562.10.17749422402");
    LukionOppiaineenTunniste koulutusmoduuli = new LukionOppiaineenSuoritusMuuValtakunnallinen(
        KoskiOppiaineetYleissivistava.HI, false);
    LukionOppiaineenSuoritus oppiaine = new LukionOppiaineenSuoritus(koulutusmoduuli);
    LukionSuoritus suoritus = new LukionOppiaineenOppimaaranSuoritus(Kieli.FI, toimipiste, SuorituksenTila.KESKEN, oppiaine);
    opiskeluoikeus.addSuoritus(suoritus);
    
    return oppija;
  }
  
}
