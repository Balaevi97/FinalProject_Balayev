package Steps.FrontSteps;

import Elements.LogIn;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static Steps.APISteps.GetMainPageTotalAmount.getTotalAmount;
import static Utils.StringValues.*;
import static com.codeborne.selenide.Condition.*;



public class LogInSteps extends LogIn {

    @Step
    public LogInSteps setUsername (String Username) {
        username.setValue(Username);
        return this;
    }
    @Step
    public LogInSteps setPassword (String Password) {
        password.setValue(Password);
        return this;
    }
    @Step
    public LogInSteps clickSubmit () {
        submit.click();
        return this;
    }

    @Step
    public LogInSteps setOTP (String otp) {
        OTP.shouldBe(clickable, Duration.ofSeconds(10)).click();
        OTP.setValue(otp);
        return this;
    }
    @Step
    public LogInSteps clickApprove () {
        approve.shouldBe(clickable, Duration.ofSeconds(15)).click();
        return this;
    }
    @Step
    public LogInSteps removeEasyAuth () {

        if (easyAuth.is(exist, Duration.ofSeconds(5))) {
            easyAuth.click();
            closeEasyAuth.click();
        }
        return this;
    }
    @Step
    public LogInSteps getBearerToken() {
        String fullToken = Selenide.executeJavaScript("return sessionStorage.getItem('token');");

        if (fullToken != null && fullToken.startsWith("Bearer ")) {
            token = fullToken.split(" ")[1];
        } else {
            System.out.println("Token not found or in an unexpected format!");
        }

        return this;
    }
    @Step
    public LogInSteps myMoney () {
        Map<Character, String> currencyMap = new HashMap<>();
        currencyMap.put('₾', "GEL");
        currencyMap.put('$', "USD");
        currencyMap.put('€', "EUR");

        String money = sumMoneyAmount.shouldBe(visible, Duration.ofSeconds(10)).getText().trim();
        myMoney = money.replaceAll("[^0-9.]", "");
        String currencyOnly = money.replaceAll("[0-9.]", "").trim();
        char currencySymbol = currencyOnly.replaceAll("[0-9.]", "").trim().charAt(currencyOnly.length()-1);

        myMoneyCurrency = currencyMap.get(currencySymbol);
         return this;
    }
    @Step
    public void assertLogin () {
        Assert.assertEquals(myMoney, getTotalAmount());
        System.out.println();
    }


}
