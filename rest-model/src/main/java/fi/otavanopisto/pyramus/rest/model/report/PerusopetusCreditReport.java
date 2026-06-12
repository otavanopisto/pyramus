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

  public void addFundedTransferCredit(PerusopetusCredit credit) {
    fundedTransferCredits.add(credit);
  }
  
  public List<PerusopetusCredit> getFundedTransferCredits() {
    return fundedTransferCredits;
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

    public int getAcceptedTransferCreditCount() {
      return acceptedTransferCreditCount;
    }

    public void incrementAcceptedTransferCreditCount() {
      this.acceptedTransferCreditCount++;
    }
    
    public void setAcceptedTransferCreditCount(int acceptedTransferCreditCount) {
      this.acceptedTransferCreditCount = acceptedTransferCreditCount;
    }

    public int getRejectedTransferCreditCount() {
      return rejectedTransferCreditCount;
    }

    public void incrementRejectedTransferCreditCount() {
      this.rejectedTransferCreditCount++;
    }

    public void setRejectedTransferCreditCount(int rejectedTransferCreditCount) {
      this.rejectedTransferCreditCount = rejectedTransferCreditCount;
    }

    public Map<String, Integer> getAcceptedByLengthUnit() {
      return acceptedByLengthUnit;
    }

    public void incrementAcceptedByLengthUnit(String acceptedLengthUnit) {
      if (this.acceptedByLengthUnit.containsKey(acceptedLengthUnit)) {
        Integer value = this.acceptedByLengthUnit.get(acceptedLengthUnit);
        this.acceptedByLengthUnit.put(acceptedLengthUnit, value + 1);
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
        this.rejectedByLengthUnit.put(rejectedLengthUnit, value + 1);
      } 
      else {
        this.rejectedByLengthUnit.put(rejectedLengthUnit, 1);
      }
    }

    public Map<PerusopetusCreditState, Integer> getAcceptedByState() {
      return acceptedByState;
    }

    public void incrementAcceptedByState(PerusopetusCreditState state) {
      if (this.acceptedByState.containsKey(state)) {
        Integer value = this.acceptedByState.get(state);
        this.acceptedByState.put(state, value + 1);
      } 
      else {
        this.acceptedByState.put(state, 1);
      }
    }

    public Map<PerusopetusCreditState, Integer> getRejectedByState() {
      return rejectedByState;
    }

    public void incrementRejectedByState(PerusopetusCreditState state) {
      if (this.rejectedByState.containsKey(state)) {
        Integer value = this.rejectedByState.get(state);
        this.rejectedByState.put(state, value + 1);
      } 
      else {
        this.rejectedByState.put(state, 1);
      }
    }

    private int acceptedCreditCount;
    private int rejectedCreditCount;
    private int acceptedTransferCreditCount;
    private int rejectedTransferCreditCount;
    private Map<String, Integer> acceptedByLengthUnit = new HashMap<>();
    private Map<String, Integer> rejectedByLengthUnit = new HashMap<>();
    private Map<PerusopetusCreditState, Integer> acceptedByState = new HashMap<>();
    private Map<PerusopetusCreditState, Integer> rejectedByState = new HashMap<>();
  }
  
  private final PerusopetusCreditReportSummary summary = new PerusopetusCreditReportSummary();
  private final List<PerusopetusCredit> acceptedCredits = new ArrayList<>();
  private final List<PerusopetusCredit> rejectedCredits = new ArrayList<>();
  private final List<PerusopetusCredit> fundedTransferCredits = new ArrayList<>();
}
