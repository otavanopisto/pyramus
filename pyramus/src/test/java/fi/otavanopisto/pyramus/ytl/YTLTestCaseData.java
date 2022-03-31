package fi.otavanopisto.pyramus.ytl;

import fi.otavanopisto.pyramus.koski.AbstractKoskiData;

public class YTLTestCaseData extends AbstractKoskiData {

  public static YTLSiirtotiedosto getOldTestCase() {
    YTLSiirtotiedosto ytl = new YTLSiirtotiedosto("2021K", 1234);
    ytl.addKokelas(getOldKokelas());
    return ytl;
  }

  public static YTLSiirtotiedosto get2022TestCase() {
    YTLSiirtotiedosto ytl = new YTLSiirtotiedosto("2021K", 1234);
    ytl.addKokelas(get2022Kokelas());
    return ytl;
  }
  
  public static YTLSiirtotiedosto getCombinedTestCase() {
    YTLSiirtotiedosto ytl = new YTLSiirtotiedosto("2021K", 1234);
    ytl.addKokelas(getOldKokelas());
    ytl.addKokelas(get2022Kokelas());
    return ytl;
  }
  
  private static AbstractKokelas getOldKokelas() {
    Kokelas kokelas = new Kokelas();
    kokelas.getEtunimet().add("Sally");
    kokelas.setSukunimi("Student");
    kokelas.setHetu("111111A111C");
    kokelas.setKokelasnumero(1);
    kokelas.setOppijanumero("1.2.34.567.89012345");
    kokelas.setKoulutustyyppi(Koulutustyyppi.lukio);
    kokelas.setTutkintotyyppi(Tutkintotyyppi.yoTutkinto);
    kokelas.setUudelleenaloittaja(false);

    
    kokelas.setÄidinkielenKoe("A");
    kokelas.addPakollinenKoe("M");
    kokelas.addYlimääräinenKoe("FA");

    return kokelas;
  }

  public static Kokelas2022 get2022Kokelas() {
    Kokelas2022 kokelas = new Kokelas2022();
    kokelas.getEtunimet().add("Sally");
    kokelas.setSukunimi("Student");
    kokelas.setHetu("111111A111C");
    kokelas.setKokelasnumero(1);
    kokelas.setOppijanumero("1.2.34.567.89012345");
    kokelas.setKoulutustyyppi(Koulutustyyppi.lukio);
    kokelas.setTutkintotyyppi(Tutkintotyyppi.yoTutkinto);
    kokelas.setUudelleenaloittaja(false);

    kokelas.addKoe(new Kokelas2022Koe("A", false));
    kokelas.addKoe(new Kokelas2022Koe("M", false));
    kokelas.addKoe(new Kokelas2022Koe("FA", false));
    
    return kokelas;
  }
}
