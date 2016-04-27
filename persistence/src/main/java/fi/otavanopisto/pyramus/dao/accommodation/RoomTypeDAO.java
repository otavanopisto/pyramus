package fi.otavanopisto.pyramus.dao.accommodation;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.accommodation.RoomType;

@Stateless
public class RoomTypeDAO extends PyramusEntityDAO<RoomType> {

  public RoomType create(String name) {
    RoomType roomType = new RoomType();
    roomType.setName(name);
    return persist(roomType);
  }

  public RoomType updateName(RoomType roomType, String name) {
   roomType.setName(name);
   return persist(roomType);
  }

  public void delete(RoomType roomType) {
    super.delete(roomType);
  }
  
}
