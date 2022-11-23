package main.cardsCommands;

import fileio.CardInput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MyCard {
    protected CardInput card;

    protected int isFrozen = 0;

    /**
     * @return 1 if the card is frozen and 0 otherwise
     */
    public int getIsFrozen() {
        return isFrozen;
    }

    /**
     * @param isFrozen 1 if frozen and 0 otherwise
     */
    public void setIsFrozen(final int isFrozen) {
        this.isFrozen = isFrozen;
    }

    /**
     * @param card The card's input data.
     */
    public MyCard(final CardInput card) {
        this.card = card;
    }

    /**
     * @return true if the card is type of Environment card and false if not
     */
    public abstract boolean isEnvironmentCard();

    /**
     * @return Gets the card's input data.
     */
    public CardInput getCard() {
        return card;
    }
}
