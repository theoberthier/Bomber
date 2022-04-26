/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.*;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.ubomb.view.ImageResource.*;


public final class SpriteFactory {

    public static Sprite create(Pane layer, GameObject gameObject) {
        if (gameObject instanceof Stone)
            return new Sprite(layer, STONE.getImage(), gameObject);
        if (gameObject instanceof Tree)
            return new Sprite(layer, TREE.getImage(), gameObject);
        if (gameObject instanceof Key)
            return new Sprite(layer, KEY.getImage(), gameObject);
        if (gameObject instanceof Box)
            return new Sprite(layer, BOX.getImage(), gameObject);
        if (gameObject instanceof Heart)
            return new Sprite(layer, HEART.getImage(), gameObject);
        if (gameObject instanceof Bomb){
            if (((Bomb) gameObject).inc && ((Bomb) gameObject).number){
                return new Sprite(layer, BONUS_BOMB_NB_INC.getImage(),gameObject);
            }
            if (((Bomb) gameObject).dec && ((Bomb) gameObject).number){
                return new Sprite(layer, BONUS_BOMB_NB_DEC.getImage(),gameObject);
            }
            if (((Bomb) gameObject).inc && ((Bomb) gameObject).getExpandRange()){
                return new Sprite(layer, BONUS_BOMB_RANGE_INC.getImage(),gameObject);
            }
            if (((Bomb) gameObject).dec && ((Bomb) gameObject).getExpandRange()){
                return new Sprite(layer, BONUS_BOMB_RANGE_DEC.getImage(),gameObject);
            }
            if(((Bomb) gameObject).getExplosionTime() != 0) {
                return new SpriteBomb(layer, BOMB_0.getImage(), gameObject);
            }
        }
        if(gameObject instanceof Door) {
            if(((Door) gameObject).isClosed())
                return new SpriteDoor(layer, DOOR_CLOSED.getImage(),gameObject);
            else return new SpriteDoor(layer, DOOR_OPENED.getImage(),gameObject);
        }
        if(gameObject instanceof Monster){
            return  new SpriteMonster(layer,(Monster) gameObject);
        }
        if(gameObject instanceof Princess) {
            return new Sprite(layer, PRINCESS.getImage(), gameObject);
        }
        if(gameObject instanceof Explosion) {
            return new Sprite(layer, EXPLOSION.getImage(), gameObject);
        }
        throw new RuntimeException("Unsupported sprite for decor " + gameObject);
    }
}
