package fi.otavanopisto.pyramus.koski.lukio;

import java.util.Date;

import fi.otavanopisto.pyramus.koski.AbstractKoskiData;
import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssit;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.model.HenkiloUusi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiNumeerinen;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiSanallinen;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusJakso;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunniste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnistePaikallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinTunnisteValtakunnallinenOPS2015;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusMuuValtakunnallinen;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionSuoritus;

public class LukioOppiaineenOppimaaraData extends AbstractKoskiData {
  
  public static Oppija getTestStudentMinimal() {
    Oppija oppija = new Oppija();
    oppija.setHenkilo(new HenkiloUusi("111111A111C", "Sally", "Student", "Sally"));
    
    LukionOpiskeluoikeus opiskeluoikeus = new LukionOpiskeluoikeus();
    oppija.addOpiskeluoikeus(opiskeluoikeus);
    
    OpiskeluoikeusJakso jakso = new OpiskeluoikeusJakso(new Date(), OpiskeluoikeudenTila.lasna);
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(jakso );
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(ACADEMYOID);
    LukionOppiaineenTunniste koulutusmoduuli = new LukionOppiaineenSuoritusMuuValtakunnallinen(
        KoskiOppiaineetYleissivistava.HI, false);
    LukionSuoritus suoritus = new LukionOppiaineenOppimaaranSuoritus(koulutusmoduuli, Kieli.FI, toimipiste);
    opiskeluoikeus.addSuoritus(suoritus);
    
    return oppija;
  }
  
  public static Oppija getTestStudent() {
    Oppija oppija = new Oppija();
    oppija.setHenkilo(new HenkiloUusi("111111A111C", "Sally", "Student", "Sally"));
    
    LukionOpiskeluoikeus opiskeluoikeus = new LukionOpiskeluoikeus();
    oppija.addOpiskeluoikeus(opiskeluoikeus);
    
    Date paattymispaiva = date(2017, 10, 10);
    opiskeluoikeus.setPaattymispaiva(paattymispaiva);

    OpiskeluoikeusJakso jakso = new OpiskeluoikeusJakso(new Date(), OpiskeluoikeudenTila.lasna);
    jakso.setOpintojenRahoitus(new KoodistoViite<OpintojenRahoitus>(OpintojenRahoitus.K1));
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(jakso);
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(new OpiskeluoikeusJakso(paattymispaiva, OpiskeluoikeudenTila.valmistunut));
    opiskeluoikeus.setLahdejarjestelmanId(getLahdeJarjestelmaID(1l));
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(ACADEMYOID);
    LukionOppiaineenTunniste koulutusmoduuli = new LukionOppiaineenSuoritusMuuValtakunnallinen(
        KoskiOppiaineetYleissivistava.HI, false);
    LukionOppiaineenOppimaaranSuoritus suoritus = new LukionOppiaineenOppimaaranSuoritus(koulutusmoduuli, Kieli.FI, toimipiste);
    opiskeluoikeus.addSuoritus(suoritus);
    
    // Kurssi 1
    LukionKurssinTunniste kurssinTunniste = new LukionKurssinTunnisteValtakunnallinenOPS2015(LukionKurssit.ÄI1, LukionKurssinTyyppi.pakollinen);
    LukionKurssinSuoritus kurssi = new LukionKurssinSuoritus(kurssinTunniste);
    kurssi.addArviointi(new KurssinArviointiNumeerinen(ArviointiasteikkoYleissivistava.GRADE_9, paattymispaiva));
    suoritus.addOsasuoritus(kurssi);
    
    // Kurssi 2
    kurssinTunniste = new LukionKurssinTunnistePaikallinen(new PaikallinenKoodi("ÄI123", kuvaus("ABC")), LukionKurssinTyyppi.syventava, kuvaus("ABC"));
    kurssi = new LukionKurssinSuoritus(kurssinTunniste);
    kurssi.addArviointi(new KurssinArviointiSanallinen(ArviointiasteikkoYleissivistava.GRADE_S, paattymispaiva, kuvaus("S")));
    suoritus.addOsasuoritus(kurssi);
    
    return oppija;
  }
  
}
