package dejssa.lines.Score;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import dejssa.lines.MainActivity;
import dejssa.lines.R;

/**
 * Created by Алексей on 31.08.2017.
 */

public class ScoreHolder extends RecyclerView.ViewHolder{

    private TextView name;
    private TextView points;

    public ScoreHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.score_name);
        points = itemView.findViewById(R.id.score_points);
    }

    public void UpdateUI(Score score, MainActivity activity){

        Log.v("Score - setting", score.getName() + " " + score.getPoints().toString());

        name.setText(score.getName());
        points.setText(score.getPoints().toString());

        AssetManager am = activity.getApplicationContext().getAssets();

        Typeface fontAkrobat = Typeface.createFromAsset(am,  "fonts/akrobat_light.otf");

        name.setTypeface(fontAkrobat);
        points.setTypeface(fontAkrobat);

    }
}