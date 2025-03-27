package Steps.APISteps;

import Models.ResponseModel.API.PostPersonAccountListResponseModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.*;

import static Calls.MyCredoGetAccountList.PostAccountListWithPersonID;
import static Models.RequestModel.API.PostPersonAccountListRequestModel.accountQuery;

public class GetAccountList {
    public static String accountType = "მიმდინარე";

    @Step
    public List<Map.Entry<String, List<PostPersonAccountListResponseModel>>> getAccountList() {

        Response response = PostAccountListWithPersonID(accountQuery);
        List<PostPersonAccountListResponseModel> accounts = response.jsonPath()
                .getList("data.accounts", PostPersonAccountListResponseModel.class);
        String targetCategory = accountType;
        List<PostPersonAccountListResponseModel> filteredAccounts = accounts.stream()
                .filter(account -> targetCategory.equals(account.getCategory()))
                .toList();

        Map<String, List<PostPersonAccountListResponseModel>> groupedAccounts = new HashMap<>();

        for (PostPersonAccountListResponseModel account : filteredAccounts) {
            String accountNumber = account.getAccountNumber();
            groupedAccounts.computeIfAbsent(accountNumber, k -> new ArrayList<>()).add(account);

        }

        List<Map.Entry<String, List<PostPersonAccountListResponseModel>>> result = new ArrayList<>();
        for (Map.Entry<String, List<PostPersonAccountListResponseModel>> entry : groupedAccounts.entrySet()) {

            result.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
        }
        result.sort(Comparator.comparing(Map.Entry::getKey));

        return result;
    }

}