package fi.otavanopisto.pyramus.binary.ytl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.Part;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationGradeDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationGrade;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;
import fi.otavanopisto.pyramus.ytl.YTLAineKoodi;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UploadMatriculationCSVBinaryRequestController extends BinaryRequestController {

  public void process(BinaryRequestContext requestContext) {
    MatriculationExamDAO matriculationExamDAO = DAOFactory.getInstance().getMatriculationExamDAO();
    MatriculationExamEnrollmentDAO matriculationExamEnrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationGradeDAO matriculationGradeDAO = DAOFactory.getInstance().getMatriculationGradeDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();

    boolean save = Boolean.TRUE.equals(requestContext.getBoolean("save"));
    Part csvFile = requestContext.getFile("matriculationCSVFile");

    LocalDate gradeDate = LocalDate.now();
    StaffMember loggedUser = staffMemberDAO.findById(requestContext.getLoggedUserId());
    
    List<YTLAineKoodi> ytlAinekoodiMapping = YTLController.readMapping();

    // Result JSON object
    JSONArray rowMessages = new JSONArray();
    JSONObject result = new JSONObject();
    result.put("rowsOk", 0);
    
    // Jackson CSV parser
    CsvMapper csvMapper = new CsvMapper();
    CsvSchema csvSchema = CsvSchema.emptySchema().withColumnSeparator(';').withHeader();
    ObjectReader objectReader = csvMapper.readerFor(MatriculationResultCSVRow.class).with(csvSchema);
    
    try (Reader reader = new InputStreamReader(csvFile.getInputStream())) {
      MappingIterator<MatriculationResultCSVRow> matriculationResults = objectReader.readValues(reader);

      int rowNumber = 0;
      int rowsOk = 0;
      while (matriculationResults.hasNext()) {
        MatriculationResultCSVRow matriculationResult = matriculationResults.next();
        rowNumber++;

        // All the variables needed to save the result
        int examYear = -1;
        MatriculationExamTerm examTerm = null;
        MatriculationExam exam = null;
        MatriculationExamEnrollment examEnrollment = null;
        MatriculationExamSubject examSubject;
        MatriculationGrade matriculationGrade;

        // Sanity checks
        String tutkintokerta = matriculationResult.getTutkintokerta();
        if (StringUtils.isBlank(tutkintokerta) || StringUtils.length(tutkintokerta) != 5) {
          rowMessages.add(String.format("Rivi %d: Tutkintokerran tunniste ei vastaa määritystä (%s)", rowNumber, tutkintokerta));
          continue;
        }
        
        if (StringUtils.isBlank(matriculationResult.getArvosana())) {
          rowMessages.add(String.format("Rivi %d: Arvosana puuttuu", rowNumber));
          continue;
        }

        // Parse grade
        MatriculationExamGrade examGrade = MatriculationExamGrade.fromYTLGrade(matriculationResult.getArvosana());
        if (examGrade == null) {
          rowMessages.add(String.format("Rivi %d: Arvosanaa (%s) ei pystytty konvertoimaan Pyramuksen käyttämään muotoon", rowNumber, matriculationResult.getArvosana()));
          continue;
        }

        // Parse subject
        YTLAineKoodi ytlAineKoodi = YTLController.ytlKoeToYTLAineKoodi(matriculationResult.getKoe(), ytlAinekoodiMapping);
        if (ytlAineKoodi == null) {
          rowMessages.add(String.format("Rivi %d: Kokeen (%s) tietoja ei ole määritetty Pyramuksen YTL-integraatiossa.", rowNumber, matriculationResult.getKoe()));
          continue;
        }
        else {
          examSubject = ytlAineKoodi.getMatriculationExamSubject();
        }
        
        // Parse candidate number
        int candidateNumber = NumberUtils.toInt(matriculationResult.getKokelasnumero(), -1);
        if (candidateNumber == -1) {
          rowMessages.add(String.format("Rivi %d: Kokelasnumero (%s) ei ole numero.", rowNumber, matriculationResult.getKokelasnumero()));
          continue;
        }

        // Parse total points
        int totalPointsInt = NumberUtils.toInt(matriculationResult.getYhteispisteet(), -1);
        if (totalPointsInt == -1) {
          rowMessages.add(String.format("Rivi %d: Kokonaispistemäärä (%s) ei ole kokonaisluku. Pisteitä ei tallenneta riville.", rowNumber, matriculationResult.getYhteispisteet()));
        }
        Integer totalPoints = totalPointsInt != -1 ? totalPointsInt : null;

        // Parse year and term
        examYear = NumberUtils.toInt(tutkintokerta.substring(0, 4));
        switch (tutkintokerta.charAt(4)) {
          case 'K': // Kevät
            examTerm = MatriculationExamTerm.SPRING;
          break;
          case 'S': // Syksy
            examTerm = MatriculationExamTerm.AUTUMN;
          break;
        }
        
        if (examYear < 1900 || examTerm == null) {
          rowMessages.add(String.format("Rivi %d: Tutkintokerran tunnistetta (%s) ei pystytty parsimaan vuodeksi ja vuodenajaksi.", rowNumber, tutkintokerta));
          continue;
        }
        
        // Fetch the MatriculationExam
        exam = matriculationExamDAO.findByYearAndTerm(examYear, examTerm);
        if (exam == null) {
          rowMessages.add(String.format("Rivi %d: Tutkintokerran tunnisteella (%s) ei löytynyt tutkintokertaa Pyramuksesta", rowNumber, tutkintokerta));
          continue;
        }
        
        examEnrollment = matriculationExamEnrollmentDAO.findByExamAndCandidateNumber(exam, candidateNumber);

        if (examEnrollment == null) {
          rowMessages.add(String.format("Rivi %d: Kokelasnumerolla (%d) ei löytynyt ilmoittautumista tutkintokertaan (%s) Pyramuksesta", rowNumber, candidateNumber, tutkintokerta));
          continue;
        }
        
        if (StringUtils.isBlank(matriculationResult.getHetu()) || StringUtils.length(matriculationResult.getHetu()) < 11) {
          rowMessages.add(String.format("Rivi %d: Henkilötunnus puutuu tiedostosta.", rowNumber));
          continue;
        }

        // TODO Regex this
        if (matriculationResult.getHetu().charAt(6) == '-' && matriculationResult.getHetu().charAt(7) == 'U') {
          rowMessages.add(String.format("Rivi %d: Huom. YTL:n väliaikainen henkilötunnus.", rowNumber));
        }
        else if (!StringUtils.equals(examEnrollment.getStudent().getPerson().getSocialSecurityNumber(), matriculationResult.getHetu())) {
          rowMessages.add(String.format("Rivi %d: Henkilötunnus ei vastaa ilmoittautuneen opiskelijan henkilötunnusta eikä se ole YTL:n väliaikainen hetu.", rowNumber));
          continue;
        }

        matriculationGrade = matriculationGradeDAO.findBy(examEnrollment.getStudent().getPerson(), examYear, examTerm, examSubject);
        if (matriculationGrade != null) {
          if (examGrade.equals(matriculationGrade.getGrade())) {
            rowMessages.add(String.format("Rivi %d: Tutkintokerran (%s) kokelaalla (%d) on jo arvosana (%s) Pyramuksessa", rowNumber, matriculationResult.getTutkintokerta(), candidateNumber, matriculationResult.getArvosana()));
          }
          else {
            rowMessages.add(String.format("Rivi %d: Tutkintokerran (%s) kokelaalla (%d) on arvosana (%s) Pyramuksessa, mutta tiedostossa on arvosana (%s)", rowNumber, matriculationResult.getTutkintokerta(), candidateNumber, matriculationGrade.getGrade().name(), matriculationResult.getArvosana()));
          }
          continue;
        }

        rowMessages.add(String.format("Rivi %d: OK Tutkintokerta (%s), kokelasnro (%d), arvosana (%s), yhteispisteet (%s)", rowNumber, matriculationResult.getTutkintokerta(), candidateNumber, matriculationResult.getArvosana(), totalPoints));
        rowsOk++;
        result.put("rowsOk", rowsOk);

        // If save is requested, create the grades - everything should be in order at this point
        
        if (save) {
          matriculationGradeDAO.create(examEnrollment.getStudent().getPerson(), examSubject, examYear, examTerm, examGrade, gradeDate, totalPoints, loggedUser);
        }
      }
      
      result.put("rows", rowMessages);
      requestContext.setResponseContent(result.toString().getBytes("UTF-8"), "application/json;charset=UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
      requestContext.setResponseContent("[\"Tiedoston lukeminen epäonnistui.\"]".getBytes(), "application/json;charset=UTF-8");
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }
  
}
