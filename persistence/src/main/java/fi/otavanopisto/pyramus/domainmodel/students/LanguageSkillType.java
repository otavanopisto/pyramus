package fi.otavanopisto.pyramus.domainmodel.students;

public enum LanguageSkillType {
  LISTENING("Kuullun ymmärtäminen"), 
  SPEAKING("Puhuminen"), 
  READING("Luetun ymmärtäminen"), 
  WRITING("Kirjoittaminen");
  
  LanguageSkillType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
  
  public String getValue() {
    return value;
  }
  
  private String value;
}
