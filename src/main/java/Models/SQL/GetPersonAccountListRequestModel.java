package Models.SQL;

import lombok.Data;

@Data
public class GetPersonAccountListRequestModel {

    private String accountId;
    private String t24AccountId;
    private String currencyId;
    private String personId;
    private String firstName;
    private String lastName;
    private String accountNumber;
    private String categoryId;
    private String currency;
    private String openActualBal;
    private String openActualBalInGel;
    private String openClearedBal;
    private String openClearedBalInGel;
    private String onlineActualBal;
    private String onlineActualBalInGel;
    private String onlineClearedBal;
    private String onlineClearedBalInGel;
    private String iban;
    private String description;



}
