package Models.ResponseModel.API;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostMainPageTotalAmountResponseModel {
    public String myMoney;
}
