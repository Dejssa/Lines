package dejssa.lines.Cash;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Алексей on 30.08.2017.
 */

public class DataBase extends SQLiteOpenHelper {

    private static final String NAME = "GAME_BD";
    private static final Integer VERSION = 1;

    static final String SAVE = "SAVE_TABLE";
    static final String SAVE_FIELD = "SAVE_FIELD";
    static final String SAVE_SCORE = "SAVE_SCORE";

    static final String SCORE_TOP = "SCORE_TOP";
    static final String SCORE_NAME = "SCORE_NAME";
    static final String SCORE_POINTS = "SCORE_POINTS";

    DataBase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+SAVE+" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SAVE_SCORE + " Integer, "
                + SAVE_FIELD + " TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+SCORE_TOP+" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SCORE_NAME + " TEXT, "
                + SCORE_POINTS + " Integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
