package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;


public abstract class GridRepo {

    private final Game game;

    GridRepo(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public abstract Grid load(int level, String name);

    Decor processEntityCode(EntityCode entityCode, Position pos, int level) {
        switch (entityCode) {
            case Empty:
                return null;
            case Stone:
                return new Stone(pos, level);
            case Tree:
                return new Tree(pos, level);
            case Key:
                return new Key(pos, level);
            case Box:
                return new Box(game, pos, level);
            case Heart:
                return new Heart(pos, level);
            case Princess:
                return new Princess(pos, level);
            case BombNumberInc:
                return new Bomb(pos,true,false,false,true, level);
            case BombNumberDec:
                return new Bomb(pos,false,true,false,true, level);
            case BombRangeInc:
                return new Bomb(pos,true,false,true,false, level);
            case BombRangeDec:
                return new Bomb(pos,false,true,true,false, level);
            case DoorNextClosed:
                return new Door(game, pos, false, true, true, level);
            case DoorNextOpened:
                return new Door(game, pos, false, true, false, level);
            case DoorPrevOpened:
                return new Door(game, pos, true, false, false, level);
           case Monster:
                this.game.getMonsters().add(new Monster(this.game,pos,this.game.monsterLives+(level*2), game.monsterVelocity+(level*5), level));
                return null;
            default:
                return null;
                // throw new RuntimeException("EntityCode " + entityCode.name() + " not processed");
        }

    }

}
