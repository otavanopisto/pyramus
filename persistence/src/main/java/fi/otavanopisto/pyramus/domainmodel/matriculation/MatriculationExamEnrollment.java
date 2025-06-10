package fi.otavanopisto.pyramus.domainmodel.matriculation;

import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentState;

@Entity
@Table (
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "exam_id", "student_id"})
    }
)
public class MatriculationExamEnrollment {

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
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

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public SchoolType getEnrollAs() {
    return enrollAs;
  }

  public void setEnrollAs(SchoolType enrollAs) {
    this.enrollAs = enrollAs;
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

  public MatriculationExamEnrollmentState getState() {
    return state;
  }

  public void setState(MatriculationExamEnrollmentState state) {
    this.state = state;
  }
  
  public Date getEnrollmentDate() {
    return enrollmentDate;
  }

  public void setEnrollmentDate(Date enrollmentDate) {
    this.enrollmentDate = enrollmentDate;
  }

  public Integer getCandidateNumber() {
    return candidateNumber;
  }

  public void setCandidateNumber(Integer candidateNumber) {
    this.candidateNumber = candidateNumber;
  }

  public DegreeType getDegreeType() {
    return degreeType;
  }

  public void setDegreeType(DegreeType degreeType) {
    this.degreeType = degreeType;
  }

  public MatriculationExam getExam() {
    return exam;
  }

  public void setExam(MatriculationExam exam) {
    this.exam = exam;
  }

  public MatriculationExamEnrollmentDegreeStructure getDegreeStructure() {
    return degreeStructure;
  }

  public void setDegreeStructure(MatriculationExamEnrollmentDegreeStructure degreeStructure) {
    this.degreeStructure = degreeStructure;
  }

  public StaffMember getHandler() {
    return handler;
  }

  public void setHandler(StaffMember handler) {
    this.handler = handler;
  }

  public String getHandlerNotes() {
    return handlerNotes;
  }

  public void setHandlerNotes(String handlerNotes) {
    this.handlerNotes = handlerNotes;
  }

  public String getContactInfoChange() {
    return contactInfoChange;
  }

  public void setContactInfoChange(String contactInfoChange) {
    this.contactInfoChange = contactInfoChange;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="MatriculationExamEnrollment")  
  @TableGenerator(name="MatriculationExamEnrollment", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
 
  @ManyToOne
  private MatriculationExam exam;
  
  @Column
  private Long nationalStudentNumber;
  
  @Column
  private String guider;
  
  @Column
  @Enumerated(EnumType.STRING)
  private SchoolType enrollAs;
  
  @Column
  @Enumerated(EnumType.STRING)
  private DegreeType degreeType;
  
  @Column
  private int numMandatoryCourses;
  
  @Column(nullable = false)
  private boolean restartExam;
  
  @Column
  private String location;
  
  @Lob
  @Basic (fetch = FetchType.LAZY)
  private String contactInfoChange;
  
  @Lob
  @Basic (fetch = FetchType.LAZY)
  private String message;
  
  @Column(nullable = false)
  private boolean canPublishName;

  @ManyToOne
  private Student student;
  
  @Column
  private Integer candidateNumber;
  
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MatriculationExamEnrollmentState state;
  
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MatriculationExamEnrollmentDegreeStructure degreeStructure;
  
  @Column(nullable = false)
  @Temporal(value = TemporalType.TIMESTAMP)
  private Date enrollmentDate;

  @ManyToOne
  private StaffMember handler;
  
  @Lob
  @Basic (fetch = FetchType.LAZY)
  private String handlerNotes;
}