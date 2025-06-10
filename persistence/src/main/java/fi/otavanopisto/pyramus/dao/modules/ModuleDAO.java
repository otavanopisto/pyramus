package fi.otavanopisto.pyramus.dao.modules;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.backend.lucene.search.query.LuceneSearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase_;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType_;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule_;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.modules.Module_;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectModule;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Stateless
public class ModuleDAO extends PyramusEntityDAO<Module> {

  public Module create(String name, String description, Long maxParticipantCount, User creatingUser) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());

    Module module = new Module();
    module.setName(name);
    module.setDescription(description);
    module.setMaxParticipantCount(maxParticipantCount);

    module.setCreator(creatingUser);
    module.setCreated(now);
    module.setLastModifier(creatingUser);
    module.setLastModified(now);

    entityManager.persist(module);

    return module;
  }

  public List<Module> listBySubject(Subject subject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Module> criteria = criteriaBuilder.createQuery(Module.class);
    Root<Module> root = criteria.from(Module.class);
    
    Subquery<CourseBase> courseModuleSubquery = criteria.subquery(CourseBase.class);
    Root<CourseModule> courseModuleRoot = courseModuleSubquery.from(CourseModule.class);
    courseModuleSubquery.select(courseModuleRoot.get(CourseModule_.course));
    courseModuleSubquery.where(criteriaBuilder.equal(courseModuleRoot.get(CourseModule_.subject), subject));
    
    criteria.select(root);
    criteria.where(
        root.in(courseModuleSubquery)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Module> listBySubjectAndCourseNumber(Subject subject, Integer courseNumber) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Module> criteria = criteriaBuilder.createQuery(Module.class);
    Root<Module> root = criteria.from(Module.class);

    Subquery<CourseBase> courseModuleSubquery = criteria.subquery(CourseBase.class);
    Root<CourseModule> courseModuleRoot = courseModuleSubquery.from(CourseModule.class);
    courseModuleSubquery.select(courseModuleRoot.get(CourseModule_.course));
    courseModuleSubquery.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseModuleRoot.get(CourseModule_.subject), subject),
            criteriaBuilder.equal(courseModuleRoot.get(CourseModule_.courseNumber), courseNumber)
        )
    );
    
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
          root.in(courseModuleSubquery)
      )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public Module updateTags(Module module, Set<Tag> tags) {
    EntityManager entityManager = getEntityManager();

    module.setTags(tags);

    entityManager.persist(module);

    return module;
  }

  public Module updateCurriculums(Module module, Set<Curriculum> curriculums) {
    EntityManager entityManager = getEntityManager();

    module.setCurriculums(curriculums);

    entityManager.persist(module);

    return module;
  }

  public Module update(Module module, String name, String description, Long maxParticipantCount, User user) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());

    module.setName(name);
    module.setDescription(description);
    module.setLastModifier(user);
    module.setLastModified(now);
    module.setMaxParticipantCount(maxParticipantCount);
    
    entityManager.persist(module);
    
    return module;
  }

  public SearchResult<Module> searchModulesBasic(int resultsPerPage, int page, String text) {
    int firstResult = page * resultsPerPage;
    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(text)) {
      queryBuilder.append("+(");
      addTokenizedSearchCriteria(queryBuilder, "name", text, false);
      addTokenizedSearchCriteria(queryBuilder, "tags.text", text, false);
      addTokenizedSearchCriteria(queryBuilder, "description", text, false);
      addTokenizedSearchCriteria(queryBuilder, "moduleComponents.name", text, false);
      addTokenizedSearchCriteria(queryBuilder, "moduleComponents.description", text, false);
      queryBuilder.append(")");
    }

    EntityManager entityManager = getEntityManager();
    SearchSession session = Search.session(entityManager);

    QueryParser parser = new QueryParser("", new StandardAnalyzer());
    String queryString = queryBuilder.toString();
    Query luceneQuery;

    try {
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      LuceneSearchResult<Module> fetch = session
          .search(Module.class)
          .extension(LuceneExtension.get())
          .where(f -> 
            f.bool()
              .must(f.fromLuceneQuery(luceneQuery))
              .filter(f.match().field("archived").matching(Boolean.FALSE))
          )
          .sort(f -> 
              f.score()
              .then().field("name_sort"))
          .fetch(firstResult, resultsPerPage);
      return searchResults(fetch, page, firstResult, resultsPerPage);
      
    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  public SearchResult<Module> searchModules(int resultsPerPage, int page, String projectName, String name, String tags, String description,
      String componentName, String componentDescription, Long ownerId, boolean filterArchived) {
    // Search with null Subject and null EducationSubtype
    return searchModules(resultsPerPage, page, projectName, name, tags, description, componentName, componentDescription, ownerId, null, null, null,
        null, filterArchived);
  }

  public SearchResult<Module> searchModules(int resultsPerPage, int page, String projectName, String name, String tags, String description,
      String componentName, String componentDescription, Long ownerId, Subject subject, EducationType educationType, EducationSubtype educationSubtype,
      Curriculum curriculum, boolean filterArchived) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    boolean hasName = !StringUtils.isBlank(name);
    boolean hasTags = !StringUtils.isBlank(tags);
    boolean hasDescription = !StringUtils.isBlank(description);
    boolean hasComponentName = !StringUtils.isBlank(componentName);
    boolean hasComponentDescription = !StringUtils.isBlank(componentDescription);

    if (hasName || hasTags || hasDescription || hasComponentName || hasComponentDescription) {
      queryBuilder.append("+(");

      if (hasName)
        addTokenizedSearchCriteria(queryBuilder, "name", name, false);

      if (hasTags)
        addTokenizedSearchCriteria(queryBuilder, "tags.text", tags, false);

      if (hasDescription)
        addTokenizedSearchCriteria(queryBuilder, "description", description, false);

      if (hasComponentName)
        addTokenizedSearchCriteria(queryBuilder, "moduleComponents.name", componentName, false);

      if (hasComponentDescription)
        addTokenizedSearchCriteria(queryBuilder, "moduleComponents.description", componentDescription, false);

      queryBuilder.append(")");
    }

    // If project text is given, only include modules that are in project(s) having the given name
    // (only the first ten matching projects, though, to prevent the search from becoming too gigantic...)

    Set<Long> moduleIds = new HashSet<>();
    if (!StringUtils.isBlank(projectName)) {
      ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();
      SearchResult<Project> searchResults = projectDAO.searchProjectsBasic(10, 0, projectName);
      List<Project> projects = searchResults.getResults();
      for (Project project : projects) {
        List<ProjectModule> projectModules = project.getProjectModules();
        for (ProjectModule projectModule : projectModules) {
          moduleIds.add(projectModule.getModule().getId());
        }
      }
      if (!moduleIds.isEmpty()) {
        queryBuilder.append(" +(");
        for (Long moduleId : moduleIds) {
          queryBuilder.append(" id: " + moduleId);
        }
        queryBuilder.append(")");
      } else {
        // Search condition by project name didn't yield any projects, so there cannot be any results
        return new SearchResult<>(0, 0, 0, 0, 0, new ArrayList<Module>());
      }
    }

    EntityManager entityManager = getEntityManager();
    SearchSession session = Search.session(entityManager);

    try {
      if (ownerId != null && ownerId > 0) {
        queryBuilder.append(" +creator.id: ").append(ownerId);
      }

      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      String queryString = queryBuilder.toString();
      Query luceneQuery;

      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      LuceneSearchResult<Module> fetch = session
          .search(Module.class)
          .extension(LuceneExtension.get())
          .where(f -> f.bool().with(b -> {
            b.must(f.fromLuceneQuery(luceneQuery));

            if (subject != null) {
              b.filter(f.match().field("courseModules.subject.id").matching(subject.getId()));
            }

            if (curriculum != null) {
              b.filter(f.match().field("curriculums.id").matching(curriculum.getId()));
            }

            if (educationType != null) {
              b.filter(f.match().field("courseEducationTypes.educationType.id").matching(educationType.getId()));
            }

            if (educationSubtype != null) {
              b.filter(f.match().field("courseEducationTypes.courseEducationSubtypes.educationSubtype.id").matching(educationSubtype.getId()));
            }
            
            if (filterArchived) {
              b.filter(f.match().field("archived").matching(Boolean.FALSE));
            }
          }))
          .sort(f -> 
              f.score()
              .then().field("name_sort"))
          .fetch(firstResult, resultsPerPage);
      return searchResults(fetch, page, firstResult, resultsPerPage);
      
    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  public List<Module> listByEducationType(EducationType educationType) {
//    return s.createCriteria(CourseEducationType.class)
//        .add(Restrictions.in("courseBase.id", s.createCriteria(Module.class).setProjection(Projections.id()).list()))
//        .add(Restrictions.eq("educationType", educationType)).setProjection(Projections.property("courseBase")).list();
    
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Module> criteria = criteriaBuilder.createQuery(Module.class);
    
    Root<Module> root = criteria.from(Module.class);
    Root<CourseEducationType> eduType = criteria.from(CourseEducationType.class);
    Join<CourseEducationType, CourseBase> eduTypeCourseBase = eduType.join(CourseEducationType_.courseBase);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Module_.id), eduTypeCourseBase.get(CourseBase_.id)),
            criteriaBuilder.equal(eduType.get(CourseEducationType_.educationType), educationType)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

}
