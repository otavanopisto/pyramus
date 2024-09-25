package fi.otavanopisto.pyramus.binary.ytl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.ytl.YTLAineKoodi;

public class YTLController {

  private static final Logger logger = Logger.getLogger(YTLController.class.getName());

  public static List<YTLAineKoodi> readMapping() {
    ObjectMapper objectMapper = new ObjectMapper();
    
    try {
      InputStream json = YTLController.class.getClassLoader().getResourceAsStream("fi/otavanopisto/pyramus/ytl/ytl_ainekoodit.json");
      return objectMapper.readValue(json, new TypeReference<List<YTLAineKoodi>>(){});
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Could not read YTL Mapping file", e);
    }
    
    return new ArrayList<>();
  }

  /**
   * Palauttaa Pyramuksen Subject.code -vastineen MatriculationExamSubjectille
   * 
   * @param examSubject
   * @param mapping
   * @return
   */
  public static String examSubjectToSubjectCode(MatriculationExamSubject examSubject, List<YTLAineKoodi> mapping) {
    return mapping.stream()
      .filter(ainekoodi -> (ainekoodi.getMatriculationExamSubject() == examSubject))
      .map(YTLAineKoodi::getAineKoodi)
      .findFirst()
      .orElse(null);
  }
  
  /**
   * Palauttaa YTLAineKoodi -vastineen MatriculationExamSubjectille
   * 
   * @param examSubject
   * @param mapping
   * @return
   */
  public static YTLAineKoodi examSubjectToYTLAineKoodi(MatriculationExamSubject examSubject, List<YTLAineKoodi> mapping) {
    return mapping.stream()
      .filter(ainekoodi -> (ainekoodi.getMatriculationExamSubject() == examSubject))
      .findFirst()
      .orElse(null);
  }
  
  /**
   * Palauttaa YTLAineKoodi -vastineen YTL:n ainekoodille
   * 
   * @param ytlSubject
   * @param mapping
   * @return
   */
  public static List<YTLAineKoodi> ytlSubjectToYTLAineKoodi(String ytlSubject, List<YTLAineKoodi> mapping) {
    return mapping.stream()
      .filter(ainekoodi -> StringUtils.equals(ainekoodi.getYtlAine(), ytlSubject))
      .collect(Collectors.toList());
  }
  
}
