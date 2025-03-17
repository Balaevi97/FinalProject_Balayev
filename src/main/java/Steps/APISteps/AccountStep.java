package Steps.APISteps;

import Data.Web.AccountsAndCardModel;
import Data.API.GetPersonAccountListResponseModel;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static Calls.MyCredoGetAccountList.PostAccountListWithPersonID;
import static Utils.URL.Query;

public class AccountStep {


    public List<Map.Entry<String, List<GetPersonAccountListResponseModel>>> getAccountList() {

        Response response = PostAccountListWithPersonID(Query);
        List<GetPersonAccountListResponseModel> accounts = response.jsonPath()
                .getList("data.accounts", GetPersonAccountListResponseModel.class);


        Map<String, List<GetPersonAccountListResponseModel>> groupedAccounts = new HashMap<>();

        for (GetPersonAccountListResponseModel account : accounts) {
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



    public void compareAccountInfo(TreeMap<String, AccountsAndCardModel> accountAndCardsMapWeb,
                                   List<Map.Entry<String, List<GetPersonAccountListResponseModel>>> accountListAPI) {
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> currencyMap = new HashMap<>();
        currencyMap.put("₾", "GEL");
        currencyMap.put("$", "USD");
        currencyMap.put("€", "EUR");

        for (Map.Entry<String, List<GetPersonAccountListResponseModel>> apiAccount : accountListAPI) {
            String apiAccountNumber = apiAccount.getKey();


            AccountsAndCardModel webAccount = accountAndCardsMapWeb.get(apiAccountNumber);

            if (webAccount != null) {
                List<GetPersonAccountListResponseModel> apiCardDetails = apiAccount.getValue();
                List<String> webCurrencyAmounts = webAccount.getAmountsByCurrency();


                if (apiCardDetails.size() == webCurrencyAmounts.size()) {
                    for (int i = 0; i < apiCardDetails.size(); i++) {
                        GetPersonAccountListResponseModel apiCardDetail = apiCardDetails.get(i);
                        String apiCardCurrency = apiCardDetail.getCurrency();
                        Double apiCardBalance = Double.parseDouble(apiCardDetail.getAvailableBalance());

                        String webCurrencyAmount = webCurrencyAmounts.get(i);

                        String numericPart = webCurrencyAmount.replaceAll("[^0-9.]", "");
                        String currencySymbol = webCurrencyAmount.replaceAll("[0-9. ]", "").trim();
                        String removeComma = currencySymbol.replaceAll(",", "");
                        String mappedCurrency = currencyMap.get(removeComma);

                        if (apiCardCurrency.equals(mappedCurrency)) {
                            double webAmount = Double.parseDouble(numericPart);

                            if (Double.compare(apiCardBalance, webAmount) != 0) {
                                softAssert.fail("Balance mismatch for account: " + apiAccountNumber +
                                        ", currency: " + apiCardCurrency +
                                        ", API balance: " + apiCardBalance +
                                        ", Web balance: " + webAmount);
                            }
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
