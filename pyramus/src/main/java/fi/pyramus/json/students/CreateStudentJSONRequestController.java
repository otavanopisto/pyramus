package fi.pyramus.json.students;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AddressDAO;
import fi.pyramus.dao.base.ContactInfoDAO;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.EmailDAO;
import fi.pyramus.dao.base.LanguageDAO;
import fi.pyramus.dao.base.MunicipalityDAO;
import fi.pyramus.dao.base.NationalityDAO;
import fi.pyramus.dao.base.PhoneNumberDAO;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.students.AbstractStudentDAO;
import fi.pyramus.dao.students.StudentActivityTypeDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.students.StudentEducationalLevelDAO;
import fi.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.pyramus.dao.students.StudentVariableDAO;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class CreateStudentJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    StudentActivityTypeDAO activityTypeDAO = DAOFactory.getInstance().getStudentActivityTypeDAO();
    StudentExaminationTypeDAO examinationTypeDAO = DAOFactory.getInstance().getStudentExaminationTypeDAO();
    StudentEducationalLevelDAO educationalLevelDAO = DAOFactory.getInstance().getStudentEducationalLevelDAO();
    StudentStudyEndReasonDAO studyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    StudentVariableDAO studentVariableDAO = DAOFactory.getInstance().getStudentVariableDAO();
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    ContactInfoDAO contactInfoDAO = DAOFactory.getInstance().getContactInfoDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();

    Date birthday = requestContext.getDate("birthday");
    String ssecId = requestContext.getString("ssecId");
    Sex sex = "male".equals(requestContext.getRequest().getParameter("gender")) ? Sex.MALE : Sex.FEMALE;
    String basicInfo = requestContext.getString("basicInfo");
    Boolean secureInfo = requestContext.getBoolean("secureInfo");
    String firstName = requestContext.getString("firstName");
    String lastName = requestContext.getString("lastName");
    String nickname = requestContext.getString("nickname");
    String additionalInfo = requestContext.getString("additionalInfo");
    String otherContactInfo = requestContext.getString("otherContactInfo");
    String education = requestContext.getString("education");
    Boolean lodging = "1".equals(requestContext.getString("lodging"));
    Double previousStudies = requestContext.getDouble("previousStudies");
    Date studyTimeEnd = requestContext.getDate("studyTimeEnd");
    Date studyStartTime = requestContext.getDate("studyStartDate");
    Date studyEndTime = requestContext.getDate("studyEndDate");
    String studyEndText = requestContext.getString("studyEndText");
    String tagsText = requestContext.getString("tags");
    
    Set<Tag> tagEntities = new HashSet<Tag>();
    if (!StringUtils.isBlank(tagsText)) {
      List<String> tags = Arrays.asList(tagsText.split("[\\ ,]"));
      for (String tag : tags) {
        if (!StringUtils.isBlank(tag)) {
          Tag tagEntity = tagDAO.findByText(tag.trim());
          if (tagEntity == null)
            tagEntity = tagDAO.create(tag);
          tagEntities.add(tagEntity);
        }
      }
    }
    
    Long entityId = requestContext.getLong("language");
    Language language = entityId == null ? null : languageDAO.findById(entityId);

    entityId = requestContext.getLong("municipality");
    Municipality municipality = entityId == null ? null : municipalityDAO.findById(entityId);

    entityId = requestContext.getLong("activityType");
    StudentActivityType activityType = entityId == null ? null : activityTypeDAO.findById(entityId);

    entityId = requestContext.getLong("examinationType");
    StudentExaminationType examinationType = entityId == null ? null : examinationTypeDAO.findById(entityId);

    entityId = requestContext.getLong("educationalLevel");
    StudentEducationalLevel educationalLevel = entityId == null ? null : educationalLevelDAO.findById(entityId);

    entityId = requestContext.getLong("nationality");
    Nationality nationality = entityId == null ? null : nationalityDAO.findById(entityId);

    entityId = requestContext.getLong("school");
    School school = entityId != null && entityId > 0 ? schoolDAO.findById(entityId) : null;

    entityId = requestContext.getLong("studyProgramme");
    StudyProgramme studyProgramme = entityId != null && entityId > 0 ? studyProgrammeDAO.findById(entityId) : null;

    entityId = requestContext.getLong("studyEndReason");
    StudentStudyEndReason studyEndReason = entityId == null ? null : studyEndReasonDAO.findById(entityId);

    AbstractStudent abstractStudent = abstractStudentDAO.findBySSN(ssecId);
    if (abstractStudent == null) {
      abstractStudent = abstractStudentDAO.create(birthday, ssecId, sex, basicInfo, secureInfo);
    }
    else {
      abstractStudentDAO.update(abstractStudent, birthday, ssecId, sex, basicInfo, secureInfo);
    }
    
    Student student = studentDAO.create(abstractStudent, firstName, lastName, nickname, additionalInfo,
        studyTimeEnd, activityType, examinationType, educationalLevel, education, nationality, municipality,
        language, school, studyProgramme, previousStudies, studyStartTime, studyEndTime, studyEndReason, studyEndText, lodging);

    // Tags

    studentDAO.setStudentTags(student, tagEntities);
    
    // Contact info
    
    contactInfoDAO.update(student.getContactInfo(), otherContactInfo);

    // Addresses
    
    int addressCount = requestContext.getInteger("addressTable.rowCount");
    for (int i = 0; i < addressCount; i++) {
      String colPrefix = "addressTable." + i;
      Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String name = requestContext.getString(colPrefix + ".name");
      String street = requestContext.getString(colPrefix + ".street");
      String postal = requestContext.getString(colPrefix + ".postal");
      String city = requestContext.getString(colPrefix + ".city");
      String country = requestContext.getString(colPrefix + ".country");
      boolean hasAddress = name != null || street != null || postal != null || city != null || country != null;
      if (hasAddress) {
        addressDAO.create(student.getContactInfo(), contactType, name, street, postal, city, country, defaultAddress);
      }
    }
    
    // Email addresses

    int emailCount = requestContext.getInteger("emailTable.rowCount");
    for (int i = 0; i < emailCount; i++) {
      String colPrefix = "emailTable." + i;
      Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String email = requestContext.getString(colPrefix + ".email");
      if (email != null) {
        emailDAO.create(student.getContactInfo(), contactType, defaultAddress, email);
      }
    }
    
    // Phone numbers

    int phoneCount = requestContext.getInteger("phoneTable.rowCount");
    for (int i = 0; i < phoneCount; i++) {
      String colPrefix = "phoneTable." + i;
      Boolean defaultNumber = requestContext.getBoolean(colPrefix + ".defaultNumber");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String number = requestContext.getString(colPrefix + ".phone");
      if (number != null) {
        phoneNumberDAO.create(student.getContactInfo(), contactType, defaultNumber, number);
      }
    }
    
    // Student variables

    Integer variableCount = requestContext.getInteger("variablesTable.rowCount");
    if (variableCount != null) {
      for (int i = 0; i < variableCount; i++) {
        String colPrefix = "variablesTable." + i;
        String variableKey = requestContext.getRequest().getParameter(colPrefix + ".key");
        String variableValue = requestContext.getRequest().getParameter(colPrefix + ".value");
        studentVariableDAO.setStudentVariable(student, variableKey, variableValue);
      }
    }
    
    // Contact information of a student won't be reflected to AbstractStudent
    // used when searching students, so a manual re-index is needed

    abstractStudentDAO.forceReindex(student.getAbstractStudent());
    
    String redirectURL = requestContext.getRequest().getContextPath() + "/students/editstudent.page?abstractStudent=" + student.getAbstractStudent().getId();
    String refererAnchor = requestContext.getRefererAnchor();
    
    if (!StringUtils.isBlank(refererAnchor)) {
      redirectURL += "#" + refererAnchor;
    }

    requestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}