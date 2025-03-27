package Steps.FrontSteps;

import Models.ResponseModel.API.PostPersonAccountListResponseModel;
import Models.ResponseModel.API.PostPersonCardListResponseModel;
import Models.ResponseModel.Web.GetAccountsAndCardModel;
import Elements.GetCardDetail;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import org.testng.Assert;


import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static Steps.APISteps.GetAccountList.accountType;
import static com.codeborne.selenide.Condition.*;


public class GetCardDetailSteps extends GetCardDetail {

    public static String accountType1 = "Instant Visa Credit Card Digital";


    @Step
    public GetCardDetailSteps cardUnblockStep () {
        cardUnblock.click();
        return this;
    }

    @Step
    public void cardUnblockApproveStep () {
        cardUnblockApprove.click();
    }

    @Step
    public GetCardDetailSteps pinResetStep () {
        pinReset.click();
        return this;
    }

    @Step
    public GetCardDetailSteps closePinResetStep () {
        closePinReset.click();
        return this;
    }

    @Step
    public GetCardDetailSteps pinResetApproveStep () {
        pinResetApprove.click();
        return this;
    }

    @Step
    public GetCardDetailSteps setOTP (String otp) {
        OTP.shouldBe(clickable, Duration.ofSeconds(5)).click();
        OTP.setValue(otp);
        return this;
    }

    @Step
    public void clickApprove () {
        approve.shouldBe(clickable, Duration.ofSeconds(15)).click();
    }

    @Step
    public String getResetPinMessage () {
        return resetPinMessage.shouldBe(visible, Duration.ofSeconds(10)).getText();
    }

    @Step
    public GetCardDetailSteps cardBlockStep () {
        cardBlock.click();
        return this;
    }

    @Step
    public GetCardDetailSteps closeWindowByButtonStep () {
        closeWindowByButton.click();
        return this;
    }

    @Step
    public GetCardDetailSteps closeWindowByXStep () {
        closeWindowByX.click();
        return this;
    }

    @Step
    public void cardBlockApproveStep () {
        cardBlockApprove.click();
    }

    @Step
    public boolean blockedCardAssert () {
        Assert.assertTrue(cardUnblock.shouldBe(visible, Duration.ofSeconds(15)).isDisplayed());
        return true;
    }

    @Step
    public Boolean cardBlockButtonAssert () {
        Assert.assertTrue(cardBlock.shouldBe(visible, Duration.ofSeconds(15)).isDisplayed());
        return true;
    }

    @Step
    public void previous () {
        previousProduct.click();
    }

    @Step
    public void next () {
        nextProduct.click();
    }

