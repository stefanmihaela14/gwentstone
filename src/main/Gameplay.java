package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.Input;
import lombok.Getter;
import lombok.Setter;
import main.cardsCommands.Environment;
import main.cardsCommands.Hero;
import main.cardsCommands.Minion;
import main.cardsCommands.MyCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

@Getter
@Setter
public class Gameplay {
    private static Gameplay instance;

    /**
     * @return ??
     */
    public static Gameplay getInstance() {
        if (instance == null) {
            Gameplay.instance = new Gameplay();
        }
        return instance;
    }

    private Gameplay() {
    }

    private Player player1 = new Player();

    private Player player2 = new Player();

    private Input inputData;

    private ArrayNode outputData;

    private int numberOfGames;

    private int player1GameIndex;

    private int player2GameIndex;

    private int player1NoCardsInDeck;

    private int player2NoCardsInDeck;

    private ArrayList<CardInput> player1CurrentDeck;

    private ArrayList<CardInput> player2CurrentDeck;

    public static final int NO_3 = 3;

    public static final int NO_4 = 4;

    public static final int NO_5 = 5;

    public static final int NO_10 = 10;

    private Table table;

// Getters and Setters

    public Table getTable() {
        return table;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    /**
     * @return the deck of the first player as an arrayList
     */
    public ArrayList<CardInput> getplayer1CurrentDeck() {
        return player1CurrentDeck;
    }

    /**
     * @return the 1st player's no of cards
     */
    public int getplayer1NoCardsInDeck() {
        return player1NoCardsInDeck;
    }

    /**
     * @return the deck of the second player as an arrayList
     */
    public ArrayList<CardInput> getplayer2CurrentDeck() {
        return player2CurrentDeck;
    }

    /**
     * @return the second player's no of cards
     */
    public int getplayer2NoCardsInDeck() {
        return player2NoCardsInDeck;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    private int playerTurn;

    private int twoTurnsDone = 0;

    private int handIndex;

    private int gameEnded = 0;

    private int gamesPlayed = 0;

    /**
     *
     * @param InputData ??
     * @param OutputData ??
     */
    public void gameRules(Input InputData, ArrayNode OutputData) {
        inputData = InputData;
        outputData = OutputData;

        numberOfGames = inputData.getGames().size();
        for (int i = 0; i < numberOfGames; i++) {
            gameEnded = 0;
            twoTurnsDone = 0;
            int roundNo = 0;
            player1.setMana(1);
            player2.setMana(1);
            if (i == 0) {
                player1.setGamesWon(0);
                player2.setGamesWon(0);
                gamesPlayed = 0;
            }

            player1GameIndex = inputData.getGames().get(i).getStartGame().getPlayerOneDeckIdx();
            player2GameIndex = inputData.getGames().get(i).getStartGame().getPlayerTwoDeckIdx();

            player1CurrentDeck = inputData.getPlayerOneDecks().getDecks().get(player1GameIndex);
            player2CurrentDeck = inputData.getPlayerTwoDecks().getDecks().get(player2GameIndex);

            player1NoCardsInDeck = player1CurrentDeck.size();
            player2NoCardsInDeck = player2CurrentDeck.size();

            this.table = new Table();


            CardInput hero1 = inputData.getGames().get(i).getStartGame().getPlayerOneHero();

            table.setHero1(new Hero(hero1));

            Hero hero2 = new Hero(inputData.getGames().get(i).getStartGame().getPlayerTwoHero());

            table.setHero2(hero2);

            playerTurn = inputData.getGames().get(i).getStartGame().getStartingPlayer();


            // shuffle the decks
            int randSeed = inputData.getGames().get(i).getStartGame().getShuffleSeed();
            Collections.shuffle(table.getMyDeckPlayer1(), new Random(randSeed));
            Collections.shuffle(table.getMyDeckPlayer2(), new Random(randSeed));

            table.putCardInHand(1);
            table.putCardInHand(2);

            for (int j = 0; j < inputData.getGames().get(i).getActions().size(); j++) {
                // mana logic
                if (twoTurnsDone == 2) {
                    roundNo++;
                    twoTurnsDone = 0;
                    int valueToIncreaseMana = roundNo + 1;
                    if (valueToIncreaseMana > NO_10) {
                        valueToIncreaseMana = NO_10;
                    }
                    int player1Mana = player1.getMana();
                    int player2Mana = player2.getMana();

                    player1.setMana(player1Mana + valueToIncreaseMana);
                    player2.setMana(player2Mana + valueToIncreaseMana);

                    table.putCardInHand(1);
                    table.putCardInHand(2);
                }

                handIndex = inputData.getGames().get(i).getActions().get(j).getHandIdx();

                boolean debuggingCommand = false;
                String[] debuggingCommands = {"getPlayerDeck", "getPlayerHero", "getPlayerTurn",
                        "getCardsInHand", "getPlayerMana", "getCardsOnTable",
                        "getEnvironmentCardsInHand", "getCardAtPosition", "getFrozenCardsOnTable",
                        "getTotalGamesPlayed", "getPlayerOneWins", "getPlayerTwoWins"};
                String command = inputData.getGames().get(i).getActions().get(j).getCommand();
                debuggingCommand = Arrays.stream(debuggingCommands).toList().contains(command);
                if (!(gameEnded == 1 && !debuggingCommand)) {
                    doActions(inputData.getGames().get(i).getActions().get(j));
                }
                //verify if any card after each action has health <= 0 and delete it
                deleteDeadCards();
            }
        }
    }

    /**
     *
     * @param actionsInput ??
     */
    public void doActions(ActionsInput actionsInput) {
        if (actionsInput.getCommand().equals("getPlayerDeck")) {
            ObjectNode newNode = outputData.addObject();
            int whichPlayer = actionsInput.getPlayerIdx();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("playerIdx", actionsInput.getPlayerIdx());
            ArrayNode outputArray = newNode.putArray("output");
            ArrayList<MyCard> deck;
            if (whichPlayer == 1) {
                deck = table.getMyDeckPlayer1();
            } else {
                deck = table.getMyDeckPlayer2();
            }
            for (int i = 0; i < deck.size(); i++) {
                CardInput tempCard = deck.get(i).getCard();
                ObjectNode cardNode = outputArray.addObject();
                cardNode.put("mana", tempCard.getMana());
                if (!(deck.get(i) instanceof Environment)) {
                    cardNode.put("attackDamage", tempCard.getAttackDamage());
                    cardNode.put("health", tempCard.getHealth());
                }
                cardNode.put("description", tempCard.getDescription());
                ArrayNode colorsArray = cardNode.putArray("colors");
                for (int j = 0; j < tempCard.getColors().size(); j++) {
                    colorsArray.add(tempCard.getColors().get(j));
                }
                cardNode.put("name", tempCard.getName());
            }
        }
        if (actionsInput.getCommand().equals("getPlayerHero")) {
            ObjectNode newNode = outputData.addObject();
            int whichPlayer = actionsInput.getPlayerIdx();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("playerIdx", actionsInput.getPlayerIdx());
            ObjectNode heroNode = newNode.putObject("output");
            CardInput hero;
            if (whichPlayer == 1) {
                hero = table.getHero1().getCard();
            } else {
                hero = table.getHero2().getCard();
            }
            heroNode.put("mana", hero.getMana());
            heroNode.put("description", hero.getDescription());
            ArrayNode colorsArray = heroNode.putArray("colors");
            for (int j = 0; j < hero.getColors().size(); j++) {
                colorsArray.add(hero.getColors().get(j));
            }
            heroNode.put("name", hero.getName());
            Hero tempHero;
            if (whichPlayer == 1) {
                tempHero = table.getHero1();
            } else {
                tempHero = table.getHero2();
            }
            heroNode.put("health", tempHero.getHealth());
        }
        if (actionsInput.getCommand().equals("endPlayerTurn")) {
            unfrozeCards(playerTurn);
            if (playerTurn == 1) {
                playerTurn = 2;
            } else {
                playerTurn = 1;
            }
            twoTurnsDone++;
            resetWhoAttacked();
        }
        if (actionsInput.getCommand().equals("getPlayerTurn")) {
            ObjectNode newNode = outputData.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("output", playerTurn);
        }
        if (actionsInput.getCommand().equals("getCardsInHand")) {
            ObjectNode newNode = outputData.addObject();
            int whichPlayer = actionsInput.getPlayerIdx();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("playerIdx", actionsInput.getPlayerIdx());
            ArrayNode outputArray = newNode.putArray("output");
            ArrayList<MyCard> hand;
            if (whichPlayer == 1) {
                hand = table.getHandPlayer1();
            } else {
                hand = table.getHandPlayer2();
            }
            for (int i = 0; i < hand.size(); i++) {
                CardInput tempHandCard = hand.get(i).getCard();
                ObjectNode cardNode = outputArray.addObject();
                cardNode.put("mana", tempHandCard.getMana());
                if (!(hand.get(i) instanceof Environment)) {
                    cardNode.put("attackDamage", tempHandCard.getAttackDamage());
                    cardNode.put("health", tempHandCard.getHealth());
                }
                cardNode.put("description", tempHandCard.getDescription());
                ArrayNode colorsArray = cardNode.putArray("colors");
                for (int j = 0; j < tempHandCard.getColors().size(); j++) {
                    colorsArray.add(tempHandCard.getColors().get(j));
                }
                cardNode.put("name", tempHandCard.getName());
            }
        }
        if (actionsInput.getCommand().equals("getPlayerMana")) {
            ObjectNode newNode = outputData.addObject();
            int whichPlayer = actionsInput.getPlayerIdx();
            Player player = null;
            if (whichPlayer == 1) {
                player = player1;
            } else {
                player = player2;
            }
            newNode.put("command", actionsInput.getCommand());
            newNode.put("playerIdx", actionsInput.getPlayerIdx());
            newNode.put("output", player.getMana());
        }
        if (actionsInput.getCommand().equals("placeCard")) {
            int output = table.putCardOnTable(handIndex, playerTurn);
            if (output == 1) {
                ObjectNode newNode = outputData.addObject();
                newNode.put("command", actionsInput.getCommand());
                newNode.put("handIdx", actionsInput.getHandIdx());
                newNode.put("error", "Cannot place environment card on table.");
            } else if (output == 2) {
                ObjectNode newNode = outputData.addObject();
                newNode.put("command", actionsInput.getCommand());
                newNode.put("handIdx", actionsInput.getHandIdx());
                newNode.put("error", "Not enough mana to place card on table.");
            } else if (output == NO_3) {
                ObjectNode newNode = outputData.addObject();
                newNode.put("command", actionsInput.getCommand());
                newNode.put("handIdx", actionsInput.getHandIdx());
                newNode.put("error", "Cannot place card on table since row is full.");
            }
        }
        if (actionsInput.getCommand().equals("getCardsOnTable")) {
            ObjectNode newNode = outputData.addObject();
            newNode.put("command", actionsInput.getCommand());
            ArrayNode outputArray = newNode.putArray("output");
            for (int i = 0; i < table.getVectorRows().length; i++) {
                ArrayNode lineArray = outputArray.addArray();
                for (int j = 0; j < table.getVectorRows()[i].size(); j++) {
                    CardInput tempCard = table.getVectorRows()[i].get(j).getCard();
                    ObjectNode cardNode = lineArray.addObject();
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
        if (actionsInput.getCommand().equals("getEnvironmentCardsInHand")) { //this should work
            ObjectNode newNode = outputData.addObject();
            int whichPlayer = actionsInput.getPlayerIdx();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("playerIdx", actionsInput.getPlayerIdx());
            ArrayNode outputArray = newNode.putArray("output");
            ArrayList<MyCard> hand;
            if (whichPlayer == 1) {
                hand = table.getHandPlayer1();
            } else {
                hand = table.getHandPlayer2();
            }
            for (int i = 0; i < hand.size(); i++) {
                CardInput tempHandCard = hand.get(i).getCard();
                if (hand.get(i) instanceof Environment) {
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
        if (actionsInput.getCommand().equals("getCardAtPosition")) {
            int x = actionsInput.getX();
            int y = actionsInput.getY();
            ObjectNode newNode = outputData.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("x", x);
            newNode.put("y", y);
            if (y + 1 > table.getVectorRows()[x].size()) {
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
            for (int k = 0; k < tempCard.getColors().size(); k++) {
                colorsArray.add(tempCard.getColors().get(k));
            }
            cardNode.put("name", tempCard.getName());
        }
        if (actionsInput.getCommand().equals("useEnvironmentCard")) {
            int handIndex = actionsInput.getHandIdx();
            int affectedRow = actionsInput.getAffectedRow();
            int whichPlayer = playerTurn;
            int errorNo = table.useEnvironmentCard(handIndex, affectedRow, whichPlayer);
            if (errorNo == 0) {
                return;
            }
            ObjectNode newNode = outputData.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("handIdx", actionsInput.getHandIdx());
            newNode.put("affectedRow", actionsInput.getAffectedRow());
            String errorMsg = null;
            if (errorNo == 1) {
                errorMsg = "Chosen card is not of type environment.";
            }
            if (errorNo == 2) {
                errorMsg = "Not enough mana to use environment card.";
            }
            if (errorNo == NO_3) {
                errorMsg = "Chosen row does not belong to the enemy.";
            }
            if (errorNo == NO_4) {
                errorMsg = "Cannot steal enemy card since the player's row is full.";
            }
            newNode.put("error", errorMsg);
        }
        if (actionsInput.getCommand().equals("getFrozenCardsOnTable")) {
            ObjectNode newNode = outputData.addObject();
            newNode.put("command", actionsInput.getCommand());
            ArrayNode outputArray = newNode.putArray("output");
            for (int i = 0; i < table.getVectorRows().length; i++) {
                for (int j = 0; j < table.getVectorRows()[i].size(); j++) {
                    CardInput tempCard = table.getVectorRows()[i].get(j).getCard();
                    if (table.getVectorRows()[i].get(j).getIsFrozen() == 1) {
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
        if (actionsInput.getCommand().equals("cardUsesAttack")) {

            int xAttacker = actionsInput.getCardAttacker().getX();
            int yAttacker = actionsInput.getCardAttacker().getY();
            int xAttacked = actionsInput.getCardAttacked().getX();
            int yAttacked = actionsInput.getCardAttacked().getY();

            Minion attackerMinion = table.getVectorRows()[xAttacker].get(yAttacker);
            Minion attackedMinion = table.getVectorRows()[xAttacked].get(yAttacked);
            int errorMsg = useAttack(attackerMinion, attackedMinion, xAttacked);

            if (errorMsg != 0) {
                ObjectNode newNode = outputData.addObject();
                newNode.put("command", actionsInput.getCommand());
                ObjectNode attackerNode = newNode.putObject("cardAttacker");
                attackerNode.put("x", actionsInput.getCardAttacker().getX());
                attackerNode.put("y", actionsInput.getCardAttacker().getY());
                ObjectNode attackedNode = newNode.putObject("cardAttacked");
                attackedNode.put("x", actionsInput.getCardAttacked().getX());
                attackedNode.put("y", actionsInput.getCardAttacked().getY());
                String errorStr = null;
                if (errorMsg == 1) {
                    errorStr = "Attacked card does not belong to the enemy.";
                }
                if (errorMsg == 2) {
                    errorStr = "Attacker card is frozen.";
                }
                if (errorMsg == NO_4) {
                    errorStr = "Attacker card has already attacked this turn.";
                }
                if (errorMsg == NO_3) {
                    errorStr = "Attacked card is not of type 'Tank'.";
                }
                newNode.put("error", errorStr);
            }
        }
        if (actionsInput.getCommand().equals("cardUsesAbility")) {
            int xAttacker = actionsInput.getCardAttacker().getX();
            int yAttacker = actionsInput.getCardAttacker().getY();
            int xAttacked = actionsInput.getCardAttacked().getX();
            int yAttacked = actionsInput.getCardAttacked().getY();

            if (yAttacker + 1 > table.getVectorRows()[xAttacker].size()) {
                return;
            }
            if (yAttacked + 1 > table.getVectorRows()[xAttacked].size()) {
                return;
            }
            Minion attackerCard = table.getVectorRows()[xAttacker].get(yAttacker);
            Minion attackedCard = table.getVectorRows()[xAttacked].get(yAttacked);

            int errorMsg = attackerCard.useAbility(attackedCard, xAttacked);
            if (errorMsg != 0) {
                ObjectNode newNode = outputData.addObject();
                newNode.put("command", actionsInput.getCommand());
                ObjectNode attackerNode = newNode.putObject("cardAttacker");
                attackerNode.put("x", actionsInput.getCardAttacker().getX());
                attackerNode.put("y", actionsInput.getCardAttacker().getY());
                ObjectNode attackedNode = newNode.putObject("cardAttacked");
                attackedNode.put("x", actionsInput.getCardAttacked().getX());
                attackedNode.put("y", actionsInput.getCardAttacked().getY());
                String errorStr = null;
                if (errorMsg == 1) {
                    errorStr = "Attacker card is frozen.";
                }
                if (errorMsg == 2) {
                    errorStr = "Attacker card has already attacked this turn.";
                }
                if (errorMsg == NO_3) {
                    errorStr = "Attacked card does not belong to the current player.";
                }
                if (errorMsg == NO_4) {
                    errorStr = "Attacked card does not belong to the enemy.";
                }
                if (errorMsg == NO_5) {
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
            if (errorMsg == 1 || errorMsg == 2 || errorMsg == NO_3) {
                ObjectNode newNode = outputData.addObject();
                newNode.put("command", actionsInput.getCommand());
                ObjectNode attackerNode = newNode.putObject("cardAttacker");
                attackerNode.put("x", actionsInput.getCardAttacker().getX());
                attackerNode.put("y", actionsInput.getCardAttacker().getY());
                String errorStr = null;
                if (errorMsg == 1) {
                    errorStr = "Attacker card is frozen.";
                }
                if (errorMsg == 2) {
                    errorStr = "Attacker card has already attacked this turn.";
                }
                if (errorMsg == NO_3) {
                    errorStr = "Attacked card is not of type 'Tank'.";
                }
                newNode.put("error", errorStr);
            }
            if (errorMsg == NO_4 || errorMsg == NO_5) {
                gameEnded = 1;
                ObjectNode newNode = outputData.addObject();
                if (errorMsg == NO_4) {
                    newNode.put("gameEnded", "Player one killed the enemy hero.");
                    player1.incrementGamesWon();
                    gamesPlayed = gamesPlayed + 1;
                }
                if (errorMsg == NO_5) {
                    newNode.put("gameEnded", "Player two killed the enemy hero.");
                    player2.incrementGamesWon();
                    gamesPlayed = gamesPlayed + 1;
                }
            }
        }
        if (actionsInput.getCommand().equals("useHeroAbility")) {
            int attackedRow = actionsInput.getAffectedRow();

            Hero currentHero = null;
            Player currentPlayer = null;
            if (playerTurn == 1) {
                currentHero = table.getHero1();
                currentPlayer = player1;
            } else {
                currentHero = table.getHero2();
                currentPlayer = player2;
            }
            int errorMsg = currentHero.heroAbility(attackedRow, currentPlayer);
            if (errorMsg != 0) {
                ObjectNode newNode = outputData.addObject();
                newNode.put("command", actionsInput.getCommand());
                newNode.put("affectedRow", actionsInput.getAffectedRow());
                String errString = null;
                if (errorMsg == 1) {
                    errString = "Not enough mana to use hero's ability.";
                }
                if (errorMsg == 2) {
                    errString = "Hero has already attacked this turn.";
                }
                if (errorMsg == NO_3) {
                    errString = "Selected row does not belong to the enemy.";
                }
                if (errorMsg == NO_4) {
                    errString = "Selected row does not belong to the current player.";
                }
                newNode.put("error", errString);
            }
        }
        if (actionsInput.getCommand().equals("getPlayerOneWins")) {
            int whichPlayer = 1;
            int gamesWon = getPlayerWins(whichPlayer);
            ObjectNode newNode = outputData.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("output", gamesWon);
        }
        if (actionsInput.getCommand().equals("getPlayerTwoWins")) {
            int whichPlayer = 2;
            int gamesWon = getPlayerWins(whichPlayer);
            ObjectNode newNode = outputData.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("output", gamesWon);
        }
        if (actionsInput.getCommand().equals("getTotalGamesPlayed")) {
            ObjectNode newNode = outputData.addObject();
            newNode.put("command", actionsInput.getCommand());
            newNode.put("output", gamesPlayed);
        }
    }

    // some auxiliary functions
    /**
     *
     * @param whichPLayer the player whose turn currently is
     * @return the number which corresponds to the output error
     */
    private int getPlayerWins(int whichPLayer) {
        Player player = null;
        if (whichPLayer == 1) {
            player = player1;
        } else {
            player = player2;
        }
        int gamesWon = player.getGamesWon();
        return gamesWon;
    }

    /**
     * after a turn reset the minion/hero who has attacked status to 0
     * to  know that it hasn't attacked this new turn
     */
    private void resetWhoAttacked() {
        for (int i = 0; i < NO_4; i++) {
            for (Minion minion: table.getVectorRows()[i]) {
                minion.setAttackedOnceThisRound(0);
            }
        }
        table.getHero1().setAttackedOnceThisRound(0);
        table.getHero2().setAttackedOnceThisRound(0);
    }

    /**
     *
     * @param minionAttacks the minion who attacks
     * @param attackedCard the minion who is attacked
     * @param xAttacked the row where is the attacked minion
     * @return the number which corresponds to the output error
     */
    private int useAttack(Minion minionAttacks, Minion attackedCard, int xAttacked) {

        // verify if the attacked card belongs to the attacker
        int whichPlayer = playerTurn;
        int enemyPlayer = NO_3 - whichPlayer;
        if (whichPlayer == 1) {
            if (xAttacked == 2 || xAttacked == NO_3) {
                return  1; // error : Attacked card does not belong to the enemy.
            }
        }
        if (whichPlayer == 2) {
            if (xAttacked == 0 || xAttacked == 1) {
                return 1;
            }
        }

        if (minionAttacks.getAttackedOnceThisRound() == 1) {
            return NO_4;
        }

        if (minionAttacks.getIsFrozen() == 1) {
            return 2; // error : Attacker card is frozen.
        }

        int enemyPlayerFirstRow = NO_4 - (2 * enemyPlayer);

        int tankCardExists = 0;
        for (int i = enemyPlayerFirstRow; i <= enemyPlayerFirstRow + 1; i++) {
            for (int j = 0; j < table.getVectorRows()[i].size(); j++) {
                CardInput tempCard = table.getVectorRows()[i].get(j).getCard();
                if (tempCard.getName().equals("Goliath") || tempCard.getName().equals("Warden")) {
                    tankCardExists = 1;
                }
            }
        }

        CardInput tempInput = attackedCard.getCard();
        if (!(tempInput.getName().equals("Goliath") || tempInput.getName().equals("Warden"))
                && tankCardExists == 1) {
            return NO_3; // error : Attacked card is not of type 'Tank'.
        } else {
            int remainingHealth = attackedCard.getHealth() - minionAttacks.getDamage();
            attackedCard.setHealth(remainingHealth);
            minionAttacks.setAttackedOnceThisRound(1);
        }

        return 0;
    }

    /**
     *
     * @param attackerCard the minion who is attacks the hero
     * @return the number which corresponds to the output error
     */
    private int useHeroAttack(Minion attackerCard) {
        if (attackerCard.getIsFrozen() == 1) {
            return 1; // error : Attacker card is frozen.
        }
        if (attackerCard.getAttackedOnceThisRound() == 1) {
            return  2; // error : Attacker card has already attacked this turn.
        }

        int whichPlayer = playerTurn;

        Hero currentHero;
        if (playerTurn == 1) {
            currentHero = table.getHero2();
        } else {
            currentHero = table.getHero1();
        }


        int enemyPlayer = NO_3 - whichPlayer;
        int enemyPlayerFirstRow = NO_4 - (2 * enemyPlayer);
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
        if (!(tempInput.getName().equals("Goliath") || tempInput.getName().equals("Warden"))
                && tankCardExists == 1) {
            return NO_3; // error : Attacked card is not of type 'Tank'.
        } else {
            int currentHeroLife = currentHero.getHealth() - attackerCard.getDamage();
            currentHero.setHealth(currentHeroLife);
        }
        attackerCard.setAttackedOnceThisRound(1);

        if (currentHero.getHealth() <= 0) {
            if (playerTurn == 1) {
                return NO_4; // Player one killed the enemy hero.
            } else {
                return NO_5; // Player two killed the enemy hero.
            }
        }
        return 0;
    }

    /**
     * after every command verify if there are any cards with health <= 0
     * and delete them from the table
     */
    private void deleteDeadCards() {
        for (int i = 0; i < NO_4; i++) {
            ArrayList<Minion> toDelete = new ArrayList<>();
            for (int j = 0; j < table.getVectorRows()[i].size(); j++) {
                if (table.getVectorRows()[i].get(j).getHealth() <= 0) {
                    toDelete.add(table.getVectorRows()[i].get(j));
                }
            }
            table.getVectorRows()[i].removeAll(toDelete);
        }
    }

    /**
     *
     * @param playerTurn the player whose turn currently is
     */
    private void unfrozeCards(int playerTurn) {
        int playerFirstRow = NO_4 - (2 * playerTurn);
        for (int i = playerFirstRow; i <= playerFirstRow + 1; i++) {
            for (int j = 0; j < table.getVectorRows()[i].size(); j++) {
                if (table.getVectorRows()[i].get(j).getIsFrozen() == 1) {
                    table.getVectorRows()[i].get(j).setIsFrozen(0);
                }
            }
        }
    }

}
