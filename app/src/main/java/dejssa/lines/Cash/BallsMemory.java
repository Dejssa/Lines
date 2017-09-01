package dejssa.lines.Cash;

import android.util.Log;

import java.util.Arrays;

import dejssa.lines.gameField.Square;

/**
 * Created by Алексей on 23.08.2017.
 */

public class BallsMemory {

    public static final int FUTURE_BALLS_AMOUNT = 3;

    private int[][] coord = {{-1,-1}, {-1,-1}, {-1,-1}};
    private char[] colors = {Square.EMPTY,Square.EMPTY,Square.EMPTY};

    public void save(int[][] coord){
        this.coord = coord;
    }

    public int[][] restoreCoord(){
        return coord;
    }

    public void saveColors(char[] colors) {
        this.colors = colors;
    }

    public char[] restoreColors() {
        return colors;
    }

    private final int SIZE = 10;

    private char[][] memory;

    public void saveField(Square[][] squares){
        memory = new char[SIZE][SIZE];
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++){
                this.memory[i][j] = squares[i][j].getColor();
            }
        }
        for (char[] aMemory : memory)
            Log.v("Step", Arrays.toString(aMemory));
    }

    public void restoreMemory( Square[][] squares){
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++){
                squares[i][j].setColor(memory[i][j]);
            }
        }
    }
}
