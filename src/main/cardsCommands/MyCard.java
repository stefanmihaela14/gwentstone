package main.cardsCommands;

import fileio.ActionsInput;
import fileio.CardInput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MyCard {
    protected CardInput card;

    protected int isFrozen = 0;

    /**
     *
     * @return 1 if the card is frozen and 0 otherwise
     */
    public int getIsFrozen() {
        return isFrozen;
    }

    /**
     *
     * @param isFrozen we set the card's frozen propriety to 1 if it's frozen or 0 otherwise
     */
    public void setIsFrozen(int isFrozen) {
        this.isFrozen = isFrozen;
    }

    /**
     *
     * @param card ???
     */
    public MyCard(CardInput card) {
        this.card = card;
    }

    /**
     *
     * @return true if the card is type of Environment card and false if not
     */
    public abstract boolean isEnvironmentCard();

    /**
     *
     * @return ??
     */
    public CardInput getCard() {
        return card;
    }
}
