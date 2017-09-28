package dejssa.lines;

import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import dejssa.lines.Cash.DataBaseOperations;
import dejssa.lines.Dialogs.MenuDlg;
import dejssa.lines.gameField.Field;
import dejssa.lines.gameField.FieldNoHint;
import dejssa.lines.gameField.Square;

public class MainActivity extends AppCompatActivity {

    private Field fieldNoHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setVersion();

        new MenuDlg(this, false).show();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    public void startGame(boolean withHint) {
        LinearLayout gameField = bindInterface();
        Square[] futureBalls = createFutureBalls();
        fieldViews();
        if(withHint)
            fieldNoHint = new Field(this, gameField, futureBalls);
        else
            fieldNoHint = new FieldNoHint(this, gameField, futureBalls);
    }

    private void setVersion() {
        TextView versionTxt = (TextView) findViewById(R.id.main_version);
        versionTxt.setText(BuildConfig.VERSION_NAME);
    }

    private LinearLayout bindInterface(){

        LinearLayout gameField = (LinearLayout) findViewById(R.id.main_game_field);

        gameField.removeAllViews();

        ViewGroup.LayoutParams layoutParams = gameField.getLayoutParams();

        layoutParams.height = gameField.getWidth();

        gameField.setLayoutParams(layoutParams);

        LinearLayout statusLayout = (LinearLayout) findViewById(R.id.main_status);
        LinearLayout mainContol = (LinearLayout) findViewById(R.id.main_control);

        statusLayout.setVisibility(View.VISIBLE);
        mainContol.setVisibility(View.VISIBLE);

        return gameField;

    }

    private Square[] createFutureBalls(){
        Square[] futureBalls = new Square[FieldNoHint.FUTURE_BALL];

        LinearLayout futureBallsLay = (LinearLayout) findViewById(R.id.futureBallsLay);

        futureBallsLay.removeAllViews();

        ViewGroup.LayoutParams ballsParams = futureBallsLay.getLayoutParams();
        ballsParams.height = futureBallsLay.getWidth() / FieldNoHint.FUTURE_BALL;

        for(int i = 0; i < futureBalls.length; i++){
            futureBalls[i] = new Square(this);
            futureBalls[i].setLayoutParams(ballsParams);
            futureBalls[i].setBackgroundResource(R.drawable.square);
            futureBallsLay.addView(futureBalls[i]);
        }

        return futureBalls;
    }

    private void fieldViews(){

        Button undo = (Button) findViewById(R.id.menu_undo);
        setCustomFont(undo);
        undo.setOnClickListener(view -> undoStep());

        Button menu = (Button) findViewById(R.id.menu);
        setCustomFont(menu);
        menu.setOnClickListener(view -> new MenuDlg(this, true).show());

        Button save = (Button) findViewById(R.id.save_game);
        setCustomFont(save);
        save.setOnClickListener(view -> saveGame());

        TextView scoreText = (TextView) findViewById(R.id.score);
        setCustomFont(scoreText, true);
        updateScore(0);
    }

    private void saveGame(){
        fieldNoHint.saveGame();
    }

    public void restoreGame(){
        DataBaseOperations operations = new DataBaseOperations(this);

        Object[] savedGame = operations.loadGame();
        if(savedGame != null) {
            startGame(false);
            Log.v("Restore", savedGame[0].toString());
            fieldNoHint.restoreGame(savedGame[0].toString().toCharArray(), (Integer) savedGame[1], (Boolean) savedGame[2]);
        }
        else{
            Toast.makeText(this, "No saved game found", Toast.LENGTH_LONG).show();;
        }
    }

    private void undoStep(){
        fieldNoHint.undoStep();
    }

    public void updateScore(int score){
        TextView scoreText = (TextView) findViewById(R.id.score);
        scoreText.setText(String.valueOf(score));
    }

    //============================================
    //              FONT CUSTOMISE
    //============================================

    public void setCustomFont(Button btn){

        AssetManager am = this.getApplicationContext().getAssets();

        Typeface fontAkrobat = Typeface.createFromAsset(am,  "fonts/akrobat_light.otf");

        btn.setTypeface(fontAkrobat);

    }

    public void setCustomFont(EditText btn){

        AssetManager am = this.getApplicationContext().getAssets();

        Typeface fontAkrobat = Typeface.createFromAsset(am,  "fonts/akrobat_light.otf");

        btn.setTypeface(fontAkrobat);

    }

    public void setCustomFont(CheckBox cbx){

        AssetManager am = this.getApplicationContext().getAssets();

        Typeface fontAkrobat = Typeface.createFromAsset(am,  "fonts/akrobat_light.otf");

        cbx.setTypeface(fontAkrobat);

    }

    public void setCustomFont(TextView btn, boolean bold){

        AssetManager am = this.getApplicationContext().getAssets();
        Typeface fontAkrobat;

        if(bold){
            fontAkrobat = Typeface.createFromAsset(am,  "fonts/akrobat_semi_bold.otf");

        }
        else
            fontAkrobat = Typeface.createFromAsset(am,  "fonts/akrobat_light.otf");

        btn.setTypeface(fontAkrobat);

    }


}




