package main;

import fileio.CardInput;

public class Minion extends MyCard{

    public Minion(CardInput card) {
        super(card);
    }

    protected int is_frozen = 0; // not frozen on 0, frozen on 1

    protected int attacked_once_this_round = 0; // after it attacks becomes 1
    public void MinionRules(){

    }

    public int getAttacked_once_this_round() {
        return attacked_once_this_round;
    }

    public void setAttacked_once_this_round(int attacked_once_this_round) {
        this.attacked_once_this_round = attacked_once_this_round;
    }

    public int getIs_frozen() {
        return is_frozen;
    }

    public void setIs_frozen(int is_frozen) {
        this.is_frozen = is_frozen;
    }
}
