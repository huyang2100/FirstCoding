package net.sourceforge.simcpux.activity;

import net.sourceforge.simcpux.bean.Criminal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by yanghu on 2018/4/29.
 */

public class CriminalLab {
    public static CriminalLab crimialLab;

    private List<Criminal> criminalList;

    private CriminalLab() {
        criminalList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Criminal criminal = new Criminal();
            criminal.setSolved(i % 2 == 0);
            criminal.setTitle("Criminal #" + i);
            criminalList.add(criminal);
        }
    }

    public List<Criminal> getCriminalList() {
        return criminalList;
    }

    public Criminal getCriminal(UUID uuid){
        for(Criminal criminal : criminalList){
            if(criminal.getId().equals(uuid)){
                return criminal;
            }
        }

        return null;
    }

    public static CriminalLab get() {
        if (crimialLab == null) {
            crimialLab = new CriminalLab();
        }
        return crimialLab;
    }
}
