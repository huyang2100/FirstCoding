package net.sourceforge.simcpux.activity;

import net.sourceforge.simcpux.bean.Crime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by yanghu on 2018/4/29.
 */

public class CrimeLab {
    public static CrimeLab crimialLab;

    private List<Crime> criminalList;

    private CrimeLab() {
        criminalList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime criminal = new Crime();
            criminal.setSolved(i % 2 == 0);
            criminal.setTitle("Crime #" + i);
            criminalList.add(criminal);
        }
    }

    public List<Crime> getCrimeList() {
        return criminalList;
    }

    public Crime getCriminal(UUID uuid){
        for(Crime criminal : criminalList){
            if(criminal.getId().equals(uuid)){
                return criminal;
            }
        }

        return null;
    }

    public static CrimeLab get() {
        if (crimialLab == null) {
            crimialLab = new CrimeLab();
        }
        return crimialLab;
    }
}
