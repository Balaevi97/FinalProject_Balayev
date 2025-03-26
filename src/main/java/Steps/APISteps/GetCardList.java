package Steps.APISteps;

import Models.ResponseModel.API.PostPersonCardListResponseModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.stream.Collectors;

import static Calls.MyCredoGetAccountList.PostAccountListWithPersonID;
import static Models.RequestModel.PostPersonCardListRequestModel.cardQuery;

public class GetCardList {

    @Step
    public List<PostPersonCardListResponseModel> getcardList() {

        Response response = PostAccountListWithPersonID(cardQuery);
        List<PostPersonCardListResponseModel> cards = response.jsonPath()
                .getList("data.cards", PostPersonCardListResponseModel.class);

        return cards.stream()
                .collect(Collectors.toMap(
                        PostPersonCardListResponseModel::getAccountNumber, card -> card,
                        (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }
}
