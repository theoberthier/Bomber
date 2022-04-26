/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.game;


import fr.ubx.poo.ubomb.go.Explosion;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.bonus.Bomb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


public class Game {

    public final int bombBagCapacity;
    public final int bombRange;
    public final int monsterVelocity;
    public final int monsterLives;
    public final int playerLives;
    public final int bombExplosionTime;
    public int levels;
    public final long playerInvisibilityTime;
    public final long monsterInvisibilityTime;
    private Grid grid;
    public final GridRepoFile gridRepo;
    private final String prefix;
    private final Player player;
    private final ArrayList<Monster> monsters = new ArrayList<>();
    private final ArrayList<Bomb> bombs = new ArrayList<>();
    private final ArrayList<Explosion> explosions = new ArrayList<>();
    public final ArrayList<Grid> worlds = new ArrayList<>();
    public final Position playerPosition;

    public Game(String worldPath) {
        try (InputStream input = new FileInputStream(new File(worldPath, "config.properties"))) {
            Properties prop = new Properties();

            // load the configuration file
            prop.load(input);
            bombBagCapacity = Integer.parseInt(prop.getProperty("bombBagCapacity", "3"));
            bombRange = Integer.parseInt(prop.getProperty("bombRange", "1"));
            monsterVelocity = Integer.parseInt(prop.getProperty("monsterVelocity", "10"));
            monsterLives = Integer.parseInt(prop.getProperty("monsterLives", "3"));
            levels = Integer.parseInt(prop.getProperty("levels", "1"));
            playerLives = Integer.parseInt(prop.getProperty("playerLives", "3"));
            playerInvisibilityTime = Long.parseLong(prop.getProperty("playerInvisibilityTime", "4000"));
            monsterInvisibilityTime = Long.parseLong(prop.getProperty("monsterInvisibilityTime", "1000"));
            bombExplosionTime = Integer.parseInt(prop.getProperty("bombExplosionTime", "4"));

            // Load the world
            prefix = prop.getProperty("prefix");
            gridRepo = new GridRepoFile(this,worldPath);
            gridRepo.gridworld(prefix);
            this.grid = worlds.get(this.levels-1);

            // Create the player
            String[] tokens = prop.getProperty("player").split("[ :x]+");
            if (tokens.length != 2)
                throw new RuntimeException("Invalid configuration format");
            playerPosition = new Position(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
            player = new Player(this, playerPosition, playerLives, 0);

        } catch (IOException ex) {
            System.err.println("Error loading configuration");
            throw new RuntimeException("Invalid configuration format");
        }
    }

    public Grid getGrid() {
        return grid;
    }

    // Returns the player, monsters and bombs at a given position
    public List<GameObject> getGameObjects(Position position) {
        List<GameObject> gos = new LinkedList<>();
        if (getPlayer().getPosition().equals(position))
            gos.add(player);
        for (Monster monster:monsters) {
            if(monster.getPosition().equals(position)){
                gos.add(monster);
            }
        }
        for (Bomb bomb:bombs) {
            if(bomb.getPosition().equals(position)){
                gos.add(bomb);
            }
        }
        return gos;
    }

    public void changeWorld(int level){
        this.grid = worlds.get(level-1);
        this.levels = level;
    }

    public int getLevels() {return this.levels;}
    public Player getPlayer() {
        return this.player;
    }
    public ArrayList<Monster> getMonsters(){
        return monsters;
    }
    public ArrayList<Bomb> getBombs(){
        return bombs;
    }
    public ArrayList<Explosion> getExplosions(){
        return explosions;
    }
    public boolean inside(Position position) {
        return true;
    }

}
