package net.sourceforge.simcpux.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yanghu on 2018/3/21.
 */

public class CoolWeatherDBHelper extends SQLiteOpenHelper {
    /**
     * 字段
     */
    public static final String KEY_NAME = "name";
    public static final String KEY_CODE = "code";
    public static final String KEY_UP_CODE = "upcode";
    public static final String KEY_LEVEL = "level";
    public static final String KEY_WEATHER_ID = "weather_id";

    /**
     * 表：省
     */
    public static String TABLE_PROVINCE = "Province";
    private static final String CREATE_PROVINCE = "create table " + TABLE_PROVINCE + " (" +
            "_id integer primary key autoincrement," +
            KEY_NAME + " text," +
            KEY_CODE + " integer," +
            KEY_LEVEL + " integer)";

    /**
     * 表：市
     */
    public static String TABLE_CITY = "City";
    private static final String CREATE_CITY = "create table " + TABLE_CITY + "(" +
            "_id integer primary key autoincrement," +
            KEY_NAME + " text," +
            KEY_CODE + " integer," +
            KEY_UP_CODE + " integer," +
            KEY_LEVEL + " integer)";

    /**
     * 表：县
     */
    public static String TABLE_COUNTRY = "Country";
    private static final String CREATE_COUNTRY = "create table " + TABLE_COUNTRY + "(" +
            "_id integer primary key autoincrement," +
            KEY_NAME + " text," +
            KEY_CODE + " integer," +
            KEY_UP_CODE + " integer," +
            KEY_LEVEL + " integer," +
            KEY_WEATHER_ID + " text)";

    public CoolWeatherDBHelper(Context context) {
        super(context, "coolweather.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_PROVINCE);
        db.execSQL("drop table if exists " + TABLE_CITY);
        db.execSQL("drop table if exists " + TABLE_COUNTRY);
        onCreate(db);
    }
}
