package fi.pyramus.dao;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.persistence.usertypes.Sex;
import fi.testutils.DatabaseDependingTest;

@SuppressWarnings("deprecation")
public class StudentDAOTest extends DatabaseDependingTest {
  private static final String FIRSTNAME          = "やかん - Teakettle";
  private static final String LASTNAME           = "铲 - Shovel";
  private static final Date   BIRTHDAY           = new Date(1990, 6, 24);
  private static final String SSN                = "123456-123D";
  private static final Sex    SEX                = Sex.FEMALE;
  private static final String NATIONALITY1_ID    = "123";
  private final static String NATIONALITY1_NAME  = "Mordor";
  private static final String NATIONALITY2_ID    = "345";
  private final static String NATIONALITY2_NAME  = "Rhûn";
  private static final String MUNICIPALITY1_ID   = "987";
  private static final String MUNICIPALITY1_NAME = "Minas Morgul"; 
  private static final String MUNICIPALITY2_ID   = "222";
  private static final String MUNICIPALITY2_NAME = "Minas Tirith";
  private final static String LANG1_ID           = "BS";
  private final static String LANG1_NAME         = "Black speech";
  private final static String LANG2_ID           = "KZ";
  private final static String LANG2_NAME         = "Khuzdul";
  private final static String LANG3_ID           = "OE";
  private final static String LANG3_NAME         = "Old Entish";
  private final static String LANG4_ID           = "CE";
  private final static String LANG4_NAME         = "Common Eldarin";
  
  @Test
  public void testCreateAbstractStudent() {
    AbstractStudent abstractStudent;
    Long abstractStudentId;
    
    /* All nulled save & load test */ 
    beginTransaction();
    abstractStudent = studentDAO.createAbstractStudent(null, null, null);
    commit();
    
    abstractStudentId = abstractStudent.getId();
    
    beginTransaction();
    abstractStudent = studentDAO.getAbstractStudent(abstractStudentId);
    Assert.assertEquals(abstractStudent.getId(), abstractStudentId);
    Assert.assertNull(abstractStudent.getBirthday());
    Assert.assertNull(abstractStudent.getSex());
    Assert.assertNull(abstractStudent.getSocialSecurityNumber());
    getCurrentSession().delete(abstractStudent);
    commit();
  }

