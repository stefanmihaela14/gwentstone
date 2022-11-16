package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CardInput;
import fileio.Input;

import java.util.ArrayList;

public class Gameplay {
    private Input input_data;
    private ArrayNode output_data;

    public Gameplay(Input input_data, ArrayNode output_data){
        this.input_data = input_data;
        this.output_data = output_data;
    }

    private static int number_of_games;
    private static int player1_deck_index;
    private static int player2_deck_index;

    private static int player1_no_cards_in_deck;

    private static int player2_no_cards_in_deck;

    private static ArrayList<CardInput> player1_current_deck;

    private static ArrayList<CardInput> player2_current_deck;

// Getters and Setters
    public static int getNumber() {
        return number_of_games;
    }

    public static void setNumber(int number_of_games) {
        Gameplay.number_of_games =number_of_games;
    }

    public static int getPlayer1_deck_index() {
        return player1_deck_index;
    }

    public static void setPlayer1_deck_index(int player1_deck_index) {
        Gameplay.player1_deck_index = player1_deck_index;
    }

    public static int getPlayer2_deck_index() {
        return player2_deck_index;
    }

    public static void setPlayer2_deck_index(int player2_deck_index) {
        Gameplay.player2_deck_index = player2_deck_index;
    }

    public static ArrayList<CardInput> getPlayer1_current_deck() {
        return player1_current_deck;
    }

    public static void setPlayer1_current_deck(ArrayList<CardInput> player1_current_deck) {
        Gameplay.player1_current_deck = player1_current_deck;
    }

    public static int getPlayer1_no_cards_in_deck() {
        return player1_no_cards_in_deck;
    }

    public static void setPlayer1_no_cards_in_deck(int player1_no_cards_in_deck) {
        Gameplay.player1_no_cards_in_deck = player1_no_cards_in_deck;
    }

    public static ArrayList<CardInput> getPlayer2_current_deck() {
        return player2_current_deck;
    }

    public static void setPlayer2_current_deck(ArrayList<CardInput> player2_current_deck) {
        Gameplay.player2_current_deck = player2_current_deck;
    }

    public static int getPlayer2_no_cards_in_deck() {
        return player2_no_cards_in_deck;
    }

    public static void setPlayer2_no_cards_in_deck(int player2_no_cards_in_deck) {
        Gameplay.player2_no_cards_in_deck = player2_no_cards_in_deck;
    }

    public void gameRules(){

//        MyCard cartenoua = new Minion();
//        input_data.getPlayerOneDecks().getDecks().get(0).get(0);
        number_of_games = input_data.getGames().size();
        for(int i = 0; i < number_of_games; i++) {
            player1_deck_index = input_data.getGames().get(i).getStartGame().getPlayerOneDeckIdx();
            player2_deck_index = input_data.getGames().get(i).getStartGame().getPlayerTwoDeckIdx();

            player1_current_deck = input_data.getPlayerOneDecks().getDecks().get(player1_deck_index);
            player2_current_deck = input_data.getPlayerTwoDecks().getDecks().get(player2_deck_index);

            player1_no_cards_in_deck = player1_current_deck.size();
            player2_no_cards_in_deck = player2_current_deck.size();
        }

    }
}
