package fi.otavanopisto.pyramus.ytl;

import java.util.ArrayList;
import java.util.List;

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
  
  private String ytlAine;
  private String ytlOppimäärä;
  private String aine;
  private String aineKoodi;
  private MatriculationExamSubject matriculationExamSubject;
  private List<YTLAineKoodiSuoritettuKurssi> suoritetutKurssit = new ArrayList<>();
}
