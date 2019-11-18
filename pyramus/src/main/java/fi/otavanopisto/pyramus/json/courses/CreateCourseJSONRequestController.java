package fi.otavanopisto.pyramus.json.courses;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CourseEducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.CourseEducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.base.EducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.courses.BasicCourseResourceDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseComponentDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseComponentResourceDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseEnrolmentTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberRoleDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStateDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.GradeCourseResourceDAO;
import fi.otavanopisto.pyramus.dao.courses.OtherCostDAO;
import fi.otavanopisto.pyramus.dao.courses.StudentCourseResourceDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.dao.resources.ResourceDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.accommodation.Room;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseComponent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseType;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.resources.Resource;
import fi.otavanopisto.pyramus.domainmodel.resources.ResourceType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.exception.DuplicateCourseStudentException;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.persistence.usertypes.MonetaryAmount;

/** A JSON request controller responsible for creating courses.
 *
 */
public class CreateCourseJSONRequestController extends JSONRequestController {

  /** Processes a JSON request.
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
   * @param binaryRequestContext The context of the binary request.
   */
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    ResourceDAO resourceDAO = DAOFactory.getInstance().getResourceDAO();
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    CourseTypeDAO courseTypeDAO = DAOFactory.getInstance().getCourseTypeDAO();
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    CourseEnrolmentTypeDAO enrolmentTypeDAO = DAOFactory.getInstance().getCourseEnrolmentTypeDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    CourseStaffMemberDAO courseStaffMemberDAO = DAOFactory.getInstance().getCourseStaffMemberDAO();
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    CourseComponentDAO componentDAO = DAOFactory.getInstance().getCourseComponentDAO();
    CourseComponentResourceDAO componentResourceDAO = DAOFactory.getInstance().getCourseComponentResourceDAO();
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
    CourseStaffMemberRoleDAO courseStaffMemberRoleDAO = DAOFactory.getInstance().getCourseStaffMemberRoleDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();

