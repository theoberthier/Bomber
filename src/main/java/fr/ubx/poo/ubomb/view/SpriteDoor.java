package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.*;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class SpriteDoor extends Sprite {
    public static final int size = 40;
    private ImageView imageView;
    private Effect effect;

    public SpriteDoor(Pane layer, Image image, GameObject gameObject) {
        super(layer,image,gameObject);
        updateImage();
    }

    @Override
    public void updateImage() {
        Door door = (Door) getGameObject();
        Image image = getImage(door.isClosed());
        setImage(image);
    }

    public Image getImage(boolean open) {return ImageResource.getDoor(open);}


}
