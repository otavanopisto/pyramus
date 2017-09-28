package fi.otavanopisto.pyramus.koski;

public class KoodistoViite<T extends Enum<T>> {

  public KoodistoViite() {
  }
  
  public KoodistoViite(T koodiarvo) {
    this.koodiarvo = koodiarvo;
  }
  
  public void setValue(T koodiarvo) {
    this.koodiarvo = koodiarvo;
  }
  
  public String getKoodiarvo() {
    return koodiarvo != null ? koodiarvo.toString() : null;
  }
  
  public String getKoodistoUri() {
    if (koodiarvo != null) {
      KoodistoEnum annotation = koodiarvo.getClass().getAnnotation(KoodistoEnum.class);
      if (annotation != null)
        return annotation.value();
    }
    
    return null;
  }

  private Enum<T> koodiarvo;
}