    @Step
    public void cardOperation (String otp) {
        try {
            for (int i = 1; i <= getTotalPagesCount() ; i++) {
                if (pinReset.isDisplayed() || cardUnblock.isDisplayed()) {

                    if (cardUnblock.isDisplayed()) {
                        cardUnblockStep()
                                .cardUnblockApproveStep();
                    } else {
                        pinResetStep()
                                .closePinResetStep()
                                .pinResetStep()
                                .closeWindowByXStep()
                                .pinResetStep()
                                .pinResetApproveStep()
                                .setOTP(otp)
                                .clickApprove();

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
                    }

                    break;
                } else {
                    next();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Step
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

    @Step
    public GetCardDetailSteps deleteRequisiteFile () {
        File Requisites = new File(Configuration.downloadsFolder).getAbsoluteFile();
        File[] invoiceFiles = Requisites.listFiles((dir, name) -> name.startsWith("Requisites"));
        if (checkFileExistence()) {

            assert invoiceFiles != null;
            for (File file : invoiceFiles) {
                boolean deleted = file.delete();
                System.out.println(deleted);
            }
        }
        return this;
    }

    @Step
    public boolean assertDeleteRequisiteFileMethod () {
        Assert.assertFalse(checkFileExistence());
        return false;
    }

    @Step
    public GetCardDetailSteps downloadRequisite () {
        requisite.click();
        return this;
    }

    @Step
    public boolean assertDownloadRequisiteMethod () {
        Assert.assertTrue(checkFileExistence());
        return true;
    }

    @Step
    public GetCardDetailSteps moveToFirstPage () {
        for (int i = getTotalPagesCount(); i >= 1 ; i--) {
            previous();
        }
        return this;
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
    public int getCurrentPage() {
        String text = pages.getText().trim();
        String[] parts = text.split("/");

        return Integer.parseInt(parts[0]);

    }

    @Step
    public int getTotalPagesCount () {
        String text = pages.getText().trim();
        String[] parts = text.split("/");

        return Integer.parseInt(parts[1]);
    }

     @Step
    public TreeMap<String, GetAccountsAndCardModel> collectCardInfo() {
        List<GetAccountsAndCardModel> allCardsInfo = new ArrayList<>();
        String previousAccountNumber = null;

        for (int i = 1; i <= getTotalPagesCount(); i++) {
            getCurrentPage();
            List<Map.Entry<String, List<GetAccountsAndCardModel>>> allCardsGrouped = getAllCardsInfo();

            for (Map.Entry<String, List<GetAccountsAndCardModel>> entry : allCardsGrouped) {
                String currentAccountNumber = entry.getKey();

                if (previousAccountNumber != null) {
                    Assert.assertNotEquals(previousAccountNumber, currentAccountNumber,
                            "Account number did not change after next()");
                }

                List<GetAccountsAndCardModel> cards = entry.getValue();

                for (GetAccountsAndCardModel currency : cards) {
                    if (currency.getCardName() == null || currency.getTotalAmount() == null || currency.getCardName().isEmpty()) {
                        continue;
                    }
                    allCardsInfo.add(currency);
                }

                previousAccountNumber = currentAccountNumber;
            }

            next();
        }

        TreeMap<String, GetAccountsAndCardModel> accountMap = new TreeMap<>();

        for (GetAccountsAndCardModel model : allCardsInfo) {
            accountMap.put(model.getAccountNumber(), model);
        }

        return accountMap;
    }

    @Step
    public GetCardDetailSteps compareAccountInfo(TreeMap<String, GetAccountsAndCardModel> accountAndCardsMapWeb,
                                                 List<Map.Entry<String, List<PostPersonAccountListResponseModel>>> accountListAPI,
                                                 List<PostPersonCardListResponseModel> cardListAPI) {
        Map<String, String> currencyMap = new HashMap<>();
        currencyMap.put("₾", "GEL");
        currencyMap.put("$", "USD");
        currencyMap.put("€", "EUR");

        for (Map.Entry<String, List<PostPersonAccountListResponseModel>> apiAccount : accountListAPI) {
            String apiAccountNumber = apiAccount.getKey();

            GetAccountsAndCardModel webAccount = accountAndCardsMapWeb.get(apiAccountNumber);
            Map<String, GetAccountsAndCardModel> filteredAccounts = accountAndCardsMapWeb.entrySet().stream()
                    .filter(entry -> !accountType.equals(entry.getValue().getCardName()))
                    .filter(entry -> !accountType1.equals(entry.getValue().getCardName()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (webAccount != null) {
                if (!accountType.equals(webAccount.getCardName()) && !accountType1.equals(webAccount.getCardName())) {
                    for (PostPersonCardListResponseModel apiCard : cardListAPI) {
                        String apiCardAccountNumber = apiCard.getAccountNumber();
                        String apiCardNickName = apiCard.getCardNickName();

                        if (apiCardAccountNumber.equals(apiAccountNumber)) {
                            String webCardNickName = webAccount.getCardName();
                            Assert.assertEquals(webCardNickName, apiCardNickName, "Card name does not match for account number: " + apiAccountNumber);
                        }
                    }
                }

                List<PostPersonAccountListResponseModel> apiAccountDetails = apiAccount.getValue();
                List<String> webCurrencyAmounts = webAccount.getAmountsByCurrency();

                if (apiAccountDetails.size() == webCurrencyAmounts.size()) {
                    for (int i = 0; i < apiAccountDetails.size(); i++) {
                        PostPersonAccountListResponseModel apiCardDetail = apiAccountDetails.get(i);
                        String apiCardCurrency = apiCardDetail.getCurrency();
                        BigDecimal apiCardBalance = new BigDecimal(apiCardDetail.getAvailableBalance());

                        String webCurrencyAmount = webCurrencyAmounts.get(i);
                        String numericPart = webCurrencyAmount.replaceAll("[^0-9.]", "");
                        String currencySymbol = webCurrencyAmount.replaceAll("[0-9. ]", "").trim();
                        String removeComma = currencySymbol.replaceAll(",", "");
                        String mappedCurrency = currencyMap.get(removeComma);

                        if (apiCardCurrency.equals(mappedCurrency)) {
                            BigDecimal webAmount = new BigDecimal(numericPart);

                            BigDecimal tolerance = new BigDecimal("0.01");
                            BigDecimal difference = apiCardBalance.subtract(webAmount).abs();

                            Assert.assertTrue(difference.compareTo(tolerance) < 0,
                                    "Mismatch: API Balance " + apiCardBalance + " != Web Balance " + webAmount);
                        }
                    }
                } else {
                    Assert.fail("Mismatched number of items for account: " + apiAccountNumber);
                }

                String webTotalAmount = webAccount.getTotalAmount();
                if (webTotalAmount != null && !webTotalAmount.isEmpty()) {
                    String numericPart = webTotalAmount.replaceAll("[^0-9.]", "");
                    String currencySymbol = webTotalAmount.replaceAll("[0-9. ]", "").trim();
                    String removeComma = currencySymbol.replaceAll(",", "");
                    String webCurrency = currencyMap.get(removeComma);
                    BigDecimal webTotal = new BigDecimal(numericPart);

                    BigDecimal apiTotal = BigDecimal.ZERO;
                    for (PostPersonAccountListResponseModel accountModel : apiAccountDetails) {
                        if (accountModel.getAvailableBalanceEqu() != null) {
                            BigDecimal roundValue = new BigDecimal(accountModel.getAvailableBalanceEqu()).setScale(2, RoundingMode.HALF_UP);
                            apiTotal = apiTotal.add(roundValue);
                        }
                    }
                    Assert.assertEquals(webTotal, apiTotal,
                            "Total amount doesn't match for account " + apiAccountNumber +
                                    " with currency " + webCurrency +
                                    ". Web total: " + webTotal + ", API total: " + apiTotal);
                }
            } else {
                Assert.fail("No matching web account found for API account: " + apiAccountNumber);
            }
        }
        return this;
    }

}