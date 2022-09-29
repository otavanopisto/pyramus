package fi.otavanopisto.pyramus.koski.koodisto;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.koski.KoodistoEnum;

public class KoskiKoodisto {

  public static Class<? extends Enum<?>> getEnum(String enumKey) {
    return koodistoMap.get(enumKey);
  }
  
  public static <E extends Enum<E>> void registerEnum(Class<E> enumClass) {
    KoodistoEnum koodistoEnum = enumClass.getAnnotation(KoodistoEnum.class);
    if (koodistoEnum != null && StringUtils.isNotBlank(koodistoEnum.value())) {
      koodistoMap.put(koodistoEnum.value(), enumClass);
    }
  }
  
  static {
    koodistoMap = new HashMap<>();
    
    registerEnum(AikuistenPerusopetuksenAlkuvaiheenKurssit2017.class);
    registerEnum(AikuistenPerusopetuksenAlkuvaiheenOppiaineet.class);
    registerEnum(AikuistenPerusopetuksenKurssit2015.class);
    registerEnum(AikuistenPerusopetuksenPaattovaiheenKurssit2017.class);
    registerEnum(ArviointiasteikkoYleissivistava.class);
    registerEnum(Kieli.class);
    registerEnum(Kielivalikoima.class);
    registerEnum(KoskiOppiaineetYleissivistava.class);
    registerEnum(Koulutus.class);
    registerEnum(Kunta.class);
    registerEnum(Lahdejarjestelma.class);
    registerEnum(LukionKurssinTyyppi.class);
    registerEnum(LukionKurssit.class);
    registerEnum(LukionKurssitOPS2004Aikuiset.class);
    registerEnum(LukionMuutOpinnot.class);
    registerEnum(LukionOppimaara.class);
    registerEnum(ModuuliKoodistoLOPS2021.class);
    registerEnum(OpintojenLaajuusYksikko.class);
    registerEnum(OpintojenRahoitus.class);
    registerEnum(OpiskeluoikeudenTila.class);
    registerEnum(OpiskeluoikeudenTyyppi.class);
    registerEnum(OppiaineAidinkieliJaKirjallisuus.class);
    registerEnum(OppiaineMatematiikka.class);
    registerEnum(PerusopetuksenSuoritusTapa.class);
    registerEnum(SuorituksenTila.class);
    registerEnum(SuorituksenTyyppi.class);
  }
  
  private static Map<String, Class<? extends Enum<?>>> koodistoMap;
}
