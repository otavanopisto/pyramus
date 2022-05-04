package fi.otavanopisto.pyramus.ui;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fi.otavanopisto.pyramus.AbstractIntegrationTest;

public class AbstractUITest extends AbstractIntegrationTest {

  protected void setWebDriver(WebDriver webDriver) {
    this.webDriver = webDriver;
  }

  protected WebDriver getWebDriver() {
    return webDriver;
  }

  protected RemoteWebDriver createChromeDriver() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--lang=en_US");
    options.addArguments("--start-maximized");
    options.setAcceptInsecureCerts(true);
    
    if(System.getProperty("it.headless").equals("true") ) {
      options.addArguments("--headless");
      options.addArguments("--disable-gpu");
      ChromeDriver driver = new ChromeDriver(options);
      driver.manage().window().setSize(new Dimension(1920, 1080));
      return driver;
    }else {
      return new ChromeDriver(options);      
    }
  }

  protected WebDriver createFirefoxDriver() {
    FirefoxProfile firefoxProfile = new FirefoxProfile();
    FirefoxOptions firefoxOptions = new FirefoxOptions();
    firefoxOptions.setAcceptInsecureCerts(true);
    firefoxOptions.setProfile(firefoxProfile);
    firefoxProfile.setPreference("intl.accept_languages", "en");
    
    if(System.getProperty("it.headless").equals("true")) {
      firefoxOptions.setHeadless(true);
    }
    
    FirefoxDriver firefoxDriver = new FirefoxDriver(firefoxOptions);
    
    firefoxDriver.manage().window().setSize(new Dimension(1920, 1080));
    
    return firefoxDriver;
  }
  
  protected WebDriver createLocalDriver() {
    switch (getBrowser()) {
      case "chrome":
        return createChromeDriver();
      case "firefox":
        return createFirefoxDriver();
    }
    
    throw new RuntimeException(String.format("Unknown browser %s", getBrowser()));
  }
    
  protected void testTitle(String path, String expected) {
    getWebDriver().get(getAppUrl(true) + path);
    assertEquals(expected, getWebDriver().getTitle());
  }

  protected void testPageElementsByName(String elementName) {
    Boolean elementExists = !getWebDriver().findElements(By.name(elementName)).isEmpty();
    assertEquals(true, elementExists);
  }

  protected void testLogin(String username, String password) throws InterruptedException {
    getWebDriver().get(getAppUrl(true) + "/users/login.page");
    getWebDriver().findElement(By.name("username")).sendKeys(username);
    getWebDriver().findElement(By.name("password")).sendKeys(password);
    waitForElementToBeClickable(By.name("login"));
    getWebDriver().findElement(By.name("login")).click();
    waitForUrlNotMatches(".*/login.*");
    String loggedInAsText = getWebDriver().findElement(By.id("GUI_headerLoggedInAs")).getText();

    assertEquals("Logged in as", loggedInAsText);
  }

  protected void login(String username, String password) {
    getWebDriver().get(getAppUrl(true) + "/users/login.page");
    getWebDriver().findElement(By.name("username")).sendKeys(username);
    getWebDriver().findElement(By.name("password")).sendKeys(password);
    waitForElementToBeClickable(By.name("login"));
    getWebDriver().findElement(By.name("login")).click();
    waitForUrlNotMatches(".*/login.*");
    waitForPresent("#GUI_headerLoggedInAs");
  }

  protected void scrollTo(String selector, int offset) {
    waitForPresent(selector);
    ((JavascriptExecutor) getWebDriver()).executeScript(String.format(""
        + "var elPos = document.querySelectorAll('%s').item(0).getBoundingClientRect().top;"
        + "var offsetPosition = elPos - %d;"
        + "window.scrollTo({ top: offsetPosition});"
        , selector, offset));
  }
  
  protected void scrollToElement(By locator) {
    waitForElementToBePresent(locator);
    WebElement element = getWebDriver().findElement(locator);
    Actions actions = new Actions(getWebDriver());
    actions.moveToElement(element).build();
  }
  
  protected void scrollToElement(String selector) {
    waitForPresent(selector);
    WebElement element = getWebDriver().findElement(By.cssSelector(selector));
    Actions actions = new Actions(getWebDriver());
    actions.moveToElement(element);
  }
  
  protected void waitForPresent(String cssSelector) {
    new WebDriverWait(getWebDriver(), Duration.ofSeconds(30)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));   
  }

  protected void waitForElementToBeClickable(By locator) {
    new WebDriverWait(getWebDriver(), Duration.ofSeconds(60)).until(ExpectedConditions.elementToBeClickable(locator));
  }

  protected void waitForElementToBePresent(By locator) {
    new WebDriverWait(getWebDriver(), Duration.ofSeconds(60)).until(ExpectedConditions.presenceOfElementLocated(locator));
  }
  
  protected void waitForUrlNotMatches(final String regex) {
    WebDriver driver = getWebDriver();
    new WebDriverWait(driver, Duration.ofSeconds(60)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver driver) {
        return !driver.getCurrentUrl().matches(regex);
      }
    });
  }

  protected void waitForUrl(final String url) {
    WebDriver driver = getWebDriver();
    new WebDriverWait(driver, Duration.ofSeconds(60)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver driver) {
        return url.equals(driver.getCurrentUrl());
      }
    });
  }

  protected void waitForUrlMatches(final String regex) {
    WebDriver driver = getWebDriver();
    new WebDriverWait(driver, Duration.ofSeconds(60)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver driver) {
        return driver.getCurrentUrl().matches(regex);
      }
    });
  }

  protected void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
    }
  }
  
  protected void assertStudentDenied(String page) {
    login(STUDENT_USERNAME, STUDENT_PASSWORD);
    getWebDriver().get(getAppUrl(true) + page);
    waitForUrlNotMatches(".*/" + page);
    String cUrl = getWebDriver().getCurrentUrl();
    assertEquals(true, cUrl.contains("accessdenied.page"));
  }
  
  protected void assertStudentAllowed(String page) {
    login(STUDENT_USERNAME, STUDENT_PASSWORD);
    getWebDriver().get(getAppUrl(true) + page);
    String cUrl = getWebDriver().getCurrentUrl();
    assertEquals(true, cUrl.endsWith(page));
  }
  
  /** 
   * @param page String page to goto
   * @param cssSelector String cssSelector for element to wait
   */
  protected void assertStudentAllowed(String page, String cssSelector) {
    login(STUDENT_USERNAME, STUDENT_PASSWORD);
    getWebDriver().get(getAppUrl(true) + page);
    waitForPresent(cssSelector);
    String cUrl = getWebDriver().getCurrentUrl();
    assertEquals(true, cUrl.endsWith(page));
  }

  private WebDriver webDriver;
  protected static final String ADMIN_USERNAME = "devadmin";
  protected static final String ADMIN_PASSWORD = "passi";
  protected static final String STUDENT_USERNAME = "tonyt";
  protected static final String STUDENT_PASSWORD = "passi";
}