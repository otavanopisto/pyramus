package fi.pyramus.dao.file;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.file.FileType;

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
