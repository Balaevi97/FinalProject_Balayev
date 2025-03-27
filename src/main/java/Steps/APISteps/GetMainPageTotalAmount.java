package Steps.APISteps;

import Models.ResponseModel.API.PostMainPageTotalAmountResponseModel;
import io.restassured.response.Response;

import static Calls.MyCredoGetAccountList.PostAccountListWithPersonID;
import static Models.RequestModel.API.PostMainPageTotalAmountRequestModel.mainPageTotalAmount;


public class GetMainPageTotalAmount {

    public static String getTotalAmount() {
        Response response = PostAccountListWithPersonID(mainPageTotalAmount);
        PostMainPageTotalAmountResponseModel amount = response.jsonPath()
                .getObject("data.customer", PostMainPageTotalAmountResponseModel.class);
        return amount.getMyMoney();
    }
}
