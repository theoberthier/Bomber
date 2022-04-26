/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Grid;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.Movable;


public class Player extends Character implements Movable {

    private int keys = 0;
    private boolean win = false;
    private int bombBagCapacity;
    private int bombBag;
    private int bombRange;
    public Player(Game game, Position position, int lives, int level) {
        super(game, position, lives, level);
        this.bombBagCapacity = game.bombBagCapacity;
        this.bombBag = game.bombBagCapacity;
        this.bombRange = game.bombRange;
    }

    public int getLives() {return this.lives;}
    public int getKeys() {return this.keys;}
    public int getBombStock() {return this.bombBagCapacity;}
    public int getBombBag() {return this.bombBag;}
    public void addBombToBag() {if(bombBag < bombBagCapacity) bombBag++;}
    public int getBombRange() {return this.bombRange;}

    public void requestOpenDoor() {
        Position next = getPosition();
        switch(getDirection()) {
            case UP:
                next = new Position(getPosition().getX(), getPosition().getY()-1);
            break;
            case DOWN:
                next = new Position(getPosition().getX(), getPosition().getY()+1);
            break;
            case LEFT:
                next = new Position(getPosition().getX()-1, getPosition().getY());
            break;
            case RIGHT:
                next = new Position(getPosition().getX()+1, getPosition().getY());
            break;
        }
        Grid grid = this.game.getGrid();
        Decor item = grid.get(next);
        if(item instanceof Door) {
            if(((Door) item).isClosed()) {
                if(this.keys > 0) {
                    this.keys--;
                    ((Door) item).open();
                    grid.set(next, item);
                    System.out.println(grid.get(next));
                    takeDoor(((Door) item).getTo());
                }
            }
        }
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        Grid grid = this.game.getGrid();
        Decor item = grid.get(nextPos);
        if (item instanceof Bonus && !(item instanceof Door)) {
            item.remove();
            ((Bonus) item).takenBy(this);
        }

        if (item instanceof Box) {
            Position boxNextPos = direction.nextPosition(nextPos);
            if (((Box) item).canMove(boxNextPos)) {
                ((Box) item).move(boxNextPos);
            }
        }
    }

    public final boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Grid grid = this.game.getGrid();
        int width = grid.getWidth();
        int height = grid.getHeight();
        Decor item = grid.get(nextPos);

        if(nextPos.getX() >= width || nextPos.getY() >= height || nextPos.getX() < 0 || nextPos.getY() < 0) return false;

        /* Box Collisions */
        Position boxNextPos = direction.nextPosition(nextPos);
        if(item instanceof Box) return ((Box)item).canMove(boxNextPos);

        /* Collisions */
        return item == null || item.isWalkable(this);
    }


    // décrementation de la position du personnage en fonction d'une direction passer en paramètre, par exemple celle des monstres, le monstre en nous touchant nous fait reculer.
    public void decPosition(Direction dir){
        if (dir == Direction.UP){
            if (canMove(dir))
                this.setPosition(new Position(getPosition().getX(),getPosition().getY() - 1));
        }
        if (dir == Direction.DOWN){
            if (canMove(dir))
                this.setPosition(new Position(getPosition().getX(),getPosition().getY() + 1));
        }
        if (dir == Direction.LEFT){
            if (canMove(dir))
                this.setPosition(new Position(getPosition().getX()-1,getPosition().getY()));
        }
        if (dir == Direction.RIGHT){
            if (canMove(dir))
                this.setPosition(new Position(getPosition().getX()+1,getPosition().getY()));
        }
    }
    @Override
    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }


    @Override
    public boolean isWalkable(Player player) {
        return false;
    }

    public void putBomb() {
        if(bombBag > 0) {
            this.bombBag--;
            this.game.getBombs().add(new Bomb(game, this.getPosition(), this.bombRange, game.bombExplosionTime, game.getLevels()));
        }
    }

    // Example of methods to define by the player
    public void takeDoor(int gotoLevel) {

    }
    public void takeKey(){keys++;}
    public void takeHeart() {lives++;}
    public void takePrincess() {win = true;}
    public void takeBomb(boolean inc, boolean dec, boolean range, boolean number) {
        int n = -1;
        if(inc) n = 1;
        if(range) this.takeRange(n);
        else if(number) this.takeCapacity(n);
    }
    public void takeRange(int n) {this.bombRange += n;}
    public void takeCapacity(int n) {this.bombBagCapacity += n;this.bombBag += n;}

    public boolean isWinner() {
        return this.win;
    }
}
