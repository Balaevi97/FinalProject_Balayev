package Elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;

public class LogIn {

    public SelenideElement username = $(byId("userName"));
    public SelenideElement password = $(byId("newPass"));
    public SelenideElement submit = $(byId("submitAuth"));
    public SelenideElement OTP = $(byXpath("//div/input[@id='otpCodeInput']"));
    public SelenideElement approve = $(byXpath("//button[text()=' დადასტურება ']"));
    //public SelenideElement easyAuth = $(byXpath("//div[@class='header']//p[text()=' მარტივი ავტორიზაცია ']"));
    public SelenideElement easyAuth = $(byXpath("//p[text()='გთხოვთ დაასრულოთ პროცესი']"));
    public SelenideElement closeEasyAuth = $(byXpath("//div[@class='header']//div [@class='icon close-black grey-010 pointer']"));
    public SelenideElement sumMoneyAmount = $(byXpath("//h3 [@class='white' and text()]"));

}
