package fi.pyramus.json.students;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.LanguageDAO;
import fi.pyramus.dao.base.MunicipalityDAO;
import fi.pyramus.dao.base.NationalityDAO;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.dao.students.AbstractStudentDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.persistence.search.SearchResult;
import fi.pyramus.persistence.search.StudentFilter;

/**
 * Request handler for searching students.
 * 
 * @author antti.viljakainen
 */
public class SearchStudentsJSONRequestContoller extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();

    Integer resultsPerPage = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("maxResults"));
    if (resultsPerPage == null) {
      resultsPerPage = 10;
    }

    Integer page = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("page"));
    if (page == null) {
      page = 0;
    }
    
    SearchResult<AbstractStudent> searchResult = null;
    
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
      Integer lodgingInt = jsonRequestContext.getInteger("lodging");
      Boolean lodging = lodgingInt == null ? null : lodgingInt == 1;
      StudentFilter studentFilter = (StudentFilter) jsonRequestContext.getEnum("studentFilter", StudentFilter.class);

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
      
      searchResult = abstractStudentDAO.searchAbstractStudents(resultsPerPage, page, firstName, lastName, nickname,
          tags, education, email, sex, ssn, addressCity, addressCountry, addressPostalCode, addressStreetAddress,
          phone, lodging, studyProgramme, language, nationality, municipality, studentFilter);
    }
    else if ("active".equals(jsonRequestContext.getRequest().getParameter("activeTab"))) {
      String query = jsonRequestContext.getRequest().getParameter("activesQuery");
      searchResult = abstractStudentDAO.searchAbstractStudentsBasic(resultsPerPage, page, query, StudentFilter.SKIP_INACTIVE);
    }
    else {
      String query = jsonRequestContext.getRequest().getParameter("query");
      searchResult = abstractStudentDAO.searchAbstractStudentsBasic(resultsPerPage, page, query, StudentFilter.INCLUDE_INACTIVE);
    }

    List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
    List<AbstractStudent> abstractStudents = searchResult.getResults();
    for (AbstractStudent abstractStudent : abstractStudents) {
    	Student student = abstractStudent.getLatestStudent();
    	if (student != null) {
    	  String activeStudyProgrammes = "";
    	  String inactiveStudyProgrammes = "";

    	  List<Student> studentList2 = studentDAO.listByAbstractStudent(abstractStudent);
    	  
    	  for (Student student1 : studentList2) {
    	    if (student1.getStudyProgramme() != null) {
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
    	  
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("abstractStudentId", abstractStudent.getId());
        info.put("id", abstractStudent.getLatestStudent().getId());
        info.put("firstName", student.getFirstName());
        info.put("lastName", student.getLastName());
        info.put("archived", student.getArchived());
        info.put("activeStudyProgrammes", activeStudyProgrammes);
        info.put("inactiveStudyProgrammes", inactiveStudyProgrammes);
        info.put("active", abstractStudent.getActive());
        results.add(info);
    	}
    }
    
    String statusMessage = "";
    Locale locale = jsonRequestContext.getRequest().getLocale();
    if (searchResult.getTotalHitCount() > 0) {
      statusMessage = Messages.getInstance().getText(
          locale,
          "students.searchStudents.searchStatus",
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
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
