package fi.otavanopisto.pyramus.koski.lukio2019;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.Test;

import io.restassured.response.Response;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.HenkiloTiedotJaOID;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipisteOID;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppimaaranSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOsasuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionSuoritus;
import fi.otavanopisto.pyramus.rest.AbstractRESTServiceTest;
import fi.otavanopisto.pyramus.rest.model.Course;
import fi.otavanopisto.pyramus.rest.model.CourseAssessment;
import fi.otavanopisto.pyramus.rest.model.CourseStudent;
import fi.otavanopisto.pyramus.rest.model.Person;
import fi.otavanopisto.pyramus.rest.model.Student;


public class KoskiLukio2019StudentTestsIT extends AbstractRESTServiceTest {

  private static final Long ORGANIZATIONID = 1l;

  @Test
  public void testBasicInformation() {
    Person person = tools().createPerson();
    Student student = tools().createStudent(person.getId(), 1l);
    Course course = tools().createCourse(getClass().getSimpleName(), ORGANIZATIONID, 4l, 1);
    CourseStudent courseStudent = tools().createCourseStudent(course.getId(), student.getId());
    CourseAssessment courseAssessment = tools().createCourseAssessment(course.getId(), firstCourseModuleId(course), student.getId(), courseStudent.getId(), 7l);
    try {
      Response response = given().headers(getAuthHeaders())
        .get("/persons/persons/{ID}/oppija", person.getId());
      
      response
        .then()
        .statusCode(200);

      Oppija oppija = response.getBody().as(Oppija.class);

      assertNotNull(oppija);
      assertEquals(1, oppija.getOpiskeluoikeudet().size());
      
      assertTrue(oppija.getHenkilo() instanceof HenkiloTiedotJaOID);
      HenkiloTiedotJaOID henkilo = (HenkiloTiedotJaOID) oppija.getHenkilo();
      assertEquals(student.getFirstName(), henkilo.getEtunimet());
      assertEquals(student.getLastName(), henkilo.getSukunimi());

      Opiskeluoikeus opiskeluoikeus = oppija.getOpiskeluoikeudet().get(0);
      assertTrue(opiskeluoikeus instanceof LukionOpiskeluoikeus);
      assertEquals(OpiskeluoikeudenTyyppi.lukiokoulutus, opiskeluoikeus.getTyyppi().getValue());
      assertEquals(toLocalDate(student.getStudyStartDate()), toLocalDate(opiskeluoikeus.getAlkamispaiva()));
      assertEquals(toLocalDate(student.getStudyEndDate()), toLocalDate(opiskeluoikeus.getPaattymispaiva()));
      
      LukionOpiskeluoikeus lukionOpiskeluoikeus = (LukionOpiskeluoikeus) opiskeluoikeus;
      assertEquals(1, lukionOpiskeluoikeus.getSuoritukset().size());
      
      LukionSuoritus lukionSuoritus = lukionOpiskeluoikeus.getSuoritukset().iterator().next();
      assertEquals(SuorituksenTyyppi.lukionoppimaara, lukionSuoritus.getTyyppi().getValue());
      
      assertTrue(lukionSuoritus instanceof LukionOppimaaranSuoritus);
      LukionOppimaaranSuoritus lukionOppimaaranSuoritus = (LukionOppimaaranSuoritus) lukionSuoritus;

      OrganisaationToimipisteOID toimipiste = (OrganisaationToimipisteOID) lukionSuoritus.getToimipiste();
      assertEquals("1.1.000.000.00.00000000000", toimipiste.getOid());
      assertEquals("70/011/2015", lukionOppimaaranSuoritus.getKoulutusmoduuli().getPerusteenDiaarinumero());
      
      /**
       * Subject
       */
      assertEquals(1, lukionOppimaaranSuoritus.getOsasuoritukset().size());
      LukionOsasuoritus subject = lukionOppimaaranSuoritus.getOsasuoritukset().iterator().next();
      
      assertTrue(subject instanceof LukionOppiaineenSuoritus);
      LukionOppiaineenSuoritus lukionOppiaineenSuoritus = (LukionOppiaineenSuoritus) subject;
      
      assertEquals(1, lukionOppiaineenSuoritus.getOsasuoritukset().size());
      LukionKurssinSuoritus lukionKurssinSuoritus = lukionOppiaineenSuoritus.getOsasuoritukset().get(0);

      /**
       * Grade
       */
      assertEquals(1, lukionKurssinSuoritus.getArviointi().size());
      KurssinArviointi kurssinArviointi = lukionKurssinSuoritus.getArviointi().get(0);
      assertEquals(ArviointiasteikkoYleissivistava.GRADE_8, kurssinArviointi.getArvosana().getValue());
      
    } finally {
      tools().deleteCourseAssessment(course.getId(), student.getId(), courseAssessment);
      tools().deleteCourseStudent(courseStudent);
      tools().deleteCourse(course);
      tools().deleteStudent(student);
      tools().deletePerson(person);
    }
  }
  
