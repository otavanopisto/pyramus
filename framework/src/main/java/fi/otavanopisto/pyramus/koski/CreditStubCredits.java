package fi.otavanopisto.pyramus.koski;

import java.util.ArrayList;

import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

public class CreditStubCredits extends ArrayList<CreditStubCredit> {

  private static final long serialVersionUID = -8809928232943880797L;

  public CreditStubCredit getBestCredit() {
    if (this.size() > 0) {
      ArviointiasteikkoYleissivistavaComparator comparator = new ArviointiasteikkoYleissivistavaComparator();
      CreditStubCredit bestCredit = this.get(0);
      ArviointiasteikkoYleissivistava bestArvosana = bestCredit.getArvosana();
      
      for (int i = 1; i < this.size(); i++) {
        CreditStubCredit credit = this.get(i);
        if (credit != null) {
          ArviointiasteikkoYleissivistava arvosana = credit.getArvosana();
          if (comparator.compare(arvosana, bestArvosana) > 0) {
            bestCredit = credit;
            bestArvosana = arvosana;
          }
        }
      }
      return bestCredit;
    } else {
      return null;
    }
  }
}
