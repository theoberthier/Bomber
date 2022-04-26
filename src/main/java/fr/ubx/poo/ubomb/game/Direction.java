/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.game;

import java.util.Random;

public enum Direction {
    UP {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.getX(), pos.getY() - delta);
        }
    },
    RIGHT {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.getX() + delta, pos.getY());
        }
    },
    DOWN {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.getX(), pos.getY() + delta);
        }
    },
    LEFT {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.getX() - delta, pos.getY());
        }
    },
    ;

    private static final Random randomGenerator = new Random();

    public static Direction random(Direction current) {
        int i = randomGenerator.nextInt(values().length);
        if (current != values()[i]){
            return values()[i];
        }
        return current;
    }

    public abstract Position nextPosition(Position pos, int delta);

    final public Position nextPosition(Position pos) {
        return nextPosition(pos, 1);
    }

}