package Models.API;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPersonCardList {
    public String cardId;
    public String cardNumber;
    public String cardCurrency;
    public String cardNickName;
    public String cardImageAddress;
    public String cardProduct;
    public String cardExpireShortDate;
    public String cardExpireDate;
    public String accountNumber;
    public String applicationId;


}
