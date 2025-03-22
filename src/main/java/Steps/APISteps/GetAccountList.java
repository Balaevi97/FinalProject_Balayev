package Steps.APISteps;

import Models.API.GetPersonCardList;
import Models.Web.GetAccountsAndCardModel;
import Models.API.GetPersonAccountListResponseModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.Collectors;

import static Calls.MyCredoGetAccountList.PostAccountListWithPersonID;
import static Utils.StringValues.*;

public class GetAccountList {

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

        return result;
    }


    @Step
    public void compareAccountInfo (TreeMap<String, GetAccountsAndCardModel> accountAndCardsMapWeb,
                                   List<Map.Entry<String, List<GetPersonAccountListResponseModel>>> accountListAPI,
                                   List<GetPersonCardList> cardListAPI) {
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> currencyMap = new HashMap<>();
        currencyMap.put("₾", "GEL");
        currencyMap.put("$", "USD");
        currencyMap.put("€", "EUR");

        for (Map.Entry<String, List<GetPersonAccountListResponseModel>> apiAccount : accountListAPI) {
            String apiAccountNumber = apiAccount.getKey();

            GetAccountsAndCardModel webAccount = accountAndCardsMapWeb.get(apiAccountNumber);
            Map<String, GetAccountsAndCardModel> filteredAccounts = accountAndCardsMapWeb.entrySet().stream()
                    .filter(entry -> !accountType.equals(entry.getValue().getCardName()))
                    .filter(entry -> !accountType1.equals(entry.getValue().getCardName()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (webAccount != null) {

                if (!accountType.equals(webAccount.getCardName()) && !accountType1.equals(webAccount.getCardName())) {
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
