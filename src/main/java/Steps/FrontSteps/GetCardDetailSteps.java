package Steps.FrontSteps;

import Data.Web.AccountsAndCardModel;
import Elements.GetCardDetail;
import io.qameta.allure.Step;

import java.util.*;


public class GetCardDetailSteps extends GetCardDetail {

//public List<AccountsAndCardModel> getAllCardsInfo() {
//    List<AccountsAndCardModel> cards = new ArrayList<>();
//    int i = 0;
//
//    while (i < getAllElement.size()) {
//        AccountsAndCardModel card = new AccountsAndCardModel();
//
//        if (i < getAllElement.size()) {
//            card.setCardName(getAllElement.get(i++).getText());
//        }
//
//        if (i < getAllElement.size()) {
//            card.setTotalAmount(getAllElement.get(i++).getText());
//        }
//
//        while (i < getAllElement.size() && getAllElement.get(i).getText().matches(".*[0-9].*")) {
//            card.getAmountsByCurrency().add(getAllElement.get(i++).getText());
//        }
//
//        while (i < getAllElement.size() && !getAllElement.get(i).getText().matches(".*[0-9].*")) {
//            card.getCardFunctionality().add(getAllElement.get(i++).getText());
//        }
//        card.setAccountNumber(accountNumber.getText());
//        cards.add(card);
//    }
//    return cards;
//}



    public List<AccountsAndCardModel> getAllCardsInfo() {
        List<AccountsAndCardModel> cards = new ArrayList<>();
        int i = 0;

        while (i < getAllElement.size()) {
            AccountsAndCardModel card = new AccountsAndCardModel();
            AccountsAndCardModel.AccountDetails accountDetails = new AccountsAndCardModel.AccountDetails();

            if (i < getAllElement.size()) {
                card.setCardName(getAllElement.get(i++).getText());
            }

            if (i < getAllElement.size()) {
                card.setTotalAmount(getAllElement.get(i++).getText());
            }

            // Assign account number
            accountDetails.setAccountNumber(accountNumber.getText());

            // Process amountsByCurrency under accountNumber
            while (i < getAllElement.size() && getAllElement.get(i).getText().matches(".*[0-9].*")) {
                accountDetails.getAmountsByCurrency().add(getAllElement.get(i++).getText());
            }

            // Assign the structured accountDetails to the card
            card.setAccountDetails(accountDetails);

            // Process cardFunctionality separately
            while (i < getAllElement.size() && !getAllElement.get(i).getText().matches(".*[0-9].*")) {
                card.getCardFunctionality().add(getAllElement.get(i++).getText());
            }

            cards.add(card);
        }
        return cards;
    }


    @Step
    ///  მიმდინარე ბარათის გვერდის წამოღება
    public int getCurrentPage() {
        String text = pages.getText().trim();
        String[] parts = text.split("/");

        return Integer.parseInt(parts[0]);
        
    }
    @Step
    ///  ბარათების ჯამური გვერდის წამოღება
    public int getTotalPagesCount () {
        String text = pages.getText().trim();
        String[] parts = text.split("/");
        
        return Integer.parseInt(parts[1]);
    }

    @Step
    /// მომდევნო ბარათზე გადასვლა
    public void next () {
        nextProduct.click();
    }


    /// მეთოდების გამოძახება გვერდების რაოდენობის მიხედვით
    public List<AccountsAndCardModel> executeMethods() {
        List<AccountsAndCardModel> allCardsInfo = new ArrayList<>();

        for (int i = 1; i <= getTotalPagesCount(); i++) {
            System.out.println("\n Current Page [" + getCurrentPage() + "]");

            List<AccountsAndCardModel> allCards = getAllCardsInfo();

            for (AccountsAndCardModel currency : allCards) {
                if (currency.getCardName() == null || currency.getTotalAmount() == null || currency.getCardName().isEmpty()) {
                    continue;
                }

                System.out.println("Currency Details: ");
                System.out.println("Card Name: " + currency.getCardName());
                System.out.println("Total Amount: " + currency.getTotalAmount());

                // Get account details
                AccountsAndCardModel.AccountDetails accountDetails = currency.getAccountDetails();
                if (accountDetails != null) {
                    System.out.println("Account Number: " + accountDetails.getAccountNumber());

                    for (String amount : accountDetails.getAmountsByCurrency()) {
                        if (amount != null && !amount.isEmpty()) {
                            char currencySymbol = amount.charAt(amount.length() - 1);
                            System.out.println("Amount in " + currencySymbol + ": " + amount);
                        }
                    }
                }

                // Print Card Functionality details
                for (String function : currency.getCardFunctionality()) {
                    if (function != null && !function.isEmpty()) {
                        System.out.println("ბარათის ფუნქცია: " + function);
                    }
                }

                allCardsInfo.add(currency);
            }

            next();
        }

        // Sorting based on the updated structure
        allCardsInfo.sort(Comparator.comparing(card -> card.getAccountDetails().getAccountNumber()));

        return allCardsInfo;
    }



}




        



