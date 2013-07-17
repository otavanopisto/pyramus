package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.AbstractStudent.class, entityType = TranquilModelType.BASE)
public class AbstractStudentBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public java.util.Date getBirthday() {
    return birthday;
  }

  public void setBirthday(java.util.Date birthday) {
    this.birthday = birthday;
  }

  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  public fi.pyramus.domainmodel.students.Sex getSex() {
    return sex;
  }

  public void setSex(fi.pyramus.domainmodel.students.Sex sex) {
    this.sex = sex;
  }

  public String getBasicInfo() {
    return basicInfo;
  }

  public void setBasicInfo(String basicInfo) {
    this.basicInfo = basicInfo;
  }

  public String getLastNameSortable() {
    return lastNameSortable;
  }

  public void setLastNameSortable(String lastNameSortable) {
    this.lastNameSortable = lastNameSortable;
  }

  public String getFirstNameSortable() {
    return firstNameSortable;
  }

  public void setFirstNameSortable(String firstNameSortable) {
    this.firstNameSortable = firstNameSortable;
  }

  public String getInactiveFirstNames() {
    return inactiveFirstNames;
  }

  public void setInactiveFirstNames(String inactiveFirstNames) {
    this.inactiveFirstNames = inactiveFirstNames;
  }

  public String getInactiveLastNames() {
    return inactiveLastNames;
  }

  public void setInactiveLastNames(String inactiveLastNames) {
    this.inactiveLastNames = inactiveLastNames;
  }

  public String getInactiveNicknames() {
    return inactiveNicknames;
  }

  public void setInactiveNicknames(String inactiveNicknames) {
    this.inactiveNicknames = inactiveNicknames;
  }

  public String getInactiveEducations() {
    return inactiveEducations;
  }

  public void setInactiveEducations(String inactiveEducations) {
    this.inactiveEducations = inactiveEducations;
  }

  public String getInactiveEmails() {
    return inactiveEmails;
  }

  public void setInactiveEmails(String inactiveEmails) {
    this.inactiveEmails = inactiveEmails;
  }

  public String getInactiveStreetAddresses() {
    return inactiveStreetAddresses;
  }

  public void setInactiveStreetAddresses(String inactiveStreetAddresses) {
    this.inactiveStreetAddresses = inactiveStreetAddresses;
  }

  public String getInactivePostalCodes() {
    return inactivePostalCodes;
  }

  public void setInactivePostalCodes(String inactivePostalCodes) {
    this.inactivePostalCodes = inactivePostalCodes;
  }

  public String getInactive() {
    return inactive;
  }

  public void setInactive(String inactive) {
    this.inactive = inactive;
  }

  public String getInactiveCities() {
    return inactiveCities;
  }

  public void setInactiveCities(String inactiveCities) {
    this.inactiveCities = inactiveCities;
  }

  public String getInactiveCountries() {
    return inactiveCountries;
  }

  public void setInactiveCountries(String inactiveCountries) {
    this.inactiveCountries = inactiveCountries;
  }

  public String getInactivePhones() {
    return inactivePhones;
  }

  public void setInactivePhones(String inactivePhones) {
    this.inactivePhones = inactivePhones;
  }

  public String getInactiveLodgings() {
    return inactiveLodgings;
  }

  public void setInactiveLodgings(String inactiveLodgings) {
    this.inactiveLodgings = inactiveLodgings;
  }

  public String getInactiveStudyProgrammeIds() {
    return inactiveStudyProgrammeIds;
  }

  public void setInactiveStudyProgrammeIds(String inactiveStudyProgrammeIds) {
    this.inactiveStudyProgrammeIds = inactiveStudyProgrammeIds;
  }

  public String getInactiveLanguageIds() {
    return inactiveLanguageIds;
  }

  public void setInactiveLanguageIds(String inactiveLanguageIds) {
    this.inactiveLanguageIds = inactiveLanguageIds;
  }

  public String getInactiveMunicipalityIds() {
    return inactiveMunicipalityIds;
  }

  public void setInactiveMunicipalityIds(String inactiveMunicipalityIds) {
    this.inactiveMunicipalityIds = inactiveMunicipalityIds;
  }

  public String getInactiveNationalityIds() {
    return inactiveNationalityIds;
  }

  public void setInactiveNationalityIds(String inactiveNationalityIds) {
    this.inactiveNationalityIds = inactiveNationalityIds;
  }

  public String getActiveFirstNames() {
    return activeFirstNames;
  }

  public void setActiveFirstNames(String activeFirstNames) {
    this.activeFirstNames = activeFirstNames;
  }

  public String getActiveLastNames() {
    return activeLastNames;
  }

  public void setActiveLastNames(String activeLastNames) {
    this.activeLastNames = activeLastNames;
  }

  public String getActiveNicknames() {
    return activeNicknames;
  }

  public void setActiveNicknames(String activeNicknames) {
    this.activeNicknames = activeNicknames;
  }

  public String getActiveEducations() {
    return activeEducations;
  }

  public void setActiveEducations(String activeEducations) {
    this.activeEducations = activeEducations;
  }

  public String getActiveEmails() {
    return activeEmails;
  }

  public void setActiveEmails(String activeEmails) {
    this.activeEmails = activeEmails;
  }

  public String getActiveStreetAddresses() {
    return activeStreetAddresses;
  }

  public void setActiveStreetAddresses(String activeStreetAddresses) {
    this.activeStreetAddresses = activeStreetAddresses;
  }

  public String getActivePostalCodes() {
    return activePostalCodes;
  }

  public void setActivePostalCodes(String activePostalCodes) {
    this.activePostalCodes = activePostalCodes;
  }

  public String getActive() {
    return active;
  }

  public void setActive(String active) {
    this.active = active;
  }

  public String getActiveCities() {
    return activeCities;
  }

  public void setActiveCities(String activeCities) {
    this.activeCities = activeCities;
  }

  public String getActiveCountries() {
    return activeCountries;
  }

  public void setActiveCountries(String activeCountries) {
    this.activeCountries = activeCountries;
  }

  public String getActivePhones() {
    return activePhones;
  }

  public void setActivePhones(String activePhones) {
    this.activePhones = activePhones;
  }

  public String getActiveLodgings() {
    return activeLodgings;
  }

  public void setActiveLodgings(String activeLodgings) {
    this.activeLodgings = activeLodgings;
  }

  public String getActiveStudyProgrammeIds() {
    return activeStudyProgrammeIds;
  }

  public void setActiveStudyProgrammeIds(String activeStudyProgrammeIds) {
    this.activeStudyProgrammeIds = activeStudyProgrammeIds;
  }

  public String getActiveLanguageIds() {
    return activeLanguageIds;
  }

  public void setActiveLanguageIds(String activeLanguageIds) {
    this.activeLanguageIds = activeLanguageIds;
  }

  public String getActiveMunicipalityIds() {
    return activeMunicipalityIds;
  }

  public void setActiveMunicipalityIds(String activeMunicipalityIds) {
    this.activeMunicipalityIds = activeMunicipalityIds;
  }

  public String getActiveNationalityIds() {
    return activeNationalityIds;
  }

  public void setActiveNationalityIds(String activeNationalityIds) {
    this.activeNationalityIds = activeNationalityIds;
  }

  public String getActiveTags() {
    return activeTags;
  }

  public void setActiveTags(String activeTags) {
    this.activeTags = activeTags;
  }

  public String getInactiveTags() {
    return inactiveTags;
  }

  public void setInactiveTags(String inactiveTags) {
    this.inactiveTags = inactiveTags;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Boolean getSecureInfo() {
    return secureInfo;
  }

  public void setSecureInfo(Boolean secureInfo) {
    this.secureInfo = secureInfo;
  }

  private Long id;

  private java.util.Date birthday;

  private String socialSecurityNumber;

  private fi.pyramus.domainmodel.students.Sex sex;

  private String basicInfo;

  private String lastNameSortable;

  private String firstNameSortable;

  private String inactiveFirstNames;

  private String inactiveLastNames;

  private String inactiveNicknames;

  private String inactiveEducations;

  private String inactiveEmails;

  private String inactiveStreetAddresses;

  private String inactivePostalCodes;

  private String inactive;

  private String inactiveCities;

  private String inactiveCountries;

  private String inactivePhones;

  private String inactiveLodgings;

  private String inactiveStudyProgrammeIds;

  private String inactiveLanguageIds;

  private String inactiveMunicipalityIds;

  private String inactiveNationalityIds;

  private String activeFirstNames;

  private String activeLastNames;

  private String activeNicknames;

  private String activeEducations;

  private String activeEmails;

  private String activeStreetAddresses;

  private String activePostalCodes;

  private String active;

  private String activeCities;

  private String activeCountries;

  private String activePhones;

  private String activeLodgings;

  private String activeStudyProgrammeIds;

  private String activeLanguageIds;

  private String activeMunicipalityIds;

  private String activeNationalityIds;

  private String activeTags;

  private String inactiveTags;

  private Long version;

  private Boolean secureInfo;

  public final static String[] properties = {"id","birthday","socialSecurityNumber","sex","basicInfo","lastNameSortable","firstNameSortable","inactiveFirstNames","inactiveLastNames","inactiveNicknames","inactiveEducations","inactiveEmails","inactiveStreetAddresses","inactivePostalCodes","inactive","inactiveCities","inactiveCountries","inactivePhones","inactiveLodgings","inactiveStudyProgrammeIds","inactiveLanguageIds","inactiveMunicipalityIds","inactiveNationalityIds","activeFirstNames","activeLastNames","activeNicknames","activeEducations","activeEmails","activeStreetAddresses","activePostalCodes","active","activeCities","activeCountries","activePhones","activeLodgings","activeStudyProgrammeIds","activeLanguageIds","activeMunicipalityIds","activeNationalityIds","activeTags","inactiveTags","version","secureInfo"};
}
