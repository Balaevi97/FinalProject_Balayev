package Setup;

import Data.API.GetPersonAccountListResponseModel;
import Data.Web.AccountsAndCardModel;
import Steps.APISteps.AccountStep;
import Steps.FrontSteps.GetCardDetailSteps;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class MoneyTransferSteps {

GetCardDetailSteps getCardDetailSteps = new GetCardDetailSteps();
    Double maxAmount;
    int maxAmountPage ;
    String accountBalance;


    AccountStep accountStep = new AccountStep();
    public int maxAmountPAge() {


        for (int i = 1; i <= getCardDetailSteps.getTotalPagesCount(); i++) {
            System.out.println("\n Current Page [" + getCardDetailSteps.getCurrentPage() + "]");

            List<Map.Entry<String, List<AccountsAndCardModel>>> allCardsGrouped = getCardDetailSteps.getAllCardsInfo();

            for (Map.Entry<String, List<AccountsAndCardModel>> entry : allCardsGrouped) {
                List<AccountsAndCardModel> cards = entry.getValue();

                for (AccountsAndCardModel currency : cards) {
                    if (currency.getCardName() == null || currency.getTotalAmount() == null || currency.getCardName().isEmpty()) {
                        continue;
                    }

                    try {
                        String totalAmount = String.valueOf(Double.parseDouble(currency.getTotalAmount().replaceAll("[^a-zA-Z0-9]","")));
                        // String parsedAmount = totalAmount.replaceAll("[^a-zA-Z0-9]","");
                        double amountInDouble = Double.parseDouble(totalAmount);
                        if (maxAmount == null || amountInDouble > maxAmount) {
                            maxAmount = amountInDouble;
                            maxAmountPage = i;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format for amount: " + currency.getTotalAmount());
                    }
                }
            }
            getCardDetailSteps.next();
        }

        System.out.println("Max Amount Found: " + maxAmount + " on Page: " + maxAmountPage);

        return maxAmountPage;
    }

    public void goToMaxAmountPage() {
        while (getCardDetailSteps.getCurrentPage() != maxAmountPage) {
            if (getCardDetailSteps.getCurrentPage() < maxAmountPage) {
                getCardDetailSteps.next();
            } else {
                getCardDetailSteps.previous();
            }
        }
        System.out.println("Returned to Page: " + getCardDetailSteps.getCurrentPage());
    }

    @Step
    public String getAccountBalance (String accountNumber, String currency) {
        List<Map.Entry<String, List<GetPersonAccountListResponseModel>>> filteredAccountList = accountStep.getAccountList();

        for (Map.Entry<String, List<GetPersonAccountListResponseModel>> entry : filteredAccountList) {
            if (entry.getKey().equals(accountNumber)) {
                for (GetPersonAccountListResponseModel account : entry.getValue()) {
                    if (account.getCurrency().equals(currency)) {
                        this.accountBalance = account.getAvailableBalance();
                        return accountBalance;
                    }
                }
            }
        }

        this.accountBalance = null;
        return null;
    }

    public void assertAccountBalance () {
        Assert.assertNotEquals(accountBalance,
                getAccountBalance("GE03CD0360000036901829", "GEL"));
    }

}