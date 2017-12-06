package fi.otavanopisto.pyramus.koski.lukio;

import java.util.Date;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.LukionOppimaara;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.model.HenkiloUusi;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusJakso;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionSuoritus;

public class LukioOppimaaraData {

  public static Oppija getTestStudentMinimal() {
    Oppija oppija = new Oppija();
    oppija.setHenkilo(new HenkiloUusi("111111A111C", "Sally", "Student", "Sally"));
    
    LukionOpiskeluoikeus opiskeluoikeus = new LukionOpiskeluoikeus();
    oppija.addOpiskeluoikeus(opiskeluoikeus);
    
    OpiskeluoikeusJakso jakso = new OpiskeluoikeusJakso(new Date(), OpiskeluoikeudenTila.lasna);
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(jakso );
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID("1.2.246.562.10.17749422402");
    LukionSuoritus suoritus = new LukionOppimaaranSuoritus(LukionOppimaara.aikuistenops, Kieli.FI, toimipiste, SuorituksenTila.KESKEN);
    opiskeluoikeus.addSuoritus(suoritus);
    
    return oppija;
  }
  
}