  @Test
  public void testDoubleAssessmentPre2022() {
    OffsetDateTime firstAssessmentDate = OffsetDateTime.of(2021, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC);
    OffsetDateTime secondAssessmentDate = OffsetDateTime.of(2021, 2, 1, 12, 0, 0, 0, ZoneOffset.UTC);
    
    Person person = tools().createPerson();
    Student student = tools().createStudent(person.getId(), 1l);
    Course course = tools().createCourse(getClass().getSimpleName(), ORGANIZATIONID, 4l, 1);
    CourseStudent courseStudent = tools().createCourseStudent(course.getId(), student.getId());
    CourseAssessment courseAssessment = tools().createCourseAssessment(course.getId(), firstCourseModuleId(course), student.getId(), courseStudent.getId(), 7l, firstAssessmentDate);
    CourseAssessment courseAssessment2 = tools().createCourseAssessment(course.getId(), firstCourseModuleId(course), student.getId(), courseStudent.getId(), 8l, secondAssessmentDate);
    try {
      Response response = given().headers(getAuthHeaders())
        .get("/persons/persons/{ID}/oppija", person.getId());
      
      response
        .then()
        .statusCode(200);

      Oppija oppija = response.getBody().as(Oppija.class);

      assertNotNull(oppija);
      assertEquals(1, oppija.getOpiskeluoikeudet().size());
      
      assertTrue(oppija.getHenkilo() instanceof HenkiloTiedotJaOID);
      HenkiloTiedotJaOID henkilo = (HenkiloTiedotJaOID) oppija.getHenkilo();
      assertEquals(student.getFirstName(), henkilo.getEtunimet());
      assertEquals(student.getLastName(), henkilo.getSukunimi());

      Opiskeluoikeus opiskeluoikeus = oppija.getOpiskeluoikeudet().get(0);
      assertTrue(opiskeluoikeus instanceof LukionOpiskeluoikeus);
      assertEquals(OpiskeluoikeudenTyyppi.lukiokoulutus, opiskeluoikeus.getTyyppi().getValue());
      assertEquals(toLocalDate(student.getStudyStartDate()), toLocalDate(opiskeluoikeus.getAlkamispaiva()));
      assertEquals(toLocalDate(student.getStudyEndDate()), toLocalDate(opiskeluoikeus.getPaattymispaiva()));

      LukionOpiskeluoikeus lukionOpiskeluoikeus = (LukionOpiskeluoikeus) opiskeluoikeus;
      assertEquals(1, lukionOpiskeluoikeus.getSuoritukset().size());
      
      LukionSuoritus lukionSuoritus = lukionOpiskeluoikeus.getSuoritukset().iterator().next();
      assertEquals(SuorituksenTyyppi.lukionoppimaara, lukionSuoritus.getTyyppi().getValue());
      
      assertTrue(lukionSuoritus instanceof LukionOppimaaranSuoritus);
      LukionOppimaaranSuoritus lukionOppimaaranSuoritus = (LukionOppimaaranSuoritus) lukionSuoritus;

      OrganisaationToimipisteOID toimipiste = (OrganisaationToimipisteOID) lukionSuoritus.getToimipiste();
      assertEquals("1.1.000.000.00.00000000000", toimipiste.getOid());
      assertEquals("70/011/2015", lukionOppimaaranSuoritus.getKoulutusmoduuli().getPerusteenDiaarinumero());
      
      /**
       * Subject
       */
      assertEquals(1, lukionOppimaaranSuoritus.getOsasuoritukset().size());
      LukionOsasuoritus subject = lukionOppimaaranSuoritus.getOsasuoritukset().iterator().next();
      
      assertTrue(subject instanceof LukionOppiaineenSuoritus);
      LukionOppiaineenSuoritus lukionOppiaineenSuoritus = (LukionOppiaineenSuoritus) subject;
      
      assertEquals(1, lukionOppiaineenSuoritus.getOsasuoritukset().size());
      LukionKurssinSuoritus lukionKurssinSuoritus = lukionOppiaineenSuoritus.getOsasuoritukset().get(0);

      /**
       * Grade, only one as pre 2022 only best grade is delivered
       */
      assertEquals(1, lukionKurssinSuoritus.getArviointi().size());
      assertEquals(ArviointiasteikkoYleissivistava.GRADE_9, lukionKurssinSuoritus.getArviointi().get(0).getArvosana().getValue());
      
    } finally {
      tools().deleteCourseAssessment(course.getId(), student.getId(), courseAssessment2);
      tools().deleteCourseAssessment(course.getId(), student.getId(), courseAssessment);
      tools().deleteCourseStudent(courseStudent);
      tools().deleteCourse(course);
      tools().deleteStudent(student);
      tools().deletePerson(person);
    }
  }
      
