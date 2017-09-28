package dejssa.lines.gameField;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Arrays;

import dejssa.lines.Cash.BallsMemory;
import dejssa.lines.Cash.DataBaseOperations;
import dejssa.lines.Dialogs.EndOfGameDlg;

import dejssa.lines.MainActivity;
import dejssa.lines.R;
import dejssa.lines.RandomGen;

/**
 * Created by Dejssa on 25.08.2017.
 * Main game class.
 */
public class FieldNoHint extends Field implements View.OnClickListener {

    public FieldNoHint(MainActivity activity, LinearLayout gameField, Square[] balls){

        super(activity, gameField, balls, false);

        this.withHint = false;
    }

    //============================================
    //              GAME RESTORE
    //============================================

    @Override
    public void restoreGame(char[] field, int score, boolean withHint){
        updateScore(score, true);
        unFree = 0;
        int[][] coord = new int[3][2];
        char[] colors = new char[3];
        int count = 0;
        int unFree = 0;
        for(int i = 0; i < SIZE; i++){
            for(int j = 0; j < SIZE; j++){
                char color = field[(i*10)+j];
                if(color >= 'a' && color <= 'z'){
                    coord[count][0] = i;
                    coord[count][1] = j;
                    colors[count] = color;
                    count++;
                }
                unFree+= (color >= 'A' && color <= 'Z') ? 1 : 0;

                squares[i][j].setColor(color);
            }
        }
        changeUnFree(unFree);

        //TODO added
        if(count != 0) {
            restoreFutureBalls(coord, colors);
        }
        else
            generateFuture(withHint);
    }

    @Override
    protected void generateNext(){
        RandomGen gen = new RandomGen();

        int[][] coords = ballsMemory.restoreCoords();
        char[] colors = ballsMemory.restoreColors();


        int crutchLength = 100 - unFree > FUTURE_BALL ? FUTURE_BALL : 100 - unFree;

        Log.v("Length - " , 100 - unFree+"");

        riseBalls(coords, colors, gen, crutchLength);

        generateFuture(withHint);
    }

    //============================================
    //            ON_CLICK SECTION
    //============================================

    @Override
    public void onClick(View view) {
        String id = String.valueOf(view.getId());
        int x;
        int y;
        if(id.length() == 1){
            x = 0;
            y = id.charAt(0) - '0';
        }
        else{
            x = id.charAt(0) - '0';
            y = id.charAt(1) - '0';
        }

        if(active_y == -1 || active_x == -1 ){
            if(squares[x][y].canPickIt())
                selectBall(x,y);
        }
        else{
            if(active_x == x && active_y == y){

                unselectedBall(x,y);

            }else if(squares[x][y].isRaisedBall()){

                selectBall(x,y);

            }else{
                ballsMemory.saveGame(this.toString(), score);
                fieldUpdate(x, y);

                steps++;
                if(steps == 5){
                    steps = 0;
                    saveGame();
                }

            }

        }
        Log.v("Step", "Active x y : " +String.valueOf(active_x) + String.valueOf(active_y));
    }

    //============================================
    //
    //============================================

    /**
     * Same with super class, however the multiplier is 2, not 1.5
     * @param score
     * @param restoring
     */
    @Override
    protected void updateScore(int score, boolean restoring){
        if(restoring){
            this.score = score;
        }
        else{

            int min_score = 5;

            if (score > min_score) {
                double calc = 1.0;
                for (int i = 0; i <= score - min_score; i++) {
                    calc *= 2;
                }
                score = 5 + Double.valueOf(calc).intValue();

            }

            this.score += score;

        }

        activity.updateScore(this.score);
    }

    @Override
    protected void riseBalls( int[][] coord, char[] colors, RandomGen gen, int rising_balls){
        if(coord[0][0] != -1){
            Log.v("Rise", "rising" + rising_balls);
            for(int i = 0; i < rising_balls; i++){

                Log.v("Rise", ""+i);
                //TODO added
                if(squares[coord[i][0]][coord[i][1]].isEmpty()){

                    Log.v("Rise - color", colors[i]+"");

                    int score = gameStep.makeBoom(new int[]{coord[i][0],coord[i][1]},(char)(colors[i]-32));
                    if(score == 0){
                        squares[coord[i][0]][coord[i][1]].setColor((char) (colors[i]-32));
                    }
                    else{
                        squares[coord[i][0]][coord[i][1]].setColor(Square.EMPTY);
                        updateScore(score, false);
                        changeUnFree(-score);
                    }
                }
                else{
                    boolean unDone = true;
                    while(unDone){

                        int x = gen.genZeroTo(SIZE);
                        int y = gen.genZeroTo(SIZE);

                        if(squares[x][y].isEmpty()){

                            squares[x][y].setColor((char) (colors[i]-32));

                            unDone = false;

                        }
                        Log.v("Rise - finding", colors[i]+"");
                    }
                }

            }

        }
        changeUnFree(rising_balls);
        Log.v("Rise", "done");
    }
}
