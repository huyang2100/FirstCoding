package net.sourceforge.simcpux.wxapi;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.constant.ConstantWX;

/**
 * Created by yanghu on 2018/3/21.
 */

public class WXModule {
    private static WXModule wxModule = new WXModule();

    public static WXModule getInstance() {
        return wxModule;
    }

    public IWXAPI api(Context context) {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, ConstantWX.APP_ID, true);
        wxapi.registerApp(ConstantWX.APP_ID);
        return wxapi;
    }
}
