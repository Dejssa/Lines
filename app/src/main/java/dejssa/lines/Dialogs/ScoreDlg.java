package dejssa.lines.Dialogs;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import dejssa.lines.Cash.DataBaseOperations;
import dejssa.lines.MainActivity;
import dejssa.lines.R;
import dejssa.lines.Score.Score;
import dejssa.lines.Score.ScoreHolder;

/**
 * Created by Алексей on 31.08.2017.
 */

public class ScoreDlg extends Dialog {

    private final MainActivity mainActivity;

    ScoreDlg(@NonNull MainActivity mainActivity) {
        super(mainActivity);
        setContentView(R.layout.dlg_score);
        this.mainActivity = mainActivity;

        loadScoreList();
    }

    private void loadScoreList(){

        DataBaseOperations operations = new DataBaseOperations(mainActivity);
        ArrayList<Score> scoreList = operations.restoreScore();

        Log.v("Score - show", ""+scoreList.size());

        RecyclerView scoreListView = findViewById(R.id.score_list);

        ScoreAdapter scoreAdapter = new ScoreAdapter(scoreList);
        scoreListView.setAdapter(scoreAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL,false);
        scoreListView.setLayoutManager(linearLayoutManager);

        Button okey = findViewById(R.id.dlg_score_okey);
        okey.setOnClickListener(view ->  dismiss());
    }

    private class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder>{

        ArrayList<Score> scoreList = new ArrayList<>();

        ScoreAdapter(ArrayList<Score> list) {
            this.scoreList = list;
        }

        @Override
        public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_line,parent,false);
            return new ScoreHolder(view);
        }
        @Override
        public void onBindViewHolder(ScoreHolder holder, int position) {
            Score score = scoreList.get(position);

            holder.UpdateUI(score, mainActivity);
        }

        @Override
        public int getItemCount() {
            return scoreList.size();
        }
    }
}
