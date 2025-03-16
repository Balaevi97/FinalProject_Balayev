import Data.API.GetPersonAccountListResponseModel;
import Data.Web.AccountsAndCardModel;
import Steps.APISteps.AccountStep;
import Steps.FrontSteps.GetCardDetailSteps;
import Steps.FrontSteps.LogInSteps;
import Steps.FrontSteps.MoveToCardSteps;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static Setup.SelenIDESetUp.openPage;

public class Run {
    LogInSteps logInSteps = new LogInSteps();
    MoveToCardSteps moveToCardSteps = new MoveToCardSteps();
    GetCardDetailSteps getCardDetailSteps = new GetCardDetailSteps();
    AccountStep accountStep = new AccountStep();
    @Test (priority = 1)
    public void LoginTest () {
        openPage("https://testmycredo.credo.ge/landing/main/auth");
        logInSteps.setUsername("BBALAEVI97")
                .setPassword("Credo@1234")
                .clickSubmit()
                .setOTP("1234")
                .clickApprove()
                .removeEasyAuth()
                .getBearerToken ()
                .assertLogin();
    }


    @Test (priority = 2)
    public void moveToCardSteps () {

        moveToCardSteps.clickProducts()
                        .openProdList()
                        .moveToProduct()
                        .assertPage();
    }

    @Test (priority = 3)
    public void getCardInfo () {
       var a = getCardDetailSteps.executeMethods();
       var b =  accountStep.getAccountList();
        accountStep.compareAccountInfo(a,b);
    //accountStep.compareAccountInfo(getCardDetailSteps.executeMethods(), accountStep.getAccountList());
        String ab = "";
    }

//    @Test
//    public void  Test () {
//        accountStep.getAccountList();
//        String a = "";
//
//    }

}
