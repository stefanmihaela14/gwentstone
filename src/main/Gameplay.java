package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Gameplay {
    private Player player1;
    private Player player2;
    private Input input_data;
    private ArrayNode output_data;

    private static Gameplay instance;

    public static Gameplay getInstance() {
        if(instance == null) {
            Gameplay.instance = new Gameplay();
        }
        return instance;
    }

    private Gameplay() {

    }

    private int number_of_games;
    private int player1_deck_index;
    private int player2_deck_index;

    private int player1_no_cards_in_deck;

    private int player2_no_cards_in_deck;

    private ArrayList<CardInput> player1_current_deck;

    private ArrayList<CardInput> player2_current_deck;

    private Table table;

// Getters and Setters


    public Table getTable() {
        return table;
    }

    public int getNumber() {
        return number_of_games;
    }

    public void setNumber(int number_of_games) {
        this.number_of_games =number_of_games;
    }

    public int getPlayer1_deck_index() {
        return player1_deck_index;
    }

    public void setPlayer1_deck_index(int player1_deck_index) {
        this.player1_deck_index = player1_deck_index;
    }

    public int getPlayer2_deck_index() {
        return player2_deck_index;
    }

    public void setPlayer2_deck_index(int player2_deck_index) {
        this.player2_deck_index = player2_deck_index;
    }

    public ArrayList<CardInput> getPlayer1_current_deck() {
        return player1_current_deck;
    }

    public void setPlayer1_current_deck(ArrayList<CardInput> player1_current_deck) {
        this.player1_current_deck = player1_current_deck;
    }

    public int getPlayer1_no_cards_in_deck() {
        return player1_no_cards_in_deck;
    }

    public void setPlayer1_no_cards_in_deck(int player1_no_cards_in_deck) {
        this.player1_no_cards_in_deck = player1_no_cards_in_deck;
    }

    public ArrayList<CardInput> getPlayer2_current_deck() {
        return player2_current_deck;
    }

    public void setPlayer2_current_deck(ArrayList<CardInput> player2_current_deck) {
        this.player2_current_deck = player2_current_deck;
    }

    public int getPlayer2_no_cards_in_deck() {
        return player2_no_cards_in_deck;
    }

    public void setPlayer2_no_cards_in_deck(int player2_no_cards_in_deck) {
        this.player2_no_cards_in_deck = player2_no_cards_in_deck;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    private int playerTurn;

    private int twoTurnsDone = 0;

    private int round_no = 0;

    private int handIndex;

    public void gameRules(Input input_data, ArrayNode output_data) {
        this.input_data = input_data;
        this.output_data = output_data;

        number_of_games = input_data.getGames().size();
        for(int i = 0; i < number_of_games; i++) {
            twoTurnsDone = 0;
            round_no = 0;
            player1 = new Player();
            player2 = new Player();

            player1_deck_index = input_data.getGames().get(i).getStartGame().getPlayerOneDeckIdx();
            player2_deck_index = input_data.getGames().get(i).getStartGame().getPlayerTwoDeckIdx();

            player1_current_deck = input_data.getPlayerOneDecks().getDecks().get(player1_deck_index);
            player2_current_deck = input_data.getPlayerTwoDecks().getDecks().get(player2_deck_index);

            player1_no_cards_in_deck = player1_current_deck.size();
            player2_no_cards_in_deck = player2_current_deck.size();

            this.table = new Table();


            CardInput hero1 = input_data.getGames().get(i).getStartGame().getPlayerOneHero();

            table.setHero_1(new Hero(hero1));

            Hero hero2 = new Hero(input_data.getGames().get(i).getStartGame().getPlayerTwoHero());

            table.setHero_2(hero2);

            playerTurn = input_data.getGames().get(i).getStartGame().getStartingPlayer();


            // shuffle the decks
            int randSeed = input_data.getGames().get(i).getStartGame().getShuffleSeed();
            Collections.shuffle(table.getMyDeckPlayer1(), new Random(randSeed));
            Collections.shuffle(table.getMyDeckPlayer2(), new Random(randSeed));

            table.putCardInHand(1);
            table.putCardInHand(2);

            for(int j = 0; j < input_data.getGames().get(i).getActions().size(); j++) {
                // mana logic
                if(twoTurnsDone == 2) {
                    System.out.println("End of round.");
                    round_no++;
                    twoTurnsDone = 0;
                    int valueToIncreaseMana = round_no + 1;
                    if(valueToIncreaseMana > 10) {
                        valueToIncreaseMana = 10;
                    }
                    int player1_mana = player1.getMana();
                    int player2_mana = player2.getMana();

                    player1.setMana(player1_mana + valueToIncreaseMana);
//                    System.out.println(player1_mana);
                    player2.setMana(player2_mana + valueToIncreaseMana);

                    table.putCardInHand(1);
                    table.putCardInHand(2);

                }

                handIndex = input_data.getGames().get(i).getActions().get(j).getHandIdx();

                doActions(input_data.getGames().get(i).getActions().get(j));

                deleteDeadCards();
            }
        }
    }

    public void doActions(ActionsInput actionsInput) {
        if(actionsInput.getCommand().equals("getPlayerDeck")) {
            ObjectNode newNode = output_data.addObject();
            int whichPlayer = actionsInput.getPlayerIdx();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("playerIdx", actionsInput.getPlayerIdx());
            ArrayNode outputArray = newNode.putArray("output");
            ArrayList<MyCard> deck;
            if(whichPlayer == 1) {
                deck = table.getMyDeckPlayer1();
            } else {
                deck = table.getMyDeckPlayer2();
            }
            for(int i = 0; i < deck.size(); i++) {
                CardInput tempCard = deck.get(i).getCard();
                ObjectNode cardNode = outputArray.addObject();
                cardNode.put("mana", tempCard.getMana());
                if(!(deck.get(i) instanceof Environment)) {
                    cardNode.put("attackDamage", tempCard.getAttackDamage());
                    cardNode.put("health", tempCard.getHealth());
                }
                cardNode.put("description", tempCard.getDescription());
                ArrayNode colorsArray = cardNode.putArray("colors");
                for(int j = 0; j < tempCard.getColors().size(); j++) {
                    colorsArray.add(tempCard.getColors().get(j));
                }
                cardNode.put("name", tempCard.getName());
            }
        }
        if(actionsInput.getCommand().equals("getPlayerHero")) {
            ObjectNode newNode = output_data.addObject();
            int whichPlayer = actionsInput.getPlayerIdx();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("playerIdx", actionsInput.getPlayerIdx());
            ObjectNode heroNode = newNode.putObject("output");
            CardInput hero;
            if(whichPlayer == 1) {
                hero = table.getHero_1().getCard();
            } else {
                hero = table.getHero_2().getCard();
            }
            heroNode.put("mana", hero.getMana());
            heroNode.put("description", hero.getDescription());
            ArrayNode colorsArray = heroNode.putArray("colors");
            for(int j = 0; j < hero.getColors().size(); j++) {
                colorsArray.add(hero.getColors().get(j));
            }
            heroNode.put("name", hero.getName());
            Hero tempHero;
            if (whichPlayer == 1) {
                tempHero = table.getHero_1();
            } else {
                tempHero = table.getHero_2();
            }
            heroNode.put("health", tempHero.getHealth());
        }
        if(actionsInput.getCommand().equals("endPlayerTurn")) {
            System.out.println("End turn!");
            if (playerTurn == 1) {
                playerTurn = 2;
            } else {
                playerTurn = 1;
            }
            twoTurnsDone++;
        }
        if(actionsInput.getCommand().equals("getPlayerTurn")) {
            ObjectNode newNode = output_data.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("output", playerTurn);
        }
        if(actionsInput.getCommand().equals("getCardsInHand")) {
            ObjectNode newNode = output_data.addObject();
            int whichPlayer = actionsInput.getPlayerIdx();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("playerIdx", actionsInput.getPlayerIdx());
            ArrayNode outputArray = newNode.putArray("output");
            ArrayList<MyCard> hand;
            if(whichPlayer == 1) {
                hand = table.getHandPlayer1();
            } else {
                hand = table.getHandPlayer2();
            }
            for(int i = 0; i < hand.size(); i++){
                CardInput tempHandCard = hand.get(i).getCard();
                ObjectNode cardNode = outputArray.addObject();
                cardNode.put("mana", tempHandCard.getMana());
                if(!(hand.get(i) instanceof Environment)) {
                    cardNode.put("attackDamage", tempHandCard.getAttackDamage());
                    cardNode.put("health", tempHandCard.getHealth());
                }
                cardNode.put("description", tempHandCard.getDescription());
                ArrayNode colorsArray = cardNode.putArray("colors");
                for(int j = 0; j < tempHandCard.getColors().size(); j++) {
                    colorsArray.add(tempHandCard.getColors().get(j));
                }
                cardNode.put("name", tempHandCard.getName());
            }
        }
        if(actionsInput.getCommand().equals("getPlayerMana")) {
            ObjectNode newNode = output_data.addObject();
            int whichPlayer = actionsInput.getPlayerIdx();
            Player player = null;
            if(whichPlayer == 1){
                player = player1;
            }
            else{
                player = player2;
            }
            newNode.put("command", actionsInput.getCommand());
            newNode.put("playerIdx", actionsInput.getPlayerIdx());
            newNode.put("output", player.getMana());
        }
        if(actionsInput.getCommand().equals("placeCard")) {
            int output = table.putCardOnTable(handIndex, playerTurn);
            if(output == 1){
                ObjectNode newNode = output_data.addObject();
                newNode.put("command", actionsInput.getCommand());
                newNode.put("handIdx", actionsInput.getHandIdx());
                newNode.put("error", "Cannot place environment card on table.");
            }
            else if(output == 2){
                ObjectNode newNode = output_data.addObject();
                newNode.put("command", actionsInput.getCommand());
                newNode.put("handIdx", actionsInput.getHandIdx());
                newNode.put("error", "Not enough mana to place card on table.");
            }
            else if(output == 3){
                ObjectNode newNode = output_data.addObject();
                newNode.put("command", actionsInput.getCommand());
                newNode.put("handIdx", actionsInput.getHandIdx());
                newNode.put("error", "Cannot place card on table since row is full.");
            }
        }
        if(actionsInput.getCommand().equals("getCardsOnTable")) {
            ObjectNode newNode = output_data.addObject();
            newNode.put("command", actionsInput.getCommand());
            ArrayNode outputArray = newNode.putArray("output");
            for (int i = 0; i < table.getVectorRows().length; i++){
                ArrayNode lineArray = outputArray.addArray();
                for(int j = 0; j < table.getVectorRows()[i].size(); j++) {
                    CardInput tempCard = table.getVectorRows()[i].get(j).getCard();
                    ObjectNode cardNode = lineArray.addObject();
                    cardNode.put("mana", tempCard.getMana());
                    cardNode.put("attackDamage", tempCard.getAttackDamage());
                    cardNode.put("health", table.getVectorRows()[i].get(j).getHealth());

                    cardNode.put("description", tempCard.getDescription());
                    ArrayNode colorsArray = cardNode.putArray("colors");
                    for(int k = 0; k < tempCard.getColors().size(); k++) {
                        colorsArray.add(tempCard.getColors().get(k));
                    }
                    cardNode.put("name", tempCard.getName());
                }
            }
        }
        if(actionsInput.getCommand().equals("getEnvironmentCardsInHand")){ //this should work
            ObjectNode newNode = output_data.addObject();
            int whichPlayer = actionsInput.getPlayerIdx();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("playerIdx", actionsInput.getPlayerIdx());
            ArrayNode outputArray = newNode.putArray("output");
            ArrayList<MyCard> hand;
            if(whichPlayer == 1) {
                hand = table.getHandPlayer1();
            } else {
                hand = table.getHandPlayer2();
            }
            for(int i = 0; i < hand.size(); i++){
                CardInput tempHandCard = hand.get(i).getCard();
                if(hand.get(i) instanceof Environment) {
                    ObjectNode cardNode = outputArray.addObject();
                    cardNode.put("mana", tempHandCard.getMana());
                    cardNode.put("description", tempHandCard.getDescription());
                    ArrayNode colorsArray = cardNode.putArray("colors");
                    for (int j = 0; j < tempHandCard.getColors().size(); j++) {
                        colorsArray.add(tempHandCard.getColors().get(j));
                    }
                    cardNode.put("name", tempHandCard.getName());
                }
            }
        }
        if(actionsInput.getCommand().equals("getCardAtPosition")){
            int x = actionsInput.getX();
            int y = actionsInput.getY();
            if(y + 1 > table.getVectorRows()[x].size()){
                return;
            }
            ObjectNode newNode = output_data.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("x", x);
            newNode.put("y", y);
            ObjectNode cardNode = newNode.putObject("output");
            CardInput tempCard = table.getVectorRows()[x].get(y).getCard();
            cardNode.put("mana", tempCard.getMana());
            cardNode.put("attackDamage", tempCard.getAttackDamage());
            cardNode.put("health", tempCard.getHealth());
            cardNode.put("description", tempCard.getDescription());
            ArrayNode colorsArray = cardNode.putArray("colors");
            for(int k = 0; k < tempCard.getColors().size(); k++) {
                colorsArray.add(tempCard.getColors().get(k));
            }
            cardNode.put("name", tempCard.getName());
        }
        if(actionsInput.getCommand().equals("useEnvironmentCard")){
            int handIndex = actionsInput.getHandIdx();
            int affectedRow = actionsInput.getAffectedRow();
            int whichPlayer = playerTurn;
            int error_no = table.useEnvironmentCard(handIndex, affectedRow, whichPlayer);
            System.out.println(error_no);
        }
    }

    private void deleteDeadCards(){
        for(int i = 0; i <4; i++){
            ArrayList<Minion> to_delete = new ArrayList<>();
            for(int j = 0; j<table.getVectorRows()[i].size(); j++){
                if(table.getVectorRows()[i].get(j).getHealth() <= 0){
                    to_delete.add(table.getVectorRows()[i].get(j));
                }
            }
            table.getVectorRows()[i].removeAll(to_delete);
        }
    }

    public Input getInput_data() {
        return input_data;
    }

    public void setInput_data(Input input_data) {
        this.input_data = input_data;
    }
}
