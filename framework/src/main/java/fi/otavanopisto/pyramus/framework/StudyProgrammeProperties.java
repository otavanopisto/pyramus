package fi.otavanopisto.pyramus.framework;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.VariableType;

public class StudyProgrammeProperties {

  public static final EntityProperty AUTOMATED_SUBJECTCHOICES = new EntityProperty("automatedSubjectChoices", 
      VariableType.BOOLEAN, "studyProgrammeProperties.automatedSubjectChoices");

  public static List<EntityProperty> listProperties() {
    return Arrays.asList(AUTOMATED_SUBJECTCHOICES);
  }
  
  public static boolean isProperty(String key) {
    if (StringUtils.isBlank(key)) {
      return false;
    }
    
    return listProperties().stream()
        .map(property -> property.getKey())
        .anyMatch(key::equals);
  }

}
