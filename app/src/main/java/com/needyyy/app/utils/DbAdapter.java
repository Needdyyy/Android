package com.needyyy.app.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Random;

/**
 * Created by Abhishek Singh Arya on 16-03-2016.
 */
public class DbAdapter extends SQLiteOpenHelper {
    private static DbAdapter mDbHelper;
    public static final String DATABASE_NAME = "needyyy";

    /*----------COLORCODE TABLE CONSTANTS----------*/
    public static final String TABLE_NAME_COLORCODE = "colorcode";

    /*----------COLORCODE TABLE CREATION----------*/
    private final String DATABASE_CREATE_TABLE_COLORCODE = "CREATE TABLE "
            + TABLE_NAME_COLORCODE + "("
            + Constant.USER_ID + ", "
            + Constant.COLOR_CODE
            + ")";

    public DbAdapter(Context context) throws PackageManager.NameNotFoundException {
        super(context, DATABASE_NAME, null, context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
    }

    public static synchronized DbAdapter getInstance(Context context) {
        if (mDbHelper == null) {
            try {
                mDbHelper = new DbAdapter(context);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mDbHelper;
    }

    public String insertQuery(String TABLE_NAME, ContentValues contentValues)
            throws SQLException {
        long id = 0;
        try {
            final SQLiteDatabase writableDatabase = getWritableDatabase();
            id = writableDatabase.insert(TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(id);
    }

    public Cursor fetchQuery(String TABLE_NAME, String userid) {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE user_id =?";
        final SQLiteDatabase readableDatabase = getReadableDatabase();
        final Cursor cursor = readableDatabase.rawQuery(selectQuery, new String[] {userid});
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void deleteAll(String TABLE_NAME) {
        try {
            final SQLiteDatabase writableDatabase = getWritableDatabase();
            writableDatabase.delete(TABLE_NAME, null, null);
        } catch (Exception e) {
            Log.e("deleteAll", e.toString());
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TABLE_COLORCODE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_COLORCODE);
        onCreate(db);
    }

    public void saveColorforUser(String userid, int i) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.USER_ID, userid);
        contentValues.put(Constant.COLOR_CODE, i);
        insertQuery(DbAdapter.TABLE_NAME_COLORCODE, contentValues);
    }
    public Integer getColorforUser(String userid) {
        Integer colorcode = 0;
        Cursor cursor = fetchQuery(DbAdapter.TABLE_NAME_COLORCODE, userid);
        for (int i = 0; i < cursor.getCount(); i++) {
            colorcode = cursor.getInt(cursor.getColumnIndex(Constant.COLOR_CODE));
            cursor.moveToNext();
        }
        return colorcode;
    }

    public Integer getColor(String userid) {
        Random rdm = new Random();
        int value = rdm.nextInt(8) + 1;
        int colorcode = 0;
        colorcode = getColorforUser(userid);
        if (colorcode == 0) {
            saveColorforUser(userid, value);
            colorcode = value;
        }
        return colorcode;
    }
}
