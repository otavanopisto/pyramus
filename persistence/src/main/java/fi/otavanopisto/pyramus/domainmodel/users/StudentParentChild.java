package fi.otavanopisto.pyramus.domainmodel.users;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.util.DateUtils;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "studentParent", "student"})
    }
)
public class StudentParentChild {
  
  public Long getId() {
    return this.id;
  }

  /**
   * Returns the expiry date for the student - parent relation.
   * Expiry is as follows:
   * - If student's full age date cannot be determined, expiry is immediate
   * - If student has study end date and it is earlier than full age date, study end date is returned
   * - Otherwise the full age date is returned
   * @return
   */
  @Transient
  public LocalDate getExpiryDate() {
    // The default expiry date
    LocalDate fullageDate = getStudent().getPerson().getFullageDate();
    if (fullageDate == null) {
      return LocalDate.now();
    }

    LocalDate studyEndDate = DateUtils.toLocalDate(getStudent().getStudyEndDate());

    if (isContinuedViewPermission()) {
      // Continued view permission only ends when studies end. Returns null if the end date is not defined.
      return studyEndDate;
    }
    else {
      // Without continued view permission the earlier date is the cutoff
      return studyEndDate != null && studyEndDate.isBefore(fullageDate) ? studyEndDate : fullageDate;
    }
  }

  public StudentParent getStudentParent() {
    return studentParent;
  }

  public void setStudentParent(StudentParent studentParent) {
    this.studentParent = studentParent;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public boolean isContinuedViewPermission() {
    return continuedViewPermission;
  }

  public void setContinuedViewPermission(boolean continuedViewPermission) {
    this.continuedViewPermission = continuedViewPermission;
  }

  public Date getContinuedViewPermissionModified() {
    return continuedViewPermissionModified;
  }

  public void setContinuedViewPermissionModified(Date continuedViewPermissionModified) {
    this.continuedViewPermissionModified = continuedViewPermissionModified;
  }

  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)  
  private Long id;

  @ManyToOne (optional = false)
  @JoinColumn(name = "studentParent")
  private StudentParent studentParent;

  @ManyToOne (optional = false)
  @JoinColumn(name = "student")
  private Student student;

  @Column
  private boolean continuedViewPermission;
  
  @Column
  private Date continuedViewPermissionModified;
}
