package fi.otavanopisto.pyramus.koski;

import java.util.Calendar;
import java.util.Date;

import fi.otavanopisto.pyramus.koski.koodisto.Kunta;
import fi.otavanopisto.pyramus.koski.koodisto.Lahdejarjestelma;
import fi.otavanopisto.pyramus.koski.model.HenkilovahvistusPaikkakunnalla;
import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.LahdeJarjestelmaID;
import fi.otavanopisto.pyramus.koski.model.Organisaatio;
import fi.otavanopisto.pyramus.koski.model.OrganisaatioHenkilo;
import fi.otavanopisto.pyramus.koski.model.OrganisaatioOID;

public class AbstractKoskiData {

  public static String ACADEMYOID = "1.2.246.562.10.17749422402";
  
  /**
   * Utility method to unify date creation
   * 
   * @param year year
   * @param month give as 1-12, it will be translated to 0-11
   * @param date date
   * @return
   */
  protected static Date date(int year, int month, int date) {
    Calendar c = new Calendar.Builder().build();
    c.set(year, month - 1, date);
    return c.getTime();
  }
  
  protected static Kuvaus kuvaus(String fiKuvaus) {
    Kuvaus kuvaus = new Kuvaus();
    kuvaus.setFi(fiKuvaus);
    return kuvaus;
  }

  protected static HenkilovahvistusPaikkakunnalla getVahvistus(Date studyEndDate, String academyOid) {
    Organisaatio henkilonOrganisaatio = new OrganisaatioOID(academyOid);
    String nimi = "Bruce Wayne";
    String titteli = "principal";
    OrganisaatioHenkilo henkilo = new OrganisaatioHenkilo(nimi, kuvaus(titteli), henkilonOrganisaatio);

    Organisaatio myontajaOrganisaatio = new OrganisaatioOID(academyOid);
    HenkilovahvistusPaikkakunnalla vahvistus = new HenkilovahvistusPaikkakunnalla(
        studyEndDate, Kunta.K491, myontajaOrganisaatio);
    vahvistus.addMyontajaHenkilo(henkilo);
    return vahvistus;
  }

  protected LahdeJarjestelmaID getLahdeJarjestelmaID(Long id) {
    return new LahdeJarjestelmaID(String.valueOf(id), Lahdejarjestelma.pyramus);
  }

}
