package fi.otavanopisto.pyramus.rest.model;

import java.time.OffsetDateTime;
import java.util.List;

public class MatriculationExamEnrollment {
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getNationalStudentNumber() {
    return nationalStudentNumber;
  }

  public void setNationalStudentNumber(Long nationalStudentNumber) {
    this.nationalStudentNumber = nationalStudentNumber;
  }

  public String getGuider() {
    return guider;
  }

  public void setGuider(String guider) {
    this.guider = guider;
  }

  public String getEnrollAs() {
    return enrollAs;
  }

  public void setEnrollAs(String enrollAs) {
    this.enrollAs = enrollAs;
  }

  public int getNumMandatoryCourses() {
    return numMandatoryCourses;
  }

  public void setNumMandatoryCourses(int numMandatoryCourses) {
    this.numMandatoryCourses = numMandatoryCourses;
  }

  public boolean isRestartExam() {
    return restartExam;
  }

  public void setRestartExam(boolean restartExam) {
    this.restartExam = restartExam;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean isCanPublishName() {
    return canPublishName;
  }

  public void setCanPublishName(boolean canPublishName) {
    this.canPublishName = canPublishName;
  }
  
  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public MatriculationExamStudentStatus getState() {
    return state;
  }

  public void setState(MatriculationExamStudentStatus state) {
    this.state = state;
  }
  
  public List<MatriculationExamAttendance> getAttendances() {
    return attendances;
  }

  public void setAttendances(List<MatriculationExamAttendance> attendances) {
    this.attendances = attendances;
  }

  public OffsetDateTime getEnrollmentDate() {
    return enrollmentDate;
  }

  public void setEnrollmentDate(OffsetDateTime enrollmentDate) {
    this.enrollmentDate = enrollmentDate;
  }

  public String getDegreeType() {
    return degreeType;
  }

  public void setDegreeType(String degreeType) {
    this.degreeType = degreeType;
  }

  public Long getExamId() {
    return examId;
  }

  public void setExamId(Long examId) {
    this.examId = examId;
  }

  public String getDegreeStructure() {
    return degreeStructure;
  }

  public void setDegreeStructure(String degreeStructure) {
    this.degreeStructure = degreeStructure;
  }

  private Long id;
  private Long nationalStudentNumber;
  private String guider;
  private String enrollAs;
  private String degreeType;
  private int numMandatoryCourses;
  private boolean restartExam;
  private String location;
  private String message;
  private boolean canPublishName;
  private Long studentId;
  private MatriculationExamStudentStatus state;
  private List<MatriculationExamAttendance> attendances;
  private OffsetDateTime enrollmentDate;
  private Long examId;
  private String degreeStructure;
}
