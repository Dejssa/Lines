package dejssa.lines;

/**
 * Created by Алексей on 23.08.2017.
 */

public class RandomGen {

    private int BALLS_LIM = 3;

    private char[] big_balls = {'R', 'Y', 'B', 'P', 'G', 'C'}; // C is a black 'Charny' from polish lang
    private char[] little_balls = {'r', 'y', 'b', 'p', 'g', 'c'}; // c is a black 'charny' from polish lang

    public char[] futureBalls(){
        char[] balls = new char[BALLS_LIM];
        for(int i = 0; i < BALLS_LIM; i++){
            balls[i] = little_balls[genZeroTo(little_balls.length)];
        }
        return balls;
    }

    public char[] firstNBalls(int n){
        char[] balls = new char[n];
        for(int i = 0; i < BALLS_LIM; i++){
            balls[i] = big_balls[genZeroTo(little_balls.length)];
        }
        return balls;
    }

    public int genZeroTo(int lim){
        return (int) (Math.random()*lim);
    }
}
