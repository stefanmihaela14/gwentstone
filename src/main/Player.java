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
     * @param Mana the amount of mana to be decreased
     */
    public void deleteMana(final int Mana) {
        mana -= Mana;
    }

    /**
     * increase the number of games won by one every time is called
     */
    public void incrementGamesWon() {
        gamesWon = gamesWon + 1;
    }

//    getters and setters

    /**
     * @return tha current player's mana
     */
    public int getMana() {
        return mana;
    }

    /**
     * @param mana change the player's current mana to a new value
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * @return the player's number of won games
     */
    public int getGamesWon() {
        return gamesWon;
    }

    /**
     * @param gamesWon change the player's current number of games won to a new value
     */
    public void setGamesWon(final int gamesWon) {
        this.gamesWon = gamesWon;
    }
}
