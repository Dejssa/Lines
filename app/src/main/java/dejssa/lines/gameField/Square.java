package dejssa.lines.gameField;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;

import dejssa.lines.R;

public class Square extends android.support.v7.widget.AppCompatImageView {

    public static final char EMPTY = '0';
    public final char RAISED_BLACK = 'C';
    public final char RAISED_BLUE = 'B';
    public final char RAISED_YELLOW = 'Y';
    public final char RAISED_GREEN = 'G';
    public final char RAISED_RED = 'R';
    public final char RAISED_PURPLE = 'P';

    AnimationDrawable animationDrawable;

    private char color = EMPTY;

    public Square(Context context) {
        super(context);
    }

    public boolean isEmpty(){
        return color == EMPTY;
    }

    public boolean canPickIt(){
        return !isEmpty() && isRaisedBall();
    }

    public boolean isRaisedBall(){
        return color == RAISED_BLACK || color == RAISED_BLUE || color == RAISED_GREEN ||
                color == RAISED_PURPLE || color == RAISED_RED || color == RAISED_YELLOW;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
        changeColor(color);
    }

    private void changeColor(char color){
        switch (color){
            case RAISED_RED:
                setBackGround(R.drawable.sq_ball_red);
                break;
            case RAISED_BLUE:
                setBackGround(R.drawable.sq_ball_blue);
                break;
            case RAISED_PURPLE:
                setBackGround(R.drawable.sq_ball_purple);
                break;
            case RAISED_BLACK:
                setBackGround(R.drawable.sq_ball_black);
                break;
            case RAISED_GREEN:
                setBackGround(R.drawable.sq_ball_green);
                break;
            case RAISED_YELLOW:
                setBackGround(R.drawable.sq_ball_yellow);
                break;

            case 'r':
                setBackGround(R.drawable.l_sq_ball_red);
                break;
            case 'b':
                setBackGround(R.drawable.l_sq_ball_blue);
                break;
            case 'p':
                setBackGround(R.drawable.l_sq_ball_purple);
                break;
            case 'c':
                setBackGround(R.drawable.l_sq_ball_black);
                break;
            case 'g':
                setBackGround(R.drawable.l_sq_ball_green);
                break;
            case 'y':
                setBackGround(R.drawable.l_sq_ball_yellow);
                break;

            case EMPTY:
                setBackGround(R.drawable.square);
                break;
            default:
                setBackGround(R.drawable.square);
                break;

        }
    }

    private void setBackGround(int drawable){
        this.setBackgroundResource(drawable);
    }

    public void selectBall(){
        animationDrawable = (AnimationDrawable) this.getBackground();
        animationDrawable.start();
    }

    public void unselectBall(){
        animationDrawable.stop();
        this.setBackgroundDrawable(null);
        setColor(color);
    }
}