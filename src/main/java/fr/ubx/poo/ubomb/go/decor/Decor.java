/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Player;

/***
 * A decor is a static element of the game.
 */
public abstract class Decor extends GameObject {

    public Decor(Game game, Position position, int level) {
        super(game, position, level);
    }

    public Decor(Position position, int level) {
        super(position, level);
    }

    @Override
    public boolean isWalkable(Player player) {
        return false;
    }
}
