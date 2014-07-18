package fi.pyramus.rest.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.SchoolFieldDAO;
import fi.pyramus.dao.base.SchoolVariableDAO;
import fi.pyramus.dao.base.SchoolVariableKeyDAO;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.domainmodel.base.SchoolVariable;
import fi.pyramus.domainmodel.base.SchoolVariableKey;
import fi.pyramus.domainmodel.base.Tag;
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

  @Inject
  private TagDAO tagDAO;
  
  /* School */

  public School createSchool(String code, String name, SchoolField schoolField) {
    School school = schoolDAO.create(code, name, schoolField);
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

  public School updateSchool(School school, String code, String name, SchoolField schoolField) {
    return schoolDAO.update(school, code, name, schoolField);
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
    schoolDAO.delete(school);
  }
  
  /* Tags */
  
  public Tag createSchoolTag(School school, String text) {
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

  public School updateSchoolTags(School school, List<String> tags) {
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
  
  /* SchoolVariable */

  public SchoolVariable createSchoolVariable(School school, Long keyId, String value) {
    SchoolVariableKey key = schoolVariableKeyDAO.findById(keyId);
    SchoolVariable schoolVariable = schoolVariableDAO.create(school, key, value);
    return schoolVariable;
  }

  public SchoolVariable findSchoolVariableById(Long id) {
    SchoolVariable schoolVariable = schoolVariableDAO.findById(id);
    return schoolVariable;
  }
  
  public List<SchoolVariable> listSchoolVariables() {
    List<SchoolVariable> schoolVariables = schoolVariableDAO.listAll();
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

}
