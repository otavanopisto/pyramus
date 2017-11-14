package fi.otavanopisto.pyramus.koski;

import java.util.Comparator;

import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

public class ArviointiasteikkoYleissivistavaComparator implements Comparator<ArviointiasteikkoYleissivistava> {

  @Override
  public int compare(ArviointiasteikkoYleissivistava o1, ArviointiasteikkoYleissivistava o2) {
    if (o1 == o2)
      return 0;
    
    int o1Numeric = ArviointiasteikkoYleissivistava.isNumeric(o1) ? Integer.valueOf(o1.toString()) : 
      o1 == ArviointiasteikkoYleissivistava.GRADE_S ? 3 : 2;
    int o2Numeric = ArviointiasteikkoYleissivistava.isNumeric(o2) ? Integer.valueOf(o2.toString()) : 
      o2 == ArviointiasteikkoYleissivistava.GRADE_S ? 3 : 2;
    
    return o1Numeric - o2Numeric;
  }
  
}
