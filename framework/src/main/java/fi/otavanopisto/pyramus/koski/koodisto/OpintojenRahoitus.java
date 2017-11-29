package fi.otavanopisto.pyramus.koski.koodisto;

import fi.otavanopisto.pyramus.koski.KoodistoEnum;

@KoodistoEnum("opintojenrahoitus")
public enum OpintojenRahoitus {

  K1(1),              // Valtionosuusrahoitteinen koulutus
  K10(10),            // Maahanmuuttajien ammatillinen koulutus (MAO)
  K2(2),              // Työvoimapoliittinen aikuiskoulutus (kansallinen rahoitus)
  K3(3),              // Työvoimapoliittinen aikuiskoulutus (ESR-rahoitteinen)
  K4(4),              // Työnantajan kokonaan rahoittama
  K6(6),              // Muuta kautta rahoitettu
  K7(7),              // Nuorten aikuisten osaamisohjelma
  K8(8),              // Aikuisten osaamisperustan vahvistaminen
  K9(9);              // Henkilöstökoulutus

  private OpintojenRahoitus(int value) {
    this.value = value;
  }
  
  @Override
  public String toString() {
    return String.valueOf(value);
  }
  
  private int value;
}
