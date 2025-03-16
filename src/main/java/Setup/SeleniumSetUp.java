package Setup;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.List;

public class SeleniumSetUp {

    public static WebDriver driver;
    public static Actions actions;
    public static JavascriptExecutor js;
    public static WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        initializeDriver();
        com.codeborne.selenide.WebDriverRunner.setWebDriver(driver);
    }

    public static WebDriver getDriver() {
        ChromeOptions options = new ChromeOptions();
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
           // options.addArguments("--incognito");
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }
        return driver;
    }

    public SeleniumSetUp() {
        driver = getDriver();
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // findElement მეთოდი
    public static  WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    public static List<WebElement> findElements(By locator) {
        return  driver.findElements(locator);
    }

    public static Actions getActions() {
        if (actions == null) {
            actions = new Actions(driver);
        }
        return actions;
    }

    public static JavascriptExecutor getJsExecutor() {
        if (js == null) {
            js = (JavascriptExecutor) driver;
        }
        return js;
    }

    public static WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        }
        return wait;
    }
    public static void initializeDriver() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        }
    }

    @AfterClass
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
