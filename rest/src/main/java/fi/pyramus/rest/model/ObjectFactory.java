package fi.pyramus.rest.model;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.joda.time.DateTime;

import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.SchoolVariable;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.modules.ModuleComponent;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.projects.ProjectModule;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.domainmodel.base.SchoolVariableKey;

@ApplicationScoped
@Stateful
public class ObjectFactory {
  
  @Inject
  private Logger logger;
  
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
            Long lengthUnitId = entity.getLength() != null ? entity.getLength().getUnit().getId() : null;
            Double length = entity.getLength() != null ? entity.getLength().getUnits() : null;
            return new fi.pyramus.rest.model.CourseComponent(entity.getId(), entity.getName(), entity.getDescription(), length, lengthUnitId, entity.getArchived());
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
        }, 
        
        new Mapper<GradingScale>() {
          @Override
          public Object map(GradingScale entity) {
            return new fi.pyramus.rest.model.GradingScale(entity.getId(), entity.getName(), entity.getDescription(), entity.getArchived());
          }
        }, 
        
        new Mapper<Grade>() {
          @Override
          public Object map(Grade entity) {
            Long gradingScaleId = entity.getGradingScale() != null ? entity.getGradingScale().getId() : null;
            return new fi.pyramus.rest.model.Grade(entity.getId(), entity.getName(), entity.getDescription(), gradingScaleId, entity.getPassingGrade(), entity.getQualification(), entity.getGPA(), entity.getArchived());
          }
        }, 
        
        new Mapper<EducationalTimeUnit>() {
          @Override
          public Object map(EducationalTimeUnit entity) {
            return new fi.pyramus.rest.model.EducationalTimeUnit(entity.getId(), entity.getName(), entity.getBaseUnits(), entity.getArchived());
          }
        }, 
        
        new Mapper<Module>() {
          @Override
          public Object map(Module entity) {
            Long creatorId = entity.getCreator().getId();
            Long lastModifierId = entity.getLastModifier() != null ? entity.getLastModifier().getId() : null;
            Long subjectId = entity.getSubject() != null ? entity.getSubject().getId() : null;
            Double length = entity.getCourseLength() != null ? entity.getCourseLength().getUnits() : null; 
            Long lenghtUnitId = entity.getCourseLength() != null ? entity.getCourseLength().getUnit().getId() : null; 
            List<String> tags = new ArrayList<>();
            
            Set<Tag> moduleTags = entity.getTags();
            if (moduleTags != null) {
              for (Tag courseTag : moduleTags) {
                tags.add(courseTag.getText());
              }
            }
            
            return new fi.pyramus.rest.model.Module(entity.getId(), entity.getName(), toDateTime(entity.getCreated()),
                toDateTime(entity.getLastModified()), entity.getDescription(), entity.getArchived(), entity.getCourseNumber(), 
                entity.getMaxParticipantCount(), creatorId, lastModifierId, subjectId, length, lenghtUnitId, tags);
          }
        }, 
        
        new Mapper<ModuleComponent>() {
          @Override
          public Object map(ModuleComponent entity) {
            Long lengthUnitId = entity.getLength() != null ? entity.getLength().getUnit().getId() : null;
            Double length = entity.getLength() != null ? entity.getLength().getUnits() : null;
            return new fi.pyramus.rest.model.ModuleComponent(entity.getId(), entity.getName(), entity.getDescription(), length, lengthUnitId, entity.getArchived());
          }
        },
        
        new Mapper<Project>() {
          @Override
          public Object map(Project entity) {
            Double optionalStudiesLength = entity.getOptionalStudiesLength() != null ? entity.getOptionalStudiesLength().getUnits() : null;
            Long optionalStudiesLengthUnitId = entity.getOptionalStudiesLength() != null ? entity.getOptionalStudiesLength().getUnit().getId() : null;
            Long creatorId = entity.getCreator().getId();
            Long lastModifierId = entity.getLastModifier() != null ? entity.getLastModifier().getId() : null;
            List<String> tags = new ArrayList<>();
            
            Set<Tag> entityTags = entity.getTags();
            if (entityTags != null) {
              for (Tag entityTag : entityTags) {
                tags.add(entityTag.getText());
              }
            }
            
            return new fi.pyramus.rest.model.Project(entity.getId(), entity.getName(), entity.getDescription(), optionalStudiesLength, optionalStudiesLengthUnitId, toDateTime(entity.getCreated()), creatorId, toDateTime(entity.getLastModified()), lastModifierId, tags, entity.getArchived());
          }
        },
        
        new Mapper<ProjectModule>() {
          @Override
          public Object map(ProjectModule entity) {
            ProjectModuleOptionality optionality = null;
            switch (entity.getOptionality()) {
              case MANDATORY:
                optionality = ProjectModuleOptionality.MANDATORY;
              break;
              case OPTIONAL:
                optionality = ProjectModuleOptionality.OPTIONAL;
              break;
            }
            
            Long moduleId = entity.getModule() != null ? entity.getModule().getId() : null;
            
            return new fi.pyramus.rest.model.ProjectModule(entity.getId(), moduleId, optionality);
          }
        },
        
        new Mapper<School>() {
          @Override
          public Object map(School entity) {
            Long fieldId = entity.getField() != null ? entity.getField().getId() : null;
            
            List<String> tags = new ArrayList<>();
            
            Set<Tag> entityTags = entity.getTags();
            if (entityTags != null) {
              for (Tag entityTag : entityTags) {
                tags.add(entityTag.getText());
              }
            }
            
            Map<String, String> variables = new HashMap<>();
            for (SchoolVariable entityVariable : entity.getVariables()) {
              variables.put(entityVariable.getKey().getVariableKey(), entityVariable.getValue());
            }
            
            return new fi.pyramus.rest.model.School(entity.getId(), entity.getCode(), entity.getName(), tags, fieldId, entity.getArchived(), variables);
          }
        }, 
        
        new Mapper<SchoolField>() {
          @Override
          public Object map(SchoolField entity) {
            return new fi.pyramus.rest.model.SchoolField(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<SchoolVariableKey>() {
          @Override
          public Object map(SchoolVariableKey entity) {
            VariableType type = null;
            switch (entity.getVariableType()) {
              case BOOLEAN:
                type = VariableType.BOOLEAN;
              break;
              case DATE:
                type = VariableType.DATE;
              break;
              case NUMBER:
                type = VariableType.NUMBER;
              break;
              case TEXT:
                type = VariableType.TEXT;
              break;
            }
            
            return new fi.pyramus.rest.model.SchoolVariableKey(entity.getVariableKey(), entity.getVariableName(), entity.getUserEditable(), type);
          }
        }
        
        
    );
  }

  @SuppressWarnings("unchecked")
  public Object createModel(Object object) {
    if (object == null) {
      logger.log(Level.WARNING, "Null object was passed to createModel");
      return null;
    }
    
    if (object instanceof List) {
      List<Object> result = new ArrayList<>();
      
      for (Object item : (List<Object>) object) {
        result.add(createModel(item));
      }
      
      return result;
    }
    
    Mapper<Object> mapper = mappers.get(object.getClass());
    if (mapper == null) {
      logger.log(Level.SEVERE, "Could not find a mapper for " + object.getClass());
      return null;
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
