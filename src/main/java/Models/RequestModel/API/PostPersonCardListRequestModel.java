package Models.RequestModel.API;

public class PostPersonCardListRequestModel {

    public static String cardQuery = """
            {"variables":{},"query":"{\\n  cards {\\n    cardId\\n    cardNumber\\n    cardCurrency\\n    cardNickName\\n    cardImageId\\n    cardImageAddress\\n    cardStatusId\\n    cardProduct\\n    cardAvailableAmount\\n    cardBlockedAmount\\n    cardExpireShortDate\\n    cardStatus\\n    cardExpireDate\\n    accountNumber\\n    isDigitalCard\\n    applicationId\\n    cardSafetyProductId\\n    cardSafetyPackageStatus\\n  }\\n}\\n"}
            """;
}
