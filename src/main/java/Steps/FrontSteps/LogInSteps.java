package Steps.FrontSteps;

import Elements.LogIn;
import com.codeborne.selenide.Selenide;
import org.testng.Assert;

import java.time.Duration;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;


public class LogInSteps extends LogIn {

    public static String token;


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
        OTP.shouldBe(clickable, Duration.ofSeconds(5)).click();
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
            System.out.println("Extracted Token: " + token);
        } else {
            System.out.println("Token not found or in an unexpected format!");
        }

        return this;
    }



//    public Boolean assertLogin () {
//        Assert.assertEquals(
//                sumMoneyAmount.getText(), "10,905.03 ₾");
//        System.out.println();
//        return true;
//    }


}
