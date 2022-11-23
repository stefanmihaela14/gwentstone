package main;

import fileio.CardInput;
import lombok.Getter;
import lombok.Setter;
import main.cardsCommands.Environment;
import main.cardsCommands.MyCard;
import main.cardsCommands.Hero;
import main.cardsCommands.Minion;
import main.cardsCommands.SpecialAbilityMinion;

import java.util.ArrayList;

@Getter
@Setter
public class Table {

    public static final int NO_3 = 3;

    public static final int NO_4 = 4;

    public static final int NO_5 = 5;

    private ArrayList<MyCard> myDeckPlayer1 = new ArrayList<>();

    private ArrayList<MyCard> myDeckPlayer2 = new ArrayList<>();

    private Hero hero1;

    private Hero hero2;

    private ArrayList<MyCard> handPlayer1 = new ArrayList<>();

    private ArrayList<MyCard> handPlayer2 = new ArrayList<>();

    private ArrayList<Minion>[] vectorRows = new ArrayList[NO_4];

    /**
     * The table consists of an array of ArrayLists where the cards will be put.
     * Verify each card's type and place it in the player's deck, not to
     * modify the input deck.
     */
    public Table() {
        for (int i = 0; i < Gameplay.getInstance().getplayer1NoCardsInDeck(); i++) {
            CardInput newCardInfo = Gameplay.getInstance().getplayer1CurrentDeck().get(i);
            MyCard newCard = null;
            if (newCardInfo.getName().equals("Sentinel")
                    || newCardInfo.getName().equals("Berserker")
                    || newCardInfo.getName().equals("Goliath")
                    || newCardInfo.getName().equals("Warden")) {
                newCard = new Minion(newCardInfo);
            }
            if (newCardInfo.getName().equals("The Ripper")
                    || newCardInfo.getName().equals("Miraj")
                    || newCardInfo.getName().equals("The Cursed One")
                    || newCardInfo.getName().equals("Disciple")) {
                newCard = new SpecialAbilityMinion(newCardInfo);
            }
            if (newCardInfo.getName().equals("Firestorm")
                    || newCardInfo.getName().equals("Winterfell")
                    || newCardInfo.getName().equals("Heart Hound")) {
                newCard = new Environment(newCardInfo);
            }
            myDeckPlayer1.add(newCard);
        }
        for (int i = 0; i < Gameplay.getInstance().getplayer2NoCardsInDeck(); i++) {
            CardInput newCardInfo = Gameplay.getInstance().getplayer2CurrentDeck().get(i);
            MyCard newCard = null;
            if (newCardInfo.getName().equals("Sentinel")
                    || newCardInfo.getName().equals("Berserker")
                    || newCardInfo.getName().equals("Goliath")
                    || newCardInfo.getName().equals("Warden")) {
                newCard = new Minion(newCardInfo);
            }
            if (newCardInfo.getName().equals("The Ripper")
                    || newCardInfo.getName().equals("Miraj")
                    || newCardInfo.getName().equals("The Cursed One")
                    || newCardInfo.getName().equals("Disciple")) {
                newCard = new SpecialAbilityMinion(newCardInfo);
            }
            if (newCardInfo.getName().equals("Firestorm")
                    || newCardInfo.getName().equals("Winterfell")
                    || newCardInfo.getName().equals("Heart Hound")) {
                newCard = new Environment(newCardInfo);
            }
            myDeckPlayer2.add(newCard);
        }

        for (int i = 0; i < vectorRows.length; i++) {
            vectorRows[i] = new ArrayList<Minion>();
        }
    }

    /**
     * Take a card from the deck and put it in an array - the player's hand -
     * @param whichPlayer the player whose turn it is
     */
    public void putCardInHand(final int whichPlayer) {
        if (whichPlayer == 1 && myDeckPlayer1.size() != 0) {
            MyCard deletedCard1 = myDeckPlayer1.remove(0);
            handPlayer1.add(deletedCard1);
        } else if (whichPlayer == 2 && myDeckPlayer2.size() != 0) {
            MyCard deletedCard2 = myDeckPlayer2.remove(0);
            handPlayer2.add(deletedCard2);
        }
    }

