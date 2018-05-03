package net.sourceforge.simcpux.activity;

import android.database.sqlite.SQLiteDatabase;

import net.sourceforge.simcpux.app.FCApplication;
import net.sourceforge.simcpux.bean.Crime;
import net.sourceforge.simcpux.dbhelper.CrimeDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by yanghu on 2018/4/29.
 */

public class CrimeLab {
    public static CrimeLab crimeLab;

    private List<Crime> crimeList;
    private final SQLiteDatabase crimeDB;

    private CrimeLab() {
        crimeList = new ArrayList<>();
        crimeDB = new CrimeDBHelper(FCApplication.getContext()).getWritableDatabase();
    }

    public List<Crime> getCrimeList() {
        return crimeList;
    }

    public Crime getCriminal(UUID uuid){
        for(Crime criminal : crimeList){
            if(criminal.getId().equals(uuid)){
                return criminal;
            }
        }

        return null;
    }

    public static CrimeLab get() {
        if (crimeLab == null) {
            crimeLab = new CrimeLab();
        }
        return crimeLab;
    }

    public void add(Crime crime) {
        crimeList.add(crime);
    }
}
