package Steps.FrontSteps;

import Elements.LogIn;
import com.codeborne.selenide.Selenide;
import org.testng.Assert;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static Steps.APISteps.GetMainPageTotalAmount.getTotalAmount;
import static Utils.StringValues.*;
import static com.codeborne.selenide.Condition.*;



public class LogInSteps extends LogIn {


    public LogInSteps setUsername (String Username) {
        username.setValue(Username);
        return this;
    }

    public LogInSteps setPassword (String Password) {
        password.setValue(Password);
        return this;
    }

    public LogInSteps clickSubmit () {
        submit.click();
        return this;
    }


    public LogInSteps setOTP (String otp) {
        OTP.shouldBe(clickable, Duration.ofSeconds(10)).click();
        OTP.setValue(otp);
        return this;
    }

    public LogInSteps clickApprove () {
        approve.shouldBe(clickable, Duration.ofSeconds(15)).click();
        return this;
    }

    public LogInSteps removeEasyAuth () {
        if (easyAuth.shouldBe(visible, Duration.ofSeconds(10)).isDisplayed()) {
            easyAuth.click();
            closeEasyAuth.click();
        }
        return this;
    }

    public LogInSteps getBearerToken() {
        String fullToken = Selenide.executeJavaScript("return sessionStorage.getItem('token');");

        if (fullToken != null && fullToken.startsWith("Bearer ")) {
            token = fullToken.split(" ")[1];
        } else {
            System.out.println("Token not found or in an unexpected format!");
        }

        return this;
    }

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
        System.out.println(myMoneyCurrency);
         return this;
    }

    public void assertLogin () {
        Assert.assertEquals(myMoney, getTotalAmount());
        System.out.println();
    }


}
