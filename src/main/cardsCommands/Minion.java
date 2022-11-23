package main.cardsCommands;

import fileio.CardInput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Minion extends MyCard {

    protected int health = 0;

    protected int damage = 0;

    protected int AttackedOnceThisRound = 0;

    protected int hasUsedSpecialAbility = 0;

    /**
     * @param card The card's input data.
     */
    public Minion(final CardInput card) {
        super(card);
        this.health = card.getHealth();
        this.damage = card.getAttackDamage();
    }

    /**
     * @return true if the card is type of Environment card and false if not
     */
    @Override
    public boolean isEnvironmentCard() {
        return false;
    }

    /**
     * does not do anything here because not all minions have abilities
     * @param attackedX the row where the attacked card is
     * @return the number which corresponds to the output error
     */
    public int useAbility(final Minion attackedCard, final int attackedX) {
        return 0;
    }

    //getter and setters
    /**
     *
     */
    public void setDamage(final int damage) {
        this.damage = damage;
    }

    /**
     * @return the minion's damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @param health the minion's health
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * @return get the minion's health
     */
    public int getHealth() {
        return health;
    }

    /**
     * @return  1 if it has attacked that round and 0 otherwise
     */
    public int getAttackedOnceThisRound() {
        return AttackedOnceThisRound;
    }

    /**
     * @param hasAttackedOnceThisRound we set the value to know
     * if the hero has attacked this round or not
     */
    public void setAttackedOnceThisRound(final int hasAttackedOnceThisRound) {
        this.AttackedOnceThisRound = hasAttackedOnceThisRound;
    }
}
