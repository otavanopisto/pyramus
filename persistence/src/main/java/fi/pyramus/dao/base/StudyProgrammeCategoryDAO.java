package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;

@Stateless
public class StudyProgrammeCategoryDAO extends PyramusEntityDAO<StudyProgrammeCategory> {

  public StudyProgrammeCategory create(String name, EducationType educationType) {
    EntityManager entityManager = getEntityManager();

    StudyProgrammeCategory studyProgrammeCategory = new StudyProgrammeCategory();
    studyProgrammeCategory.setArchived(Boolean.FALSE);
    studyProgrammeCategory.setName(name);
    studyProgrammeCategory.setEducationType(educationType);

    entityManager.persist(studyProgrammeCategory);

    return studyProgrammeCategory;
  }

  public StudyProgrammeCategory update(StudyProgrammeCategory studyProgrammeCategory, String name, EducationType educationType) {
    studyProgrammeCategory.setName(name);
    studyProgrammeCategory.setEducationType(educationType);

    return persist(studyProgrammeCategory);
  }

}
