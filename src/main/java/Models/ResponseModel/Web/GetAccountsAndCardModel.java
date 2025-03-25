package Models.ResponseModel.Web;

import lombok.*;

import java.util.*;

@Data
public class GetAccountsAndCardModel {

    private  String cardName;
    private  String totalAmount;
    private  String accountNumber;
    private  List<String> amountsByCurrency = new ArrayList<>();

}