    /**
     * Verify each card's type and place it accordingly on the table
     * @return the number which corresponds to the output error
     */
    public int putCardOnTable(final int indexCard, final int whichPlayer) {
        if (whichPlayer == 1) {
            if (!handPlayer1.isEmpty() && indexCard + 1 <= handPlayer1.size()) {
                MyCard temporaryCard1 = handPlayer1.get(indexCard);
                if (!(temporaryCard1 instanceof Minion)) {
                    return 1;
                }
                if (temporaryCard1.getCard().getName().equals("The Ripper")
                        || temporaryCard1.getCard().getName().equals("Miraj")
                        || temporaryCard1.getCard().getName().equals("Goliath")
                        || temporaryCard1.getCard().getName().equals("Warden")) {
                    if (Gameplay.getInstance().getPlayer1().getMana()
                            < temporaryCard1.getCard().getMana()) {
                        return 2;
                    }

                    if (vectorRows[2].size() < NO_5) {
                        Player player1 = Gameplay.getInstance().getPlayer1();
                        player1.deleteMana(temporaryCard1.getCard().getMana());
                        vectorRows[2].add((Minion) temporaryCard1);
                        handPlayer1.remove(indexCard);
                    } else {
                        return NO_3;
                    }
                } else if (temporaryCard1.getCard().getName().equals("Sentinel")
                        || temporaryCard1.getCard().getName().equals("Berserker")
                        || temporaryCard1.getCard().getName().equals("The Cursed One")
                        || temporaryCard1.getCard().getName().equals("Disciple")) {
                    if (Gameplay.getInstance().getPlayer1().getMana()
                            < temporaryCard1.getCard().getMana()) {
                        return 2;
                    }

                    if (vectorRows[NO_3].size() < NO_5) {
                        Player player1 = Gameplay.getInstance().getPlayer1();
                        player1.deleteMana(temporaryCard1.getCard().getMana());
                        vectorRows[NO_3].add((Minion) temporaryCard1);
                        handPlayer1.remove(indexCard);

                    } else {
                        return NO_3;
                    }
                } else {
                    return 1;
                }
            }
        } else {
            if (!handPlayer2.isEmpty() && indexCard + 1 <= handPlayer2.size()) {
                    MyCard temporaryCard2 = handPlayer2.get(indexCard);
                    if (!(temporaryCard2 instanceof Minion)) {
                        return 1;
                    }

                    if (temporaryCard2.getCard().getName().equals("The Ripper")
                            || temporaryCard2.getCard().getName().equals("Miraj")
                            || temporaryCard2.getCard().getName().equals("Goliath")
                            || temporaryCard2.getCard().getName().equals("Warden")) {

                        if (Gameplay.getInstance().getPlayer2().getMana()
                                < temporaryCard2.getCard().getMana()) {
                            return 2;
                        }

                        if (vectorRows[1].size() < NO_5) {
                            vectorRows[1].add((Minion) temporaryCard2);
                            Player player2 = Gameplay.getInstance().getPlayer2();
                            player2.deleteMana(temporaryCard2.getCard().getMana());
                            handPlayer2.remove(indexCard);
                        } else {
                            return NO_3;
                        }
                    } else if (temporaryCard2.getCard().getName().equals("Sentinel")
                            || temporaryCard2.getCard().getName().equals("Berserker")
                            || temporaryCard2.getCard().getName().equals("The Cursed One")
                            || temporaryCard2.getCard().getName().equals("Disciple")) {
                        if (Gameplay.getInstance().getPlayer2().getMana()
                                < temporaryCard2.getCard().getMana()) {
                            return 2;
                        }

                        if (vectorRows[0].size() < NO_5) {
                            vectorRows[0].add((Minion) temporaryCard2);
                            Player player2 = Gameplay.getInstance().getPlayer2();
                            player2.deleteMana(temporaryCard2.getCard().getMana());
                            handPlayer2.remove(indexCard);

                        } else {
                            return NO_3;
                        }
                    } else {
                        return 1;
                    }
                }
            }
        return 0;
    }

