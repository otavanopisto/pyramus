package fi.otavanopisto.pyramus.domainmodel.base;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.TypeBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.AssociationInverseSide;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.TypeBinding;

import fi.otavanopisto.pyramus.domainmodel.base.search.PersonKoskiOIDTypeBinder;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.security.ContextReference;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

@Entity
@Indexed
@TypeBinding(binder = @TypeBinderRef(type = PersonKoskiOIDTypeBinder.class))
public class Person implements ContextReference {
  
  /**
   * Returns unique identifier for this Person
   * 
   * @return unique id of this Person
   */
  public Long getId() {
    return id;
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  /**
   * Sets birthday for this Person
   * 
   * @param birthday
   *          New birthday
   */
  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  /**
   * Returns birthday given for this Person
   * 
   * @return birthday
   */
  public Date getBirthday() {
    return birthday;
  }

  /**
   * Sets social security number for this Person
   * 
   * @param socialSecurityNumber
   *          New social security number
   */
  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  /**
   * Returns social security number given for this Person
   * 
   * @return social security number
   */
  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  /**
   * Returns sex given for this Person
   * 
   * @return sex
   */
  public Sex getSex() {
    return sex;
  }

  /**
   * Sets the sex of this Person
   * 
   * @param sex
   *          New sex
   */
  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public List<StaffMember> getStaffMembers() {
    List<User> users = getUsers();
    List<StaffMember> staffMembers = new ArrayList<>();
    
    for (User user : users) {
      if (user instanceof StaffMember)
        staffMembers.add((StaffMember) user);
    }
    
    return staffMembers;
  }

  @Transient
  @IndexedEmbedded(includeEmbeddedObjectId = true, structure = ObjectStructure.NESTED)
  @AssociationInverseSide(inversePath = @ObjectPath({ @PropertyValue(propertyName = "person")}))
  @IndexingDependency(derivedFrom = @ObjectPath({ @PropertyValue(propertyName = "users") }))
  public List<Student> getStudents() {
    List<User> users = getUsers();
    List<Student> students = new ArrayList<>();
    
    for (User user : users) {
      if (user instanceof Student)
        students.add((Student) user);
    }
    
    return students;
  }

  public void addUser(User user) {
    if (!this.getUsers().contains(user)) {
      user.setPerson(this);
      getUsers().add(user);
    } else {
      throw new PersistenceException("Student is already in this Person");
    }
  }

  public void removeUser(User user) {
    // TODO: should probably do something with defaultuser too
    if (this.getUsers().contains(user)) {
      user.setPerson(null);
      getUsers().remove(user);
    } else {
      throw new PersistenceException("Student is not in this Person");
    }
  }

  @Transient
  public Student getLatestStudent() {
    List<Student> students = new ArrayList<>();
    
    if (this.getUsers() != null) {
      for (Student student : this.getStudents()) {
        if (!student.getArchived())
          students.add(student);
      }
      
      Collections.sort(students, new PersonStudentComparator());
    }
    
    return students.isEmpty() ? null : students.get(0);
  }
  
  public void setBasicInfo(String basicInfo) {
    this.basicInfo = basicInfo;
  }

  public String getBasicInfo() {
    return basicInfo;
  }

  @Transient
  @IndexedEmbedded(includeEmbeddedObjectId = true, structure = ObjectStructure.NESTED)
  @AssociationInverseSide(inversePath = @ObjectPath({ @PropertyValue(propertyName = "person")}))
  @IndexingDependency(derivedFrom = @ObjectPath({ @PropertyValue(propertyName = "users") }))
  private StaffMember getStaffMember() {
    List<StaffMember> staffMembers = getStaffMembers();
    return !staffMembers.isEmpty() ? staffMembers.get(0) : null;
  }

  /**
   * Compares this person's birthday to the current date and returns
   * 
   * <ul>
   *   <li>Boolean.TRUE if the student is under 18 years old</li>
   *   <li>Boolean.FALSE if the student is over 18 years old</li>
   *   <li>null otherwise - if f.ex. the birthday field is null</li>
   * </ul>
   * 
   * @return
   */
  @Transient
  public Boolean isUnderAge() {
    Date person_bday = getBirthday();
    
    if (person_bday != null) {
      LocalDate ld_birthday = Instant.ofEpochMilli(person_bday.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
      LocalDate ld_threshold = LocalDate.now().minusYears(18);

      return ld_birthday.isAfter(ld_threshold);
    }
    
    return null;
  }
  
  @Transient
  @KeywordField (projectable = Projectable.NO, sortable = Sortable.YES)
  @IndexingDependency(derivedFrom = @ObjectPath({ @PropertyValue(propertyName = "users") }))
  public String getLastNameSortable() {
    Student student = getLatestStudent();
    StaffMember staffMember = getStaffMember();
    return student != null ? student.getLastName() : staffMember != null ? staffMember.getLastName() : "";
  }

  @Transient
  @KeywordField (projectable = Projectable.NO, sortable = Sortable.YES)
  @IndexingDependency(derivedFrom = @ObjectPath({ @PropertyValue(propertyName = "users") }))
  public String getFirstNameSortable() {
    Student student = getLatestStudent();
    StaffMember staffMember = getStaffMember();
    return student != null ? student.getFirstName() : staffMember != null ? staffMember.getFirstName() : "";
  }

  /**
   * Returns whether this abstract student contains at least one non-archived student who has got his
   * study end date set and that date is in the past.
   *  
   * @return <code>true</code> if this abstract student contains at least one inactive student, otherwise <code>false</code>
   */
  @Transient
  @GenericField
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO)
  public boolean getInactive() {
    boolean result = false;
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        result = true;
        break;
      }
    }
    return result;
  }

  /**
   * Returns whether this abstract student contains at least one non-archived student who hasn't got his
   * study end date set or it has been set but it is in the future.
   *  
   * @return <code>true</code> if this abstract student contains at least one active student, otherwise <code>false</code>
   */
  @Transient
  @KeywordField (projectable = Projectable.NO, sortable = Sortable.YES)
  @IndexingDependency(derivedFrom = @ObjectPath({ @PropertyValue(propertyName = "users") }))
  public String getActive() {
    return Boolean.toString(hasActiveStudents());
  }

  /**
   * Returns whether this abstract student contains at least one non-archived student who hasn't got his
   * study end date set or it has been set but it is in the future.
   *  
   * @return <code>true</code> if this abstract student contains at least one active student, otherwise <code>false</code>
   */
  @Transient
  public boolean hasActiveStudents() {
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        return true;
      }
    }
    return false;
  }

  @Transient
  public Date getLatestStudyEndDate() {
    return getStudents().stream()
      .map(Student::getStudyEndDate)
      .filter(Objects::nonNull)
      .max(Date::compareTo)
      .orElse(null);
  }

  // TODO: Naming conventions pls
  @Transient
  @GenericField
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO)
  public Boolean getStaff() {
    StaffMember staffMember = getStaffMember();

    return Boolean.valueOf(staffMember != null);
  }

  public Boolean getSecureInfo() {
    return secureInfo;
  }

  public void setSecureInfo(Boolean secureInfo) {
    this.secureInfo = secureInfo;
  }
  
  private List<User> getUsers() {
    return users;
  }

  @SuppressWarnings("unused")
  private void setUsers(List<User> users) {
    this.users = users;
  }

  public User getDefaultUser() {
    return defaultUser;
  }

  public void setDefaultUser(User defaultUser) {
    this.defaultUser = defaultUser;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "Person")
  @TableGenerator(name = "Person", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;

  @Version
  @Column(nullable = false)
  private Long version;
  
  @Column
  @Temporal(value = TemporalType.DATE)
  private Date birthday;

  @Column
  @KeywordField(projectable = Projectable.NO)
  private String socialSecurityNumber;

  @Column
  @Enumerated (EnumType.STRING)
  @GenericField(projectable = Projectable.NO)
  private Sex sex;

  @NotNull
  @Column (nullable = false)
  @GenericField
  private Boolean secureInfo = Boolean.FALSE;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  private String basicInfo;

  @OneToMany
  @JoinColumn(name = "person_id")
  private List<User> users = new ArrayList<>();
  
  @OneToOne
  private User defaultUser;
}
