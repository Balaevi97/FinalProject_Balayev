
import Setup.MoneyTransferSteps;
import Steps.APISteps.AccountStep;
import Steps.FrontSteps.GetCardDetailSteps;
import Steps.FrontSteps.LogInSteps;
import Steps.FrontSteps.MoveToCardSteps;
import org.testng.annotations.Test;


import static Setup.SelenIDESetUp.openPage;

public class Run {
    LogInSteps logInSteps = new LogInSteps();
    MoveToCardSteps moveToCardSteps = new MoveToCardSteps();
    GetCardDetailSteps getCardDetailSteps = new GetCardDetailSteps();
    MoneyTransferSteps moneyTransfer = new MoneyTransferSteps();
    AccountStep accountStep = new AccountStep();
    @Test (priority = 1)
    public void LoginTest () {
        openPage("https://testmycredo.credo.ge/landing/main/auth");
        logInSteps.setUsername("bbalaevi97")
                .setPassword("Credo@1234")
                .clickSubmit()
                .setOTP("1234")
                .clickApprove()
                .removeEasyAuth()
                .getBearerToken ();
               // .assertLogin();
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
    //    moneyTransfer.maxAmountPAge();
        moneyTransfer.getAccountBalance("GE03CD0360000036901829", "GEL");
        moneyTransfer.assertAccountBalance();
//        accountStep.compareAccountInfo(getCardDetailSteps.collectCardInfo(), accountStep.getAccountList(), accountStep.getcardList());
//       // getCardDetailSteps.collectCardInfo();
//        getCardDetailSteps.operationOnCard("1234");

    }

    @Test (priority = 4)
    public void Test () {
        accountStep.getcardList();
    }



}
