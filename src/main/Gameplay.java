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
    private Player player1 = new Player();
    private Player player2 = new Player();
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

    public int getPlayerTurn(){
        return playerTurn;
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

    private int gameEnded = 0;

    private int gamesPlayed = 0;

    public void gameRules(Input input_data, ArrayNode output_data) {
        this.input_data = input_data;
        this.output_data = output_data;

        number_of_games = input_data.getGames().size();
        for(int i = 0; i < number_of_games; i++) {
            gameEnded = 0;
            twoTurnsDone = 0;
            round_no = 0;
            player1.setMana(1);
            player2.setMana(1);
            if(i == 0){
                player1.setGamesWon(0);
                player2.setGamesWon(0);
                gamesPlayed = 0;
            }

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
                    round_no++;
                    twoTurnsDone = 0;
                    int valueToIncreaseMana = round_no + 1;
                    if(valueToIncreaseMana > 10) {
                        valueToIncreaseMana = 10;
                    }
                    int player1_mana = player1.getMana();
                    int player2_mana = player2.getMana();

                    player1.setMana(player1_mana + valueToIncreaseMana);
                    player2.setMana(player2_mana + valueToIncreaseMana);

                    table.putCardInHand(1);
                    table.putCardInHand(2);
                }

                handIndex = input_data.getGames().get(i).getActions().get(j).getHandIdx();

                boolean debuggingCommand = false;
                String[] debuggingCommands = {"getPlayerDeck","getPlayerHero","getPlayerTurn",
                        "getCardsInHand","getPlayerMana","getCardsOnTable",
                        "getEnvironmentCardsInHand","getCardAtPosition","getFrozenCardsOnTable",
                        "getTotalGamesPlayed","getPlayerOneWins","getPlayerTwoWins"};
                String command = input_data.getGames().get(i).getActions().get(j).getCommand();
                debuggingCommand = Arrays.stream(debuggingCommands).toList().contains(command);
                if(!(gameEnded == 1 && !debuggingCommand)){
                    doActions(input_data.getGames().get(i).getActions().get(j));
                }
                //verify if any card after each action has health <= 0 and delete it
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
//            System.out.println("End turn!");
            unfrozeCards(playerTurn);
            if (playerTurn == 1) {
                playerTurn = 2;
            } else {
                playerTurn = 1;
            }
            twoTurnsDone++;
            resetWhoAttacked();
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
                    cardNode.put("attackDamage", table.getVectorRows()[i].get(j).getDamage());
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
        if(actionsInput.getCommand().equals("getEnvironmentCardsInHand")) { //this should work
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
        if(actionsInput.getCommand().equals("getCardAtPosition")) {
            int x = actionsInput.getX();
            int y = actionsInput.getY();
            ObjectNode newNode = output_data.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("x", x);
            newNode.put("y", y);
            if(y + 1 > table.getVectorRows()[x].size()){
                newNode.put("output", "No card available at that position.");
                return;
            }


            ObjectNode cardNode = newNode.putObject("output");
            CardInput tempCard = table.getVectorRows()[x].get(y).getCard();
            cardNode.put("mana", tempCard.getMana());
            cardNode.put("attackDamage", table.getVectorRows()[x].get(y).getDamage());
            cardNode.put("health", table.getVectorRows()[x].get(y).getHealth());
            cardNode.put("description", tempCard.getDescription());
            ArrayNode colorsArray = cardNode.putArray("colors");
            for(int k = 0; k < tempCard.getColors().size(); k++) {
                colorsArray.add(tempCard.getColors().get(k));
            }
            cardNode.put("name", tempCard.getName());
        }
        if(actionsInput.getCommand().equals("useEnvironmentCard")) {
            int handIndex = actionsInput.getHandIdx();
            int affectedRow = actionsInput.getAffectedRow();
            int whichPlayer = playerTurn;
            int errorNo = table.useEnvironmentCard(handIndex, affectedRow, whichPlayer);
            if(errorNo == 0) {
                return;
            }
            ObjectNode newNode = output_data.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("handIdx", actionsInput.getHandIdx());
            newNode.put("affectedRow", actionsInput.getAffectedRow());
            String errorMsg = null;
            if(errorNo == 1) {
                errorMsg = "Chosen card is not of type environment.";
            }
            if(errorNo == 2) {
                errorMsg = "Not enough mana to use environment card.";
            }
            if(errorNo == 3) {
                errorMsg = "Chosen row does not belong to the enemy.";
            }
            if(errorNo == 4){
                errorMsg = "Cannot steal enemy card since the player's row is full.";
            }
            newNode.put("error", errorMsg);
        }
        if(actionsInput.getCommand().equals("getFrozenCardsOnTable")) {
            ObjectNode newNode = output_data.addObject();
            newNode.put("command", actionsInput.getCommand());
            ArrayNode outputArray = newNode.putArray("output");
            for (int i = 0; i < table.getVectorRows().length; i++) {
                for(int j = 0; j < table.getVectorRows()[i].size(); j++) {
                    CardInput tempCard = table.getVectorRows()[i].get(j).getCard();
                    if(table.getVectorRows()[i].get(j).getIs_frozen() == 1) {
                        ObjectNode cardNode = outputArray.addObject();
                        cardNode.put("mana", tempCard.getMana());
                        cardNode.put("attackDamage", table.getVectorRows()[i].get(j).getDamage());
                        cardNode.put("health", table.getVectorRows()[i].get(j).getHealth());

                        cardNode.put("description", tempCard.getDescription());
                        ArrayNode colorsArray = cardNode.putArray("colors");
                        for (int k = 0; k < tempCard.getColors().size(); k++) {
                            colorsArray.add(tempCard.getColors().get(k));
                        }
                        cardNode.put("name", tempCard.getName());
                    }
                }
            }
        }
        if(actionsInput.getCommand().equals("cardUsesAttack")) {

            int xAttacker = actionsInput.getCardAttacker().getX();
            int yAttacker = actionsInput.getCardAttacker().getY();
            int xAttacked = actionsInput.getCardAttacked().getX();
            int yAttacked = actionsInput.getCardAttacked().getY();

            Minion attackerMinion = table.getVectorRows()[xAttacker].get(yAttacker);
            Minion attackedMinion = table.getVectorRows()[xAttacked].get(yAttacked);
            int errorMsg = useAttack(attackerMinion, attackedMinion, xAttacked);

            if(errorMsg != 0) {
                ObjectNode newNode = output_data.addObject();
                newNode.put("command", actionsInput.getCommand());
                ObjectNode attackerNode = newNode.putObject("cardAttacker");
                attackerNode.put("x", actionsInput.getCardAttacker().getX());
                attackerNode.put("y", actionsInput.getCardAttacker().getY());
                ObjectNode attackedNode = newNode.putObject("cardAttacked");
                attackedNode.put("x", actionsInput.getCardAttacked().getX());
                attackedNode.put("y", actionsInput.getCardAttacked().getY());
                String errorStr = null;
                if(errorMsg == 1) {
                    errorStr = "Attacked card does not belong to the enemy.";
                }
                if(errorMsg == 2){
                    errorStr = "Attacker card is frozen.";
                }
                if(errorMsg == 4){
                    errorStr = "Attacker card has already attacked this turn.";
                }
                if(errorMsg == 3){
                    errorStr = "Attacked card is not of type 'Tank'.";
                }
                newNode.put("error", errorStr);
            }
        }
        if(actionsInput.getCommand().equals("cardUsesAbility")) {
            int xAttacker = actionsInput.getCardAttacker().getX();
            int yAttacker = actionsInput.getCardAttacker().getY();
            int xAttacked = actionsInput.getCardAttacked().getX();
            int yAttacked = actionsInput.getCardAttacked().getY();

            if(yAttacker + 1 > table.getVectorRows()[xAttacker].size()){
                return;
            }
            if(yAttacked + 1 > table.getVectorRows()[xAttacked].size()){
                return;
            }
            Minion attackerCard = table.getVectorRows()[xAttacker].get(yAttacker);
            Minion attackedCard = table.getVectorRows()[xAttacked].get(yAttacked);

            int errorMsg = attackerCard.useAbility(attackedCard, xAttacked);
            if(errorMsg != 0){
                ObjectNode newNode = output_data.addObject();
                newNode.put("command", actionsInput.getCommand());
                ObjectNode attackerNode = newNode.putObject("cardAttacker");
                attackerNode.put("x", actionsInput.getCardAttacker().getX());
                attackerNode.put("y", actionsInput.getCardAttacker().getY());
                ObjectNode attackedNode = newNode.putObject("cardAttacked");
                attackedNode.put("x", actionsInput.getCardAttacked().getX());
                attackedNode.put("y", actionsInput.getCardAttacked().getY());
                String errorStr = null;
                if(errorMsg == 1) {
                    errorStr = "Attacker card is frozen.";
                }
                if(errorMsg == 2) {
                    errorStr = "Attacker card has already attacked this turn.";
                }
                if(errorMsg == 3) {
                    errorStr = "Attacked card does not belong to the current player.";
                }
                if(errorMsg == 4) {
                    errorStr = "Attacked card does not belong to the enemy.";
                }
                if(errorMsg == 5) {
                    errorStr = "Attacked card is not of type 'Tank'.";
                }
                newNode.put("error", errorStr);
            }
        }
        if (actionsInput.getCommand().equals("useAttackHero")) {

            int xAttacker = actionsInput.getCardAttacker().getX();
            int yAttacker = actionsInput.getCardAttacker().getY();

            Minion attackerCard = table.getVectorRows()[xAttacker].get(yAttacker);

            int errorMsg = useHeroAttack(attackerCard);
            if(errorMsg == 1 || errorMsg ==2 || errorMsg == 3){
                ObjectNode newNode = output_data.addObject();
                newNode.put("command", actionsInput.getCommand());
                ObjectNode attackerNode = newNode.putObject("cardAttacker");
                attackerNode.put("x", actionsInput.getCardAttacker().getX());
                attackerNode.put("y", actionsInput.getCardAttacker().getY());
                String errorStr = null;
                if(errorMsg == 1) {
                    errorStr = "Attacker card is frozen.";
                }
                if(errorMsg == 2) {
                    errorStr = "Attacker card has already attacked this turn.";
                }
                if(errorMsg == 3) {
                    errorStr = "Attacked card is not of type 'Tank'.";
                }
                newNode.put("error", errorStr);
            }
            if(errorMsg == 4 || errorMsg == 5){
                gameEnded = 1;
                ObjectNode newNode = output_data.addObject();
                if(errorMsg == 4) {
                    newNode.put("gameEnded", "Player one killed the enemy hero.");
                    player1.incrementGamesWon();
                    gamesPlayed = gamesPlayed + 1;
                }
                if(errorMsg == 5){
                    newNode.put("gameEnded", "Player two killed the enemy hero.");
                    player2.incrementGamesWon();
                    gamesPlayed = gamesPlayed + 1;
                }
            }
        }
        if(actionsInput.getCommand().equals("useHeroAbility")){
            int attackedRow = actionsInput.getAffectedRow();

            Hero currentHero = null;
            Player currentPlayer = null;
            if(playerTurn == 1){
                currentHero = table.getHero_1();
                currentPlayer = player1;
            } else {
                currentHero = table.getHero_2();
                currentPlayer = player2;
            }
            int errorMsg = currentHero.heroAbility(attackedRow, currentPlayer);
            if(errorMsg != 0){
                ObjectNode newNode = output_data.addObject();
                newNode.put("command", actionsInput.getCommand());
                newNode.put("affectedRow", actionsInput.getAffectedRow());
                String errString = null;
                if(errorMsg == 1) {
                    errString = "Not enough mana to use hero's ability.";
                }
                if(errorMsg == 2) {
                    errString = "Hero has already attacked this turn.";
                }
                if(errorMsg == 3) {
                    errString = "Selected row does not belong to the enemy.";
                }
                if(errorMsg == 4) {
                    errString = "Selected row does not belong to the current player.";
                }
                newNode.put("error", errString);
            }
        }
        if(actionsInput.getCommand().equals("getPlayerOneWins")){
            int whichPlayer = 1;
            int gamesWon = getPlayerWins(whichPlayer);
            ObjectNode newNode = output_data.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("output", gamesWon);
        }
        if(actionsInput.getCommand().equals("getPlayerTwoWins")){
            int whichPlayer = 2;
            int gamesWon = getPlayerWins(whichPlayer);
            ObjectNode newNode = output_data.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("output", gamesWon);
        }
        if(actionsInput.getCommand().equals("getTotalGamesPlayed")){
            ObjectNode newNode = output_data.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("output", gamesPlayed);
        }
    }

    // some auxiliary functions
    private int getPlayerWins(int whichPLayer){
        Player player = null;
        if(whichPLayer == 1){
            player = player1;
        } else {
            player = player2;
        }
        int gamesWon = player.getGamesWon();
        return gamesWon;
    }

    private void resetWhoAttacked(){
        for(int i = 0; i<4; i++){
            for(Minion minion: table.getVectorRows()[i]){
                minion.setAttacked_once_this_round(0);
            }
        }
        table.getHero_1().setAttacked_once_this_round(0);
        table.getHero_2().setAttacked_once_this_round(0);
    }

    private int useAttack(Minion minionAttacks, Minion attackedCard, int xAttacked){

        // verify if the attacked card belongs to the attacker
        int whichPlayer = playerTurn;
        int enemyPlayer = 3 - whichPlayer;
        if(whichPlayer == 1) {
            if (xAttacked == 2 || xAttacked == 3) {
                return  1; // error : Attacked card does not belong to the enemy.
            }
        }
        if(whichPlayer == 2) {
            if(xAttacked == 0 || xAttacked == 1) {
                return 1;
            }
        }

        if(minionAttacks.getAttacked_once_this_round() == 1){
            return 4;
        }

        if(minionAttacks.getIs_frozen() == 1){
            return 2; // error : Attacker card is frozen.
        }

        int enemyPlayerFirstRow = 4 - (2 * enemyPlayer);

        int tankCardExists = 0;
        for(int i = enemyPlayerFirstRow; i <= enemyPlayerFirstRow + 1; i++) {
            for(int j = 0; j < table.getVectorRows()[i].size(); j++) {
                CardInput tempCard = table.getVectorRows()[i].get(j).getCard();
                if(tempCard.getName().equals("Goliath") || tempCard.getName().equals("Warden")) {
                    tankCardExists = 1;
                }
            }
        }

        CardInput tempInput = attackedCard.getCard();
        if(!(tempInput.getName().equals("Goliath") || tempInput.getName().equals("Warden"))
                && tankCardExists == 1) {
            return 3; // error : Attacked card is not of type 'Tank'.
        } else {
            int remainingHealth = attackedCard.getHealth() - minionAttacks.getDamage();
            attackedCard.setHealth(remainingHealth);
            minionAttacks.setAttacked_once_this_round(1);
        }

        return 0;
    }

    private int useHeroAttack(Minion attackerCard) {
        if(attackerCard.getIs_frozen() == 1){
            return 1; // error : Attacker card is frozen.
        }
        if (attackerCard.getAttacked_once_this_round() == 1) {
            return  2; // error : Attacker card has already attacked this turn.
        }

        int whichPlayer = playerTurn;

        Hero currentHero;
        if(playerTurn == 1){
            currentHero = table.getHero_2();
        } else {
            currentHero = table.getHero_1();
        }


        int enemyPlayer = 3 - whichPlayer;
        int enemyPlayerFirstRow = 4 - (2 * enemyPlayer);
        int tankCardExists = 0;


        for (int i = enemyPlayerFirstRow; i <= enemyPlayerFirstRow + 1; i++) {
            for (int j = 0; j < table.getVectorRows()[i].size(); j++) {
                CardInput tempCard = table.getVectorRows()[i].get(j).getCard();
                if (tempCard.getName().equals("Goliath") || tempCard.getName().equals("Warden")) {
                    tankCardExists = 1;
                }
            }
        }

        CardInput tempInput = attackerCard.getCard();
        if(!(tempInput.getName().equals("Goliath") || tempInput.getName().equals("Warden"))
                && tankCardExists == 1) {
            return 3; // error : Attacked card is not of type 'Tank'.
        } else {
            int currentHeroLife = currentHero.getHealth() - attackerCard.getDamage();
            currentHero.setHealth(currentHeroLife);
        }
        attackerCard.setAttacked_once_this_round(1);

        if(currentHero.getHealth() <= 0){
            if(playerTurn == 1){
                return 4; // Player one killed the enemy hero.
            } else {
                return 5; // Player two killed the enemy hero.
            }
        }
        return 0;
    }

    private void deleteDeadCards() {
        for(int i = 0; i <4; i++){
            ArrayList<Minion> to_delete = new ArrayList<>();
            for(int j = 0; j<table.getVectorRows()[i].size(); j++) {
                if(table.getVectorRows()[i].get(j).getHealth() <= 0) {
                    to_delete.add(table.getVectorRows()[i].get(j));
                }
            }
            table.getVectorRows()[i].removeAll(to_delete);
        }
    }

    private void unfrozeCards(int playerTurn) {
        int playerFirstRow = 4 - (2 * playerTurn);
        for(int i = playerFirstRow; i <= playerFirstRow + 1; i++) {
            for(int j = 0; j<table.getVectorRows()[i].size(); j++) {
                if(table.getVectorRows()[i].get(j).getIs_frozen() == 1) {
                    table.getVectorRows()[i].get(j).setIs_frozen(0);
                }
            }
        }
    }
    public Input getInput_data() {
        return input_data;
    }

    public void setInput_data(Input input_data) {
        this.input_data = input_data;
    }

}
