package main;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    public Player() {

    }

    private int mana = 1;

    private int gamesWon = 0;

    /**
     *
     * @param Mana
     */
    public void deleteMana(int Mana) {
        mana -= Mana;
    }

    /**
     *
     */
    public void incrementGamesWon() {
        gamesWon = gamesWon + 1;
    }

//    getters and setters

    /**
     *
     * @return
     */
    public int getMana() {
        return mana;
    }

    /**
     *
     * @param mana
     */
    public void setMana(int mana) {
        this.mana = mana;
    }

    /**
     *
     * @return
     */
    public int getGamesWon() {
        return gamesWon;
    }

    /**
     *
     * @param gamesWon
     */
    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }
}
