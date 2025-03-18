package Steps.FrontSteps;

import Data.Web.AccountsAndCardModel;
import Elements.GetCardDetail;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.ex.ElementIsNotClickableError;
import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;


import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.switchTo;


public class GetCardDetailSteps extends GetCardDetail {

    @Step
    public List<Map.Entry<String, List<AccountsAndCardModel>>> getAllCardsInfo() {
        Map<String, List<AccountsAndCardModel>> groupedCards = new HashMap<>();
        int i = 0;

        while (i < getAllElement.size()) {
            AccountsAndCardModel card = new AccountsAndCardModel();

            if (i < getAllElement.size()) {
                card.setCardName(getAllElement.get(i++).getText());
            }

            if (i < getAllElement.size()) {
                card.setTotalAmount(getAllElement.get(i++).getText());
            }

            String accountNumberText = accountNumber.getText();
            card.setAccountNumber(accountNumberText);

            List<String> amountsByCurrency = new ArrayList<>();
            while (i < getAllElement.size() && getAllElement.get(i).getText().matches(".*[0-9].*")) {
                amountsByCurrency.add(getAllElement.get(i++).getText());
            }
            card.setAmountsByCurrency(amountsByCurrency);

            List<String> cardFunctionalities = new ArrayList<>();
            while (i < getAllElement.size() && !getAllElement.get(i).getText().matches(".*[0-9].*")) {
                cardFunctionalities.add(getAllElement.get(i++).getText());
            }
            card.setCardFunctionality(cardFunctionalities);

            if (!groupedCards.containsKey(accountNumberText)) {
                groupedCards.put(accountNumberText, new ArrayList<>());
            }
            groupedCards.get(accountNumberText).add(card);
        }

        List<Map.Entry<String, List<AccountsAndCardModel>>> result = new ArrayList<>(groupedCards.entrySet());
        result.sort(Comparator.comparing(Map.Entry::getKey));

        return result;
    }

    @Step
    ///  მიმდინარე ბარათის გვერდის წამოღება
    public int getCurrentPage() {
        String text = pages.getText().trim();
        String[] parts = text.split("/");

        return Integer.parseInt(parts[0]);

    }
    @Step
    ///  ბარათების ჯამური გვერდის წამოღება
    public int getTotalPagesCount () {
        String text = pages.getText().trim();
        String[] parts = text.split("/");

        return Integer.parseInt(parts[1]);
    }

    @Step
    /// მომდევნო ბარათზე გადასვლა
    public void next () {
        nextProduct.click();
    }

    public void previous () {
        previousProduct.click();
    }


    /// მეთოდების გამოძახება გვერდების რაოდენობის მიხედვით
    public TreeMap<String, AccountsAndCardModel> collectCardInfo () {
        List<AccountsAndCardModel> allCardsInfo = new ArrayList<>();

        for (int i = 1; i <= getTotalPagesCount(); i++) {
            System.out.println("\n Current Page [" + getCurrentPage() + "]");

            // Get all cards grouped by account number
            List<Map.Entry<String, List<AccountsAndCardModel>>> allCardsGrouped = getAllCardsInfo();

            for (Map.Entry<String, List<AccountsAndCardModel>> entry : allCardsGrouped) {
                String accountNumber = entry.getKey();
                List<AccountsAndCardModel> cards = entry.getValue();

                for (AccountsAndCardModel currency : cards) {
                    if (currency.getCardName() == null || currency.getTotalAmount() == null || currency.getCardName().isEmpty()) {
                        continue;
                    }

                    System.out.println("Currency Details: ");
                    System.out.println("Card Name: " + currency.getCardName());
                    System.out.println("Total Amount: " + currency.getTotalAmount());

                    // Print Account Number directly from the model
                    System.out.println("Account Number: " + currency.getAccountNumber());

                    // Print AmountsByCurrency details
                    for (String amount : currency.getAmountsByCurrency()) {
                        if (amount != null && !amount.isEmpty()) {
                            char currencySymbol = amount.charAt(amount.length() - 1);
                            System.out.println("Amount in " + currencySymbol + ": " + amount);
                        }
                    }

                    // Print Card Functionality details
                    for (String function : currency.getCardFunctionality()) {
                        if (function != null && !function.isEmpty()) {
                            System.out.println("ბარათის ფუნქცია: " + function);
                        }
                    }

                    allCardsInfo.add(currency);
                }
            }

            next();
        }

        TreeMap<String, AccountsAndCardModel> accountMap = new TreeMap<>();

        for (AccountsAndCardModel model : allCardsInfo) {
            accountMap.put(model.getAccountNumber(), model);
        }
        return accountMap;
    }

/// /////////////////////////////////
    /// ბარათის დაბლოკვა

