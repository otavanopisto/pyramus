package fi.otavanopisto.pyramus.rest.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.ContactInfoDAO;
import fi.otavanopisto.pyramus.dao.base.ContactURLDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolFieldDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolVariableDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURL;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolField;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolVariable;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolVariableKey;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

@Dependent
@Stateless
public class SchoolController {
  
  @Inject
  private SchoolDAO schoolDAO;
  
  @Inject
  private SchoolFieldDAO schoolFieldDAO;
  
  @Inject
  private SchoolVariableDAO schoolVariableDAO;

  @Inject
  private SchoolVariableKeyDAO schoolVariableKeyDAO;

  @Inject
  private TagDAO tagDAO;

  @Inject
  private EmailDAO emailDAO;

  @Inject
  private PhoneNumberDAO phoneNumberDAO;
  
  @Inject
  private AddressDAO addressDAO;

  @Inject
  private ContactURLDAO contactURLDAO;

  @Inject
  private ContactInfoDAO contactInfoDAO;
  
  /* School */

  public School createSchool(String code, String name, SchoolField schoolField, StudentGroup studentGroup, BillingDetails billingDetails) {
    School school = schoolDAO.create(code, name, schoolField, studentGroup, billingDetails);
    return school;
  }
  
  public School findSchoolById(Long id) {
    School school = schoolDAO.findById(id);
    return school;
  }

  public List<School> listSchools() {
    List<School> schools = schoolDAO.listAll();
    return schools;
  }

  public List<School> listUnarchivedSchools() {
    List<School> schools = schoolDAO.listUnarchived();
    return schools;
  }

  public SearchResult<School> searchSchools(int resultsPerPage, int page, String code, String name, String tags, boolean filterArchived) {
    SearchResult<School> schools = schoolDAO.searchSchools(resultsPerPage, page, code, name, tags, filterArchived);
    return schools;
  }

  public School updateSchool(School school, String code, String name, SchoolField schoolField, StudentGroup studentGroup) {
    return schoolDAO.update(school, code, name, schoolField, studentGroup);
  }

  public School archiveSchool(School school, User user) {
    schoolDAO.archive(school, user);
    return school;
  }

  public School unarchiveSchool(School school, User user) {
    schoolDAO.unarchive(school, user);
    return school;
  }

  public void deleteSchool(School school) {
    List<SchoolVariable> variables = schoolVariableDAO.listBySchool(school);
    for (SchoolVariable variable : variables) {
      schoolVariableDAO.delete(variable);
    }
    
    ArrayList<Tag> tags = new ArrayList<>(school.getTags());
    
    for (Tag tag : tags) {
      removeSchoolTag(school, tag);
    }
    
    schoolDAO.delete(school);
  }
  
  /* Tags */
  
  public synchronized Tag createSchoolTag(School school, String text) {
    Tag tag = tagDAO.findByText(text);
    if (tag == null) {
      tag = tagDAO.create(text);
    }
    
    addSchoolTag(school, tag);
    
    return tag;
  }
  
  public School addSchoolTag(School school, Tag tag) {
    return schoolDAO.addTag(school, tag);
  }

  public School removeSchoolTag(School school, Tag tag) {
    return schoolDAO.removeTag(school, tag);
  }
  
  public School updateSchoolAdditionalContactInfo(School school, String additionalContactInfo) {
    contactInfoDAO.update(school.getContactInfo(), additionalContactInfo);
    return school;
  }

  public synchronized School updateSchoolTags(School school, List<String> tags) {
    Set<String> newTags = new HashSet<>(tags);
    Set<Tag> schoolTags = new HashSet<>(school.getTags());
    
    for (Tag schoolTag : schoolTags) {
      if (!newTags.contains(schoolTag.getText())) {
        removeSchoolTag(school, schoolTag);
      }
        
      newTags.remove(schoolTag.getText());
    }
    
    for (String newTag : newTags) {
      createSchoolTag(school, newTag);
    }
    
    return school;
  }

  public synchronized School updateSchoolVariables(School school, Map<String, String> variables) {
    Set<String> newKeys = new HashSet<>(variables.keySet());
    Set<String> oldKeys = new HashSet<>();
    Set<String> updateKeys = new HashSet<>();
    
    for (SchoolVariable variable : listSchoolVariablesBySchool(school)) {
      oldKeys.add(variable.getKey().getVariableKey());
    }

    for (String oldKey : oldKeys) {
      if (!newKeys.contains(oldKey)) {
        SchoolVariableKey key = findSchoolVariableKeyByVariableKey(oldKey);
        SchoolVariable schoolVariable = findSchoolVariableBySchoolAndKey(school, key);
        deleteSchoolVariable(schoolVariable);
      } else {
        updateKeys.add(oldKey);
      }
      
      newKeys.remove(oldKey);
    }
    
    for (String newKey : newKeys) {
      String value = variables.get(newKey);
      SchoolVariableKey key = findSchoolVariableKeyByVariableKey(newKey);
      createSchoolVariable(school, key, value);
    }
    
    for (String updateKey : updateKeys) {
      String value = variables.get(updateKey);
      SchoolVariableKey key = findSchoolVariableKeyByVariableKey(updateKey);
      SchoolVariable schoolVariable = findSchoolVariableBySchoolAndKey(school, key);
      updateSchoolVariable(schoolVariable, value);
    }
    
    return school;
  }
  
