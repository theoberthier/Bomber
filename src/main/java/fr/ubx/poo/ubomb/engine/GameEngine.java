/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.engine;

import fr.ubx.poo.ubomb.game.Direction;
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
import fr.ubx.poo.ubomb.go.*;
import fr.ubx.poo.ubomb.go.decor.bonus.Door;
import fr.ubx.poo.ubomb.go.decor.bonus.Key;
import fr.ubx.poo.ubomb.view.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;

/* Time */
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private int level;
    private final Player player;
    private final List<Sprite> sprites = new LinkedList<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private final Stage stage;
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private int initialWorld = 1;


    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.stage = stage;
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        this.level = game.getLevels();
        initialize();
        buildAndSetGameLoop();
    }

    private void initialize() {
        Group root = new Group();
        layer = new Pane();

        int height = game.getGrid().getHeight();
        int width = game.getGrid().getWidth();
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        // Create sprites
        setSprites();

    }

    void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);
                // Do actions
                update(now);
                createNewBombs(now);
                checkCollision(now);
                checkExplosions();

                // Graphic update
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

    private boolean createExplosion(Position nextPos, Bomb bomb) {
        Decor elem = game.worlds.get(bomb.level-1).get(nextPos);
        if(elem instanceof Tree || elem instanceof Stone || elem instanceof Key || elem instanceof Door) return false;
        Explosion exp = new Explosion(game, nextPos, bomb.level);
        game.getExplosions().add(exp);
        if(bomb.level == game.levels) sprites.add(SpriteFactory.create(layer, exp));
        return !(elem instanceof Box);
    }

    private void explosion(Bomb bomb) {
        createExplosion(bomb.getPosition(), bomb);
        Position nextPos;

        // Gauche
        for(int i = 1; i < bomb.getExplosionRange()+1; i++) {
            nextPos = new Position(bomb.getPosition().getX() - i, bomb.getPosition().getY());
            if(!createExplosion(nextPos, bomb)) break;
        }
        // Droite
        for(int i = 1; i < bomb.getExplosionRange()+1; i++) {
            nextPos = new Position(bomb.getPosition().getX() + i, bomb.getPosition().getY());
            if(!createExplosion(nextPos, bomb)) break;
        }
        // Haut
        for(int i = 1; i < bomb.getExplosionRange()+1; i++) {
            nextPos = new Position(bomb.getPosition().getX(), bomb.getPosition().getY() - i);
            if(!createExplosion(nextPos, bomb)) break;
        }
        // Bas
        for(int i = 1; i < bomb.getExplosionRange()+1; i++) {
            nextPos = new Position(bomb.getPosition().getX(), bomb.getPosition().getY() + i);
            if(!createExplosion(nextPos, bomb)) break;
        }
    }

    private void checkExplosions() {
        for(Bomb bomb:game.getBombs()) {
            if(bomb.getExplosionTime() == game.bombExplosionTime) {
                if(System.currentTimeMillis() >= bomb.activation + ((long)bomb.getExplosionTime()*1000)) {
                    bomb.explode();
                }
            }
        }
        for(Bomb bomb:game.getBombs()) {
            if(bomb.isExplosed()) {
                explosion(bomb);
                game.getBombs().remove(bomb);
                break;
            }
        }
        for(Explosion explosion:game.getExplosions()) {
            if(System.currentTimeMillis() >= explosion.getActivation() + 100) {
                explosion.remove();
                game.getExplosions().remove(explosion);
                break;
            }
        }
    }

    private void createNewBombs(long now) {
        for(Bomb bomb:game.getBombs()) {
            if(bomb.getExplosionTime() == game.bombExplosionTime) {
                if(!bomb.isPrint()) {
                    sprites.add(SpriteFactory.create(layer, bomb));
                    bomb.isPrinted();
                }
                bomb.setModified(true);
            }
        }
    }

    private void checkCollision(long now) {
        // gestion colisions entre personnage et monstres si oui on décremente la vie du personnage.
        List<GameObject> gos = game.getGameObjects(player.getPosition());
        for (Monster monster:game.getMonsters()) {
            if(monster.level == game.levels && gos.contains(monster)){
                    if(!player.getInvisible())
                        player.decPosition(monster.getDirection());
                    player.decrementeLives();
                }
        }
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        } else if (input.isMoveDown()) {
            player.requestMove(Direction.DOWN);
        } else if (input.isMoveLeft()) {
            player.requestMove(Direction.LEFT);
        } else if (input.isMoveRight()) {
            player.requestMove(Direction.RIGHT);
        } else if (input.isMoveUp()) {
            player.requestMove(Direction.UP);
            input.clear();
        } else if (input.isKey()) {
            player.requestOpenDoor();
        }
        else if (input.isBomb()) {
            player.putBomb();
        }

        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }

    private void update(long now) {
        Decor elem = game.getGrid().get(game.getPlayer().getPosition());
        if(elem instanceof Door) {
            game.changeWorld(((Door)elem).getTo());
            changeWorld();
        }

        player.update(now);
        if (!player.isInvisible()){
            player.setInvisible(false);
            player.setModified(true);
        }

        if (player.getLives() == 0) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }

        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }

        for (Monster monster:game.getMonsters()) {
            if(game.levels == monster.getLevel()){
                monster.update(now);
            }
        }
    }

    public void cleanupSprites() {
        sprites.forEach(sprite -> {
            if (sprite.getGameObject().isDeleted()) {
                game.getGrid().remove(sprite.getPosition());
                cleanUpSprites.add(sprite);
            }
        });
        cleanUpSprites.forEach(Sprite::remove);
        sprites.removeAll(cleanUpSprites);
        cleanUpSprites.clear();
    }

    private void render() {
        sprites.forEach(Sprite::render);
    }

    public void start() {
        gameLoop.start();
    }

    private void setSprites() {
        cleanUpSprites.addAll(sprites);
        for (Decor decor : game.getGrid().values()) {
            sprites.add(SpriteFactory.create(layer, decor));
            decor.setModified(true);
        }
        for (Monster monster:this.game.getMonsters()) {
            if(monster.level == game.levels)
                sprites.add(new SpriteMonster(layer,monster));
        }
        for (Bomb bomb:this.game.getBombs()) {
            if(bomb.level == game.levels)
                sprites.add(SpriteFactory.create(layer,bomb));
        }
        for (Explosion explosion:this.game.getExplosions()) {
            if(explosion.level == game.levels)
                sprites.add(SpriteFactory.create(layer,explosion));
        }
        if(initialWorld == 1) {
            player.setPosition(game.playerPosition);
            sprites.add(new SpritePlayer(layer, player));
        }
        else {
            Grid ogrid = game.worlds.get(game.levels-(game.levels-level)-1);
            level = game.levels;
            Position doorPos = player.getPosition();
            Decor d = ogrid.get(doorPos);
            if(((Door)d).isPrec()) {
                sprites.forEach(sprite -> {
                    GameObject g = sprite.getGameObject();
                    if(g instanceof Door) {
                        if(((Door)g).isNext()) {
                            int x = g.getPosition().getX();
                            int y = g.getPosition().getY() + 1;
                            player.setPosition(new Position(x,y));
                        }
                    }
                });
            }
            else if(((Door)d).isNext()){
                sprites.forEach(sprite -> {
                    GameObject g = sprite.getGameObject();
                    if(g instanceof Door) {
                        if(((Door)g).isPrec()) {
                            int x = g.getPosition().getX();
                            int y = g.getPosition().getY() - 1;
                            player.setPosition(new Position(x,y));
                        }
                    }
                });
            }
            if(player.isModified()) {
                sprites.add(new SpritePlayer(layer, player));
            }
        }
    }
    public void changeWorld() {
        initialWorld = 0;
        initialize();
    }
}
