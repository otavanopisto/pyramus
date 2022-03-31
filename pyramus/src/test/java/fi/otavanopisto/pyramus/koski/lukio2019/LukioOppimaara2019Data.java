package fi.otavanopisto.pyramus.koski.lukio2019;

import java.util.Date;

import fi.otavanopisto.pyramus.koski.AbstractKoskiData;
import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.LukionOppimaara;
import fi.otavanopisto.pyramus.koski.koodisto.ModuuliKoodistoLOPS2021;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenLaajuusYksikko;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.HenkiloUusi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiNumeerinen;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointiSanallinen;
import fi.otavanopisto.pyramus.koski.model.Laajuus;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusJakso;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenArviointi;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOpintojaksonSuoritus2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOpintojaksonTunniste2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOpintojaksonTunnisteMuuModuuli2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOpintojaksonTunnistePaikallinen2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppiaineenSuoritus2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppiaineenSuoritusAidinkieli2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppimaaranSuoritus2019;

public class LukioOppimaara2019Data extends AbstractKoskiData {

  public static Oppija getTestStudentMinimal() {
    Oppija oppija = new Oppija();
    oppija.setHenkilo(new HenkiloUusi("111111A111C", "Sally", "Student", "Sally"));
    
    LukionOpiskeluoikeus opiskeluoikeus = new LukionOpiskeluoikeus();
    oppija.addOpiskeluoikeus(opiskeluoikeus);
    
    OpiskeluoikeusJakso jakso = new OpiskeluoikeusJakso(new Date(), OpiskeluoikeudenTila.lasna);
    opiskeluoikeus.getTila().addOpiskeluoikeusJakso(jakso);
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(ACADEMYOID);
    LukionOppimaaranSuoritus2019 suoritus = new LukionOppimaaranSuoritus2019(LukionOppimaara.aikuistenops, Kieli.FI, toimipiste);
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
    LukionOppimaaranSuoritus2019 suoritus = new LukionOppimaaranSuoritus2019(
        LukionOppimaara.aikuistenops, Kieli.FI, toimipiste);
    opiskeluoikeus.addSuoritus(suoritus);

    // Oppiaine
    LukionOppiaineenSuoritusAidinkieli2019 koulutusmoduuli = new LukionOppiaineenSuoritusAidinkieli2019(
        OppiaineAidinkieliJaKirjallisuus.AI1, true);
    LukionOppiaineenSuoritus2019 oppiaine = new LukionOppiaineenSuoritus2019(koulutusmoduuli, false);
    suoritus.addOsasuoritus(oppiaine);

    // Oppiaineen arviointi
    oppiaine.addArviointi(new LukionOppiaineenArviointi(ArviointiasteikkoYleissivistava.GRADE_9, paattymispaiva));
    
    // Kurssi 1
    LukionOpintojaksonTunniste2019 kurssinTunniste = new LukionOpintojaksonTunnisteMuuModuuli2019(ModuuliKoodistoLOPS2021.ÄI1, new Laajuus(2, OpintojenLaajuusYksikko.op), true);
    LukionOpintojaksonSuoritus2019 kurssi = new LukionOpintojaksonSuoritus2019(kurssinTunniste, SuorituksenTyyppi.lukionvaltakunnallinenmoduuli);
    kurssi.addArviointi(new KurssinArviointiNumeerinen(ArviointiasteikkoYleissivistava.GRADE_9, paattymispaiva));
    oppiaine.addOsasuoritus(kurssi);
    
    // Kurssi 2
    kurssinTunniste = new LukionOpintojaksonTunnistePaikallinen2019(new PaikallinenKoodi("ÄI123", kuvaus("ABC")), new Laajuus(2, OpintojenLaajuusYksikko.op), false, kuvaus("ABC"));
    kurssi = new LukionOpintojaksonSuoritus2019(kurssinTunniste, SuorituksenTyyppi.lukionpaikallinenopintojakso);
    kurssi.addArviointi(new KurssinArviointiSanallinen(ArviointiasteikkoYleissivistava.GRADE_S, paattymispaiva, kuvaus("S")));
    oppiaine.addOsasuoritus(kurssi);
    
    return oppija;
  }
  
}
