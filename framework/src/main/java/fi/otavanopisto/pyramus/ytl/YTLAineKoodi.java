package fi.otavanopisto.pyramus.ytl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;

/**
 * TODO Kenttien nimeäminen voisi olla järkevämpi
 */
public class YTLAineKoodi {
  
  /**
   * Kokeen selkokielinen nimi
   * 
   * @return Kokeen selkokielinen nimi
   */
  public String getAine() {
    return aine;
  }

  public void setAine(String aine) {
    this.aine = aine;
  }

  /**
   * Aineen koodi, johon koe liittyy. Tämä viittaa Subject.code -kentän arvoon
   * 
   * @return aineen koodi
   */
  public String getAineKoodi() {
    return aineKoodi;
  }

  public void setAineKoodi(String aineKoodi) {
    this.aineKoodi = aineKoodi;
  }

  /**
   * Kokeen tunniste Pyramuksessa
   * 
   * @return kokeen tunniste Pyramuksessa
   */
  public MatriculationExamSubject getMatriculationExamSubject() {
    return matriculationExamSubject;
  }

  public void setMatriculationExamSubject(MatriculationExamSubject matriculationExamSubject) {
    this.matriculationExamSubject = matriculationExamSubject;
  }

  public List<YTLAineKoodiSuoritettuKurssi> getSuoritetutKurssit() {
    return suoritetutKurssit;
  }

  public void setSuoritetutKurssit(List<YTLAineKoodiSuoritettuKurssi> suoritetutKurssit) {
    this.suoritetutKurssit = suoritetutKurssit;
  }

  /**
   * YTL:n käyttämä kokeen koodi
   * 
   * @return YTL:n käyttämä kokeen koodi
   */
  public String getYtlAine() {
    return ytlAine;
  }

  public void setYtlAine(String ytlAine) {
    this.ytlAine = ytlAine;
  }

  public Set<String> getSuoritetutKurssitLisäaineet() {
    return suoritetutKurssitLisäaineet;
  }

  public void setSuoritetutKurssitLisäaineet(Set<String> suoritetutKurssitLisäaineet) {
    this.suoritetutKurssitLisäaineet = suoritetutKurssitLisäaineet;
  }

  private String ytlAine;
  private String aine;
  private String aineKoodi;
  private MatriculationExamSubject matriculationExamSubject;
  private List<YTLAineKoodiSuoritettuKurssi> suoritetutKurssit = new ArrayList<>();
  private Set<String> suoritetutKurssitLisäaineet = new HashSet<>();
}
