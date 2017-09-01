
package dejssa.lines.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;


import dejssa.lines.BuildConfig;
import dejssa.lines.MainActivity;
import dejssa.lines.R;

/**
 * Created by Dejssa on 22.08.2017.
 * Main menu dialog, here is a process of 4 buttons Load Game, New Game, Score, Exit
 */

public class MenuDlg extends Dialog{

    private final MainActivity activity;

    public MenuDlg(@NonNull MainActivity activity, boolean isCancelable) {
        super(activity);
        this.activity = activity;
        setCancelable(isCancelable);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_menu);

        bindInterface();
    }

    private void bindInterface() {

        Button restoreBtn = findViewById(R.id.menu_restore_game);
        activity.setCustomFont(restoreBtn);
        restoreBtn.setOnClickListener(view -> {
            activity.restoreGame();
            dismiss();
        });

        Button newGameBtn = findViewById(R.id.menu_new_game);
        activity.setCustomFont(newGameBtn);
        newGameBtn.setOnClickListener(view -> {
            activity.startGame();
            dismiss();
        });

        Button scoreBtn = findViewById(R.id.menu_score);
        activity.setCustomFont(scoreBtn);
        scoreBtn.setOnClickListener(view -> {
            ScoreDlg scoreDlg = new ScoreDlg(activity);
            scoreDlg.show();
        });

        Button exitBtn = findViewById(R.id.menu_exit);
        activity.setCustomFont(exitBtn);
        exitBtn.setOnClickListener(view -> {
            activity.finish();
            System.exit(0);
        });
    }

}
