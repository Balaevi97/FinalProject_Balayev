
import Steps.FrontSteps.*;
import Steps.APISteps.AccountStep;
import org.testng.annotations.Test;


import static Setup.SelenIDESetUp.openPage;
import static Utils.StringValues.*;
import static Utils.URL.myCredo;

public class TestRunner {
    LogInSteps logInSteps = new LogInSteps();
    MoveToCardSteps moveToCardSteps = new MoveToCardSteps();
    GetCardDetailSteps getCardDetailSteps = new GetCardDetailSteps();
    MoneyTransferSteps moneyTransfer = new MoneyTransferSteps();
    AccountStep accountStep = new AccountStep();
    @Test (priority = 1)
    public void LoginTest () {
        openPage(myCredo);
        logInSteps.setUsername(bahruzUsername)
                .setPassword(bahruzPassword)
                .clickSubmit()
                .setOTP(OTP)
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
        accountStep.compareAccountInfo(getCardDetailSteps.collectCardInfo(), accountStep.getAccountList(), accountStep.getcardList());


    }

    @Test (priority = 4)
    public void moneyTransfer () {
        moneyTransfer.getMaxAmountPAge();
        getCardDetailSteps.operationOnCard(OTP);
        moneyTransfer.goToMaxAmountPage()
                .moveToTransfer ()
                .transferToOwnAccount ()
                .getTransferCardCurrency();

        moneyTransfer.openReceiverAccountList ()
                .choseAccount();
        moneyTransfer.getAccountBalanceAPI(moneyTransfer.receiverAccountForTransfer,
                moneyTransfer.transferCardAmountSymbol);
        moneyTransfer.getReceiverAccountAmount();
        moneyTransfer.chooseCurrency();

        moneyTransfer.setTransferAmount ()
                .approvePayment ()
                .closeMessageWindow ()
                .assertAccountBalanceAPI ()
                .clickProducts ()
                .assertAccountBalanceWeb ();

    }




}
