package fi.otavanopisto.pyramus.util.dataimport;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;

public class DataImportStrategyProvider {

  protected DataImportStrategyProvider() {
  }
  
  public static DataImportStrategyProvider instance() {
    return _instance;
  }
  
  public void registerFieldHandler(EntityImportStrategy entityStrategy, String fieldName, FieldHandlingStrategy strategy) {
    EntityFieldHandlerProvider entityHandler = getEntityFieldHandlerProvider(entityStrategy);

    if (entityHandler == null) {
      entityHandler = new EntityFieldHandlerProvider();
      registerEntityFieldHandler(entityStrategy, entityHandler);
    }
    
    entityHandler.registerFieldHandler(fieldName, strategy);
  }
  
  public FieldHandlingStrategy getFieldHandler(EntityImportStrategy entityStrategy, String fieldName) {
    EntityFieldHandlerProvider entityHandler = getEntityFieldHandlerProvider(entityStrategy);
    
    if (entityHandler != null) {
      FieldHandlingStrategy fieldHandlingStrategy = entityHandler.getFieldHandlingStrategy(fieldName);
      return fieldHandlingStrategy;
    } else {
      return null;
    }
  }
  
  private EntityFieldHandlerProvider getEntityFieldHandlerProvider(EntityImportStrategy entityStrategy) {
    return entityFieldHandlers.get(entityStrategy);
  }
  
  public EntityHandlingStrategy getEntityHandler(EntityImportStrategy strategy) {
    return entityHandlers.get(strategy);
  }
  
  public void registerEntityHandler(EntityImportStrategy strategy, EntityHandlingStrategy entityHandler) {
    entityHandlers.put(strategy, entityHandler);
  }

  private void registerEntityFieldHandler(EntityImportStrategy entityStrategy, EntityFieldHandlerProvider prov) {
    entityFieldHandlers.put(entityStrategy, prov);
  }

  private Map<EntityImportStrategy, EntityFieldHandlerProvider> entityFieldHandlers = new HashMap<>();
  private Map<EntityImportStrategy, EntityHandlingStrategy> entityHandlers = new HashMap<>();
  private static DataImportStrategyProvider _instance;
  
  static {
    _instance = new DataImportStrategyProvider();

    EntityImportStrategy entityStrategy;
    Class<?> subClass;
    
    entityStrategy = EntityImportStrategy.STUDENT;
    subClass = Student.class;
    instance().registerEntityHandler(entityStrategy, new DefaultEntityHandlingStrategy(Student.class, entityStrategy) {
      
      @Override
      public void initializeContext(DataImportContext context) {
        super.initializeContext(context);
        
        PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
        
        if (context.hasField("socialSecurityNumber")) {
          String ssn = context.getFieldValue("socialSecurityNumber");

          if (ssn != null) {
            Person person = personDAO.findBySSN(ssn);
            context.addEntity(Person.class, person);
          }
        }
      }
      
      @Override
      protected void bindEntities(DataImportContext context) {
        super.bindEntities(context);
        
        // Bind Address, Email, Phone, Person etc. to Student or create new ones where needed
        Student student = (Student) context.getEntity(Student.class);
        
        Person person = (Person) context.getEntity(Person.class);
        if (person == null) {
          person = new Person();
          context.addEntity(Person.class, person);
        }
        
        person.addUser(student);

        // Adress        
        Address address = (Address) context.getEntity(Address.class);
        if (address != null) {
          ContactInfo contactInfo = getStudentContactInfo(student);
          contactInfo.addAddress(address);
        }

        // Email
        Email email = (Email) context.getEntity(Email.class);
        if (email != null) {
          ContactInfo contactInfo = getStudentContactInfo(student);
          contactInfo.addEmail(email);
        }

        // PhoneNumber
        PhoneNumber phone = (PhoneNumber) context.getEntity(PhoneNumber.class);
        if (phone != null) {
          ContactInfo contactInfo = getStudentContactInfo(student);
          contactInfo.addPhoneNumber(phone);
        }
      }

      private ContactInfo getStudentContactInfo(Student student) {
        ContactInfo result = student.getContactInfo();
        if (result == null) {
          result = new ContactInfo();
          student.setContactInfo(result);
        }
        return result;
      }
    });
    
    instance().registerFieldHandler(entityStrategy, "firstName", new DefaultFieldHandingStrategy(subClass));
    instance().registerFieldHandler(entityStrategy, "lastName", new DefaultFieldHandingStrategy(subClass));
    instance().registerFieldHandler(entityStrategy, "studyProgrammeName", new DefaultFieldHandingStrategy(subClass) {
      @Override
      public void handleField(String fieldName, Object fieldValue, DataImportContext context) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
        
        Student student = (Student) getEntity(Student.class, context);
        StudyProgramme studyProgramme = studyProgrammeDAO.findByName((String) fieldValue);
        if (studyProgramme != null) {
          DataImportUtils.setFieldValue(student, DataImportUtils.getField(student, "studyProgramme"), studyProgramme);
        } else {
          throw new SmvcRuntimeException(PyramusStatusCode.VALIDATION_FAILURE, "StudyProgramme not found with name: " + fieldValue);
        } 
      }
    });
    
