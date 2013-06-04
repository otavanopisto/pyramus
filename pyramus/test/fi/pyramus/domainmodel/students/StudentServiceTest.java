package fi.pyramus.domainmodel.students;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.persistence.usertypes.Sex;
import fi.testutils.DatabaseDependingTest;


@SuppressWarnings("deprecation")
@Test
public class StudentServiceTest extends DatabaseDependingTest {

  private static final String TEST_FIRSTNAME            = "John";
  private static final String TEST_LASTNAME             = "Doe";
  private static final Sex    TEST_SEX                  = Sex.FEMALE;
  private static final Date   TEST_BIRTHDAY             = new Date(1990, 6, 24);
  private static final String TEST_SSECID               = "123D";
  private static final String TEST_NATIONALITY_NAME     = "United States";
  private static final String TEST_NATIONALITY_ID       = "123";
  private static final String TEST_LANGUAGE_NAME        = "English";
  private static final String TEST_LANGUAGE_ID          = "ASD";
  private static final String TEST_MUNICIPALITY_NAME    = "Los Angeles";
  private static final String TEST_MUNICIPALITY_ID      = "3583";
  private static final String TEST_PARENTALINFO         = "Mother: Jane Doe";
  private static final String TEST_ADDITIONALINFO       = "Cancelled on 1.1.2009\nRejoined 1.2.2009";
  private static final String TEST_PHONE                = "1-888-CALLME";
  private static final String TEST_COUNRTY              = "Suomi";
  private static final String TEST_CITY                 = "Mikkeli";
  private static final String TEST_POSTALCODE           = "12345";
  private static final String TEST_STREETADDRESS        = "Fake st. 8";
  
  @BeforeMethod
  protected void setUp() throws Exception {
    beginTransaction();
    
    AbstractStudent abstractStudent = new AbstractStudent();
    abstractStudent.setBirthday(TEST_BIRTHDAY);
    abstractStudent.setSocialSecurityNumber(TEST_SSECID);
    abstractStudent.setSex(TEST_SEX);
    getCurrentSession().save(abstractStudent);
    
    Student student = new Student();
    student.setAbstractStudent(abstractStudent);
    student.setFirstName(TEST_FIRSTNAME);
    student.setLastName(TEST_LASTNAME);
    getCurrentSession().save(student);
    
    Nationality nationality = new Nationality();
    nationality.setCode(TEST_NATIONALITY_ID);
    nationality.setName(TEST_NATIONALITY_NAME);
    getCurrentSession().save(nationality);
    
    Language language = new Language();
    language.setCode(TEST_LANGUAGE_ID);
    language.setName(TEST_LANGUAGE_NAME);
    getCurrentSession().save(language);

    Municipality municipality = new Municipality();
    municipality.setCode(TEST_MUNICIPALITY_ID);
    municipality.setName(TEST_MUNICIPALITY_NAME);
    getCurrentSession().save(municipality);

    commit();
    
    abstractStudentId = abstractStudent.getId();
    studentId = student.getId();
    nationalityId = nationality.getId();
    languageId = language.getId();
    municipalityId = municipality.getId();
  }

  @AfterMethod
  protected void tearDown() throws Exception {
    beginTransaction();

    getCurrentSession().delete(getCurrentSession().load(Student.class, studentId));
    getCurrentSession().delete(getCurrentSession().load(AbstractStudent.class, abstractStudentId));

    getCurrentSession().delete(getCurrentSession().load(Nationality.class, nationalityId));
    getCurrentSession().delete(getCurrentSession().load(Language.class, languageId));
    getCurrentSession().delete(getCurrentSession().load(Municipality.class, municipalityId));
    
    commit();
  }

  @Test
  public void testGetId() {
    beginTransaction();
    try {
      Student student = (Student) getCurrentSession().load(Student.class, studentId);
      Assert.assertEquals(studentId, student.getId());
    } finally {
      commit();
    }
  }

