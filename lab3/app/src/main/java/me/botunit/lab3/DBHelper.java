package me.botunit.lab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.ArrayMap;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    Context context;
    public DBHelper(Context context) {
        super(context, "db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Toast.makeText(context, "DATABASE ONCREATE", Toast.LENGTH_SHORT).show();
        db.execSQL("create table data (id text, value text)");
        ContentValues contentValues = new ContentValues();
        String[] keys = { "input1", "input2", "spinner", "result" };
        for (String key: keys) {
            contentValues = new ContentValues();
            contentValues.put("id", key);
            contentValues.put("value", "");
            db.insert("data", null, contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Map<String, String> getValues(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from data", null );
        res.moveToFirst();
        Map<String, String> m = new HashMap<String, String>();
        while(res.isAfterLast() == false){
            m.put(
                    res.getString(res.getColumnIndex("id")),
                    res.getString(res.getColumnIndex("value"))
            );
            res.moveToNext();
        }
        return m;
    }

    public String getKey(String key){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select * from data where id='" + key + "'", null );
        if (res.getCount() < 1){
            Toast.makeText(context, "getKey('" + key + "'), nothing found", Toast.LENGTH_SHORT).show();
            return "";
        }
        res.moveToFirst();
        return res.getString(res.getColumnIndex("value"));
    }

    public void updateKey(String key, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("value", value);
        db.update("data", contentValues, "id = ? ", new String[] { key } );
    }
}
