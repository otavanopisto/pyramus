package fi.otavanopisto.pyramus.koski;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.PersonStudentComparator;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.exception.NoLatestStudentException;
import fi.otavanopisto.pyramus.koski.model.Henkilo;
import fi.otavanopisto.pyramus.koski.model.HenkiloTiedotJaOID;
import fi.otavanopisto.pyramus.koski.model.HenkiloUusi;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.KoskiAikuistenPerusopetuksenStudentHandler;
import fi.otavanopisto.pyramus.koski.model.apa.KoskiAPAStudentHandler;
import fi.otavanopisto.pyramus.koski.model.internetix.KoskiInternetixStudentHandler;
import fi.otavanopisto.pyramus.koski.model.lukio.KoskiLukioStudentHandlerDelegator;

@ApplicationScoped
public class KoskiController {

  @Inject
  private Logger logger;
  
  @Inject
  private KoskiSettings settings;
  
  @Inject
  private KoskiAPAStudentHandler apaHandler;

  @Inject
  private KoskiLukioStudentHandlerDelegator lukioHandler;
  
  @Inject
  private KoskiInternetixStudentHandler internetixHandler;

  @Inject
  private KoskiAikuistenPerusopetuksenStudentHandler aikuistenPerusopetuksenHandler;

  @Inject 
  private PersonVariableDAO personVariableDAO;
  
  /**
   * Lists student OIDs with the currently active handler for it (i.e. the ones that should currently be in use)
   */
  public Set<String> listStudentOIDs(Student student) {
    KoskiStudyProgrammeHandler handlerType = settings.getStudyProgrammeHandlerType(student.getStudyProgramme().getId());
    KoskiStudentHandler studentHandler = handlerType != null ? getStudentHandler(handlerType) : null;
    if (studentHandler != null) {
      Set<KoskiStudentId> koskiStudentIds = studentHandler.listOids(student);
      return koskiStudentIds.stream()
          .map(koskiStudentId -> koskiStudentId.getOid())
          .collect(Collectors.toSet());
    } else {
      return Collections.emptySet();
    }
  }
  
  public KoskiStudentHandler getStudentHandler(KoskiStudyProgrammeHandler handlerType) {
    switch (handlerType) {
      case aikuistenperusopetus:
        return aikuistenPerusopetuksenHandler;
      case lukio:
        return lukioHandler;
      case aineopiskeluperusopetus:
      case aineopiskelulukio:
        return internetixHandler;
      case aikuistenperusopetuksenalkuvaihe:
        return apaHandler;

      default:
        logger.severe(String.format("Handler for type %s couldn't be determined.", handlerType));
        return null;
    }
  }

  public Oppija personToOppija(Person person) throws NoLatestStudentException {
    Student latestStudent = resolveLatestStudent(person);
    
    if (latestStudent == null) {
      throw new NoLatestStudentException();
    }

    String personOid = personVariableDAO.findByPersonAndKey(person, KoskiConsts.VariableNames.KOSKI_HENKILO_OID);

    Henkilo henkilo;
    if (StringUtils.isNotBlank(personOid)) {
      henkilo = new HenkiloTiedotJaOID(personOid, person.getSocialSecurityNumber(), latestStudent.getFirstName(), latestStudent.getLastName(), getCallname(latestStudent));
    } else {
      henkilo = new HenkiloUusi(person.getSocialSecurityNumber(), latestStudent.getFirstName(), latestStudent.getLastName(), getCallname(latestStudent));
    }
    
    Oppija oppija = new Oppija();
    oppija.setHenkilo(henkilo);
    
    List<Student> reportedStudents = new ArrayList<>();
    
    for (Student s : person.getStudents()) {
      if (settings.isReportedStudent(s)) {
        List<Opiskeluoikeus> opiskeluoikeudet = studentToOpiskeluoikeus(s);
        for (Opiskeluoikeus o : opiskeluoikeudet) {
          if (o != null) {
            oppija.addOpiskeluoikeus(o);
            reportedStudents.add(s);
          }
        }
      }
    }
    
    return oppija;
  }
  
  public List<Opiskeluoikeus> studentToOpiskeluoikeus(Student student) {
    KoskiStudyProgrammeHandler handler = settings.getStudyProgrammeHandlerType(student.getStudyProgramme().getId());
    switch (handler) {
      case aikuistenperusopetus:
        return asList(aikuistenPerusopetuksenHandler.studentToModel(student, settings.getAcademyIdentifier(), KoskiStudyProgrammeHandler.aikuistenperusopetus));
      case lukio:
        return asList(lukioHandler.studentToModel(student, settings.getAcademyIdentifier(), KoskiStudyProgrammeHandler.lukio));
      case aineopiskeluperusopetus:
      case aineopiskelulukio:
        return internetixHandler.studentToModel(student, settings.getAcademyIdentifier());
      case aikuistenperusopetuksenalkuvaihe:
        return asList(apaHandler.studentToModel(student, settings.getAcademyIdentifier()));

      default:
        logger.log(Level.WARNING, String.format("Student %d with studyprogramme %s was not reported to Koski because no handler was specified.", 
            student.getId(), student.getStudyProgramme().getName()));
        return null;
    }
  }

  private Student resolveLatestStudent(Person person) {
    Student student = person.getLatestStudent();
    if (student != null) {
      return student;
    } else {
      List<Student> students = person.getStudents();
      if (CollectionUtils.isNotEmpty(students)) {
        return students.stream()
          .sorted(new PersonStudentComparator())
          .findFirst()
          .orElse(null);
      } else {
        logger.log(Level.WARNING, String.format("Could not resolve latest student for person %d", person != null ? person.getId() : null));
        return null;
      }
    }
  }

  private String getCallname(Student student) {
    if (StringUtils.isNotBlank(student.getNickname()) && (StringUtils.containsIgnoreCase(student.getFirstName(), student.getNickname())))
      return student.getNickname();
    else {
      if (StringUtils.isNotBlank(student.getFirstName())) {
        String[] split = StringUtils.split(StringUtils.trim(student.getFirstName()), ' ');
        if (split != null && split.length > 0)
          return split[0];
      }
    }
    
    return null;
  }

  private <T> List<T> asList(T o) {
    List<T> list = new ArrayList<T>();
    list.add(o);
    return list;
  }

}
