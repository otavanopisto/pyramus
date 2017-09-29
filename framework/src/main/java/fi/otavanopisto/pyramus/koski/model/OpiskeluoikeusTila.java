package fi.otavanopisto.pyramus.koski.model;

import java.util.ArrayList;
import java.util.List;

public class OpiskeluoikeusTila {

  public void addOpiskeluoikeusJakso(OpiskeluoikeusJakso jakso) {
    opiskeluoikeusjaksot.add(jakso);
  }
  
  public List<OpiskeluoikeusJakso> getOpiskeluoikeusjaksot() {
    return opiskeluoikeusjaksot;
  }

  private final List<OpiskeluoikeusJakso> opiskeluoikeusjaksot = new ArrayList<>();
}
