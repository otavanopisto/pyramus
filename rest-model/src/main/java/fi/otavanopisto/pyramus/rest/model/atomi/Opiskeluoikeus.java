package fi.otavanopisto.pyramus.rest.model.atomi;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import fi.otavanopisto.pyramus.atomi.AtomiReportType;

public class Opiskeluoikeus {

  public String getTunniste() {
    return tunniste;
  }
  
  public void setTunniste(String tunniste) {
    this.tunniste = tunniste;
  }
  
  public LocalDate getAloituspäivämäärä() {
    return aloituspäivämäärä;
  }
  
  public void setAloituspäivämäärä(LocalDate aloituspäivämäärä) {
    this.aloituspäivämäärä = aloituspäivämäärä;
  }
  
  public LocalDate getPäättymispäivämäärä() {
    return päättymispäivämäärä;
  }
  
  public void setPäättymispäivämäärä(LocalDate päättymispäivämäärä) {
    this.päättymispäivämäärä = päättymispäivämäärä;
  }
  
  public LocalDate getValmistumispäivämäärä() {
    return valmistumispäivämäärä;
  }
  
  public void setValmistumispäivämäärä(LocalDate valmistumispäivämäärä) {
    this.valmistumispäivämäärä = valmistumispäivämäärä;
  }
  
  public LocalDate getEropäivämäärä() {
    return eropäivämäärä;
  }
  
  public void setEropäivämäärä(LocalDate eropäivämäärä) {
    this.eropäivämäärä = eropäivämäärä;
  }
  
  public OpiskeluoikeudenTila getTila() {
    return tila;
  }
  
  public void setTila(OpiskeluoikeudenTila tila) {
    this.tila = tila;
  }
  
  public Opiskelija getOpiskelija() {
    return opiskelija;
  }
  
  public void setOpiskelija(Opiskelija opiskelija) {
    this.opiskelija = opiskelija;
  }
  
  public AtomiViittaus getKoulutusohjelma() {
    return koulutusohjelma;
  }

  public void setKoulutusohjelma(AtomiViittaus koulutusohjelma) {
    this.koulutusohjelma = koulutusohjelma;
  }

  public void addSaatavillaOlevaDokumentti(AtomiReportType type) {
    saatavillaOlevatDokumentit.add(type);
  }
  
  public Set<AtomiReportType> getSaatavillaOlevatDokumentit() {
    return saatavillaOlevatDokumentit;
  }

  public void setSaatavillaOlevatDokumentit(Set<AtomiReportType> saatavillaOlevatDokumentit) {
    this.saatavillaOlevatDokumentit = saatavillaOlevatDokumentit;
  }

  private String tunniste;
  private LocalDate aloituspäivämäärä;
  private LocalDate päättymispäivämäärä;
  private LocalDate valmistumispäivämäärä;
  private LocalDate eropäivämäärä;
  private OpiskeluoikeudenTila tila;
  private Opiskelija opiskelija;
  private AtomiViittaus koulutusohjelma;
  private Set<AtomiReportType> saatavillaOlevatDokumentit = new HashSet<>();
}
