package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Door extends Bonus {
    private boolean prec;
    private boolean next;
    private boolean closed;
    private int to;
    public Door(Game game, Position position, boolean prec, boolean next, boolean closed, int level){
        super(game, position, level);
        this.prec = prec;
        this.next = next;
        this.closed = closed;
        if(next) {this.to = level+1;}
        else {this.to = level-1;}
    }
    @Override
    public void takenBy(Player player) {
        player.takeDoor(this.to);
    }
    public boolean isPrec() {return this.prec;}
    public boolean isNext() {return this.next;}
    public boolean isClosed() {return this.closed;}
    public int getTo() {return this.to;}
    public void open() {closed = false; setModified(true);}
    public boolean isWalkable(Player player) {
        return !closed;
    }


}
