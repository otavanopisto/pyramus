package fi.pyramus.domainmodel.students;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceException;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.PhoneNumber;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.persistence.search.filters.StudentIdFilterFactory;

/**
 * AbstractStudent defines a student "shell" that can be used to link different student instances to each other. i.e. same student can be "archived" from
 * previous education department and joined as new Student to a new education department thus perceiving all the information the student had at the time of the
 * earlier studies.
 * 
 * @author antti.viljakainen
 */

@Entity
@Indexed
@FullTextFilterDefs (
  @FullTextFilterDef (
     name="StudentIdFilter",
     impl=StudentIdFilterFactory.class
  )
)
public class AbstractStudent {

  /**
   * Returns unique identifier for this AbstractStudent
   * 
   * @return unique id of this AbstractStudent
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets birthday for this AbstractStudent
   * 
   * @param birthday
   *          New birthday
   */
  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  /**
   * Returns birthday given for this AbstractStudent
   * 
   * @return birthday
   */
  public Date getBirthday() {
    return birthday;
  }

  /**
   * Sets social security number for this AbstractStudent
   * 
   * @param socialSecurityNumber
   *          New social security number
   */
  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  /**
   * Returns social security number given for this AbstractStudent
   * 
   * @return social security number
   */
  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  /**
   * Returns sex given for this AbstractStudent
   * 
   * @return sex
   */
  public Sex getSex() {
    return sex;
  }

  /**
   * Sets the sex of this AbstractStudent
   * 
   * @param sex
   *          New sex
   */
  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public List<Student> getStudents() {
    return students;
  }

  @SuppressWarnings("unused")
  private void setStudents(List<Student> students) {
    this.students = students;
  }

  public void addStudent(Student student) {
    if (!this.students.contains(student)) {
      student.setAbstractStudent(this);
      students.add(student);
    } else {
      throw new PersistenceException("Student is already in this AbstractStudent");
    }
  }

  public void removeStudent(Student student) {
    if (this.students.contains(student)) {
      student.setAbstractStudent(null);
      students.remove(student);
    } else {
      throw new PersistenceException("Student is not in this AbstractStudent");
    }
  }

  @Transient
  public Student getLatestStudent() {
    List<Student> students = new ArrayList<Student>();
    
    List<Student> oldStudents = this.getStudents();
    
    for (Student student : oldStudents) {
      if (!student.getArchived())
        students.add(student);
    }

    if (students.size() > 0) {
      Student stu = null;

      for (Student stu2 : students) {
        /**
        * Ordering study programmes as follows
        *  1. studies that have start date but no end date (ongoing)
        *  2. studies that have no start nor end date
        *  3. studies that have ended
        *  4. studies that are archived
        *  5. other
        */

       // First item
       if (stu == null) {
         stu = stu2;
         continue;
       }
       
       int o1class =
         (stu.getArchived()) ? 4:
           (stu.getStudyStartDate() != null && stu.getStudyEndDate() == null) ? 1:
             (stu.getStudyStartDate() == null && stu.getStudyEndDate() == null) ? 2:
               (stu.getStudyEndDate() != null) ? 3:
                 5;
       int o2class =
         (stu2.getArchived()) ? 4:
           (stu2.getStudyStartDate() != null && stu2.getStudyEndDate() == null) ? 1:
             (stu2.getStudyStartDate() == null && stu2.getStudyEndDate() == null) ? 2:
               (stu2.getStudyEndDate() != null) ? 3:
                 5;
    
       int cmp = 0;
       if (o1class == o2class) {
         // classes are the same, we try to do last comparison from the start dates
         cmp = ((stu.getStudyStartDate() != null) && (stu2.getStudyStartDate() != null)) ? 
             stu2.getStudyStartDate().compareTo(stu.getStudyStartDate()) : 0; 
       } else
         cmp = o1class < o2class ? -1 : o1class == o2class ? 0 : 1;

       // If the new evaluated Student is more recent, we swap that in place
       if (cmp > 0)
         stu = stu2;
      }
      
      return stu;
      
//      Collections.sort(students, new Comparator<Student>() {
//        @Override
//        public int compare(Student o1, Student o2) {
//          /**
//           * Ordering study programmes as follows
//           *  1. studies that have start date but no end date (ongoing)
//           *  2. studies that have no start nor end date
//           *  3. studies that have ended
//           *  4. studies that are archived
//           *  5. other
//           */
//          
//          int o1class =
//            (o1.getArchived()) ? 4:
//              (o1.getStudyStartDate() != null && o1.getStudyEndDate() == null) ? 1:
//                (o1.getStudyStartDate() == null && o1.getStudyEndDate() == null) ? 2:
//                  (o1.getStudyEndDate() != null) ? 3:
//                    5;
//          int o2class =
//            (o2.getArchived()) ? 4:
//              (o2.getStudyStartDate() != null && o2.getStudyEndDate() == null) ? 1:
//                (o2.getStudyStartDate() == null && o2.getStudyEndDate() == null) ? 2:
//                  (o2.getStudyEndDate() != null) ? 3:
//                    5;
//  
//          if (o1class == o2class) {
//            // classes are the same, we try to do last comparison from the start dates
//            return ((o1.getStudyStartDate() != null) && (o2.getStudyStartDate() != null)) ? 
//                o2.getStudyStartDate().compareTo(o1.getStudyStartDate()) : 0; 
//          } else
//            return o1class < o2class ? -1 : o1class == o2class ? 0 : 1;
//        }
//      });
//
//      return students.get(0);
    } else
      return null;
  }

