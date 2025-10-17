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
   * Palauttaa YTLAineKoodi -vastineen YTL:n ainekoodille. Jos aineessa on esim useita kielitasoja, palauttaa useamman tietueen.
   * Tälle ei käytännössä pitäisi olla mitään käyttöä, koska ei pitäisi olla tilanteita, joissa pitäisi listata asioita ainekohtaisesti.
   * 
   * @deprecated ytl_ainekoodit.json refaktoroitava niin, että ei ole erikseen ainekoodia+oppimäärää vain vain ytl:n koekoodi
   * @param ytlSubject
   * @param mapping
   * @return
   */
  @Deprecated
  public static List<YTLAineKoodi> ytlSubjectToYTLAineKoodi(String ytlSubject, List<YTLAineKoodi> mapping) {
    return mapping.stream()
      .filter(ainekoodi -> StringUtils.equals(ainekoodi.getYtlAine(), ytlSubject))
      .collect(Collectors.toList());
  }
  
  
  /**
   * Palauttaa YTLAineKoodi-vastineen YTL:n kokeen tunnisteelle. Kokeen tunnisteiden
   * pitäisi olla uniikkeja, mutta jos ei ole, niin palauttaa ensimmäisen. Palauttaa
   * null, jos kokeen koodilla ei löydy YTLAineKoodi-tietuetta.
   * 
   * Lista YTL:n käyttämistä kokeista ja niiden tunnisteista:
   * https://github.com/digabi/ilmoittautuminen/wiki/Ylioppilastutkinnon-kokeet
   * https://github.com/digabi/ilmoittautuminen/blob/master/koekoodit_ja_nimet.csv
   * 
   * @param ytlKoe YTL:n käyttämä kokeen tunniste, esim M, A5, SA, ...
   * @param mapping
   * @return
   */
  public static YTLAineKoodi ytlKoeToYTLAineKoodi(String ytlKoe, List<YTLAineKoodi> mapping) {
    return mapping.stream()
      .filter(ainekoodi -> StringUtils.equals(ainekoodi.getYhdistettyAineKoodi(), ytlKoe))
      .findFirst()
      .orElse(null);
  }
  
}
