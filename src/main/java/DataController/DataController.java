package DataController;

import Models.RequestModel.API.GetUserLoginListRequestModel;
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

}
