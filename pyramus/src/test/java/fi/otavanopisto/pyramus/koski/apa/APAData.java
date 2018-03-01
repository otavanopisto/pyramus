package fi.otavanopisto.pyramus.koski.apa;

import java.util.Date;

import fi.otavanopisto.pyramus.koski.AbstractKoskiData;
import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenAlkuvaiheenKurssit2017;
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
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.apa.APAKurssinSuoritus;
import fi.otavanopisto.pyramus.koski.model.apa.APAKurssinTunniste;
import fi.otavanopisto.pyramus.koski.model.apa.APAKurssinTunnisteOPS2017;
import fi.otavanopisto.pyramus.koski.model.apa.APAKurssinTunnistePaikallinen;
import fi.otavanopisto.pyramus.koski.model.apa.APAOppiaineenSuoritus;
import fi.otavanopisto.pyramus.koski.model.apa.APAOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.model.apa.APAOppiaineenTunnisteAidinkieli;
import fi.otavanopisto.pyramus.koski.model.apa.APASuoritus;

public class APAData extends AbstractKoskiData {

  public static Oppija getTestStudentMinimal() {
    Oppija oppija = new Oppija();
    oppija.setHenkilo(new HenkiloUusi("111111A111C", "Sally", "Student", "Sally"));
    
    AikuistenPerusopetuksenOpiskeluoikeus opiskeluoikeus = new AikuistenPerusopetuksenOpiskeluoikeus();
    oppija.addOpiskeluoikeus(opiskeluoikeus);
    
    OpiskeluoikeusJakso jakso = new OpiskeluoikeusJakso(new Date(), OpiskeluoikeudenTila.lasna);
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(jakso);
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(ACADEMYOID);
    APASuoritus suoritus = new APASuoritus(PerusopetuksenSuoritusTapa.koulutus, Kieli.FI, toimipiste);
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
    APASuoritus suoritus = new APASuoritus(PerusopetuksenSuoritusTapa.koulutus, Kieli.FI, toimipiste);
    opiskeluoikeus.addSuoritus(suoritus);
    
    APAOppiaineenTunniste koulutusmoduuli = new APAOppiaineenTunnisteAidinkieli(OppiaineAidinkieliJaKirjallisuus.AI1);
    APAOppiaineenSuoritus oppiaine = new APAOppiaineenSuoritus(koulutusmoduuli);
    suoritus.addOsasuoritus(oppiaine);
    
    APAKurssinTunniste tunniste = new APAKurssinTunnisteOPS2017(AikuistenPerusopetuksenAlkuvaiheenKurssit2017.AÄI1);
    APAKurssinSuoritus kurssi = new APAKurssinSuoritus(tunniste);
    kurssi.addArviointi(new KurssinArviointiNumeerinen(ArviointiasteikkoYleissivistava.GRADE_9, paattymispaiva));
    oppiaine.addOsasuoritus(kurssi);
    
    tunniste = new APAKurssinTunnistePaikallinen(new PaikallinenKoodi("AÄI123", kuvaus("ABC")));
    kurssi = new APAKurssinSuoritus(tunniste);
    kurssi.addArviointi(new KurssinArviointiSanallinen(ArviointiasteikkoYleissivistava.GRADE_S, paattymispaiva, kuvaus("S")));
    oppiaine.addOsasuoritus(kurssi);
    
    return oppija;
  }
  
}
