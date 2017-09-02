package dejssa.lines.Cash;

import android.util.Log;

import java.util.Arrays;

import dejssa.lines.gameField.Square;

/**
 * Created by Алексей on 23.08.2017.
 */

public class BallsMemory {



    private int size;

    private int[][] coord;// = {{-1,-1}, {-1,-1}, {-1,-1}};
    private char[] colors;// = {Square.EMPTY,Square.EMPTY,Square.EMPTY};
    private Object[] game_save;

    public BallsMemory(int size) {
        this.size = size;
        coord = new int[size][2];
        colors = new char[size];
        for(int i = 0; i < size; i++){
            coord[i] = new int[]{-1,-1};
            colors[i] = Square.EMPTY;
        }

    }

    public void saveCoords(int[][] coord){
        this.coord = coord;
    }

    public int[][] restoreCoords(){
        return coord;
    }

    public void saveColors(char[] colors) {
        this.colors = colors;
    }

    public char[] restoreColors() {
        return colors;
    }

    public void saveGame(String field, Integer score){
        game_save = new Object[]{field, score};
    }

    public Object[] restoreGame(){
        return game_save;
    }
}
