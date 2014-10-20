package fi.pyramus.json.courses;

import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.CourseEducationSubtypeDAO;
import fi.pyramus.dao.base.CourseEducationTypeDAO;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.base.EducationSubtypeDAO;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.courses.BasicCourseResourceDAO;
import fi.pyramus.dao.courses.CourseComponentDAO;
import fi.pyramus.dao.courses.CourseComponentResourceDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.pyramus.dao.courses.CourseDescriptionDAO;
import fi.pyramus.dao.courses.CourseEnrolmentTypeDAO;
import fi.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.pyramus.dao.courses.CourseStaffMemberDAO;
import fi.pyramus.dao.courses.CourseStaffMemberRoleDAO;
import fi.pyramus.dao.courses.CourseStateDAO;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.dao.courses.GradeCourseResourceDAO;
import fi.pyramus.dao.courses.OtherCostDAO;
import fi.pyramus.dao.courses.StudentCourseResourceDAO;
import fi.pyramus.dao.resources.ResourceDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.pyramus.domainmodel.base.CourseEducationType;
import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.BasicCourseResource;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseComponentResource;
import fi.pyramus.domainmodel.courses.CourseDescription;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.courses.CourseStaffMember;
import fi.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.pyramus.domainmodel.courses.GradeCourseResource;
import fi.pyramus.domainmodel.courses.OtherCost;
import fi.pyramus.domainmodel.courses.StudentCourseResource;
import fi.pyramus.domainmodel.resources.Resource;
import fi.pyramus.domainmodel.resources.ResourceType;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.persistence.usertypes.MonetaryAmount;

/**
 * The controller responsible of modifying an existing course. 
 * 
 * @see fi.pyramus.views.modules.EditCourseViewController
 */
