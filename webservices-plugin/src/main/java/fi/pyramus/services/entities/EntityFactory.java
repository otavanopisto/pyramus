package fi.pyramus.services.entities;

public interface EntityFactory<T> {
  
  public T buildFromDomainObject(Object domainObject);
  
}
