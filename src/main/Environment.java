package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Environment extends MyCard{

    @Override
    public boolean isEnvironmentCard() {
        return true;
    }

    public Environment(CardInput card) {
        super(card);
    }

    public void useAbility(int affectedRow) {
        ArrayList<Minion> auxTable = Gameplay.getInstance().getTable().getVectorRows()[affectedRow];
        if(card.getName().equals("Firestorm")) {
            for(int i = 0; i < auxTable.size(); i++) {
                int currentHealth = auxTable.get(i).getHealth();
                auxTable.get(i).setHealth(currentHealth - 1);
            }
        }
        if(card.getName().equals("Winterfell")) {
            for(int i = 0; i < auxTable.size(); i++) {
                auxTable.get(i).setIs_frozen(1);
            }
        }
        if(card.getName().equals("Heart Hound")) {
            ArrayList<Minion> row = Gameplay.getInstance().getTable().getVectorRows()[affectedRow];
            int max_health = 0;
            Minion max_health_card = null;
            for(int i = 0; i<row.size(); i++){
                Minion card = row.get(i);
                if(card.getHealth() > max_health){
                    max_health = card.getHealth();
                    max_health_card = card;
                }
            }
            int to_row = 3 - affectedRow;
            row.remove(max_health_card);
            if(max_health_card != null) {
                Gameplay.getInstance().getTable().getVectorRows()[to_row].add(max_health_card);
            }

        }
    }
}
