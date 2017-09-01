package dejssa.lines.Cash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dejssa.lines.Score.Score;
import dejssa.lines.Score.ScoreCompare;
import dejssa.lines.gameField.Square;

/**
 * Created by Алексей on 30.08.2017.
 */

public class DataBaseOperations  {

    private final DataBase dataBase;
    private SQLiteDatabase sqLiteDatabase;

    private final String index = "GAME";

    public DataBaseOperations(Context context) {
        dataBase = new DataBase(context);
    }

    public void saveGame(Square[][] field, Integer score){
        deletePrevious();

        StringBuilder fieldToSave = new StringBuilder(100);

        for(int i = 0; i < field.length; i++) {
            for(int j = 0; j < field.length; j++){
                fieldToSave.append(field[i][j].getColor());
            }
        }

        Log.v("Save - field", fieldToSave.toString());

        sqLiteDatabase = dataBase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DataBase.SAVE_SCORE, score);
        cv.put(DataBase.SAVE_FIELD, fieldToSave.toString());

        sqLiteDatabase.insert(DataBase.SAVE, null, cv);

        sqLiteDatabase.close();
    }

    private void deletePrevious(){
        Object[] savedGame = loadGame();
        if(savedGame != null){
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.delete(DataBase.SAVE, DataBase.SAVE_SCORE + " = ? ", new String[]{savedGame[1].toString()});
        }
    }

    public Object[] loadGame(){
        sqLiteDatabase = dataBase.getReadableDatabase();
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

        return field.equals("") && score == 0 ? null : new Object[]{field, score};
    }

    public ArrayList<Score> restoreScore(){
        sqLiteDatabase = dataBase.getReadableDatabase();
        ArrayList<Score> scoreList = new ArrayList<>();
        Cursor cursor;
        if(sqLiteDatabase != null) {
            cursor = sqLiteDatabase.query(DataBase.SCORE_TOP, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(DataBase.SCORE_NAME));
                    Integer points = cursor.getInt(cursor.getColumnIndex(DataBase.SCORE_POINTS));
                    scoreList.add(new Score(points, name));
                    Log.v("Score - ", name + " " + points);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        Collections.sort(scoreList, new ScoreCompare());

        return scoreList;
    }

    public void saveScore(List<Score> scoreList){

        Collections.sort(scoreList, new ScoreCompare());

        while(scoreList.size() > 5)
            scoreList.remove(5);

        sqLiteDatabase = dataBase.getWritableDatabase();

        sqLiteDatabase.execSQL("delete from "+ DataBase.SCORE_TOP);

        ContentValues cv = new ContentValues();

        for(Score score : scoreList) {

            cv.put(DataBase.SCORE_NAME, score.getName());
            cv.put(DataBase.SCORE_POINTS, score.getPoints());

            sqLiteDatabase.insert(DataBase.SCORE_TOP, null, cv);

        }

        sqLiteDatabase.close();

    }
}
