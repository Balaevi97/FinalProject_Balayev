package DataController;

import Models.RequestModel.GetUserLoginListRequestModel;
import SQLDatabaseAccess.SQLDatabaseAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataController {

    public static String getUserList = """
            	IF OBJECT_ID('tempdb..#tmpuser') IS NOT NULL
            		DROP TABLE #tmpuser
            
            	SELECT   a.[ExternalId]
            			,a.[PersonalNumber]
            			,a.[FirstName]
            			,a.[LastName]
            			,b.Login
            			,b.Password
            
            			into #tmpuser
            	FROM [IBankUsermanagement].[auth].[Persons] a
            	LEFT JOIN [IBankUsermanagement].[auth].[UserLogins] b ON a.id = b.PersonId
            		where a.ExternalId in ( 2157121, 2894105, 2890805)
            
            		UPDATE  #tmpuser
            		SET Password = 'Credo@1234'
            		WHERE ExternalId = 2157121
            
            		UPDATE  #tmpuser
            		SET Password = 'Aa123123'
            		WHERE ExternalId = 2894105
            
            		UPDATE  #tmpuser
            		SET Password = 'Credo@1234'
            		WHERE ExternalId = 2890805
            
            	SELECT * from #tmpuser
            """;
    
    
    
    
    
    
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

    public static List<GetUserLoginListRequestModel> userLoginList (String AccountData) throws SQLException {

        List<GetUserLoginListRequestModel> getPersonRequestModels = new ArrayList<>();
        Connection databaseSQL = SQLDatabaseAccess.getConnectionUserLogin244();

        PreparedStatement preparedStatementFirsName = databaseSQL.prepareStatement(AccountData);
        ResultSet Accounts = preparedStatementFirsName.executeQuery();

        while (Accounts.next()) {
            GetUserLoginListRequestModel userLoginListResponseModel = new GetUserLoginListRequestModel();
            userLoginListResponseModel.setExternalId(Accounts.getString("ExternalId"));
            userLoginListResponseModel.setPersonalNumber(Accounts.getString("PersonalNumber"));
            userLoginListResponseModel.setFirstName(Accounts.getString("FirstName"));
            userLoginListResponseModel.setLastName(Accounts.getString("LastName"));
            userLoginListResponseModel.setLogin(Accounts.getString("Login"));
            userLoginListResponseModel.setPassword(Accounts.getString("Password"));
            getPersonRequestModels.add(userLoginListResponseModel);
        }

        return getPersonRequestModels;
    }


//   public static Object [][]  getUserLoginList () throws SQLException {
//       List<GetUserLoginListRequestModel> getSMSRequestList = userLoginList(DataController.getUserList);
//
//       Object[][] data = new Object[getSMSRequestList.size()][1];
//       for (int i = 0; i <getSMSRequestList.size() ; i++) {
//           data[i][0] = getSMSRequestList.get(i);
//           System.out.println(data[i][0].toString());
//       }
//       return data;
//   }

    public static List<GetUserLoginListRequestModel> getUserLoginList() throws SQLException {

        List<GetUserLoginListRequestModel> getUserLoginList = userLoginList(DataController.getUserList);

        StringBuilder data = new StringBuilder();

        for (GetUserLoginListRequestModel model : getUserLoginList) {

            data.append("ExternalId: ").append(model.getExternalId())
                    .append(", PersonalNumber: ").append(model.getPersonalNumber())
                    .append(", FirstName: ").append(model.getFirstName())
                    .append(", LastName: ").append(model.getLastName())
                    .append(", Login: ").append(model.getLogin())
                    .append(", Password: ").append(model.getPassword())
                    .append("\n");
        }


        return getUserLoginList;
    }





    public static List<GetUserLoginListRequestModel> getPersonRequestLists (String AccountData) throws SQLException {

        List<GetUserLoginListRequestModel> getPersonRequestModels = new ArrayList<>();
        Connection databaseSQL = SQLDatabaseAccess.getConnectionUserLogin244();

        PreparedStatement preparedStatementFirsName = databaseSQL.prepareStatement(AccountData);
        ResultSet Accounts = preparedStatementFirsName.executeQuery();

        while (Accounts.next()) {
            GetUserLoginListRequestModel userLoginListResponseModel = new GetUserLoginListRequestModel();
            userLoginListResponseModel.setExternalId(Accounts.getString("ExternalId"));
            userLoginListResponseModel.setPersonalNumber(Accounts.getString("PersonalNumber"));
            userLoginListResponseModel.setFirstName(Accounts.getString("FirstName"));
            userLoginListResponseModel.setLastName(Accounts.getString("LastName"));
            userLoginListResponseModel.setLogin(Accounts.getString("Login"));
            userLoginListResponseModel.setPassword(Accounts.getString("Password"));
            getPersonRequestModels.add(userLoginListResponseModel);
        }

        return getPersonRequestModels;
    }



}
