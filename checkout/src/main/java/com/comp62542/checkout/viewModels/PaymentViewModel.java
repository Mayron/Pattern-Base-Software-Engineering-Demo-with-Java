package com.comp62542.checkout.viewModels;

import java.util.List;

public class PaymentViewModel {

    public static class Card {
        public String nameOnCard;
        public String cardNumber;
        public String expiry;
        public String id;

        public String getNameOnCard() {
            return nameOnCard;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public String getExpiry() {
            return expiry;
        }

        public String getId() {
            return id;
        }
    }

    public List<Card> cards;
    public boolean addCard;
    public String paypalId;
    public String googlePayId;

    public boolean getAddCard() {
        return addCard;
    }

    public List<Card> getCards() {
        return cards;
    }

    public String getPaypalId() {
        return paypalId;
    }

    public String getGooglePayId() {
        return googlePayId;
    }

}