package fi.otavanopisto.pyramus.rest.model.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public PerusopetusCreditReportSummary getSummary() {
    return summary;
  }

  public class PerusopetusCreditReportSummary {
    
    public int getAcceptedCreditCount() {
      return acceptedCreditCount;
    }
    
    public void incrementAcceptedCreditCount() {
      this.acceptedCreditCount++;
    }
    
    public int getRejectedCreditCount() {
      return rejectedCreditCount;
    }
    
    public void incrementRejectedCreditCount() {
      this.rejectedCreditCount++;
    }

    public Map<String, Integer> getAcceptedByLengthUnit() {
      return acceptedByLengthUnit;
    }

    public void incrementAcceptedByLengthUnit(String acceptedLengthUnit) {
      if (this.acceptedByLengthUnit.containsKey(acceptedLengthUnit)) {
        Integer value = this.acceptedByLengthUnit.get(acceptedLengthUnit);
        this.acceptedByLengthUnit.put(acceptedLengthUnit, value++);
      } 
      else {
        this.acceptedByLengthUnit.put(acceptedLengthUnit, 1);
      }
    }

    public Map<String, Integer> getRejectedByLengthUnit() {
      return rejectedByLengthUnit;
    }

    public void incrementRejectedByLengthUnit(String rejectedLengthUnit) {
      if (this.rejectedByLengthUnit.containsKey(rejectedLengthUnit)) {
        Integer value = this.rejectedByLengthUnit.get(rejectedLengthUnit);
        this.rejectedByLengthUnit.put(rejectedLengthUnit, value++);
      } 
      else {
        this.rejectedByLengthUnit.put(rejectedLengthUnit, 1);
      }
    }

    private int acceptedCreditCount;
    private int rejectedCreditCount;
    private Map<String, Integer> acceptedByLengthUnit = new HashMap<>();
    private Map<String, Integer> rejectedByLengthUnit = new HashMap<>();
  }
  
  private final PerusopetusCreditReportSummary summary = new PerusopetusCreditReportSummary();
  private final List<PerusopetusCredit> acceptedCredits = new ArrayList<>();
  private final List<PerusopetusCredit> rejectedCredits = new ArrayList<>();
}
