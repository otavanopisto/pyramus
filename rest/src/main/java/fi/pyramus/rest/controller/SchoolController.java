package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.SchoolFieldDAO;
import fi.pyramus.dao.base.SchoolVariableDAO;
import fi.pyramus.dao.base.SchoolVariableKeyDAO;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.domainmodel.base.SchoolVariable;
import fi.pyramus.domainmodel.base.SchoolVariableKey;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.search.SearchResult;

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
  
   public School createSchool(String code, String name, SchoolField schoolField) {
     School school = schoolDAO.create(code, name, schoolField);
     return school;
   }
   
   public SchoolField createSchoolField(String name) {
     SchoolField schoolField = schoolFieldDAO.create(name);
     return schoolField;
   }
   
   public SchoolVariable createSchoolVariable(School school, Long keyId, String value) {
     SchoolVariableKey key = schoolVariableKeyDAO.findById(keyId);
     SchoolVariable schoolVariable = schoolVariableDAO.create(school, key, value);
     return schoolVariable;
   }
  
  public List<School> findSchools() {
    List<School> schools = schoolDAO.listAll();
    return schools;
  }
  
  public SearchResult<School> searchSchools(int resultsPerPage, int page, String code, String name, String tags, boolean filterArchived) {
      SearchResult<School> schools = schoolDAO.searchSchools(resultsPerPage, page, code, name, tags, filterArchived);
      return schools;
  }
  
  public List<School> findUnarchivedSchools() {
    List<School> schools = schoolDAO.listUnarchived();
    return schools;
  }

  public School findSchoolById(Long id) {
    try {
      School school = schoolDAO.findById(id);
      return school;
    } catch (NullPointerException e) {
      return null;
    }
  }

  public List<SchoolField> findSchoolFields() {
    List<SchoolField> schoolFields = schoolFieldDAO.listAll();
    return schoolFields;
  }

  public SchoolField findSchoolFieldById(Long id) {
    try {
      SchoolField schoolField = schoolFieldDAO.findById(id);
      return schoolField;
    } catch (NullPointerException e) {
      return null;
    }
  }

  public List<SchoolVariable> findSchoolVariables() {
    List<SchoolVariable> schoolVariables = schoolVariableDAO.listAll();
    return schoolVariables;
  }

  public SchoolVariable findSchoolVariablesById(Long id) {
    try {
      SchoolVariable schoolVariable = schoolVariableDAO.findById(id);
      return schoolVariable;
    } catch (NullPointerException e) {
      return null;
    }
  }
  
  public School updateSchool(School school, String code, String name, SchoolField schoolField) {
    return schoolDAO.update(school, code, name, schoolField);
  }
  
  public SchoolField updateSchoolField(SchoolField schoolField, String name) {
    return schoolFieldDAO.update(schoolField, name);
  }
   
  public SchoolVariable updateSchoolVariable(SchoolVariable schoolVariable, String value) {
    return schoolVariableDAO.update(schoolVariable, value);
  }

  public School archiveSchool(School school, User user) {
    schoolDAO.archive(school, user);
    return school;
  }

  public School unarchiveSchool(School school, User user) {
    schoolDAO.unarchive(school, user);
    return school;
  }
 
  public SchoolField archiveSchoolField(SchoolField schoolField, User user) {
    schoolFieldDAO.archive(schoolField, user);
    return schoolField;
  }

  public SchoolField unarchiveSchoolField(SchoolField schoolField, User user) {
    schoolFieldDAO.unarchive(schoolField, user);
    return schoolField;
  }

//  public SchoolVariable archiveSchoolVariable(SchoolVariable schoolVariable) {
//    schoolVariableDAO.archive(schoolVariable);
//    return schoolVariable;
//  }
//  
//  public SchoolVariable unarchiveSchoolVariable(SchoolVariable schoolVariable) {
//    schoolVariableDAO.unarchive(schoolVariable);
//    return schoolVariable;
//  }
  

}
