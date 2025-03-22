package Steps.APISteps;

import Models.API.GetPersonCardList;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.stream.Collectors;

import static Calls.MyCredoGetAccountList.PostAccountListWithPersonID;
import static Utils.StringValues.cardQuery;

public class GetCardList {

    @Step
    public List<GetPersonCardList> getcardList() {

        Response response = PostAccountListWithPersonID(cardQuery);
        List<GetPersonCardList> cards = response.jsonPath()
                .getList("data.cards", GetPersonCardList.class);

        return cards.stream()
                .collect(Collectors.toMap(
                        GetPersonCardList::getAccountNumber, card -> card,
                        (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }
}
