package Steps.FrontSteps;

import Models.Web.GetAccountsAndCardModel;
import Elements.GetCardDetail;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import org.testng.Assert;


import java.io.File;
import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;


public class GetCardDetailSteps extends GetCardDetail {

    public boolean checkFileExistence() {
        int timeout = 10000;
        long startTime = System.currentTimeMillis();
        double endTime = startTime + timeout;
        File downloadsFolder = new File(Configuration.downloadsFolder).getAbsoluteFile();

        while (System.currentTimeMillis() < endTime) {
            File[] matchingFiles = downloadsFolder.listFiles((dir, name) -> name.startsWith("Requisites"));

            if (matchingFiles != null && matchingFiles.length > 0) {
                return true;
            }
        }
        return false;
    }


    public GetCardDetailSteps deleteRequisiteFile () {
        File Requisites = new File(Configuration.downloadsFolder);
        File[] invoiceFiles = Requisites.listFiles((dir, name) -> name.startsWith("Requisites"));
        if (checkFileExistence()) {

            assert invoiceFiles != null;
            for (File file : invoiceFiles) {
                boolean deleted = file.delete();
            }
        }
        return this;
    }

    public boolean assertDeleteRequisiteFileMethod () {
        Assert.assertFalse(checkFileExistence());
        return false;
    }

    public GetCardDetailSteps downloadRequisite () {
        requisite.click();
        return this;
    }

    public boolean assertDownloadRequisiteMethod () {
        Assert.assertTrue(checkFileExistence());
        return true;
    }

    @Step
    public List<Map.Entry<String, List<GetAccountsAndCardModel>>> getAllCardsInfo() {
        Map<String, List<GetAccountsAndCardModel>> groupedCards = new HashMap<>();
        int i = 0;

        while (i < getAllElement.size()) {
            GetAccountsAndCardModel card = new GetAccountsAndCardModel();

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

            if (!groupedCards.containsKey(accountNumberText)) {
                groupedCards.put(accountNumberText, new ArrayList<>());
            }
            groupedCards.get(accountNumberText).add(card);
        }

        List<Map.Entry<String, List<GetAccountsAndCardModel>>> result = new ArrayList<>(groupedCards.entrySet());
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
    public TreeMap<String, GetAccountsAndCardModel> collectCardInfo () {
        List<GetAccountsAndCardModel> allCardsInfo = new ArrayList<>();

        for (int i = 1; i <= getTotalPagesCount(); i++) {
            getCurrentPage();
            List<Map.Entry<String, List<GetAccountsAndCardModel>>> allCardsGrouped = getAllCardsInfo();

            for (Map.Entry<String, List<GetAccountsAndCardModel>> entry : allCardsGrouped) {
                List<GetAccountsAndCardModel> cards = entry.getValue();

                for (GetAccountsAndCardModel currency : cards) {
                    if (currency.getCardName() == null || currency.getTotalAmount() == null || currency.getCardName().isEmpty()) {
                        continue;
                    }
                     currency.getCardName();
                    currency.getTotalAmount();
                    currency.getAccountNumber();

                    for (String amount : currency.getAmountsByCurrency()) {
                        if (amount != null && !amount.isEmpty()) {
                            char currencySymbol = amount.charAt(amount.length() - 1);
                        }
                    }

                    allCardsInfo.add(currency);
                }
            }

            next();
        }

        TreeMap<String, GetAccountsAndCardModel> accountMap = new TreeMap<>();

        for (GetAccountsAndCardModel model : allCardsInfo) {
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

    public void cardBlockApproveStep () {
        cardBlockApprove.click();
    }

    public boolean blockedCardAssert () {
        Assert.assertTrue(cardUnblock.shouldBe(visible, Duration.ofSeconds(15)).isDisplayed());
        return true;
    }

    public GetCardDetailSteps cardUnblockStep () {
        cardUnblock.click();
        return this;
    }

    public void cardUnblockApproveStep () {
        cardUnblockApprove.click();
    }


    ///  პინის აღდგენა

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

    public void clickApprove () {
        approve.shouldBe(clickable, Duration.ofSeconds(15)).click();
    }

    public String getResetPinMessage () {
        return resetPinMessage.shouldBe(visible, Duration.ofSeconds(10)).getText();

    }


    public void operationOnCard (String otp) {
        try {
            for (int i = getTotalPagesCount(); i >=1 ; i--) {
                if (pinReset.isDisplayed() && cardBlock.isDisplayed()) {
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

                    break;
                } else {
                    previous();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }




}




        



