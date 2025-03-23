import Steps.APISteps.GetCardList;
import Steps.FrontSteps.*;
import Steps.APISteps.GetAccountList;
import org.testng.annotations.Test;

import static Setup.SelenIDESetUp.openPage;
import static Utils.StringValues.*;
import static Utils.URL.*;

public class TestRunner {
    LogInSteps logInSteps = new LogInSteps();
    MoveToCardSteps moveToCardSteps = new MoveToCardSteps();
    GetCardDetailSteps getCardDetailSteps = new GetCardDetailSteps();
    MoneyTransferSteps moneyTransfer = new MoneyTransferSteps();
    GetAccountList accountStep = new GetAccountList();
    GetCardList getcardList = new GetCardList();

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
                .myMoney()
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
        getCardDetailSteps.operationOnCard(OTP);
        getCardDetailSteps.checkFileExistence();
        getCardDetailSteps.deleteRequisiteFile ()
                            .assertDeleteRequisiteFileMethod ();
        getCardDetailSteps.downloadRequisite ()
                            .assertDownloadRequisiteMethod ();
        getCardDetailSteps.moveToFirstPage ();
        getCardDetailSteps.compareAccountInfo(getCardDetailSteps.collectCardInfo(), accountStep.getAccountList(), getcardList.getcardList())
        .moveToFirstPage ();
    }

    @Test (priority = 4)
    public void moneyTransfer () {
        moneyTransfer//.moveToFirstPage ()
                    .getMaxAmountPAge();
        moneyTransfer.goToMaxAmountPage()
                    .moveToTransfer ()
                    .transferToOwnAccount ()
                    .getTransferCardCurrency();
        moneyTransfer.openReceiverAccountList ()
                    .choseAccount();
        moneyTransfer.getAccountBalanceAPI(receiverAccountForTransfer, transferCardAmountSymbol);
        moneyTransfer.getReceiverAccountAmount();
        moneyTransfer.chooseCurrency();
        moneyTransfer.setTransferAmount ()
                    .approvePayment ()
                    .closeMessageWindow ()
                    .checkAccountBalanceAPI()
                    .assertAccountBalanceAPI ()
                    .clickProducts ()
                    .openProdList ()
                    .getRenewalAccountAmount()
                    .assertAmountChangesOnAccount ();
        moneyTransfer.assertAccountBalanceWeb ();
    }

}