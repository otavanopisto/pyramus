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

    // Base

    "languages", 
    "educationtypes", 
    "educationsubtypes", 
    "subjects",
    "nationalities", 
    "timeunits", 
    "schoolfields", 
    "schools", 
    
    "municipalities", 
    
    // Course
    
    "coursestates", 
    "courseparticipationtypes", 
    
    // Student

    "studyendreasons", 
    "contacttypes", 
    "studyprogrammecategories", 
    "studyprogrammes", 
    "studenteducationallevels", 
    "studentactivitytypes", 
    
    // Credits
    
    "transfercredittemplates", 
  };
  
  public String next(String phase) {
    int index = getPhaseIndex(phase);
    
    if (((index + 1) < getPhaseCount())) {
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
  
  public String[] getPhases() {
    return PHASES;
  }
  
}
