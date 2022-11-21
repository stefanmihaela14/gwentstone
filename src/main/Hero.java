package main;

import fileio.CardInput;

public class Hero extends MyCard{
    public Hero(CardInput card) {
        super(card);
    }

    @Override
    public boolean isEnvironmentCard() {
        return false;
    }

    protected int attacked_once_this_round = 0; // after it attacks becomes 1

    public int getAttacked_once_this_round() {
        return attacked_once_this_round;
    }

    public void setAttacked_once_this_round(int attacked_once_this_round) {
        this.attacked_once_this_round = attacked_once_this_round;
    }

    private int health = 30;

    public int heroAbility(int attackedRow, Player player) {
        Table table = Gameplay.getInstance().getTable();
        int attackedCardPosition = -1;

        if(player.getMana() < card.getMana()){
            return 1; // error : Not enough mana to use hero's ability.
        }
        if(getAttacked_once_this_round() == 1){
            return 2; // error : Hero has already attacked this turn.
        }

        int playerTurn = Gameplay.getInstance().getPlayerTurn();
        String name = card.getName();

        if(name.equals("Lord Royce") || name.equals("Empress Thorina")){
            if(playerTurn == 1) {
                if(attackedRow == 2 || attackedRow == 3) {
                    return 3; // error : Selected row does not belong to the enemy.
                }
            }
            if(playerTurn == 2){
                if(attackedRow == 1 || attackedRow == 0){
                    return 3;
                }
            }
        } else if (name.equals("General Kocioraw") || name.equals("King Mudface")){
            if(playerTurn == 1) {
                if(attackedRow == 1 || attackedRow == 0){
                    return 4; // error : Selected row does not belong to the current player.
                }
            }
            if(playerTurn == 2){
                if(attackedRow == 2 || attackedRow == 3){
                    return 4;
                }
            }
        }

        if(card.getName().equals("Lord Royce")) {
            int biggestDamage = -1;
            for(int i = 0; i < table.getVectorRows()[attackedRow].size(); i++) {
                if(table.getVectorRows()[attackedRow].get(i).getDamage() > biggestDamage){
                    attackedCardPosition = i;
                }
            }
            Minion attackedCard = table.getVectorRows()[attackedRow].get(attackedCardPosition);
            attackedCard.setIs_frozen(1);
        }
        if(card.getName().equals("Empress Thorina")) {
            int biggestHealth = 0;
            for(int i = 0; i < table.getVectorRows()[attackedRow].size(); i++) {
                if(table.getVectorRows()[attackedRow].get(i).getHealth() > biggestHealth){
                    attackedCardPosition = i;
                }
            }
            table.getVectorRows()[attackedRow].remove(attackedCardPosition);
        }
        if(card.getName().equals("King Mudface")) {
            for(int i = 0; i < table.getVectorRows()[attackedRow].size(); i++) {
                int newHealth = table.getVectorRows()[attackedRow].get(i).getHealth() + 1;
                table.getVectorRows()[attackedRow].get(i).setHealth(newHealth);
            }
        }
        if(card.getName().equals("General Kocioraw")) {
            for(int i = 0; i < table.getVectorRows()[attackedRow].size(); i++) {
                int newDamage = table.getVectorRows()[attackedRow].get(i).getDamage() + 1;
                table.getVectorRows()[attackedRow].get(i).setDamage(newDamage);
            }
        }
        int remainingMana = player.getMana() - card.getMana();
        player.setMana(remainingMana);
        setAttacked_once_this_round(1);
        return 0;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
