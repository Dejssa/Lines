package dejssa.lines.gameField;

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
public class Field implements View.OnClickListener {

    protected final Integer SIZE = 10;

    public static final Integer FUTURE_BALL = 3;

    protected int steps = 0;
    protected int unFree = 0;

    protected Square[] futureBalls;
    protected Square[][] squares = new Square[SIZE][SIZE];

    protected int score = 0;

    protected int active_x = -1;
    protected int active_y = -1;

    protected final MainActivity activity;

    protected static BallsMemory ballsMemory;
    protected GameStep gameStep;

    protected boolean withHint;


    public Field(MainActivity activity, LinearLayout gameField, Square[] balls){

        this.activity = activity;

        this.withHint = true;

        ballsMemory = new BallsMemory(FUTURE_BALL);

        futureBalls = balls;

        createField(gameField);

        generateFirst();

        gameStep = new GameStep(squares);

        generateNext();
    }

    public Field(MainActivity activity, LinearLayout gameField, Square[] balls, boolean withHint){

        this.activity = activity;

        ballsMemory = new BallsMemory(FUTURE_BALL);

        futureBalls = balls;

        createField(gameField);

        generateFirst();

        gameStep = new GameStep(squares);

        generateNext();
    }

    //done
    @Override
    public String toString(){
        StringBuilder fieldToSave = new StringBuilder(100);

        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++){
                fieldToSave.append(squares[i][j].getColor());
            }
        }

        return fieldToSave.toString();
    }
    //done
    protected void createField(LinearLayout gameField){
        LinearLayout[] field = new LinearLayout[SIZE];

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, 1f);

        for(int i = 0;i < SIZE; i++){

            field[i] = new LinearLayout(activity);
            field[i].setLayoutParams(params);

            for(int j = 0;j < SIZE; j++){

                squares[i][j] = new Square(activity);
                squares[i][j].setLayoutParams(params);
                squares[i][j].setBackgroundResource(R.drawable.square);
                squares[i][j].setOnClickListener(this);
                squares[i][j].setId(Integer.parseInt(String.valueOf(i)+String.valueOf(j)));

                field[i].addView(squares[i][j]);

            }
            gameField.addView(field[i]);
        }
    }

    protected void generateFirst(){

        RandomGen gen = new RandomGen();

        char[] colors = gen.firstNBalls(3);

        for(int i = 0; i < 3;){

            int x = gen.genZeroTo(SIZE);
            int y = gen.genZeroTo(SIZE);

            Log.v("Gen", x+ " " + y);

            if(!squares[x][y].isRaisedBall()){
                squares[x][y].setColor(colors[i]);
                i++;
            }

        }
    }

    //done
    public void undoStep(){
        Object[] game_save = ballsMemory.restoreGame();
        if(game_save != null) {
            Log.v("Step",String.valueOf(game_save[0]));
            restoreGame(String.valueOf(game_save[0]).toCharArray(), (Integer) game_save[1], withHint);
            Log.v("Step", "Restoring");
        }
    }

    //done
    public void saveGame(){

        DataBaseOperations operations = new DataBaseOperations(activity);

        operations.saveGame(this.toString(), score, withHint);
    }

    //============================================
    //              GAME RESTORE
    //============================================
    //TODO CHECK IT
    public void restoreGame(char[] field, int score, boolean withHint){
        this.withHint = withHint;
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
        restoreFutureBalls(coord, colors);
    }

    //done
    protected void restoreFutureBalls(int[][] coords, char[] colors){
        Log.v("Restore - colors" ,Arrays.toString(colors));
        for(int i = 0; i < colors.length; i++)
            Log.v("Restore - coords" ,Arrays.toString(coords[i]));
        ballsMemory.saveCoords(coords);
        ballsMemory.saveColors(colors);
        for(int i = 0; i < colors.length; i++){
            futureBalls[i].setColor((char) (colors[i]-32));
        }
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

    protected void fieldUpdate(int x, int y){
        char saveColor = squares[active_x][active_y].getColor();
        squares[active_x][active_y].setColor(Square.EMPTY);
        int score = gameStep.step(new int[]{active_x, active_y}, new int[]{x, y}, saveColor);
        if (score == 0) {
            Log.v("Step - placing", "placing on " + x + " " + y);
            squares[x][y].setColor(saveColor);

            active_y = active_x = -1;
            generateNext();

        } else if(score >= 5){

            active_y = active_x = -1;

            updateScore(score,false);

            changeUnFree(-score);
        }
        else{
            squares[active_x][active_y].setColor(saveColor);
            Toast.makeText(activity, "Cannot reach", Toast.LENGTH_SHORT).show();

        }
    }

    //done
    protected void selectBall(int x, int y){
        if(active_x != -1 && active_y != -1){
            squares[active_x][active_y].unselectBall();
        }
        active_x = x;
        active_y = y;
        squares[active_x][active_y].selectBall();
    }

    protected void unselectedBall(int x, int y){
        squares[x][y].unselectBall();
        active_x = active_y = -1;
    }

    //============================================
    //
    //============================================
    //done
    protected void updateScore(int score, boolean restoring){
        if(restoring){
            this.score = score;
        }
        else{

            int min_score = 5;

            if (score > min_score) {
                double calc = 1.0;
                for (int i = 0; i <= score - min_score; i++) {
                    calc *= 1.5;
                }
                score = 5 + Double.valueOf(calc).intValue();

            }

            this.score += score;

        }

        activity.updateScore(this.score);
    }
    //TODO CHECK IT

    protected void generateNext(){
        RandomGen gen = new RandomGen();

        int[][] coords = ballsMemory.restoreCoords();
        char[] colors = ballsMemory.restoreColors();


        int crutchLength = 100 - unFree > FUTURE_BALL ? FUTURE_BALL : 100 - unFree;

        Log.v("Length - " , 100 - unFree+"");

        riseBalls(coords, colors, gen, crutchLength);

        generateFuture(withHint);
    }

    protected void generateFuture(boolean withHint){
        RandomGen gen = new RandomGen();

        int length = 100 - (unFree+3) > FUTURE_BALL ? FUTURE_BALL : 100 - (unFree+3);

        int[][] coords = ballsMemory.getClearCoords();
        char[] colors = gen.futureBalls();

        ballsMemory.saveColors(colors);

        for(int i = 0; i < length;){

            int x = gen.genZeroTo(SIZE);
            int y = gen.genZeroTo(SIZE);

            Log.v("Gen" + length, x+ " " + y);

            if(squares[x][y].isEmpty()){

                //TODO added
                if(withHint)
                    squares[x][y].setColor(colors[i]);

                futureBalls[i].setColor((char)(colors[i]-32));

                coords[i][0] = x;
                coords[i][1] = y;

                i++;

            }

        }

        ballsMemory.saveCoords(coords);
    }

    /*
    protected void generateNext(){
        RandomGen gen = new RandomGen();

        int[][] coords = ballsMemory.restoreCoords();
        char[] colors = ballsMemory.restoreColors();

        int length = 100 - (unFree+3) > FUTURE_BALL ? FUTURE_BALL : 100 - (unFree+3);
        int crutchLength = 100 - unFree > FUTURE_BALL ? FUTURE_BALL : 100 - unFree;

        Log.v("Length - " , 100 - unFree+"");

        riseBalls(coords, colors, gen, crutchLength);

        colors = gen.futureBalls();
        ballsMemory.saveColors(colors);

        for(int i = 0; i < length;){

            int x = gen.genZeroTo(SIZE);
            int y = gen.genZeroTo(SIZE);

            Log.v("Gen" + length, x+ " " + y);

            if(squares[x][y].isEmpty()){

                squares[x][y].setColor(colors[i]);
                futureBalls[i].setColor((char)(colors[i]-32));

                coords[i][0] = x;
                coords[i][1] = y;

                i++;

            }

        }

        ballsMemory.saveCoords(coords);

    }*/

    protected void riseBalls( int[][] coord, char[] colors, RandomGen gen, int rising_balls){
        if(coord[0][0] != -1){
            Log.v("Rise", "rising" + rising_balls);
            for(int i = 0; i < rising_balls; i++){

                Log.v("Rise", ""+i);

                if(squares[coord[i][0]][coord[i][1]].getColor() == colors[i]){

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
    //done
    protected void changeUnFree(int i){
        unFree+=i;
        Log.v("Field", unFree + " is filled");
        if(unFree >= 100){
            Toast.makeText(activity, "You earn " + score, Toast.LENGTH_LONG).show();
            new EndOfGameDlg(activity, score).show();
        }
        if(i <= -5) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
