package Utils;

public class URL {

    public static String MyCredoAccountList = "https://testpublicapi.mycredo.ge";
    public static String accountType = "მიმდინარე";
    public static String accountQuery = """
            {"variables":{},"query":"{\\n  accounts {\\n    accountItemId\\n    hasActiveWallet\\n    accountId\\n    accountNumber\\n    account\\n    currency\\n    categoryId\\n    category\\n    hasCard\\n    status\\n    type\\n    cssAccountId\\n    availableBalance\\n    currencyPriority\\n    availableBalanceEqu\\n    isDefault\\n    isHidden\\n    rate\\n    activationDate\\n    allowedOperations\\n  }\\n}\\n"}
            """;
    public static String cardQuery = """
            {"variables":{},"query":"{\\n  cards {\\n    cardId\\n    cardNumber\\n    cardCurrency\\n    cardNickName\\n    cardImageId\\n    cardImageAddress\\n    cardStatusId\\n    cardProduct\\n    cardAvailableAmount\\n    cardBlockedAmount\\n    cardExpireShortDate\\n    cardStatus\\n    cardExpireDate\\n    accountNumber\\n    isDigitalCard\\n    applicationId\\n    cardSafetyProductId\\n    cardSafetyPackageStatus\\n  }\\n}\\n"}
""";



}
