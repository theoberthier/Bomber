/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;

public class Stone extends Decor {
    public Stone(Position position, int level) {
        super(position, level);
    }
    @Override
    public void explode() {}
}
