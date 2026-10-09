package fi.otavanopisto.pyramus.rest;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Sarakkeet Koskesta saatavan kurssikertymäraportin arvioinnit-taulukosta.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KoskiCSVCreditRow {

  public KoskiCSVCreditRow() {
  }
  
  public String getOpiskeluoikeudenOid() {
    return opiskeluoikeudenOid;
  }

  public void setOpiskeluoikeudenOid(String opiskeluoikeudenOid) {
    this.opiskeluoikeudenOid = opiskeluoikeudenOid;
  }

  public String getOppijanOid() {
    return oppijanOid;
  }

  public void setOppijanOid(String oppijanOid) {
    this.oppijanOid = oppijanOid;
  }

  public String getOppijanMasterOid() {
    return oppijanMasterOid;
  }

  public void setOppijanMasterOid(String oppijanMasterOid) {
    this.oppijanMasterOid = oppijanMasterOid;
  }

  public LocalDate getAlkamispaiva() {
    return alkamispaiva;
  }

  public void setAlkamispaiva(LocalDate alkamispaiva) {
    this.alkamispaiva = alkamispaiva;
  }

  public LocalDate getPaattymispaiva() {
    return paattymispaiva;
  }

  public void setPaattymispaiva(LocalDate paattymispaiva) {
    this.paattymispaiva = paattymispaiva;
  }

  public String getViimeisinOpiskeluoikeudenTila() {
    return viimeisinOpiskeluoikeudenTila;
  }

  public void setViimeisinOpiskeluoikeudenTila(String viimeisinOpiskeluoikeudenTila) {
    this.viimeisinOpiskeluoikeudenTila = viimeisinOpiskeluoikeudenTila;
  }

  public String getSuorituksenTyyppi() {
    return suorituksenTyyppi;
  }

  public void setSuorituksenTyyppi(String suorituksenTyyppi) {
    this.suorituksenTyyppi = suorituksenTyyppi;
  }

  public String getKurssinKoodi() {
    return kurssinKoodi;
  }

  public void setKurssinKoodi(String kurssinKoodi) {
    this.kurssinKoodi = kurssinKoodi;
  }

  public String getKurssinNimi() {
    return kurssinNimi;
  }

  public void setKurssinNimi(String kurssinNimi) {
    this.kurssinNimi = kurssinNimi;
  }

  public KoskiBoolean getPaikallinenKurssi() {
    return paikallinenKurssi;
  }

  public void setPaikallinenKurssi(KoskiBoolean paikallinenKurssi) {
    this.paikallinenKurssi = paikallinenKurssi;
  }

  public Integer getArviointienLukumaara() {
    return arviointienLukumaara;
  }

  public void setArviointienLukumaara(Integer arviointienLukumaara) {
    this.arviointienLukumaara = arviointienLukumaara;
  }

  public LocalDate getEnsimmaisenArvioinninPaivamaara() {
    return ensimmaisenArvioinninPaivamaara;
  }

  public void setEnsimmaisenArvioinninPaivamaara(LocalDate ensimmaisenArvioinninPaivamaara) {
    this.ensimmaisenArvioinninPaivamaara = ensimmaisenArvioinninPaivamaara;
  }

  public LocalDate getParhaanArvioinninPaivamaara() {
    return parhaanArvioinninPaivamaara;
  }

  public void setParhaanArvioinninPaivamaara(LocalDate parhaanArvioinninPaivamaara) {
    this.parhaanArvioinninPaivamaara = parhaanArvioinninPaivamaara;
  }

  public String getKurssinParasArvosana() {
    return kurssinParasArvosana;
  }

  public void setKurssinParasArvosana(String kurssinParasArvosana) {
    this.kurssinParasArvosana = kurssinParasArvosana;
  }

  public LocalDate getArviointipaiva() {
    return arviointipaiva;
  }

  public void setArviointipaiva(LocalDate arviointipaiva) {
    this.arviointipaiva = arviointipaiva;
  }

  public String getArvosana() {
    return arvosana;
  }

  public void setArvosana(String arvosana) {
    this.arvosana = arvosana;
  }

  public KoskiBoolean getEnsimmainenArviointi() {
    return ensimmainenArviointi;
  }

  public void setEnsimmainenArviointi(KoskiBoolean ensimmainenArviointi) {
    this.ensimmainenArviointi = ensimmainenArviointi;
  }

  public KoskiBoolean getHylatynKorotus() {
    return hylatynKorotus;
  }

  public void setHylatynKorotus(KoskiBoolean hylatynKorotus) {
    this.hylatynKorotus = hylatynKorotus;
  }

  public KoskiBoolean getHyvaksytynKorotus() {
    return hyvaksytynKorotus;
  }

  public void setHyvaksytynKorotus(KoskiBoolean hyvaksytynKorotus) {
    this.hyvaksytynKorotus = hyvaksytynKorotus;
  }

  public String getRahoitusmuotoArviointipaivana() {
    return rahoitusmuotoArviointipaivana;
  }

  public void setRahoitusmuotoArviointipaivana(String rahoitusmuotoArviointipaivana) {
    this.rahoitusmuotoArviointipaivana = rahoitusmuotoArviointipaivana;
  }

  public KoskiBoolean getTunnustettu() {
    return tunnustettu;
  }

  public void setTunnustettu(KoskiBoolean tunnustettu) {
    this.tunnustettu = tunnustettu;
  }

  public KoskiBoolean getTunnustettuRahoituksenPiirissa() {
    return tunnustettuRahoituksenPiirissa;
  }

  public void setTunnustettuRahoituksenPiirissa(KoskiBoolean tunnustettuRahoituksenPiirissa) {
    this.tunnustettuRahoituksenPiirissa = tunnustettuRahoituksenPiirissa;
  }  

  @JsonProperty("Opiskeluoikeuden oid")
  private String opiskeluoikeudenOid;

  @JsonProperty("Oppijan oid")
  private String oppijanOid;

  @JsonProperty("Oppijan master-oid")
  private String oppijanMasterOid;

  @JsonProperty("Alkamispäivä")
  private LocalDate alkamispaiva;

  @JsonProperty("Päättymispäivä")
  private LocalDate paattymispaiva;

  @JsonProperty("Viimeisin opiskeluoikeuden tila")
  private String viimeisinOpiskeluoikeudenTila;

  @JsonProperty("Suorituksen tyyppi")
  private String suorituksenTyyppi;

  @JsonProperty("Kurssin koodi")
  private String kurssinKoodi;

  @JsonProperty("Kurssin nimi")
  private String kurssinNimi;

  @JsonProperty("Paikallinen kurssi")
  private KoskiBoolean paikallinenKurssi;

  @JsonProperty("Arviointien lukumäärä")
  private Integer arviointienLukumaara;

  @JsonProperty("Ensimmäisen arvioinnin päivämäärä")
  private LocalDate ensimmaisenArvioinninPaivamaara;

  @JsonProperty("Parhaan arvioinnin päivämäärä")
  private LocalDate parhaanArvioinninPaivamaara;

  @JsonProperty("Kurssin paras arvosana")
  private String kurssinParasArvosana;

  @JsonProperty("Arviointipäivä")
  private LocalDate arviointipaiva;

  @JsonProperty("Arvosana")
  private String arvosana;

  @JsonProperty("Ensimmäinen arviointi")
  private KoskiBoolean ensimmainenArviointi;

  @JsonProperty("Hylätyn korotus")
  private KoskiBoolean hylatynKorotus;

  @JsonProperty("Hyväksytyn korotus")
  private KoskiBoolean hyvaksytynKorotus;

  @JsonProperty("Rahoitusmuoto arviointipäivänä")
  private String rahoitusmuotoArviointipaivana;

  @JsonProperty("Tunnustettu")
  private KoskiBoolean tunnustettu;

  @JsonProperty("Tunnustettu – rahoituksen piirissä")
  private KoskiBoolean tunnustettuRahoituksenPiirissa;
  
  public static enum KoskiBoolean {
    KYLLA ("kyllä", true),
    EI ("ei", false);

    KoskiBoolean(String text, boolean boolValue) {
      this.text = text;
      this.boolValue = boolValue;
    }
    
    public boolean boolValue() {
      return boolValue;
    }
    
    @JsonValue
    public String text() {
      return text;
    }
    
    private String text;
    private boolean boolValue;
  }
}