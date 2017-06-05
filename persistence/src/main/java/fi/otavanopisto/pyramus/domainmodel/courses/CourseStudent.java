package fi.otavanopisto.pyramus.domainmodel.courses;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.accommodation.Room;
import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Entity
public class CourseStudent extends CourseUser implements ArchivableEntity {
  
  public Date getEnrolmentTime() {
    return enrolmentTime;
  }
  
  public void setEnrolmentTime(Date enrolmentTime) {
    this.enrolmentTime = enrolmentTime;
  }
  
  public Student getStudent() {
    return student;
  }
  
  public void setStudent(Student student) {
    this.student = student;
  }
  
  public CourseParticipationType getParticipationType() {
    return participationType;
  }
  
  public void setParticipationType(CourseParticipationType participationType) {
    this.participationType = participationType;
  }
  
  public CourseEnrolmentType getCourseEnrolmentType() {
    return courseEnrolmentType;
  }
  
  public void setCourseEnrolmentType(CourseEnrolmentType courseEnrolmentType) {
    this.courseEnrolmentType = courseEnrolmentType;
  }
  
  /**
   * Sets the archived flag of this object.
   * 
   * @param archived The archived flag of this object
   */
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  /**
   * Returns the archived flag of this object.
   * 
   * @return The archived flag of this object
   */
  public Boolean getArchived() {
    return archived;
  }
  
  public void setLodging(Boolean lodging) {
    this.lodging = lodging;
  }

  public Boolean getLodging() {
    return lodging;
  }
  
  public CourseOptionality getOptionality() {
    return optionality;
  }
  
  public void setOptionality(CourseOptionality optionality) {
    this.optionality = optionality;
  }

  public void setBillingDetails(BillingDetails billingDetails) {
    this.billingDetails = billingDetails;
  }

  public BillingDetails getBillingDetails() {
    return billingDetails;
  }
  
  public Room getRoom() {
    return room;
  }
  
  public void setRoom(Room room) {
    this.room = room;
  }
  
  public BigDecimal getLodgingFee() {
    return lodgingFee;
  }
  
  public void setLodgingFee(BigDecimal lodgingFee) {
    this.lodgingFee = lodgingFee;
  }
  
  public Currency getLodgingFeeCurrency() {
    return lodgingFeeCurrency;
  }
  
  public void setLodgingFeeCurrency(Currency lodgingFeeCurrency) {
    this.lodgingFeeCurrency = lodgingFeeCurrency;
  }
  
  public String getOrganization() {
    return organization;
  }
  
  public void setOrganization(String organization) {
    this.organization = organization;
  }
  
  public String getAdditionalInfo() {
    return additionalInfo;
  }
  
  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public BigDecimal getReservationFee() {
    return reservationFee;
  }

  public void setReservationFee(BigDecimal reservationFee) {
    this.reservationFee = reservationFee;
  }

  public Currency getReservationFeeCurrency() {
    return reservationFeeCurrency;
  }

  public void setReservationFeeCurrency(Currency reservationFeeCurrency) {
    this.reservationFeeCurrency = reservationFeeCurrency;
  }

  public String getRoomAdditionalInfo() {
    return roomAdditionalInfo;
  }

  public void setRoomAdditionalInfo(String roomAdditionalInfo) {
    this.roomAdditionalInfo = roomAdditionalInfo;
  }

  @Column (nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date enrolmentTime;
  
  @ManyToOne (optional = false)  
  @JoinColumn(name="student")
  private Student student;

  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;
  
  @ManyToOne
  @JoinColumn(name="participationType")
  private CourseParticipationType participationType;
  
  @ManyToOne
  @JoinColumn(name="enrolmentType")
  private CourseEnrolmentType courseEnrolmentType;

  @NotNull
  @Column(nullable = false)
  private Boolean lodging = Boolean.FALSE;
  
  @Column
  @Enumerated (EnumType.STRING)
  private CourseOptionality optionality;

  @ManyToOne 
  @JoinColumn(name="billingDetails")
  private BillingDetails billingDetails;

  private BigDecimal reservationFee;
  
  private Currency reservationFeeCurrency;
  
  private BigDecimal lodgingFee;
  
  private Currency lodgingFeeCurrency;

  @ManyToOne
  private Room room;

  private String roomAdditionalInfo;
  
  private String organization;
  
  private String additionalInfo;
}