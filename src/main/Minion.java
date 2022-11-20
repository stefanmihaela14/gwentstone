package main;

import fileio.CardInput;

public class Minion extends MyCard{

    protected int health = 0;

    public Minion(CardInput card) {
        super(card);
        this.health = card.getHealth();
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


    public int getAttacked_once_this_round() {
        return attacked_once_this_round;
    }

    public void setAttacked_once_this_round(int attacked_once_this_round) {
        this.attacked_once_this_round = attacked_once_this_round;
    }

}
