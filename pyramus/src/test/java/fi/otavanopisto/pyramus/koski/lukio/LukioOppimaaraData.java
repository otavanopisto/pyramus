package fi.otavanopisto.pyramus.koski.lukio;

import java.util.Date;

import fi.otavanopisto.pyramus.koski.AbstractKoskiData;
import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssit;
import fi.otavanopisto.pyramus.koski.koodisto.LukionOppimaara;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
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
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenArviointi;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritusAidinkieli;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionSuoritus;

public class LukioOppimaaraData extends AbstractKoskiData {

  public static Oppija getTestStudentMinimal() {
    Oppija oppija = new Oppija();
    oppija.setHenkilo(new HenkiloUusi("111111A111C", "Sally", "Student", "Sally"));
    
    LukionOpiskeluoikeus opiskeluoikeus = new LukionOpiskeluoikeus();
    oppija.addOpiskeluoikeus(opiskeluoikeus);
    
    OpiskeluoikeusJakso jakso = new OpiskeluoikeusJakso(new Date(), OpiskeluoikeudenTila.lasna);
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(jakso );
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(ACADEMYOID);
    LukionSuoritus suoritus = new LukionOppimaaranSuoritus(LukionOppimaara.aikuistenops, Kieli.FI, toimipiste, SuorituksenTila.KESKEN);
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
    LukionOppimaaranSuoritus suoritus = new LukionOppimaaranSuoritus(
        LukionOppimaara.aikuistenops, Kieli.FI, toimipiste, SuorituksenTila.KESKEN);
    opiskeluoikeus.addSuoritus(suoritus);

    // Oppiaine
    LukionOppiaineenTunniste koulutusmoduuli = new LukionOppiaineenSuoritusAidinkieli(
        OppiaineAidinkieliJaKirjallisuus.AI1, true);
    LukionOppiaineenSuoritus oppiaine = new LukionOppiaineenSuoritus(koulutusmoduuli);
    suoritus.addOsasuoritus(oppiaine);

    // Oppiaineen arviointi
    oppiaine.addArviointi(new LukionOppiaineenArviointi(ArviointiasteikkoYleissivistava.GRADE_9, paattymispaiva));
    
    // Kurssi 1
    LukionKurssinTunniste kurssinTunniste = new LukionKurssinTunnisteValtakunnallinenOPS2015(LukionKurssit.ÄI1, LukionKurssinTyyppi.pakollinen);
    LukionKurssinSuoritus kurssi = new LukionKurssinSuoritus(kurssinTunniste, SuorituksenTila.VALMIS);
    kurssi.addArviointi(new KurssinArviointiNumeerinen(ArviointiasteikkoYleissivistava.GRADE_9, paattymispaiva));
    oppiaine.addOsasuoritus(kurssi);
    
    // Kurssi 2
    kurssinTunniste = new LukionKurssinTunnistePaikallinen(new PaikallinenKoodi("ÄI123", kuvaus("ABC")), LukionKurssinTyyppi.syventava, kuvaus("ABC"));
    kurssi = new LukionKurssinSuoritus(kurssinTunniste, SuorituksenTila.VALMIS);
    kurssi.addArviointi(new KurssinArviointiSanallinen(ArviointiasteikkoYleissivistava.GRADE_S, paattymispaiva, kuvaus("S")));
    oppiaine.addOsasuoritus(kurssi);
    
    return oppija;
  }
  
}
