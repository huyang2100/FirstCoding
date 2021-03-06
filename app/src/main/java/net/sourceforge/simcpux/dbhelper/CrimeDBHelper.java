package net.sourceforge.simcpux.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.sourceforge.simcpux.schema.CrimeDBSchema;

/**
 * Created by yanghu on 2018/5/3.
 */

public class CrimeDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crime.db";

    public CrimeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeDBSchema.CrimeTable.NAME + "(" +
                "_id integer primary key autoincrement," +
                CrimeDBSchema.Cols.UUID + " text," +
                CrimeDBSchema.Cols.TITLE + " text," +
                CrimeDBSchema.Cols.SUSPECT + " text," +
                CrimeDBSchema.Cols.DATE + " text," +
                CrimeDBSchema.Cols.SOLVED + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
