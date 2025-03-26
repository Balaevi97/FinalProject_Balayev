package Models.RequestModel;

public class PostPersonAccountListRequestModel {

    public static String accountQuery = """
            {"variables":{},"query":"{\\n  accounts {\\n    accountItemId\\n    hasActiveWallet\\n    accountId\\n    accountNumber\\n    account\\n    currency\\n    categoryId\\n    category\\n    hasCard\\n    status\\n    type\\n    cssAccountId\\n    availableBalance\\n    currencyPriority\\n    availableBalanceEqu\\n    isDefault\\n    isHidden\\n    rate\\n    activationDate\\n    allowedOperations\\n  }\\n}\\n"}
    """;
}
