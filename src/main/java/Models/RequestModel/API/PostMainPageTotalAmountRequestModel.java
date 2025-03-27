package Models.RequestModel.API;

import static Models.RequestModel.Web.LogInRequestModel.myMoneyCurrency;

public class PostMainPageTotalAmountRequestModel {

    public static String mainPageTotalAmount =
            "{\"variables\":{\"currency\":\"" + myMoneyCurrency + "\"},\"query\":\"query ($currency: String) {\\n  customer {\\n    defaultCurrency\\n    defaultCurrencyBalance\\n    backgroundImageAddress\\n    myMoney(currency: $currency)\\n  }\\n}\\n\"}";

}
