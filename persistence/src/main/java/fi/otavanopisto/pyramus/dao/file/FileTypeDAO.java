package fi.otavanopisto.pyramus.dao.file;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.file.FileType;

@Stateless
public class FileTypeDAO extends PyramusEntityDAO<FileType> {

  public FileType create(String name) {
    EntityManager entityManager = getEntityManager();

    FileType fileType = new FileType();
    fileType.setName(name);
    
    entityManager.persist(fileType);
    
    return fileType;
  }
  
  public FileType update(FileType fileType, String name) {
    EntityManager entityManager = getEntityManager();

    fileType.setName(name);
    
    entityManager.persist(fileType);
    
    return fileType;
  }
  
}
