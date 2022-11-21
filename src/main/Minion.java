package main;

import fileio.CardInput;

public class Minion extends MyCard{

    protected int health = 0;

    protected int damage = 0;

    public Minion(CardInput card) {
        super(card);
        this.health = card.getHealth();
        this.damage = card.getAttackDamage();
    }

    public int useAbility(Minion attackedCard, int attacked_x){
        return 0;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
    public int getDamage() {
        return damage;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public boolean isEnvironmentCard() {
        return false;
    }

    protected int attacked_once_this_round = 0; // after it attacks becomes 1
    public void MinionRules(){

    }


    protected int hasUsedSpecialAbility = 0;

    public int getHasUsedSpecialAbility() {
        return hasUsedSpecialAbility;
    }

    public void setHasUsedSpecialAbility(int hasUsedSpecialAbility) {
        this.hasUsedSpecialAbility = hasUsedSpecialAbility;
    }


    public int getAttacked_once_this_round() {
        return attacked_once_this_round;
    }

    public void setAttacked_once_this_round(int attacked_once_this_round) {
        this.attacked_once_this_round = attacked_once_this_round;
    }

}
