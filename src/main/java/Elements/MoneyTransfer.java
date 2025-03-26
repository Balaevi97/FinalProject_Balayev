package Elements;

import com.codeborne.selenide.*;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class MoneyTransfer {


    public SelenideElement transfer = $(byId("transfer"));
    public SelenideElement ownAccount = $(byXpath("//div/p[@class='paragraph-14' and contains(text(),'საკუთარ')]"));
    public SelenideElement selectedCardNumber = $(byXpath("//div/p[@class='paragraph-12 opa-06 selected-accounts']"));
    public SelenideElement selectedCardSymbol = $(byXpath("//div/p[@class='paragraph-12 balance']"));
    public SelenideElement transferTo = $(byXpath("//div[@class='advanced-accounts-select']"));


    public ElementsCollection card_AccountList = $$(byXpath("//div[@class='account-selection-wrapper']/div//p[@class='paragraph-12' and contains(text(),'GE')]"));
    public ElementsCollection accountAndCardNames = $$(byXpath("//div/parent::div/p[@class='paragraph-12']"));
    public ElementsCollection selectedAccount = $$(byXpath("//div[@class='wrapper']/p[@class='paragraph-12' and contains(text(),'')]"));
    public SelenideElement transferMessage = $(byXpath("//div[@class='content']"));
    public SelenideElement closeMessageWin = $(byXpath("//div[@class='header']//div [@class='icon close-black grey-010 pointer']"));
    public SelenideElement choseCard_Account = $(byXpath("//input[@name='amount']"));
    public SelenideElement payment = $(byXpath("//button[@class='primary next']"));
    public SelenideElement products = $(byXpath("//p [text()='პროდუქტები']"));
    public ElementsCollection loadPage = $$(byXpath("//div[@class='header trigger']/p[contains(@class,'block-header-caps-20')]"));
    public SelenideElement checkProdList = $(byId("checkProdList"));
    public ElementsCollection changedAmount = $$(byXpath("//p[@class='block-header-caps-14 white']"));

}
