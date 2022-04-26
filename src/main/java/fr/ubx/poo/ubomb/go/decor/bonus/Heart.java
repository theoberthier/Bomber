package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Heart extends Bonus{
    public Heart(Position position, int level){
        super(position, level);
    }
    @Override
    public boolean isWalkable(Player player) {
        return true;
    }

    @Override
    public void takenBy(Player player) {
        remove(); player.takeHeart();
    }

}
