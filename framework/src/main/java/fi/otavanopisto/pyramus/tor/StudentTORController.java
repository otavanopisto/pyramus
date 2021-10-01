package fi.otavanopisto.pyramus.tor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.students.StudentSubjectGradeDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditType;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade;

public class StudentTORController {

  public static StudentTOR constructStudentTOR(Student student) throws Exception {
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    CreditLinkDAO creditLinkDAO = DAOFactory.getInstance().getCreditLinkDAO();
    
    List<CourseAssessment> courseAssessmentsByStudent = courseAssessmentDAO.listByStudent(student);
    List<TransferCredit> transferCreditsByStudent = transferCreditDAO.listByStudent(student);
    List<CreditLink> linkedCourseAssessmentByStudent = creditLinkDAO.listByStudentAndType(student, CreditType.CourseAssessment);
    List<CreditLink> linkedTransferCreditsByStudent = creditLinkDAO.listByStudentAndType(student, CreditType.TransferCredit);

    StudentTOR tor = new StudentTOR();
    TORProblems problems = tor.getProblems();

    for (CourseAssessment courseAssessment : courseAssessmentsByStudent) {
      if (courseAssessment.getCourseStudent() != null && courseAssessment.getCourseStudent().getCourse() != null) {
        Subject subject = courseAssessment.getCourseStudent().getCourse().getSubject();
        Integer courseNumber = courseAssessment.getCourseStudent().getCourse().getCourseNumber();
        Set<Curriculum> creditCurriculums = courseAssessment.getCourseStudent().getCourse().getCurriculums();
        
        EducationalLength courseEducationalLength = courseAssessment.getCourseStudent().getCourse().getCourseLength();
        Double courseLength = courseEducationalLength != null ? courseEducationalLength.getUnits() : null;
        TORCourseLengthUnit courseLengthUnit = courseEducationalLength != null ? getCourseLengthUnit(courseEducationalLength.getUnit(), problems) : null;
        
        addTORCredit(tor, student, subject, courseAssessment, courseNumber, creditCurriculums, courseLength, courseLengthUnit, problems);
      }
    }
    
    for (TransferCredit transferCredit : transferCreditsByStudent) {
      Subject subject = transferCredit.getSubject();
      Integer courseNumber = transferCredit.getCourseNumber();
      Set<Curriculum> creditCurriculums = transferCredit.getCurriculum() != null ? 
          new HashSet<>(Arrays.asList(transferCredit.getCurriculum())) : Collections.emptySet();

      EducationalLength courseEducationalLength = transferCredit.getCourseLength();
      Double courseLength = courseEducationalLength != null ? courseEducationalLength.getUnits() : null;
      TORCourseLengthUnit courseLengthUnit = courseEducationalLength != null ? getCourseLengthUnit(courseEducationalLength.getUnit(), problems) : null;
      addTORCredit(tor, student, subject, transferCredit, courseNumber, creditCurriculums, courseLength, courseLengthUnit, problems);
    }
    
    for (CreditLink linkedCourseAssessment : linkedCourseAssessmentByStudent) {
      CourseAssessment courseAssessment = (CourseAssessment) linkedCourseAssessment.getCredit();
      if (courseAssessment != null && courseAssessment.getCourseStudent() != null && courseAssessment.getCourseStudent().getCourse() != null) {
        Subject subject = courseAssessment.getCourseStudent().getCourse().getSubject();
        Integer courseNumber = courseAssessment.getCourseStudent().getCourse().getCourseNumber();
        Set<Curriculum> creditCurriculums = courseAssessment.getCourseStudent().getCourse().getCurriculums();

        EducationalLength courseEducationalLength = courseAssessment.getCourseStudent().getCourse().getCourseLength();
        Double courseLength = courseEducationalLength != null ? courseEducationalLength.getUnits() : null;
        TORCourseLengthUnit courseLengthUnit = courseEducationalLength != null ? getCourseLengthUnit(courseEducationalLength.getUnit(), problems) : null;
        addTORCredit(tor, student, subject, courseAssessment, courseNumber, creditCurriculums, courseLength, courseLengthUnit, problems);
      }
    }
    
    for (CreditLink linkedTransferCredit : linkedTransferCreditsByStudent) {
      TransferCredit transferCredit = (TransferCredit) linkedTransferCredit.getCredit();
      if (transferCredit != null) {
        Subject subject = transferCredit.getSubject();
        Integer courseNumber = transferCredit.getCourseNumber();
        Set<Curriculum> creditCurriculums = transferCredit.getCurriculum() != null ? 
            new HashSet<>(Arrays.asList(transferCredit.getCurriculum())) : Collections.emptySet();
        
        EducationalLength courseEducationalLength = transferCredit.getCourseLength();
        Double courseLength = courseEducationalLength != null ? courseEducationalLength.getUnits() : null;
        TORCourseLengthUnit courseLengthUnit = courseEducationalLength != null ? getCourseLengthUnit(courseEducationalLength.getUnit(), problems) : null;
        addTORCredit(tor, student, subject, transferCredit, courseNumber, creditCurriculums, courseLength, courseLengthUnit, problems);
      }
    }

    tor.postProcess();
    return tor;
  }
  
