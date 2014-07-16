package fi.pyramus.rest.model;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;

import org.joda.time.DateTime;

import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.courses.CourseEnrolmentType;

@ApplicationScoped
@Stateful
public class ObjectFactory {
  
  @PostConstruct
  public void init() {
    addMappers(
        new Mapper<fi.pyramus.domainmodel.base.AcademicTerm>() {
          @Override
          public Object map(fi.pyramus.domainmodel.base.AcademicTerm entity) {
            return new AcademicTerm(entity.getId(), entity.getName(), toDateTime( entity.getStartDate() ), toDateTime( entity.getEndDate() ), entity.getArchived());
          }
        }, 
        
        new Mapper<CourseParticipationType>() {
          @Override
          public Object map(CourseParticipationType entity) {
            return new fi.pyramus.rest.model.CourseParticipationType(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<CourseEnrolmentType>() {
          @Override
          public Object map(CourseEnrolmentType entity) {
            return new fi.pyramus.rest.model.CourseEnrolmentType(entity.getId(), entity.getName());
          }
        }, 
        
        new Mapper<CourseState>() {
          @Override
          public Object map(CourseState entity) {
            return new fi.pyramus.rest.model.CourseState(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<Course>() {
          @Override
          public Object map(Course entity) {
            Long subjectId = null;
            Subject courseSubject = entity.getSubject();
            if (courseSubject != null) {
              subjectId = courseSubject.getId();
            }
            
            List<String> tags = new ArrayList<String>();
            Set<Tag> courseTags = entity.getTags();
            if (courseTags != null) {
              for (Tag courseTag : courseTags) {
                tags.add(courseTag.getText());
              }
            }

            Double length = entity.getCourseLength() != null ? entity.getCourseLength().getUnits() : null;
            Long lengthUnitId = entity.getCourseLength() != null ? entity.getCourseLength().getUnit().getId() : null;
            DateTime created = toDateTime(entity.getCreated() );
            DateTime lastModified = toDateTime(entity.getLastModified() );
            DateTime beginDate = toDateTime(entity.getBeginDate() );
            DateTime endDate = toDateTime(entity.getEndDate() );
            DateTime enrolmentTimeEnd = toDateTime( entity.getEnrolmentTimeEnd() );
            
            return new fi.pyramus.rest.model.Course(entity.getId(), entity.getName(), created, 
                lastModified, entity.getDescription(), entity.getArchived(), entity.getCourseNumber(), 
                entity.getMaxParticipantCount(), beginDate, endDate, entity.getNameExtension(), 
                entity.getLocalTeachingDays(), entity.getTeachingHours(), entity.getDistanceTeachingDays(), 
                entity.getAssessingHours(), entity.getPlanningHours(), enrolmentTimeEnd, entity.getCreator().getId(), 
                entity.getLastModifier().getId(), subjectId, length, lengthUnitId, entity.getModule().getId(), entity.getState().getId(), tags);
          }
        }, 
        
        new Mapper<CourseComponent>() {
          @Override
          public Object map(CourseComponent entity) {
            return new fi.pyramus.rest.model.CourseComponent(entity.getId(), entity.getName(), entity.getDescription(), entity.getLength().getUnits(), entity.getLength().getUnit().getId(), entity.getArchived());
          }
        }, 
        
        new Mapper<CourseDescriptionCategory>() {
          @Override
          public Object map(CourseDescriptionCategory entity) {
            return new fi.pyramus.rest.model.CourseDescriptionCategory(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<EducationType>() {
          @Override
          public Object map(EducationType entity) {
            return new fi.pyramus.rest.model.EducationType(entity.getId(), entity.getName(), entity.getCode(), entity.getArchived());
          }
        }, 
        
        new Mapper<EducationSubtype>() {
          @Override
          public Object map(EducationSubtype entity) {
            Long educationTypeId = entity.getEducationType() != null ? entity.getEducationType().getId() : null;
            return new fi.pyramus.rest.model.EducationSubtype(entity.getId(), entity.getName(), entity.getCode(), educationTypeId, entity.getArchived());
          }
        }, 
        
        new Mapper<Subject>() {
          @Override
          public Object map(Subject entity) {
            Long educationTypeId = entity.getEducationType() != null ? entity.getEducationType().getId() : null;
            return new fi.pyramus.rest.model.Subject(entity.getId(), entity.getCode(), entity.getName(), educationTypeId, entity.getArchived());
          }
        }

    
    );
  }

  @SuppressWarnings("unchecked")
  public Object createModel(Object object) {
    if (object instanceof List) {
      List<Object> result = new ArrayList<>();
      
      for (Object item : (List<Object>) object) {
        result.add(createModel(item));
      }
      
      return result;
    }
    
    return mappers.get(object.getClass()).map(object);
  }

  private DateTime toDateTime(Date date) {
    if (date == null) {
      return null;
    }
    
    return new DateTime(date.getTime());
  }
  
  private static interface Mapper <T> {
    public Object map(T entity);
  }
  
  private void addMappers(Mapper<?>... mappers) {
    for (Mapper<?> mapper : mappers) {
      addMapper(mapper);
    }
  }

  @SuppressWarnings("unchecked")
  private void addMapper(Mapper<?> mapper) {
    ParameterizedType parameterizedType = (ParameterizedType) mapper.getClass().getGenericInterfaces()[0];
    Class<?> type = (Class<?>) parameterizedType.getActualTypeArguments()[0];
    mappers.put(type, (Mapper<Object>) mapper);
  }
  
  private Map<Class<?>, Mapper<Object>> mappers = new HashMap<Class<?>, ObjectFactory.Mapper<Object>>();
}
