package net.sourceforge.simcpux.activity;

import net.sourceforge.simcpux.bean.Crime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by yanghu on 2018/4/29.
 */

public class CrimeLab {
    public static CrimeLab crimeLab;

    private List<Crime> crimeList;

    private CrimeLab() {
        crimeList = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Crime criminal = new Crime();
//            criminal.setSolved(i % 2 == 0);
//            criminal.setTitle("Crime #" + i);
//            crimeList.add(criminal);
//        }
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
