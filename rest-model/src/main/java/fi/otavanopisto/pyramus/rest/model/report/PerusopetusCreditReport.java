package fi.otavanopisto.pyramus.rest.model.report;

import java.util.ArrayList;
import java.util.List;

public class PerusopetusCreditReport {

  public void addAcceptedCredit(PerusopetusCredit credit) {
    acceptedCredits.add(credit);
  }
  
  public List<PerusopetusCredit> getAcceptedCredits() {
    return acceptedCredits;
  }
  
  public void addRejectedCredit(PerusopetusCredit credit) {
    rejectedCredits.add(credit);
  }
  
  public List<PerusopetusCredit> getRejectedCredits() {
    return rejectedCredits;
  }

  private final List<PerusopetusCredit> acceptedCredits = new ArrayList<>();
  private final List<PerusopetusCredit> rejectedCredits = new ArrayList<>();
}
