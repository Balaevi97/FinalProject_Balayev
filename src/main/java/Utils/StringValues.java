package Utils;

public class StringValues {
    /// Users
    public static String bahruzUsername = "bbalaevi97";
    public static String bahruzPassword = "Credo@1234";
    public static String iloUsername = "IILO01";
    public static String iloPassword = "Aa123123";


    public static String OTP = "1234";
    public static String accountType = "მიმდინარე";
    public static String accountType1 = "Instant Visa Credit Card Digital";

    public static String mainPageTotalAmount = """
            {"variables":{"currency":"GEL"},"query":"query ($currency: String) {\\n  customer {\\n    defaultCurrency\\n    defaultCurrencyBalance\\n    backgroundImageAddress\\n    myMoney(currency: $currency)\\n  }\\n}\\n"}
            """;

    public static String accountQuery = """
            {"variables":{},"query":"{\\n  accounts {\\n    accountItemId\\n    hasActiveWallet\\n    accountId\\n    accountNumber\\n    account\\n    currency\\n    categoryId\\n    category\\n    hasCard\\n    status\\n    type\\n    cssAccountId\\n    availableBalance\\n    currencyPriority\\n    availableBalanceEqu\\n    isDefault\\n    isHidden\\n    rate\\n    activationDate\\n    allowedOperations\\n  }\\n}\\n"}
            """;
    public static String cardQuery = """
            {"variables":{},"query":"{\\n  cards {\\n    cardId\\n    cardNumber\\n    cardCurrency\\n    cardNickName\\n    cardImageId\\n    cardImageAddress\\n    cardStatusId\\n    cardProduct\\n    cardAvailableAmount\\n    cardBlockedAmount\\n    cardExpireShortDate\\n    cardStatus\\n    cardExpireDate\\n    accountNumber\\n    isDigitalCard\\n    applicationId\\n    cardSafetyProductId\\n    cardSafetyPackageStatus\\n  }\\n}\\n"}
            """;

    public static Double maxAmountWeb;
    public static int maxAmountPage ;

    public static Double accountBalanceAPI2 = 0.0;

    public static String transferCardAmountSymbol;

    public static String receiverAccountForTransfer;
    public static Double receiverAccountPreviousAmount;




        public static Double percentage = 15.0;
        public static Double totalPercentage = 100.0;
}
