package fi.otavanopisto.pyramus.views.system;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStateDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.dao.resources.MaterialResourceDAO;
import fi.otavanopisto.pyramus.dao.resources.ResourceCategoryDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;
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
        EducationalTimeUnit etu = educationalTimeUnitDAO.findById(new Long(1));
        moduleDAO.create("Moduli " + i, null, null, new Double(10), etu, "Kuvaustekstiä modulille " + i, null, user);
      }
    }
    else if ("course".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        EducationalTimeUnit etu = educationalTimeUnitDAO.findById(new Long(1));
        CourseState courseState = courseStateDAO.findById(new Long(1));
        courseDAO.create(moduleDAO.findById(new Long(1)), "Kurssi " + i, "", courseState, null, null, null, null, null, new Double(10), etu, null, null, null, null, null, null, "Kuvaustekstiä kurssille " + i, null, null, user);
      }
    }
    else if ("resource".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        ResourceCategory resourceCategory = resourceCategoryDAO.findById(new Long(1));
        materialResourceDAO.create("Materiaaliresurssi " + i, resourceCategory, new Double(500));
      }
    }
    else if ("project".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        EducationalTimeUnit etu = educationalTimeUnitDAO.findById(new Long(1));
        projectDAO.create("Projekti " + i, "Kuvaustekstiä projektille " + i, new Double(10), etu, user);
      }
    }
    else if ("student".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        Person person = personDAO.create(new Date(), "030310-123R", Sex.MALE, null, Boolean.FALSE);
        studentDAO.create(person, "Etunimi " + i, "Sukunimi " + i, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, Boolean.FALSE, false);
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
