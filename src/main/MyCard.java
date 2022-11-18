package main;

import fileio.CardInput;

public abstract class MyCard {
    protected CardInput card;

    public MyCard(CardInput card){
        this.card = card;
    }

    public CardInput getCard() {
        return card;
    }
}
