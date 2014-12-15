package fi.pyramus.dao.users;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.ContactInfo;
import fi.pyramus.domainmodel.base.ContactInfo_;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.Email_;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.domainmodel.users.User_;

@Stateless
public class UserDAO extends PyramusEntityDAO<User> {
  
}
