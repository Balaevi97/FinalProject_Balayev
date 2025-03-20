package Steps.FrontSteps;

import Data.API.GetPersonAccountListResponseModel;
import Data.Web.GetAccountsAndCardModel;
import Elements.MoneyTransfer;
import Steps.APISteps.AccountStep;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.StringValues.percentage;
import static Utils.StringValues.totalPercentage;
import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;


public class MoneyTransferSteps extends MoneyTransfer {

GetCardDetailSteps getCardDetailSteps = new GetCardDetailSteps();
    Double maxAmountWeb;

    int maxAmountPage ;
    Double accountBalanceAPI;
    private Double accountBalanceAPI2 = 0.0;


    public String transferCardAmountSymbol;
    public String receiverAccountForTransfer;
    public Double receiverAccountPreviousAmount;

    AccountStep accountStep = new AccountStep();

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

        System.out.println("Max Amount Found: " + maxAmountWeb + " on Page: " + maxAmountPage);

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
        return card_AccountList.texts(); // აქ ვაბრუნებთ ანგარიშების სიას
    }

    public void choseAccount() {
        List<String> accountList = getCard_AccountList();
        String selectedCard = getSelectedCardNumber();

        for (String account : accountList) {
            if (!account.equals(selectedCard)) {
                receiverAccountForTransfer = account;
                card_AccountList.get(accountList.indexOf(account)).click();
                break;
            }
        }

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
        if (transferMessage.shouldBe(visible, Duration.ofSeconds(10)).isDisplayed()) {
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
                        if (accountBalanceAPI-calculateTransferAmount () != accountBalanceAPI2) {
                            accountBalanceAPI2 =  Math.round(accountBalanceAPI-calculateTransferAmount ()*100.0)/100.0;
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

        Assert.assertEquals(accountBalanceAPI2 + calculateTransferAmount(),
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
            product.shouldBe(visible, Duration.ofSeconds(10));
            product.shouldBe(clickable, Duration.ofSeconds(10));
        }
        checkProdList.click();
        return this;
    }

    public Double getRenewalAccountAmount () {
        SelenideElement currencyToSelect = selectedAccount.filter(Condition.text(receiverAccountForTransfer))
                .first()
                .ancestor("div[2]")
                .$(byXpath("p[@class='block-header-caps-20']"));

        return Double.parseDouble(currencyToSelect.getText().replaceAll("[^a-zA-Z0-9.]",""));

    }

    public void assertAccountBalanceWeb () {
        Assert.assertEquals(receiverAccountPreviousAmount,
                getRenewalAccountAmount () - calculateTransferAmount() );
    }


}