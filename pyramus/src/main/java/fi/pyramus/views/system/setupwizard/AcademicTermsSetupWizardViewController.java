package fi.pyramus.views.system.setupwizard;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AcademicTermDAO;
import fi.pyramus.domainmodel.base.AcademicTerm;

public class AcademicTermsSetupWizardViewController extends SetupWizardController {

  public AcademicTermsSetupWizardViewController() {
    super("academicterms");
  }

  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();
    
    List<AcademicTerm> academicTerms = academicTermDAO.listUnarchived();
    Collections.sort(academicTerms, new Comparator<AcademicTerm>() {
      public int compare(AcademicTerm o1, AcademicTerm o2) {
        return o1.getStartDate() == null ? -1 : o2.getStartDate() == null ? 1 : o1.getStartDate().compareTo(o2.getStartDate());
      }
    });
    
    JSONArray jsonAcademicTerms = new JSONArray();
    for (AcademicTerm academicTerm : academicTerms) {
      JSONObject jsonAcademicTerm = new JSONObject();
      jsonAcademicTerm.put("name", academicTerm.getName());
      if (academicTerm.getStartDate() != null) {
        jsonAcademicTerm.put("startDate", academicTerm.getStartDate().getTime());
      }
      if (academicTerm.getEndDate() != null) {
        jsonAcademicTerm.put("endDate", academicTerm.getEndDate().getTime());
      }
      jsonAcademicTerm.put("id", academicTerm.getId());
      jsonAcademicTerms.add(jsonAcademicTerm);
    }

    this.setJsDataVariable(requestContext, "academicTerms", jsonAcademicTerms.toString());

  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();

    int rowCount = requestContext.getInteger("termsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "termsTable." + i;
      Long termId = requestContext.getLong(colPrefix + ".termId");
      String name = requestContext.getString(colPrefix + ".name");
      Date startDate =  requestContext.getDate(colPrefix + ".startDate");
      Date endDate = requestContext.getDate(colPrefix + ".endDate");
      
      if (termId == null) {
        throw new SetupWizardException("Academic term is missing!");
      }
      
      academicTermDAO.create(name, startDate, endDate); 
      
    }

  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    // TODO Auto-generated method stub
    return false;
  }

}
