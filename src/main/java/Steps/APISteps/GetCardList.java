package Steps.APISteps;

import Models.ResponseModel.API.GetPersonCardListResponseModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.stream.Collectors;

import static Calls.MyCredoGetAccountList.PostAccountListWithPersonID;
import static Utils.StringValues.cardQuery;

public class GetCardList {

    @Step
    public List<GetPersonCardListResponseModel> getcardList() {

        Response response = PostAccountListWithPersonID(cardQuery);
        List<GetPersonCardListResponseModel> cards = response.jsonPath()
                .getList("data.cards", GetPersonCardListResponseModel.class);

        return cards.stream()
                .collect(Collectors.toMap(
                        GetPersonCardListResponseModel::getAccountNumber, card -> card,
                        (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }
}
