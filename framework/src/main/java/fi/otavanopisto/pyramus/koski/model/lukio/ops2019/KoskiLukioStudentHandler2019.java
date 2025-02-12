package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.grading.SpokenLanguageExamDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.SpokenLanguageExam;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.KoskiConsts;
import fi.otavanopisto.pyramus.koski.KoskiStudentId;
import fi.otavanopisto.pyramus.koski.KoskiStudyProgrammeHandler;
import fi.otavanopisto.pyramus.koski.OpiskelijanOPS;
import fi.otavanopisto.pyramus.koski.StudentSubjectSelections;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoKehittyvanKielitaidonTasot;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.koodisto.LukionOppimaara;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.settings.StudyEndReasonMapping;

public class KoskiLukioStudentHandler2019 extends AbstractKoskiLukioStudentHandler2019 {

  public static final String USERVARIABLE_UNDER18START = KoskiConsts.UserVariables.STARTED_UNDER18;
  public static final String USERVARIABLE_UNDER18STARTREASON = KoskiConsts.UserVariables.UNDER18_STARTREASON;
  private static final KoskiStudyProgrammeHandler HANDLER_TYPE = KoskiStudyProgrammeHandler.lukio;

  @Inject
  private Logger logger;
  
  @Inject
  private SpokenLanguageExamDAO spokenLanguageExamDAO;

  public Opiskeluoikeus oppimaaranOpiskeluoikeus(Student student, String academyIdentifier, KoskiStudyProgrammeHandler handler) {
    if (handler != HANDLER_TYPE) {
      logger.log(Level.SEVERE, String.format("Wrong handler type %s, expected %s w/person %d.", handler, HANDLER_TYPE, student.getPerson().getId()));
      return null;
    }
    
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
    opiskeluoikeus.setLahdejarjestelmanId(getLahdeJarjestelmaID(handler, student.getId()));
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
    opiskeluoikeus.setOppimaaraSuoritettu(sisällytäVahvistus);

    String departmentIdentifier = settings.getToimipisteOID(student.getStudyProgramme().getId(), academyIdentifier);
    
    OrganisaationToimipiste toimipiste = new OrganisaationToimipisteOID(departmentIdentifier);
    EducationType studentEducationType = student.getStudyProgramme() != null && student.getStudyProgramme().getCategory() != null ? 
        student.getStudyProgramme().getCategory().getEducationType() : null;
    
    // NON-COMMON
    
    List<LukionOsasuoritus2019> oppiaineet = assessmentsToModel(ops, student, studentEducationType, studentSubjects, laskeKeskiarvot);

    LukionOppimaaranSuoritus2019 suoritus = new LukionOppimaaranSuoritus2019(
        LukionOppimaara.aikuistenops, Kieli.FI, toimipiste);
    suoritus.getKoulutusmoduuli().setPerusteenDiaarinumero(getDiaarinumero(student));
    suoritus.setTodistuksellaNakyvatLisatiedot(getTodistuksellaNakyvatLisatiedot(student));
    if (sisällytäVahvistus) {
      suoritus.setVahvistus(getVahvistus(student, departmentIdentifier));
    }
    opiskeluoikeus.addSuoritus(suoritus);
    
    oppiaineet.forEach(oppiaine -> suoritus.addOsasuoritus(oppiaine));

    // Suullisen kielitaidon kokeet
    
    List<SpokenLanguageExam> spokenLanguageExams = spokenLanguageExamDAO.listByStudent(student);
    
    for (SpokenLanguageExam spokenLanguageExam : spokenLanguageExams) {
      Kielivalikoima kieli = null;
      
      if (spokenLanguageExam.getCredit() instanceof CourseAssessment) {
        CourseAssessment ca = (CourseAssessment) spokenLanguageExam.getCredit();
        if (ca.getSubject() != null && StringUtils.isNotBlank(ca.getSubject().getCode())) {
          kieli = EnumUtils.getEnum(Kielivalikoima.class, ca.getSubject().getCode().substring(0, 2));
        }
      }
      else if (spokenLanguageExam.getCredit() instanceof TransferCredit) {
        TransferCredit tc = (TransferCredit) spokenLanguageExam.getCredit();
        if (tc.getSubject() != null && StringUtils.isNotBlank(tc.getSubject().getCode())) {
          kieli = EnumUtils.getEnum(Kielivalikoima.class, tc.getSubject().getCode().substring(0, 2));
        }
      }
      
      if (kieli == null) {
        koskiPersonLogDAO.create(student.getPerson(), student, KoskiPersonState.GENERIC_ERROR, new Date(), "Suullisen kielitaidon kokeen kieltä ei pystytty määrittämään.");
        continue;
      }
      
      ArviointiasteikkoYleissivistava arvosana = getArvosana(spokenLanguageExam.getGrade());
      ArviointiasteikkoKehittyvanKielitaidonTasot taitotaso = EnumUtils.getEnum(ArviointiasteikkoKehittyvanKielitaidonTasot.class, spokenLanguageExam.getSkillLevel().name());
      LocalDate paiva = spokenLanguageExam.getTimestamp().toLocalDate();
      
      SuullisenKielitaidonKoe2019 suko = new SuullisenKielitaidonKoe2019(
          kieli,
          arvosana, 
          taitotaso, 
          paiva);
      suoritus.addSuullisenKielitaidonKoe(suko);
    }
    
    return opiskeluoikeus;
  }

  @Override
  public void saveOrValidateOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    if (handler == HANDLER_TYPE) {
      saveOrValidateOid(student, oid);
    } else {
      logger.severe(String.format("saveOrValidateOid called with wrong handler %s, expected %s ", handler, HANDLER_TYPE));
    }
  }

  @Override
  public void removeOid(KoskiStudyProgrammeHandler handler, Student student, String oid) {
    if (handler == HANDLER_TYPE) {
      removeOid(student, oid);
    } else {
      logger.severe(String.format("removeOid called with wrong handler %s, expected %s ", handler, HANDLER_TYPE));
    }
  }
  
  @Override
  public Set<KoskiStudentId> listOids(Student student) {
    String oid = userVariableDAO.findByUserAndKey(student, KoskiConsts.VariableNames.KOSKI_STUDYPERMISSION_ID);
    if (StringUtils.isNotBlank(oid)) {
      return new HashSet<>(Arrays.asList(new KoskiStudentId(getStudentIdentifier(HANDLER_TYPE, student.getId()), oid)));
    } else {
      return new HashSet<>();
    }
  }
  
}
