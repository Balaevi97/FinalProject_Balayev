package Steps.APISteps;

import Models.API.GetPersonAccountListResponseModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.*;

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

}