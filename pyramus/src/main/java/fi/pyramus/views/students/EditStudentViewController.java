package fi.pyramus.views.students;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.ContactURLTypeDAO;
import fi.pyramus.dao.base.LanguageDAO;
import fi.pyramus.dao.base.MunicipalityDAO;
import fi.pyramus.dao.base.NationalityDAO;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.dao.grading.CourseAssessmentDAO;
import fi.pyramus.dao.grading.CreditLinkDAO;
import fi.pyramus.dao.grading.TransferCreditDAO;
import fi.pyramus.dao.students.AbstractStudentDAO;
import fi.pyramus.dao.students.StudentActivityTypeDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.students.StudentEducationalLevelDAO;
import fi.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.pyramus.dao.students.StudentVariableKeyDAO;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.ContactURLType;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentVariableKey;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

public class EditStudentViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext pageRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    StudentActivityTypeDAO studentActivityTypeDAO = DAOFactory.getInstance().getStudentActivityTypeDAO();
    StudentEducationalLevelDAO studentEducationalLevelDAO = DAOFactory.getInstance().getStudentEducationalLevelDAO();
    StudentExaminationTypeDAO studentExaminationTypeDAO = DAOFactory.getInstance().getStudentExaminationTypeDAO();
    StudentStudyEndReasonDAO studyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    StudentVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getStudentVariableKeyDAO();
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

    Long abstractStudentId = pageRequestContext.getLong("abstractStudent");
    AbstractStudent abstractStudent = abstractStudentDAO.findById(abstractStudentId);
    
    List<Student> students = studentDAO.listByAbstractStudent(abstractStudent);
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
    
    Map<Long, String> studentTags = new HashMap<Long, String>();
    Map<Long, Boolean> studentHasCredits = new HashMap<Long, Boolean>();
    
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
    }
    
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

    List<StudentVariableKey> studentVariableKeys = variableKeyDAO.listUserEditableStudentVariableKeys();
    Collections.sort(studentVariableKeys, new StringAttributeComparator("getVariableName"));
    
    pageRequestContext.getRequest().setAttribute("tags", studentTags);
    pageRequestContext.getRequest().setAttribute("abstractStudent", abstractStudent);
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
    pageRequestContext.getRequest().setAttribute("studyProgrammes", studyProgrammeDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("studyEndReasons", studyEndReasonDAO.listByParentReason(null));
    pageRequestContext.getRequest().setAttribute("variableKeys", studentVariableKeys);
    pageRequestContext.getRequest().setAttribute("studentHasCredits", studentHasCredits);
    
    pageRequestContext.setIncludeJSP("/templates/students/editstudent.jsp");
  }

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
    return Messages.getInstance().getText(locale, "students.editStudent.breadcrumb");
  }

}
