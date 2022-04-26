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

public class SpriteBomb extends Sprite {
    public static final int size = 40;
    private ImageView imageView;
    private Effect effect;

    public SpriteBomb(Pane layer, Image image, GameObject gameObject) {
        super(layer,image,gameObject);
        updateImage();
    }

    @Override
    public void updateImage() {
        Bomb bomb = (Bomb) getGameObject();
        int t = (int) (bomb.getExplosionTime() - 1 - ((System.currentTimeMillis() - bomb.activation) / 1000));
        t = t < 0 ? 0 : t;
        Image image = getImage(t);
        setImage(image);
    }

    public Image getImage(int t) {
        return ImageResource.getBomb(t);
    }


}
