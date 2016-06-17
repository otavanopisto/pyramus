package fi.otavanopisto.pyramus.dao.modules;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectModule;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase_;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType_;
import fi.otavanopisto.pyramus.domainmodel.modules.Module_;

@Stateless
public class ModuleDAO extends PyramusEntityDAO<Module> {

  public Module create(String name, Subject subject, Curriculum curriculum, Integer courseNumber, Double moduleLength, 
      EducationalTimeUnit moduleLengthTimeUnit, String description, Long maxParticipantCount, User creatingUser) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());

    EducationalLength educationalLength = new EducationalLength();
    educationalLength.setUnit(moduleLengthTimeUnit);
    educationalLength.setUnits(moduleLength);

    Module module = new Module();
    module.setName(name);
    module.setDescription(description);
    module.setSubject(subject);
    module.setCourseNumber(courseNumber);
    module.setCourseLength(educationalLength);
    module.setMaxParticipantCount(maxParticipantCount);
    module.setCurriculum(curriculum);

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
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(Module_.subject), subject)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Module> listBySubjectAndCourseNumber(Subject subject, Integer courseNumber) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Module> criteria = criteriaBuilder.createQuery(Module.class);
    Root<Module> root = criteria.from(Module.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(Module_.subject), subject),
        criteriaBuilder.equal(root.get(Module_.courseNumber), courseNumber)
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

  public Module update(Module module, String name, Subject subject, Curriculum curriculum, Integer courseNumber, Double length, 
      EducationalTimeUnit lengthTimeUnit, String description, Long maxParticipantCount, User user) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());

    EducationalLength educationalLength = module.getCourseLength();
    if (educationalLength == null) {
      educationalLength = new EducationalLength();
    }
    educationalLength.setUnit(lengthTimeUnit);
    educationalLength.setUnits(length);

    module.setName(name);
    module.setDescription(description);
    module.setSubject(subject);
    module.setCourseNumber(courseNumber);
    module.setCourseLength(educationalLength);
    module.setLastModifier(user);
    module.setLastModified(now);
    module.setMaxParticipantCount(maxParticipantCount);
    module.setCurriculum(curriculum);
    
    entityManager.persist(module);
    
    return module;
  }

  @SuppressWarnings("unchecked")
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
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    QueryParser parser = new QueryParser(Version.LUCENE_36, "", new StandardAnalyzer(Version.LUCENE_36));
    String queryString = queryBuilder.toString();
    Query luceneQuery;

    try {
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, Module.class)
          .setSort(new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("nameSortable", SortField.STRING) })).setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      query.enableFullTextFilter("ArchivedModule").setParameter("archived", Boolean.FALSE);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());
    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  public SearchResult<Module> searchModules(int resultsPerPage, int page, String projectName, String name, String tags, String description,
      String componentName, String componentDescription, Long ownerId, boolean filterArchived) {
    // Search with null Subject and null EducationSubtype
    return searchModules(resultsPerPage, page, projectName, name, tags, description, componentName, componentDescription, ownerId, null, null, null,
        filterArchived);
  }

  @SuppressWarnings("unchecked")
  public SearchResult<Module> searchModules(int resultsPerPage, int page, String projectName, String name, String tags, String description,
      String componentName, String componentDescription, Long ownerId, Subject subject, EducationType educationType, EducationSubtype educationSubtype,
      boolean filterArchived) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    boolean hasName = !StringUtils.isBlank(name);
    boolean hasTags = !StringUtils.isBlank(tags);
    boolean hasDescription = !StringUtils.isBlank(description);
    boolean hasComponentName = !StringUtils.isBlank(componentName);
    boolean hasComponentDescription = !StringUtils.isBlank(componentDescription);
    boolean hasSubject = subject != null;
    boolean hasEduType = educationType != null;
    boolean hasEduSubtype = educationSubtype != null;

    if (hasName || hasTags || hasDescription || hasComponentName || hasComponentDescription || hasSubject || hasEduType || hasEduSubtype) {
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

      if (hasSubject)
        addTokenizedSearchCriteria(queryBuilder, "subject.id", subject.getId().toString(), true);

      if (hasEduType)
        addTokenizedSearchCriteria(queryBuilder, "courseEducationTypes.educationType.id", educationType.getId().toString(), true);

      if (hasEduSubtype)
        addTokenizedSearchCriteria(queryBuilder, "courseEducationTypes.courseEducationSubtypes.educationSubtype.id", educationSubtype.getId().toString(), true);

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
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      if (ownerId != null && ownerId > 0) {
        queryBuilder.append(" +creator.id: ").append(ownerId);
      }

      QueryParser parser = new QueryParser(Version.LUCENE_36, "", new StandardAnalyzer(Version.LUCENE_36));
      String queryString = queryBuilder.toString();
      Query luceneQuery;

      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, Module.class)
          .setSort(new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("nameSortable", SortField.STRING) })).setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      if (filterArchived)
        query.enableFullTextFilter("ArchivedModule").setParameter("archived", Boolean.FALSE);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());

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
