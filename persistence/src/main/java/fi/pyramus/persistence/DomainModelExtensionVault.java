package fi.pyramus.persistence;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import fi.pyramus.domainmodel.DomainModelExtensionDescriptor;

import sun.misc.Service;

@SuppressWarnings("restriction")
public class DomainModelExtensionVault {

  public static DomainModelExtensionVault getInstance() {
    return instance;
  }
  
  private final static DomainModelExtensionVault instance = new DomainModelExtensionVault();
  
  public Set<String> getEntityNames() {
    Set<String> entities = new HashSet<String>();
    
    for (Class<?> entityClass : entityClasses) {
      entities.add(entityClass.getName());
    }
    
    return entities;
  }
  
  public Set<Class<?>> getEntityClasses() {
    return entityClasses;
  }
  
  @SuppressWarnings("unchecked")
  private void loadExtensions() {
    Iterator<DomainModelExtensionDescriptor> descriptors = Service.providers(DomainModelExtensionDescriptor.class);
    
    while (descriptors.hasNext()) {
      DomainModelExtensionDescriptor descriptor = descriptors.next();
      for (Class<?> entityClass : descriptor.getEntityClasses()) {
        entityClasses.add(entityClass);
      }
    }
  } 
  
  private Set<Class<?>> entityClasses = new HashSet<Class<?>>();
  
  static {
    getInstance().loadExtensions();
  }
}
