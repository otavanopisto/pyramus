package fi.otavanopisto.pyramus.views.students;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.enterprise.inject.spi.CDI;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.PyramusConsts;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.binary.ytl.YTLController;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentRequestDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.ProjectAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationGradeDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportDAO;
import fi.otavanopisto.pyramus.dao.students.StudentCardDAO;
import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryCommentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.students.StudentImageDAO;
import fi.otavanopisto.pyramus.dao.students.StudentLodgingPeriodDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.StudentParentDAO;
import fi.otavanopisto.pyramus.dao.users.StudentParentInvitationDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.file.StudentFile;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditType;
import fi.otavanopisto.pyramus.domainmodel.grading.ProjectAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationGrade;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectModule;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectSubjectCourse;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportContextType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCard;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryComment;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.users.PersonVariable;
import fi.otavanopisto.pyramus.domainmodel.users.PersonVariableKey;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentInvitation;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.framework.PyramusRequestControllerAccess;
import fi.otavanopisto.pyramus.framework.PyramusViewController2;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.koski.KoskiConsts;
import fi.otavanopisto.pyramus.koski.KoskiController;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;
import fi.otavanopisto.pyramus.security.impl.Permissions;
import fi.otavanopisto.pyramus.tor.StudentTOR;
import fi.otavanopisto.pyramus.tor.StudentTORController;
import fi.otavanopisto.pyramus.tor.TORCourse;
import fi.otavanopisto.pyramus.tor.TORSubject;
import fi.otavanopisto.pyramus.tor.curriculum.TORCurriculum;
import fi.otavanopisto.pyramus.tor.curriculum.TORCurriculumModule;
import fi.otavanopisto.pyramus.tor.curriculum.TORCurriculumSubject;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;
import fi.otavanopisto.pyramus.views.PyramusViewPermissions;
import fi.otavanopisto.pyramus.ytl.YTLAineKoodi;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * ViewController for editing student information.
 */
public class ViewStudentViewController extends PyramusViewController2 implements Breadcrumbable {
  
  private static final Logger logger = Logger.getLogger(ViewStudentViewController.class.getName());
  
