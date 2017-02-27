package com.github.keyurgolani.gameoflife;

import android.util.Log;

/**
 * Created by keyurgolani on 2/24/17.
 */

public class LifeUtils {

    private boolean[][] life;
    private int length;
    private int height;
    private int width;

    public LifeUtils(int rows, int cols) {
        life = new boolean[cols][rows];
        for(int i = 0; i < life.length; i++) {
            for(int j = 0; j < life[i].length; j++) {
                life[i][j] = false;
            }
        }
        length = rows * cols;
        height = rows;
        width = cols;
    }

    public void setCell(int x, int y, boolean isAlive) {
        if(isAlive) {
            this.breedCell(x, y);
        } else {
            this.killCell(x, y);
        }
    }

    public void toggleLife(int x, int y) {
        life[x][y] = !life[x][y];
    }

    public void killCell(int x, int y) {
        life[x][y] = false;
    }

    public void breedCell(int x, int y) {
        life[x][y] = true;
    }

    public boolean isAlive(int x, int y) {
        return life[x][y];
    }

    public boolean[][] getLife() {
        return life;
    }

    public void setLife(boolean[][] newLife) {
        this.life = newLife;
    }

    public LifeUtils getDuplicateLife() {
        boolean[][] newLife = new boolean[width][height];
        for(int i = 0; i < life.length; i++) {
            for(int j = 0; j < life[i].length; j++) {
                newLife[i][j] = life[i][j];
            }
        }
        LifeUtils currentGeneration = new LifeUtils(height, width);
        currentGeneration.setLife(newLife);
        return currentGeneration;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
