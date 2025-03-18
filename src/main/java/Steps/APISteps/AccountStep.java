package Steps.APISteps;

import Data.API.GetPersonCardList;
import Data.Web.AccountsAndCardModel;
import Data.API.GetPersonAccountListResponseModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.Collectors;

import static Calls.MyCredoGetAccountList.PostAccountListWithPersonID;
import static Utils.URL.*;

public class AccountStep {

    @Step
    public List<Map.Entry<String, List<GetPersonAccountListResponseModel>>> getAccountList() {

        Response response = PostAccountListWithPersonID(accountQuery);
        List<GetPersonAccountListResponseModel> accounts = response.jsonPath()
                .getList("data.accounts", GetPersonAccountListResponseModel.class);
        String targetCategory = accountType;
        List<GetPersonAccountListResponseModel> filteredAccounts = accounts.stream()
                .filter(account -> targetCategory.equals(account.getCategory()))
                .toList();

        Map<String, List<GetPersonAccountListResponseModel>> groupedAccounts = new HashMap<>();

        for (GetPersonAccountListResponseModel account : filteredAccounts) {
            String accountNumber = account.getAccountNumber();
            groupedAccounts.computeIfAbsent(accountNumber, k -> new ArrayList<>()).add(account);

        }

        List<Map.Entry<String, List<GetPersonAccountListResponseModel>>> result = new ArrayList<>();
        for (Map.Entry<String, List<GetPersonAccountListResponseModel>> entry : groupedAccounts.entrySet()) {

            result.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
        }

        result.sort(Comparator.comparing(Map.Entry::getKey));

        System.out.println(result);

        return result;
    }

    public List<GetPersonCardList> getcardList() {

        Response response = PostAccountListWithPersonID(cardQuery);
        List<GetPersonCardList> cards = response.jsonPath()
                .getList("data.cards", GetPersonCardList.class);

        List<GetPersonCardList> filteredCards = cards.stream()
                .collect(Collectors.toMap(
                        GetPersonCardList::getAccountNumber, card -> card,
                        (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();

        return filteredCards;
    }


    @Step
    public void compareAccountInfo(TreeMap<String, AccountsAndCardModel> accountAndCardsMapWeb,
                                   List<Map.Entry<String, List<GetPersonAccountListResponseModel>>> accountListAPI,
                                   List<GetPersonCardList> cardListAPI) {
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> currencyMap = new HashMap<>();
        currencyMap.put("₾", "GEL");
        currencyMap.put("$", "USD");
        currencyMap.put("€", "EUR");

        for (Map.Entry<String, List<GetPersonAccountListResponseModel>> apiAccount : accountListAPI) {
            String apiAccountNumber = apiAccount.getKey();

            AccountsAndCardModel webAccount = accountAndCardsMapWeb.get(apiAccountNumber);
            Map<String, AccountsAndCardModel> filteredAccounts = accountAndCardsMapWeb.entrySet().stream()
                    .filter(entry -> !"მიმდინარე".equals(entry.getValue().getCardName()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (webAccount != null) {

                if (!"მიმდინარე".equals(webAccount.getCardName())) {
                    for (GetPersonCardList apiCard : cardListAPI) {
                        String apiCardAccountNumber = apiCard.getAccountNumber();
                        String apiCardNickName = apiCard.getCardNickName();

                        if (apiCardAccountNumber.equals(apiAccountNumber)) {
                            String webCardNickName = webAccount.getCardName();
                            Assert.assertEquals(webCardNickName, apiCardNickName, "Card Name Does Not Match for Account Number: " + apiAccountNumber);
                        }
                    }
                }

                List<GetPersonAccountListResponseModel> apiAccountDetails = apiAccount.getValue();
                List<String> webCurrencyAmounts = webAccount.getAmountsByCurrency();

                if (apiAccountDetails.size() == webCurrencyAmounts.size()) {
                    for (int i = 0; i < apiAccountDetails.size(); i++) {
                        GetPersonAccountListResponseModel apiCardDetail = apiAccountDetails.get(i);
                        String apiCardCurrency = apiCardDetail.getCurrency();
                        Double apiCardBalance = Double.parseDouble(apiCardDetail.getAvailableBalance());

                        String webCurrencyAmount = webCurrencyAmounts.get(i);

                        String numericPart = webCurrencyAmount.replaceAll("[^0-9.]", "");
                        String currencySymbol = webCurrencyAmount.replaceAll("[0-9. ]", "").trim();
                        String removeComma = currencySymbol.replaceAll(",", "");
                        String mappedCurrency = currencyMap.get(removeComma);

                        if (apiCardCurrency.equals(mappedCurrency)) {
                            double webAmount = Double.parseDouble(numericPart);
                            Assert.assertEquals(apiCardBalance, webAmount);
                        } else {
                            softAssert.fail("Currency mismatch for account: " + apiAccountNumber +
                                    ", expected currency: " + apiCardCurrency +
                                    ", found currency: " + mappedCurrency);
                        }
                    }
                } else {
                    softAssert.fail("Mismatched number of items for account: " + apiAccountNumber);
                }
            } else {
                softAssert.fail("No matching web account found for API account: " + apiAccountNumber);
            }
        }

        softAssert.assertAll();
    }


}
