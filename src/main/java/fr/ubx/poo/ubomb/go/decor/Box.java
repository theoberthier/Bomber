package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Grid;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.decor.bonus.Bomb;

import java.util.List;

public class Box extends Decor{

    public Box(Game game,Position position, int level) {
        super(game, position, level);
    }
    public boolean canMove(Position position) {
        Grid grid = game.getGrid();
        int width = grid.getWidth();
        int height = grid.getHeight();

        if(grid.get(position) == null && position.getX() < width && position.getY() < height && position.getX() >= 0 && position.getY() >= 0) {
            List<GameObject> gos = game.getGameObjects(position);
            if(gos.size() == 0) return true;
            for (GameObject go:gos) {
                if(go.level == game.levels) return false;
            }
            return true;
        }
        return false;
    }
    public void move(Position position) {
        Grid grid = this.game.getGrid();

        if(canMove(position)) {
            grid.update(getPosition(), position, this);
        }

    }
}