  @Test
  public void testDoubleAssessmentPost2022() {
    OffsetDateTime firstAssessmentDate = OffsetDateTime.of(2021, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC);
    OffsetDateTime secondAssessmentDate = OffsetDateTime.of(2022, 2, 1, 12, 0, 0, 0, ZoneOffset.UTC);

    Person person = tools().createPerson();
    Student student = tools().createStudent(person.getId(), 1l);
    Course course = tools().createCourse(getClass().getSimpleName(), ORGANIZATIONID, 4l, 1);
    CourseStudent courseStudent = tools().createCourseStudent(course.getId(), student.getId());
    CourseAssessment courseAssessment = tools().createCourseAssessment(course.getId(), firstCourseModuleId(course), student.getId(), courseStudent.getId(), 7l, firstAssessmentDate);
    CourseAssessment courseAssessment2 = tools().createCourseAssessment(course.getId(), firstCourseModuleId(course), student.getId(), courseStudent.getId(), 8l, secondAssessmentDate);
    try {
      Response response = given().headers(getAuthHeaders())
        .get("/persons/persons/{ID}/oppija", person.getId());
      
      response
        .then()
        .statusCode(200);

      Oppija oppija = response.getBody().as(Oppija.class);

      assertNotNull(oppija);
      assertEquals(1, oppija.getOpiskeluoikeudet().size());
      
      assertTrue(oppija.getHenkilo() instanceof HenkiloTiedotJaOID);
      HenkiloTiedotJaOID henkilo = (HenkiloTiedotJaOID) oppija.getHenkilo();
      assertEquals(student.getFirstName(), henkilo.getEtunimet());
      assertEquals(student.getLastName(), henkilo.getSukunimi());

      Opiskeluoikeus opiskeluoikeus = oppija.getOpiskeluoikeudet().get(0);
      assertTrue(opiskeluoikeus instanceof LukionOpiskeluoikeus);
      assertEquals(OpiskeluoikeudenTyyppi.lukiokoulutus, opiskeluoikeus.getTyyppi().getValue());
      assertEquals(toLocalDate(student.getStudyStartDate()), toLocalDate(opiskeluoikeus.getAlkamispaiva()));
      assertEquals(toLocalDate(student.getStudyEndDate()), toLocalDate(opiskeluoikeus.getPaattymispaiva()));

      LukionOpiskeluoikeus lukionOpiskeluoikeus = (LukionOpiskeluoikeus) opiskeluoikeus;
      assertEquals(1, lukionOpiskeluoikeus.getSuoritukset().size());
      
      LukionSuoritus lukionSuoritus = lukionOpiskeluoikeus.getSuoritukset().iterator().next();
      assertEquals(SuorituksenTyyppi.lukionoppimaara, lukionSuoritus.getTyyppi().getValue());
      
      assertTrue(lukionSuoritus instanceof LukionOppimaaranSuoritus);
      LukionOppimaaranSuoritus lukionOppimaaranSuoritus = (LukionOppimaaranSuoritus) lukionSuoritus;

      OrganisaationToimipisteOID toimipiste = (OrganisaationToimipisteOID) lukionSuoritus.getToimipiste();
      assertEquals("1.1.000.000.00.00000000000", toimipiste.getOid());
      assertEquals("70/011/2015", lukionOppimaaranSuoritus.getKoulutusmoduuli().getPerusteenDiaarinumero());
      
      /**
       * Subject
       */
      assertEquals(1, lukionOppimaaranSuoritus.getOsasuoritukset().size());
      LukionOsasuoritus subject = lukionOppimaaranSuoritus.getOsasuoritukset().iterator().next();
      
      assertTrue(subject instanceof LukionOppiaineenSuoritus);
      LukionOppiaineenSuoritus lukionOppiaineenSuoritus = (LukionOppiaineenSuoritus) subject;
      
      assertEquals(1, lukionOppiaineenSuoritus.getOsasuoritukset().size());
      LukionKurssinSuoritus lukionKurssinSuoritus = lukionOppiaineenSuoritus.getOsasuoritukset().get(0);

      /**
       * Grade, both grades post 2022
       */
      assertEquals(2, lukionKurssinSuoritus.getArviointi().size());
      assertEquals(ArviointiasteikkoYleissivistava.GRADE_8, lukionKurssinSuoritus.getArviointi().get(0).getArvosana().getValue());
      assertEquals(ArviointiasteikkoYleissivistava.GRADE_9, lukionKurssinSuoritus.getArviointi().get(1).getArvosana().getValue());
      
    } finally {
      tools().deleteCourseAssessment(course.getId(), student.getId(), courseAssessment2);
      tools().deleteCourseAssessment(course.getId(), student.getId(), courseAssessment);
      tools().deleteCourseStudent(courseStudent);
      tools().deleteCourse(course);
      tools().deleteStudent(student);
      tools().deletePerson(person);
    }
  }
      
