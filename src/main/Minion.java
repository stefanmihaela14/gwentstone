package main;
import java.util.Random;

public class Minion implements CardTamplate{

    public Minion(){

    }

    //mana
    @Override
    public void setMana(int mana) {

    }
    @Override
    public void getMana() {

    }
    //description
    @Override
    public void setDescription(String description) {

    }
    @Override
    public void getDescription() {

    }
    //colors
    @Override
    public void setColors(String colors) {

    }
    @Override
    public void getColors() {

    }
    //name
    @Override
    public void setName(String name) {

    }
    @Override
    public void getName() {

    }

    //Sentinel Berserker Goliath Warden
    public void type_of_minion(String minion_name) {
        String name = getName();
        if(minion_name.equals("Santinel") | minion_name.equals("Berserker") |
                minion_name.equals("Goliath") | minion_name.equals("Warden")) {
            System.out.printf("Cartea este un simplu minion");
        } else{
            if(minion_name.equals("The Ripper")) {
                int attack_one_opponent = 2;
            }
            if(minion_name.equals("Miraj")) {
                int attack_one_opponent = 2;
            }
            if(minion_name.equals("The Cursed One")) {
                int attack_one_opponent = 2;
            }
            if(minion_name.equals("Disciple")) {
                int attack_one_opponent = 2;
            }
        }
    }
}
