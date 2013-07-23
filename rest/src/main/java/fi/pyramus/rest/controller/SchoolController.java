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

  // Schools

  // public School createSchool(String code, String name, SchoolField schoolField) {
  // School school = schoolDAO.create(code, name, schoolField);
  // return school;
  // }

  public List<School> getSchools() {
    List<School> schools = schoolDAO.listAll();
    return schools;
  }

  public School getSchoolById(Long id) {
    try {
      School school = schoolDAO.findById(id);
      return school;
    } catch (NullPointerException e) {
      return null;
    }
  }

  // public School updateSchool(School school, String code, String name, SchoolField schoolField) {
  // schoolDAO.update(school, code, name, schoolField);
  // return school;
  // }

  public School archiveSchool(School school) {
    schoolDAO.archive(school);
    return school;
  }

  public School unarchiveSchool(School school) {
    schoolDAO.unarchive(school);
    return school;
  }

  // SchoolFields

  // public SchoolField createSchoolField(SchoolFieldComplete schoolFieldEntity){
  // SchoolField schoolField = schoolFieldDAO.create(schoolFieldEntity.getName());
  // return schoolField;
  // }

  public List<SchoolField> getSchoolFields() {
    List<SchoolField> schoolFields = schoolFieldDAO.listAll();
    return schoolFields;
  }

  public SchoolField getSchoolFieldById(Long id) {
    try {
      SchoolField schoolField = schoolFieldDAO.findById(id);
      return schoolField;
    } catch (NullPointerException e) {
      return null;
    }
  }

  // public SchoolField updateSchoolField(SchoolField schoolField, String name) {
  // SchoolField schoolFieldUpdated = schoolFieldDAO.update(schoolField, name);
  // if(schoolFieldUpdated != null) {
  // return schoolFieldUpdated;
  // } else {
  // return null;
  // }
  // }

  public SchoolField archiveSchoolField(SchoolField schoolField) {
    schoolFieldDAO.archive(schoolField);
    return schoolField;
  }

  public SchoolField unarchiveSchoolField(SchoolField schoolField) {
    schoolFieldDAO.unarchive(schoolField);
    return schoolField;
  }

  // SchoolVariables

  public List<SchoolVariable> getSchoolVariables() {
    List<SchoolVariable> schoolVariables = schoolVariableDAO.listAll();
    return schoolVariables;
  }

  public SchoolVariable getSchoolVariabledById(Long id) {
    try {
      SchoolVariable schoolVariable = schoolVariableDAO.findById(id);
      return schoolVariable;
    } catch (NullPointerException e) {
      return null;
    }
  }
}
