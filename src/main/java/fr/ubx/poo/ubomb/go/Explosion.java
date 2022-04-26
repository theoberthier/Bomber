package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Grid;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.Box;
import fr.ubx.poo.ubomb.go.decor.Decor;
import fr.ubx.poo.ubomb.go.decor.Stone;
import fr.ubx.poo.ubomb.go.decor.Tree;
import fr.ubx.poo.ubomb.go.decor.bonus.Bomb;
import fr.ubx.poo.ubomb.go.decor.bonus.Heart;
import fr.ubx.poo.ubomb.go.decor.bonus.Key;

import java.util.List;

public class Explosion extends GameObject {
    private final long activation;
    public Explosion(Game game, Position position, int level) {
        super(game, position, level);
        this.activation = System.currentTimeMillis();
        explode();
    }
    public long getActivation() { return activation; }

    @Override
    public boolean isWalkable(Player player) {
        return false;
    }
    private boolean canExplode(Decor elem) {
        if(elem == null) return true;
        return (elem instanceof Bomb || elem instanceof Heart || elem instanceof Box);
    }

    @Override
    public void explode() {
        Position nextPos = getPosition();
        Decor elem = game.worlds.get(level-1).get(nextPos);

        List<GameObject> gos = game.getGameObjects(nextPos);
        for (GameObject go:gos) {
            go.explode();
        }
        if(elem != null && canExplode(elem) && elem.level == level) elem.explode();

    }

}
