package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Grid;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.decor.Box;
import fr.ubx.poo.ubomb.go.decor.Decor;
import fr.ubx.poo.ubomb.go.decor.Stone;
import fr.ubx.poo.ubomb.go.decor.Tree;
import fr.ubx.poo.ubomb.go.decor.bonus.Door;


public class Monster extends Character implements Movable {
    private long RefreshTime = 0;
    public final int velocity;

    public Monster(Game game,Position position,int lives, int velocity, int level) {
        super(game,position,lives, level); this.velocity = velocity;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Grid grid = this.game.getGrid();
        int width = grid.getWidth();
        int height = grid.getHeight();
        Decor item = grid.get(nextPos);
        // mouvement limité à la taille de la fenêtre
        if(nextPos.getX() >= width || nextPos.getY() >= height || nextPos.getX() < 0 || nextPos.getY() < 0)
            return false;
        // Collisions
        return !(item instanceof Box) && !(item instanceof Stone) && !(item instanceof Tree) && !(item instanceof Door);
    }

    @Override
    public void requestMove(Direction direction) {
        if(level == game.levels) {
            if (direction != this.direction) {
                this.direction = direction;
                setModified(true);
            }
            moveRequested = true;
        }
    }


    @Override
    public void update(long now) {
        //si le moveRequest est à true alors on fait changer la direction du monstre
        if (moveRequested) {
            requestMove(Direction.random(getDirection()));
            if (canMove(direction)) {
                doMove(direction);
            }
            moveRequested = false;
        }
        //le taux de rachraichissement des monstres ce fait toutes les secondes / par la velocité
        if (now - RefreshTime >= 10_000_000_000L/velocity){
            moveRequested = true;
            RefreshTime = now;
        }

    }
}