  public ViewStudentViewController() {
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

    if (!Permissions.instance().hasEnvironmentPermission(staffMember, PyramusViewPermissions.VIEW_STUDENT)) {
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
  
  /**
   * Processes the page request.
   * 
   * In parameters
   * - student
   * - person
   * 
   * Page parameters
   * - student - Student object
   * - nationalities - List of Nationality objects
   * - municipalities - List of Municipality objects
   * - languages - List of Language objects
   * - studentCourses - List of CourseStudent objects
   * - studentContactEntries - List of StudentContactLogEntry objects
   * 
   * @param pageRequestContext pageRequestContext
   */
  public void process(PageRequestContext pageRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    StudentImageDAO imageDAO = DAOFactory.getInstance().getStudentImageDAO();
    StudentContactLogEntryDAO logEntryDAO = DAOFactory.getInstance().getStudentContactLogEntryDAO();
    StudentContactLogEntryCommentDAO entryCommentDAO = DAOFactory.getInstance().getStudentContactLogEntryCommentDAO();
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    StudentProjectDAO studentProjectDAO = DAOFactory.getInstance().getStudentProjectDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    ProjectAssessmentDAO projectAssessmentDAO = DAOFactory.getInstance().getProjectAssessmentDAO();
    CreditLinkDAO creditLinkDAO = DAOFactory.getInstance().getCreditLinkDAO();
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    CourseAssessmentRequestDAO courseAssessmentRequestDAO = DAOFactory.getInstance().getCourseAssessmentRequestDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    StudentLodgingPeriodDAO studentLodgingPeriodDAO = DAOFactory.getInstance().getStudentLodgingPeriodDAO();
    PersonVariableDAO personVariableDAO = DAOFactory.getInstance().getPersonVariableDAO();
    PersonVariableKeyDAO personVariableKeyDAO = DAOFactory.getInstance().getPersonVariableKeyDAO();
    StudentStudyPeriodDAO studentStudyPeriodDAO = DAOFactory.getInstance().getStudentStudyPeriodDAO();
    MatriculationExamEnrollmentDAO matriculationExamEnrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    StudentCardDAO studentCardDAO = DAOFactory.getInstance().getStudentCardDAO();
    StudentParentInvitationDAO studentParentInvitationDAO = DAOFactory.getInstance().getStudentParentInvitationDAO();
    StudentParentDAO studentParentDAO = DAOFactory.getInstance().getStudentParentDAO();

    KoskiController koskiController = CDI.current().select(KoskiController.class).get();

    Long loggedUserId = pageRequestContext.getLoggedUserId();
    StaffMember loggedUser = staffMemberDAO.findById(loggedUserId);
    
    Long personId = pageRequestContext.getLong("person");
    
    Person person = personDAO.findById(personId);
    
    pageRequestContext.getRequest().setAttribute("person", person);

    StaffMember staffMember = staffMemberDAO.findByPerson(person);
    pageRequestContext.getRequest().setAttribute("staffMember", staffMember);
    
    Map<Long, StudentCard> studentCards = new HashMap<>();
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

    Map<Long, Boolean> studentHasImage = new HashMap<>();
    Map<Long, List<CourseStudent>> courseStudents = new HashMap<>();
    Map<Long, List<StudentContactLogEntry>> contactEntries = new HashMap<>();
    Map<Long, List<TransferCredit>> transferCredits = new HashMap<>();
    Map<Long, List<CourseAssessment>> courseAssessments = new HashMap<>();
    Map<Long, CourseAssessmentRequest> courseAssessmentRequests = new HashMap<>();
    Map<Long, List<StudentGroup>> studentGroups = new HashMap<>();
    Map<Long, List<StudentProjectBean>> studentProjects = new HashMap<>();
    Map<Long, CourseAssessment> courseAssessmentsByCourseStudent = new HashMap<>();
    // StudentProject.id -> List of module beans
    Map<Long, List<StudentProjectModuleBean<StudentProjectModule>>> studentProjectModules = new HashMap<>();
    Map<Long, List<StudentProjectModuleBean<StudentProjectSubjectCourse>>> studentProjectSubjectCourses = new HashMap<>();
    final Map<Long, List<StudentContactLogEntryComment>> contactEntryComments = new HashMap<>();
    Map<Long, List<StudentLodgingPeriod>> studentLodgingPeriods = new HashMap<>();
    Map<Long, List<StudentStudyPeriod>> studentStudyPeriods = new HashMap<>();
    Map<Long, StudentTOR> subjectCredits = new HashMap<>();
    Map<Long, List<MatriculationExamEnrollment>> studentMatriculationEnrollments = new HashMap<>();
    Map<Long, Boolean> studentHasParents = new HashMap<>();
    Map<Long, Boolean> studentHasParentInvitations = new HashMap<>();
    Map<Long, Set<String>> koskiStudentOIDs = new HashMap<>();
    List<ViewStudentValidationWarning> studentValidations = new ArrayList<>();
    
    JSONObject linkedCourseAssessments = new JSONObject();
    JSONObject linkedTransferCredits = new JSONObject();
    JSONObject studentFiles = new JSONObject();
    JSONObject studentVariablesJSON = new JSONObject();
    JSONArray studentReportsJSON = new JSONArray();
    JSONArray curriculumsJSON = new JSONArray();
    JSONObject studentAssessmentsJSON = new JSONObject();
    JSONObject studentParentsJSON = new JSONObject();
    JSONObject studentParentInvitationsJSON = new JSONObject();
    
    List<Report> studentReports = reportDAO.listByContextType(ReportContextType.Student);
    Collections.sort(studentReports, new StringAttributeComparator("getName"));

    for (Report report : studentReports) {
      JSONObject obj = new JSONObject();
      obj.put("id", report.getId().toString());
      obj.put("name", report.getName());
      studentReportsJSON.add(obj);
    }
    
    /* PersonVariables */
    
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
    
    /* Curriculums */
    
    List<Curriculum> curriculums = curriculumDAO.listUnarchived();
    
    for (Curriculum curriculum : curriculums) {
      JSONObject obj = new JSONObject();
      obj.put("id", curriculum.getId().toString());
      obj.put("name", curriculum.getName());
      curriculumsJSON.add(obj);
    }
    
    for (Student student : students) {

      // Student card
      StudentCard studentCard =  studentCardDAO.findByStudent(student);
      
      if (studentCard != null) {
        studentCards.put(student.getId(), studentCard);
      }
      /**
       * Fetch courses this student is part of and sort the courses by course name
       */

      List<CourseStudent> courseStudentsByStudent = courseStudentDAO.listByStudent(student);

      Collections.sort(courseStudentsByStudent, new Comparator<CourseStudent>() {
        private String getCourseAssessmentCompareStr(CourseStudent courseStudent) {
          String result = "";
          if (courseStudent != null)
            if (courseStudent.getCourse() != null)
              result = courseStudent.getCourse().getName();
            
          return result;
        }
        
        @Override
        public int compare(CourseStudent o1, CourseStudent o2) {
          String s1 = getCourseAssessmentCompareStr(o1);
          String s2 = getCourseAssessmentCompareStr(o2);
          
          return s1.compareToIgnoreCase(s2);
        }
      });

      /**
       * Course Assessment Requests by Course Student
       */

      for (CourseStudent courseStudent : courseStudentsByStudent) {
        List<CourseAssessmentRequest> courseAssessmentRequestsByCourseStudent = courseAssessmentRequestDAO.listByCourseStudent(courseStudent);

        Collections.sort(courseAssessmentRequestsByCourseStudent, new Comparator<CourseAssessmentRequest>() {
          @Override
          public int compare(CourseAssessmentRequest o1, CourseAssessmentRequest o2) {
            return o2.getCreated().compareTo(o1.getCreated());
          }
        });

        if (!courseAssessmentRequestsByCourseStudent.isEmpty()) {
          courseAssessmentRequests.put(courseStudent.getId(), courseAssessmentRequestsByCourseStudent.get(0));
        }
      }
      
      /**
       * Contact log entries
       */
      
      List<StudentContactLogEntry> listStudentContactEntries = logEntryDAO.listByStudent(student);

      // Firstly populate comments
      
      for (int j = 0; j < listStudentContactEntries.size(); j++) {
        StudentContactLogEntry entry = listStudentContactEntries.get(j);
        
        List<StudentContactLogEntryComment> listComments = entryCommentDAO.listByEntry(entry);
        
        Collections.sort(listComments, new Comparator<StudentContactLogEntryComment>() {
          public int compare(StudentContactLogEntryComment o1, StudentContactLogEntryComment o2) {
            Date d1 = o1.getCommentDate();
            Date d2 = o2.getCommentDate();
            
            int val = d1 == null ? 
                d2 == null ? 0 : 1 :
                  d2 == null ? -1 : d1.compareTo(d2);
            
            if (val == 0)
              return o1.getId().compareTo(o2.getId());
            else
              return val;
          }
        });
        
        contactEntryComments.put(entry.getId(), listComments);
      }

      // And then sort the entries by latest date of entry or its comments
      
      Collections.sort(listStudentContactEntries, new Comparator<StudentContactLogEntry>() {
        private Date getDateForEntry(StudentContactLogEntry entry) {
          Date d = entry.getEntryDate();

          List<StudentContactLogEntryComment> comments = contactEntryComments.get(entry.getId());
          
          for (int i = 0; i < comments.size(); i++) {
            StudentContactLogEntryComment comment = comments.get(i);
            
            if (d == null) {
              d = comment.getCommentDate();
            } else {
              if (d.before(comment.getCommentDate()))
                d = comment.getCommentDate();
            }
          }
          
          return d;
        }
        
        public int compare(StudentContactLogEntry o1, StudentContactLogEntry o2) {
          Date d1 = getDateForEntry(o1);
          Date d2 = getDateForEntry(o2);

          int val = d1 == null ? 
              d2 == null ? 0 : 1 :
                d2 == null ? -1 : d2.compareTo(d1);

          if (val == 0)
            return o2.getId().compareTo(o1.getId());
          else
            return val;
        }
      });


      /**
       * Students Course Assessments, sorted by course name
       */
      
      List<CourseAssessment> courseAssessmentsByStudent = courseAssessmentDAO.listByStudent(student);
      for (CourseAssessment courseAssessment : courseAssessmentsByStudent) {
        Long courseStudentId = courseAssessment.getCourseStudent().getId(); 
        courseAssessmentsByCourseStudent.put(courseStudentId, courseAssessment);
      }
      
      Collections.sort(courseAssessmentsByStudent, new Comparator<CourseAssessment>() {
        private String getCourseAssessmentCompareStr(CourseAssessment courseAssessment) {
          String result = "";
          if (courseAssessment != null)
            if (courseAssessment.getCourseStudent() != null)
              if (courseAssessment.getCourseStudent().getCourse() != null)
                result = courseAssessment.getCourseStudent().getCourse().getName();
            
          return result;
        }
        
        @Override
        public int compare(CourseAssessment o1, CourseAssessment o2) {
          String s1 = getCourseAssessmentCompareStr(o1);
          String s2 = getCourseAssessmentCompareStr(o2);
          
          return s1.compareToIgnoreCase(s2);
        }
      });

      JSONArray jsonCourseStudentAssessments = new JSONArray();
      
      for (CourseStudent courseStudent : courseStudentsByStudent) {
        for (CourseModule courseModule : courseStudent.getCourse().getCourseModules()) {
          List<CourseAssessment> courseAssessmentList = courseAssessmentsByStudent.stream()
            .filter(courseAssessment -> Objects.equals(courseModule.getId(), courseAssessment.getCourseModule().getId()))
            .collect(Collectors.toList());
          
          if (CollectionUtils.isNotEmpty(courseAssessmentList) && courseModule.getCourse() != null) {
            courseAssessmentList.sort(Comparator.comparing(CourseAssessment::getDate).thenComparing(CourseAssessment::getId));
            CourseBase course = courseModule.getCourse();
            
            JSONObject obj = new JSONObject();
            
            obj.put("courseStudentId", courseStudent.getId());
            obj.put("courseName", course.getName());
            obj.put("subjectName", getSubjectText(courseModule.getSubject(), pageRequestContext.getRequest().getLocale()));
            obj.put("courseNumber", courseModule.getCourseNumber());
            
            JSONArray jsonCurriculums = new JSONArray();
            for (Curriculum curriculum : course.getCurriculums()) {
              JSONObject curobj = new JSONObject();
              curobj.put("name", curriculum.getName());
              jsonCurriculums.add(curobj);
            }
            obj.put("curriculums", jsonCurriculums);
  
            if (courseModule.getCourseLength() != null) {
              EducationalLength courseLength = courseModule.getCourseLength();
              obj.put("courseLength", courseLength.getUnits().toString());
              if (courseLength.getUnit() != null) {
                obj.put("courseLengthUnitName", courseLength.getUnit().getName());
                obj.put("courseLengthUnitSymbol", courseLength.getUnit().getSymbol());
              }
            }
            
            JSONArray jsonCourseAssessments = new JSONArray();
            for (CourseAssessment ass : courseAssessmentList) {
              JSONObject assobj = new JSONObject();
              assobj.put("timestamp", ass.getDate() != null ? ass.getDate().getTime() : null);
              assobj.put("gradeName", ass.getGrade() != null ? ass.getGrade().getName() : null);
              assobj.put("gradingScaleName", (ass.getGrade() != null && ass.getGrade().getGradingScale() != null) ? 
                  ass.getGrade().getGradingScale().getName() : null);
              assobj.put("passing", ass.getGrade() != null ? ass.getGrade().getPassingGrade() : Boolean.FALSE);
              assobj.put("assessorName", ass.getAssessor() != null ? ass.getAssessor().getFullName() : null);
              jsonCourseAssessments.add(assobj);
            }
            obj.put("assessments", jsonCourseAssessments);
            jsonCourseStudentAssessments.add(obj);
          }
        }
      }
      studentAssessmentsJSON.put(student.getId(), jsonCourseStudentAssessments);
      
      
      /**
       * Fetching and sorting of Transfer Credits 
       */
      
      List<TransferCredit> transferCreditsByStudent = transferCreditDAO.listByStudent(student);
      Collections.sort(transferCreditsByStudent, new Comparator<TransferCredit>() {
        private String getCourseAssessmentCompareStr(TransferCredit tCredit) {
          String result = "";
         
          if (tCredit != null)
            result = tCredit.getCourseName();
           
          return result;
        }
        
        @Override
        public int compare(TransferCredit o1, TransferCredit o2) {
          String s1 = getCourseAssessmentCompareStr(o1);
          String s2 = getCourseAssessmentCompareStr(o2);
          
          return s1.compareToIgnoreCase(s2);
        }
      });

      /**
       * Linked CourseAssessments
       */
      
      List<CreditLink> linkedCourseAssessmentByStudent = creditLinkDAO.listByStudentAndType(student, CreditType.CourseAssessment);
      
      Collections.sort(linkedCourseAssessmentByStudent, new Comparator<CreditLink>() {
        private String getCourseAssessmentCompareStr(CourseAssessment courseAssessment) {
          String result = "";
          if (courseAssessment != null)
            if (courseAssessment.getCourseStudent() != null)
              if (courseAssessment.getCourseStudent().getCourse() != null)
                result = courseAssessment.getCourseStudent().getCourse().getName();
            
          return result;
        }
        
        @Override
        public int compare(CreditLink o1, CreditLink o2) {
          String s1 = getCourseAssessmentCompareStr((CourseAssessment) o1.getCredit());
          String s2 = getCourseAssessmentCompareStr((CourseAssessment) o2.getCredit());
          
          return s1.compareToIgnoreCase(s2);
        }
      });
      
      JSONArray arr = new JSONArray();
      for (CreditLink linkedCourseAssessment : linkedCourseAssessmentByStudent) {
        CourseAssessment courseAssessment = (CourseAssessment) linkedCourseAssessment.getCredit();
        
        String subjectName = getSubjectText(courseAssessment.getSubject(), pageRequestContext.getRequest().getLocale());
        
        JSONObject obj = new JSONObject();
        obj.put("creditLinkId", linkedCourseAssessment.getId().toString());
        obj.put("courseStudentId", courseAssessment.getCourseStudent().getId().toString());

        obj.put("courseName", courseAssessment.getCourseStudent().getCourse().getName());
        obj.put("subjectName", subjectName);
        obj.put("courseNumber", courseAssessment.getCourseNumber());
        obj.put("creditDate", courseAssessment.getDate().getTime());
        obj.put("courseLength", courseAssessment.getCourseLength().getUnits().toString());
        obj.put("courseLengthUnitName", courseAssessment.getCourseLength().getUnit().getName());
        obj.put("courseLengthUnitSymbol", courseAssessment.getCourseLength().getUnit().getSymbol());
        
        obj.put("gradeName", courseAssessment.getGrade() != null ? courseAssessment.getGrade().getName() : null);
        obj.put("gradingScaleName", courseAssessment.getGrade() != null ? courseAssessment.getGrade().getGradingScale().getName() : null);
        obj.put("assessingUserName", courseAssessment.getAssessor().getFullName());
        
        JSONArray courseCurriculums = new JSONArray();
        if (CollectionUtils.isNotEmpty(courseAssessment.getCourseStudent().getCourse().getCurriculums())) {
          for (Curriculum curriculum : courseAssessment.getCourseStudent().getCourse().getCurriculums()) {
            JSONObject courseCurriculum = new JSONObject();
            courseCurriculum.put("curriculumId", curriculum.getId());
            courseCurriculum.put("curriculumName", curriculum.getName());
            courseCurriculums.add(courseCurriculum);
          }
        }
        obj.put("curriculums", courseCurriculums);
        
        arr.add(obj);
      }
      
      if (!arr.isEmpty())
        linkedCourseAssessments.put(student.getId(), arr);
      
      /**
       * Linked TransferCredits
       */
      
      List<CreditLink> linkedTransferCreditsByStudent = creditLinkDAO.listByStudentAndType(student, CreditType.TransferCredit);

      Collections.sort(linkedTransferCreditsByStudent, new Comparator<CreditLink>() {
        private String getCourseAssessmentCompareStr(TransferCredit tCredit) {
          String result = "";
         
          if (tCredit != null)
            result = tCredit.getCourseName();
           
          return result;
        }
        
        @Override
        public int compare(CreditLink o1, CreditLink o2) {
          String s1 = getCourseAssessmentCompareStr((TransferCredit) o1.getCredit());
          String s2 = getCourseAssessmentCompareStr((TransferCredit) o2.getCredit());
          
          return s1.compareToIgnoreCase(s2);
        }
      });
      
      arr = new JSONArray();
      for (CreditLink linkedTransferCredit : linkedTransferCreditsByStudent) {
        TransferCredit transferCredit = (TransferCredit) linkedTransferCredit.getCredit();
        
        String subjectName = getSubjectText(transferCredit.getSubject(), pageRequestContext.getRequest().getLocale());
        
        JSONObject obj = new JSONObject();
        obj.put("creditLinkId", linkedTransferCredit.getId().toString());
        obj.put("transferCreditId", transferCredit.getId().toString());

        obj.put("courseName", transferCredit.getCourseName());
        obj.put("subjectName", subjectName);
        obj.put("courseNumber", transferCredit.getCourseNumber());
        obj.put("creditDate", transferCredit.getDate().getTime());
        obj.put("courseLength", transferCredit.getCourseLength().getUnits().toString());
        obj.put("courseLengthUnitName", transferCredit.getCourseLength().getUnit().getName());
        obj.put("courseLengthUnitSymbol", transferCredit.getCourseLength().getUnit().getSymbol());
        
        obj.put("gradeName", transferCredit.getGrade() != null ? transferCredit.getGrade().getName() : null);
        obj.put("gradingScaleName", transferCredit.getGrade() != null ? transferCredit.getGrade().getGradingScale().getName() : null);
        obj.put("assessingUserName", transferCredit.getAssessor().getFullName());
        
        if (transferCredit.getCurriculum() != null) {
          Curriculum curriculum = transferCredit.getCurriculum();
          obj.put("curriculumId", curriculum.getId());
          obj.put("curriculumName", curriculum.getName());
        }
        
        arr.add(obj);
      }
      
      if (!arr.isEmpty())
        linkedTransferCredits.put(student.getId(), arr);
      
      /**
       * Project beans setup
       */
      List<StudentProject> studentsStudentProjects = studentProjectDAO.listByStudent(student);
      List<StudentProjectBean> studentProjectBeans = new ArrayList<>();
      for (StudentProject studentProject : studentsStudentProjects) {
        int mandatoryModuleCount = 0;
        int optionalModuleCount = 0;
        int passedMandatoryModuleCount = 0;
        int passedOptionalModuleCount = 0;
        
        List<StudentProjectModuleBean<StudentProjectModule>> studentProjectModuleBeans = new ArrayList<>();
        List<StudentProjectModuleBean<StudentProjectSubjectCourse>> studentProjectSubjectCourseBeans = new ArrayList<>();
        
        /**
         * Go through project modules to
         *  a) count mandatory/optional modules
         *  b) count mandatory/optional modules that have passing grade on them
         *  c) create beans to be passed to jsp
         */
        
        List<CourseAssessment> allStudentCourseAssessments = courseAssessmentDAO.listByStudent(student);
        List<TransferCredit> allStudentTransferCredits = transferCreditDAO.listByStudent(student);
        List<CreditLink> allStudentCreditLinks = creditLinkDAO.listByStudent(student);

        for (CreditLink creditLink : allStudentCreditLinks) {
          switch (creditLink.getCredit().getCreditType()) {
            case CourseAssessment:
              allStudentCourseAssessments.add(((CourseAssessment) creditLink.getCredit()));
            break;

            case TransferCredit:
              allStudentTransferCredits.add(((TransferCredit) creditLink.getCredit()));
            break;
            
            case ProjectAssessment:
            break;
          }
        }
        
        for (StudentProjectModule studentProjectModule : studentProject.getStudentProjectModules()) {
          boolean hasPassingGrade = false;

          List<CourseStudent> projectCourseCourseStudentList = new ArrayList<>();
          List<TransferCredit> projectCourseTransferCreditList = new ArrayList<>();

          // Find out if there is a course that has passing grade for the module
          
          for (CourseAssessment assessment : allStudentCourseAssessments) {
            if (assessment.getCourseStudent().getCourse().getModule().getId().equals(studentProjectModule.getModule().getId())) {
              projectCourseCourseStudentList.add(assessment.getCourseStudent());
              if (assessment.getGrade() != null && assessment.getGrade().getPassingGrade())
                hasPassingGrade = true; 
            }
          }

          if (studentProjectModule.getModule().getCourseModules() != null) {
            for (CourseModule courseModule : studentProjectModule.getModule().getCourseModules()) {
              for (TransferCredit tc : allStudentTransferCredits) {
                if ((tc.getCourseNumber() != null) && (tc.getCourseNumber() != -1) && (tc.getSubject() != null)) {
                  if (tc.getCourseNumber().equals(courseModule.getCourseNumber()) && tc.getSubject().equals(courseModule.getSubject())) {
                    projectCourseTransferCreditList.add(tc);
                    if (tc.getGrade() != null && tc.getGrade().getPassingGrade())
                      hasPassingGrade = true;
                  }
                }
              }
            }
          }
          
          if (studentProjectModule.getOptionality() == CourseOptionality.MANDATORY) {
            mandatoryModuleCount++;
            if (hasPassingGrade)
              passedMandatoryModuleCount++;
          } else if (studentProjectModule.getOptionality() == CourseOptionality.OPTIONAL) {
            optionalModuleCount++;
            if (hasPassingGrade)
              passedOptionalModuleCount++;
          }
          
          StudentProjectModuleBean<StudentProjectModule> moduleBean = new StudentProjectModuleBean<>(studentProjectModule, hasPassingGrade, projectCourseCourseStudentList, projectCourseTransferCreditList);
          studentProjectModuleBeans.add(moduleBean);
        }

        // Add ModuleBeans to response
        studentProjectModules.put(studentProject.getId(), studentProjectModuleBeans);

        
        for (StudentProjectSubjectCourse studentProjectSubjectCourse : studentProject.getStudentProjectSubjectCourses()) {
          boolean hasPassingGrade = false;

          List<CourseStudent> projectCourseCourseStudentList = new ArrayList<>();
          List<TransferCredit> projectCourseTransferCreditList = new ArrayList<>();

          // Find out if there is a course that has passing grade for the module
          
          if ((studentProjectSubjectCourse.getCourseNumber() != null) && (studentProjectSubjectCourse.getCourseNumber() != -1) && (studentProjectSubjectCourse.getSubject() != null)) {

            for (CourseAssessment assessment : allStudentCourseAssessments) {
              if ((assessment.getCourseNumber() != null) && (assessment.getCourseNumber() != -1) && (assessment.getSubject() != null)) {
                if (assessment.getCourseNumber().equals(studentProjectSubjectCourse.getCourseNumber()) && assessment.getSubject().equals(studentProjectSubjectCourse.getSubject())) {
                  projectCourseCourseStudentList.add(assessment.getCourseStudent());
                  if (assessment.getGrade() != null && assessment.getGrade().getPassingGrade()) {
                    hasPassingGrade = true;
                  }
                }
              }
            }
          
            for (TransferCredit tc : allStudentTransferCredits) {
              if ((tc.getCourseNumber() != null) && (tc.getCourseNumber() != -1) && (tc.getSubject() != null)) {
                if (tc.getCourseNumber().equals(studentProjectSubjectCourse.getCourseNumber()) && tc.getSubject().equals(studentProjectSubjectCourse.getSubject())) {
                  projectCourseTransferCreditList.add(tc);
                  if (tc.getGrade() != null && tc.getGrade().getPassingGrade())
                    hasPassingGrade = true;
                }
              }
            }
          }
          
          if (studentProjectSubjectCourse.getOptionality() == CourseOptionality.MANDATORY) {
            mandatoryModuleCount++;
            if (hasPassingGrade)
              passedMandatoryModuleCount++;
          } else if (studentProjectSubjectCourse.getOptionality() == CourseOptionality.OPTIONAL) {
            optionalModuleCount++;
            if (hasPassingGrade)
              passedOptionalModuleCount++;
          }
          
          StudentProjectModuleBean<StudentProjectSubjectCourse> moduleBean = new StudentProjectModuleBean<>(studentProjectSubjectCourse, hasPassingGrade, projectCourseCourseStudentList, projectCourseTransferCreditList);
          studentProjectSubjectCourseBeans.add(moduleBean);
        }

        // Add ModuleBeans to response
        studentProjectSubjectCourses.put(studentProject.getId(), studentProjectSubjectCourseBeans);
        
        List<ProjectAssessment> projectAssessments = projectAssessmentDAO.listByProjectAndArchived(studentProject, Boolean.FALSE);
        Collections.sort(projectAssessments, new Comparator<ProjectAssessment>() {
          @Override
          public int compare(ProjectAssessment o1, ProjectAssessment o2) {
            return o2.getDate().compareTo(o1.getDate());
          }
        });

        StudentProjectBean bean = new StudentProjectBean(studentProject, mandatoryModuleCount, optionalModuleCount, passedMandatoryModuleCount, passedOptionalModuleCount, projectAssessments);
        studentProjectBeans.add(bean);
      }

      List<StudentFile> files = studentFileDAO.listByStudent(student);
      Collections.sort(files, new StringAttributeComparator("getName", true));

      arr = new JSONArray();
      for (StudentFile file : files) {
        JSONObject obj = new JSONObject();
        obj.put("id", file.getId());
        obj.put("name", file.getName());
        obj.put("fileTypeName", file.getFileType() != null ? file.getFileType().getName() : "");
        obj.put("created", file.getCreated().getTime());
        obj.put("lastModified", file.getLastModified().getTime());
        arr.add(obj);
      }
      if (!arr.isEmpty()) {
        studentFiles.put(student.getId(), arr);
      }
      
      // Separate list of Student's OIDs
      Set<String> studentOIDs = koskiController.listStudentOIDs(student);
      if (CollectionUtils.isNotEmpty(studentOIDs)) {
        koskiStudentOIDs.put(student.getId(), studentOIDs);
      }
      
      JSONArray variables = new JSONArray();
      for (UserVariable userVariable : userVariableDAO.listByUserAndUserEditable(student, true)) {
        JSONObject variable = new JSONObject();
        variable.put("type", userVariable.getKey().getVariableType());
        variable.put("name", userVariable.getKey().getVariableName());
        variable.put("key", userVariable.getKey().getVariableKey());
        variable.put("value", userVariable.getValue() != null ? userVariable.getValue() : "");
        variables.add(variable);
      }
      
      if (!variables.isEmpty())
        studentVariablesJSON.put(student.getId(), variables);
      
      // Student Image
      studentHasImage.put(student.getId(), imageDAO.findStudentHasImage(student));

      List<StudentLodgingPeriod> studentLodgingPeriodEntities = studentLodgingPeriodDAO.listByStudent(student);
      studentLodgingPeriodEntities.sort(Comparator.comparing(StudentLodgingPeriod::getBegin, Comparator.nullsLast(Comparator.naturalOrder())));
      
      List<StudentStudyPeriod> studentStudyPeriodEntities = studentStudyPeriodDAO.listByStudent(student);
      studentStudyPeriodEntities.sort(Comparator.comparing(StudentStudyPeriod::getBegin, Comparator.nullsLast(Comparator.naturalOrder())));
      
      courseStudents.put(student.getId(), courseStudentsByStudent);
      courseAssessments.put(student.getId(), courseAssessmentsByStudent);
      contactEntries.put(student.getId(), listStudentContactEntries);
      transferCredits.put(student.getId(), transferCreditsByStudent);
      studentGroups.put(student.getId(), studentGroupDAO.listByStudent(student, null, null, false));
      studentProjects.put(student.getId(), studentProjectBeans);
      studentLodgingPeriods.put(student.getId(), studentLodgingPeriodEntities);
      studentStudyPeriods.put(student.getId(), studentStudyPeriodEntities);
      studentMatriculationEnrollments.put(student.getId(), matriculationExamEnrollmentDAO.listByStudent(student));

      
      arr = new JSONArray();
      List<StudentParentInvitation> studentParentInvitations = studentParentInvitationDAO.listBy(student);
      for (StudentParentInvitation studentParentInvitation : studentParentInvitations) {
        JSONObject studentParentJSON = new JSONObject();
        studentParentJSON.put("invitationId", studentParentInvitation.getId());
        studentParentJSON.put("expired", studentParentInvitation.isExpired());
        studentParentJSON.put("firstName", studentParentInvitation.getFirstName());
        studentParentJSON.put("lastName", studentParentInvitation.getLastName());
        studentParentJSON.put("email", studentParentInvitation.getEmail());
        arr.add(studentParentJSON);
      }
      studentHasParentInvitations.put(student.getId(), !studentParentInvitations.isEmpty());
      studentParentInvitationsJSON.put(student.getId(), arr);
      
      arr = new JSONArray();
      List<StudentParent> studentParents = studentParentDAO.listBy(student);
      for (StudentParent studentParent : studentParents) {
        JSONObject studentParentJSON = new JSONObject();
        studentParentJSON.put("personId", studentParent.getPersonId());
        studentParentJSON.put("userId", studentParent.getId());
        studentParentJSON.put("firstName", studentParent.getFirstName());
        studentParentJSON.put("lastName", studentParent.getLastName());
        
        String emails =
            studentParent.getContactInfo() != null && studentParent.getContactInfo().getEmails() != null 
            ? studentParent.getContactInfo().getEmails().stream().map(Email::getAddress).collect(Collectors.joining(", "))
            : null;
        studentParentJSON.put("email", emails);
        arr.add(studentParentJSON);
      }
      studentHasParents.put(student.getId(), !studentParents.isEmpty());
      studentParentsJSON.put(student.getId(), arr);
      
      studentValidations.addAll(ViewStudentTools.validate(student));

      try {
        StudentTOR tor = StudentTORController.constructStudentTOR(student, true);
        subjectCredits.put(student.getId(), tor);
      } catch (Exception ex) {
        logger.log(Level.SEVERE, String.format("Failed to construct TOR for student %d", student.getId()), ex);
      }
    }

    ObjectMapper mapper = new ObjectMapper();
    StringWriter writer = new StringWriter();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    try {
      mapper.writeValue(writer, subjectCredits);

      String requestStr = writer.toString();
      setJsDataVariable(pageRequestContext, "subjectCredits", requestStr);
    } catch (JsonGenerationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    // Audit (not logging when returning to this page after save reloads it) 

    if (!StringUtils.contains(pageRequestContext.getReferer(false), "viewstudent.page")) {
      studentDAO.auditView(personId, null, "View student");
    }

    constructMatriculationTabContent(person, pageRequestContext);
    
    setJsDataVariable(pageRequestContext, "studentAssessments", studentAssessmentsJSON.toString());
    setJsDataVariable(pageRequestContext, "linkedCourseAssessments", linkedCourseAssessments.toString());
    setJsDataVariable(pageRequestContext, "linkedTransferCredits", linkedTransferCredits.toString());
    setJsDataVariable(pageRequestContext, "studentFiles", studentFiles.toString());
    setJsDataVariable(pageRequestContext, "studentReports", studentReportsJSON.toString());
    setJsDataVariable(pageRequestContext, "curriculums", curriculumsJSON.toString());
    setJsDataVariable(pageRequestContext, "studentVariables", studentVariablesJSON.toString());
    setJsDataVariable(pageRequestContext, "studentParents", studentParentsJSON.toString());
    setJsDataVariable(pageRequestContext, "studentParentInvitations", studentParentInvitationsJSON.toString());
    
    pageRequestContext.getRequest().setAttribute("studentCards", studentCards);
    pageRequestContext.getRequest().setAttribute("students", students);
    pageRequestContext.getRequest().setAttribute("courses", courseStudents);
    pageRequestContext.getRequest().setAttribute("contactEntries", contactEntries);
    pageRequestContext.getRequest().setAttribute("contactEntryComments", contactEntryComments);
    pageRequestContext.getRequest().setAttribute("transferCredits", transferCredits);
    pageRequestContext.getRequest().setAttribute("courseAssessments", courseAssessments);
    pageRequestContext.getRequest().setAttribute("studentGroups", studentGroups);
    pageRequestContext.getRequest().setAttribute("studentProjects", studentProjects);
    pageRequestContext.getRequest().setAttribute("studentProjectModules", studentProjectModules);
    pageRequestContext.getRequest().setAttribute("studentProjectSubjectCourses", studentProjectSubjectCourses);
    pageRequestContext.getRequest().setAttribute("courseAssessmentsByCourseStudent", courseAssessmentsByCourseStudent);
    pageRequestContext.getRequest().setAttribute("studentHasImage", studentHasImage);
    pageRequestContext.getRequest().setAttribute("courseAssessmentRequests", courseAssessmentRequests);
    pageRequestContext.getRequest().setAttribute("studentLodgingPeriods", studentLodgingPeriods);
    pageRequestContext.getRequest().setAttribute("studentStudyPeriods", studentStudyPeriods);
    pageRequestContext.getRequest().setAttribute("studentMatriculationEnrollments", studentMatriculationEnrollments);
    pageRequestContext.getRequest().setAttribute("studentHasParents", studentHasParents);
    pageRequestContext.getRequest().setAttribute("studentHasParentInvitations", studentHasParentInvitations);
    pageRequestContext.getRequest().setAttribute("studentValidations", studentValidations);
    
    pageRequestContext.getRequest().setAttribute("koskiPersonURL", koskiController.getVirkailijaUrl());
    pageRequestContext.getRequest().setAttribute("koskiPersonOID", personVariableDAO.findByPersonAndKey(person, KoskiConsts.VariableNames.KOSKI_HENKILO_OID));
    pageRequestContext.getRequest().setAttribute("koskiStudentOIDs", koskiStudentOIDs);
    
    pageRequestContext.getRequest().setAttribute("hasPersonVariables", CollectionUtils.isNotEmpty(personVariableKeys));

    pageRequestContext.setIncludeJSP("/templates/students/viewstudent.jsp");
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "students.viewStudent.breadcrumb");
  }

  /**
   * Loads all the matriculation tab data for the view.
   * 
   * The persons latest(/active) student is used for curriculum and study degree purposes.
   * The student must have
   * - OPS 2021
   * - Lukio education type (in study programme category)
   * 
   * @param person
   * @param pageRequestContext
   */
  private void constructMatriculationTabContent(Person person, PageRequestContext pageRequestContext) {
    Student latestStudent = person.getLatestStudent();
    String code = latestStudent.getStudyProgramme() != null &&
        latestStudent.getStudyProgramme().getCategory() !=  null &&
        latestStudent.getStudyProgramme().getCategory().getEducationType() != null &&
        latestStudent.getStudyProgramme().getCategory().getEducationType().getCode() != null
        ? latestStudent.getStudyProgramme().getCategory().getEducationType().getCode() : null;
    boolean isHighSchoolStudent = StringUtils.equalsIgnoreCase(PyramusConsts.STUDYPROGRAMME_LUKIO, code);
    
    if (!isHighSchoolStudent ||
        latestStudent == null ||
        latestStudent.getCurriculum() == null ||
        !PyramusConsts.OPS_2021.equals(latestStudent.getCurriculum().getName())) {
      return;
    }

    TORCurriculum torCurriculum;
    try {
      torCurriculum = StudentTORController.getCurriculum(latestStudent);
    } catch (Exception e) {
      logger.log(Level.SEVERE, String.format("Couldn't load TORCurriculum for student %d", latestStudent.getId()), e);
      return;
    }

    StudentTOR studentTOR;
    try {
      studentTOR = StudentTORController.constructStudentTOR(latestStudent, torCurriculum);
    } catch (Exception e) {
      logger.log(Level.SEVERE, String.format("Couldn't load StudentTOR for student %d", latestStudent.getId()), e);
      return;
    }

    MatriculationExamEnrollmentDAO matriculationExamEnrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamAttendanceDAO matriculationExamAttendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    MatriculationGradeDAO matriculationGradeDAO = DAOFactory.getInstance().getMatriculationGradeDAO();
    List<MatriculationExamEnrollment> enrollments = matriculationExamEnrollmentDAO.listByPerson(person);

    // This is used for subject code mappings
    List<YTLAineKoodi> mapping = YTLController.readMapping();

    // Map a term (year+term) to a bean containing the term's enrollments and grades
    Map<String, MatriculationEnrollmentBean> termBeans = new HashMap<>();

    /*
     * Populate MatriculationExamEnrollments to termBeans. We skip the grades
     * at this point as it makes it easier to go through the MatriculationGrades.
     */
    for (MatriculationExamEnrollment enrollment : enrollments) {
      if (enrollment.getExam() == null || enrollment.getExam().getExamTerm() == null || enrollment.getExam().getExamYear() == null) {
        // Skip enrollment that doesn't have term or year defined - although they always should
        continue;
      }
      
      String term = enrollment.getExam().getExamTerm().name() + enrollment.getExam().getExamYear();
      
      String studyProgrammeName = enrollment.getStudent() != null && enrollment.getStudent().getStudyProgramme() != null ? enrollment.getStudent().getStudyProgramme().getName() : null;
      MatriculationEnrollmentBean termBean = new MatriculationEnrollmentBean(enrollment.getExam().getExamYear(), enrollment.getExam().getExamTerm(), enrollment.getState(), studyProgrammeName);
      termBeans.put(term, termBean);

      List<MatriculationExamAttendance> attendances = matriculationExamAttendanceDAO.listByEnrollmentAndStatus(enrollment, MatriculationExamAttendanceStatus.ENROLLED);
      
      for (MatriculationExamAttendance attendance : attendances) {
        MatriculationAttendanceBean attendanceBean = assembleAttendance(attendance.getSubject(), torCurriculum, studentTOR, mapping);
        if (attendanceBean != null) {
          termBean.addAttendance(attendanceBean);
        }
      }
    }

    /*
     * List the MatriculationGrades for the person and add them to the
     * termBeans list. If there is a bean already, attach the grade to that.
     */
    List<MatriculationGrade> personGrades = matriculationGradeDAO.listBy(person);
    for (MatriculationGrade matriculationGrade : personGrades) {
      String term = matriculationGrade.getTerm().name() + matriculationGrade.getYear();
      MatriculationExamGrade grade = matriculationGrade != null ? matriculationGrade.getGrade() : null;
      LocalDate gradeDate = matriculationGrade != null ? matriculationGrade.getGradeDate() : null;
      
      MatriculationEnrollmentBean termBean = termBeans.get(term);
      if (termBean == null) {
        // If we have to make a new term bean based on MatriculationGrade, the following fields will be null - take into account
        String studyProgrammeName = null;
        MatriculationExamEnrollmentState state = null;
        termBeans.put(term, termBean = new MatriculationEnrollmentBean(matriculationGrade.getYear(), matriculationGrade.getTerm(), state , studyProgrammeName));
      }
      
      MatriculationAttendanceBean attendanceBean = termBean.findAttendance(matriculationGrade.getSubject());
      if (attendanceBean == null) {
        attendanceBean = assembleAttendance(matriculationGrade.getSubject(), torCurriculum, studentTOR, mapping);
        if (attendanceBean != null) {
          termBean.addAttendance(attendanceBean);
        }
      }
      
      if (attendanceBean != null) {
        attendanceBean.setGrade(grade);
        attendanceBean.setGradeDate(gradeDate);
      }
    }

    Messages messages = Messages.getInstance();
    Locale locale = pageRequestContext.getRequest().getLocale();
    List<TermOption> termOptions = new ArrayList<>();
    for (int yearind = Calendar.getInstance().get(Calendar.YEAR) + 4; yearind >= 2016; yearind--) {
      String value;
      boolean enabled;

      value = MatriculationExamTerm.AUTUMN.name() + String.valueOf(yearind);
      enabled = !termBeans.containsKey(value);
      termOptions.add(new TermOption(yearind, MatriculationExamTerm.AUTUMN.name(), messages.getText(locale, "terms.seasons.autumn") + " " + String.valueOf(yearind), enabled));

      value = MatriculationExamTerm.SPRING.name() + String.valueOf(yearind);
      enabled = !termBeans.containsKey(value);
      termOptions.add(new TermOption(yearind, MatriculationExamTerm.SPRING.name(), messages.getText(locale, "terms.seasons.spring") + " " + String.valueOf(yearind), enabled));
    }

    Collection<MatriculationEnrollmentBean> enrollmentBeans = termBeans.values();
    
    pageRequestContext.getRequest().setAttribute("termOptions", termOptions);   
    pageRequestContext.getRequest().setAttribute("matriculationExamTerms", enrollmentBeans);
  }

  private MatriculationAttendanceBean assembleAttendance(MatriculationExamSubject subject, TORCurriculum torCurriculum, StudentTOR studentTOR, List<YTLAineKoodi> mapping) {
    List<MatriculationAttendanceModuleBean> modules = new ArrayList<>();
    
    String subjectCode = YTLController.examSubjectToSubjectCode(subject, mapping);
    
    if (subjectCode == null) {
      logger.log(Level.WARNING, String.format("Couldn't map subject %s", subject));
      return null;
    }

    // torCurriculumSubject may be null if the student has matriculation attendance to a subject that's not in the curriculum
    TORCurriculumSubject torCurriculumSubject = torCurriculum.getSubjectByCode(subjectCode);

    // torSubject may be null if the student has no credits from the subject
    TORSubject torSubject = studentTOR.findSubject(subjectCode);
    
    // These count the included subjects also
    Double sumMandatoryModuleLength = torCurriculumSubject != null ? (double) torCurriculumSubject.getMandatoryModuleLengthSumWithIncludedModules(torCurriculum) : 0d;
    Double sumCompletedMandatoryModuleLength = torSubject != null ? sumCompletedMandatoryModuleLength = torSubject.getMandatoryCreditPointsCompletedWithIncludedSubjects(studentTOR, torCurriculum) : 0d;

    if (torCurriculumSubject != null) {
      for (TORCurriculumModule torCurriculumModule : torCurriculumSubject.getModules()) {
        String moduleName = torCurriculumSubject.getCode() + torCurriculumModule.getCourseNumber() + " " + torCurriculumModule.getName();
  
        Date gradeDate = null;
        String gradeName = null;
        
        if (torSubject != null) {
          TORCourse torCourse = torSubject.findCourse(torCurriculumModule.getCourseNumber());
          if (torCourse != null && torCourse.getLatestCredit() != null) {
            gradeDate = torCourse.getLatestCredit().getDate();
            gradeName = torCourse.getLatestCredit().getGradeName();
          }
        }
        
        modules.add(new MatriculationAttendanceModuleBean(subjectCode, torCurriculumModule.getCourseNumber(), moduleName, gradeName, gradeDate));
      }
  
      /*
       * If the subject has included subjects, we still have to collect the modules separately here.
       * Some summary functions already count them in, but due to lack of connections, we can't ask
       * the subject to list all included modules.
       */
      if (CollectionUtils.isNotEmpty(torCurriculumSubject.getIncludedSubjects())) {
        for (String includedSubject : torCurriculumSubject.getIncludedSubjects()) {
          TORCurriculumSubject inclTorCurriculumSubject = torCurriculum.getSubjectByCode(includedSubject);
          TORSubject inclTorSubject = studentTOR.findSubject(includedSubject);
  
          for (TORCurriculumModule torCurriculumModule : inclTorCurriculumSubject.getModules()) {
            String moduleName = inclTorCurriculumSubject.getCode() + torCurriculumModule.getCourseNumber() + " " + torCurriculumModule.getName();
  
            Date gradeDate = null;
            String gradeName = null;
            
            if (inclTorSubject != null) {
              TORCourse torCourse = inclTorSubject.findCourse(torCurriculumModule.getCourseNumber());
              if (torCourse != null && torCourse.getLatestCredit() != null) {
                gradeDate = torCourse.getLatestCredit().getDate();
                gradeName = torCourse.getLatestCredit().getGradeName();
              }
            }
            
            modules.add(new MatriculationAttendanceModuleBean(includedSubject, torCurriculumModule.getCourseNumber(), moduleName, gradeName, gradeDate));
          }
        }
      }
    }
    
    // Sort modules by course number and subject code
    modules.sort(Comparator.comparing(MatriculationAttendanceModuleBean::getCourseNumber)
        .thenComparing(Comparator.nullsFirst(Comparator.comparing(MatriculationAttendanceModuleBean::getSubjectCode))));

    String subjectName = torCurriculumSubject != null ? torCurriculumSubject.getName() : subjectCode;
    return new MatriculationAttendanceBean(subject, subjectName, sumMandatoryModuleLength, sumCompletedMandatoryModuleLength, modules);
  }

  public class MatriculationEnrollmentBean {
    private final List<MatriculationAttendanceBean> attendances;
    private final int year;
    private final MatriculationExamTerm term;
    private final String studyProgrammeName;
    private final MatriculationExamEnrollmentState state;
    
    public MatriculationEnrollmentBean(int year, MatriculationExamTerm term, MatriculationExamEnrollmentState state, String studyProgrammeName) {
      this.year = year;
      this.term = term;
      this.state = state;
      this.attendances = new ArrayList<>();
      this.studyProgrammeName = studyProgrammeName;
    }

    public List<MatriculationAttendanceBean> getAttendances() {
      return attendances;
    }

    public void addAttendance(MatriculationAttendanceBean attendance) {
      this.attendances.add(attendance);
    }
    
    public MatriculationAttendanceBean findAttendance(MatriculationExamSubject subject) {
      return this.attendances.stream().filter(attendance -> attendance.getSubject() == subject).findFirst().orElse(null);
    }
    
    public int getYear() {
      return year;
    }

    public MatriculationExamTerm getTerm() {
      return term;
    }

    public String getStudyProgrammeName() {
      return studyProgrammeName;
    }

    public MatriculationExamEnrollmentState getState() {
      return state;
    }
  }
  
  public class MatriculationAttendanceBean {
    private final List<MatriculationAttendanceModuleBean> modules;
    private final Double sumMandatoryModuleLength;
    private final Double sumCompletedMandatoryModuleLength;
    private final MatriculationExamSubject subject;
    private final String subjectName;
    private MatriculationExamGrade grade;
    private LocalDate gradeDate;

    public MatriculationAttendanceBean(MatriculationExamSubject subject, String subjectName, Double sumMandatoryModuleLength, Double sumCompletedMandatoryModuleLength, List<MatriculationAttendanceModuleBean> modules) {
      this.subject = subject;
      this.subjectName = subjectName;
      this.sumMandatoryModuleLength = sumMandatoryModuleLength;
      this.sumCompletedMandatoryModuleLength = sumCompletedMandatoryModuleLength;
      this.modules = modules;
    }
    
    public Double getSumMandatoryModuleLength() {
      return sumMandatoryModuleLength;
    }

    public Double getSumCompletedMandatoryModuleLength() {
      return sumCompletedMandatoryModuleLength;
    }

    public List<MatriculationAttendanceModuleBean> getModules() {
      return modules;
    }

    public String getSubjectName() {
      return subjectName;
    }

    public MatriculationExamGrade getGrade() {
      return grade;
    }

    public void setGrade(MatriculationExamGrade grade) {
      this.grade = grade;
    }

    public LocalDate getGradeDate() {
      return gradeDate;
    }

    public void setGradeDate(LocalDate gradeDate) {
      this.gradeDate = gradeDate;
    }

    public MatriculationExamSubject getSubject() {
      return subject;
    }
  }
  
  public class MatriculationAttendanceModuleBean {
    private final String name;
    private final String gradeName;
    private final Date gradeDate;
    private final String subjectCode;
    private final int courseNumber;

    public MatriculationAttendanceModuleBean(String subjectCode, int courseNumber, String name, String gradeName, Date gradeDate) {
      this.subjectCode = subjectCode;
      this.courseNumber = courseNumber;
      this.name = name;
      this.gradeName = gradeName;
      this.gradeDate = gradeDate;
    }
    
    public String getName() {
      return name;
    }

    public String getGradeName() {
      return gradeName;
    }

    public Date getGradeDate() {
      return gradeDate;
    }

    public String getSubjectCode() {
      return subjectCode;
    }

    public int getCourseNumber() {
      return courseNumber;
    }
  }
  
  public class StudentProjectBean {
    private final StudentProject studentProject;
    private final int passedOptionalModuleCount;
    private final int mandatoryModuleCount;
    private final int optionalModuleCount;
    private final int passedMandatoryModuleCount;
    private final List<ProjectAssessment> assessments;

    public StudentProjectBean(StudentProject studentProject, int mandatoryModuleCount, int optionalModuleCount,
        int passedMandatoryModuleCount, int passedOptionalModuleCount, List<ProjectAssessment> assessments) {
      this.studentProject = studentProject;
      this.mandatoryModuleCount = mandatoryModuleCount;
      this.optionalModuleCount = optionalModuleCount;
      this.passedOptionalModuleCount = passedOptionalModuleCount;
      this.passedMandatoryModuleCount = passedMandatoryModuleCount;
      this.assessments = assessments;
    }

    public StudentProject getStudentProject() {
      return studentProject;
    }

    public int getPassedOptionalModuleCount() {
      return passedOptionalModuleCount;
    }

    public int getMandatoryModuleCount() {
      return mandatoryModuleCount;
    }

    public int getOptionalModuleCount() {
      return optionalModuleCount;
    }

    public int getPassedMandatoryModuleCount() {
      return passedMandatoryModuleCount;
    }

    public List<ProjectAssessment> getAssessments() {
      return assessments;
    }
  }
  
  public class StudentProjectModuleBean<T> {
    private final T studentProjectModule;
    private final boolean hasPassingGrade;
    private final List<CourseStudent> courseStudents;
    private final List<TransferCredit> transferCredits;

    public StudentProjectModuleBean(T studentProjectModule, boolean hasPassingGrade, 
        List<CourseStudent> courseStudents, List<TransferCredit> transferCredits) {
      this.studentProjectModule = studentProjectModule;
      this.hasPassingGrade = hasPassingGrade;
      this.courseStudents = courseStudents;
      this.transferCredits = transferCredits;
    }

    public T getStudentProjectModule() {
      return studentProjectModule;
    }

    public boolean isHasPassingGrade() {
      return hasPassingGrade;
    }

    public List<CourseStudent> getCourseStudents() {
      return courseStudents;
    }

    public List<TransferCredit> getTransferCredits() {
      return transferCredits;
    }
  }
  
  private String getSubjectText(Subject subject, Locale locale) {
    if (subject == null)
      return null;
    
    String subjectName = subject.getName();
    String subjectCode = subject.getCode();
    String subjectEducationType = subject.getEducationType() != null ? subject.getEducationType().getName() : null;
    
    String localizedSubject = subjectName;
    
    if (StringUtils.isNotBlank(subjectCode) && StringUtils.isNotBlank(subjectEducationType)) {
      localizedSubject = Messages.getInstance().getText(locale, 
          "generic.subjectFormatterWithEducationType", new Object[] {
        subjectCode,
        subjectName,
        subjectEducationType
      });
    } else if (StringUtils.isNotBlank(subjectEducationType)) {
      localizedSubject = Messages.getInstance().getText(locale, 
          "generic.subjectFormatterNoSubjectCode", new Object[] {
        subjectName,
        subjectEducationType
      });
    } else if (StringUtils.isNotBlank(subjectCode)) {
      localizedSubject = Messages.getInstance().getText(locale, 
          "generic.subjectFormatterNoEducationType", new Object[] {
        subjectCode,
        subjectName
      });
    }

    return localizedSubject;
  }
  
  public class TermOption {
    public TermOption(int year, String term, String displayText, boolean enabled) {
      this.year = year;
      this.term = term;
      this.displayText = displayText;
      this.enabled = enabled;
    }
    
    public String getDisplayText() {
      return displayText;
    }

    public boolean isEnabled() {
      return enabled;
    }

    public String getTerm() {
      return term;
    }

    public int getYear() {
      return year;
    }

    private final int year;
    private final String term;
    private final String displayText;
    private final boolean enabled;
  }
}

