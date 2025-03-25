package Utils;

import java.math.BigDecimal;

public class StringValues {
    /// Users
//    public static String bahruzUsername = "bbalaevi97";
//    public static String bahruzPassword = "Credo@1234";
//    public static String iloUsername = "IILO01";
//    public static String iloPassword = "Aa123123";
    public static String personId;
    public static String username;
    public static String password;
    /// Main page variables
    public static String token;
    public static String myMoney;
    public static String myMoneyCurrency;


    ///  Product variables
    public static String OTP = "1234";
    public static String accountType = "მიმდინარე";
    public static String accountType1 = "Instant Visa Credit Card Digital";


    ///  Graphql queries
    public String mainPageTotalAmount =
            "{\"variables\":{\"currency\":\"" + myMoneyCurrency + "\"},\"query\":\"query ($currency: String) {\\n  customer {\\n    defaultCurrency\\n    defaultCurrencyBalance\\n    backgroundImageAddress\\n    myMoney(currency: $currency)\\n  }\\n}\\n\"}";

    public static String accountQuery = """
            {"variables":{},"query":"{\\n  accounts {\\n    accountItemId\\n    hasActiveWallet\\n    accountId\\n    accountNumber\\n    account\\n    currency\\n    categoryId\\n    category\\n    hasCard\\n    status\\n    type\\n    cssAccountId\\n    availableBalance\\n    currencyPriority\\n    availableBalanceEqu\\n    isDefault\\n    isHidden\\n    rate\\n    activationDate\\n    allowedOperations\\n  }\\n}\\n"}
    """;
    public static String cardQuery = """
            {"variables":{},"query":"{\\n  cards {\\n    cardId\\n    cardNumber\\n    cardCurrency\\n    cardNickName\\n    cardImageId\\n    cardImageAddress\\n    cardStatusId\\n    cardProduct\\n    cardAvailableAmount\\n    cardBlockedAmount\\n    cardExpireShortDate\\n    cardStatus\\n    cardExpireDate\\n    accountNumber\\n    isDigitalCard\\n    applicationId\\n    cardSafetyProductId\\n    cardSafetyPackageStatus\\n  }\\n}\\n"}
            """;

    /// Money transfer variables
    public static Double maxAmountWeb;
    public static int maxAmountPage ;

    public static BigDecimal accountBalanceAPI2;

    public static String transferCardAmountSymbol;
    public static Double transferCardAmountAmount;

    public static String receiverAccountForTransfer;
    public static BigDecimal receiverAccountPreviousAmount;

    public static Double percentage = 15.0;
    public static Double totalPercentage = 100.0;
}
