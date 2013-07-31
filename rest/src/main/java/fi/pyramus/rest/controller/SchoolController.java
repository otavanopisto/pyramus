package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.SchoolFieldDAO;
import fi.pyramus.dao.base.SchoolVariableDAO;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.domainmodel.base.SchoolVariable;

@Dependent
@Stateless
public class SchoolController {
  @Inject
  private SchoolDAO schoolDAO;
  @Inject
  private SchoolFieldDAO schoolFieldDAO;
  @Inject
  private SchoolVariableDAO schoolVariableDAO;
  
   public School createSchool(String code, String name, SchoolField schoolField) {
     School school = schoolDAO.create(code, name, schoolField);
     return school;
   }
   
   public SchoolField createSchoolField(String name) {
     SchoolField schoolField = schoolFieldDAO.create(name);
     return schoolField;
   }
  
  public List<School> findSchools() {
    List<School> schools = schoolDAO.listAll();
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

  public School archiveSchool(School school) {
    schoolDAO.archive(school);
    return school;
  }

  public School unarchiveSchool(School school) {
    schoolDAO.unarchive(school);
    return school;
  }
 
  public SchoolField archiveSchoolField(SchoolField schoolField) {
    schoolFieldDAO.archive(schoolField);
    return schoolField;
  }

  public SchoolField unarchiveSchoolField(SchoolField schoolField) {
    schoolFieldDAO.unarchive(schoolField);
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
