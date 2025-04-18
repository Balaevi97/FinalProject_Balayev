package Models.ResponseModel.API;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostPersonAccountListResponseModel {

    public String accountItemId;
    public String accountId;
    public String accountNumber;
    public String account;
    public String currency;
    public String category;
    public String availableBalance;
    public String availableBalanceEqu;

}
