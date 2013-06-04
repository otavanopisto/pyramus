package fi.pyramus.services;

import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.apache.commons.lang.StringUtils;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.dao.grading.CourseAssessmentDAO;
import fi.pyramus.dao.grading.CourseAssessmentRequestDAO;
import fi.pyramus.dao.grading.CreditDAO;
import fi.pyramus.dao.grading.CreditVariableDAO;
import fi.pyramus.dao.grading.GradeDAO;
import fi.pyramus.dao.grading.GradingScaleDAO;
import fi.pyramus.dao.grading.TransferCreditDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.grading.CourseAssessment;
import fi.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.pyramus.domainmodel.grading.Credit;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.domainmodel.grading.TransferCredit;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.services.entities.EntityFactoryVault;
import fi.pyramus.services.entities.grading.CourseAssessmentEntity;
import fi.pyramus.services.entities.grading.CourseAssessmentRequestEntity;
import fi.pyramus.services.entities.grading.GradeEntity;
import fi.pyramus.services.entities.grading.GradingScaleEntity;
import fi.pyramus.services.entities.grading.TransferCreditEntity;

@Stateless
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@RolesAllowed("WebServices")
public class GradingService extends PyramusService {
  
  public TransferCreditEntity[] listStudentsTransferCredits(@WebParam (name = "studentId") Long studentId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    return (TransferCreditEntity[]) EntityFactoryVault.buildFromDomainObjects(transferCreditDAO.listByStudent(studentDAO.findById(studentId)));
  }
  
  public CourseAssessmentEntity[] listStudentsCourseAssessments(@WebParam (name = "studentId") Long studentId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    return (CourseAssessmentEntity[]) EntityFactoryVault.buildFromDomainObjects(courseAssessmentDAO.listByStudent(studentDAO.findById(studentId)));
  }

  public GradeEntity createGrade(@WebParam (name = "gradingScaleId") Long gradingScaleId, @WebParam (name = "name") String name, @WebParam (name = "description") String description, @WebParam (name = "passingGrade") Boolean passingGrade, @WebParam (name = "GPA") Double GPA, @WebParam (name = "qualification") String qualification) {
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    Grade grade = gradeDAO.create(gradingScaleDAO.findById(gradingScaleId), name, description, passingGrade, GPA, qualification);
    validateEntity(grade);
    return EntityFactoryVault.buildFromDomainObject(grade);
  }
  
  public GradingScaleEntity createGradingScale(@WebParam (name = "name") String name, @WebParam (name = "description") String description) {
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    GradingScale gradingScale = gradingScaleDAO.create(name, description);
    validateEntity(gradingScale);
    return EntityFactoryVault.buildFromDomainObject(gradingScale);
  } 
  
  public GradeEntity getGradeById(@WebParam (name = "gradeId") Long gradeId) {
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    return EntityFactoryVault.buildFromDomainObject(gradeDAO.findById(gradeId));
  }
  
  public GradingScaleEntity getGradingScaleById(@WebParam (name = "gradingScaleId") Long gradingScaleId) {
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    return EntityFactoryVault.buildFromDomainObject(gradingScaleDAO.findById(gradingScaleId));
  }
  
  public GradingScaleEntity[] listGradingScales() {
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    return (GradingScaleEntity[]) EntityFactoryVault.buildFromDomainObjects(gradingScaleDAO.listUnarchived());
  }
  
  public void updateGrade(@WebParam (name = "gradeId") Long gradeId, @WebParam (name = "name") String name, @WebParam (name = "description") String description, @WebParam (name = "passingGrade") Boolean passingGrade, @WebParam (name = "GPA") Double GPA, @WebParam (name = "qualification") String qualification) {
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    Grade grade = gradeDAO.findById(gradeId);
    gradeDAO.update(grade, name, description, passingGrade, GPA, qualification);
    validateEntity(grade);
  }
  
  public void updateGradingScale(@WebParam (name = "gradingScaleId") Long gradingScaleId, @WebParam (name = "name") String name, @WebParam (name = "description") String description) {
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    GradingScale gradingScale = gradingScaleDAO.findById(gradingScaleId);
    gradingScaleDAO.update(gradingScale, name, description);
    validateEntity(gradingScale);
  }
  
  public CourseAssessmentEntity createCourseAssessment(@WebParam (name = "courseStudentId") Long courseStudentId, @WebParam (name = "assessingUserId") Long assessingUserId, @WebParam (name = "gradeId") Long gradeId, @WebParam (name = "date") Date date, @WebParam (name = "verbalAssessment") String verbalAssessment) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();

    User assessingUser = userDAO.findById(assessingUserId);
    Grade grade = gradeDAO.findById(gradeId);
    
    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
    
    CourseAssessment courseAssessment = courseAssessmentDAO.create(courseStudent, assessingUser, grade, date, verbalAssessment);
    
    validateEntity(courseAssessment);
    
