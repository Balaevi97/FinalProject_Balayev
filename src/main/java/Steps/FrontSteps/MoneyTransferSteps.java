package Steps.FrontSteps;

import Models.API.GetPersonAccountListResponseModel;
import Models.Web.GetAccountsAndCardModel;
import Elements.MoneyTransfer;
import Steps.APISteps.GetAccountList;
import com.codeborne.selenide.*;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.io.File;
import java.time.Duration;
import java.util.*;

import static Utils.StringValues.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;


public class MoneyTransferSteps extends MoneyTransfer {

GetCardDetailSteps getCardDetailSteps = new GetCardDetailSteps();
    public Double accountBalanceAPI;

    GetAccountList accountStep = new GetAccountList();

    public int getMaxAmountPAge() {

        for (int i = getCardDetailSteps.getTotalPagesCount(); i >= 1 ; i--) {
            System.out.println("\n Current Page [" + getCardDetailSteps.getCurrentPage() + "]");

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
            getCardDetailSteps.previous();
        }

        return maxAmountPage;
    }

    public MoneyTransferSteps goToMaxAmountPage() {
        while (getCardDetailSteps.getCurrentPage() != maxAmountPage) {
            if (getCardDetailSteps.getCurrentPage() < maxAmountPage) {
                getCardDetailSteps.next();
            } else {
                getCardDetailSteps.previous();
            }
        }
        System.out.println("Returned to Page: " + getCardDetailSteps.getCurrentPage());
    return this;
    }









    public MoneyTransferSteps moveToTransfer () {
        transfer.click();
        return this;
    }

    public MoneyTransferSteps transferToOwnAccount () {
        ownAccount.shouldBe(clickable, Duration.ofSeconds(10)).click();
        return this;
    }

    public String getSelectedCardNumber () {
        return selectedCardNumber.getText();
    }

    public String getTransferCardCurrency() {
        return transferCardAmountSymbol = selectedCardSymbol.shouldBe(visible, Duration.ofSeconds(10)).getText().replaceAll("[0-9.,\\s]", "");
    }

    public MoneyTransferSteps openReceiverAccountList () {
        transferTo.shouldBe(clickable, Duration.ofSeconds(10)).click();
        return this;
    }

    public List<String> getCard_AccountList() {
        return card_AccountList.texts();
    }

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

    public void chooseCurrency() {

        SelenideElement currencyToSelect = selectedAccount.filter(Condition.text(receiverAccountForTransfer))
                .first()
                .closest("div[contains(@class, 'account-group')]")
                .$(byXpath("./div[@class='accounts-container']//p[contains(text(),'" + transferCardAmountSymbol + "')]"));

        currencyToSelect.click();

    }

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


    public Double calculateTransferAmount () {
        return Math.round(((percentage/totalPercentage)*maxAmountWeb) * totalPercentage) / totalPercentage;
    }



    public MoneyTransferSteps setTransferAmount () {
        choseCard_Account.setValue(String.valueOf(calculateTransferAmount ()));

        return this;
    }

    public MoneyTransferSteps approvePayment () {
        payment.shouldBe(clickable, Duration.ofSeconds(3)).click();
        return this;
    }

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


    public MoneyTransferSteps assertAccountBalanceAPI () {

        Assert.assertEquals(accountBalanceAPI + calculateTransferAmount(),
                getAccountBalanceAPI(receiverAccountForTransfer, transferCardAmountSymbol), "Amounts does not match");
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
            product.shouldBe(visible, Duration.ofSeconds(10));
            product.shouldBe(clickable, Duration.ofSeconds(10));
        }
        checkProdList.click();
        return this;
    }

    public Double getRenewalAccountAmount () {
        double amount = 0.0;
        try{
            SelenideElement currencyToSelect = selectedAccount.filter(Condition.text(receiverAccountForTransfer))
                    .first()
                    .ancestor("div[2]")
                    .$(byXpath("p[@class='block-header-caps-20']"));

            if (!currencyToSelect.isDisplayed()) {
                openProdList ();
            } else {
                return amount = Double.parseDouble(currencyToSelect.getText().replaceAll("[^a-zA-Z0-9.]",""));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return amount;
    }

    public void assertAccountBalanceWeb () {
        Assert.assertEquals(receiverAccountPreviousAmount,
                getRenewalAccountAmount () - calculateTransferAmount() );
    }


}