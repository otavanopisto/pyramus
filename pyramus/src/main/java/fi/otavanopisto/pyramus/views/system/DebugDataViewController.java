package fi.otavanopisto.pyramus.views.system;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseModuleDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStateDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.dao.resources.MaterialResourceDAO;
import fi.otavanopisto.pyramus.dao.resources.ResourceCategoryDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.resources.ResourceCategory;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class DebugDataViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    ResourceCategoryDAO resourceCategoryDAO = DAOFactory.getInstance().getResourceCategoryDAO();
    MaterialResourceDAO materialResourceDAO = DAOFactory.getInstance().getMaterialResourceDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();
    CourseModuleDAO courseModuleDAO = DAOFactory.getInstance().getCourseModuleDAO();

    String type = requestContext.getRequest().getParameter("type");
    int count = Integer.parseInt(requestContext.getRequest().getParameter("count"));
    int start = 1;
    String s = requestContext.getRequest().getParameter("start");
    if (!StringUtils.isBlank(s)) {
      start = Integer.parseInt(s);
    }

    User user = userDAO.findById(requestContext.getLoggedUserId());
    
    if ("module".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        EducationalTimeUnit etu = educationalTimeUnitDAO.findById(1L);
        Module module = moduleDAO.create("Moduli " + i, "Kuvaustekstiä modulille " + i, null, user);
        courseModuleDAO.create(module, null, null, 10d, etu);
      }
    }
    else if ("course".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        EducationalTimeUnit etu = educationalTimeUnitDAO.findById(1L);
        CourseState courseState = courseStateDAO.findById(1L);
        Organization organization = organizationDAO.findById(1L);
        Course course = courseDAO.create(moduleDAO.findById(1L), organization, "Kurssi " + i, "", courseState, null, null, null, null, null, null, null, null, null, null, null, "Kuvaustekstiä kurssille " + i, null, null, null, null, user);
        courseModuleDAO.create(course, null, null, 10d, etu);
      }
    }
    else if ("resource".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        ResourceCategory resourceCategory = resourceCategoryDAO.findById(1L);
        materialResourceDAO.create("Materiaaliresurssi " + i, resourceCategory, 500d);
      }
    }
    else if ("project".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        EducationalTimeUnit etu = educationalTimeUnitDAO.findById(1L);
        projectDAO.create("Projekti " + i, "Kuvaustekstiä projektille " + i, 10d, etu, user);
      }
    }
    else if ("student".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        Person person = personDAO.create(new Date(), "030310-123R", Sex.MALE, null, Boolean.FALSE);
        studentDAO.create(person, "Etunimi " + i, "Sukunimi " + i, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false);
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
