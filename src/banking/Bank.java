package banking;

import java.util.LinkedList;
import java.util.List;

public class Bank {
    List<Card> cards = new LinkedList<>();

    Card createAccount() {
        Card c = new Card();
        cards.add(c);
        return c;
    }

    public Card findByAccount(String cardNumber) {
        for (Card card: cards) {
            if (card.getNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }
}