  private static TORCourseLengthUnit getCourseLengthUnit(EducationalTimeUnit unit, TORProblems problems) {
    TORCourseLengthUnit lengthUnit = TORCourseLengthUnit.valueOf(unit.getSymbol());
    
    if (lengthUnit == null) {
      problems.add(new TORProblem(TORProblemType.UNRESOLVABLE_LENGTHUNIT, String.format("%s (%s)", unit.getSymbol(), unit.getName())));
    }
    
    return lengthUnit;
  }

  private static void addTORCredit(StudentTOR tor, Student student, Subject subject, Credit credit, Integer courseNumber, Set<Curriculum> creditCurriculums, Double courseLength, TORCourseLengthUnit courseLengthUnit, TORProblems problems) {
    if (credit.getGrade() == null) {
      return;
    }
    
    if (student.getCurriculum() != null) {
      Long studentCurriculumId = student.getCurriculum().getId();
      
      if (!creditCurriculums.isEmpty()) {
        boolean matchingCurriculum = creditCurriculums.stream()
          .map(Curriculum::getId)
          .anyMatch(studentCurriculumId::equals);
        
        // Both student and credit have curriculums set, but they didn't match -> skip the credit
        if (!matchingCurriculum) {
          return;
        }
      }
    }
    
    Long educationTypeId = subject.getEducationType() != null ? subject.getEducationType().getId() : null;
    fi.otavanopisto.pyramus.tor.Subject subjectModel = new fi.otavanopisto.pyramus.tor.Subject(
        subject.getId(), subject.getCode(), subject.getName(), educationTypeId, subject.getArchived());
    
    TORSubject torSubject = tor.findSubject(subject.getId());
    if (torSubject == null) {
      torSubject = TORSubject.from(subjectModel);
      tor.addSubject(torSubject);
      
      StudentSubjectGradeDAO studentSubjectGradeDAO = DAOFactory.getInstance().getStudentSubjectGradeDAO();
      StudentSubjectGrade studentSubjectGrade = studentSubjectGradeDAO.findBy(student, subject);
      if (studentSubjectGrade != null && studentSubjectGrade.getGrade() != null) {
        fi.otavanopisto.pyramus.domainmodel.grading.Grade grade = studentSubjectGrade.getGrade();
        Grade gradeModel = new Grade(grade.getId(), grade.getName(), grade.getDescription(), grade.getGradingScale().getId(),
            grade.getPassingGrade(), grade.getQualification(), grade.getGPA(), grade.getArchived());
        torSubject.setMeanGrade(gradeModel);
      }
    }

    TORCourse torCourse = torSubject.findCourse(courseNumber);
    if (torCourse == null) {
      torCourse = new TORCourse(subjectModel, courseNumber, courseLength, courseLengthUnit);
      torSubject.addCourse(torCourse);
    } else {
      // Validate the lengthUnit matches
      
      if (torCourse.getLengthUnit() != courseLengthUnit) {
        problems.add(new TORProblem(TORProblemType.INCOMPATIBLE_LENGTHUNITS, String.format("%s%d", subjectModel.getCode(), courseNumber)));
      }
    }
    
    String gradeName = credit.getGrade().getName();
    Double gpa = credit.getGrade().getGPA();
    torCourse.addCredit(new TORCredit(credit.getGrade().getId(), gradeName, gpa, credit.getDate(), 
        TORCreditType.COURSEASSESSMENT, credit.getGrade().getPassingGrade()));
  }

}
