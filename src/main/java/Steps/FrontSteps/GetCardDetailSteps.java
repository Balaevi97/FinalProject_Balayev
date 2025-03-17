package Steps.FrontSteps;

import Data.Web.AccountsAndCardModel;
import Elements.GetCardDetail;
import io.qameta.allure.Step;


import java.util.*;


public class GetCardDetailSteps extends GetCardDetail {

    @Step
    public List<Map.Entry<String, List<AccountsAndCardModel>>> getAllCardsInfo() {
        Map<String, List<AccountsAndCardModel>> groupedCards = new HashMap<>();
        int i = 0;

        while (i < getAllElement.size()) {
            AccountsAndCardModel card = new AccountsAndCardModel();

            if (i < getAllElement.size()) {
                card.setCardName(getAllElement.get(i++).getText());
            }

            if (i < getAllElement.size()) {
                card.setTotalAmount(getAllElement.get(i++).getText());
            }

            String accountNumberText = accountNumber.getText();
            card.setAccountNumber(accountNumberText);

            List<String> amountsByCurrency = new ArrayList<>();
            while (i < getAllElement.size() && getAllElement.get(i).getText().matches(".*[0-9].*")) {
                amountsByCurrency.add(getAllElement.get(i++).getText());
            }
            card.setAmountsByCurrency(amountsByCurrency);

            List<String> cardFunctionalities = new ArrayList<>();
            while (i < getAllElement.size() && !getAllElement.get(i).getText().matches(".*[0-9].*")) {
                cardFunctionalities.add(getAllElement.get(i++).getText());
            }
            card.setCardFunctionality(cardFunctionalities);

            if (!groupedCards.containsKey(accountNumberText)) {
                groupedCards.put(accountNumberText, new ArrayList<>());
            }
            groupedCards.get(accountNumberText).add(card);
        }

        List<Map.Entry<String, List<AccountsAndCardModel>>> result = new ArrayList<>(groupedCards.entrySet());
        result.sort(Comparator.comparing(Map.Entry::getKey));

        return result;
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
    public TreeMap<String, AccountsAndCardModel> executeMethods() {
        List<AccountsAndCardModel> allCardsInfo = new ArrayList<>();

        for (int i = 1; i <= getTotalPagesCount(); i++) {
            System.out.println("\n Current Page [" + getCurrentPage() + "]");

            // Get all cards grouped by account number
            List<Map.Entry<String, List<AccountsAndCardModel>>> allCardsGrouped = getAllCardsInfo();

            for (Map.Entry<String, List<AccountsAndCardModel>> entry : allCardsGrouped) {
                String accountNumber = entry.getKey();
                List<AccountsAndCardModel> cards = entry.getValue();

                for (AccountsAndCardModel currency : cards) {
                    if (currency.getCardName() == null || currency.getTotalAmount() == null || currency.getCardName().isEmpty()) {
                        continue;
                    }

                    System.out.println("Currency Details: ");
                    System.out.println("Card Name: " + currency.getCardName());
                    System.out.println("Total Amount: " + currency.getTotalAmount());

                    // Print Account Number directly from the model
                    System.out.println("Account Number: " + currency.getAccountNumber());

                    // Print AmountsByCurrency details
                    for (String amount : currency.getAmountsByCurrency()) {
                        if (amount != null && !amount.isEmpty()) {
                            char currencySymbol = amount.charAt(amount.length() - 1);
                            System.out.println("Amount in " + currencySymbol + ": " + amount);
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
            }

            next();
        }

        TreeMap<String, AccountsAndCardModel> accountMap = new TreeMap<>();

        for (AccountsAndCardModel model : allCardsInfo) {
            accountMap.put(model.getAccountNumber(), model);
        }
        return accountMap;
    }




}




        



