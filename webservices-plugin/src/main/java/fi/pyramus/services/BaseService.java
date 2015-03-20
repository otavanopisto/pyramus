package fi.pyramus.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AcademicTermDAO;
import fi.pyramus.dao.base.AddressDAO;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.EducationSubtypeDAO;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.LanguageDAO;
import fi.pyramus.dao.base.MunicipalityDAO;
import fi.pyramus.dao.base.NationalityDAO;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.SchoolVariableDAO;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.services.entities.EntityFactoryVault;
import fi.pyramus.services.entities.base.AcademicTermEntity;
import fi.pyramus.services.entities.base.EducationSubtypeEntity;
import fi.pyramus.services.entities.base.EducationTypeEntity;
import fi.pyramus.services.entities.base.EducationalTimeUnitEntity;
import fi.pyramus.services.entities.base.LanguageEntity;
import fi.pyramus.services.entities.base.MunicipalityEntity;
import fi.pyramus.services.entities.base.NationalityEntity;
import fi.pyramus.services.entities.base.SchoolEntity;
import fi.pyramus.services.entities.base.StudyProgrammeEntity;
import fi.pyramus.services.entities.base.SubjectEntity;
import fi.pyramus.util.StringAttributeComparator;

@Stateless
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@RolesAllowed("WebServices")
public class BaseService extends PyramusService {

  public NationalityEntity getNationalityByCode(@WebParam (name = "code") String code) {
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    return EntityFactoryVault.buildFromDomainObject(nationalityDAO.findByCode(code));
  }

  public NationalityEntity getNationalityById(@WebParam (name = "nationalityId") Long nationalityId) {
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    return EntityFactoryVault.buildFromDomainObject(nationalityDAO.findById(nationalityId));
  }

  public NationalityEntity[] listNationalities() {
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    List<Nationality> nationalities = nationalityDAO.listUnarchived();
    Collections.sort(nationalities, new StringAttributeComparator("getName"));
    return (NationalityEntity[]) EntityFactoryVault.buildFromDomainObjects(nationalities);
  }

  public LanguageEntity getLanguageByCode(@WebParam (name = "code") String code) {
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    return EntityFactoryVault.buildFromDomainObject(languageDAO.findByCode(code));
  }

  public LanguageEntity getLanguageById(@WebParam (name = "languageId") Long languageId) {
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    return EntityFactoryVault.buildFromDomainObject(languageDAO.findById(languageId));
  }

  public LanguageEntity[] listLanguages() {
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    List<Language> languages = languageDAO.listUnarchived();
    Collections.sort(languages, new StringAttributeComparator("getName"));
    return (LanguageEntity[]) EntityFactoryVault.buildFromDomainObjects(languages);
  }

  public MunicipalityEntity getMunicipalityByCode(@WebParam (name = "code") String code) {
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    return EntityFactoryVault.buildFromDomainObject(municipalityDAO.findByCode(code));
  }

  public MunicipalityEntity getMunicipalityById(@WebParam (name = "municipalityId") Long municipalityId) {
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    return EntityFactoryVault.buildFromDomainObject(municipalityDAO.findById(municipalityId));
  }

  public MunicipalityEntity[] listMunicipalities() {
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    List<Municipality> municipalities = municipalityDAO.listUnarchived();
    Collections.sort(municipalities, new StringAttributeComparator("getName"));
    return (MunicipalityEntity[]) EntityFactoryVault.buildFromDomainObjects(municipalities);
  }

  public EducationalTimeUnitEntity getEducationalTimeUnitById(@WebParam (name = "educationalTimeUnitId") Long educationalTimeUnitId) {
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    EducationalTimeUnit educationalTimeUnit = educationalTimeUnitDAO.findById(educationalTimeUnitId);
    return EntityFactoryVault.buildFromDomainObject(educationalTimeUnit);
  }

  public EducationalTimeUnitEntity[] listEducationalTimeUnits() {
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    List<EducationalTimeUnit> educationalTimeUnits = educationalTimeUnitDAO.listUnarchived();
    Collections.sort(educationalTimeUnits, new StringAttributeComparator("getName"));
    return (EducationalTimeUnitEntity[]) EntityFactoryVault.buildFromDomainObjects(educationalTimeUnits);
  }

  public EducationalTimeUnitEntity createEducationalTimeUnit(@WebParam (name = "baseUnits") Double baseUnits, @WebParam (name = " name") String name) {
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    EducationalTimeUnit educationalTimeUnit = educationalTimeUnitDAO.create(baseUnits, name, "");
    validateEntity(educationalTimeUnit);
    return EntityFactoryVault.buildFromDomainObject(educationalTimeUnit);
  }

