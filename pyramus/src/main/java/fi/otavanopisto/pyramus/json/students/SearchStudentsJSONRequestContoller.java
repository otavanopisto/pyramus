package fi.otavanopisto.pyramus.json.students;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.LanguageDAO;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.dao.base.NationalityDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.persistence.search.PersonFilter;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

/**
 * Request handler for searching students.
 * 
 * @author antti.viljakainen
 */
public class SearchStudentsJSONRequestContoller extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    
    StaffMember loggedUser = staffMemberDAO.findById(jsonRequestContext.getLoggedUserId());
    Organization organization = UserUtils.canAccessAllOrganizations(loggedUser) ?
        null : loggedUser.getOrganization();
    
    Integer resultsPerPage = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("maxResults"));
    if (resultsPerPage == null) {
      resultsPerPage = 10;
    }

    Integer page = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("page"));
    if (page == null) {
      page = 0;
    }
    
    SearchResult<Person> searchResult;
    
    if ("advanced".equals(jsonRequestContext.getRequest().getParameter("activeTab"))) {
      String firstName = jsonRequestContext.getString("firstname");
      String nickname = jsonRequestContext.getString("nickname");
      String lastName = jsonRequestContext.getString("lastname");
      String tags = jsonRequestContext.getString("tags");
      if (!StringUtils.isBlank(tags))
        tags = tags.replace(',', ' ');
      
      String education = jsonRequestContext.getString("education");
      String email = jsonRequestContext.getString("email");
      Sex sex = (Sex) jsonRequestContext.getEnum("sex", Sex.class);
      String ssn = jsonRequestContext.getString("ssn");
      String addressCity = jsonRequestContext.getString("addressCity");
      String addressCountry = jsonRequestContext.getString("addressCountry");
      String addressPostalCode = jsonRequestContext.getString("addressPostalCode");
      String addressStreetAddress = jsonRequestContext.getString("addressStreetAddress");
      String phone = jsonRequestContext.getString("phone");
      String title = jsonRequestContext.getString("title");
      PersonFilter personFilter = (PersonFilter) jsonRequestContext.getEnum("studentFilter", PersonFilter.class);

      Language language = null;
      Long languageId = jsonRequestContext.getLong("language");
      if (languageId != null) {
        language = languageDAO.findById(languageId);
      }

      Nationality nationality = null;
      Long nationalityId = jsonRequestContext.getLong("nationality");
      if (nationalityId != null) {
        nationality = nationalityDAO.findById(nationalityId);
      }

      Municipality municipality = null;
      Long municipalityId = jsonRequestContext.getLong("municipality");
      if (municipalityId != null) {
        municipality = municipalityDAO.findById(municipalityId);
      }
      
      StudyProgramme studyProgramme = null;
      Long studyProgrammeId = jsonRequestContext.getLong("studyProgramme");
      if (studyProgrammeId != null) {
        studyProgramme = studyProgrammeDAO.findById(studyProgrammeId);
      }
      
      searchResult = personDAO.searchPersons(resultsPerPage, page, firstName, lastName, nickname,
          tags, education, email, sex, ssn, addressCity, addressCountry, addressPostalCode, addressStreetAddress,
          phone, studyProgramme, language, nationality, municipality, title, personFilter, organization);
    }
    else if ("active".equals(jsonRequestContext.getRequest().getParameter("activeTab"))) {
      String query = jsonRequestContext.getRequest().getParameter("activesQuery");
      searchResult = personDAO.searchPersonsBasic(resultsPerPage, page, query, PersonFilter.ACTIVE_STUDENTS, organization);
    }
    else {
      String query = jsonRequestContext.getRequest().getParameter("query");
      searchResult = personDAO.searchPersonsBasic(resultsPerPage, page, query, PersonFilter.ALL, organization);
    }

    List<Map<String, Object>> results = new ArrayList<>();
    List<Person> persons = searchResult.getResults();
    for (Person person : persons) {
      List<Student> studentList2 = studentDAO.listByPerson(person);
      StaffMember staffMember = staffMemberDAO.findByPerson(person);
      
      if (!studentList2.isEmpty() || staffMember != null) {
        String activeStudyProgrammes = "";
        String inactiveStudyProgrammes = "";
  
        for (Student student1 : studentList2) {
          if (student1.getStudyProgramme() != null && UserUtils.canAccessOrganization(loggedUser, student1.getOrganization())) {
            if (!student1.getHasFinishedStudies()) {
              if (activeStudyProgrammes.length() == 0)
                activeStudyProgrammes = student1.getStudyProgramme().getName();
              else
                activeStudyProgrammes += ", " + student1.getStudyProgramme().getName();
            } else {
              if (inactiveStudyProgrammes.length() == 0)
                inactiveStudyProgrammes = student1.getStudyProgramme().getName();
              else
                inactiveStudyProgrammes += ", " + student1.getStudyProgramme().getName();
            }
          }
        }
  
        String firstName = "";
        String lastName = "";
        Boolean archived = Boolean.FALSE;
        Long id = null;
        
        if (person.getLatestStudent() != null) {
          firstName = person.getLatestStudent().getFirstName();
          lastName = person.getLatestStudent().getLastName();
          archived = person.getLatestStudent().getArchived();
          id = person.getLatestStudent().getId();
        } else {
          if (staffMember != null) {
            firstName = staffMember.getFirstName();
            lastName = staffMember.getLastName();
          }
        }
        
        Map<String, Object> info = new HashMap<>();
        info.put("personId", person.getId());
        info.put("id", id);
        info.put("firstName", firstName);
        info.put("lastName", lastName);
        info.put("archived", archived);
        info.put("activeStudyProgrammes", activeStudyProgrammes);
        info.put("inactiveStudyProgrammes", inactiveStudyProgrammes);
        info.put("active", person.getActive());
        results.add(info);
      }
    }
    
    String statusMessage;
    Locale locale = jsonRequestContext.getRequest().getLocale();
    if (searchResult.getTotalHitCount() > 0) {
      statusMessage = Messages.getInstance().getText(
          locale,
          "students.searchStudentgs.searchStatus",
          new Object[] { searchResult.getFirstResult() + 1, searchResult.getLastResult() + 1,
              searchResult.getTotalHitCount() });
    }
    else {
      statusMessage = Messages.getInstance().getText(locale, "students.searchStudents.searchStatusNoMatches");
    }
    jsonRequestContext.addResponseParameter("results", results);
    jsonRequestContext.addResponseParameter("statusMessage", statusMessage);
    jsonRequestContext.addResponseParameter("pages", searchResult.getPages());
    jsonRequestContext.addResponseParameter("page", searchResult.getPage());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