  public void setBasicInfo(String basicInfo) {
    this.basicInfo = basicInfo;
  }

  public String getBasicInfo() {
    return basicInfo;
  }

  @Transient
  @Field(analyze = Analyze.NO, store = Store.YES)
  public String getLastNameSortable() {
    Student student = getLatestStudent();
    return student != null ? student.getLastName() : "";
  }

  @Transient
  @Field(analyze = Analyze.NO, store = Store.YES)
  public String getFirstNameSortable() {
    Student student = getLatestStudent();
    return student != null ? student.getFirstName() : "";
  }

  @Transient
  @Field
  public String getInactiveFirstNames() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        if (student.getFirstName() != null) {
          results.add(student.getFirstName());
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getInactiveLastNames() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        if (student.getLastName() != null) {
          results.add(student.getLastName());
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getInactiveNicknames() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        if (student.getNickname() != null) {
          results.add(student.getNickname());
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getInactiveEducations() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        if (student.getEducation() != null) {
          results.add(student.getEducation());
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getInactiveEmails() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        for (Email email : student.getContactInfo().getEmails()) {
          if (email.getAddress() != null) {
            results.add(email.getAddress());
          }
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getInactiveStreetAddresses() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        for (Address address : student.getContactInfo().getAddresses()) {
          if (address.getStreetAddress() != null) {
            results.add(address.getStreetAddress());
          }
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getInactivePostalCodes() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        for (Address address : student.getContactInfo().getAddresses()) {
          if (address.getPostalCode() != null) {
            results.add(address.getPostalCode());
          }
        }
      }
    }
    return setToString(results);
  }

  /**
   * Returns whether this abstract student contains at least one non-archived student who has got his
   * study end date set and that date is in the past.
   *  
   * @return <code>true</code> if this abstract student contains at least one inactive student, otherwise <code>false</code>
   */
  @Transient
  @Field
  public String getInactive() {
    String result = Boolean.FALSE.toString();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        result = Boolean.TRUE.toString();
        break;
      }
    }
    return result;
  }

  @Transient
  @Field
  public String getInactiveCities() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        for (Address address : student.getContactInfo().getAddresses()) {
          if (address.getCity() != null) {
            results.add(address.getCity());
          }
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getInactiveCountries() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        for (Address address : student.getContactInfo().getAddresses()) {
          if (address.getCountry() != null) {
            results.add(address.getCountry());
          }
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getInactivePhones() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        for (PhoneNumber phoneNumber : student.getContactInfo().getPhoneNumbers()) {
          results.add(phoneNumber.getNumber());
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getInactiveLodgings() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        if (student.getLodging() != null) {
          results.add(student.getLodging().toString());
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getInactiveStudyProgrammeIds() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        if (student.getStudyProgramme() != null)
          results.add(student.getStudyProgramme().getId().toString());
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getInactiveLanguageIds() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        if (student.getLanguage() != null)
          results.add(student.getLanguage().getId().toString());
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getInactiveMunicipalityIds() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        if (student.getMunicipality() != null)
          results.add(student.getMunicipality().getId().toString());
      }
    }
    return results.toString();
  }

  @Transient
  @Field
  public String getInactiveNationalityIds() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        if (student.getNationality() != null)
          results.add(student.getNationality().getId().toString());
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveFirstNames() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        if (student.getFirstName() != null) {
          results.add(student.getFirstName());
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveLastNames() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        if (student.getLastName() != null) {
          results.add(student.getLastName());
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveNicknames() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        if (student.getNickname() != null) {
          results.add(student.getNickname());
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveEducations() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        if (student.getEducation() != null) {
          results.add(student.getEducation());
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveEmails() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        for (Email email : student.getContactInfo().getEmails()) {
          if (email.getAddress() != null) {
            results.add(email.getAddress());
          }
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveStreetAddresses() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        for (Address address : student.getContactInfo().getAddresses()) {
          if (address.getStreetAddress() != null) {
            results.add(address.getStreetAddress());
          }
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActivePostalCodes() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        for (Address address : student.getContactInfo().getAddresses()) {
          if (address.getPostalCode() != null) {
            results.add(address.getPostalCode());
          }
        }
      }
    }
    return setToString(results);
  }

  /**
   * Returns whether this abstract student contains at least one non-archived student who hasn't got his
   * study end date set or it has been set but it is in the future.
   *  
   * @return <code>true</code> if this abstract student contains at least one active student, otherwise <code>false</code>
   */
  @Transient
  @Field
  public String getActive() {
    String result = Boolean.FALSE.toString();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        result = Boolean.TRUE.toString();
        break;
      }
    }
    return result;
  }

  @Transient
  @Field
  public String getActiveCities() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        for (Address address : student.getContactInfo().getAddresses()) {
          if (address.getCity() != null) {
            results.add(address.getCity());
          }
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveCountries() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        for (Address address : student.getContactInfo().getAddresses()) {
          if (address.getCountry() != null) {
            results.add(address.getCountry());
          }
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActivePhones() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        for (PhoneNumber phoneNumber : student.getContactInfo().getPhoneNumbers()) {
          results.add(phoneNumber.getNumber());
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveLodgings() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        if (student.getLodging() != null) {
          results.add(student.getLodging().toString());
        }
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveStudyProgrammeIds() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        if (student.getStudyProgramme() != null)
          results.add(student.getStudyProgramme().getId().toString());
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveLanguageIds() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        if (student.getLanguage() != null)
          results.add(student.getLanguage().getId().toString());
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveMunicipalityIds() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        if (student.getMunicipality() != null)
          results.add(student.getMunicipality().getId().toString());
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveNationalityIds() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        if (student.getNationality() != null)
          results.add(student.getNationality().getId().toString());
      }
    }
    return setToString(results);
  }

  @Transient
  @Field
  public String getActiveTags() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && student.getActive()) {
        for (Tag tag : student.getTags()) {
          results.add(tag.getText());
        }
      }
    }

    return setToString(results);
  }

