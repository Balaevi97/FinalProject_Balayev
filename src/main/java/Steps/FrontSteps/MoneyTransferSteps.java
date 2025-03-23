package Steps.FrontSteps;

import Models.API.GetPersonAccountListResponseModel;
import Models.Web.GetAccountsAndCardModel;
import Elements.MoneyTransfer;
import Steps.APISteps.GetAccountList;
import com.codeborne.selenide.*;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.*;

import static Utils.StringValues.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.refresh;


public class MoneyTransferSteps extends MoneyTransfer {

GetCardDetailSteps getCardDetailSteps = new GetCardDetailSteps();
    public Double accountBalanceAPI;

    GetAccountList accountStep = new GetAccountList();
    @Step
    public MoneyTransferSteps moveToFirstPage () {
        for (int i = getCardDetailSteps.getTotalPagesCount(); i >= 1 ; i--) {
            getCardDetailSteps.previous();
        }
        return this;
    }

    @Step
    public int getMaxAmountPAge() {

        for (int i = 1; i <= getCardDetailSteps.getTotalPagesCount() ; i++) {

            List<Map.Entry<String, List<GetAccountsAndCardModel>>> allCardsGrouped = getCardDetailSteps.getAllCardsInfo();

            for (Map.Entry<String, List<GetAccountsAndCardModel>> entry : allCardsGrouped) {
                List<GetAccountsAndCardModel> cards = entry.getValue();

                for (GetAccountsAndCardModel currency : cards) {
                    if (currency.getCardName() == null || currency.getTotalAmount() == null || currency.getCardName().isEmpty()) {
                        continue;
                    }

                    try {
                        String totalAmount = String.valueOf(Double.parseDouble(currency.getTotalAmount().replaceAll("[^a-zA-Z0-9.]","")));
                        double amountInDouble = Double.parseDouble(totalAmount);
                        if (maxAmountWeb == null || amountInDouble > maxAmountWeb) {
                            maxAmountWeb = amountInDouble;
                            maxAmountPage = i;

                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format for amount: " + currency.getTotalAmount());
                    }
                }
            }
            getCardDetailSteps.next();
        }
        return maxAmountPage;
    }

    @Step
    public MoneyTransferSteps goToMaxAmountPage() {
        while (getCardDetailSteps.getCurrentPage() != maxAmountPage) {
            if (getCardDetailSteps.getCurrentPage() < maxAmountPage) {
                getCardDetailSteps.next();
            } else {
                getCardDetailSteps.previous();
            }
        }
    return this;
    }

    @Step
    public MoneyTransferSteps moveToTransfer () {
        transfer.click();
        return this;
    }
    @Step
    public MoneyTransferSteps transferToOwnAccount () {
        ownAccount.shouldBe(clickable, Duration.ofSeconds(10)).click();
        return this;
    }
    @Step
    public String getSelectedCardNumber () {
        return selectedCardNumber.getText();
    }

    @Step
    public String getTransferCardAmount() {
        return transferCardAmountAmount = selectedCardSymbol.shouldBe(visible, Duration.ofSeconds(10)).getText().replaceAll("[^a-zA-Z0-9.]", "");
    }

    @Step
    public String getTransferCardCurrency() {
        return transferCardAmountSymbol = selectedCardSymbol.getText().replaceAll("[0-9.,\\s]", "");
    }
    @Step
    public MoneyTransferSteps openReceiverAccountList () {
        transferTo.shouldBe(clickable, Duration.ofSeconds(10)).click();
        return this;
    }
    @Step
    public List<String> getCard_AccountList() {
        return card_AccountList.texts();
    }
    @Step
    public MoneyTransferSteps choseAccount() {
        List<String> accountList = getCard_AccountList();
        String selectedCard = getSelectedCardNumber();

        for (String account : accountList) {
            if (!account.equals(selectedCard)) {
                receiverAccountForTransfer = account;
                card_AccountList.get(accountList.indexOf(account)).click();
                break;
            }
        }
return this;
    }
    @Step
    public void chooseCurrency() {

        SelenideElement currencyToSelect = selectedAccount.filter(Condition.text(receiverAccountForTransfer))
                .first()
                .closest("div[contains(@class, 'account-group')]")
                .$(byXpath("./div[@class='accounts-container']//p[contains(text(),'" + transferCardAmountSymbol + "')]"));

        currencyToSelect.click();

    }
    @Step
    public Double getReceiverAccountAmount () {

        SelenideElement currencyToSelect = selectedAccount.filter(Condition.text(receiverAccountForTransfer))
                .first()
                .closest("div")
                .closest("div")
                .$(byXpath("./p[@class='paragraph-18']"));
        String amounts = currencyToSelect.getText().replaceAll("[^a-zA-Z0-9.]","");
        receiverAccountPreviousAmount = Double.valueOf(amounts);

        return receiverAccountPreviousAmount;
    }

    @Step
    public Double calculateTransferAmount () {
        return Math.round(((percentage/totalPercentage)*maxAmountWeb) * totalPercentage) / totalPercentage;
    }


    @Step
    public MoneyTransferSteps setTransferAmount () {
        choseCard_Account.setValue(String.valueOf(calculateTransferAmount ()));

        return this;
    }
    @Step
    public MoneyTransferSteps approvePayment () {
        payment.shouldBe(clickable, Duration.ofSeconds(3)).click();
        return this;
    }
    @Step
    public MoneyTransferSteps closeMessageWindow () {
        if (transferMessage.shouldBe(visible, Duration.ofSeconds(15)).isDisplayed()) {
            transferMessage.click();
            closeMessageWin.click();
        }
        return this;
    }


    @Step
    public Double getAccountBalanceAPI (String accountNumber, String currency) {

        List<Map.Entry<String, List<GetPersonAccountListResponseModel>>> filteredAccountList = accountStep.getAccountList();
        Map<String, String> currencyMap = new HashMap<>();
        currencyMap.put("₾", "GEL");
        currencyMap.put("$", "USD");
        currencyMap.put("€", "EUR");

        for (Map.Entry<String, List<GetPersonAccountListResponseModel>> entry : filteredAccountList) {
            if (entry.getKey().equals(accountNumber)) {
                for (GetPersonAccountListResponseModel account : entry.getValue()) {
                    String convertCurrencySymbol = currencyMap.get(currency);

                    if (account.getCurrency().equals(convertCurrencySymbol)) {
                        this.accountBalanceAPI = Math.round(Double.parseDouble(account.getAvailableBalance()) * 100.0) / 100.0;
                        if (accountBalanceAPI+calculateTransferAmount() != accountBalanceAPI2) {
                            accountBalanceAPI2 =  Math.round(accountBalanceAPI+calculateTransferAmount()*100.0)/100.0;
                        }
                        return accountBalanceAPI;
                    }
                }
            }
        }

        this.accountBalanceAPI = null;
        return null;
    }

    @Step
    public MoneyTransferSteps assertAccountBalanceAPI () {
    SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(accountBalanceAPI + calculateTransferAmount(),
                getAccountBalanceAPI(receiverAccountForTransfer, transferCardAmountSymbol) ,"Amounts does not match. Before transfer amount is " +
                accountBalanceAPI + calculateTransferAmount() + " and after transfer amount is " +
                getAccountBalanceAPI(receiverAccountForTransfer, transferCardAmountSymbol));
        return this;
    }

    @Step
    public MoneyTransferSteps clickProducts () {
        products.click();
        return this;
    }

    @Step
    public MoneyTransferSteps openProdList () {

        for (SelenideElement product : loadPage) {
            if (product.shouldBe(clickable, Duration.ofSeconds(10)).isDisplayed()) {
                checkProdList.click();
                break;
            }
        }

        return this;
    }

    public MoneyTransferSteps getRenewalAccountAmount () {

        try{
            SelenideElement currencyToSelect = selectedAccount.filter(Condition.text(receiverAccountForTransfer))
                    .first();
            if (!currencyToSelect.isDisplayed()) {
                openProdList ();
            } else {
                currencyToSelect.click();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return this;
    }

    public Double getChangedAmount () {
        if (!changedAmount.isDisplayed()) {
            getRenewalAccountAmount ();
        }
        return Double.parseDouble(changedAmount.shouldBe(visible, Duration.ofSeconds(5)).getText().replaceAll("[^a-zA-Z0-9.]",""));
    }


    public boolean assertAmountChangesOnAccount () {
        while (Objects.equals(receiverAccountPreviousAmount, getChangedAmount())){
            refresh();
        }
        return true;
    }

    public void assertAccountBalanceWeb () {
        Assert.assertEquals(receiverAccountPreviousAmount,
                getChangedAmount() - calculateTransferAmount());
    }
}