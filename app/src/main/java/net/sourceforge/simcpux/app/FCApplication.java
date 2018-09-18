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
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import net.sourceforge.simcpux.R;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by yanghu on 2018/4/10.
 */

public class FCApplication extends TinkerApplication {

    public FCApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "net.sourceforge.simcpux.app.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

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
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static Context getContext() {
        return context;
    }

}
