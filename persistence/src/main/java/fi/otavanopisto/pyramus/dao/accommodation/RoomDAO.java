package fi.otavanopisto.pyramus.dao.accommodation;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.accommodation.Room;
import fi.otavanopisto.pyramus.domainmodel.accommodation.RoomType;
import fi.otavanopisto.pyramus.domainmodel.accommodation.Room_;

@Stateless
public class RoomDAO extends PyramusEntityDAO<Room> {

  public Room create(String name, RoomType type) {
    Room room = new Room();
    room.setName(name);
    room.setType(type);
    return persist(room);
  }
  
  public List<Room> listByType(RoomType type) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Room> criteria = criteriaBuilder.createQuery(Room.class);
    Root<Room> root = criteria.from(Room.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(Room_.type), type));
    
    return entityManager.createQuery(criteria).getResultList();
  }


  public Room updateName(Room room, String name) {
   room.setName(name);
   return persist(room);
  }

  public void delete(Room room) {
    super.delete(room);
  }
  
}