  @Test
  public void testCreateStudent() {
    beginTransaction();
    AbstractStudent abstractStudent = studentDAO.createAbstractStudent(BIRTHDAY, SSN, SEX);
    commit();
    
    final Long abstractStudentId = abstractStudent.getId();
    
    /* null & empty first name tests */ 
    checkProperty("firstName", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        studentDAO.createStudent(studentDAO.getAbstractStudent(abstractStudentId), null, LASTNAME, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      }
    });
    checkProperty("firstName", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        studentDAO.createStudent(studentDAO.getAbstractStudent(abstractStudentId), "", LASTNAME, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      }
    });
    
    /* null & empty last name tests */ 
    checkProperty("lastName", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        studentDAO.createStudent(studentDAO.getAbstractStudent(abstractStudentId), FIRSTNAME, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      }
    });
    checkProperty("lastName", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        studentDAO.createStudent(studentDAO.getAbstractStudent(abstractStudentId), FIRSTNAME, "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      }
    });
    
    /* Clean up */ 
    beginTransaction();
    getCurrentSession().delete(getCurrentSession().load(AbstractStudent.class, abstractStudentId));
    commit();
  }
  
  @Test
  public void testGetAbstractStudent() {
    AbstractStudent abstractStudent;
    Long abstractStudentId;
    
    /* Normal save & load test */     
    beginTransaction();
    abstractStudent = studentDAO.createAbstractStudent(BIRTHDAY, SSN, SEX);
    commit();
    
    abstractStudentId = abstractStudent.getId();
    
    beginTransaction();
    abstractStudent = studentDAO.getAbstractStudent(abstractStudentId);
    Assert.assertEquals(abstractStudent.getId(), abstractStudentId);
    Assert.assertEquals(abstractStudent.getBirthday(), BIRTHDAY);
    Assert.assertEquals(abstractStudent.getSex(), SEX);
    Assert.assertEquals(abstractStudent.getSocialSecurityNumber(), SSN);
    getCurrentSession().delete(abstractStudent);
    commit();
  }
  
  @Test
  public void testGetStudent() {
    Student student;
    Long studentId;
    
    beginTransaction();
    AbstractStudent abstractStudent = studentDAO.createAbstractStudent(BIRTHDAY, SSN, SEX);
    commit();
    
    Long abstractStudentId = abstractStudent.getId();
    
    /* Normal save & load test */     
    beginTransaction();
    student = studentDAO.createStudent(abstractStudent, FIRSTNAME, LASTNAME, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    commit();
    
    studentId = student.getId();
    
    beginTransaction();
    student = studentDAO.getStudent(studentId);
    Assert.assertEquals(student.getId(), studentId);
    Assert.assertEquals(student.getFirstName(), FIRSTNAME);
    Assert.assertEquals(student.getLastName(), LASTNAME);
        
    getCurrentSession().delete(student);
    getCurrentSession().delete(getCurrentSession().load(AbstractStudent.class, abstractStudentId));
    commit();
  }

  @Test
  public void testListStudents() {
    beginTransaction();
    AbstractStudent abstractStudent1 = studentDAO.createAbstractStudent(null, "1234", SEX);
    Student student1 = studentDAO.createStudent(abstractStudent1, FIRSTNAME, LASTNAME, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    AbstractStudent abstractStudent2 = studentDAO.createAbstractStudent(null, "4678", SEX);
    Student student2 = studentDAO.createStudent(abstractStudent2, FIRSTNAME, LASTNAME, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Student student3 = studentDAO.createStudent(abstractStudent2, FIRSTNAME, LASTNAME, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    commit();
    
    beginTransaction();
    
    /* TODO: Correct entities ? */
    
    Assert.assertEquals(studentDAO.listStudents().size(), 3);
    
    getCurrentSession().delete(getCurrentSession().load(Student.class, student3.getId()));
    getCurrentSession().delete(getCurrentSession().load(Student.class, student2.getId()));
    getCurrentSession().delete(getCurrentSession().load(Student.class, student1.getId()));
    
    getCurrentSession().delete(getCurrentSession().load(AbstractStudent.class, abstractStudent2.getId()));
    getCurrentSession().delete(getCurrentSession().load(AbstractStudent.class, abstractStudent1.getId()));
    
    commit();
  }

  @Test
  public void testListNationalities() {
    beginTransaction();
    Nationality nationality1 = new Nationality();
    nationality1.setCode(NATIONALITY1_ID);
    nationality1.setArchived(false);
    nationality1.setName(NATIONALITY1_NAME);
    getCurrentSession().saveOrUpdate(nationality1);
    
    Nationality nationality2 = new Nationality();
    nationality2.setCode(NATIONALITY2_ID);
    nationality2.setArchived(false);
    nationality2.setName(NATIONALITY2_NAME);
    getCurrentSession().saveOrUpdate(nationality2);
    commit();
    
    beginTransaction();
    
    /* TODO: Correct entities ? */
    
    Assert.assertEquals(baseDAO.listNationalities().size(), 2);
    
    getCurrentSession().delete(getCurrentSession().load(Nationality.class, nationality2.getId()));
    getCurrentSession().delete(getCurrentSession().load(Nationality.class, nationality1.getId()));
    
    commit();
  }

  @Test
  public void testListHomeCounties() {
    beginTransaction();
    
    Municipality municipality1 = new Municipality();
    municipality1.setCode(MUNICIPALITY1_ID);
    municipality1.setArchived(false);
    municipality1.setName(MUNICIPALITY1_NAME);
    getCurrentSession().saveOrUpdate(municipality1);
    
    Municipality municipality2 = new Municipality();
    municipality2.setCode(MUNICIPALITY2_ID);
    municipality2.setArchived(false);
    municipality2.setName(MUNICIPALITY2_NAME);
    getCurrentSession().saveOrUpdate(municipality2);
    
    commit();
    
    beginTransaction();

    /* TODO: Correct entities ? */
    
    Assert.assertEquals(baseDAO.listMunicipalities().size(), 2);
    getCurrentSession().delete(getCurrentSession().load(Municipality.class, municipality2.getId()));
    getCurrentSession().delete(getCurrentSession().load(Municipality.class, municipality1.getId()));
    
    commit();
  }

  @Test
  public void testListLanguages() { 
    beginTransaction();
    
    Language lang1 = new Language();
    lang1.setCode(LANG1_ID);
    lang1.setArchived(false);
    lang1.setName(LANG1_NAME);
    getCurrentSession().saveOrUpdate(lang1);
    
    Language lang2 = new Language();
    lang2.setCode(LANG2_ID);
    lang2.setArchived(false);
    lang2.setName(LANG2_NAME);
    getCurrentSession().saveOrUpdate(lang2);
    
    Language lang3 = new Language();
    lang3.setCode(LANG3_ID);
    lang3.setArchived(false);
    lang3.setName(LANG3_NAME);
    getCurrentSession().saveOrUpdate(lang3);
    
    Language lang4 = new Language();
    lang4.setCode(LANG4_ID);
    lang4.setArchived(false);
    lang4.setName(LANG4_NAME);
    getCurrentSession().saveOrUpdate(lang4);
    
    commit();
    
    beginTransaction();

    /* TODO: Correct entities ? */
    
    Assert.assertEquals(baseDAO.listLanguages().size(), 4);

    getCurrentSession().delete(getCurrentSession().load(Language.class, lang1.getId()));
    getCurrentSession().delete(getCurrentSession().load(Language.class, lang2.getId()));
    getCurrentSession().delete(getCurrentSession().load(Language.class, lang3.getId()));
    getCurrentSession().delete(getCurrentSession().load(Language.class, lang4.getId()));
    
    commit();
  }

  private BaseDAO baseDAO = new BaseDAO();
  private StudentDAO studentDAO = new StudentDAO();

}
