package fi.pyramus.services;

import java.util.Set;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.SystemDAO;

public class PyramusService {

  protected void validateEntity(Object entity) {
	SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO(); 
	  
	Set<ConstraintViolation<Object>> constraintViolations = systemDAO.validateEntity(entity);
	if (constraintViolations.size() != 0) {
	  String message = "";
	  for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
	    message += constraintViolation.getMessage() + '\n';
	  }  	
		
	  throw new PersistenceException(message);
	}
  }
}