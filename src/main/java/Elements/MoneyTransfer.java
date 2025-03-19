package Elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MoneyTransfer {

    public SelenideElement transfer = $(byId("transfer"));
    public SelenideElement ownAccount = $(byXpath("//div/p[@class='paragraph-14' and contains(text(),'საკუთარ')]"));
    public SelenideElement transferTo = $(byXpath("//div[@class='advanced-accounts-select']"));
    public SelenideElement getSelectedCardNumber = $(byXpath("//div/p[@class='paragraph-12 opa-06 selected-accounts']"));
    public SelenideElement getSelectedCardAmount = $(byXpath("//div/p[@class='paragraph-12 balance']"));

    public SelenideElement choseCard_Account = $(byXpath("//input[@name='amount']"));
    public ElementsCollection card_AccountList = $$(byXpath("//div[@class='account-selection-wrapper']/div"));
    public ElementsCollection currency = $$(byXpath("//div[@class='accounts-container']/div"));

    public SelenideElement payment = $(byXpath("//button[@class='primary next']"));

}
