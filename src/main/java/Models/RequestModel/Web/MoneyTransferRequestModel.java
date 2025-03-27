package Models.RequestModel.Web;

import java.math.BigDecimal;

public class MoneyTransferRequestModel {

    public static String accountType = "შემნახველი";

    public static int maxAmountPage ;
    public static String transferCardAmountSymbol;
    public static Double transferCardAmountAmount;

    public static String receiverAccountForTransfer;
    public static BigDecimal receiverAccountPreviousAmountWeb;
    public static BigDecimal receiverAccountAfterAmountWeb =  BigDecimal.ZERO;

    public static BigDecimal calculatedAmountForAssertAPI = BigDecimal.ZERO;

    public static Double percentage = 15.0;
    public static Double totalPercentage = 100.0;
    public static BigDecimal amountForPayment = BigDecimal.ZERO;
}
