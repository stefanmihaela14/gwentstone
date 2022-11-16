package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Table {
    public Table(){

    }

    private ArrayList<MyCard> myDeckPlayer1;

    private ArrayList<MyCard> myDeckPlayer2;


    public ArrayList<MyCard> getMyDeck() {
        return myDeckPlayer1;
    }

    public void setMyDeck(ArrayList<MyCard> myDeck) {
        this.myDeckPlayer1 = myDeck;
    }

    private MyCard hero_1;

    private MyCard hero_2;


    public void makeTable(){
        for(int i = 0; i < Gameplay.getPlayer1_no_cards_in_deck(); i++){
            CardInput new_card_info = Gameplay.getPlayer1_current_deck().get(i);
            MyCard new_card;
            if(new_card_info.getName().equals("Sentinel") ||
                    new_card_info.getName().equals("Berserker") ||
                    new_card_info.getName().equals("Goliath") ||
                    new_card_info.getName().equals("Warden") ||) {
                new_card = new Minion(new_card_info);
            }
            if(new_card_info.getName().equals("The Ripper")){
                new_card = new SpecialAbilityMinion(new_card_info);
            }
            myDeckPlayer1.add(new_card);
        }
        for(int i = 0; i < Gameplay.getPlayer2_no_cards_in_deck(); i++){
            CardInput new_card_info = Gameplay.getPlayer2_current_deck().get(i);
            MyCard new_card;
            if(new_card_info.getName().equals("The Ripper")){
                new_card = new SpecialAbilityMinion(new_card_info);
            }
            myDeckPlayer2.add(new_card);
        }
    }


//    Getters and Setters:
    public MyCard getHero_1() {
        return hero_1;
    }

    public void setHero_1(MyCard hero_1) {
        this.hero_1 = hero_1;
    }

    public MyCard getHero_2() {
        return hero_2;
    }

    public void setHero_2(MyCard hero_2) {
        this.hero_2 = hero_2;
    }

    public ArrayList<MyCard> getMyDeckPlayer2() {
        return myDeckPlayer2;
    }

    public void setMyDeckPlayer2(ArrayList<MyCard> myDeck) {
        this.myDeckPlayer2 = myDeck;
    }
}
