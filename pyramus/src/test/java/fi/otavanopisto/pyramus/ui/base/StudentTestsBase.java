package fi.otavanopisto.pyramus.ui.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import fi.otavanopisto.pyramus.SqlAfter;
import fi.otavanopisto.pyramus.SqlBefore;
import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class StudentTestsBase extends AbstractUITest {
  
  @Test
  public void testCreateStudent(){
    login(ADMIN_USERNAME, ADMIN_PASSWORD);
    getWebDriver().get(getAppUrl(true) + "/students/createstudent.page");
    waitForUrlNotMatches(".*/index.*");
    getWebDriver().findElement(By.id("birthday-text")).sendKeys("09/29/1985");
    getWebDriver().findElement(By.name("ssecId")).sendKeys("29091985-1234");
//    TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();
    waitForElementToBeClickable(By.name("studyProgramme"));
    Select studyProgrammeDropdown = new Select(getWebDriver().findElement(By.name("studyProgramme")));
    studyProgrammeDropdown.selectByValue("2");
    getWebDriver().findElement(By.name("firstName")).sendKeys("Test");
    getWebDriver().findElement(By.name("lastName")).sendKeys("Student");
    getWebDriver().findElement(By.name("emailTable.0.email")).sendKeys("teststudent@notmail.test");
//    TODO: Create Student button name is "login"...
    waitForElementToBeClickable(By.name("login"));
    getWebDriver().findElement(By.name("login")).click();
    waitForUrlMatches(".*#at-basic.*");
    assertEquals("Edit Student Test Student", getWebDriver().getTitle());
  }
  
  @Test
  public void testSearchStudent(){
    login(ADMIN_USERNAME, ADMIN_PASSWORD);
    getWebDriver().get(getAppUrl(true) + "/students/searchstudents.page");
    waitForUrlNotMatches(".*/index.*");
    getWebDriver().findElement(By.cssSelector("#searchStudentsSearchContainer .basicSearchQueryField")).sendKeys("Tony");
    getWebDriver().findElement(By.name("query")).click();
//  TODO: No usable name, class or id for this element.
    waitForElementToBePresent(By.cssSelector(".ixTableCell:nth-of-type(2) span"));
    String studentName = getWebDriver().findElement(By.cssSelector(".ixTableCell:nth-of-type(2) span")).getText();
        
    assertEquals("Tester, Tony", studentName);
  }

  @Test
  public void testCourseViewStudent(){
    login(ADMIN_USERNAME, ADMIN_PASSWORD);
    getWebDriver().get(getAppUrl(true) + "/courses/searchcourses.page");
    waitForUrlNotMatches(".*/index.*");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSection input[type~=\"submit\"]")).click();
//  TODO: No usable name, class or id for this element.
    waitForElementToBePresent(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img"));
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img")).click();
    waitForUrlNotMatches(".*/searchcourses.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();
    waitForElementToBePresent(By.cssSelector("div#students"));
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(10) img")).click();
    waitForUrlNotMatches(".*/viewcourse.*");
    Boolean elementExists = !getWebDriver().findElements(By.cssSelector("#studentViewBasicInfoWrapper")).isEmpty();
    assertEquals(true, elementExists);
  }

  @Test
  public void testCourseEditStudentBasicDataSsec(){
    login(ADMIN_USERNAME, ADMIN_PASSWORD);
    getWebDriver().get(getAppUrl(true) + "/courses/searchcourses.page");
    waitForUrlNotMatches(".*/index.*");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSection input[type~=\"submit\"]")).click();
//  TODO: No usable name, class or id for this element.
    waitForElementToBePresent(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img"));
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img")).click();
    waitForUrlNotMatches(".*/searchcourses.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();
    waitForElementToBePresent(By.cssSelector("div#students"));
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(11) img")).click();
    waitForUrlNotMatches(".*/viewcourse.*");
    
    getWebDriver().findElement(By.name("ssecId")).clear();
    getWebDriver().findElement(By.name("ssecId")).sendKeys("010150-1234");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSectionOffTab input")).click();
    waitForElementToBeClickable(By.name("ssecId"));
    String ssec = getWebDriver().findElement(By.name("ssecId")).getAttribute("value");   
    assertEquals("010150-1234", ssec);
  }

  @Test
  public void testCourseEditStudentBasicDataBirthday(){
    login(ADMIN_USERNAME, ADMIN_PASSWORD);
    getWebDriver().get(getAppUrl(true) + "/courses/searchcourses.page");
    waitForUrlNotMatches(".*/index.*");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSection input[type~=\"submit\"]")).click();
//  TODO: No usable name, class or id for this element.
    waitForElementToBePresent(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img"));
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img")).click();
    waitForUrlNotMatches(".*/searchcourses.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();
    waitForElementToBePresent(By.cssSelector("div#students"));
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(11) img")).click();
    waitForUrlNotMatches(".*/viewcourse.*");
    
    getWebDriver().findElement(By.id("birthday-text")).clear();
    getWebDriver().findElement(By.id("birthday-text")).sendKeys("02/08/1980");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSectionOffTab input")).click();
    waitForElementToBeClickable(By.id("birthday-text"));
    String birthday = getWebDriver().findElement(By.id("birthday-text")).getAttribute("value");   
    assertEquals("02/08/1980", birthday);
  }
  
  @Test
  public void testCourseEditStudentBasicDataGender(){
    login(ADMIN_USERNAME, ADMIN_PASSWORD);
    getWebDriver().get(getAppUrl(true) + "/courses/searchcourses.page");
    waitForUrlNotMatches(".*/index.*");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSection input[type~=\"submit\"]")).click();
//  TODO: No usable name, class or id for this element.
    waitForElementToBePresent(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img"));
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img")).click();
    waitForUrlNotMatches(".*/searchcourses.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();
    waitForElementToBePresent(By.cssSelector("div#students"));
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(11) img")).click();
    waitForUrlNotMatches(".*/viewcourse.*");
    
    new Select(getWebDriver().findElement(By.name("gender"))).selectByValue("male");

    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSectionOffTab input")).click();
    waitForElementToBeClickable(By.id("birthday-text"));
    
    assertEquals("Male", new Select(getWebDriver().findElement(By.name("gender"))).getFirstSelectedOption().getText());
  }

  @Test
  public void testCourseEditStudentBasicDataSecureInfo(){
    login(ADMIN_USERNAME, ADMIN_PASSWORD);
    getWebDriver().get(getAppUrl(true) + "/courses/searchcourses.page");
    waitForUrlNotMatches(".*/index.*");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSection input[type~=\"submit\"]")).click();
//  TODO: No usable name, class or id for this element.
    waitForElementToBePresent(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img"));
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img")).click();
    waitForUrlNotMatches(".*/searchcourses.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();
    waitForElementToBePresent(By.cssSelector("div#students"));
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(11) img")).click();
    waitForUrlNotMatches(".*/viewcourse.*");
    
    getWebDriver().findElement(By.name("secureInfo")).click();

    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSectionOffTab input")).click();
    waitForElementToBeClickable(By.id("birthday-text"));
    
    assertEquals(true, getWebDriver().findElement(By.name("secureInfo")).isSelected());
  }
  
  @Test
  public void testCourseEditStudentStudyprogrammeDataFirstName(){
    login(ADMIN_USERNAME, ADMIN_PASSWORD);
    getWebDriver().get(getAppUrl(true) + "/courses/searchcourses.page");
    waitForUrlNotMatches(".*/index.*");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSection input[type~=\"submit\"]")).click();
//  TODO: No usable name, class or id for this element.
    waitForElementToBePresent(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img"));
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img")).click();
    waitForUrlNotMatches(".*/searchcourses.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();
    waitForElementToBePresent(By.cssSelector("div#students"));
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(11) img")).click();
    waitForUrlNotMatches(".*/viewcourse.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();   
//  TODO: No usable name, class or id.
    getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(3) input")).clear();
    getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(3) input")).sendKeys("TestSt");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSectionOffTab input[type~=\"submit\"]")).click();
    waitForElementToBeClickable(By.cssSelector(".activeTab .genericFormSection:nth-of-type(3) input"));
    assertEquals("TestSt", getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(3) input")).getAttribute("value"));
  }
  
  @Test
  public void testCourseEditStudentStudyprogrammeDataLastName(){
    login(ADMIN_USERNAME, ADMIN_PASSWORD);
    getWebDriver().get(getAppUrl(true) + "/courses/searchcourses.page");
    waitForUrlNotMatches(".*/index.*");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSection input[type~=\"submit\"]")).click();
//  TODO: No usable name, class or id for this element.
    waitForElementToBePresent(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img"));
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img")).click();
    waitForUrlNotMatches(".*/searchcourses.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();
    waitForElementToBePresent(By.cssSelector("div#students"));
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(11) img")).click();
    waitForUrlNotMatches(".*/viewcourse.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();   
//  TODO: No usable name, class or id.
    getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(4) input")).clear();
    getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(4) input")).sendKeys("Testlastname");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSectionOffTab input[type~=\"submit\"]")).click();
    waitForElementToBeClickable(By.cssSelector(".activeTab .genericFormSection:nth-of-type(4) input"));
    assertEquals("Testlastname", getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(4) input")).getAttribute("value"));
  }
  
  @Test
  public void testCourseEditStudentStudyprogrammeDataNickname(){
    login(ADMIN_USERNAME, ADMIN_PASSWORD);
    getWebDriver().get(getAppUrl(true) + "/courses/searchcourses.page");
    waitForUrlNotMatches(".*/index.*");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSection input[type~=\"submit\"]")).click();
//  TODO: No usable name, class or id for this element.
    waitForElementToBePresent(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img"));
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img")).click();
    waitForUrlNotMatches(".*/searchcourses.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();
    waitForElementToBePresent(By.cssSelector("div#students"));
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(11) img")).click();
    waitForUrlNotMatches(".*/viewcourse.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();   
//  TODO: No usable name, class or id.
    getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(5) input")).clear();
    getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(5) input")).sendKeys("Testnick");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSectionOffTab input[type~=\"submit\"]")).click();
    waitForElementToBeClickable(By.cssSelector(".activeTab .genericFormSection:nth-of-type(5) input"));
    assertEquals("Testnick", getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(5) input")).getAttribute("value"));
  }
  
  @Test
  public void testCourseEditStudentStudyprogrammeDataTags(){
    login(ADMIN_USERNAME, ADMIN_PASSWORD);
    getWebDriver().get(getAppUrl(true) + "/courses/searchcourses.page");
    waitForUrlNotMatches(".*/index.*");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSection input[type~=\"submit\"]")).click();
//  TODO: No usable name, class or id for this element.
    waitForElementToBePresent(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img"));
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(4) img")).click();
    waitForUrlNotMatches(".*/searchcourses.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();
    waitForElementToBePresent(By.cssSelector("div#students"));
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".ixTableRow:first-child .ixTableCell:nth-of-type(11) img")).click();
    waitForUrlNotMatches(".*/viewcourse.*");
//  TODO: No usable name, class or id for this element.
    getWebDriver().findElement(By.cssSelector(".tabLabel:last-child")).click();   
//  TODO: No usable name, class or id.
    getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(6) input")).clear();
    getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(6) input")).sendKeys("Test");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSectionOffTab input[type~=\"submit\"]")).click();
    waitForElementToBeClickable(By.cssSelector(".activeTab .genericFormSection:nth-of-type(6) input"));
    assertEquals("Test", getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(6) input")).getAttribute("value"));
  }
  
}
