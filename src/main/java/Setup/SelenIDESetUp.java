package Setup;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;
import io.restassured.RestAssured;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;

import static com.codeborne.selenide.Selenide.open;

public class SelenIDESetUp {

    @BeforeClass
    public static void setDriver() {
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", System.getProperty("user.home"));
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("download.directory_upgrade", true);
        chromePrefs.put("safebrowsing.enabled", true);
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);

        System.setProperty("webdriver.http.factory", "jdk-http-client");
        Configuration.browserSize = null;
        Configuration.browserCapabilities = options;
        Configuration.browser = "chrome";
        Configuration.downloadsFolder = System.getProperty("user.home") ;
        Configuration.headless = false;
        Configuration.timeout = 10000;
        Configuration.proxyEnabled = false;
        Configuration.fastSetValue = true;
        Configuration.fileDownload = FileDownloadMode.FOLDER;
        Configuration.reportsFolder = "test-result/reports";
        Configuration.pageLoadStrategy = "eager";
        Configuration.reopenBrowserOnFail = true;


        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--incognito");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--accept-insecure-certs");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-web-security");
        options.addArguments("--test-type");
        options.addArguments("--disable-gpu");

    }
    public static void openPage (String url) {
        setDriver();
        RestAssured.useRelaxedHTTPSValidation();
        open(url);
    }


    @AfterClass
    public void tearDown() {
        Selenide.closeWebDriver();
    }


}
