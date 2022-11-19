package main;

import fileio.CardInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Table {

    private ArrayList<MyCard> myDeckPlayer1 = new ArrayList<>();

    private ArrayList<MyCard> myDeckPlayer2 = new ArrayList<>();


    public ArrayList<MyCard> getMyDeckPlayer1() {
        return myDeckPlayer1;
    }

    public void setMyDeckPlayer1(ArrayList<MyCard> myDeck) {
        this.myDeckPlayer1 = myDeck;
    }

    private Hero hero_1;

    private Hero hero_2;

    private ArrayList<MyCard> handPlayer1 = new ArrayList<>();

    private ArrayList<MyCard> handPlayer2 = new ArrayList<>();

    private ArrayList<MyCard>[] vectorRows = new ArrayList[4];


    public Table() {
        for(int i = 0; i < Gameplay.getInstance().getPlayer1_no_cards_in_deck(); i++) {
            CardInput new_card_info = Gameplay.getInstance().getPlayer1_current_deck().get(i);
            MyCard new_card = null;
            if(new_card_info.getName().equals("Sentinel") ||
                    new_card_info.getName().equals("Berserker") ||
                    new_card_info.getName().equals("Goliath") ||
                    new_card_info.getName().equals("Warden")) {
                new_card = new Minion(new_card_info);
            }
            if(new_card_info.getName().equals("The Ripper") ||
                    new_card_info.getName().equals("Miraj") ||
                    new_card_info.getName().equals("The Cursed One") ||
                    new_card_info.getName().equals("Disciple")) {
                new_card = new SpecialAbilityMinion(new_card_info);
            }
            if(new_card_info.getName().equals("Firestorm") ||
                    new_card_info.getName().equals("Winterfell") ||
                    new_card_info.getName().equals("Heart Hound")) {
                new_card = new Environment(new_card_info);
            }
            myDeckPlayer1.add(new_card);
        }
        for(int i = 0; i < Gameplay.getInstance().getPlayer2_no_cards_in_deck(); i++) {
            CardInput new_card_info = Gameplay.getInstance().getPlayer2_current_deck().get(i);
            MyCard new_card = null;
            if (new_card_info.getName().equals("Sentinel") ||
                    new_card_info.getName().equals("Berserker") ||
                    new_card_info.getName().equals("Goliath") ||
                    new_card_info.getName().equals("Warden")) {
                new_card = new Minion(new_card_info);
            }
            if (new_card_info.getName().equals("The Ripper") ||
                    new_card_info.getName().equals("Miraj") ||
                    new_card_info.getName().equals("The Cursed One") ||
                    new_card_info.getName().equals("Disciple")) {
                new_card = new SpecialAbilityMinion(new_card_info);
            }
            if (new_card_info.getName().equals("Firestorm") ||
                    new_card_info.getName().equals("Winterfell") ||
                    new_card_info.getName().equals("Heart Hound")) {
                new_card = new Environment(new_card_info);
            }
            myDeckPlayer2.add(new_card);
        }

        for(int i = 0; i<vectorRows.length; i++) {
            vectorRows[i] = new ArrayList<MyCard>();
        }
    }

    public void putCardInHand(int whichPlayer) {
        if (whichPlayer == 1 && myDeckPlayer1.size() != 0) {
            MyCard deletedCard1 = myDeckPlayer1.remove(0);
            handPlayer1.add(deletedCard1);
        } else if (whichPlayer == 2 && myDeckPlayer2.size() != 0){
            MyCard deletedCard2 = myDeckPlayer2.remove(0);
            handPlayer2.add(deletedCard2);
        }
    }

    public int putCardOnTable(int indexCard, int whichPlayer) {
        if (whichPlayer == 1) {
            if (!handPlayer1.isEmpty() && indexCard + 1 <= handPlayer1.size()) {
                MyCard temporaryCard1 = handPlayer1.get(indexCard);
                if (temporaryCard1.getCard().getName().equals("The Ripper") ||
                        temporaryCard1.getCard().getName().equals("Miraj") ||
                        temporaryCard1.getCard().getName().equals("Goliath") ||
                        temporaryCard1.getCard().getName().equals("Warden")) {
                    if (Gameplay.getInstance().getPlayer1().getMana() < temporaryCard1.getCard().getMana()) {
                        return 2;
                    }

                    if (vectorRows[2].size() < 5) {
                        Gameplay.getInstance().getPlayer1().depleteMana(temporaryCard1.getCard().getMana());
                        vectorRows[2].add(temporaryCard1);
                        handPlayer1.remove(indexCard);

                    }
                    else{
                        return 3;
                    }
                }
                else if (temporaryCard1.getCard().getName().equals("Sentinel") ||
                        temporaryCard1.getCard().getName().equals("Berserker") ||
                        temporaryCard1.getCard().getName().equals("The Cursed One") ||
                        temporaryCard1.getCard().getName().equals("Disciple")) {
                    if (Gameplay.getInstance().getPlayer1().getMana() < temporaryCard1.getCard().getMana()) {
                        return 2;
                    }

                    if (vectorRows[3].size() < 5) {
                        Gameplay.getInstance().getPlayer1().depleteMana(temporaryCard1.getCard().getMana());
                        vectorRows[3].add(temporaryCard1);
                        handPlayer1.remove(indexCard);

                    }
                    else{
                        return 3;
                    }
                }
                else {
                    return 1;
                }
            }
        } else {
            if (!handPlayer2.isEmpty() && indexCard + 1 <= handPlayer2.size())
                {
                    MyCard temporaryCard2 = handPlayer2.get(indexCard);

                    if (temporaryCard2.getCard().getName().equals("The Ripper") ||
                            temporaryCard2.getCard().getName().equals("Miraj") ||
                            temporaryCard2.getCard().getName().equals("Goliath") ||
                            temporaryCard2.getCard().getName().equals("Warden")) {

                        if (Gameplay.getInstance().getPlayer2().getMana() < temporaryCard2.getCard().getMana()) {
                            return 2;
                        }

                        if (vectorRows[1].size() < 5) {
                            vectorRows[1].add(temporaryCard2);
                            Gameplay.getInstance().getPlayer2().depleteMana(temporaryCard2.getCard().getMana());
                            handPlayer2.remove(indexCard);
                        }
                        else{
                            return 3;
                        }
                    }
                    else if (temporaryCard2.getCard().getName().equals("Sentinel") ||
                            temporaryCard2.getCard().getName().equals("Berserker") ||
                            temporaryCard2.getCard().getName().equals("The Cursed One") ||
                            temporaryCard2.getCard().getName().equals("Disciple")) {
                        if (Gameplay.getInstance().getPlayer2().getMana() < temporaryCard2.getCard().getMana()) {
                            return 2;
                        }

                        if (vectorRows[0].size() < 5) {
                            vectorRows[0].add(temporaryCard2);
                            Gameplay.getInstance().getPlayer2().depleteMana(temporaryCard2.getCard().getMana());
                            handPlayer2.remove(indexCard);

                        }
                        else{
                            return 3;
                        }
                    }
                    else {
                        return 1;
                    }
                }
            }
        return 0;
    }
//    Getters and Setters:
    public Hero getHero_1() {
        return hero_1;
    }

    public void setHero_1(Hero hero_1) {
        this.hero_1 = hero_1;
    }

    public Hero getHero_2() {
        return hero_2;
    }

    public void setHero_2(Hero hero_2) {
        this.hero_2 = hero_2;
    }

    public ArrayList<MyCard> getMyDeckPlayer2() {
        return myDeckPlayer2;
    }

    public void setMyDeckPlayer2(ArrayList<MyCard> myDeck) {
        this.myDeckPlayer2 = myDeck;
    }

    public ArrayList<MyCard> getHandPlayer1() {
        return handPlayer1;
    }

    public void setHandPlayer1(ArrayList<MyCard> handPlayer1) {
        this.handPlayer1 = handPlayer1;
    }

    public ArrayList<MyCard> getHandPlayer2() {
        return handPlayer2;
    }

    public void setHandPlayer2(ArrayList<MyCard> hand) {
        handPlayer2 = hand;
    }

    public ArrayList<MyCard>[] getVectorRows() {
        return vectorRows;
    }

    public void setVectorRows(ArrayList<MyCard>[] vectorRows) {
        this.vectorRows = vectorRows;
    }
}
