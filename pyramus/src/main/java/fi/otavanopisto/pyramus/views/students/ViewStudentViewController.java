package fi.otavanopisto.pyramus.views.students;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentRequestDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.ProjectAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportDAO;
import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryCommentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.students.StudentImageDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
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
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectModule;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportContextType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryComment;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

/**
 * ViewController for editing student information.
 * 
 * @author antti.viljakainen
 */
public class ViewStudentViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Returns allowed roles for this page. Allowed are UserRole.MANAGER and UserRole.ADMINISTRATOR.
   * 
   * @return allowed roles
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
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

    Long personId = pageRequestContext.getLong("person");
    
    Person person = personDAO.findById(personId);
    
    pageRequestContext.getRequest().setAttribute("person", person);

    StaffMember staffMember = staffMemberDAO.findByPerson(person);
    pageRequestContext.getRequest().setAttribute("staffMember", staffMember);
    
    List<Student> students = studentDAO.listByPerson(person);
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
    
    Map<Long, Boolean> studentHasImage = new HashMap<Long, Boolean>();
    Map<Long, List<CourseStudent>> courseStudents = new HashMap<Long, List<CourseStudent>>();
    Map<Long, List<StudentContactLogEntry>> contactEntries = new HashMap<Long, List<StudentContactLogEntry>>();
    Map<Long, List<TransferCredit>> transferCredits = new HashMap<Long, List<TransferCredit>>();
    Map<Long, List<CourseAssessment>> courseAssessments = new HashMap<Long, List<CourseAssessment>>();
    Map<Long, CourseAssessmentRequest> courseAssessmentRequests = new HashMap<Long, CourseAssessmentRequest>();
    Map<Long, List<StudentGroup>> studentGroups = new HashMap<Long, List<StudentGroup>>();
    Map<Long, List<StudentProjectBean>> studentProjects = new HashMap<Long, List<StudentProjectBean>>();
    Map<Long, CourseAssessment> courseAssessmentsByCourseStudent = new HashMap<Long, CourseAssessment>();
    // StudentProject.id -> List of module beans
    Map<Long, List<StudentProjectModuleBean>> studentProjectModules = new HashMap<Long, List<StudentProjectModuleBean>>();
    final Map<Long, List<StudentContactLogEntryComment>> contactEntryComments = new HashMap<Long, List<StudentContactLogEntryComment>>();
    
    JSONObject linkedCourseAssessments = new JSONObject();
    JSONObject linkedTransferCredits = new JSONObject();
    JSONObject studentFiles = new JSONObject();
    JSONArray studentReportsJSON = new JSONArray();
    
    List<Report> studentReports = reportDAO.listByContextType(ReportContextType.Student);
    Collections.sort(studentReports, new StringAttributeComparator("getName"));
    
    for (Report report : studentReports) {
      JSONObject obj = new JSONObject();
      obj.put("id", report.getId().toString());
      obj.put("name", report.getName());
      studentReportsJSON.add(obj);
    }
    
    for (int i = 0; i < students.size(); i++) {
    	Student student = students.get(i);
    	
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

        if (courseAssessmentRequestsByCourseStudent.size() > 0) {
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
        
        String subjectName = getSubjectText(courseAssessment.getCourseStudent().getCourse().getSubject(), pageRequestContext.getRequest().getLocale());
        
        JSONObject obj = new JSONObject();
        obj.put("creditLinkId", linkedCourseAssessment.getId().toString());
        obj.put("courseStudentId", courseAssessment.getCourseStudent().getId().toString());

        obj.put("courseName", courseAssessment.getCourseStudent().getCourse().getName());
        obj.put("subjectName", subjectName);
        obj.put("creditDate", courseAssessment.getDate().getTime());
        obj.put("courseLength", courseAssessment.getCourseStudent().getCourse().getCourseLength().getUnits().toString());
        obj.put("courseLengthUnitName", courseAssessment.getCourseStudent().getCourse().getCourseLength().getUnit().getName());
        
        obj.put("gradeName", courseAssessment.getGrade() != null ? courseAssessment.getGrade().getName() : null);
        obj.put("gradingScaleName", courseAssessment.getGrade() != null ? courseAssessment.getGrade().getGradingScale().getName() : null);
        obj.put("assessingUserName", courseAssessment.getAssessor().getFullName());
        
        arr.add(obj);
      }
      
      if (arr.size() > 0)
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
        obj.put("creditDate", transferCredit.getDate().getTime());
        obj.put("courseLength", transferCredit.getCourseLength().getUnits().toString());
        obj.put("courseLengthUnitName", transferCredit.getCourseLength().getUnit().getName());
        
        obj.put("gradeName", transferCredit.getGrade() != null ? transferCredit.getGrade().getName() : null);
        obj.put("gradingScaleName", transferCredit.getGrade() != null ? transferCredit.getGrade().getGradingScale().getName() : null);
        obj.put("assessingUserName", transferCredit.getAssessor().getFullName());
        
        arr.add(obj);
      }
      
      if (arr.size() > 0)
        linkedTransferCredits.put(student.getId(), arr);
      
      /**
       * Project beans setup
       */
      List<StudentProject> studentsStudentProjects = studentProjectDAO.listByStudent(student);
      List<StudentProjectBean> studentProjectBeans = new ArrayList<StudentProjectBean>();
      for (StudentProject studentProject : studentsStudentProjects) {
        int mandatoryModuleCount = 0;
        int optionalModuleCount = 0;
        int passedMandatoryModuleCount = 0;
        int passedOptionalModuleCount = 0;
        
        List<StudentProjectModuleBean> studentProjectModuleBeans = new ArrayList<StudentProjectModuleBean>();
        
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
          }
        }
        
        for (StudentProjectModule studentProjectModule : studentProject.getStudentProjectModules()) {
          boolean hasPassingGrade = false;

          List<CourseStudent> projectCourseCourseStudentList = new ArrayList<CourseStudent>();
          List<TransferCredit> projectCourseTransferCreditList = new ArrayList<TransferCredit>();

          // Find out if there is a course that has passing grade for the module
          
          for (CourseAssessment assessment : allStudentCourseAssessments) {
            if (assessment.getCourseStudent().getCourse().getModule().getId().equals(studentProjectModule.getModule().getId())) {
              projectCourseCourseStudentList.add(assessment.getCourseStudent());
              if (assessment.getGrade() != null && assessment.getGrade().getPassingGrade())
                hasPassingGrade = true; 
            }
          }
          
          if ((studentProjectModule.getModule().getCourseNumber() != null) && (studentProjectModule.getModule().getCourseNumber() != -1) && (studentProjectModule.getModule().getSubject() != null)) {
            for (TransferCredit tc : allStudentTransferCredits) {
              if ((tc.getCourseNumber() != null) && (tc.getCourseNumber() != -1) && (tc.getSubject() != null)) {
                if (tc.getCourseNumber().equals(studentProjectModule.getModule().getCourseNumber()) && tc.getSubject().equals(studentProjectModule.getModule().getSubject())) {
                  projectCourseTransferCreditList.add(tc);
                  if (tc.getGrade() != null && tc.getGrade().getPassingGrade())
                    hasPassingGrade = true;
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
          
          StudentProjectModuleBean moduleBean = new StudentProjectModuleBean(studentProjectModule, hasPassingGrade, projectCourseCourseStudentList, projectCourseTransferCreditList);
          studentProjectModuleBeans.add(moduleBean);
        }

        // Add ModuleBeans to response
        studentProjectModules.put(studentProject.getId(), studentProjectModuleBeans);
        
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
      if (arr.size() > 0)
        studentFiles.put(student.getId(), arr);
      
      // Student Image
      studentHasImage.put(student.getId(), imageDAO.findStudentHasImage(student));

      courseStudents.put(student.getId(), courseStudentsByStudent);
      courseAssessments.put(student.getId(), courseAssessmentsByStudent);
      contactEntries.put(student.getId(), listStudentContactEntries);
      transferCredits.put(student.getId(), transferCreditsByStudent);
      studentGroups.put(student.getId(), studentGroupDAO.listByStudent(student));
      studentProjects.put(student.getId(), studentProjectBeans);
    }

    setJsDataVariable(pageRequestContext, "linkedCourseAssessments", linkedCourseAssessments.toString());
    setJsDataVariable(pageRequestContext, "linkedTransferCredits", linkedTransferCredits.toString());
    setJsDataVariable(pageRequestContext, "studentFiles", studentFiles.toString());
    setJsDataVariable(pageRequestContext, "studentReports", studentReportsJSON.toString());
    
    pageRequestContext.getRequest().setAttribute("students", students);
    pageRequestContext.getRequest().setAttribute("courses", courseStudents);
    pageRequestContext.getRequest().setAttribute("contactEntries", contactEntries);
    pageRequestContext.getRequest().setAttribute("contactEntryComments", contactEntryComments);
    pageRequestContext.getRequest().setAttribute("transferCredits", transferCredits);
    pageRequestContext.getRequest().setAttribute("courseAssessments", courseAssessments);
    pageRequestContext.getRequest().setAttribute("studentGroups", studentGroups);
    pageRequestContext.getRequest().setAttribute("studentProjects", studentProjects);
    pageRequestContext.getRequest().setAttribute("studentProjectModules", studentProjectModules);
    pageRequestContext.getRequest().setAttribute("courseAssessmentsByCourseStudent", courseAssessmentsByCourseStudent);
    pageRequestContext.getRequest().setAttribute("studentHasImage", studentHasImage);
    pageRequestContext.getRequest().setAttribute("courseAssessmentRequests", courseAssessmentRequests);

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
  
  public class StudentProjectModuleBean {
    private final StudentProjectModule studentProjectModule;
    private final boolean hasPassingGrade;
    private final List<CourseStudent> courseStudents;
    private final List<TransferCredit> transferCredits;

    public StudentProjectModuleBean(StudentProjectModule studentProjectModule, boolean hasPassingGrade, 
        List<CourseStudent> courseStudents, List<TransferCredit> transferCredits) {
      this.studentProjectModule = studentProjectModule;
      this.hasPassingGrade = hasPassingGrade;
      this.courseStudents = courseStudents;
      this.transferCredits = transferCredits;
    }

    public StudentProjectModule getStudentProjectModule() {
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
    
    if ((subjectCode != null) && (subjectEducationType != null)) {
      localizedSubject = Messages.getInstance().getText(locale, 
          "generic.subjectFormatterWithEducationType", new Object[] {
        subjectCode,
        subjectName,
        subjectEducationType
      });
    } else if (subjectEducationType != null) {
      localizedSubject = Messages.getInstance().getText(locale, 
          "generic.subjectFormatterNoSubjectCode", new Object[] {
        subjectName,
        subjectEducationType
      });
    } else if (subjectCode != null) {
      localizedSubject = Messages.getInstance().getText(locale, 
          "generic.subjectFormatterNoEducationType", new Object[] {
        subjectCode,
        subjectName
      });
    }

    return localizedSubject;
  }
  
}

