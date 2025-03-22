package Steps.APISteps;

import Models.API.GetMainPageTotalAmountResponseModel;
import io.restassured.response.Response;

import static Calls.MyCredoGetAccountList.PostAccountListWithPersonID;
import static Utils.StringValues.mainPageTotalAmount;

public class GetMainPageTotalAmount {

    public static String getTotalAmount() {
        Response response = PostAccountListWithPersonID(mainPageTotalAmount);
        GetMainPageTotalAmountResponseModel amount = response.jsonPath()
                .getObject("data.customer", GetMainPageTotalAmountResponseModel.class);
        return amount.getMyMoney();
    }
}