  /**
   * Returns whether this abstract student contains at least one student who has got his/her
   * study end date set or it has been set but it is in the future.
   *  
   * @return <code>true</code> if this abstract student contains at least one active student, otherwise <code>false</code>
   */
  @Transient
  @Field
  public String getInactiveTags() {
    Set<String> results = new HashSet<String>();
    for (Student student : getStudents()) {
      if (!student.getArchived() && !student.getActive()) {
        for (Tag tag : student.getTags()) {
          results.add(tag.getText());
        }
      }
    }

    return setToString(results);
  }

  private String setToString(Set<String> set) {
    StringBuilder sb = new StringBuilder();
    Iterator<String> i = set.iterator();
    while (i.hasNext()) {
      sb.append(i.next());
      if (i.hasNext()) {
        sb.append(' ');
      }
    }
    return sb.toString();
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public Boolean getSecureInfo() {
    return secureInfo;
  }

  public void setSecureInfo(Boolean secureInfo) {
    this.secureInfo = secureInfo;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "AbstractStudent")
  @TableGenerator(name = "AbstractStudent", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;

  @Column
  @Temporal(value = TemporalType.DATE)
  private Date birthday;

  @Column
  @Field(store = Store.NO)
  private String socialSecurityNumber;

  @Column
  @Enumerated (EnumType.STRING)
  @Field(store = Store.NO)
  private Sex sex;

  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean secureInfo = Boolean.FALSE;

  @Basic(fetch = FetchType.LAZY)
  @Column(length = 1073741824)
  private String basicInfo;

  @OneToMany
  @JoinColumn(name = "abstractStudent")
  @IndexedEmbedded
  private List<Student> students = new ArrayList<Student>();

  @Version
  @Column(nullable = false)
  private Long version;
}
