
import Steps.APISteps.GetCardList;
import Steps.FrontSteps.*;
import Steps.APISteps.GetAccountList;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static Models.RequestModel.Web.LogInRequestModel.*;
import static Models.RequestModel.Web.MoneyTransferRequestModel.*;
import static Setup.SelenIDESetUp.openPage;


import static Utils.URL.*;
import static Utils.UserTaker.*;


public class TestRunner {
    LogInSteps logInSteps = new LogInSteps();
    MoveToCardSteps moveToCardSteps = new MoveToCardSteps();
    GetCardDetailSteps getCardDetailSteps = new GetCardDetailSteps();
    MoneyTransferSteps moneyTransfer = new MoneyTransferSteps();
    GetAccountList accountStep = new GetAccountList();
    GetCardList getcardList = new GetCardList();


    @Test (priority = 1)
    public void LoginTest () throws SQLException {
        generateRandomUser ();
        openPage(myCredo);
        logInSteps.setUsername(username)
                    .setPassword(password)
                    .clickSubmit()
                    .setOTP(OTPCode)
                    .clickApprove()
                    .removeEasyAuthWindow()
                    .removeContinueProcessWindow()
                    .getBearerToken ()
                    .myMoney()
                    .assertLogin();
    }


    @Test (priority = 2)
    public void moveToCardSteps () {
        moveToCardSteps.clickProducts()
                        .openProdList()
                        .moveToProduct()
                        .assertIsOnRightPage();
    }

    @Test (priority = 3)
    public void getCardInfo () {
        getCardDetailSteps.cardOperation(OTPCode);
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
        moneyTransfer.moveToFirstPage ()
                    .getMaxAmountPAge();
        moneyTransfer.goToMaxAmountPage()
                    .moveToTransfer ()
                    .transferToOwnAccount ()
                    .getTransferCardAmount();
        moneyTransfer.getTransferCardCurrency();
        moneyTransfer.openReceiverAccountList ()
                    .choseAccount();
        moneyTransfer.getAccountBalanceAPI(receiverAccountForTransfer, transferCardAmountSymbol, true);
        moneyTransfer.getReceiverAccountAmount();
        moneyTransfer.calculateTransferAmount();
        moneyTransfer.chooseCurrency();
        moneyTransfer.getCalculatedAmountForAssertWeb ();
        moneyTransfer.setTransferAmount ()
                    .approvePayment ()
                    .closeMessageWindow ();
        moneyTransfer.getAccountBalanceAPI(receiverAccountForTransfer, transferCardAmountSymbol, false);
        moneyTransfer.getCalculatedAmountForAssertAPI ();
        moneyTransfer.assertAccountBalanceAPI ()
                    .clickProducts ()
                    .openProdList ()
                    .getRenewalAccountAmount()
                    .checkIfAmountChanged ();
        moneyTransfer.assertAccountBalanceWeb ();
    }

}