public class EditCourseJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to edit a course.
   * The request should contain the following parameters:
   * <dl>
   *   <dt><code>name</code></dt>
   *   <dd>The name of the course.</dd>
   *   <dt><code>nameExtension</code></dt>
   *   <dd>The name extension of the course.</dd>
   *   <dt><code>state</code></dt>
   *   <dd>The ID of the initial course state.</dd>
   *   <dt><code>description</code></dt>
   *   <dd>The description of the course, in HTML.</dd>
   *   <dt><code>module</code></dt>
   *   <dd>The ID of the module the course is associated with.</dd>
   *   <dt><code>subject</code></dt>
   *   <dd>The ID of the subject of the course.</dd>
   *   <dt><code>courseNumber</code></dt>
   *   <dd>The course number of the course.</dd>
   *   <dt><code>beginDate</code></dt>
   *   <dd>The beginning date of the course, as a timestamp in ms.</dd>
   *   <dt><code>endDate</code></dt>
   *   <dd>The end date of the course, as a timestamp in ms.</dd>
   *   <dt><code>enrolmentTimeEnd</code></dt>
   *   <dd>The time when enrollment for the course ends, as a timestamp in ms.</dd>
   *   <dt><code>courseLength</code></dt>
   *   <dd>The length of the course, in units specified in <code>courseLengthTimeUnit</code>.</dd>
   *   <dt><code>courseLengthTimeUnit</code></dt>
   *   <dd>The ID of the time unit for the courses.</dd>
   *   <dt><code>maxParticipantCount</code></dt>
   *   <dd>The maximum number of participants for this course.</dd>
   *   <dt><code>distanceTeachingDays</code></dt>
   *   <dd>The number of days of distance teaching.</dd>
   *   <dt><code>localTeachingDays</code></dt>
   *   <dd>The number of days of local teaching.</dd>
   *   <dt><code>teachingHours</code></dt>
   *   <dd>The total number of teaching hours.</dd>
   *   <dt><code>planningHours</code></dt>
   *   <dd>The total number of hours used for planning.
   *   <dt><code>assessingHours</code></dt>
   *   <dd>The total number of hours used for assessing.
   *   <dt><code>tags</code></dt>
   *   <dd>The tags associated with the course, separated by spaces.</dd>
   * </dl>
   * The following are not single but multiple parameters, arranged
   * as collections of objects. Each parameter is defined by the
   * following scheme (zero indexed):<br/>
   * <code>
   * <i>collection name</i>.<i>property name</i>
   * </code>
   * or<br/>
   * <code>
   * <i>collection name</i>.<i>object index</i>.<i>property name</i>
   * </code>
   * <dl>
   *   <dt><code>courseDescription.*.catId</code></dt>
   *   <dd>The course description category ID to add the description to.
   *   <dt><code>courseDescription.*.text</code></dt>
   *   <dd>The description of the course.</dd>
   *   <dt><code>personnelTable.rowCount</code></dt>
   *   <dd>The number of personnel in the course.</dd>
   *   <dt><code>personnelTable.*.userId</code></dt>
   *   <dd>The ID of the user in the personnel table.</dd>
   *   <dt><code>personnelTable.*.roleId</code></dt>
   *   <dd>The role ID of the user in the personnel table.</dd>
   *   <dt><code>components.componentCount</code></dt>
   *   <dd>The number of course components in this course.</dd>
   *   <dt><code>components.*.0.name</code></dt>
   *   <dd>The name of the course component.</dd>
   *   <dt><code>components.*.0.length</code></dt>
   *   <dd>The length of the course component, in default time units.</dd>
   *   <dt><code>components.*.0.description</code></dt>
   *   <dd>The description of the course component.</dd>
   *   <dt><code>components.*.resourceCategoryCount</code></dt>
   *   <dd>The number of resource categories in the course component.</dd>
   *   <dt><code>components.*.*.resources.rowCount</code></dt>
   *   <dd>The number of resources in the resource category.</dd>
   *   <dt><code>components.*.*.resources.*.resourceId</code></dt>
   *   <dd>The ID of the resource.</dd>
   *   <dt><code>components.*.*.resources.*.quantity</code></dt>
   *   <dd>The quantity of the resource.</dd>
   *   <dt><code>components.*.*.resources.*.usage</code></dt>
   *   <dd>The usage of the resource.</dd>
   *   <dt><code>educationType.*.*</code></dt>
   *   <dd>The education types and subtypes (as numerical IDs) of the course.</dd>
   *   <dt><code>basicResourcesTable.rowCount</code></dt>
   *   <dd>The number of basic resources associated with the course.</dd>
   *   <dt><code>basicResourcesTable.*.hours</code></dt>
   *   <dd>The number of hours the resource is used.</dd>
   *   <dt><code>basicResourcesTable.*.hourlyCost</code></dt>
   *   <dd>The hourly cost of the resource.</dd>
   *   <dt><code>basicResourcesTable.*.units</code></dt>
   *   <dd>The number of units of the resource used.</dd>
   *   <dt><code>basicResourcesTable.*.unitCost</code></dt>
   *   <dd>The cost of a single unit of the resource.</dd>
   *   <dt><code>basicResourcesTable.*.resourceId</code></dt>
   *   <dd>The ID of the resource.</dd>
   *   <dt><code>studentResourcesTable.rowCount</code></dt>
   *   <dd>The number of student resources associated with the course.</dd>
   *   <dt><code>studentResourcesTable.*.hours</code></dt>
   *   <dd>The number of hours the resource is used.</dd>
   *   <dt><code>studentResourcesTable.*.hourlyCost</code></dt>
   *   <dd>The hourly cost of the resource.</dd>
   *   <dt><code>studentResourcesTable.*.unitCost</code></dt>
   *   <dd>The cost of a single unit of the resource.</dd>
   *   <dt><code>studentResourcesTable.*.resourceId</code></dt>
   *   <dd>The ID of the resource.</dd>
   *   <dt><code>gradeResourcesTable.rowCount</code></dt>
   *   <dd>The number of grade resources associated with the course.</dd>
   *   <dt><code>gradeResourcesTable.*.hours</code></dt>
   *   <dd>The number of hours the resource is used.</dd>
   *   <dt><code>gradeResourcesTable.*.hourlyCost</code></dt>
   *   <dd>The hourly cost of the resource.</dd>
   *   <dt><code>gradeResourcesTable.*.unitCost</code></dt>
   *   <dd>The cost of a single unit of the resource.</dd>
   *   <dt><code>gradeResourcesTable.*.resourceId</code></dt>
   *   <dd>The ID of the resource.</dd>
   *   <dt><code>otherCostsTable.rowCount</code></dt>
   *   <dd>The number of other costs associated with this course.</dd>
   *   <dt><code>otherCostsTable.*.name</code></dt>
   *   <dd>The name of the cost.</dd>
   *   <dt><code>otherCostsTable.*.count</code></dt>
   *   <dd>The amount of the cost.</dd>
   *   <dt><code>studentsTable.rowCount</code></dt>
   *   <dd>The number of students attending this course.</dd>
   *   <dt><code>studentsTable.*.studentId</code></dt>
   *   <dd>The ID of the student.</dd>
   *   <dt><code>studentsTable.*.enrolmentDate</code></dt>
   *   <dd>The enrollment date of the student, as a timestamp in ms.</dd>
   *   <dt><code>studentsTable.*.enrolmentType</code></dt>
   *   <dd>The ID of the enrollment type of the student.</dd>
   *   <dt><code>studentsTable.*.participationType</code></dt>
   *   <dd>The ID of the participation type of the student.</dd>
   *   <dt><code>studentsTable.*.lodging</code></dt>
   *   <dd><code>true</code> if the student is lodging on campus.</dd>
   *   <dt><code>studentsTable.*.optionality</code></dt>
   *   <dd>The optionality of the course for the student:
   *   <code>MANDATORY</code> or <code>OPTIONAL</code></dd>
   * </dl>
   * 
   * @param requestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    ResourceDAO resourceDAO = DAOFactory.getInstance().getResourceDAO();
    CourseDescriptionDAO courseDescriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    CourseComponentResourceDAO componentResourceDAO = DAOFactory.getInstance().getCourseComponentResourceDAO();
    CourseEnrolmentTypeDAO enrolmentTypeDAO = DAOFactory.getInstance().getCourseEnrolmentTypeDAO();
    CourseComponentDAO componentDAO = DAOFactory.getInstance().getCourseComponentDAO();
    CourseStaffMemberDAO courseStaffMemberDAO = DAOFactory.getInstance().getCourseStaffMemberDAO();
    CourseStaffMemberRoleDAO courseStaffMemberRoleDAO = DAOFactory.getInstance().getCourseStaffMemberRoleDAO();
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    OtherCostDAO otherCostDAO = DAOFactory.getInstance().getOtherCostDAO();
    BasicCourseResourceDAO basicCourseResourceDAO = DAOFactory.getInstance().getBasicCourseResourceDAO();
    StudentCourseResourceDAO studentCourseResourceDAO = DAOFactory.getInstance().getStudentCourseResourceDAO();
    GradeCourseResourceDAO gradeCourseResourceDAO = DAOFactory.getInstance().getGradeCourseResourceDAO();
    CourseEducationTypeDAO courseEducationTypeDAO = DAOFactory.getInstance().getCourseEducationTypeDAO();
    CourseEducationSubtypeDAO courseEducationSubtypeDAO = DAOFactory.getInstance().getCourseEducationSubtypeDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    // Course basic information

    Long courseId = requestContext.getLong("course");
    Course course = courseDAO.findById(courseId);
    String name = requestContext.getString("name");
    String nameExtension = requestContext.getString("nameExtension");
    Long courseStateId = requestContext.getLong("state");
    Long maxParticipantCount = requestContext.getLong("maxParticipantCount");
    Date enrolmentTimeEnd = requestContext.getDate("enrolmentTimeEnd");
    CourseState courseState = courseStateId == null ? course.getState() : courseStateDAO.findById(courseStateId);
    String description = requestContext.getString("description");
    Subject subject = subjectDAO.findById(requestContext.getLong("subject"));
    Integer courseNumber = requestContext.getInteger("courseNumber");
    Date beginDate = requestContext.getDate("beginDate");
    Date endDate = requestContext.getDate("endDate");
    Double courseLength = requestContext.getDouble("courseLength");
    EducationalTimeUnit courseLengthTimeUnit = educationalTimeUnitDAO.findById(requestContext
        .getLong("courseLengthTimeUnit"));
    Double distanceTeachingDays = requestContext.getDouble("distanceTeachingDays");
    Double localTeachingDays = requestContext.getDouble("localTeachingDays");
    Double teachingHours = requestContext.getDouble("teachingHours");
    Double planningHours = requestContext.getDouble("planningHours");
    Double assessingHours = requestContext.getDouble("assessingHours");
    String tagsText = requestContext.getString("tags");
    
    Long version = requestContext.getLong("version");
    if (!course.getVersion().equals(version))
      throw new StaleObjectStateException(Course.class.getName(), course.getId());
    
    Set<Tag> tagEntities = new HashSet<Tag>();
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
    
    User user = userDAO.findById(requestContext.getLoggedUserId());

    courseDAO.update(course, name, nameExtension, courseState, subject, courseNumber, beginDate, endDate,
        courseLength, courseLengthTimeUnit, distanceTeachingDays, localTeachingDays, teachingHours, planningHours, assessingHours, 
        description, maxParticipantCount, enrolmentTimeEnd, user);
    
    // Tags

    courseDAO.setCourseTags(course, tagEntities);
    
    // Education types and subtypes submitted from the web page

    Map<Long, Vector<Long>> chosenEducationTypes = new HashMap<Long, Vector<Long>>();
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

    // Remove education types and subtypes

    List<CourseEducationType> courseEducationTypes = course.getCourseEducationTypes();
    for (int i = courseEducationTypes.size() - 1; i >= 0; i--) {
      CourseEducationType courseEducationType = courseEducationTypes.get(i);
      if (!chosenEducationTypes.containsKey(courseEducationType.getEducationType().getId())) {
        courseEducationTypeDAO.delete(courseEducationType);
      } else {
        Vector<Long> v = chosenEducationTypes.get(courseEducationType.getEducationType().getId());
        List<CourseEducationSubtype> courseEducationSubtypes = courseEducationType.getCourseEducationSubtypes();
        for (int j = courseEducationSubtypes.size() - 1; j >= 0; j--) {
          CourseEducationSubtype courseEducationSubtype = courseEducationSubtypes.get(j);
          if (!v.contains(courseEducationSubtype.getEducationSubtype().getId())) {
            courseEducationType.removeSubtype(courseEducationSubtype);
          }
        }
      }
    }

    // Add education types and subtypes

    for (Long educationTypeId : chosenEducationTypes.keySet()) {
      EducationType educationType = educationTypeDAO.findById(educationTypeId);
      CourseEducationType courseEducationType;
      if (!course.contains(educationType)) {
        courseEducationType = courseEducationTypeDAO.create(course, educationType);
      }
      else {
        courseEducationType = course.getCourseEducationTypeByEducationTypeId(educationTypeId);
      }
      for (Long educationSubtypeId : chosenEducationTypes.get(educationTypeId)) {
        EducationSubtype educationSubtype = educationSubtypeDAO.findById(educationSubtypeId);
        if (!courseEducationType.contains(educationSubtype)) {
          courseEducationSubtypeDAO.create(courseEducationType, educationSubtype);
        }
      }
    }

    // Course Descriptions
    
    List<CourseDescriptionCategory> descriptionCategories = descriptionCategoryDAO.listUnarchived();
    Set<CourseDescription> nonExistingDescriptions = new HashSet<CourseDescription>();
    
    for (CourseDescriptionCategory cat: descriptionCategories) {
      String varName = "courseDescription." + cat.getId().toString();
      Long descriptionCatId = requestContext.getLong(varName + ".catId");
      String descriptionText = requestContext.getString(varName + ".text");

      CourseDescription oldDesc = descriptionDAO.findByCourseAndCategory(course, cat);

      if ((descriptionCatId != null) && (descriptionCatId.intValue() != -1)) {
        // Description has been submitted from form 
        if (oldDesc != null)
          descriptionDAO.update(oldDesc, course, cat, descriptionText);
        else
          descriptionDAO.create(course, cat, descriptionText);
      } else {
        // Description wasn't submitted from form, if it exists, it's marked for deletion 
        if (oldDesc != null)
          nonExistingDescriptions.add(oldDesc);
      }
    }
    
    // Delete non existing descriptions
    for (CourseDescription desc: nonExistingDescriptions) {
      courseDescriptionDAO.delete(desc);
    }
    
    // Personnel

    Set<Long> existingIds = new HashSet<Long>();
    int rowCount = requestContext.getInteger("personnelTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "personnelTable." + i;
      Long courseUserId = requestContext.getLong(colPrefix + ".courseUserId");
      Long userId = requestContext.getLong(colPrefix + ".userId");
      Long roleId = requestContext.getLong(colPrefix + ".roleId");
      user = userDAO.findById(userId);
      CourseStaffMemberRole role = courseStaffMemberRoleDAO.findById(roleId);
      if (courseUserId == -1) {
        courseUserId = courseStaffMemberDAO.create(course, user, role).getId();
      } else {
        courseStaffMemberDAO.updateRole(courseStaffMemberDAO.findById(courseUserId), role);
      }
      
      existingIds.add(courseUserId);
    }
    List<CourseStaffMember> courseUsers = courseStaffMemberDAO.listByCourse(courseDAO.findById(courseId));
    for (CourseStaffMember courseUser : courseUsers) {
      if (!existingIds.contains(courseUser.getId())) {
        courseStaffMemberDAO.delete(courseUser);
      }
    }

    // Course components

    rowCount = requestContext.getInteger("components.componentCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "components.component." + i;
      Long componentId = requestContext.getLong(colPrefix + ".0.componentId");
      String componentName = requestContext.getString(colPrefix + ".0.name");
      Double componentLength = requestContext.getDouble(colPrefix + ".0.length");
      String componentDescription = requestContext.getString(colPrefix + ".0.description");

      // TODO Component length; should be just hours but it currently depends on the default time unit - ok?  
      EducationalTimeUnit componentTimeUnit = defaultsDAO.getDefaults().getBaseTimeUnit();

      CourseComponent courseComponent;
      
      if (componentId == -1) {
        courseComponent = componentDAO.create(course, componentLength, componentTimeUnit, componentName,
            componentDescription);
      } else {
        courseComponent = componentDAO.update(componentDAO.findById(componentId), componentLength, componentTimeUnit,
            componentName, componentDescription);
      }
                        
      Long resourceCategoryCount = requestContext.getLong(colPrefix + ".resourceCategoryCount");
      for (int categoryIndex = 0; categoryIndex < resourceCategoryCount; categoryIndex++) {
        String resourcesPrefix = colPrefix + "." + categoryIndex + ".resources";
        Long resourcesCount = requestContext.getLong(resourcesPrefix + ".rowCount");
        
        for (int j = 0; j < resourcesCount; j++) {
          String resourcePrefix = resourcesPrefix + "." + j;
          
          Long id = requestContext.getLong(resourcePrefix + ".id");
          Long resourceId = requestContext.getLong(resourcePrefix + ".resourceId");
          Resource resource = resourceDAO.findById(resourceId);
          Double usagePercent;
          
          if (resource.getResourceType() == ResourceType.MATERIAL_RESOURCE) {
            usagePercent = requestContext.getDouble(resourcePrefix + ".quantity") * 100;
          } else {
            usagePercent = requestContext.getDouble(resourcePrefix + ".usage");
          }
          
          if (id == -1) {
            componentResourceDAO.create(courseComponent, resource, usagePercent);
          } else {
            CourseComponentResource courseComponentResource = componentResourceDAO.findById(id);            
            componentResourceDAO.updateUsagePercent(courseComponentResource, usagePercent);
          }
        }
      }
    }
    
    // Basic course resources

    existingIds = new HashSet<Long>();
    rowCount = requestContext.getInteger("basicResourcesTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "basicResourcesTable." + i;
      Double hours = requestContext.getDouble(colPrefix + ".hours");
      if (hours == null) {
        hours = 0.0;
      }
      MonetaryAmount hourlyCost = new MonetaryAmount();
      hourlyCost.setAmount(requestContext.getDouble(colPrefix + ".hourlyCost"));
      Integer units = requestContext.getInteger(colPrefix + ".units");
      MonetaryAmount unitCost = new MonetaryAmount();
      unitCost.setAmount(requestContext.getDouble(colPrefix + ".unitCost"));
      Long resourceId = requestContext.getLong(colPrefix + ".resourceId");
      Resource resource = resourceDAO.findById(resourceId);
      Long basicResourceId = requestContext.getLong(colPrefix + ".basicResourceId");
      if (basicResourceId == -1) {
        basicResourceId = basicCourseResourceDAO.create(course, resource, hours, hourlyCost, units, unitCost)
            .getId();
      }
      else {
        basicCourseResourceDAO.update(basicCourseResourceDAO.findById(basicResourceId), hours, hourlyCost,
            units, unitCost);
      }
      existingIds.add(basicResourceId);
    }
    List<BasicCourseResource> basicCourseResources = basicCourseResourceDAO.listByCourse(course);
    for (BasicCourseResource basicCourseResource : basicCourseResources) {
      if (!existingIds.contains(basicCourseResource.getId())) {
        basicCourseResourceDAO.delete(basicCourseResource);
      }
    }

    // Student course resources

    existingIds = new HashSet<Long>();
    rowCount = requestContext.getInteger("studentResourcesTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studentResourcesTable." + i;
      Double hours = requestContext.getDouble(colPrefix + ".hours");
      if (hours == null) {
        hours = 0.0;
      }
      MonetaryAmount hourlyCost = new MonetaryAmount();
      hourlyCost.setAmount(requestContext.getDouble(colPrefix + ".hourlyCost"));
      MonetaryAmount unitCost = new MonetaryAmount();
      hourlyCost.setAmount(requestContext.getDouble(colPrefix + ".unitCost"));
      Long resourceId = requestContext.getLong(colPrefix + ".resourceId");
      Resource resource = resourceDAO.findById(resourceId);
      Long studentResourceId = requestContext.getLong(colPrefix + ".studentResourceId");
      if (studentResourceId == -1) {
        studentResourceId = studentCourseResourceDAO.create(course, resource, hours, hourlyCost, unitCost)
            .getId();
      }
      else {
        studentCourseResourceDAO.update(studentCourseResourceDAO.findById(studentResourceId), hours, hourlyCost,
            unitCost);
      }
      existingIds.add(studentResourceId);
    }
    List<StudentCourseResource> studentCourseResources = studentCourseResourceDAO.listByCourse(course);
    for (StudentCourseResource studentCourseResource : studentCourseResources) {
      if (!existingIds.contains(studentCourseResource.getId())) {
        studentCourseResourceDAO.delete(studentCourseResource);
      }
    }

    // Grade course resources

    existingIds = new HashSet<Long>();
    rowCount = requestContext.getInteger("gradeResourcesTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "gradeResourcesTable." + i;
      Double hours = requestContext.getDouble(colPrefix + ".hours");
      if (hours == null) {
        hours = 0.0;
      }
      MonetaryAmount hourlyCost = new MonetaryAmount();
      hourlyCost.setAmount(requestContext.getDouble(colPrefix + ".hourlyCost"));
      MonetaryAmount unitCost = new MonetaryAmount();
      unitCost.setAmount(requestContext.getDouble(colPrefix + ".unitCost"));
      Long resourceId = requestContext.getLong(colPrefix + ".resourceId");
      Resource resource = resourceDAO.findById(resourceId);
      Long gradeResourceId = requestContext.getLong(colPrefix + ".gradeResourceId");
      if (gradeResourceId == -1) {
        gradeResourceId = gradeCourseResourceDAO.create(course, resource, hours, hourlyCost, unitCost).getId();
      }
      else {
        gradeCourseResourceDAO.update(gradeCourseResourceDAO.findById(gradeResourceId), hours, hourlyCost,
            unitCost);
      }
      existingIds.add(gradeResourceId);
    }
    List<GradeCourseResource> gradeCourseResources = gradeCourseResourceDAO.listByCourse(course);
    for (GradeCourseResource gradeCourseResource : gradeCourseResources) {
      if (!existingIds.contains(gradeCourseResource.getId())) {
        gradeCourseResourceDAO.delete(gradeCourseResource);
      }
    }

    // Other costs

    existingIds = new HashSet<Long>();
    rowCount = requestContext.getInteger("otherCostsTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "otherCostsTable." + i;
      name = requestContext.getString(colPrefix + ".name");
      MonetaryAmount cost = new MonetaryAmount();
      cost.setAmount(requestContext.getDouble(colPrefix + ".cost"));
      Long otherCostId = requestContext.getLong(colPrefix + ".otherCostId");
      if (otherCostId == -1) {
        otherCostId = otherCostDAO.create(course, name, cost).getId();
      }
      else {
        otherCostDAO.update(otherCostDAO.findById(otherCostId), name, cost);
      }
      existingIds.add(otherCostId);
    }
    List<OtherCost> otherCosts = otherCostDAO.listByCourse(course);
    for (OtherCost otherCost : otherCosts) {
      if (!existingIds.contains(otherCost.getId())) {
        otherCostDAO.delete(otherCost);
      }
    }

    // Students

    existingIds = new HashSet<Long>();
    rowCount = requestContext.getInteger("studentsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studentsTable." + i;

      Long studentId = requestContext.getLong(colPrefix + ".studentId");
      Long courseStudentId = requestContext.getLong(colPrefix + ".courseStudentId");
      Date enrolmentDate = requestContext.getDate(colPrefix + ".enrolmentDate");
      Long enrolmentTypeId = requestContext.getLong(colPrefix + ".enrolmentType");
      Long participationTypeId = requestContext.getLong(colPrefix + ".participationType");
      Boolean lodging = requestContext.getBoolean(colPrefix + ".lodging");
      
      CourseEnrolmentType enrolmentType = enrolmentTypeId != null ? enrolmentTypeDAO.findById(enrolmentTypeId) : null;
      CourseParticipationType participationType = participationTypeId != null ? participationTypeDAO.findById(participationTypeId) : null;
      CourseStudent courseStudent;
      CourseOptionality optionality = (CourseOptionality) requestContext.getEnum(colPrefix + ".optionality", CourseOptionality.class);

      if (courseStudentId == -1) {
        /* New student */
        Student student = studentDAO.findById(studentId);
        courseStudent = courseStudentDAO.create(course, student, enrolmentType, participationType, enrolmentDate, lodging, optionality, null, Boolean.FALSE);
      }
      else {
        boolean modified = new Integer(1).equals(requestContext.getInteger(colPrefix + ".modified"));
        if (modified) {
          /* Existing student */
          Student student = studentDAO.findById(studentId);
          courseStudent = courseStudentDAO.findById(courseStudentId);
          courseStudentDAO.update(courseStudent, student, enrolmentType, participationType, enrolmentDate, lodging, optionality);
        }
      }
    }
    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
