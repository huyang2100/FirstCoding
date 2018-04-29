package net.sourceforge.simcpux.activity;

import net.sourceforge.simcpux.bean.Criminal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanghu on 2018/4/29.
 */

public class CrimialLab {
    public static CrimialLab crimialLab;

    private List<Criminal> criminalList;

    private CrimialLab() {
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

    public static CrimialLab get() {
        if (crimialLab == null) {
            crimialLab = new CrimialLab();
        }
        return crimialLab;
    }
}