    // Address
    subClass = Address.class;
    instance().registerFieldHandler(entityStrategy, "city", new DefaultFieldHandingStrategy(subClass));
    instance().registerFieldHandler(entityStrategy, "country", new DefaultFieldHandingStrategy(subClass));
    instance().registerFieldHandler(entityStrategy, "postalCode", new DefaultFieldHandingStrategy(subClass));
    instance().registerFieldHandler(entityStrategy, "streetAddress", new DefaultFieldHandingStrategy(subClass));

    // Email
    subClass = Email.class;
    instance().registerFieldHandler(entityStrategy, "email", new DefaultFieldHandingStrategy(subClass, "address"));

    // Phone
    subClass = PhoneNumber.class;
    instance().registerFieldHandler(entityStrategy, "phoneNumber", new DefaultFieldHandingStrategy(subClass, "number"));

    // Person
    subClass = Person.class;
    instance().registerFieldHandler(entityStrategy, "birthday", new DefaultFieldHandingStrategy(subClass));
    instance().registerFieldHandler(entityStrategy, "socialSecurityNumber", new DefaultFieldHandingStrategy(subClass));// new SocialSecurityNumberHandlingStrategy(subClass, true));
    instance().registerFieldHandler(entityStrategy, "sex", new DefaultFieldHandingStrategy(subClass));

    // Course
    entityStrategy = EntityImportStrategy.COURSE;
    subClass = Course.class;
    instance().registerEntityHandler(entityStrategy, new DefaultEntityHandlingStrategy(Course.class, entityStrategy) {
      @Override
      protected void bindEntities(DataImportContext context) {
        super.bindEntities(context);

        Date now = new Date();
        User user = context.getLoggedUser();
        
        Course course = (Course) context.getEntity(Course.class);
        course.setCreated(now);
        course.setLastModified(now);
        course.setCreator(user);
        course.setLastModifier(user);
        
        Module module = new Module();
        module.setName(course.getName());
        module.setDescription(course.getDescription());
        module.setCreated(course.getCreated());
        module.setLastModified(course.getLastModified());
        module.setCreator(course.getCreator());
        module.setLastModifier(course.getLastModifier());
        
        course.setModule(module);
        
        context.addEntity(Module.class, module);
      }
    });
    
    instance().registerFieldHandler(entityStrategy, "name", new DefaultFieldHandingStrategy(subClass));
    instance().registerFieldHandler(entityStrategy, "description", new DefaultFieldHandingStrategy(subClass));
    instance().registerFieldHandler(entityStrategy, "beginDate", new DefaultFieldHandingStrategy(subClass));
    instance().registerFieldHandler(entityStrategy, "endDate", new DefaultFieldHandingStrategy(subClass));

    // CourseStudent
    entityStrategy = EntityImportStrategy.COURSESTUDENT;
    subClass = CourseStudent.class;
    instance().registerEntityHandler(entityStrategy, new DefaultEntityHandlingStrategy(CourseStudent.class, entityStrategy) {
      @Override
      protected void bindEntities(DataImportContext context) {
        super.bindEntities(context);
        CourseStudent courseStudent = (CourseStudent) context.getEntity(CourseStudent.class);
        courseStudent.setEnrolmentTime(new Date());
      }      
    });
    instance().registerFieldHandler(entityStrategy, "student", new ReferenceFieldHandlingStrategy(subClass, "student", Student.class));
    instance().registerFieldHandler(entityStrategy, "course", new ReferenceFieldHandlingStrategy(subClass, "course", Course.class));
  }
}