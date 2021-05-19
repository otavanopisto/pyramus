package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.KoskiConsts;
import fi.otavanopisto.pyramus.koski.KoskiStudentId;
import fi.otavanopisto.pyramus.koski.KoskiStudyProgrammeHandler;
import fi.otavanopisto.pyramus.koski.OpiskelijanOPS;
import fi.otavanopisto.pyramus.koski.StudentSubjectSelections;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.LukionOppimaara;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.settings.StudyEndReasonMapping;

/**
 * Käsittelijä 2019 (2021) lukion opetussuunnitelman aineopiskelijoille.
 */
public class KoskiInternetixLukioStudentHandler2019 extends AbstractKoskiLukioStudentHandler2019 {

  public static final String USERVARIABLE_UNDER18START = KoskiConsts.UserVariables.STARTED_UNDER18;
  public static final String USERVARIABLE_UNDER18STARTREASON = KoskiConsts.UserVariables.UNDER18_STARTREASON;
  private static final KoskiStudyProgrammeHandler HANDLER_TYPE = KoskiStudyProgrammeHandler.aineopiskelulukio;

  public Opiskeluoikeus oppiaineidenOppimaaranOpiskeluoikeus(Student student, String academyIdentifier) {
    StudentSubjectSelections studentSubjects = loadStudentSubjectSelections(student, getDefaultSubjectSelections());
    String studyOid = userVariableDAO.findByUserAndKey(student, KOSKI_STUDYPERMISSION_ID);

    // Skip student if it is archived and the studyoid is blank
    if (Boolean.TRUE.equals(student.getArchived()) && StringUtils.isBlank(studyOid)) {
      return null;
    }
    
    OpiskelijanOPS ops = resolveOPS(student);
    if (ops == null) {
      koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.NO_CURRICULUM, new Date());
      return null;
    }
    
    if (student.getStudyStartDate() == null) {
      koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.NO_STUDYSTARTDATE, new Date());
      return null;
    }
    
    LukionOpiskeluoikeus opiskeluoikeus = new LukionOpiskeluoikeus();
    opiskeluoikeus.setLahdejarjestelmanId(getLahdeJarjestelmaID(HANDLER_TYPE, student.getId()));
    opiskeluoikeus.setAlkamispaiva(student.getStudyStartDate());
    opiskeluoikeus.setPaattymispaiva(student.getStudyEndDate());
    if (StringUtils.isNotBlank(studyOid)) {
      opiskeluoikeus.setOid(studyOid);
    }

    opiskeluoikeus.setLisatiedot(getLisatiedot(student));

    OpintojenRahoitus opintojenRahoitus = opintojenRahoitus(student);
    StudyEndReasonMapping lopetusSyy = opiskelujaksot(student, opiskeluoikeus.getTila(), opintojenRahoitus);
    boolean laskeKeskiarvot = lopetusSyy != null ? lopetusSyy.getLaskeAinekeskiarvot() : false;
    boolean sisällytäVahvistus = lopetusSyy != null ? lopetusSyy.getSisällytäVahvistaja() : false;

    String departmentIdentifier = settings.getToimipisteOID(student.getStudyProgramme().getId(), academyIdentifier);
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(departmentIdentifier);
    EducationType studentEducationType = student.getStudyProgramme() != null && student.getStudyProgramme().getCategory() != null ? 
        student.getStudyProgramme().getCategory().getEducationType() : null;
    
    Set<LukionOsasuoritus2019> oppiaineet = assessmentsToModel(ops, student, studentEducationType, studentSubjects, laskeKeskiarvot);

    LukionOppiaineenOppimaaranSuoritus2019 suoritus = new LukionOppiaineenOppimaaranSuoritus2019(
        LukionOppimaara.aikuistenops, Kieli.FI, toimipiste, getDiaarinumero(student));
    suoritus.setTodistuksellaNakyvatLisatiedot(getTodistuksellaNakyvatLisatiedot(student));
    if (sisällytäVahvistus) {
      suoritus.setVahvistus(getVahvistus(student, departmentIdentifier));
    }
    opiskeluoikeus.addSuoritus(suoritus);
    
    for (LukionOsasuoritus2019 oppiaine : oppiaineet) {
      // Aineopinnot sallivat vain oppiaineen suorituksia
      if (oppiaine instanceof LukionOppiaineenSuoritus2019) {
        suoritus.addOsasuoritus((LukionOppiaineenSuoritus2019) oppiaine);
      } else {
        String tyyppi = String.valueOf(oppiaine.getTyyppi().getValue());
        koskiPersonLogDAO.create(student.getPerson(), KoskiPersonState.GENERIC_WARNING, new Date(), tyyppi);
      }
    }
    
    return opiskeluoikeus;
  }

  @Override
  public void saveOrValidateOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    saveOrValidateInternetixOid(handler, student, oid);
  }
  
  @Override
  public void removeOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    removeInternetixOid(handler, student, oid);
  }
  
  @Override
  public Set<KoskiStudentId> listOids(Student student) {
    return loadInternetixOids(student);
  }

}
