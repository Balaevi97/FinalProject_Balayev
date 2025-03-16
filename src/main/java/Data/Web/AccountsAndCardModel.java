package Data.Web;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class AccountsAndCardModel {

//    private String cardName;
//    private String totalAmount;
//    private String accountNumber;
//    private List<String> amountsByCurrency = new ArrayList<>();
//    private List<String> cardFunctionality = new ArrayList<>();

    private String cardName;
    private String totalAmount;
    private AccountDetails accountDetails; // Store accountNumber and amountsByCurrency together
    private List<String> cardFunctionality = new ArrayList<>();
    @Getter
    @Setter
    @ToString
    public static class AccountDetails {
        private String accountNumber;
        private List<String> amountsByCurrency = new ArrayList<>();
    }


    public AccountsAndCardModel() {
        this.accountDetails = new AccountDetails();
    }

}
