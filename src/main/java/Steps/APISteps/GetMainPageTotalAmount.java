package Steps.APISteps;

import Models.API.GetMainPageTotalAmountResponseModel;
import Utils.StringValues;
import io.restassured.response.Response;

import static Calls.MyCredoGetAccountList.PostAccountListWithPersonID;


public class GetMainPageTotalAmount {

    public static String getTotalAmount() {
        StringValues stringValues = new StringValues();
        System.out.println(stringValues.mainPageTotalAmount);
        Response response = PostAccountListWithPersonID(stringValues.mainPageTotalAmount);
        GetMainPageTotalAmountResponseModel amount = response.jsonPath()
                .getObject("data.customer", GetMainPageTotalAmountResponseModel.class);
        return amount.getMyMoney();
    }
}
