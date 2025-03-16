package Steps.APISteps;

import Data.Web.AccountsAndCardModel;
import Data.API.GetPersonAccountListResponseModel;
import io.qameta.allure.Step;
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



    @Step
//    public void compareAccountInfo(List<AccountsAndCardModel> accountAndCardsListWeb,
//                                   List<Map.Entry<String, List<GetPersonAccountListResponseModel>>> accountListAPI) {
//        SoftAssert softAssert = new SoftAssert();
//        boolean assertIsTrue = true;
//        Map<String, String> currencyMap = new HashMap<>();
//        currencyMap.put("₾", "GEL");
//        currencyMap.put("$", "USD");
//        currencyMap.put("€", "EUR");
//
//        for (Map.Entry<String, List<GetPersonAccountListResponseModel>> apiAccount : accountListAPI) {
//            String apiAccountNumber = apiAccount.getKey();
//            boolean matchFound = false;
//
//            // Account Cards
//            for (GetPersonAccountListResponseModel apiCardDetail : apiAccount.getValue()) {
//                String apiCardCurrency = apiCardDetail.getCurrency();
//                Double apiCardBalance = Double.parseDouble(apiCardDetail.getAvailableBalance());
//
//                // Loop through accountAndCardsListWeb
//                for (AccountsAndCardModel webAccount : accountAndCardsListWeb) {
//                    AccountsAndCardModel.AccountDetails accountDetails = webAccount.getAccountDetails(); // Updated to use AccountDetails
//                    if (accountDetails == null) continue; // Handle potential null cases
//
//                    String webAccountNumber = accountDetails.getAccountNumber(); // Updated access
//
//                    for (String webCardCurrency : accountDetails.getAmountsByCurrency()) { // Updated access
//                        String cleanedCurrency = webCardCurrency.replaceAll("[^a-zA-Z₾$€]", "");
//                        String mappedCurrency = currencyMap.get(cleanedCurrency);
//
//                        if (apiAccountNumber.equals(webAccountNumber) && apiCardCurrency.equals(mappedCurrency)) {
//                            double webAccountBalance = Double.parseDouble(webAccount.getTotalAmount().replaceAll("[^a-zA-Z0-9.]", ""));
//                            if (Double.compare(apiCardBalance, webAccountBalance) != 0) {
//                                softAssert.fail("Balance mismatch for account: " + apiAccountNumber);
//                            }
//                            matchFound = true;
//                            break;
//                        }
//                    }
//
//                    if (matchFound) break;
//                }
//            }
//
//            if (!matchFound) {
//                softAssert.fail("No matching account found for API Account: " + apiAccountNumber);
//            }
//        }
//        softAssert.assertAll();
//    }

    public void compareAccountInfo(List<AccountsAndCardModel> accountAndCardsListWeb,
                                   List<Map.Entry<String, List<GetPersonAccountListResponseModel>>> accountListAPI) {
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> currencyMap = new HashMap<>();
        currencyMap.put("₾", "GEL");
        currencyMap.put("$", "USD");
        currencyMap.put("€", "EUR");

        for (Map.Entry<String, List<GetPersonAccountListResponseModel>> apiAccount : accountListAPI) {
            String apiAccountNumber = apiAccount.getKey();
            boolean matchFound = false;

            // Loop through API Account Cards for the current account number
            for (GetPersonAccountListResponseModel apiCardDetail : apiAccount.getValue()) {
                String apiCardCurrency = apiCardDetail.getCurrency();
                Double apiCardBalance = Double.parseDouble(apiCardDetail.getAvailableBalance());

                // Loop through accountAndCardsListWeb for the current account number
                for (AccountsAndCardModel webAccount : accountAndCardsListWeb) {
                    AccountsAndCardModel.AccountDetails accountDetails = webAccount.getAccountDetails();
                    if (accountDetails == null) continue; // Handle null cases

                    String webAccountNumber = accountDetails.getAccountNumber();

                    // Check if account numbers match
                    if (apiAccountNumber.equals(webAccountNumber)) {
                        for (String webCardCurrency : accountDetails.getAmountsByCurrency()) {
                            String cleanedCurrency = webCardCurrency.replaceAll("[^a-zA-Z₾$€]", "");
                            String mappedCurrency = currencyMap.get(cleanedCurrency);

                            // Check if currencies match
                            if (apiCardCurrency.equals(mappedCurrency)) {
                                double webAccountBalance = Double.parseDouble(webAccount.getTotalAmount().replaceAll("[^a-zA-Z0-9.]", ""));

                                // Compare balances
                                if (Double.compare(apiCardBalance, webAccountBalance) != 0) {
                                    softAssert.fail("Balance mismatch for account: " + apiAccountNumber +
                                            ", currency: " + apiCardCurrency +
                                            ", API balance: " + apiCardBalance +
                                            ", Web balance: " + webAccountBalance);
                                }
                                matchFound = true;
                                break;
                            }
                        }
                    }

                    if (matchFound) break;
                }
            }

            // If no match found for API Account, report the failure
            if (!matchFound) {
                softAssert.fail("No matching account found for API Account: " + apiAccountNumber);
            }
        }

// Assert all collected failures at the end
        softAssert.assertAll();
    }

}