  @Test
  public void testDoubleAssessmentSorting() {
    OffsetDateTime newerAssessmentDate = OffsetDateTime.of(2022, 6, 1, 12, 0, 0, 0, ZoneOffset.UTC);
    OffsetDateTime olderAssessmentDate = OffsetDateTime.of(2022, 2, 1, 12, 0, 0, 0, ZoneOffset.UTC);
    
    Person person = tools().createPerson();
    Student student = tools().createStudent(person.getId(), 1l);
    Course course = tools().createCourse(getClass().getSimpleName(), ORGANIZATIONID, 4l, 1);
    CourseStudent courseStudent = tools().createCourseStudent(course.getId(), student.getId());
    CourseAssessment courseAssessment = tools().createCourseAssessment(course.getId(), firstCourseModuleId(course), student.getId(), courseStudent.getId(), 6l, newerAssessmentDate);
    CourseAssessment courseAssessment2 = tools().createCourseAssessment(course.getId(), firstCourseModuleId(course), student.getId(), courseStudent.getId(), 8l, olderAssessmentDate);
    try {
      Response response = given().headers(getAuthHeaders())
        .get("/persons/persons/{ID}/oppija", person.getId());
      
      response
        .then()
        .statusCode(200);

      Oppija oppija = response.getBody().as(Oppija.class);

      assertNotNull(oppija);
      assertEquals(1, oppija.getOpiskeluoikeudet().size());
      
      assertTrue(oppija.getHenkilo() instanceof HenkiloTiedotJaOID);
      HenkiloTiedotJaOID henkilo = (HenkiloTiedotJaOID) oppija.getHenkilo();
      assertEquals(student.getFirstName(), henkilo.getEtunimet());
      assertEquals(student.getLastName(), henkilo.getSukunimi());

      Opiskeluoikeus opiskeluoikeus = oppija.getOpiskeluoikeudet().get(0);
      assertTrue(opiskeluoikeus instanceof LukionOpiskeluoikeus);
      assertEquals(OpiskeluoikeudenTyyppi.lukiokoulutus, opiskeluoikeus.getTyyppi().getValue());
      assertEquals(toLocalDate(student.getStudyStartDate()), toLocalDate(opiskeluoikeus.getAlkamispaiva()));
      assertEquals(toLocalDate(student.getStudyEndDate()), toLocalDate(opiskeluoikeus.getPaattymispaiva()));

      LukionOpiskeluoikeus lukionOpiskeluoikeus = (LukionOpiskeluoikeus) opiskeluoikeus;
      assertEquals(1, lukionOpiskeluoikeus.getSuoritukset().size());
      
      LukionSuoritus lukionSuoritus = lukionOpiskeluoikeus.getSuoritukset().iterator().next();
      assertEquals(SuorituksenTyyppi.lukionoppimaara, lukionSuoritus.getTyyppi().getValue());
      
      assertTrue(lukionSuoritus instanceof LukionOppimaaranSuoritus);
      LukionOppimaaranSuoritus lukionOppimaaranSuoritus = (LukionOppimaaranSuoritus) lukionSuoritus;

      OrganisaationToimipisteOID toimipiste = (OrganisaationToimipisteOID) lukionSuoritus.getToimipiste();
      assertEquals("1.1.000.000.00.00000000000", toimipiste.getOid());
      assertEquals("70/011/2015", lukionOppimaaranSuoritus.getKoulutusmoduuli().getPerusteenDiaarinumero());
      
      /**
       * Subject
       */
      assertEquals(1, lukionOppimaaranSuoritus.getOsasuoritukset().size());
      LukionOsasuoritus subject = lukionOppimaaranSuoritus.getOsasuoritukset().iterator().next();
      
      assertTrue(subject instanceof LukionOppiaineenSuoritus);
      LukionOppiaineenSuoritus lukionOppiaineenSuoritus = (LukionOppiaineenSuoritus) subject;
      
      assertEquals(1, lukionOppiaineenSuoritus.getOsasuoritukset().size());
      LukionKurssinSuoritus lukionKurssinSuoritus = lukionOppiaineenSuoritus.getOsasuoritukset().get(0);

      /**
       * Grade, sorted
       */
      assertEquals(2, lukionKurssinSuoritus.getArviointi().size());
      assertNull(lukionKurssinSuoritus.getTunnustettu());
      assertEquals(ArviointiasteikkoYleissivistava.GRADE_9, lukionKurssinSuoritus.getArviointi().get(0).getArvosana().getValue());
      assertEquals(toLocalDate(olderAssessmentDate), toLocalDate(lukionKurssinSuoritus.getArviointi().get(0).getPaiva()));
      assertEquals(ArviointiasteikkoYleissivistava.GRADE_7, lukionKurssinSuoritus.getArviointi().get(1).getArvosana().getValue());
      assertEquals(toLocalDate(newerAssessmentDate), toLocalDate(lukionKurssinSuoritus.getArviointi().get(1).getPaiva()));
      
    } finally {
      tools().deleteCourseAssessment(course.getId(), student.getId(), courseAssessment2);
      tools().deleteCourseAssessment(course.getId(), student.getId(), courseAssessment);
      tools().deleteCourseStudent(courseStudent);
      tools().deleteCourse(course);
      tools().deleteStudent(student);
      tools().deletePerson(person);
    }
  }

  private Long firstCourseModuleId(Course course) {
    return course.getCourseModules().iterator().next().getId();    
  }
}
