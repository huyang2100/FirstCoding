package net.sourceforge.simcpux.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;


import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;

import net.sourceforge.simcpux.R;

/**
 * Created by yanghu on 2018/4/10.
 */

public class FCApplication extends Application {

    private static Context context;
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary,android.R.color.white);
                ClassicsHeader classicsHeader = new ClassicsHeader(context);
                classicsHeader.setSpinnerStyle(SpinnerStyle.FixedBehind);
                return classicsHeader;
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        CrashReport.initCrashReport(getApplicationContext(), "27314ebfdd", true);
    }

    public static Context getContext() {
        return context;
    }

}
