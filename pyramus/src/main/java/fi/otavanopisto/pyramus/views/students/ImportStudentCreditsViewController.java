package fi.otavanopisto.pyramus.views.students;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditType;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of the Create StudentGroup view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.students.CreateStudentGroupJSONRequestController
 */
public class ImportStudentCreditsViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    CreditLinkDAO creditLinkDAO = DAOFactory.getInstance().getCreditLinkDAO();
    
    Long studentId = pageRequestContext.getLong("studentId");
    
    final Student baseStudent = studentDAO.findById(studentId);
    JSONObject courseAssessmentsByStudent = new JSONObject();
    JSONObject transferCreditsByStudent = new JSONObject();
    JSONObject linkedCourseAssessments = new JSONObject();
    JSONObject linkedTransferCredits = new JSONObject();
    
    List<Student> students = studentDAO.listByPerson(baseStudent.getPerson());

    Collections.sort(students, new Comparator<Student>() {
      @Override
      public int compare(Student o1, Student o2) {
//        if (o1.equals(baseStudent))
//          return -1;
//        if (o2.equals(baseStudent))
//          return 1;
        
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
    
    
    List<Long> linkedCourseAssessmentIds = new ArrayList<Long>();
    List<Long> linkedTransferCreditIds = new ArrayList<Long>();
    
    List<CreditLink> creditLinks = creditLinkDAO.listByStudent(baseStudent);
    for (CreditLink creditLink : creditLinks) {
      switch (creditLink.getCredit().getCreditType()) {
        case CourseAssessment:
          linkedCourseAssessmentIds.add(creditLink.getCredit().getId());
        break;
        
        case TransferCredit:
          linkedTransferCreditIds.add(creditLink.getCredit().getId());
        break;
      }
    }
    
    List<CourseAssessment> baseCourseAssessments = courseAssessmentDAO.listByStudent(baseStudent);
    for (CourseAssessment courseAssessment : baseCourseAssessments)
      linkedCourseAssessmentIds.add(courseAssessment.getId());
    
    List<TransferCredit> baseTransferCredits = transferCreditDAO.listByStudent(baseStudent);
    for (TransferCredit transferCredit : baseTransferCredits)
      linkedTransferCreditIds.add(transferCredit.getId());

    for (Student student : students) {
      List<CourseAssessment> courseAssessments = courseAssessmentDAO.listByStudent(student);
      List<TransferCredit> transferCredits = transferCreditDAO.listByStudent(student);

      Collections.sort(courseAssessments, new Comparator<CourseAssessment>() {
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
      
      Collections.sort(transferCredits, new Comparator<TransferCredit>() {
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
      
      JSONArray arr = new JSONArray();
      for (CourseAssessment courseAssessment : courseAssessments) {
        if ((linkedCourseAssessmentIds.contains(courseAssessment.getId())) && (!student.getId().equals(baseStudent.getId())))
          continue;

        String subjectName = getSubjectText(courseAssessment.getCourseStudent().getCourse().getSubject(), pageRequestContext.getRequest().getLocale());
        
        JSONObject obj = new JSONObject();
        obj.put("courseStudentId", courseAssessment.getCourseStudent().getId().toString());
        obj.put("courseAssessmentId", courseAssessment.getId().toString());

        obj.put("courseName", courseAssessment.getCourseStudent().getCourse().getName());
        obj.put("subjectName", subjectName);
        obj.put("creditDate", courseAssessment.getDate().getTime());
        obj.put("courseLength", courseAssessment.getCourseStudent().getCourse().getCourseLength().getUnits().toString());
        obj.put("courseLengthUnitName", courseAssessment.getCourseStudent().getCourse().getCourseLength().getUnit().getName());
        
        obj.put("gradeName", courseAssessment.getGrade() != null ? courseAssessment.getGrade().getName() : null);
        obj.put("gradingScaleName", courseAssessment.getGrade() != null ? courseAssessment.getGrade().getGradingScale().getName() : null);
        obj.put("assessingUserName", courseAssessment.getAssessor().getFullName());
        obj.put("isLinked", linkedCourseAssessmentIds.contains(courseAssessment.getId()));
        
        arr.add(obj);
      }
      if (arr.size() > 0)
        courseAssessmentsByStudent.put(student.getId(), arr);
      
      arr = new JSONArray();
      for (TransferCredit transferCredit : transferCredits) {
        if ((linkedTransferCreditIds.contains(transferCredit.getId())) && (!student.getId().equals(baseStudent.getId())))
          continue;

        String subjectName = getSubjectText(transferCredit.getSubject(), pageRequestContext.getRequest().getLocale());
        
        JSONObject obj = new JSONObject();
        obj.put("transferCreditId", transferCredit.getId().toString());

        obj.put("courseName", transferCredit.getCourseName());
        obj.put("subjectName", subjectName);
        obj.put("creditDate", transferCredit.getDate().getTime());
        obj.put("courseLength", transferCredit.getCourseLength().getUnits().toString());
        obj.put("courseLengthUnitName", transferCredit.getCourseLength().getUnit().getName());
        
        obj.put("gradeName", transferCredit.getGrade() != null ? transferCredit.getGrade().getName() : null);
        obj.put("gradingScaleName", transferCredit.getGrade() != null ? transferCredit.getGrade().getGradingScale().getName() : null);
        obj.put("assessingUserName", transferCredit.getAssessor().getFullName());
        obj.put("isLinked", linkedTransferCreditIds.contains(transferCredit.getId()));
        
        arr.add(obj);
      }
      if (arr.size() > 0)
        transferCreditsByStudent.put(student.getId(), arr);
      
      
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
      
      arr = new JSONArray();
      for (CreditLink linkedCourseAssessment : linkedCourseAssessmentByStudent) {
        CourseAssessment courseAssessment = (CourseAssessment) linkedCourseAssessment.getCredit();

        if ((linkedCourseAssessmentIds.contains(courseAssessment.getId())) && (!student.getId().equals(baseStudent.getId())))
          continue;
        
        String subjectName = getSubjectText(courseAssessment.getCourseStudent().getCourse().getSubject(), pageRequestContext.getRequest().getLocale());
        
        JSONObject obj = new JSONObject();
        obj.put("creditLinkId", linkedCourseAssessment.getId().toString());
        obj.put("courseAssessmentId", courseAssessment.getId().toString());
        obj.put("courseStudentId", courseAssessment.getCourseStudent().getId().toString());

        obj.put("courseName", courseAssessment.getCourseStudent().getCourse().getName());
        obj.put("subjectName", subjectName);
        obj.put("creditDate", courseAssessment.getDate().getTime());
        obj.put("courseLength", courseAssessment.getCourseStudent().getCourse().getCourseLength().getUnits().toString());
        obj.put("courseLengthUnitName", courseAssessment.getCourseStudent().getCourse().getCourseLength().getUnit().getName());
        
        obj.put("gradeName", courseAssessment.getGrade() != null ? courseAssessment.getGrade().getName() : null);
        obj.put("gradingScaleName", courseAssessment.getGrade() != null ? courseAssessment.getGrade().getGradingScale().getName() : null);
        obj.put("assessingUserName", courseAssessment.getAssessor().getFullName());
        obj.put("isLinked", linkedCourseAssessmentIds.contains(courseAssessment.getId()));
        
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
        
        if ((linkedTransferCreditIds.contains(transferCredit.getId())) && (!student.getId().equals(baseStudent.getId())))
          continue;
        
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
        obj.put("isLinked", linkedTransferCreditIds.contains(transferCredit.getId()));
        
        arr.add(obj);
      }
      
      if (arr.size() > 0)
        linkedTransferCredits.put(student.getId(), arr);
    }
    
    setJsDataVariable(pageRequestContext, "courseAssessments", courseAssessmentsByStudent.toString());
    setJsDataVariable(pageRequestContext, "transferCredits", transferCreditsByStudent.toString());
    setJsDataVariable(pageRequestContext, "linkedCourseAssessments", linkedCourseAssessments.toString());
    setJsDataVariable(pageRequestContext, "linkedTransferCredits", linkedTransferCredits.toString());

    pageRequestContext.getRequest().setAttribute("baseStudent", baseStudent);
//    students.remove(baseStudent);
    pageRequestContext.getRequest().setAttribute("students", students);
    
    pageRequestContext.setIncludeJSP("/templates/students/importstudentcredits.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Creating student groups is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "students.importStudentCredits.breadcrumb");
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