    return EntityFactoryVault.buildFromDomainObject(courseAssessment);
  }
  
  public CourseAssessmentEntity updateCourseAssessment(@WebParam (name = "courseAssessmentId") Long courseAssessmentId, @WebParam (name = "assessingUserId") Long assessingUserId, @WebParam (name = "gradeId") Long gradeId, @WebParam (name = "date") Date date, @WebParam (name = "verbalAssessment") String verbalAssessment) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();

    User assessingUser = userDAO.findById(assessingUserId);
    Grade grade = gradeDAO.findById(gradeId);
    CourseAssessment courseAssessment = courseAssessmentDAO.findById(courseAssessmentId);
    
    courseAssessment = courseAssessmentDAO.update(courseAssessment, assessingUser, grade, date, verbalAssessment);
    
    validateEntity(courseAssessment);
    
    return EntityFactoryVault.buildFromDomainObject(courseAssessment);
  }
  
  public CourseAssessmentEntity getCourseAssessmentByCourseStudentId(@WebParam (name = "courseStudentId") Long courseStudentId) {
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();

    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
    
    CourseAssessment courseAssessment = courseAssessmentDAO.findByCourseStudent(courseStudent);
    return EntityFactoryVault.buildFromDomainObject(courseAssessment);
  }
  
  public TransferCreditEntity createTransferCredit(@WebParam (name = "courseName") String courseName, @WebParam (name = "courseNumber") Integer courseNumber, @WebParam (name = "courseLength") Double courseLength, @WebParam (name = "courseLengthUnitId") Long courseLengthUnitId, @WebParam (name = "schoolId") Long schoolId, @WebParam (name = "subjectId") Long subjectId, @WebParam (name = "optionality") String optionality, @WebParam (name = "studentId") Long studentId, @WebParam (name = "assessingUserId") Long assessingUserId, @WebParam (name = "gradeId") Long gradeId, @WebParam (name = "date") Date date, @WebParam (name = "verbalAssessment") String verbalAssessment) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();

    EducationalTimeUnit courseLengthUnit = educationalTimeUnitDAO.findById(courseLengthUnitId);
    School school = schoolDAO.findById(schoolId);
    Subject subject = subjectDAO.findById(subjectId);
    Student student = studentDAO.findById(studentId);
    User assessingUser = userDAO.findById(assessingUserId);
    Grade grade = gradeDAO.findById(gradeId);
    CourseOptionality courseOptionality = null;
    if (!StringUtils.isBlank(optionality))
      courseOptionality = CourseOptionality.valueOf(optionality);
      
    TransferCredit transferCredit = transferCreditDAO.create(courseName, courseNumber, courseLength, courseLengthUnit, school, subject, courseOptionality, student, assessingUser, grade, date, verbalAssessment);
    
    validateEntity(transferCredit);
    
    return EntityFactoryVault.buildFromDomainObject(transferCredit);
  }

  public CourseAssessmentRequestEntity createCourseAssessmentRequest(
      @WebParam (name = "courseStudentId") Long courseStudentId, 
      @WebParam (name = "created") Date created, 
      @WebParam (name = "requestText") String requestText) {
    CourseAssessmentRequestDAO courseAssessmentRequestDAO = DAOFactory.getInstance().getCourseAssessmentRequestDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();

    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);

    CourseAssessmentRequest courseAssessmentRequest = courseAssessmentRequestDAO.create(courseStudent, created, requestText);
    
    validateEntity(courseAssessmentRequest);
    
    return EntityFactoryVault.buildFromDomainObject(courseAssessmentRequest);
  }
  
  public CourseAssessmentRequestEntity[] listCourseAssessmentRequestsByCourse(
      @WebParam (name = "courseId") Long courseId) {
    CourseAssessmentRequestDAO courseAssessmentRequestDAO = DAOFactory.getInstance().getCourseAssessmentRequestDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    
    Course course = courseDAO.findById(courseId);
    
    return (CourseAssessmentRequestEntity[]) EntityFactoryVault.buildFromDomainObjects(courseAssessmentRequestDAO.listByCourse(course));
  }

  public CourseAssessmentRequestEntity[] listCourseAssessmentRequestsByStudent(
      @WebParam (name = "studentId") Long studentId) {
    CourseAssessmentRequestDAO courseAssessmentRequestDAO = DAOFactory.getInstance().getCourseAssessmentRequestDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    
    Student student = studentDAO.findById(studentId);
    
    return (CourseAssessmentRequestEntity[]) EntityFactoryVault.buildFromDomainObjects(courseAssessmentRequestDAO.listByStudent(student));
  }

  public CourseAssessmentRequestEntity[] listCourseAssessmentRequestsByCourseStudent(
      @WebParam (name = "courseStudentId") Long courseStudentId) {
    CourseAssessmentRequestDAO courseAssessmentRequestDAO = DAOFactory.getInstance().getCourseAssessmentRequestDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    
    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
    
    return (CourseAssessmentRequestEntity[]) EntityFactoryVault.buildFromDomainObjects(courseAssessmentRequestDAO.listByCourseStudent(courseStudent));
  }
  
  public void setCreditVariable(@WebParam (name="creditId") Long creditId, @WebParam (name="key") String key, @WebParam (name="value") String value) {
    CreditDAO creditDAO = DAOFactory.getInstance().getCreditDAO();
    CreditVariableDAO creditVariableDAO = DAOFactory.getInstance().getCreditVariableDAO();
    Credit credit = creditDAO.findById(creditId);
    creditVariableDAO.setCreditVariable(credit, key, value);
  }

  public String getCreditVariable(@WebParam (name="creditId") Long creditId, @WebParam (name="key") String key) {
    CreditDAO creditDAO = DAOFactory.getInstance().getCreditDAO();
    CreditVariableDAO creditVariableDAO = DAOFactory.getInstance().getCreditVariableDAO();

    Credit credit = creditDAO.findById(creditId);
    return creditVariableDAO.findByCreditAndKey(credit, key);
  }

}