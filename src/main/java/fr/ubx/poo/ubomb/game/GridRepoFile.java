package fr.ubx.poo.ubomb.game;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class GridRepoFile extends GridRepo{
    private final ArrayList<EntityCode[][]> levels;
    public GridRepoFile(Game game,String worldPath) throws IOException {
        super(game);
        this.levels = loadWorld(worldPath);
    }

    //chargement d'une grid en fonction de son nom et de son niveau.
    public final Grid load(int level, String name) {
        EntityCode[][] entities =  this.levels.get(level-1);
        if (entities == null) return null;
        int width = entities[0].length;
        int height = entities.length;
        Grid grid = new Grid(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Position position = new Position(i, j);
                EntityCode entityCode = entities[j][i];
                grid.set(position, processEntityCode(entityCode, position, level));
            }
        }
        return grid;
    }
    // créeation de tout les tableau d'entité depuis des fichier texte, en fonction du path donnée,
    public final ArrayList<EntityCode[][]> loadWorld(String worldPath) throws IOException {
        String[] nameList = new File(worldPath).list();
        int nbWorld = nameList.length;
        ArrayList<EntityCode[][]> worlds = new ArrayList<>();

        for (int k = 1; k < nbWorld; k++) {
            StringBuilder chaine = new StringBuilder();
            FileReader in = new FileReader(worldPath + "/level" + k + ".txt");
            int charcode;
            while ((charcode = in.read()) != -1) {
                chaine.append((char) charcode);
            }

            String[] string = chaine.toString().split("\n");
            EntityCode[][] world = new EntityCode[string.length][string[0].length()];
            for (int i = 0; i < string.length; i++) {
                for (int j = 0; j < string[0].length(); j++) {
                    world[i][j] = EntityCode.fromCode(string[i].charAt(j));
                }
            }
            worlds.add(world);
        }
        return worlds;
    }
    //chargement de toute les grids depuis les tableau d'entité présent dans la liste générer avec loadworld
    public void gridworld(String name) throws IOException {
        for (int i = 0; i < levels.size(); i++) {
            this.getGame().worlds.add(load(i+1,name));
        }
    }
}
