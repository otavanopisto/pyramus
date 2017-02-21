package fi.otavanopisto.pyramus.views.system.setupwizard;

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

    "adminpassword",
    "educationtypes", 
    "educationsubtypes", 
    "subjects",
    "nationalities", 
    "languages", 
    "timeunits", 
    "academicterms",
    "schoolfields", 
    "schools", 
    "municipalities", 
    "curriculums",
    
    // Course
    
    "coursestates", 
    "coursetypes", 
    "courseparticipationtypes",
    "courseuserroles",
    
    // Student

    "studyendreasons", 
    "contacttypes", 
    "examinationtypes", 
    "studyprogrammecategories", 
    "studyprogrammes", 
    "studenteducationallevels", 
    "studentactivitytypes",
    
    // Final page
    
    "final"
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
