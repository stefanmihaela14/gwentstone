package main.cardsCommands;

import fileio.CardInput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Minion extends MyCard {

    public void MinionRules() {

    }

    protected int health = 0;

    protected int damage = 0;

    protected int AttackedOnceThisRound = 0; // after it attacks becomes 1

    protected int hasUsedSpecialAbility = 0;

    /**
     *
     * @param card ?? an instance of the minion card
     */
    public Minion(CardInput card) {
        super(card);
        this.health = card.getHealth();
        this.damage = card.getAttackDamage();
    }

    /**
     *
     * @return true if the card is type of Environment card and false if not
     */
    @Override
    public boolean isEnvironmentCard() {
        return false;
    }

    /**
     * does not do anything here because not all minions have abilities
     * @param attackedCard the attacked card
     * @param attackedX the row where the attacked card is
     * @return he number which corresponds to the output error
     */
    public int useAbility(Minion attackedCard, int attackedX) {
        return 0;
    }

    //getter and setters
    /**
     *
     * @param damage the minion's damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     *
     * @return the minion's damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     *
     * @param health the minion's health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     *
     * @return get the minion's health
     */
    public int getHealth() {
        return health;
    }

    /**
     *
     * @return  1 if it has attacked that round and 0 otherwise
     */
    public int getAttackedOnceThisRound() {
        return AttackedOnceThisRound;
    }

    /**
     *
     * @param AttackedOnceThisRound we set the value to know
     * if the hero has attacked this round or not
     */
    public void setAttackedOnceThisRound(int AttackedOnceThisRound) {
        this.AttackedOnceThisRound = AttackedOnceThisRound;
    }
}
