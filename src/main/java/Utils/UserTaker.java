package Utils;

import Models.RequestModel.GetUserLoginListRequestModel;
import static Utils.StringValues.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import static DataController.DataController.getUserLoginList;

public class UserTaker {

    public static void generateRandomUser () throws SQLException {
        List<GetUserLoginListRequestModel> users = getUserLoginList();
        Random random = new Random();
        GetUserLoginListRequestModel userInfo = users.get(random.nextInt(users.size()));
        personId = userInfo.getExternalId();
        username = userInfo.getLogin();
        password = userInfo.getPassword();
        System.out.println("კლიენტის პერსონ აიდი არის: " + personId + " მომხმარებლის სახელი: " + username + " პაროლი არის: " + password);

    }


}
