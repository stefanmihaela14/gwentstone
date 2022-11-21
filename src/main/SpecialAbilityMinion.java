package main;

import fileio.CardInput;

public class SpecialAbilityMinion extends Minion{

    public SpecialAbilityMinion(CardInput card) {
        super(card);
    }


    public int useAbility(Minion attackedCard, int attacked_x) {
        Table table = Gameplay.getInstance().getTable();

        if(getIs_frozen() == 1){
            return 1; // error : Attacker card is frozen.
        }

        if(attacked_once_this_round == 1) {
            return 2; // error : Attacker card has already attacked this turn.
        }

        int whichPlayer = Gameplay.getInstance().getPlayerTurn();
        int enemyPlayer = 3 - whichPlayer;

        if(card.getName().equals("Disciple")) {
            if(whichPlayer == 1) {
                if (attacked_x == 0 || attacked_x == 1) {
                    return  3; // error : Attacked card does not belong to the current player.
                }
            }
            if(whichPlayer == 2) {
                if(attacked_x == 2 || attacked_x == 3) {
                    return 3;
                }
            }
        } else {
            if(whichPlayer == 1) {
                if (attacked_x == 2 || attacked_x == 3) {
                    return  4; // error : Attacked card does not belong to the enemy.
                }
            }
            if(whichPlayer == 2) {
                if(attacked_x == 0 || attacked_x == 1) {
                    return 4;
                }
            }
        }

        int enemyPlayerFirstRow = 4 - (2 * enemyPlayer);
        int tankCardExists = 0;

        if(!card.getName().equals("Disciple")) {
            for (int i = enemyPlayerFirstRow; i <= enemyPlayerFirstRow + 1; i++) {
                for (int j = 0; j < table.getVectorRows()[i].size(); j++) {
                    CardInput tempCard = table.getVectorRows()[i].get(j).getCard();
                    if (tempCard.getName().equals("Goliath") || tempCard.getName().equals("Warden")) {
                        tankCardExists = 1;
                    }
                }
            }
        }

        CardInput tempInput = attackedCard.getCard();
        if(!(tempInput.getName().equals("Goliath") || tempInput.getName().equals("Warden"))
                && tankCardExists == 1) {
            return 5; // error : Attacked card is not of type 'Tank'.
        } else {
            if(card.getName().equals("The Ripper")) {
                int currentDamage = attackedCard.getDamage() - 2;
                if (currentDamage < 0) {
                    currentDamage = 0;
                }
                attackedCard.setDamage(currentDamage);
            }
            if(card.getName().equals("Miraj")) {
                int attackerLife = getHealth();
                int attackedLife = attackedCard.getHealth();
                setHealth(attackedLife);
                attackedCard.setHealth(attackerLife);
            }
            if(card.getName().equals("The Cursed One")) {
                int attackedLife = attackedCard.getHealth();
                int attackedAttackDamage = attackedCard.getDamage();
                attackedCard.setHealth(attackedAttackDamage);
                attackedCard.setDamage(attackedLife);
            }
            if(card.getName().equals("Disciple")) {
                int currentLife = attackedCard.getHealth() + 2;
                attackedCard.setHealth(currentLife);
            }
            attacked_once_this_round = 1;
        }

        return 0;
    }

}
