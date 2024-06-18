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

    return warnings;
  }
  
  
}
