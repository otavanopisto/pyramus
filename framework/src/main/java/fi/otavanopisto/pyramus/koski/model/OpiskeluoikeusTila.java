package fi.otavanopisto.pyramus.koski.model;

import java.util.HashSet;
import java.util.Set;

public class OpiskeluoikeusTila {

  public void addOpiskeluoikeusJakso(OpiskeluoikeusJakso jakso) {
    opiskeluoikeusjaksot.add(jakso);
  }
  
  public Set<OpiskeluoikeusJakso> getOpiskeluoikeusjaksot() {
    return opiskeluoikeusjaksot;
  }

  public void setOpiskeluoikeusjaksot(Set<OpiskeluoikeusJakso> opiskeluoikeusjaksot) {
    this.opiskeluoikeusjaksot = opiskeluoikeusjaksot;
  }

  private Set<OpiskeluoikeusJakso> opiskeluoikeusjaksot = new HashSet<>();
}
