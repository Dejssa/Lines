package dejssa.lines.Cash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Алексей on 30.08.2017.
 */

public class DataBase extends SQLiteOpenHelper {

    private static final String NAME = "GAME_BD";
    private static final Integer VERSION = 2;

    static final String SAVE = "SAVE_TABLE";
    static final String SAVE_FIELD = "SAVE_FIELD";
    static final String SAVE_SCORE = "SAVE_SCORE";
    static final String SAVE_MODE = "SAVE_MODE";

    static final String SCORE_TOP = "SCORE_TOP";
    static final String SCORE_NAME = "SCORE_NAME";
    static final String SCORE_POINTS = "SCORE_POINTS";

    private Context context;

    DataBase(Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+SAVE+" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SAVE_SCORE + " Integer, "
                + SAVE_MODE + " Integer, "
                + SAVE_FIELD + " TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+SCORE_TOP+" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SCORE_NAME + " TEXT, "
                + SCORE_POINTS + " Integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) {
        if(oldV == 1 && newV == 2){
            updateFrom_1_to_2(sqLiteDatabase);
        }
    }

    private void updateFrom_1_to_2(SQLiteDatabase sqLiteDatabase){
        try{

            String field = "";
            Integer score = 0;
            Cursor cursor;
            if(sqLiteDatabase != null) {
                cursor = sqLiteDatabase.query(DataBase.SAVE, null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        field = cursor.getString(cursor.getColumnIndex(DataBase.SAVE_FIELD));
                        score = cursor.getInt(cursor.getColumnIndex(DataBase.SAVE_SCORE));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }

            Log.v("DataBase", field + " " + score);

            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL("drop table " + SAVE);
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+SAVE+" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SAVE_SCORE + " Integer, "
                    + SAVE_MODE + " Integer, "
                    + SAVE_FIELD + " TEXT);");

            ContentValues cv = new ContentValues();

            cv.put(DataBase.SAVE_SCORE, score);
            cv.put(DataBase.SAVE_FIELD, field);
            cv.put(DataBase.SAVE_MODE, 1);

            sqLiteDatabase.insert(DataBase.SAVE, null, cv);

            sqLiteDatabase.setTransactionSuccessful();
            Log.v("DataBase", "Updated");
        }
        finally {
            sqLiteDatabase.endTransaction();
        }
    }
}