    /**
     * Verify the possible errors
     * @param handIndex the index of the card from the player's hand
     * @param affectedRow the row whose cards will be damaged
     * @param whichPlayer current player's turn
     * @return the number which corresponds to the output error
     */
    public int useEnvironmentCard(final int handIndex, final int affectedRow,
                                  final int whichPlayer) {
        // get the opposite row
        int oppositeRow = NO_3 - affectedRow;

        MyCard envCard;
        Player newPlayer;
        ArrayList<MyCard> hand;
        if (whichPlayer == 1) {
            if (handIndex + 1 > handPlayer1.size()) {
                return -1;
            }
            envCard = handPlayer1.get(handIndex);
            newPlayer = Gameplay.getInstance().getPlayer1();
            hand = handPlayer1;
        } else {
            if (handIndex + 1 > handPlayer2.size()) {
                return -1;
            }
            envCard = handPlayer2.get(handIndex);
            newPlayer = Gameplay.getInstance().getPlayer2();
            hand = handPlayer2;
        }
        if (!envCard.isEnvironmentCard()) {
            return 1; // error : Chosen card is not of type environment.
        }
        if ((newPlayer.getMana() < envCard.getCard().getMana())) {
            return 2; // error : Not enough mana to use environment card.
        }
        if (whichPlayer == 1) {
            if (affectedRow == 2 || affectedRow == NO_3) {
                return NO_3; // error : Chosen row does not belong to the enemy.
            }
        } else {
            if (affectedRow == 0 || affectedRow == 1) {
                return NO_3;
            }
        }
        if (envCard.getCard().getName().equals("Heart Hound")) {
            if (!(getVectorRows()[oppositeRow].size() < NO_5)) {
                return NO_4; // error : Cannot steal enemy card since the player's row is full.
            }
        }
        if (envCard instanceof Environment) {
            ((Environment) envCard).useAbility(affectedRow);
        }
        int manaToRemove = envCard.getCard().getMana();
        int playerMana = newPlayer.getMana();
        newPlayer.setMana(playerMana - manaToRemove);
        hand.remove(handIndex);
        return 0;
    }

//    Getters and Setters:

    /**
     * @return player one's hero
     */
    public Hero getHero1() {
        return hero1;
    }

    /**
     * @param hero1 set the hero of player one
     */
    public void setHero1(final Hero hero1) {
        this.hero1 = hero1;
    }

    /**
     * @return player two's hero
     */
    public Hero getHero2() {
        return hero2;
    }

    /**
     * @param hero2 set the hero of player two
     */
    public void setHero2(final Hero hero2) {
        this.hero2 = hero2;
    }

    /**
     * @return the player's deck of cards as an arrayList with elements of type MyCard
     */
    public ArrayList<MyCard> getMyDeckPlayer1() {
        return myDeckPlayer1;
    }

    /**
     * @return the player's deck of cards as an arrayList with elements of type MyCard
     */
    public ArrayList<MyCard> getMyDeckPlayer2() {
        return myDeckPlayer2;
    }

    /**
     * @return the player's cards in hand as an ArrayList with elements of type MyCard
     */
    public ArrayList<MyCard> getHandPlayer1() {
        return handPlayer1;
    }

    /**
     * @return the player's cards in hand as an ArrayList with elements of type MyCard
     */
    public ArrayList<MyCard> getHandPlayer2() {
        return handPlayer2;
    }

    /**
     * @return the arrayList with the table and the cards on it
     */
    public ArrayList<Minion>[] getVectorRows() {
        return vectorRows;
    }

}
