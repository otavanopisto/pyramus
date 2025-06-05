package fi.otavanopisto.pyramus.views.students;

import java.util.ArrayList;
import java.util.List;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.framework.DateUtils;
import fi.otavanopisto.pyramus.views.students.ViewStudentValidationWarning.ViewStudentValidationType;
import fi.otavanopisto.pyramus.views.students.ViewStudentValidationWarning.ViewStudentValidationWarningSeverity;

public class ViewStudentTools {

  public static List<ViewStudentValidationWarning> validate(Student student) {
    List<ViewStudentValidationWarning> warnings = new ArrayList<>();

    /*
     * Validate that studyEndDate is not before studyStartDate if they both exist.
     */
    if (student.getStudyStartDate() != null && student.getStudyEndDate() != null) {
      // studyEndDate should not be before studyStartDate
      if (student.getStudyEndDate().before(student.getStudyStartDate())) {
        warnings.add(new ViewStudentValidationWarning(student, ViewStudentValidationType.STUDYENDDATE_BEFORE_STARTDATE,
            ViewStudentValidationWarningSeverity.ERROR));
      }
    }

    /*
     * Validate that study periods do not exit the timeframe between
     * studyStartDate and studyEndDate. isWithin allows either date
     * to not exist, if they don't, they just aren't compared against.
     */
    StudentStudyPeriodDAO studentStudyPeriodDAO = DAOFactory.getInstance().getStudentStudyPeriodDAO();
    List<StudentStudyPeriod> studyPeriods = studentStudyPeriodDAO.listByStudent(student);
    for (StudentStudyPeriod studyPeriod : studyPeriods) {
      if ((studyPeriod.getBegin() != null && !DateUtils.isWithin(studyPeriod.getBegin(), student.getStudyStartDate(), student.getStudyEndDate())) ||
          (studyPeriod.getEnd() != null && !DateUtils.isWithin(studyPeriod.getEnd(), student.getStudyStartDate(), student.getStudyEndDate()))) {
        warnings.add(new ViewStudentValidationWarning(student, ViewStudentValidationType.STUDYPERIOD_OUTSIDE_STUDYTIME,
            ViewStudentValidationWarningSeverity.ERROR));
      }
    }
    
    /*
     * Validate that default student is both set and doesn't point to an archived student.
     */
    if (student.getPerson() == null || student.getPerson().getDefaultUser() == null) {
      warnings.add(new ViewStudentValidationWarning(student, ViewStudentValidationType.DEFAULT_USER_NOT_SET,
          ViewStudentValidationWarningSeverity.ERROR));
    }
    else {
      if (student.getPerson().getDefaultUser().getArchived() == null || Boolean.TRUE.equals(student.getPerson().getDefaultUser().getArchived())) {
        warnings.add(new ViewStudentValidationWarning(student, ViewStudentValidationType.DEFAULT_USER_ARCHIVED,
            ViewStudentValidationWarningSeverity.ERROR));
      }
    }
    
    
    /*
     * Validate that a student doesn't have overlapping study rights for the same educational level
     */
    
    List<Student> students = student.getPerson().getStudents();
    
    for (Student student1 : students) {
      for (Student student2 : students) {
        // Skip if archived student
        if (student1.getArchived() || student2.getArchived()) {
          continue;
        }
        // Skip if trying to compare two of the same student
        if (student1.getId() == student2.getId()) {
          continue;
        }
        
        // Skip if the education levels don’t match
        String s1EducationTypeCode = null;
        String s2EducationTypeCode = null;
        
        if (student1.getStudyProgramme() != null) {
          if (student1.getStudyProgramme().getCategory() != null) {
            if (student1.getStudyProgramme().getCategory().getEducationType() != null) {
              s1EducationTypeCode = student1.getStudyProgramme().getCategory().getEducationType().getCode();
            }
          }
        }
        
        if (student2.getStudyProgramme() != null) {
          if (student2.getStudyProgramme().getCategory() != null) {
            if (student2.getStudyProgramme().getCategory().getEducationType() != null) {
              s2EducationTypeCode = student2.getStudyProgramme().getCategory().getEducationType().getCode();
            }
          }
        }
        
        if (s1EducationTypeCode != null || s2EducationTypeCode != null) {
          if (s1EducationTypeCode != s2EducationTypeCode) {
            continue;
          }
        }
        
        // The start dates of the studies must be set
        if (student1.getStudyStartDate() != null && student2.getStudyStartDate() != null) {
          // student1 alkupvm ennen kuin student2, mutta student1 loppupvm ei asetettu -> warning
          if (student1.getStudyEndDate() == null) {
            if (student1.getStudyStartDate().before(student2.getStudyStartDate())) {
              warnings.add(new ViewStudentValidationWarning(student, ViewStudentValidationType.OVERLAPPING_STUDY_RIGHTS,
                  ViewStudentValidationWarningSeverity.ERROR));
            }
          } else {
            // student1 alkaa ennen kuin student2, mutta student1 loppupvm vasta student2 alun jälkeen -> warning
            if (DateUtils.isWithin(student2.getStudyStartDate(), student1.getStudyStartDate(), student1.getStudyEndDate())){
              warnings.add(new ViewStudentValidationWarning(student, ViewStudentValidationType.OVERLAPPING_STUDY_RIGHTS,
                  ViewStudentValidationWarningSeverity.ERROR));
            }
          }
        }
      }
    }
    
    return warnings;
  }
  
  
}
