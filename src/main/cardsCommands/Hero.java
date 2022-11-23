package main.cardsCommands;

import fileio.CardInput;
import lombok.Getter;
import lombok.Setter;
import main.Gameplay;
import main.Player;
import main.Table;

@Getter
@Setter
public class Hero extends MyCard {

    /**
     * @param card The hero's input data.
     */
    public Hero(final CardInput card) {
        super(card);
    }

    /**
     * @return true if the card is type of Environment card and false if not
     */
    @Override
    public boolean isEnvironmentCard() {
        return false;
    }

    protected int attackedOnceThisRound = 0;

    /**
     * @return 1 if it has attacked that round and 0 otherwise
     */
    public int getAttackedOnceThisRound() {
        return attackedOnceThisRound;
    }

    /**
     * @param attackedOnceThisRound we set the value to know
     * if the hero has attacked this round or not
     */
    public void setAttackedOnceThisRound(final int attackedOnceThisRound) {
        this.attackedOnceThisRound = attackedOnceThisRound;
    }

    public static final int NO_3 = 3;

    public static final int NO_4 = 4;

    public static final int HEALTH_START = 30;

    private int health = HEALTH_START;

    /**
     * @return the hero's health
     */
    public int getHealth() {
        return health;
    }

    /**
     * @param health the new health of the hero
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * verify which hero is and make the logic for its ability
     * @param player an instance of the player who uses the hero
     * @return the number which corresponds to the output error
     */
    public int heroAbility(final int attackedRow, final Player player) {
        Table table = Gameplay.getInstance().getTable();
        int attackedCardPosition = -1;

        if (player.getMana() < card.getMana()) {
            return 1; // error : Not enough mana to use hero's ability.
        }
        if (getAttackedOnceThisRound() == 1) {
            return 2; // error : Hero has already attacked this turn.
        }

        int playerTurn = Gameplay.getInstance().getPlayerTurn();
        String name = card.getName();

        if (name.equals("Lord Royce") || name.equals("Empress Thorina")) {
            if (playerTurn == 1) {
                if (attackedRow == 2 || attackedRow == NO_3) {
                    return NO_3; // error : Selected row does not belong to the enemy.
                }
            }
            if (playerTurn == 2) {
                if (attackedRow == 1 || attackedRow == 0) {
                    return NO_3;
                }
            }
        } else if (name.equals("General Kocioraw") || name.equals("King Mudface")) {
            if (playerTurn == 1) {
                if (attackedRow == 1 || attackedRow == 0) {
                    return NO_4; // error : Selected row does not belong to the current player.
                }
            }
            if (playerTurn == 2) {
                if (attackedRow == 2 || attackedRow == NO_3) {
                    return NO_4;
                }
            }
        }

        if (card.getName().equals("Lord Royce")) {
            int biggestDamage = -1;
            for (int i = 0; i < table.getVectorRows()[attackedRow].size(); i++) {
                if (table.getVectorRows()[attackedRow].get(i).getDamage() > biggestDamage) {
                    attackedCardPosition = i;
                }
            }
            Minion attackedCard = table.getVectorRows()[attackedRow].get(attackedCardPosition);
            attackedCard.setIsFrozen(1);
        }
        if (card.getName().equals("Empress Thorina")) {
            int biggestHealth = 0;
            for (int i = 0; i < table.getVectorRows()[attackedRow].size(); i++) {
                if (table.getVectorRows()[attackedRow].get(i).getHealth() > biggestHealth) {
                    attackedCardPosition = i;
                }
            }
            table.getVectorRows()[attackedRow].remove(attackedCardPosition);
        }
        if (card.getName().equals("King Mudface")) {
            for (int i = 0; i < table.getVectorRows()[attackedRow].size(); i++) {
                int newHealth = table.getVectorRows()[attackedRow].get(i).getHealth() + 1;
                table.getVectorRows()[attackedRow].get(i).setHealth(newHealth);
            }
        }
        if (card.getName().equals("General Kocioraw")) {
            for (int i = 0; i < table.getVectorRows()[attackedRow].size(); i++) {
                int newDamage = table.getVectorRows()[attackedRow].get(i).getDamage() + 1;
                table.getVectorRows()[attackedRow].get(i).setDamage(newDamage);
            }
        }
        int remainingMana = player.getMana() - card.getMana();
        player.setMana(remainingMana);
        setAttackedOnceThisRound(1);
        return 0;
    }


}
