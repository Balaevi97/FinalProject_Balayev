package Steps.FrontSteps;

import Models.ResponseModel.API.PostPersonAccountListResponseModel;
import Models.ResponseModel.Web.GetAccountsAndCardModel;
import Elements.MoneyTransfer;
import Steps.APISteps.GetAccountList;
import com.codeborne.selenide.*;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;
import static Models.RequestModel.Web.MoneyTransferRequestModel.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;


public class MoneyTransferSteps extends MoneyTransfer {

    public BigDecimal accountBalancePreviousAPI = BigDecimal.ZERO;
    public BigDecimal accountBalanceAfterAPI = BigDecimal.ZERO;



    GetCardDetailSteps getCardDetailSteps = new GetCardDetailSteps();

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
    if (getCardDetailSteps.getCurrentPage() != 1) {
        for (int i = getCardDetailSteps.getTotalPagesCount(); i >= 1; i--) {
            getCardDetailSteps.previous();
        }
    }
        for (int i = 1; i <= getCardDetailSteps.getTotalPagesCount() ; i++) {

            List<Map.Entry<String, List<GetAccountsAndCardModel>>> allCardsGrouped = getCardDetailSteps.getAllCardsInfo();

            for (Map.Entry<String, List<GetAccountsAndCardModel>> entry : allCardsGrouped) {
                List<GetAccountsAndCardModel> cards = entry.getValue();

                for (GetAccountsAndCardModel currency : cards) {
                    if (currency.getCardName() == null || currency.getTotalAmount() == null || currency.getCardName().isEmpty()) {
                        continue;
                    }

                    try {

                        double totalAmount = currency.getAmountsByCurrency().stream()
                                .findFirst().map(amount -> {
                                    String cleanedAmount = amount.replaceAll("[^0-9,.]", "");
                                    return Double.parseDouble(cleanedAmount.replace(",", ""));
                                }).orElse(0.0);

                        if (transferCardAmountAmount == null || totalAmount > transferCardAmountAmount) {
                            transferCardAmountAmount = totalAmount;
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
        Selenide.executeJavaScript("document.documentElement.scrollTop = 0;");
        ownAccount.shouldBe(clickable, Duration.ofSeconds(10)).click();
        return this;
    }

    @Step
    public Double getTransferCardAmount() {
        return transferCardAmountAmount = Double.valueOf(selectedCardSymbol.shouldBe(visible, Duration.ofSeconds(10)).getText().replaceAll("[^a-zA-Z0-9.]", ""));
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
    public String getSelectedCardNumber () {
        return selectedCardNumber.getText();
    }

    public List<String> getCard_AccountListName() {
        return accountAndCardNames.texts();
    }

    @Step
    public MoneyTransferSteps choseAccount() {
        List<String> accountList = getCard_AccountList();
        String selectedCard = getSelectedCardNumber();
        List<String> cardNames = getCard_AccountListName();

        for (int i = 0; i < accountList.size(); i++) {
            String account = accountList.get(i);
            String cardName = cardNames.get(i);

            if (!account.equals(selectedCard) && !cardName.trim().equals(accountType)) {
                receiverAccountForTransfer = account;
                card_AccountList.get(i).click();
                break;
            }
        }
        return this;
    }

    @Step
    public BigDecimal getAccountBalanceAPI(String accountNumber, String currency, boolean isFirstCapture) {

        while (true) {
            List<Map.Entry<String, List<PostPersonAccountListResponseModel>>> filteredAccountList = accountStep.getAccountList();

            if (filteredAccountList == null || filteredAccountList.isEmpty()) {
                System.out.println("Error: Account list is empty or null");
                return null;
            }

            Map<String, String> currencyMap = new HashMap<>();
            currencyMap.put("₾", "GEL");
            currencyMap.put("$", "USD");
            currencyMap.put("€", "EUR");

            String convertCurrencySymbol = currencyMap.get(currency);

            for (Map.Entry<String, List<PostPersonAccountListResponseModel>> entry : filteredAccountList) {
                if (entry.getKey().equals(accountNumber)) {
                    for (PostPersonAccountListResponseModel account : entry.getValue()) {
                        if (account.getCurrency().equals(convertCurrencySymbol)) {
                            BigDecimal currentBalance = new BigDecimal(account.getAvailableBalance());
                            if (this.accountBalancePreviousAPI != null &&
                                    currentBalance.compareTo(this.accountBalancePreviousAPI) == 0) {
                                continue;
                            }

                            if (isFirstCapture) {
                                this.accountBalancePreviousAPI = currentBalance;
                            } else {
                                this.accountBalanceAfterAPI = currentBalance;
                            }
                            return currentBalance;
                        }
                    }
                }
            }
        }
    }


    @Step
    public BigDecimal getReceiverAccountAmount () {

        SelenideElement previousAmount = selectedAccount.filter(Condition.text(receiverAccountForTransfer))
                .first()
                .closest("div[contains(@class, 'account-group')]")
                .$(byXpath("./div[@class='accounts-container']//p[contains(text(),'" + transferCardAmountSymbol + "')]"));
    String amount = previousAmount.getText().replaceAll("[^0-9.]", "");
        receiverAccountPreviousAmountWeb = new BigDecimal(amount);

        return receiverAccountPreviousAmountWeb;
    }

    @Step
    public BigDecimal calculateTransferAmount() {
        double rawAmount = (percentage / totalPercentage) * transferCardAmountAmount;
        amountForPayment = new BigDecimal(rawAmount).setScale(2, RoundingMode.HALF_UP);
        return amountForPayment;

    }

    @Step
    public void chooseCurrency() {

        SelenideElement currencyToSelect = selectedAccount.filter(Condition.text(receiverAccountForTransfer))
                .first()
                .closest("div[contains(@class, 'account-group')]")
                .$(byXpath("./div[@class='accounts-container']//p[contains(text(),'" + transferCardAmountSymbol + "')]"));

        currencyToSelect.click();

    }

    public BigDecimal getCalculatedAmountForAssertWeb () {
        return receiverAccountAfterAmountWeb = receiverAccountPreviousAmountWeb.add(amountForPayment);
    }

    @Step
    public MoneyTransferSteps setTransferAmount () {
        choseCard_Account.setValue(String.valueOf(amountForPayment));
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


    public BigDecimal getCalculatedAmountForAssertAPI () {
        return calculatedAmountForAssertAPI = accountBalanceAfterAPI.subtract(amountForPayment);
    }


    @Step
    public MoneyTransferSteps assertAccountBalanceAPI () {
        Assert.assertEquals(accountBalancePreviousAPI , calculatedAmountForAssertAPI ,"Amounts does not match");
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
            if (product.shouldBe(clickable, Duration.ofSeconds(5)).isDisplayed()) {
                checkProdList.click();
                break;
            }
        }

        return this;
    }

    public MoneyTransferSteps getRenewalAccountAmount() {
        try {
            if (selectedAccount.filter(Condition.text(receiverAccountForTransfer)).first().is(exist, Duration.ofSeconds(5))) {
                selectedAccount.filter(Condition.text(receiverAccountForTransfer)).first().click();
            } else {
                openProdList();
                selectedAccount.filter(Condition.text(receiverAccountForTransfer)).first().click();
            }

        } catch (Exception e) {
           System.out.println("Error in getRenewalAccountAmount(): " + e.getMessage());
        }

        return this;
    }

    public BigDecimal getChangedAmount() {

        try {
            if (loadPage.first().is(exist, Duration.ofSeconds(3))) {
                openProdList();
                selectedAccount.filter(Condition.text(receiverAccountForTransfer)).first().click();
            }
            return new BigDecimal(changedAmount.first().shouldBe(visible, Duration.ofSeconds(5))
                    .shouldHave(Condition.partialText("₾"))
                    .getText()
                    .replaceAll("[^0-9.]", ""));

        } catch (Exception e) {
            System.out.println("Error in getChangedAmount(): " + e.getMessage());
            return BigDecimal.ZERO;
        }

    }

    public BigDecimal checkIfAmountChanged () {

        while (true) {

            if (Objects.equals(receiverAccountPreviousAmountWeb, getChangedAmount())) {
                products.click();

                openProdList()
                        .getRenewalAccountAmount()
                        .getChangedAmount();
            } else {
                break;
            }
        }

        return BigDecimal.ZERO;
    }

    public MoneyTransferSteps assertAccountBalanceWeb () {
        Assert.assertEquals(receiverAccountAfterAmountWeb, getChangedAmount());
        return this;
    }

    public void  anyQuestions () {
        String[] anyQuestions = {

                "     █████╗ ███╗   ██╗██╗   ██╗     ██████╗ ██╗   ██╗███████╗███████╗████████╗██╗ ██████╗ ███╗   ██╗███████╗   ███████╗  ",
                "    ██╔══██╗████╗  ██║██║   ██║    ██╔═══██╗██║   ██║██╔════╝██╔════╝╚══██╔══╝██║██╔═══██╗████╗  ██║██╔════╝        ██╔  ",
                "    ███████║██╔██╗ ██║██║   ██║    ██║   ██║██║   ██║█████╗  ███████╗   ██║   ██║██║   ██║██╔██╗ ██║███████╗   ████ ██╔  ",
                "    ██╔══██║██║╚██╗██║╚██████╔╝    ██║▄▄ ██║██║   ██║██╔══╝  ╚════██║   ██║   ██║██║   ██║██║╚██╗██║╚════██║   ██╔       ",
                "    ██║  ██║██║ ╚████║ ╚═██══╝     ╚██████╔╝╚██████╔╝███████╗███████║   ██║   ██║╚██████╔╝██║ ╚████║███████║             ",
                "    ╚═╝  ╚═╝╚═╝  ╚═══╝   ██║        ╚══▀▀═╝  ╚═════╝ ╚══════╝╚══════╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝   ██╔       ",

        };

        for (String line : anyQuestions) {
            System.out.println(line);
        }
    }
}