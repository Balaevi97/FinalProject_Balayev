package Models.RequestModel.API;

import lombok.Data;

@Data
public class GetUserLoginListRequestModel {

    private String ExternalId;
    private String PersonalNumber;
    private String FirstName;
    private String LastName;
    private String Login;
    private String Password;

}
