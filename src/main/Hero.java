package main;

import fileio.CardInput;

public class Hero extends MyCard{
    public Hero(CardInput card) {
        super(card);
    }

    @Override
    public boolean isEnvironmentCard() {
        return false;
    }

    private int health = 30;



    public void which_hero() {
        if(card.getName().equals("Lord Royce")) {

        }
        if(card.getName().equals("Empress Thorina")) {

        }
        if(card.getName().equals("King Mudface")) {

        }
        if(card.getName().equals("General Kocioraw")) {

        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
