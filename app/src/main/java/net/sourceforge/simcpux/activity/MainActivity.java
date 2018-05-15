package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.constant.ConstantReceiver;
import net.sourceforge.simcpux.receiver.NetWorkReceiver;
import net.sourceforge.simcpux.utils.PopUtil;
import net.sourceforge.simcpux.wxapi.WXModule;


public class MainActivity extends AppCompatActivity {

    private LocalBroadcastManager broadcastManager;
    private NetWorkReceiver netWorkReceiver;
    private IWXAPI wxapi;
    private long lastPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        regToWX();
        initReceiver();
        initLisenter();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastPressTime >= 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            lastPressTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    private void regToWX() {
        wxapi = WXModule.getInstance().api(this);
    }

    private void initReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        netWorkReceiver = new NetWorkReceiver();
        broadcastManager.registerReceiver(netWorkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (netWorkReceiver != null) {
            broadcastManager.unregisterReceiver(netWorkReceiver);
            netWorkReceiver = null;
        }
    }

    private void initLisenter() {
        findViewById(R.id.recyclerview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.patch9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Patch9Activity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.sum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysUIActivity.actionStart(MainActivity.this);
            }
        });
        findViewById(R.id.translucentbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslucentBarActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.colorStatusBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorStatusBarActivity.actionStart(MainActivity.this, "This is title", "This is content");
            }
        });

        findViewById(R.id.image_nimation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageAnimationActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.fragment_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTestActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.fragment_best_practice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentBestPracticeActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.receiver_code_network).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiverCodeNetWorkActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.receiver_send_stand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast(new Intent(getPackageName() + ConstantReceiver.MY));
            }
        });

        findViewById(R.id.receiver_send_local).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                broadcastManager.sendBroadcast(new Intent(ConnectivityManager.CONNECTIVITY_ACTION));
            }
        });

        findViewById(R.id.receiver_send_offline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.persistence_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersistenceActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.persistence_sp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.share_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String br = System.getProperties().getProperty("line.separator");
                StringBuilder sb = new StringBuilder();
                sb.append("Hello Android!!!").append(br).append("你好中国！！").append(br).append("Hello 苹果！！！");
                sendSms(MainActivity.this, sb.toString());
            }
        });

        findViewById(R.id.share_onekey).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWX();
            }
        });

        findViewById(R.id.show_pw_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View shareRoot = View.inflate(MainActivity.this, R.layout.share_bottom, null);
                final PopupWindow pw = PopUtil.showBottom(MainActivity.this, shareRoot, findViewById(R.id.sv));
                shareRoot.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pw.dismiss();
                    }
                });
            }
        });

        findViewById(R.id.db_bookstore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBBookStoreActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.runtime_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        findViewById(R.id.contentresolver_showcontacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowContactsActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, new Intent(MainActivity.this, RecyclerActivity.class), 0);


                Notification notification = new NotificationCompat.Builder(MainActivity.this, "1")
                        .setContentTitle("First Coding")
                        .setContentText("Yang's MacBook Air 13")
                        .setAutoCancel(true)
                        .setSmallIcon(android.R.drawable.star_on)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img))
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();

                NotificationManager nm = (NotificationManager) MainActivity.this.getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= 26) {
                    nm.createNotificationChannel(new NotificationChannel("1", "channel", NotificationManager.IMPORTANCE_HIGH));
                }
                nm.notify(1, notification);
            }
        });

        findViewById(R.id.take_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePictureActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.play_music).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusicActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.play_movie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMovieActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.download_best_practice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadBestActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.lbs_baidu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LBSBaiduActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.material_design).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDesignActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.listview_multi_viewholder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiViewHolderActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.coolweather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoolWeatherActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.smartrefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartRefreshLayoutActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.slidingtablayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlidingTabLayoutActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.jzvideoplayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JZVideoPlayerActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.mukehome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MuKeHomeActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.url_okhttp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpURLConnectionActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.copytext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("", "\n" +
                        "Many of these poems bear witness to his years spent in India and China \n" +
                        "\n" +
                        "这些诗中有很多见证了他在印度和中国度过的岁月。"));
                Toast.makeText(MainActivity.this, "复制成功！", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.chronometer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ChronometerActivity.newIntent(MainActivity.this));
            }
        });

        findViewById(R.id.crimial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CriminalListActivity.newIntent(MainActivity.this));
            }
        });

        findViewById(R.id.zbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ZXingActivity.newIntent(MainActivity.this));
            }
        });

        findViewById(R.id.view_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ViewConnectActivity.newIntent(MainActivity.this));
            }
        });

        final ImageView iv_icon = findViewById(R.id.iv_icon);
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = iv_icon.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.record_audio_start).getConstantState();
                if (flag) {
                    iv_icon.setImageResource(R.drawable.record_audio_pause);
                } else {
                    iv_icon.setImageResource(R.drawable.record_audio_start);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "你已拒绝该权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void call() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                new AlertDialog.Builder(this)
                        .setMessage("申请Call权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel://10086"));
        startActivity(intent);
    }

    private void shareWX() {
        if (!wxapi.isWXAppInstalled()) {
            Toast.makeText(this, "请先安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }

        WXTextObject textObject = new WXTextObject();
        textObject.text = "分享的内容: 今晚8点老地方不见不散！\r\nAndroid\r\niphone7 plus";

        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.mediaObject = textObject;
        mediaMessage.description = "这是描述内容：重要通知！";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = mediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        wxapi.sendReq(req);
    }


    /**
     * 短信分享
     *
     * @param mContext
     * @param smstext  短信分享内容
     * @return
     */
    public void sendSms(Context mContext, String smstext) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", smstext);

        if (mIntent.resolveActivity(getPackageManager()) != null) {
            mContext.startActivity(mIntent);
        } else {
            Toast.makeText(mContext, "请先安装短信应用！", Toast.LENGTH_SHORT).show();
        }
    }

}
