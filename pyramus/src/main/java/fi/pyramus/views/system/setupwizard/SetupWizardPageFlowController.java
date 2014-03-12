package fi.pyramus.views.system.setupwizard;

import org.apache.commons.lang.ArrayUtils;

public class SetupWizardPageFlowController {
  
  public static synchronized SetupWizardPageFlowController getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SetupWizardPageFlowController();
    }
    
    return INSTANCE;
  }
  
  private static SetupWizardPageFlowController INSTANCE;
  
  private static final String[] PHASES = new String[] {
    "index",
    "studentactivitytypes", 
    "studyprogrammecategories", 
    "nationalities", 
    "timeunits", 
    "coursestates", 
    "contacttypes", 
    "schoolfields", 
    "courseparticipationtypes", 
    "studyendreasons", 
    "transfercredittemplates", 
    "schools", 
    "studenteducationallevels", 
    "municipalities", 
    "educationsubtypes", 
    "studyprogrammes", 
    "languages", 
    "educationtypes", 
    "subjects"
  };
  
  public String next(String phase) {
    int index = getPhaseIndex(phase);
    
    if (((index + 1) < (getPhaseCount() - 1))) {
      return PHASES[index + 1];
    } 
    
    return null;
  }

  public int getPhaseIndex(String phase) {
    return ArrayUtils.indexOf(PHASES, phase);
  }

  public int getPhaseCount() {
    return PHASES.length;
  }
  
  
  
}
