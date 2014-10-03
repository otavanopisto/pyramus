package fi.pyramus.ui.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import fi.pyramus.SqlAfter;
import fi.pyramus.SqlBefore;
import fi.pyramus.ui.AbstractUITest;

public class StudentTestsBase extends AbstractUITest {
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
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
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchStudent(){
    login(ADMIN_USERNAME, ADMIN_PASSWORD);
    getWebDriver().get(getAppUrl(true) + "/students/searchstudents.page");
    waitForUrlNotMatches(".*/index.*");
    getWebDriver().findElement(By.cssSelector("#searchStudentsSearchContainer .basicSearchQueryField")).sendKeys("Tanya");
    getWebDriver().findElement(By.name("query")).click();
//  TODO: No usable name, class or id for this element.
    waitForElementToBePresent(By.cssSelector(".ixTableCell:nth-of-type(2) span"));
    String studentName = getWebDriver().findElement(By.cssSelector(".ixTableCell:nth-of-type(2) span")).getText();
        
    assertEquals("Test #1, Tanya", studentName);
  }

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
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
    Boolean elementExists = getWebDriver().findElements(By.cssSelector("#studentViewBasicInfoWrapper")).size() > 0;
    assertEquals(true, elementExists);
  }

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCourseEditStudentBasicData(){
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
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCourseEditStudentStudyprogrammeData(){
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
    getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(3) input")).sendKeys("Neonaine");
    getWebDriver().findElement(By.cssSelector(".genericFormSubmitSectionOffTab input[type~=\"submit\"]")).click();
    waitForElementToBeClickable(By.cssSelector(".activeTab .genericFormSection:nth-of-type(3) input"));
    assertEquals("Neonaine", getWebDriver().findElement(By.cssSelector(".activeTab .genericFormSection:nth-of-type(3) input")).getAttribute("value"));
  }
  
}
