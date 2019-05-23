package fi.otavanopisto.pyramus.ytl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubject;

public class YTLAineKoodi {
  
  public String getAine() {
    return aine;
  }

  public void setAine(String aine) {
    this.aine = aine;
  }

  public String getAineKoodi() {
    return aineKoodi;
  }

  public void setAineKoodi(String aineKoodi) {
    this.aineKoodi = aineKoodi;
  }

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

  public String getYtlAine() {
    return ytlAine;
  }

  public void setYtlAine(String ytlAine) {
    this.ytlAine = ytlAine;
  }

  public String getYtlOppimäärä() {
    return ytlOppimäärä;
  }

  public void setYtlOppimäärä(String ytlOppimäärä) {
    this.ytlOppimäärä = ytlOppimäärä;
  }

  public String getYhdistettyAineKoodi() {
    return ytlAine + (StringUtils.isNotBlank(ytlOppimäärä) ? ytlOppimäärä : "");
  }
  
  public Set<String> getSuoritetutKurssitLisäaineet() {
    return suoritetutKurssitLisäaineet;
  }

  public void setSuoritetutKurssitLisäaineet(Set<String> suoritetutKurssitLisäaineet) {
    this.suoritetutKurssitLisäaineet = suoritetutKurssitLisäaineet;
  }

  private String ytlAine;
  private String ytlOppimäärä;
  private String aine;
  private String aineKoodi;
  private MatriculationExamSubject matriculationExamSubject;
  private List<YTLAineKoodiSuoritettuKurssi> suoritetutKurssit = new ArrayList<>();
  private Set<String> suoritetutKurssitLisäaineet = new HashSet<>();
}
