package main;

import fileio.ActionsInput;
import fileio.CardInput;

public abstract class MyCard {
    protected CardInput card;

    protected int is_frozen = 0; // not frozen on 0, frozen on 1

    public int getIs_frozen() {
        return is_frozen;
    }

    public void setIs_frozen(int is_frozen) {
        this.is_frozen = is_frozen;
    }

    public MyCard(CardInput card){
        this.card = card;
    }

    public abstract boolean isEnvironmentCard();

    public CardInput getCard() {
        return card;
    }
}
