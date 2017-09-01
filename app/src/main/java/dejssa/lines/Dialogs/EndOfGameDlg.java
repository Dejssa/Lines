package dejssa.lines.Dialogs;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import dejssa.lines.Cash.DataBaseOperations;
import dejssa.lines.MainActivity;
import dejssa.lines.R;
import dejssa.lines.Score.Score;

/**
 * Created by Алексей on 31.08.2017.
 */

public class EndOfGameDlg extends Dialog{

    private MainActivity context;
    private final Integer score;

    public EndOfGameDlg(@NonNull MainActivity context, Integer score) {
        super(context);
        setContentView(R.layout.dlg_save_score);
        this.context = context;
        this.score = score;

        setCancelable(false);

        bindToButton();
    }

    private void bindToButton() {
        Button ok = findViewById(R.id.dlg_save_ok);
        EditText name = findViewById(R.id.dlg_save_name);
        context.setCustomFont(name);

        DataBaseOperations operations = new DataBaseOperations(context);

        List<Score> scoreList = operations.restoreScore();


        ok.setOnClickListener(view -> {
            String nameStr = name.getText().toString();
            if(name.length() > 0) {
                scoreList.add(new Score(score, nameStr));
                operations.saveScore(scoreList);
                dismiss();
                new MenuDlg(context, false);
            }
            else{
                Toast.makeText(context, "Enter the name", Toast.LENGTH_LONG).show();
            }
        });
    }


}
