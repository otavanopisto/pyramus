package fi.otavanopisto.pyramus.views.students;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.ContactURLTypeDAO;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.LanguageDAO;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.dao.base.NationalityDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.students.StudentActivityTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentCardDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentEducationalLevelDAO;
import fi.otavanopisto.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentLodgingPeriodDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.Archived;
import fi.otavanopisto.pyramus.domainmodel.TSB;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCard;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.domainmodel.users.PersonVariable;
import fi.otavanopisto.pyramus.domainmodel.users.PersonVariableKey;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.framework.PyramusRequestControllerAccess;
import fi.otavanopisto.pyramus.framework.PyramusViewController2;
import fi.otavanopisto.pyramus.framework.StaffMemberProperties;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.otavanopisto.pyramus.security.impl.Permissions;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;
import fi.otavanopisto.pyramus.views.PyramusViewPermissions;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EditStudentViewController extends PyramusViewController2 implements Breadcrumbable {

  public EditStudentViewController() {
    super(
        PyramusRequestControllerAccess.REQUIRELOGIN // access
    );
  }

  @Override
  protected boolean checkAccess(RequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();

    Long loggedUserId = requestContext.getLoggedUserId();
    StaffMember staffMember = staffMemberDAO.findById(loggedUserId);

    if (!Permissions.instance().hasEnvironmentPermission(staffMember, PyramusViewPermissions.EDIT_STUDENT)) {
      return false;
    }
    else {
      Long personId = requestContext.getLong("person");
      Person person = personDAO.findById(personId);

      // #1416: Staff members may only access students of their specified study programmes
      if (UserUtils.canAccessStudent(staffMember, person)) {
        if (UserUtils.canAccessAllOrganizations(staffMember)) {
          return true;
        }
        for (Student student : person.getStudents()) {
          if (UserUtils.isMemberOf(staffMember, student.getOrganization())) {
            // Having one common organization is enough - though the view may not allow editing all
            return true;
          }
        }
      }

      return false;
    }
  }
  
  public void process(PageRequestContext pageRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    StudentActivityTypeDAO studentActivityTypeDAO = DAOFactory.getInstance().getStudentActivityTypeDAO();
    StudentEducationalLevelDAO studentEducationalLevelDAO = DAOFactory.getInstance().getStudentEducationalLevelDAO();
    StudentExaminationTypeDAO studentExaminationTypeDAO = DAOFactory.getInstance().getStudentExaminationTypeDAO();
    StudentStudyEndReasonDAO studyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    UserVariableKeyDAO userVariableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    ContactURLTypeDAO contactURLTypeDAO = DAOFactory.getInstance().getContactURLTypeDAO();
    CreditLinkDAO creditLinkDAO = DAOFactory.getInstance().getCreditLinkDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
    StudentLodgingPeriodDAO studentLodgingPeriodDAO = DAOFactory.getInstance().getStudentLodgingPeriodDAO();
    PersonVariableKeyDAO personVariableKeyDAO = DAOFactory.getInstance().getPersonVariableKeyDAO();
    PersonVariableDAO personVariableDAO = DAOFactory.getInstance().getPersonVariableDAO();
    StudentStudyPeriodDAO studentStudyPeriodDAO = DAOFactory.getInstance().getStudentStudyPeriodDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    StudentCardDAO studentCardDAO = DAOFactory.getInstance().getStudentCardDAO();
    Locale locale = pageRequestContext.getRequest().getLocale();
    
    User loggedUser = userDAO.findById(pageRequestContext.getLoggedUserId());
    
    Long personId = pageRequestContext.getLong("person");
    Person person = personDAO.findById(personId);
    
    List<Student> students = UserUtils.canAccessAllOrganizations(loggedUser) ?
        studentDAO.listByPerson(person) : studentDAO.listByPersonAndOrganization(person, loggedUser.getOrganization());
        
    Collections.sort(students, new Comparator<Student>() {
      @Override
      public int compare(Student o1, Student o2) {
        /**
         * Ordering study programmes as follows
         *  1. studies that have start date but no end date (ongoing)
         *  2. studies that have no start nor end date
         *  3. studies that have ended
         *  4. studies that are archived
         *  5. other
         */
        
        int o1class =
          (o1.getArchived()) ? 4:
            (o1.getStudyStartDate() != null && o1.getStudyEndDate() == null) ? 1:
              (o1.getStudyStartDate() == null && o1.getStudyEndDate() == null) ? 2:
                (o1.getStudyEndDate() != null) ? 3:
                  5;
        int o2class =
          (o2.getArchived()) ? 4:
            (o2.getStudyStartDate() != null && o2.getStudyEndDate() == null) ? 1:
              (o2.getStudyStartDate() == null && o2.getStudyEndDate() == null) ? 2:
                (o2.getStudyEndDate() != null) ? 3:
                  5;

        if (o1class == o2class) {
          // classes are the same, we try to do last comparison from the start dates
          return ((o1.getStudyStartDate() != null) && (o2.getStudyStartDate() != null)) ? 
              o2.getStudyStartDate().compareTo(o1.getStudyStartDate()) : 0; 
        } else
          return o1class < o2class ? -1 : o1class == o2class ? 0 : 1;
      }
    });
    
    Map<Long, String> studentTags = new HashMap<>();
    Map<Long, Boolean> studentHasCredits = new HashMap<>();
    Map<Long, Boolean> studentHasFiles = new HashMap<>();
    Map<Long, StudentCard> studentCards = new HashMap<>();
    
    List<UserVariableKey> userVariableKeys = userVariableKeyDAO.listByUserEditable(Boolean.TRUE);
    Collections.sort(userVariableKeys, new StringAttributeComparator("getVariableName"));
    
    JSONObject studentLodgingPeriods = new JSONObject();
    JSONObject studentStudyPeriodsJSON = new JSONObject();
    
    for (Student student : students) {
      StringBuilder tagsBuilder = new StringBuilder();
      Iterator<Tag> tagIterator = student.getTags().iterator();
      while (tagIterator.hasNext()) {
        Tag tag = tagIterator.next();
        tagsBuilder.append(tag.getText());
        if (tagIterator.hasNext())
          tagsBuilder.append(' ');
      }
      
      studentTags.put(student.getId(), tagsBuilder.toString());
      
      studentHasCredits.put(student.getId(), 
          creditLinkDAO.countByStudent(student) +
          courseAssessmentDAO.countByStudent(student) +
          transferCreditDAO.countByStudent(student) > 0);
      studentHasFiles.put(student.getId(), CollectionUtils.isNotEmpty(studentFileDAO.listByStudent(student, TSB.FALSE)));
      
      JSONArray variables = new JSONArray();
      for (UserVariableKey userVariableKey : userVariableKeys) {
        UserVariable userVariable = userVariableDAO.findByUserAndVariableKey(student, userVariableKey);
        JSONObject variable = new JSONObject();
        variable.put("type", userVariableKey.getVariableType());
        variable.put("name", userVariableKey.getVariableName());
        variable.put("key", userVariableKey.getVariableKey());
        variable.put("value", userVariable != null ? userVariable.getValue() : "");
        variables.add(variable);
      }
      
      setJsDataVariable(pageRequestContext, "variables." + student.getId(), variables.toString());

      List<StudentLodgingPeriod> studentLodgingPeriodEntities = studentLodgingPeriodDAO.listByStudent(student);
      studentLodgingPeriodEntities.sort(Comparator.comparing(StudentLodgingPeriod::getBegin, Comparator.nullsLast(Comparator.naturalOrder())));
      
      JSONArray lodgingPeriods = new JSONArray(); 
      for (StudentLodgingPeriod period : studentLodgingPeriodEntities) {
        JSONObject periodJSON = new JSONObject();
        periodJSON.put("id", period.getId());
        periodJSON.put("begin", period.getBegin() != null ? period.getBegin().getTime() : null);
        periodJSON.put("end", period.getEnd() != null ? period.getEnd().getTime() : null);
        lodgingPeriods.add(periodJSON);
      }
      if (!lodgingPeriods.isEmpty()) {
        studentLodgingPeriods.put(student.getId(), lodgingPeriods);
      }
      
      List<StudentStudyPeriod> studyPeriods = studentStudyPeriodDAO.listByStudent(student);
      studyPeriods.sort(Comparator.comparing(StudentStudyPeriod::getBegin, Comparator.nullsLast(Comparator.naturalOrder())));
      
      JSONArray studyPeriodsJSON = new JSONArray();
      for (StudentStudyPeriod studyPeriod : studyPeriods) {
        JSONObject periodJSON = new JSONObject();
        periodJSON.put("id", studyPeriod.getId());
        periodJSON.put("begin", studyPeriod.getBegin() != null ? studyPeriod.getBegin().getTime() : null);
        periodJSON.put("end", studyPeriod.getEnd() != null ? studyPeriod.getEnd().getTime() : null);
        periodJSON.put("type", studyPeriod.getPeriodType());
        studyPeriodsJSON.add(periodJSON);
      }
      if (!studyPeriodsJSON.isEmpty()) {
        studentStudyPeriodsJSON.put(student.getId(), studyPeriodsJSON);
      }
      
      studentCards.put(student.getId(), studentCardDAO.findByStudent(student));
      
    }
    
    setJsDataVariable(pageRequestContext, "studentLodgingPeriods", studentLodgingPeriods.toString());
    setJsDataVariable(pageRequestContext, "studentStudyPeriods", studentStudyPeriodsJSON.toString());

    List<PersonVariableKey> personVariableKeys = personVariableKeyDAO.listUserEditablePersonVariableKeys();
    Collections.sort(personVariableKeys, new StringAttributeComparator("getVariableName"));
    
    JSONArray personVariablesJSON = new JSONArray();
    for (PersonVariableKey personVariableKey : personVariableKeys) {
      PersonVariable personVariable = personVariableDAO.findByPersonAndVariableKey(person, personVariableKey);
      JSONObject personVariableJSON = new JSONObject();
      personVariableJSON.put("type", personVariableKey.getVariableType());
      personVariableJSON.put("name", personVariableKey.getVariableName());
      personVariableJSON.put("key", personVariableKey.getVariableKey());
      personVariableJSON.put("value", personVariable != null ? personVariable.getValue() : "");
      personVariablesJSON.add(personVariableJSON);
    }
    setJsDataVariable(pageRequestContext, "personVariables", personVariablesJSON.toString());
    
    List<Nationality> nationalities = nationalityDAO.listUnarchived();
    Collections.sort(nationalities, new StringAttributeComparator("getName"));
    
    List<Municipality> municipalities = municipalityDAO.listUnarchived();
    Collections.sort(municipalities, new StringAttributeComparator("getName"));

    List<Language> languages = languageDAO.listUnarchived();
    Collections.sort(languages, new StringAttributeComparator("getName"));

    List<School> schools = schoolDAO.listUnarchived();
    Collections.sort(schools, new StringAttributeComparator("getName"));

    List<ContactURLType> contactURLTypes = contactURLTypeDAO.listUnarchived();
    Collections.sort(contactURLTypes, new StringAttributeComparator("getName"));

    List<ContactType> contactTypes = contactTypeDAO.listUnarchived();
    Collections.sort(contactTypes, new StringAttributeComparator("getName"));
    
    String username = "";
    boolean hasInternalAuthenticationStrategies = AuthenticationProviderVault.getInstance().hasInternalStrategies();

    if (UserUtils.allowEditCredentials(loggedUser, person)) {
      if (hasInternalAuthenticationStrategies) {
        // TODO: Support for multiple internal authentication providers
        List<InternalAuthenticationProvider> internalAuthenticationProviders = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
        if (internalAuthenticationProviders.size() == 1) {
          InternalAuthenticationProvider internalAuthenticationProvider = internalAuthenticationProviders.get(0);
          if (internalAuthenticationProvider != null) {
            UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(internalAuthenticationProvider.getName(), person);
            if (internalAuthenticationProvider.canUpdateCredentials()) {
              if (userIdentification != null) {
                username = internalAuthenticationProvider.getUsername(userIdentification.getExternalId());
              }
            }
          }
        }
      }
    }

    JSONArray studentStudyPeriodTypesJSON = new JSONArray();
    for (StudentStudyPeriodType studentStudyPeriodType : StudentStudyPeriodType.values()) {
      JSONObject studyPeriodType = new JSONObject();
      studyPeriodType.put("id", studentStudyPeriodType.toString());
      studyPeriodType.put("displayName", Messages.getInstance().getText(locale, String.format("generic.studentStudyPeriods.%s", studentStudyPeriodType)));
      studyPeriodType.put("beginOnly", StudentStudyPeriodType.BEGINDATE_ONLY.contains(studentStudyPeriodType));
      studentStudyPeriodTypesJSON.add(studyPeriodType);
    }
    setJsDataVariable(pageRequestContext, "studentStudyPeriodTypes", studentStudyPeriodTypesJSON.toString());

    List<Curriculum> curriculums = curriculumDAO.listUnarchived();
    Collections.sort(curriculums, new StringAttributeComparator("getName"));

    List<StudyProgramme> studyProgrammes = UserUtils.canAccessAllOrganizations(loggedUser) ? 
        studyProgrammeDAO.listUnarchived() : studyProgrammeDAO.listByOrganization(loggedUser.getOrganization(), Archived.UNARCHIVED);
    Collections.sort(studyProgrammes, new StringAttributeComparator("getName"));
    
    List<StaffMember> studyApprovers = staffMemberDAO.listByProperty(StaffMemberProperties.STUDY_APPROVER.getKey(), "1");
    // Add study approvers to the list that have been used before so the selections can be persisted
    List<StaffMember> selectedStudyApprovers = students.stream()
      .map(student -> student.getStudyApprover())
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
    
    for (StaffMember selectedStudyApprover : selectedStudyApprovers) {
      Long selectedStudyApproverId = selectedStudyApprover.getId();
      
      boolean isSelectedInList = studyApprovers.stream()
        .map(StaffMember::getId)
        .anyMatch(selectedStudyApproverId::equals);
      
      if (!isSelectedInList) {
        studyApprovers.add(selectedStudyApprover);
      }
    }
    studyApprovers.sort(Comparator.comparing(StaffMember::getLastName).thenComparing(StaffMember::getFirstName));
    
    // Audit (not logging when returning to this page after save reloads it) 

    if (!StringUtils.contains(pageRequestContext.getReferer(false), "editstudent.page")) {
      studentDAO.auditView(personId, null, "Edit student");
    }

    readUserVariablePresets(pageRequestContext);
    
    pageRequestContext.getRequest().setAttribute("tags", studentTags);
    pageRequestContext.getRequest().setAttribute("person", person);
    pageRequestContext.getRequest().setAttribute("studentCards", studentCards);
    pageRequestContext.getRequest().setAttribute("students", students);
    pageRequestContext.getRequest().setAttribute("activityTypes", studentActivityTypeDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("contactURLTypes", contactURLTypes);
    pageRequestContext.getRequest().setAttribute("contactTypes", contactTypes);
    pageRequestContext.getRequest().setAttribute("examinationTypes", studentExaminationTypeDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("educationalLevels", studentEducationalLevelDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("nationalities", nationalities);
    pageRequestContext.getRequest().setAttribute("municipalities", municipalities);
    pageRequestContext.getRequest().setAttribute("languages", languages);
    pageRequestContext.getRequest().setAttribute("schools", schools);
    pageRequestContext.getRequest().setAttribute("studyProgrammes", studyProgrammes);
    pageRequestContext.getRequest().setAttribute("curriculums", curriculums);
    pageRequestContext.getRequest().setAttribute("studyEndReasons", studyEndReasonDAO.listByParentReason(null));
    pageRequestContext.getRequest().setAttribute("variableKeys", userVariableKeys);
    pageRequestContext.getRequest().setAttribute("personVariableKeys", personVariableKeys);
    pageRequestContext.getRequest().setAttribute("studentHasCredits", studentHasCredits);
    pageRequestContext.getRequest().setAttribute("studentHasFiles", studentHasFiles);
    pageRequestContext.getRequest().setAttribute("hasInternalAuthenticationStrategies", hasInternalAuthenticationStrategies);
    pageRequestContext.getRequest().setAttribute("username", username);
    pageRequestContext.getRequest().setAttribute("allowEditCredentials", UserUtils.allowEditCredentials(loggedUser, person));
    pageRequestContext.getRequest().setAttribute("studyApprovers", studyApprovers);
    
    pageRequestContext.setIncludeJSP("/templates/students/editstudent.jsp");
  }

  /**
   * Reads a json from file system and delivers it to frontend. The file is of format
   * 
   * {
   *   "variableKey (from UserVariable.variableKey)": {
   *     "presets": [
   *       {
   *         label: "Lorem ipsum",
   *         value: "dolor sit amet"
   *       }
   *     ]
   *   }
   * }
   *
   */
  private void readUserVariablePresets(PageRequestContext pageRequestContext) {
    String json = null;

    String fileName = System.getProperty("jboss.server.config.dir") + "/pyramus-uservariable-presets.json";
    File file = new File(fileName);
    if (file.exists()) {
      try {
        json = FileUtils.readFileToString(file);
      } catch (IOException e) {
      }
    }
    
    if (StringUtils.isBlank(json)) {
      json = "{}";
    }
    
    setJsDataVariable(pageRequestContext, "userVariablePresets", json);
  }
  
  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "students.editStudent.breadcrumb");
  }

}
