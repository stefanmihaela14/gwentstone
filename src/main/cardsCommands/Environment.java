package main.cardsCommands;

import fileio.CardInput;
import lombok.Getter;
import lombok.Setter;
import main.Gameplay;

import java.util.ArrayList;

@Getter
@Setter
public class Environment extends MyCard {

    public static final int NO_3 = 3;

    /**
     *
     * @return true if the card is type of Environment card and false if not
     */
    @Override
    public boolean isEnvironmentCard() {
        return true;
    }

    /**
     *
     * @param card ????
     */
    public Environment(CardInput card) {
        super(card);
    }

    /**
     * Verify which environment type of card is and implement its ability
     * to later use it in Gameplay
     * @param affectedRow the row attacked
     */
    public void useAbility(int affectedRow) {
        ArrayList<Minion> auxTable = Gameplay.getInstance().getTable().getVectorRows()[affectedRow];
        if (card.getName().equals("Firestorm")) {
            for (int i = 0; i < auxTable.size(); i++) {
                int currentHealth = auxTable.get(i).getHealth();
                auxTable.get(i).setHealth(currentHealth - 1);
            }
        }
        if (card.getName().equals("Winterfell")) {
            for (int i = 0; i < auxTable.size(); i++) {
                auxTable.get(i).setIsFrozen(1);
            }
        }
        if (card.getName().equals("Heart Hound")) {
            ArrayList<Minion> row = Gameplay.getInstance().getTable().getVectorRows()[affectedRow];
            int maxHealth = 0;
            Minion maxHealthCard = null;
            for (int i = 0; i < row.size(); i++) {
                Minion card = row.get(i);
                if (card.getHealth() > maxHealth) {
                    maxHealth = card.getHealth();
                    maxHealthCard = card;
                }
            }
            int toRow = NO_3 - affectedRow;
            row.remove(maxHealthCard);
            if (maxHealthCard != null) {
                Gameplay.getInstance().getTable().getVectorRows()[toRow].add(maxHealthCard);
            }

        }
    }
}
