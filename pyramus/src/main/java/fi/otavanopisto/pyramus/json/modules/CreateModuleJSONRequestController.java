package fi.otavanopisto.pyramus.json.modules;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CourseEducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.CourseEducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.base.EducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseModuleDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleComponentDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.ixtable.PyramusIxTableFacade;
import fi.otavanopisto.pyramus.util.ixtable.PyramusIxTableRowFacade;

/**
 * The controller responsible of creating a new module.
 * 
 * @see fi.otavanopisto.pyramus.views.users.EditUserViewController
 */
public class CreateModuleJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to create a module.
   * 
   * @param requestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    CourseEducationTypeDAO courseEducationTypeDAO = DAOFactory.getInstance().getCourseEducationTypeDAO();
    CourseEducationSubtypeDAO courseEducationSubtypeDAO = DAOFactory.getInstance().getCourseEducationSubtypeDAO();
    ModuleComponentDAO moduleComponentDAO = DAOFactory.getInstance().getModuleComponentDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();    
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
    CourseModuleDAO courseModuleDAO = DAOFactory.getInstance().getCourseModuleDAO();    

    String name = requestContext.getString("name");
    String description = requestContext.getString("description");
    User loggedUser = userDAO.findById(requestContext.getLoggedUserId());
    Long maxParticipantCount = requestContext.getLong("maxParticipantCount");
    String tagsText = requestContext.getString("tags");

    List<Curriculum> allCurriculums = curriculumDAO.listUnarchived();
    Set<Curriculum> curriculums = new HashSet<>();
    for (Curriculum curriculum : allCurriculums) {
      if ("1".equals(requestContext.getString("curriculum." + curriculum.getId()))) {
        curriculums.add(curriculum);
      }
    }
    
    Set<Tag> tagEntities = new HashSet<>();
    if (!StringUtils.isBlank(tagsText)) {
      List<String> tags = Arrays.asList(tagsText.split("[\\ ,]"));
      for (String tag : tags) {
        if (!StringUtils.isBlank(tag)) {
          Tag tagEntity = tagDAO.findByText(tag.trim());
          if (tagEntity == null)
            tagEntity = tagDAO.create(tag);
          tagEntities.add(tagEntity);
        }
      }
    }
    
    Module module = moduleDAO.create(name, description, maxParticipantCount, loggedUser);

    PyramusIxTableFacade courseModulesTable = PyramusIxTableFacade.from(requestContext, "courseModulesTable");
    for (PyramusIxTableRowFacade courseModulesTableRow : courseModulesTable.rows()) {
      Subject subject = subjectDAO.findById(courseModulesTableRow.getLong("subject"));
      Integer courseNumber = courseModulesTableRow.getInteger("courseNumber");
      Double courseLength = courseModulesTableRow.getDouble("courseLength");
      EducationalTimeUnit courseLengthTimeUnit = educationalTimeUnitDAO.findById(courseModulesTableRow.getLong("courseLengthTimeUnit"));

      courseModuleDAO.create(module, subject, courseNumber, courseLength, courseLengthTimeUnit);
    }

    moduleDAO.updateCurriculums(module, curriculums);
    
    // Tags
    
    moduleDAO.updateTags(module, tagEntities);
    
    // Course Descriptions
    
    List<CourseDescriptionCategory> descriptionCategories = descriptionCategoryDAO.listUnarchived();
    
    for (CourseDescriptionCategory cat: descriptionCategories) {
      String varName = "courseDescription." + cat.getId().toString();
      Long descriptionCatId = requestContext.getLong(varName + ".catId");
      String descriptionText = requestContext.getString(varName + ".text");

      if (descriptionCatId != null && descriptionCatId.intValue() != -1) {
        descriptionDAO.create(module, cat, descriptionText);
      }
    }
    
    // Module components

    int rowCount = requestContext.getInteger("componentsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "componentsTable." + i;
      String componentName = requestContext.getString(colPrefix + ".name");
      Double componentLength = requestContext.getDouble(colPrefix + ".length");
      String componentDescription = requestContext.getString(colPrefix + ".description");
      // TODO Component length; should be just hours but it currently depends on the default time unit - ok?  
      EducationalTimeUnit componentTimeUnit = defaultsDAO.getDefaults().getBaseTimeUnit();
      moduleComponentDAO.create(module, componentLength, componentTimeUnit, componentName, componentDescription)
          .getId();
    }

    // Education types and subtypes submitted from the web page

    Map<Long, Vector<Long>> chosenEducationTypes = new HashMap<>();
    Enumeration<String> parameterNames = requestContext.getRequest().getParameterNames();
    while (parameterNames.hasMoreElements()) {
      name = (String) parameterNames.nextElement();
      if (name.startsWith("educationType.")) {
        String[] nameElements = name.split("\\.");
        Long educationTypeId = new Long(nameElements[1]);
        Long educationSubtypeId = new Long(nameElements[2]);
        Vector<Long> v = chosenEducationTypes.containsKey(educationTypeId) ? chosenEducationTypes.get(educationTypeId)
            : new Vector<Long>();
        v.add(educationSubtypeId);
        if (!chosenEducationTypes.containsKey(educationTypeId)) {
          chosenEducationTypes.put(educationTypeId, v);
        }
      }
    }

    // Add education types and subtypes

    for (Map.Entry<Long, Vector<Long>> entry : chosenEducationTypes.entrySet()) {
      EducationType educationType = educationTypeDAO.findById(entry.getKey());
      CourseEducationType courseEducationType;
      if (!module.contains(educationType)) {
        courseEducationType = courseEducationTypeDAO.create(module, educationType);
      }
      else {
        courseEducationType = module.getCourseEducationTypeByEducationTypeId(entry.getKey());
      }
      for (Long educationSubtypeId : entry.getValue()) {
        EducationSubtype educationSubtype = educationSubtypeDAO.findById(educationSubtypeId);

        if (!courseEducationType.contains(educationSubtype)) {
          courseEducationSubtypeDAO.create(courseEducationType, educationSubtype);
        }
      }
    }

    String redirectURL = requestContext.getRequest().getContextPath() + "/modules/editmodule.page?module=" + module.getId();
    String refererAnchor = requestContext.getRefererAnchor();
    
    if (!StringUtils.isBlank(refererAnchor))
      redirectURL += "#" + refererAnchor;
        
    requestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
