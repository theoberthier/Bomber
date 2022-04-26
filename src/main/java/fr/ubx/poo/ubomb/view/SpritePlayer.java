/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.go.character.Player;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpritePlayer extends Sprite {


    private final ColorAdjust effect = new ColorAdjust();

    public SpritePlayer(Pane layer, Player player) {
        super(layer, null, player);
        effect.setBrightness(0.8);
        updateImage();
    }

    @Override
    public void updateImage() {
        Player player = (Player) getGameObject();
        if (player.isInvisible()){
            Image image = getImage(player.getDirection());
            setImage(image,getEffect());
        }
        else{
            Image image = getImage(player.getDirection());
            setImage(image);
        }

    }

    public ColorAdjust getEffect() {
        return effect;
    }

    public Image getImage(Direction direction) {
        return ImageResource.getPlayer(direction);
    }
}
