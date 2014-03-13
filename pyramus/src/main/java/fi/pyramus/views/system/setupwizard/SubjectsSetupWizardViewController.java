package fi.pyramus.views.system.setupwizard;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

public class SubjectsSetupWizardViewController extends SetupWizardController {
  
  public SubjectsSetupWizardViewController() {
    super("subjects");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    

    List<Subject> subjects = subjectDAO.listUnarchived();
    Collections.sort(subjects, new StringAttributeComparator("getName"));

    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    
    JSONArray jsonSubjects = new JSONArrayExtractor("name", "code", "id").extract(subjects);
    for (int i=0; i<jsonSubjects.size(); i++) {
      JSONObject jsonStudyProgrammeCategory = jsonSubjects.getJSONObject(i);
      if (subjects.get(i).getEducationType() != null)
        jsonStudyProgrammeCategory.put("educationTypeId", subjects.get(i).getEducationType().getId());
    }
    String jsonEducationTypes = new JSONArrayExtractor("name", "id").extractString(educationTypes);

    this.setJsDataVariable(requestContext, "subjects", jsonSubjects.toString());
    this.setJsDataVariable(requestContext, "educationTypes", jsonEducationTypes);
  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    


    int rowCount = requestContext.getInteger("subjectsTable.rowCount");
    Set<Long> removedSubjectIds = new HashSet<Long>();
    for (Subject subject : subjectDAO.listAll()) {
      removedSubjectIds.add(subject.getId());
    }
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "subjectsTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      String code = requestContext.getString(colPrefix + ".code");
      Long educationTypeId = requestContext.getLong(colPrefix + ".educationTypeId");
      
      EducationType educationType = null;
      if (educationTypeId != null) {
          educationType = educationTypeDAO.findById(educationTypeId);
      }
      subjectDAO.create(code, name, educationType);
    }
    for (Long removedSubjectId : removedSubjectIds) {
      subjectDAO.archive(subjectDAO.findById(removedSubjectId));
    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    return !subjectDAO.listAll().isEmpty();
  }
}
