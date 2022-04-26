package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Grid;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.*;

public class Bomb extends Bonus{
    public boolean inc;
    public boolean dec;
    public boolean expandRange;
    public boolean number;
    private int explosionRange;
    private int explosionTime;
    public long activation;
    private boolean explosed = false;
    public boolean printed = true;
    public Bomb(Position position,boolean inc,boolean dec,boolean expandRange,boolean number, int level){
        super(position, level);
        this.inc = inc;
        this.dec = dec;
        this.expandRange = expandRange;
        this.number = number;
        this.explosed = true;
    }
    public Bomb(Game game, Position position, int explosionRange, int explosionTime, int level) {
        super(game, position, level);
        this.explosionRange = explosionRange;
        this.explosionTime = explosionTime;
        this.activation = System.currentTimeMillis();
        printed = false;
    }

    public void explode() {
        remove();
        if(!explosed) {
            explosed = true;
            game.getPlayer().addBombToBag();
        }
    }
    public boolean isExplosed() {return explosed;}

    public void isPrinted() { printed = true;}

    public boolean getExpandRange() {return expandRange;}
    public int getExplosionRange() {return explosionRange;}
    public int getExplosionTime() {return explosionTime;}
    public boolean isPrint() {return printed;}
    @Override
    public void takenBy(Player player) {
        if(game == null) {
            remove();
            player.takeBomb(this.inc, this.dec, this.expandRange, this.number);
        }
    }
    public boolean isWalkable(Player player) {
        return true;
    }
}
