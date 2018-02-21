package fi.otavanopisto.pyramus.koski.aikuistenperusopetus;

import java.util.Date;

import fi.otavanopisto.pyramus.koski.AbstractKoskiData;
import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenKurssit2015;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.model.HenkiloUusi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiNumeerinen;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiSanallinen;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusJakso;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinSuoritus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunniste;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnisteOPS2015;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenKurssinTunnistePaikallinen;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOppimaaranSuoritus;

public class APOOppimaaraData extends AbstractKoskiData {

  public static Oppija getTestStudentMinimal() {
    Oppija oppija = new Oppija();
    oppija.setHenkilo(new HenkiloUusi("111111A111C", "Sally", "Student", "Sally"));
    
    AikuistenPerusopetuksenOpiskeluoikeus opiskeluoikeus = new AikuistenPerusopetuksenOpiskeluoikeus();
    oppija.addOpiskeluoikeus(opiskeluoikeus);
    
    OpiskeluoikeusJakso jakso = new OpiskeluoikeusJakso(new Date(), OpiskeluoikeudenTila.lasna);
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(jakso);
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(ACADEMYOID);
    AikuistenPerusopetuksenOppimaaranSuoritus suoritus = new AikuistenPerusopetuksenOppimaaranSuoritus(
        PerusopetuksenSuoritusTapa.koulutus, Kieli.FI, toimipiste);
    opiskeluoikeus.addSuoritus(suoritus);
    
    return oppija;
  }
  
  public static Oppija getTestStudent() {
    Oppija oppija = new Oppija();
    oppija.setHenkilo(new HenkiloUusi("111111A111C", "Sally", "Student", "Sally"));
    
    AikuistenPerusopetuksenOpiskeluoikeus opiskeluoikeus = new AikuistenPerusopetuksenOpiskeluoikeus();
    oppija.addOpiskeluoikeus(opiskeluoikeus);
    
    Date paattymispaiva = date(2017, 10, 10);
    opiskeluoikeus.setPaattymispaiva(paattymispaiva);

    OpiskeluoikeusJakso jakso = new OpiskeluoikeusJakso(new Date(), OpiskeluoikeudenTila.lasna);
    jakso.setOpintojenRahoitus(new KoodistoViite<OpintojenRahoitus>(OpintojenRahoitus.K1));
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(jakso);
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(new OpiskeluoikeusJakso(paattymispaiva, OpiskeluoikeudenTila.valmistunut));
    opiskeluoikeus.setLahdejarjestelmanId(getLahdeJarjestelmaID(1l));
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(ACADEMYOID);
    AikuistenPerusopetuksenOppimaaranSuoritus suoritus = new AikuistenPerusopetuksenOppimaaranSuoritus(
        PerusopetuksenSuoritusTapa.koulutus, Kieli.FI, toimipiste);
    opiskeluoikeus.addSuoritus(suoritus);

    // Oppiaine
    AikuistenPerusopetuksenOppiaineenTunniste koulutusmoduuli = new AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli(
        OppiaineAidinkieliJaKirjallisuus.AI1, true);
    AikuistenPerusopetuksenOppiaineenSuoritus oppiaine = new AikuistenPerusopetuksenOppiaineenSuoritus(koulutusmoduuli);
    suoritus.addOsasuoritus(oppiaine);

    // Oppiaineen arviointi
    oppiaine.addArviointi(new KurssinArviointiNumeerinen(ArviointiasteikkoYleissivistava.GRADE_9, paattymispaiva));
    
    // Kurssi 1
    AikuistenPerusopetuksenKurssinTunniste kurssinTunniste = new AikuistenPerusopetuksenKurssinTunnisteOPS2015(AikuistenPerusopetuksenKurssit2015.ÄI1);
    AikuistenPerusopetuksenKurssinSuoritus kurssi = new AikuistenPerusopetuksenKurssinSuoritus(kurssinTunniste);
    kurssi.addArviointi(new KurssinArviointiNumeerinen(ArviointiasteikkoYleissivistava.GRADE_9, paattymispaiva));
    oppiaine.addOsasuoritus(kurssi);
    
    // Kurssi 2
    kurssinTunniste = new AikuistenPerusopetuksenKurssinTunnistePaikallinen(new PaikallinenKoodi("ÄI123", kuvaus("ABC")));
    kurssi = new AikuistenPerusopetuksenKurssinSuoritus(kurssinTunniste);
    kurssi.addArviointi(new KurssinArviointiSanallinen(ArviointiasteikkoYleissivistava.GRADE_S, paattymispaiva, kuvaus("S")));
    oppiaine.addOsasuoritus(kurssi);
    
    return oppija;
  }
  
}