    public Boolean cardBlockButtonAssert () {
        Assert.assertTrue(cardBlock.shouldBe(visible, Duration.ofSeconds(15)).isDisplayed());
        return true;
    }

    public GetCardDetailSteps cardBlockStep () {
        cardBlock.click();
        return this;
    }

    public GetCardDetailSteps closeWindowByButtonStep () {
        closeWindowByButton.click();
        return this;
    }


    public GetCardDetailSteps closeWindowByXStep () {
        closeWindowByX.click();
        return this;
    }

    public GetCardDetailSteps cardBlockApproveStep () {
        cardBlockApprove.click();
        return this;
    }

    public boolean blockedCardAssert () {
        Assert.assertTrue(cardUnblock.shouldBe(visible, Duration.ofSeconds(15)).isDisplayed());
        return true;
    }

    public GetCardDetailSteps cardUnblockStep () {
        cardUnblock.click();
        return this;
    }

    public GetCardDetailSteps cardUnblockApproveStep () {
        cardUnblockApprove.click();
        return this;
    }


    ///  პინის აღდგენა

    public boolean pinResetButtonAssert () {
        Assert.assertTrue(pinReset.isDisplayed());
        return true;
    }

    public GetCardDetailSteps pinResetStep () {
        pinReset.click();
        return this;
    }

    public GetCardDetailSteps closePinResetStep () {
        closePinReset.click();
        return this;
    }

    public GetCardDetailSteps pinResetApproveStep () {
        pinResetApprove.click();
        return this;
    }

    public GetCardDetailSteps setOTP (String otp) {
        OTP.shouldBe(clickable, Duration.ofSeconds(5)).click();
        OTP.setValue(otp);
        return this;
    }

    public GetCardDetailSteps clickApprove () {
        approve.shouldBe(clickable, Duration.ofSeconds(15)).click();
        return this;
    }

    public String getResetPinMessage () {
        return resetPinMessage.shouldBe(visible, Duration.ofSeconds(10)).getText();

    }


    public GetCardDetailSteps operationOnCard (String otp) {
        WebDriverWait wait = new WebDriverWait(Selenide.webdriver().object(), Duration.ofSeconds(10));
    for (int i = getTotalPagesCount(); i >= 1; i--) {
        try {

            try {
                if (pinReset.isDisplayed()) {
                    pinResetStep()
                            .closePinResetStep()
                            .pinResetStep()
                            .closeWindowByXStep()
                            .pinResetStep()
                            .pinResetApproveStep()
                            .setOTP(otp)
                            .clickApprove();
                    System.out.println(getResetPinMessage());
                    Assert.assertEquals(getResetPinMessage(), "ახალი პინ კოდი sms-ით გამოგიგზავნეთ");
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }


            try {
                if (cardBlock.isDisplayed()) {
                    cardBlockStep()
                            .closeWindowByButtonStep()
                            .cardBlockStep()
                            .closeWindowByXStep()
                            .cardBlockStep()
                            .cardBlockApproveStep();
                    Assert.assertTrue(blockedCardAssert());

                    cardUnblockStep()
                            .cardUnblockApproveStep();
                    Assert.assertTrue(cardBlockButtonAssert());
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }


            previous();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        }



        return this;
    }

}




        



