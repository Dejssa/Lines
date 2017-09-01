package dejssa.lines.Score;

/**
 * Created by Алексей on 31.08.2017.
 * Container of Score line for top
 */

public class Score {
    private Integer points;
    private String name;

    public Score(Integer points, String name) {
        this.points = points;
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }
}