  /* SchoolField */

  public SchoolField createSchoolField(String name) {
    SchoolField schoolField = schoolFieldDAO.create(name);
    return schoolField;
  }
  
  public SchoolField findSchoolFieldById(Long id) {
    SchoolField schoolField = schoolFieldDAO.findById(id);
    return schoolField;
  }
  
  public List<SchoolField> listSchoolFields() {
    List<SchoolField> schoolFields = schoolFieldDAO.listAll();
    return schoolFields;
  }

  public SchoolField updateSchoolField(SchoolField schoolField, String name) {
    return schoolFieldDAO.update(schoolField, name);
  }

  public SchoolField archiveSchoolField(SchoolField schoolField, User user) {
    schoolFieldDAO.archive(schoolField, user);
    return schoolField;
  }

  public SchoolField unarchiveSchoolField(SchoolField schoolField, User user) {
    schoolFieldDAO.unarchive(schoolField, user);
    return schoolField;
  }

  public void deleteSchoolField(SchoolField schoolField) {
    schoolFieldDAO.delete(schoolField);
  }
  
  /* SchoolVariable */

  public SchoolVariable createSchoolVariable(School school, SchoolVariableKey key, String value) {
    return schoolVariableDAO.create(school, key, value);
  }

  public SchoolVariable findSchoolVariableById(Long id) {
    SchoolVariable schoolVariable = schoolVariableDAO.findById(id);
    return schoolVariable;
  }

  public SchoolVariable findSchoolVariableBySchoolAndKey(School school, SchoolVariableKey key) {
    return schoolVariableDAO.findBySchoolAndVariableKey(school, key);
  }

  public void deleteSchoolVariable(SchoolVariable variable) {
    schoolVariableDAO.delete(variable);
  }
  
  public List<SchoolVariable> listSchoolVariablesBySchool(School school) {
    List<SchoolVariable> schoolVariables = schoolVariableDAO.listBySchool(school);
    return schoolVariables;
  }

  public SchoolVariable updateSchoolVariable(SchoolVariable schoolVariable, String value) {
    return schoolVariableDAO.update(schoolVariable, value);
  }

  public SchoolVariable archiveSchoolVariable(SchoolVariable schoolVariable, User user) {
    schoolVariableDAO.archive(schoolVariable, user);
    return schoolVariable;
  }

  public SchoolVariable unarchiveSchoolVariable(SchoolVariable schoolVariable, User user) {
    schoolVariableDAO.unarchive(schoolVariable, user);
    return schoolVariable;
  }

  /* SchoolVariableKey */
  
  public SchoolVariableKey createSchoolVariableKey(String key, String name, VariableType variableType, Boolean userEditable) {
    return schoolVariableKeyDAO.create(key, name, variableType, userEditable);
  }

  public SchoolVariableKey findSchoolVariableKeyByVariableKey(String variableKey) {
    return schoolVariableKeyDAO.findVariableKey(variableKey);
  }
  
  public List<SchoolVariableKey> listSchoolVariableKeys() {
    return schoolVariableKeyDAO.listAll();
  }

  public SchoolVariableKey updateSchoolVariableKey(SchoolVariableKey schoolVariableKey, String variableName, VariableType variableType, Boolean userEditable) {
    return 
      schoolVariableKeyDAO.updateUserEditable(
        schoolVariableKeyDAO.updateVariableName(
            schoolVariableKeyDAO.updateVariableType(schoolVariableKey, variableType), variableName), userEditable);
  }

  public void deleteSchoolVariableKey(SchoolVariableKey schoolVariableKey) {
    schoolVariableKeyDAO.delete(schoolVariableKey);
  }

  /* Email */

  public Email addSchoolEmail(School school, ContactType contactType, String address, Boolean defaultAddress) {
    // Trim the email address
    address = address != null ? address.trim() : null;

    return emailDAO.create(school.getContactInfo(), contactType, defaultAddress, address);
  }
  
  /* Address */

  public Address addSchoolAddress(School school, ContactType contactType, Boolean defaultAddress, String name, String streetAddress, String postalCode, String city, String country) {
    return addressDAO.create(school.getContactInfo(), contactType, name ,streetAddress, postalCode, city, country, defaultAddress);
  }

  /* PhoneNumber */

  public PhoneNumber addSchoolPhoneNumber(School school, ContactType contactType, String number, Boolean defaultNumber) {
    return phoneNumberDAO.create(school.getContactInfo(), contactType, defaultNumber, number);
  }
  
  /* ContactURL */

  public ContactURL addSchoolContactURL(School school, ContactURLType contactURLType, String url) {
    return contactURLDAO.create(school.getContactInfo(), contactURLType, url);
  }
  
}
