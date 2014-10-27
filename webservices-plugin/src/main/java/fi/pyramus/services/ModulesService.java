package fi.pyramus.services;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.pyramus.dao.courses.CourseDescriptionDAO;
import fi.pyramus.dao.modules.ModuleComponentDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.base.CourseBase;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.modules.ModuleComponent;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.services.entities.EntityFactoryVault;
import fi.pyramus.services.entities.courses.CourseDescriptionEntity;
import fi.pyramus.services.entities.modules.ModuleComponentEntity;
import fi.pyramus.services.entities.modules.ModuleEntity;

@Stateless
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@RolesAllowed("WebServices")
public class ModulesService extends PyramusService {

  public ModuleEntity createModule(@WebParam(name = "name") String name, @WebParam(name = "subjectId") Long subjectId,
      @WebParam(name = "courseNumber") Integer courseNumber, @WebParam(name = "moduleLength") Double moduleLength,
      @WebParam(name = "moduleLengthTimeUnitId") Long moduleLengthTimeUnitId, @WebParam(name = "description") String description,
      @WebParam(name = "creatingUserId") Long creatingUserId) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();

    Subject subject = subjectDAO.findById(subjectId);
    User creatingUser = userDAO.findById(creatingUserId);
    EducationalTimeUnit moduleLengthTimeUnit = moduleLengthTimeUnitId == null ? null : educationalTimeUnitDAO.findById(moduleLengthTimeUnitId);

    Module module = moduleDAO.create(name, subject, courseNumber, moduleLength, moduleLengthTimeUnit, description, null, creatingUser);

    validateEntity(module);

    return EntityFactoryVault.buildFromDomainObject(module);
  }

  public void updateModule(@WebParam(name = "moduleId") Long moduleId, @WebParam(name = "name") String name, @WebParam(name = "subjectId") Long subjectId,
      @WebParam(name = "courseNumber") Integer courseNumber, @WebParam(name = "length") Double length,
      @WebParam(name = "lengthTimeUnitId") Long lengthTimeUnitId, @WebParam(name = "description") String description,
      @WebParam(name = "modifyingUserId") Long modifyingUserId) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();

    Module module = moduleDAO.findById(moduleId);
    Subject subject = subjectDAO.findById(subjectId);
    User modifyingUser = userDAO.findById(modifyingUserId);
    EducationalTimeUnit moduleLengthTimeUnit = lengthTimeUnitId == null ? null : educationalTimeUnitDAO.findById(lengthTimeUnitId);

    moduleDAO.update(module, name, subject, courseNumber, length, moduleLengthTimeUnit, description, module.getMaxParticipantCount(), modifyingUser);

    validateEntity(module);
  }

  public ModuleEntity getModuleById(@WebParam(name = "moduleId") Long moduleId) {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    return EntityFactoryVault.buildFromDomainObject(moduleDAO.findById(moduleId));
  }

  public void archiveModule(@WebParam(name = "moduleId") Long moduleId) {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    Module module = moduleDAO.findById(moduleId);
    moduleDAO.archive(module);
  }

  public ModuleComponentEntity getModuleComponentById(@WebParam(name = "moduleComponentId") Long moduleComponentId) {
    ModuleComponentDAO moduleComponentDAO = DAOFactory.getInstance().getModuleComponentDAO();
    return EntityFactoryVault.buildFromDomainObject(moduleComponentDAO.findById(moduleComponentId));
  }

  public ModuleComponentEntity createModuleComponent(@WebParam(name = "moduleId") Long moduleId, @WebParam(name = "length") Double length,
      @WebParam(name = "lengthTimeUnitId") Long lengthTimeUnitId, @WebParam(name = "name") String name, @WebParam(name = "description") String description) {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    ModuleComponentDAO moduleComponentDAO = DAOFactory.getInstance().getModuleComponentDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();

    Module module = moduleDAO.findById(moduleId);
    EducationalTimeUnit lengthTimeUnit = lengthTimeUnitId == null ? null : educationalTimeUnitDAO.findById(lengthTimeUnitId);

    ModuleComponent moduleComponent = moduleComponentDAO.create(module, length, lengthTimeUnit, name, description);

    validateEntity(moduleComponent);

    return EntityFactoryVault.buildFromDomainObject(moduleComponent);
  }

  public ModuleComponentEntity updateModuleComponent(@WebParam(name = "moduleComponentId") Long moduleComponentId, @WebParam(name = "length") Double length,
      @WebParam(name = "lengthTimeUnitId") Long lengthTimeUnitId, @WebParam(name = "name") String name, @WebParam(name = "description") String description) {
    ModuleComponentDAO moduleComponentDAO = DAOFactory.getInstance().getModuleComponentDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();

    ModuleComponent moduleComponent = moduleComponentDAO.findById(moduleComponentId);
    EducationalTimeUnit lengthTimeUnit = lengthTimeUnitId == null ? null : educationalTimeUnitDAO.findById(lengthTimeUnitId);

    moduleComponentDAO.update(moduleComponent, length, lengthTimeUnit, name, description);

    validateEntity(moduleComponent);

    return EntityFactoryVault.buildFromDomainObject(moduleComponent);
  }

  public ModuleEntity[] listModules() {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    return (ModuleEntity[]) EntityFactoryVault.buildFromDomainObjects(moduleDAO.listUnarchived());
  }

  public ModuleEntity[] listModulesByEducationType(@WebParam(name = "educationTypeId") Long educationTypeId) {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();

    EducationType educationType = educationTypeDAO.findById(educationTypeId);
    return (ModuleEntity[]) EntityFactoryVault.buildFromDomainObjects(moduleDAO.listByEducationType(educationType));
  }

  public CourseDescriptionEntity[] listModuleDescriptionsByModuleId(@WebParam(name = "moduleId") Long moduleId) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseBase courseBase = courseDAO.findById(moduleId);
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    return (CourseDescriptionEntity[]) EntityFactoryVault.buildFromDomainObjects(descriptionDAO.listByCourseBase(courseBase));
  }

  public CourseDescriptionEntity getModuleDescriptionByModuleIdAndCategoryId(@WebParam(name = "moduleId") Long moduleId,
      @WebParam(name = "categoryId") Long categoryId) {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    CourseBase courseBase = moduleDAO.findById(moduleId);
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    CourseDescriptionCategory category = descriptionCategoryDAO.findById(categoryId);
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();

    return (CourseDescriptionEntity) EntityFactoryVault.buildFromDomainObject(descriptionDAO.findByCourseAndCategory(courseBase, category));
  }

}
