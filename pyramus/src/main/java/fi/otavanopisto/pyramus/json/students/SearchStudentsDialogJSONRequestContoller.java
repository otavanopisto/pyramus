package fi.otavanopisto.pyramus.json.students;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
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
public class SearchStudentsDialogJSONRequestContoller extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
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
    
    String query = jsonRequestContext.getRequest().getParameter("query");
    PersonFilter personFilter = (PersonFilter) jsonRequestContext.getEnum("studentFilter", PersonFilter.class);
    StudyProgramme studyProgramme = null;
    StudentGroup studentGroup = null;
    
    Long studyProgrammeId = jsonRequestContext.getLong("studyProgrammeId");
    Long studentGroupId = jsonRequestContext.getLong("studentGroupId");
    
    if (studyProgrammeId.intValue() != -1)
      studyProgramme = studyProgrammeDAO.findById(studyProgrammeId);
    if (studentGroupId.intValue() != -1)
      studentGroup = studentGroupDAO.findById(studentGroupId);

    searchResult = personDAO.searchPersonsBasic(resultsPerPage, page, query, personFilter, studyProgramme, studentGroup, organization, loggedUser.getStudyProgrammes());
    
    List<Map<String, Object>> results = new ArrayList<>();
    List<Person> persons = searchResult.getResults();
    for (Person person : persons) {
      Student student = person.getLatestStudent();
      if (student != null) {
        Map<String, Object> info = new HashMap<>();
        info.put("personId", person.getId());
        info.put("id", student.getId());
        info.put("firstName", student.getFirstName());
        info.put("lastName", student.getLastName());
        info.put("archived", student.getArchived());
        results.add(info);
      }
    }
    
    String statusMessage;
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
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
