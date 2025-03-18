package Elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class GetCardDetail {

    public SelenideElement pages = $(byXpath("//div[@class='navigation']//p"));
    public SelenideElement accountNumber = $(byXpath("//div[@class='wrapper']//p[@class='block-header-caps-16' and contains(text(),'GE')]"));
    public ElementsCollection getAllElement = $$(byXpath("//div[@class='brief-info']//p"));
    public SelenideElement nextProduct = $(byId("selectNextProduct"));
    public SelenideElement previousProduct = $(byId("selectPreviousProduct"));


    /// ოპერაციები ბარათზე
    /// ბარათის დაბლოკვა
    public SelenideElement cardBlock = $(byId("cardStatusId"));
    public SelenideElement closeWindowByButton = $(byId("popupCard"));
    public SelenideElement closeWindowByX = $(byXpath("//div[@class='txt']/following-sibling::div[@class='icon close-black grey-010 pointer']")); /// Card/PIN
    public SelenideElement cardBlockApprove = $(byId("lockCard"));
    public SelenideElement cardUnblock = $(byId("showBlockOrUnblockCardPopup"));
    public SelenideElement cardUnblockApprove = $(byId("unblock"));


    ///  პინის ცვლილება
    public SelenideElement pinReset = $(byId("pinReset"));
    public SelenideElement closePinReset = $(byId("closePinReset"));
    public SelenideElement pinResetApprove = $(byId("resetPin"));
    public SelenideElement OTP = $(byXpath("//div/input[@id='otpCodeInput']"));
    public SelenideElement approve = $(byXpath("//button[text()=' დადასტურება ']"));
    public SelenideElement resetPinMessage = $(byId("growlText"));

}
