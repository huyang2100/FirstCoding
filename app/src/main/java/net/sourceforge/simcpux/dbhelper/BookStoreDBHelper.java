package net.sourceforge.simcpux.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yanghu on 2018/3/21.
 */

public class BookStoreDBHelper extends SQLiteOpenHelper {
    public static String TABLE_BOOK = "Book";
    public static final String KEY_NAME = "name";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_PAGENUMBER = "pagenumber";
    public static final String KEY_PRICE = "price";
    public static final String KEY_CODE = "code";
    private static final java.lang.String CREATE_BOOK = "create table " + TABLE_BOOK + " (" +
            "_id integer primary key autoincrement," +
            KEY_NAME + " text," +
            KEY_AUTHOR + " text," +
            KEY_PAGENUMBER + " integer," +
            KEY_PRICE + " real)";
    public static String TABLE_CATEGORY = "Category";
    private static final java.lang.String CREATE_CATEGORY = "create table " + TABLE_CATEGORY + "(" +
            "_id integer primary key autoincrement," +
            KEY_NAME + " text," +
            KEY_CODE + " integer)";
    private final Context context;

    public BookStoreDBHelper(Context context) {
        super(context, "bookstore.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_BOOK);
        db.execSQL("drop table if exists " + TABLE_CATEGORY);
        onCreate(db);
    }
}
