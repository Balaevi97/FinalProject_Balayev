package Models.RequestModel;

import static Steps.FrontSteps.LogInSteps.myMoneyCurrency;

public class PostMainPageTotalAmountRequestModel {

    public static String mainPageTotalAmount =
            "{\"variables\":{\"currency\":\"" + myMoneyCurrency + "\"},\"query\":\"query ($currency: String) {\\n  customer {\\n    defaultCurrency\\n    defaultCurrencyBalance\\n    backgroundImageAddress\\n    myMoney(currency: $currency)\\n  }\\n}\\n\"}";

}
