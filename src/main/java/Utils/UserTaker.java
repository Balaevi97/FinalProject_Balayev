package Utils;

import Models.RequestModel.API.GetUserLoginListRequestModel;
import io.qameta.allure.Step;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import static Models.RequestModel.Web.LogInRequestModel.*;
import static DataController.DataController.getUserLoginList;


public class UserTaker {

    @Step
    public static void generateRandomUser () throws SQLException {
        List<GetUserLoginListRequestModel> users = getUserLoginList();
        Random random = new Random();
        GetUserLoginListRequestModel userInfo = users.get(random.nextInt(users.size()));
        personId = userInfo.getExternalId();
        username = userInfo.getLogin();
        password = userInfo.getPassword();
        System.out.println("personId: " + personId + " username: " + username + " password: " + password);

    }


}
