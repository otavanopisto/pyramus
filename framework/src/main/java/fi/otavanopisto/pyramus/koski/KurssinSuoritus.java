package fi.otavanopisto.pyramus.koski;

import java.util.List;

import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.OsaamisenTunnustaminen;

public interface KurssinSuoritus {

  public void addArviointi(KurssinArviointi arviointi);
  public List<KurssinArviointi> getArviointi();

  public OsaamisenTunnustaminen getTunnustettu();
  public void setTunnustettu(OsaamisenTunnustaminen tunnustettu);

}