  @Test
  public void testBasicAttributes() {
    beginTransaction();
    try {
      Student student = (Student) getCurrentSession().load(Student.class, studentId);

      Assert.assertEquals(student.getAbstractStudent().getBirthday().getTime(), TEST_BIRTHDAY.getTime());
      Assert.assertEquals(student.getAbstractStudent().getSocialSecurityNumber(), TEST_SSECID);

      Assert.assertEquals(student.getFirstName(), TEST_FIRSTNAME);
      Assert.assertEquals(student.getLastName(), TEST_LASTNAME);
    } finally {
      commit();
    }
  }
  
  public void testAdditionalInfos() {
    beginTransaction();
    Student student = (Student) getCurrentSession().load(Student.class, studentId);

    Nationality nationality = (Nationality) getCurrentSession().load(Nationality.class, nationalityId);
    Language language = (Language) getCurrentSession().load(Language.class, languageId);
    Municipality municipality = (Municipality) getCurrentSession().load(Municipality.class, municipalityId);

    student.setPhone(TEST_PHONE);
    student.setAdditionalInfo(TEST_ADDITIONALINFO);
    student.setParentalInfo(TEST_PARENTALINFO);
    student.setNationality(nationality);
    student.setMunicipality(municipality);
    student.setLanguage(language);
    getCurrentSession().save(student);
    
    commit();

    /**
     * Reload and verify the information is unchanged
     */
    
    beginTransaction();
    try {
      student = (Student) getCurrentSession().load(Student.class, studentId);

      Assert.assertEquals(student.getPhone(), TEST_PHONE);
      Assert.assertEquals(student.getAdditionalInfo(), TEST_ADDITIONALINFO);
      Assert.assertEquals(student.getParentalInfo(), TEST_PARENTALINFO);

      Assert.assertEquals(student.getNationality().getCode(), TEST_NATIONALITY_ID);
      Assert.assertEquals(student.getNationality().getName(), TEST_NATIONALITY_NAME);
      Assert.assertEquals(student.getMunicipality().getCode(), TEST_MUNICIPALITY_ID);
      Assert.assertEquals(student.getMunicipality().getName(), TEST_MUNICIPALITY_NAME);
      Assert.assertEquals(student.getLanguage().getCode(), TEST_LANGUAGE_ID);
      Assert.assertEquals(student.getLanguage().getName(), TEST_LANGUAGE_NAME);
    } finally {
      commit();
    }
  }
  
  public void testAddress() {
    beginTransaction();
    Student student = (Student) getCurrentSession().load(Student.class, studentId);
    
    Address address = new Address();
    address.setCity(TEST_CITY);
    address.setCountry(TEST_COUNRTY);
    address.setPostalCode(TEST_POSTALCODE);
    address.setStreetAddress(TEST_STREETADDRESS);
    student.setAddress(address);
    
    getCurrentSession().save(student);
    
    commit();

    /**
     * Reload and verify the information is unchanged
     */
    
    beginTransaction();
    try {
      student = (Student) getCurrentSession().load(Student.class, studentId);

      Assert.assertEquals(TEST_CITY, student.getAddress().getCity());
      Assert.assertEquals(TEST_COUNRTY, student.getAddress().getCountry());
      Assert.assertEquals(TEST_POSTALCODE, student.getAddress().getPostalCode());
      Assert.assertEquals(TEST_STREETADDRESS, student.getAddress().getStreetAddress());
    } finally {
      commit();      
    }
  }

  public void testGetFullName() {
    beginTransaction();
    
    Student student = (Student) getCurrentSession().load(Student.class, studentId);
    Assert.assertEquals(studentId, student.getId());
    Assert.assertEquals(student.getFullName(), TEST_FIRSTNAME + ' ' + TEST_LASTNAME);
   
    commit();  
  }
  
  private Long nationalityId;
  private Long languageId;
  private Long municipalityId;
  private Long abstractStudentId;
  private Long studentId;
}
