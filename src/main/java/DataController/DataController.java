package DataController;

import Models.SQL.GetPersonAccountListRequestModel;
import SQLDatabaseAccess.SQLDatabaseAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataController {

    public String getPersonAccountList = """

                    SELECT  acc.[AccountId]
                            ,a.[T24AccountId]
                            ,a.CurrencyId
                            ,acc.PersonId
                            ,acc.[Account]
                            ,acc.[AccountNumber]
                            ,acc.[CategoryId]
                            ,t24.CURRENCY
                            ,t24.OPEN_ACTUAL_BAL
                			,ROUND(CASE
                						WHEN t24.CURRENCY = 'EUR' THEN CAST((t24.OPEN_ACTUAL_BAL * 2.945) AS FLOAT)
                						WHEN t24.CURRENCY = 'USD' THEN CAST((t24.OPEN_ACTUAL_BAL * 2.815) AS FLOAT)
                						ELSE CAST(t24.OPEN_ACTUAL_BAL AS FLOAT)
                					END, 2
                			) AS OPEN_ACTUAL_BAL_InGEL
            
                            ,t24.OPEN_CLEARED_BAL
                			,ROUND(CASE
                						WHEN t24.CURRENCY = 'EUR' THEN CAST((t24.OPEN_CLEARED_BAL * 2.945) AS FLOAT)
                						WHEN t24.CURRENCY = 'USD' THEN CAST((t24.OPEN_CLEARED_BAL * 2.815)  AS FLOAT)
                						ELSE CAST(t24.OPEN_CLEARED_BAL  AS FLOAT)
                					END,2
                			) AS OPEN_CLEARED_BAL_InGEL
            
                            ,t24.ONLINE_ACTUAL_BAL
                			,ROUND(CASE
                						WHEN t24.CURRENCY = 'EUR' THEN CAST((t24.ONLINE_ACTUAL_BAL * 2.945) AS FLOAT)
                						WHEN t24.CURRENCY = 'USD' THEN CAST((t24.ONLINE_ACTUAL_BAL * 2.815) AS FLOAT)
                						ELSE CAST(t24.ONLINE_ACTUAL_BAL AS FLOAT)
                					END,2
                			) AS ONLINE_ACTUAL_BAL_InGEL
            
                            ,t24.ONLINE_CLEARED_BAL
                			,ROUND(CASE
                						WHEN t24.CURRENCY = 'EUR' THEN CAST((t24.ONLINE_CLEARED_BAL * 2.945) AS FLOAT)
                						WHEN t24.CURRENCY = 'USD' THEN CAST((t24.ONLINE_CLEARED_BAL * 2.815) AS FLOAT)
                						ELSE CAST(t24.ONLINE_CLEARED_BAL AS FLOAT)
                					END, 2
                			) AS ONLINE_CLEARED_BAL_InGEL
            
                            ,t24.IBAN
                            ,cat.Description
            
                      FROM [CredoBnk].[account].[Account] acc
                      inner join [CredoBnk].[account].[AccountItem] a on a.[AccountId] = acc.[AccountId]
                      inner join [CredoBnk].[dbo].[CustomerAccountType] cat on cat.Category = acc.CategoryId
                      inner join [T24DATA].[dbo].[ACCOUNTS] t24 on t24.ACCOUNT_NUMBER = cast(a.[T24AccountId] as VARCHAR(50)) and acc.[AccountNumber] = LEFT(t24.IBAN, LEN(t24.IBAN) - 3)
                   
                      where acc.PersonId = ?
                      and a.DateUpdated is null
            """;

    public static List<GetPersonAccountListRequestModel> getPersonRequestLists (String AccountData) throws SQLException {

        List<GetPersonAccountListRequestModel> getPersonRequestModels = new ArrayList<>();
        Connection databaseSQL = SQLDatabaseAccess.getConnectionSMSModule();

        PreparedStatement preparedStatementFirsName = databaseSQL.prepareStatement(AccountData);
        ResultSet Accounts = preparedStatementFirsName.executeQuery();

        while (Accounts.next()) {
            GetPersonAccountListRequestModel getPersonAccountListRequestModel = new GetPersonAccountListRequestModel();
            getPersonAccountListRequestModel.setPersonId(Accounts.getString("PersonId"));
            getPersonAccountListRequestModel.setFirstName(Accounts.getString("FirstName"));
            getPersonAccountListRequestModel.setLastName(Accounts.getString("LastName"));
            getPersonAccountListRequestModel.setCurrency(Accounts.getString("CURRENCY"));
            getPersonAccountListRequestModel.setOpenActualBalInGel(Accounts.getString(""));
            getPersonRequestModels.add(getPersonAccountListRequestModel);
        }

        return getPersonRequestModels;
    }



}
