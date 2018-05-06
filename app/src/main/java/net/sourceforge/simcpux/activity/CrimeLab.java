package net.sourceforge.simcpux.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.annotation.NonNull;

import net.sourceforge.simcpux.app.FCApplication;
import net.sourceforge.simcpux.bean.Crime;
import net.sourceforge.simcpux.dbhelper.CrimeDBHelper;
import net.sourceforge.simcpux.schema.CrimeDBSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by yanghu on 2018/4/29.
 */

public class CrimeLab {
    public static CrimeLab crimeLab;

    private final SQLiteDatabase crimeDB;

    private CrimeLab() {
        crimeDB = new CrimeDBHelper(FCApplication.getContext()).getWritableDatabase();
    }

    public List<Crime> getCrimeList() {
        ArrayList<Crime> crimes = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = crimeDB.query(CrimeDBSchema.CrimeTable.NAME, null, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Crime crime = new Crime();
                    String uuid = cursor.getString(cursor.getColumnIndex(CrimeDBSchema.Cols.UUID));
                    crime.setId(UUID.fromString(uuid));
                    String title = cursor.getString(cursor.getColumnIndex(CrimeDBSchema.Cols.TITLE));
                    crime.setTitle(title);
                    String suspect = cursor.getString(cursor.getColumnIndex(CrimeDBSchema.Cols.SUSPECT));
                    crime.setSuspect(suspect);
                    String date = cursor.getString(cursor.getColumnIndex(CrimeDBSchema.Cols.DATE));
                    crime.setDate(new Date(Long.valueOf(date)));
                    int solved = cursor.getInt(cursor.getColumnIndex(CrimeDBSchema.Cols.SOLVED));
                    crime.setSolved(solved == 1);

                    crimes.add(crime);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return crimes;
    }

    public File getPhotoFile(Crime crime) {
        File dir = new File(Environment.getExternalStorageDirectory(), "images");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, crime.getPhotoFileName());
    }

    public Crime getCrime(UUID uuid) {
        Cursor cursor = null;
        Crime crime = null;
        try {
            cursor = crimeDB.query(CrimeDBSchema.CrimeTable.NAME, null, CrimeDBSchema.Cols.UUID + "= ?", new String[]{uuid.toString()}, null, null, null);
            while (cursor.moveToNext()) {
                crime = new Crime();
                String uid = cursor.getString(cursor.getColumnIndex(CrimeDBSchema.Cols.UUID));
                crime.setId(UUID.fromString(uid));
                String title = cursor.getString(cursor.getColumnIndex(CrimeDBSchema.Cols.TITLE));
                crime.setTitle(title);
                String suspect = cursor.getString(cursor.getColumnIndex(CrimeDBSchema.Cols.SUSPECT));
                crime.setSuspect(suspect);
                String date = cursor.getString(cursor.getColumnIndex(CrimeDBSchema.Cols.DATE));
                crime.setDate(new Date(Long.valueOf(date)));
                int solved = cursor.getInt(cursor.getColumnIndex(CrimeDBSchema.Cols.SOLVED));
                crime.setSolved(solved == 1);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return crime;
    }

    public static CrimeLab get() {
        if (crimeLab == null) {
            crimeLab = new CrimeLab();
        }
        return crimeLab;
    }

    public void add(Crime crime) {
        crimeDB.insert(CrimeDBSchema.CrimeTable.NAME, null, getContentValues(crime));
    }

    @NonNull
    private ContentValues getContentValues(Crime crime) {
        ContentValues cv = new ContentValues();
        cv.put(CrimeDBSchema.Cols.UUID, crime.getId().toString());
        cv.put(CrimeDBSchema.Cols.TITLE, crime.getTitle());
        cv.put(CrimeDBSchema.Cols.SUSPECT, crime.getSuspect());
        cv.put(CrimeDBSchema.Cols.DATE, crime.getDate().getTime());
        cv.put(CrimeDBSchema.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return cv;
    }

    public void update(Crime crime) {
        crimeDB.update(CrimeDBSchema.CrimeTable.NAME, getContentValues(crime), CrimeDBSchema.Cols.UUID + "= ?", new String[]{crime.getId().toString()});
    }
}
