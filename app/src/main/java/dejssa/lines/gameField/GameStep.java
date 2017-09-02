package dejssa.lines.gameField;

import android.util.Log;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Dejssa on 25.08.2017.
 * Find path to selected point and process all actions that connected to it.
 */

public class GameStep {

    private char[][] field;
    private Square[][] square;

    GameStep(Square[][] square) {
        field = new char[square.length][square.length];
        this.square = square;
    }

    int step(int[] a, int[]b){

        for(int i = 0; i < square.length; i++) {
            for(int j = 0; j < square.length; j++){
                this.field[i][j] = square[i][j].getColor();
            }
        }

        class Coords{
            int x;
            int y;
            Coords(int x, int y){
                this.x = x;
                this.y = y;
            }
        }

        Queue<Coords> queue = new PriorityQueue<>(100, (nodeA, nodeB) -> 0);

        queue.add(new Coords(a[0],a[1]));

        Log.v("Step", a[0] + " " + a[1]);

        boolean reach = false;
        while(!queue.isEmpty() || reach){

            Coords s = queue.poll();

            if(s.x > 0 && isFree(s.x-1,s.y)){
                mark(s.x-1, s.y);
                queue.add(new Coords(s.x-1,s.y));
            }
            if(s.x < field.length-1 && isFree(s.x+1,s.y)){
                mark(s.x+1, s.y);
                queue.add(new Coords(s.x+1,s.y));
            }
            if( s.y < field.length-1 && isFree(s.x,s.y+1)){
                mark(s.x,s.y+1);
                queue.add(new Coords(s.x,s.y+1));
            }
            if( s.y > 0 && isFree(s.x,s.y-1)){
                mark(s.x,s.y-1);
                queue.add(new Coords(s.x,s.y-1));
            }
            if(s.x == b[0] && s.y == b[1]){
                reach = true;
                break;
            }

        }

        if(reach) {

            int score = makeBoom(b, square[a[0]][a[1]].getColor());

            Log.v("Score", "" + score);
            return score;
        }
        else
            return -1;
    }

    int makeBoom(int[] a, char ball){
        int score = 0;

        score += diagFirst(a, ball);
        score += diagSecond(a, ball);
        score += horizontal(a, ball);
        score += vertical(a, ball);
        return score;
    }


    private void mark(int x, int y){
        field[x][y] = '1';
    }

    private boolean isFree(int x, int y){
        return field[x][y] != 'R' && field[x][y] != 'C' &&
                field[x][y] != 'B' && field[x][y] != 'P' &&
                field[x][y] != 'G' && field[x][y] != 'Y' && field[x][y] != '1';
    }

    private int diagFirst(int[] a, char ball){
        int score = 1;
        int lim = Math.min(a[0],  a[1]);
        Log.v("ScoreDiag1 - lim", "" + a[0] + a[1]);
        ArrayList<int[]> toDel = new ArrayList<>();
        for(int i = 1; i <= lim; i++){
            Log.v("ScoreDiag1 ball", ""+square[a[0]-i][a[1]-i].getColor());
            if(square[a[0]-i][a[1]-i].getColor() == ball){
                score++;
                toDel.add(new int[]{a[0]-i, a[1]-i});
            }
            else{
                break;
            }

        }
        lim = Math.min(10-a[0],  10-a[1]);
        for(int i = 1; i <= lim-1; i++){
            if(square[a[0]+i][a[1]+i].getColor() == ball){
                score++;
                toDel.add(new int[]{a[0]+i, a[1]+i});
            }
            else{
                break;
            }
        }
        if(score >= 5){
            for(int i = 0; i < toDel.size(); i++){
                int[] del = toDel.get(i);
                square[del[0]][del[1]].setColor(Square.EMPTY);
            }
            return score;
        }
        else
            return 0;
    }

    private int diagSecond(int[] a, char ball){
        int score = 1;
        int lim = 10;
        ArrayList<int[]> toDel = new ArrayList<>();
        for(int i = 1; i < lim; i++){
            System.out.println(a[0]+i);
            if(a[0]+i <= 9 && a[1]-i >=0 && square[a[0]+i][a[1]-i].getColor() == ball){
                score++;
                toDel.add(new int[]{a[0]+i, a[1]-i});
            }
            else{
                break;
            }
        }

        for(int i = 1; i <= lim; i++){

            if(a[1]+i <= 9 && a[0]-i >=0 && square[a[0]-i][a[1]+i].getColor() == ball){
                score++;
                toDel.add(new int[]{a[0]-i, a[1]+i});
            }
            else{
                break;
            }
        }

        if(score >= 5){
            for(int i = 0; i < toDel.size(); i++){
                int[] del = toDel.get(i);
                square[del[0]][del[1]].setColor(Square.EMPTY);
            }
            return score;
        }
        else
            return 0;
    }

    private int horizontal(int[] a, char ball){
        int score = 1;
        ArrayList<int[]> toDel = new ArrayList<>();
        for(int i = 1; i <= 9-a[1]; i++){
            if(a[1]+i <= 9 && square[a[0]][a[1]+i].getColor() == ball){
                score++;
                toDel.add(new int[]{a[0], a[1]+i});
            }
            else{
                break;
            }
        }
        System.out.println(a[1]-1);
        for(int i = 1; i <= a[1]; i++){
            if(a[1]-i >= 0 && square[a[0]][a[1]-i].getColor() == ball){
                score++;
                toDel.add(new int[]{a[0], a[1]-i});
            }
            else{
                break;
            }
        }

        if(score >= 5){
            for(int i = 0; i < toDel.size(); i++){
                int[] del = toDel.get(i);
                square[del[0]][del[1]].setColor(Square.EMPTY);
            }
            return score;
        }
        else
            return 0;
    }

    private int vertical(int[] a, char ball){
        int score = 1;
        ArrayList<int[]> toDel = new ArrayList<>();
        for(int i = 1; i <= 9-a[0]; i++){
            if(a[0]+i <= 9 && square[a[0]+i][a[1]].getColor() == ball){
                score++;
                toDel.add(new int[]{a[0]+i, a[1]});
            }
            else{
                break;
            }
        }

        for(int i = 1; i <= a[0]; i++){
            if(a[0]-i >= 0 && square[a[0]-i][a[1]].getColor() == ball){
                score++;
                toDel.add(new int[]{a[0]-i, a[1]});
            }
            else{
                break;
            }
        }

        if(score >= 5){
            for(int i = 0; i < toDel.size(); i++){
                int[] del = toDel.get(i);
                square[del[0]][del[1]].setColor(Square.EMPTY);
            }
            return score;
        }
        else
            return 0;
    }

}
