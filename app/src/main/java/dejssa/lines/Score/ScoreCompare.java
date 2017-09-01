package dejssa.lines.Score;

import java.util.Comparator;

import dejssa.lines.Score.Score;

public class ScoreCompare implements Comparator<Score> {

    @Override
    public int compare(Score score1, Score score2) {
        if(score1.getPoints().equals(score2.getPoints())){
            return score1.getName().compareTo(score2.getName());
        }
        else
            return score1.getPoints().compareTo(score2.getPoints());
    }
}