    User loggedUser = staffMemberDAO.findById(requestContext.getLoggedUserId());
    Organization organization = organizationDAO.findById(requestContext.getLong("organizationId"));
    if (!UserUtils.canAccessOrganization(loggedUser, organization)) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Invalid organization.");
    }
    
    // Course basic information

    String name = requestContext.getString("name");
    String nameExtension = requestContext.getString("nameExtension");
    Long courseStateId = requestContext.getLong("state");
    Long courseTypeId = requestContext.getLong("type");
    CourseState courseState = courseStateDAO.findById(courseStateId);
    CourseType courseType = courseTypeId != null ? courseTypeDAO.findById(courseTypeId) : null;
    String description = requestContext.getString("description");
    Module module = moduleDAO.findById(requestContext.getLong("module"));
    Subject subject = subjectDAO.findById(requestContext.getLong("subject"));
    Integer courseNumber = requestContext.getInteger("courseNumber");
    Date beginDate = requestContext.getDate("beginDate");
    Date endDate = requestContext.getDate("endDate");
    Date enrolmentTimeEnd = requestContext.getDate("enrolmentTimeEnd");
    Double courseLength = requestContext.getDouble("courseLength");
    Long courseLengthTimeUnitId = requestContext.getLong("courseLengthTimeUnit");
    Long maxParticipantCount = requestContext.getLong("maxParticipantCount");
    EducationalTimeUnit courseLengthTimeUnit = educationalTimeUnitDAO.findById(courseLengthTimeUnitId);
    Double distanceTeachingDays = requestContext.getDouble("distanceTeachingDays");
    Double localTeachingDays = requestContext.getDouble("localTeachingDays");
    Double teachingHours = requestContext.getDouble("teachingHours");
    Double distanceTeachingHours = requestContext.getDouble("distanceTeachingHours");
    Double planningHours = requestContext.getDouble("planningHours");
    Double assessingHours = requestContext.getDouble("assessingHours");
    String tagsText = requestContext.getString("tags");
    BigDecimal courseFee = requestContext.getBigDecimal("courseFee");
    Currency courseFeeCurrency = requestContext.getCurrency("courseFeeCurrency");
    
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
    
    Course course = courseDAO.create(module, organization, name, nameExtension, courseState, courseType, subject, courseNumber, beginDate, endDate,
        courseLength, courseLengthTimeUnit, distanceTeachingDays, localTeachingDays, teachingHours, distanceTeachingHours, planningHours, 
        assessingHours, description, maxParticipantCount, courseFee, courseFeeCurrency, enrolmentTimeEnd, loggedUser);

    // Curriculums
    
    courseDAO.updateCurriculums(course, curriculums);
    
    // Tags
    
    courseDAO.setCourseTags(course, tagEntities);
    
    // Course Descriptions
    
    List<CourseDescriptionCategory> descriptionCategories = descriptionCategoryDAO.listUnarchived();
    
    for (CourseDescriptionCategory cat: descriptionCategories) {
      String varName = "courseDescription." + cat.getId().toString();
      Long descriptionCatId = requestContext.getLong(varName + ".catId");
      String descriptionText = requestContext.getString(varName + ".text");

      if (descriptionCatId != null && descriptionCatId.intValue() != -1) {
        // Description has been submitted from form 
        descriptionDAO.create(course, cat, descriptionText);
      }
    }
    
    // Personnel

    int rowCount = requestContext.getInteger("personnelTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "personnelTable." + i;
      Long userId = requestContext.getLong(colPrefix + ".userId");
      Long roleId = requestContext.getLong(colPrefix + ".roleId");
      StaffMember staffMember = userDAO.findById(userId);
      CourseStaffMemberRole role = courseStaffMemberRoleDAO.findById(roleId);
      courseStaffMemberDAO.create(course, staffMember, role);
    }
    
    // Course components

    rowCount = requestContext.getInteger("components.componentCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "components.component." + i;
      String componentName = requestContext.getString(colPrefix + ".0.name");
      Double componentLength = requestContext.getDouble(colPrefix + ".0.length");
      String componentDescription = requestContext.getString(colPrefix + ".0.description");

      // TODO Component length; should be just hours but it currently depends on the default time unit - ok?  
      EducationalTimeUnit componentTimeUnit = defaultsDAO.getDefaults().getBaseTimeUnit();

      CourseComponent courseComponent = componentDAO.create(course, componentLength, componentTimeUnit, componentName,
          componentDescription);
                        
      Long resourceCategoryCount = requestContext.getLong(colPrefix + ".resourceCategoryCount");
      for (int categoryIndex = 0; categoryIndex < resourceCategoryCount; categoryIndex++) {
        String resourcesPrefix = colPrefix + "." + categoryIndex + ".resources";
        Long resourcesCount = requestContext.getLong(resourcesPrefix + ".rowCount");
        
        for (int j = 0; j < resourcesCount; j++) {
          String resourcePrefix = resourcesPrefix + "." + j;
          
          Long resourceId = requestContext.getLong(resourcePrefix + ".resourceId");
          Resource resource = resourceDAO.findById(resourceId);
          Double usagePercent;
          
          if (resource.getResourceType() == ResourceType.MATERIAL_RESOURCE) {
            usagePercent = requestContext.getDouble(resourcePrefix + ".quantity") * 100;
          } else {
            usagePercent = requestContext.getDouble(resourcePrefix + ".usage");
          }
          
          componentResourceDAO.create(courseComponent, resource, usagePercent);
        }
      }
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
        Vector<Long> v = chosenEducationTypes.containsKey(educationTypeId) ? chosenEducationTypes.get(educationTypeId) : new Vector<Long>();
        v.add(educationSubtypeId);
        if (!chosenEducationTypes.containsKey(educationTypeId)) {
          chosenEducationTypes.put(educationTypeId, v);
        }
      }
    }
    for (Map.Entry<Long, Vector<Long>> entry : chosenEducationTypes.entrySet()) {
      EducationType educationType = educationTypeDAO.findById(entry.getKey());
      CourseEducationType courseEducationType;
      if (!course.contains(educationType)) {
        courseEducationType = courseEducationTypeDAO.create(course, educationType);
      }
      else {
        courseEducationType = course.getCourseEducationTypeByEducationTypeId(entry.getKey());
      }
      for (Long educationSubtypeId : entry.getValue()) {
        EducationSubtype educationSubtype = educationSubtypeDAO.findById(educationSubtypeId);
        if (!courseEducationType.contains(educationSubtype)) {
          courseEducationSubtypeDAO.create(courseEducationType, educationSubtype);
        }
      }
    }

    // Basic course resources

    int basicResourcesTableRowCount = requestContext.getInteger("basicResourcesTable.rowCount");
    for (int i = 0; i < basicResourcesTableRowCount; i++) {
      String colPrefix = "basicResourcesTable." + i;
      Double hours = requestContext.getDouble(colPrefix + ".hours");
      if (hours == null) {
        hours = 0.0;
      }
      MonetaryAmount hourlyCost = new MonetaryAmount();
      hourlyCost.setAmount(requestContext.getDouble(colPrefix + ".hourlyCost"));
      Integer units = requestContext.getInteger(colPrefix + ".units");
      if (units == null) {
        units = 0;
      }
      MonetaryAmount unitCost = new MonetaryAmount();
      unitCost.setAmount(requestContext.getDouble(colPrefix + ".unitCost"));
      Long resourceId = requestContext.getLong(colPrefix + ".resourceId");
      Resource resource = resourceDAO.findById(resourceId);
      basicCourseResourceDAO.create(course, resource, hours, hourlyCost, units, unitCost);
    }

    // Student course resources

    int studentResourcesTableRowCount = requestContext.getInteger("studentResourcesTable.rowCount");
    for (int i = 0; i < studentResourcesTableRowCount; i++) {
      String colPrefix = "studentResourcesTable." + i;
      Double hours = requestContext.getDouble(colPrefix + ".hours");
      if (hours == null) {
        hours = 0.0;
      }
      MonetaryAmount hourlyCost = new MonetaryAmount();
      hourlyCost.setAmount(requestContext.getDouble(colPrefix + ".hourlyCost"));
      MonetaryAmount unitCost = new MonetaryAmount();
      unitCost.setAmount(requestContext.getDouble(colPrefix + ".unitCost"));
      Long resourceId = NumberUtils.createLong(requestContext.getRequest().getParameter(colPrefix + ".resourceId"));
      Resource resource = resourceDAO.findById(resourceId);
      studentCourseResourceDAO.create(course, resource, hours, hourlyCost, unitCost);
    }

    // Grade course resources

    int gradeResourcesTableRowCount = requestContext.getInteger("gradeResourcesTable.rowCount");
    for (int i = 0; i < gradeResourcesTableRowCount; i++) {
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
      gradeCourseResourceDAO.create(course, resource, hours, hourlyCost, unitCost);
    }

    // Other costs

    int otherCostsTableRowCount = requestContext.getInteger("otherCostsTable.rowCount");
    for (int i = 0; i < otherCostsTableRowCount; i++) {
      String colPrefix = "otherCostsTable." + i;
      name = requestContext.getString(colPrefix + ".name");
      MonetaryAmount cost = new MonetaryAmount();
      cost.setAmount(requestContext.getDouble(colPrefix + ".cost"));
      otherCostDAO.create(course, name, cost);
    }

    // Students

    int studentsTableRowCount = requestContext.getInteger("studentsTable.rowCount");
    for (int i = 0; i < studentsTableRowCount; i++) {
      String colPrefix = "studentsTable." + i;

      Long studentId = requestContext.getLong(colPrefix + ".studentId");
      Date enrolmentDate = requestContext.getDate(colPrefix + ".enrolmentDate");
      Long enrolmentTypeId = requestContext.getLong(colPrefix + ".enrolmentType");
      Long participationTypeId = requestContext.getLong(colPrefix + ".participationType");
      Boolean lodging = requestContext.getBoolean(colPrefix + ".lodging");
      String organizationName = null;
      String additionalInfo = null;
      Room room = null;
      BigDecimal lodgingFee = null;
      Currency lodgingFeeCurrency = null;
      BigDecimal reservationFee = null;
      Currency reservationFeeCurrency = null;

      Student student = studentDAO.findById(studentId);
      CourseEnrolmentType enrolmentType = enrolmentTypeId != null ? enrolmentTypeDAO.findById(enrolmentTypeId) : null;
      CourseParticipationType participationType = participationTypeId != null ? participationTypeDAO.findById(participationTypeId) : null;
      CourseOptionality optionality = (CourseOptionality) requestContext.getEnum(colPrefix + ".optionality", CourseOptionality.class);

      try {
        courseStudentDAO.create(course, student, enrolmentType, participationType, enrolmentDate, lodging, optionality, null, 
            organizationName, additionalInfo, room, lodgingFee, lodgingFeeCurrency, reservationFee, reservationFeeCurrency, Boolean.FALSE);
      } catch (DuplicateCourseStudentException dcse) {
        Locale locale = requestContext.getRequest().getLocale();
        throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, 
            Messages.getInstance().getText(locale, "generic.errors.duplicateCourseStudent", new Object[] { student.getFullName() }));
      }
    }
    
    String redirectURL = requestContext.getRequest().getContextPath() + "/courses/editcourse.page?course=" + course.getId();
    String refererAnchor = requestContext.getRefererAnchor();
    
    if (!StringUtils.isBlank(refererAnchor))
      redirectURL += "#" + refererAnchor;

    requestContext.setRedirectURL(redirectURL);
  }

  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}