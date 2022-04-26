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

public class Character extends GameObject implements Movable {
    private Position position;
    protected int lives;
    protected Direction direction;
    protected boolean moveRequested = false;
    private long invisibilityTime = 0;
    protected boolean invisible = false;

    public Character(Game game,Position position,int lives, int level){
        super(game,position, level);
        this.lives = lives;
        this.direction = Direction.DOWN;
    }
    public Character(Position position,int lives, int level){
        super(position, level);
        this.lives = lives;
        this.direction = Direction.DOWN;
    }

    public Direction getDirection() {
        return direction;
    }


    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {return false;}

    @Override
    public void doMove(Direction direction) {
        // Check if we need to pick something up
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void update(long now) {}

    public void decrementeLives() {
        if (!isInvisible()) {
            this.lives--;
            invisibilityTime = System.currentTimeMillis();
            invisible = true;
            setModified(true);
        }
    }

    public boolean isInvisible() {
        long calcul = game.playerInvisibilityTime + invisibilityTime;
        return calcul > System.currentTimeMillis();
    }

    public boolean getInvisible() {
        return invisible;
    }
    public void setInvisible(boolean bool) {
        invisible = bool;
    }

    @Override
    public void explode() {
        decrementeLives();
        if(this instanceof Monster) {
            if(lives == 0) remove();
        }
    }

    @Override
    public boolean isWalkable(Player player) {
        return false;
    }
}
