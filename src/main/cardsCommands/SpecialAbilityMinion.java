package main.cardsCommands;

import fileio.CardInput;
import lombok.Getter;
import lombok.Setter;
import main.Gameplay;
import main.Table;

@Setter
@Getter
public class SpecialAbilityMinion extends Minion {

    public static final int NO_3 = 3;

    public static final int NO_2 = 2;

    public static final int NO_4 = 4;

    public static final int NO_5 = 5;

    /**
     * @param card The card's input data.
     */
    public SpecialAbilityMinion(final CardInput card) {
        super(card);
    }

    /**
     * verify which special minion it is and make the logic for its ability
     * @param attackedX the row where the attacked card is
     * @return the number which corresponds to the output error
     */
    public int useAbility(final Minion attackedCard, final int attackedX) {
        Table table = Gameplay.getInstance().getTable();

        if (getIsFrozen() == 1) {
            return 1; // error : Attacker card is frozen.
        }

        if (getAttackedOnceThisRound() == 1) {
            return 2; // error : Attacker card has already attacked this turn.
        }

        int whichPlayer = Gameplay.getInstance().getPlayerTurn();
        int enemyPlayer = NO_3 - whichPlayer;

        if (card.getName().equals("Disciple")) {
            if (whichPlayer == 1) {
                if (attackedX == 0 || attackedX == 1) {
                    return  NO_3; // error : Attacked card does not belong to the current player.
                }
            }
            if (whichPlayer == 2) {
                if (attackedX == 2 || attackedX == NO_3) {
                    return NO_3;
                }
            }
        } else {
            if (whichPlayer == 1) {
                if (attackedX == NO_2 || attackedX == NO_3) {
                    return  NO_4; // error : Attacked card does not belong to the enemy.
                }
            }
            if (whichPlayer == 2) {
                if (attackedX == 0 || attackedX == 1) {
                    return NO_4;
                }
            }
        }

        int enemyPlayerFirstRow = NO_4 - (2 * enemyPlayer);
        int tankCardExists = 0;

        if (!card.getName().equals("Disciple")) {
            for (int i = enemyPlayerFirstRow; i <= enemyPlayerFirstRow + 1; i++) {
                for (int j = 0; j < table.getVectorRows()[i].size(); j++) {
                    CardInput tempCard = table.getVectorRows()[i].get(j).getCard();
                    if (tempCard.getName().equals("Goliath")
                            || tempCard.getName().equals("Warden")) {
                        tankCardExists = 1;
                    }
                }
            }
        }

        CardInput tempInput = attackedCard.getCard();
        if (!(tempInput.getName().equals("Goliath") || tempInput.getName().equals("Warden"))
                && tankCardExists == 1) {
            return NO_5; // error : Attacked card is not of type 'Tank'.
        } else {
            if (card.getName().equals("The Ripper")) {
                int currentDamage = attackedCard.getDamage() - 2;
                if (currentDamage < 0) {
                    currentDamage = 0;
                }
                attackedCard.setDamage(currentDamage);
            }
            if (card.getName().equals("Miraj")) {
                int attackerLife = getHealth();
                int attackedLife = attackedCard.getHealth();
                setHealth(attackedLife);
                attackedCard.setHealth(attackerLife);
            }
            if (card.getName().equals("The Cursed One")) {
                int attackedLife = attackedCard.getHealth();
                int attackedAttackDamage = attackedCard.getDamage();
                attackedCard.setHealth(attackedAttackDamage);
                attackedCard.setDamage(attackedLife);
            }
            if (card.getName().equals("Disciple")) {
                int currentLife = attackedCard.getHealth() + 2;
                attackedCard.setHealth(currentLife);
            }
            setAttackedOnceThisRound(1);
        }

        return 0;
    }

}
