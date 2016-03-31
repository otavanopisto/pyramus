package fi.otavanopisto.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.LanguageDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
@Dependent
public class LanguageController {

  @Inject
  private LanguageDAO languageDAO;
  
  public Language createLanguage(String name, String code) {
    Language language = languageDAO.create(code, name);
    return language;
  }
  
  public List<Language> listLanguages() {
    List<Language> languages = languageDAO.listAll();
    return languages;
  }
  
  public List<Language> listUnarchivedLanguages() {
    List<Language> languages = languageDAO.listUnarchived();
    return languages;
  }
  
  public Language findLanguageById(Long id) {
    Language language = languageDAO.findById(id);
    return language;
  }
  public Language updateLanguage(Language language, String name, String code) {
    Language updated = languageDAO.update(language, name, code);
    return updated;
  }


  public Language archiveLanguage(Language language, User user) {
    languageDAO.archive(language, user);
    return language;
  }

  public void deleteLanguage(Language language) {
    languageDAO.delete(language);
  }

}
