package fi.otavanopisto.pyramus.domainmodel.students;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

@Entity
public class StudentGroupUser {

  /**
   * Returns unique identifier for this StudentGroupUser
   * 
   * @return unique id of this StudentGroupUser
   */
  public Long getId() {
    return id;
  }
  
  protected void setStudentGroup(StudentGroup studentGroup) {
    this.studentGroup = studentGroup;
  }

  public StudentGroup getStudentGroup() {
    return studentGroup;
  }

  public void setStaffMember(StaffMember staffMember) {
    this.staffMember = staffMember;
  }

  public StaffMember getStaffMember() {
    return staffMember;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public boolean getMessageRecipient() {
    return messageRecipient;
  }

  public void setMessageRecipient(boolean messageRecipient) {
    this.messageRecipient = messageRecipient;
  }

  public boolean isGroupAdvisor() {
    return groupAdvisor;
  }

  public void setGroupAdvisor(boolean groupAdvisor) {
    this.groupAdvisor = groupAdvisor;
  }

  public boolean isStudyAdvisor() {
    return studyAdvisor;
  }

  public void setStudyAdvisor(boolean studyAdvisor) {
    this.studyAdvisor = studyAdvisor;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentGroupUser")  
  @TableGenerator(name="StudentGroupUser", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;

  @OneToOne
  @JoinColumn (name = "studentGroup")
  private StudentGroup studentGroup;

  @ManyToOne
  @IndexedEmbedded(includeEmbeddedObjectId = true)
  private StaffMember staffMember;  

  @Column (nullable = false)
  private boolean groupAdvisor;

  @Column (nullable = false)
  private boolean studyAdvisor;
  
  @Column (nullable = false)
  private boolean messageRecipient;

  @Version
  @Column(nullable = false)
  private Long version;
}