  public AcademicTermEntity getAcademicTermById(@WebParam (name = "academicTermId") Long academicTermId) {
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();
    AcademicTerm academicTerm = academicTermDAO.findById(academicTermId);
    return EntityFactoryVault.buildFromDomainObject(academicTerm);
  }

  public AcademicTermEntity[] listAcademicTerms() {
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();
    List<AcademicTerm> academicTerms = academicTermDAO.listUnarchived();

    Collections.sort(academicTerms, new Comparator<AcademicTerm>() {
      public int compare(AcademicTerm o1, AcademicTerm o2) {
        return o1.getStartDate() == null ? -1 : o2.getStartDate() == null ? 1 : o1.getStartDate().compareTo(o2.getStartDate());
      }
    });
    
    return (AcademicTermEntity[]) EntityFactoryVault.buildFromDomainObjects(academicTerms);
  }

  /*Dateformat: [-]CCYY-MM-DDThh:mm:ss[Z|(+|-)hh:mm] */
  public AcademicTermEntity createAcademicTerm(@WebParam (name = "name") String name, @WebParam (name = "startDate") Date startDate, @WebParam (name = "endDate") Date endDate) {
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();
    AcademicTerm academicTerm = academicTermDAO.create(name, startDate, endDate);
    validateEntity(academicTerm);
    return EntityFactoryVault.buildFromDomainObject(academicTerm);
  }

  public void updateAcademicTerm(@WebParam (name = "academicTermId") Long academicTermId, @WebParam (name = "name") String name, @WebParam (name = "startDate") Date startDate, @WebParam (name = "endDate") Date endDate) {
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();
    AcademicTerm academicTerm = academicTermDAO.findById(academicTermId);
    academicTermDAO.update(academicTerm, name, startDate, endDate);
    validateEntity(academicTerm);
  }

