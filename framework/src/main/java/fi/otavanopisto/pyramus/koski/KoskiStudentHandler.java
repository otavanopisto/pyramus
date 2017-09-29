package fi.otavanopisto.pyramus.koski;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.model.Kuvaus;

public class KoskiStudentHandler {

  @Inject 
  private UserVariableDAO userVariableDAO;

  @Inject
  protected KoskiSettings settings;
  
  protected Kuvaus kuvaus(String fiKuvaus) {
    Kuvaus kuvaus = new Kuvaus();
    kuvaus.setFi(fiKuvaus);
    return kuvaus;
  }

  protected String getDiaarinumero(Student student) {
    Long studyProgrammeId = student.getStudyProgramme() != null ? student.getStudyProgramme().getId() : null;
    Long curriculumId = student.getCurriculum() != null ? student.getCurriculum().getId() : null;
    
    if ((studyProgrammeId != null) && (curriculumId != null))
      return settings.getDiaariNumero(studyProgrammeId, curriculumId);
    else
      return null;
  }

  protected ArviointiasteikkoYleissivistava getArvosana(Grade grade) {
    switch (grade.getName()) {
      case "4":
        return ArviointiasteikkoYleissivistava.GRADE_4;
      case "5":
        return ArviointiasteikkoYleissivistava.GRADE_5;
      case "6":
        return ArviointiasteikkoYleissivistava.GRADE_6;
      case "7":
        return ArviointiasteikkoYleissivistava.GRADE_7;
      case "8":
        return ArviointiasteikkoYleissivistava.GRADE_8;
      case "9":
        return ArviointiasteikkoYleissivistava.GRADE_9;
      case "10":
        return ArviointiasteikkoYleissivistava.GRADE_10;
      case "H":
        return ArviointiasteikkoYleissivistava.GRADE_H;
      case "S":
        return ArviointiasteikkoYleissivistava.GRADE_S;
    }
    
    return null;
  }

  protected StudentSubjectSelections loadStudentSubjectSelections(Student student, OpiskeluoikeudenTyyppi opiskeluoikeudenTyyppi) {
    StudentSubjectSelections studentSubjects = new StudentSubjectSelections();
    
    String math = userVariableDAO.findByUserAndKey(student, "lukioMatematiikka");
    String lang = userVariableDAO.findByUserAndKey(student, "lukioAidinkieli");
    String aLang = userVariableDAO.findByUserAndKey(student, "lukioKieliA");
    String a1Lang = userVariableDAO.findByUserAndKey(student, "lukioKieliA1");
    String a2Lang = userVariableDAO.findByUserAndKey(student, "lukioKieliA2");
    String b1Lang = userVariableDAO.findByUserAndKey(student, "lukioKieliB1");
    String b2Lang = userVariableDAO.findByUserAndKey(student, "lukioKieliB2");
    String b3Lang = userVariableDAO.findByUserAndKey(student, "lukioKieliB3");

    if (StringUtils.isNotBlank(math))
      studentSubjects.setMath(math);
    if (StringUtils.isNotBlank(lang))
      studentSubjects.setPrimaryLanguage(lang);

    if (StringUtils.isNotBlank(aLang))
      studentSubjects.setALanguages(aLang);
    if (StringUtils.isNotBlank(a1Lang))
      studentSubjects.setA1Languages(a1Lang);
    if (StringUtils.isNotBlank(a2Lang))
      studentSubjects.setA2Languages(a2Lang);
    
    if (StringUtils.isNotBlank(b1Lang))
      studentSubjects.setB1Languages(b1Lang);
    if (StringUtils.isNotBlank(b2Lang))
      studentSubjects.setB2Languages(b2Lang);
    if (StringUtils.isNotBlank(b3Lang))
      studentSubjects.setB3Languages(b3Lang);

    if (StringUtils.isBlank(studentSubjects.getMath())) {
      switch (opiskeluoikeudenTyyppi) {
        case lukiokoulutus:
          studentSubjects.setMath("MAB");
        break;
        
        default:
        break;
      }
    }
    
    if (StringUtils.isBlank(studentSubjects.getPrimaryLanguage())) {
      switch (opiskeluoikeudenTyyppi) {
        case lukiokoulutus:
          studentSubjects.setPrimaryLanguage("AI");
        break;
        
        case aikuistenperusopetus:
          studentSubjects.setPrimaryLanguage("äi");
        break;

        default:
        break;
      }
    }

    
    return studentSubjects;
  }
  
}