  public EducationSubtypeEntity createEducationSubtype(@WebParam (name = "educationTypeId") Long educationTypeId, @WebParam (name = "name") String name, @WebParam (name = "code") String code) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();    
    EducationType educationType = educationTypeDAO.findById(educationTypeId);
    EducationSubtype educationSubtype = educationSubtypeDAO.create(educationType, name, code);
    validateEntity(educationSubtype);
    return EntityFactoryVault.buildFromDomainObject(educationSubtype);
  }

  public void updateEducationSubtype(@WebParam (name = "educationSubtypeId") Long educationSubtypeId, @WebParam (name = "name") String name, @WebParam (name = "code") String code) {
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();    
    EducationSubtype educationSubtype = educationSubtypeDAO.findById(educationSubtypeId);
    educationSubtypeDAO.update(educationSubtype, name, code);
    validateEntity(educationSubtype);
  }

  public EducationTypeEntity createEducationType(@WebParam (name = "name") String name, @WebParam (name = "code") String code) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationType educationType = educationTypeDAO.create(name, code);
    validateEntity(educationType);
    return EntityFactoryVault.buildFromDomainObject(educationType);
  }

  public void updateEducationType(@WebParam (name = "educationTypeId") Long educationTypeId, @WebParam (name = "name") String name, @WebParam (name = "code") String code) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationType educationType = educationTypeDAO.findById(educationTypeId);
    educationTypeDAO.update(educationType, name, code);
    validateEntity(educationType);
  }

  public EducationSubtypeEntity getEducationSubtypeById(@WebParam (name = "educationSubtypeId") Long educationSubtypeId) {
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();    
    return EntityFactoryVault.buildFromDomainObject(educationSubtypeDAO.findById(educationSubtypeId));
  }

  public EducationSubtypeEntity getEducationSubtypeByCode(@WebParam (name = "code") String code) {
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();    
    return EntityFactoryVault.buildFromDomainObject(educationSubtypeDAO.findByCode(code));
  }

  public EducationTypeEntity getEducationTypeById(@WebParam (name = "educationTypeId") Long educationTypeId) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    return EntityFactoryVault.buildFromDomainObject(educationTypeDAO.findById(educationTypeId));
  }

  public EducationTypeEntity getEducationTypeByCode(@WebParam (name = "code") String code) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    return EntityFactoryVault.buildFromDomainObject(educationTypeDAO.findByCode(code));
  }

  public EducationTypeEntity[] listEducationTypes() {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    return (EducationTypeEntity[]) EntityFactoryVault.buildFromDomainObjects(educationTypes);
  }

  public EducationSubtypeEntity[] listEducationSubtypesByEducationType(@WebParam (name = "educationTypeId") Long educationTypeId) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();    

    EducationType educationType = educationTypeDAO.findById(educationTypeId);
    List<EducationSubtype> educationSubtypes = educationSubtypeDAO.listByEducationType(educationType);
    Collections.sort(educationSubtypes, new StringAttributeComparator("getName"));
    return (EducationSubtypeEntity[]) EntityFactoryVault.buildFromDomainObjects(educationSubtypes);
  }

  public SubjectEntity getSubjectById(@WebParam (name = "subjectId") Long subjectId) {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    return EntityFactoryVault.buildFromDomainObject(subjectDAO.findById(subjectId));
  }

  public SubjectEntity getSubjectByCode(@WebParam (name = "code") String code) {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    return EntityFactoryVault.buildFromDomainObject(subjectDAO.findByCode(code));
  }

  public SchoolEntity[] listSchools() {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    List<School> schools = schoolDAO.listUnarchived();
    Collections.sort(schools, new StringAttributeComparator("getName"));
    return (SchoolEntity[]) EntityFactoryVault.buildFromDomainObjects(schools);
  }

  public SchoolEntity[] listSchoolsByVariable(@WebParam (name = "key") String key, @WebParam (name = "value") String value) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    List<School> schools = schoolDAO.listByVariable(key, value);
    Collections.sort(schools, new StringAttributeComparator("getName"));
    return (SchoolEntity[]) EntityFactoryVault.buildFromDomainObjects(schools);
  }

  public StudyProgrammeEntity[] listStudyProgrammes() {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    return (StudyProgrammeEntity[]) EntityFactoryVault.buildFromDomainObjects(studyProgrammeDAO.listUnarchived());
  }

  public SubjectEntity[] listSubjects() {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    List<Subject> subjects = subjectDAO.listUnarchived();
    Collections.sort(subjects, new StringAttributeComparator("getName"));
    return (SubjectEntity[]) EntityFactoryVault.buildFromDomainObjects(subjects);
  }

  public SubjectEntity createSubject(@WebParam (name = "code") String code, @WebParam (name = "name") String name, @WebParam (name = "educationTypeId") Long educationTypeId) {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationType educationType = educationTypeId != null ? educationTypeDAO.findById(educationTypeId) : null;
    Subject subject = subjectDAO.create(code, name, educationType);
    validateEntity(subject);
    return EntityFactoryVault.buildFromDomainObject(subject);
  }

  public void updateSubject(@WebParam (name = "subjectId") Long subjectId, @WebParam (name = "code") String code, @WebParam (name = "name") String name, @WebParam (name = "educationTypeId") Long educationTypeId) {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    Subject subject = subjectDAO.findById(subjectId);
    EducationType educationType = educationTypeId != null ? educationTypeDAO.findById(educationTypeId) : null;
    subjectDAO.update(subject, code, name, educationType);
    validateEntity(subject);
  }

  public SchoolEntity createSchool(@WebParam (name = "code") String code, @WebParam (name = "name") String name) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    // TODO: schoolField parameter
    School school = schoolDAO.create(code, name, null);
    validateEntity(school);
    return EntityFactoryVault.buildFromDomainObject(school);
  }

  public SchoolEntity getSchoolById(@WebParam (name = "schoolId") Long schoolId) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    return EntityFactoryVault.buildFromDomainObject(schoolDAO.findById(schoolId));
  }

  public void updateSchool(@WebParam (name = "schoolId") Long schoolId, @WebParam (name = "code") String code, @WebParam (name = "name") String name) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    School school = schoolDAO.findById(schoolId);
    // TODO: schoolField parameter
    schoolDAO.update(school, code, name, school.getField());
    validateEntity(school);
  }

  public String getSchoolVariable(@WebParam (name = "schoolId") Long schoolId, @WebParam (name = "key") String key) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    SchoolVariableDAO schoolVariableDAO = DAOFactory.getInstance().getSchoolVariableDAO();
    School school = schoolDAO.findById(schoolId);
    return schoolVariableDAO.findValueBySchoolAndKey(school, key);
  }

  public void setSchoolVariable(@WebParam (name = "schoolId") Long schoolId, @WebParam (name = "key") String key, @WebParam (name = "value") String value) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    SchoolVariableDAO schoolVariableDAO = DAOFactory.getInstance().getSchoolVariableDAO();
    School school = schoolDAO.findById(schoolId);
    schoolVariableDAO.setVariable(school, key, value);
  }

  public void updateAddress(@WebParam (name = "addressId") Long addressId, @WebParam (name = "defaultAddress") Boolean defaultAddress, @WebParam (name = "contactTypeId") Long contactTypeId, 
      @WebParam (name = "name") String name, @WebParam (name = "streetAddress") String streetAddress, @WebParam (name = "postalCode") String postalCode, @WebParam (name = "city") String city, @WebParam (name = "country") String country) {
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();

    Address address = addressDAO.findById(addressId);
    ContactType contactType = contactTypeDAO.findById(contactTypeId);
    
    addressDAO.update(address, defaultAddress, contactType, name, streetAddress, postalCode, city, country);
  }
}
