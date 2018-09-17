package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.bean.App;
import net.sourceforge.simcpux.constant.ConstantReceiver;
import net.sourceforge.simcpux.constant.ConstantSP;
import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.receiver.NetWorkReceiver;
import net.sourceforge.simcpux.utils.AESCrypt;
import net.sourceforge.simcpux.utils.PopUtil;
import net.sourceforge.simcpux.utils.SPUtil;
import net.sourceforge.simcpux.view.ProgressRequestBody;
import net.sourceforge.simcpux.wxapi.WXModule;
import net.sourceforge.simcpux.xml.MyHandler;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private LocalBroadcastManager broadcastManager;
    private NetWorkReceiver netWorkReceiver;
    private IWXAPI wxapi;
    private long lastPressTime;
    private int soundid;
    private SoundPool soundPool;
    private static final String TAG = "MainActivity";
    private Handler handler = new Handler();
    private int selItem;
    private String xmlStr = "<apps>\n" +
            "\t<app>\n" +
            "\t\t<id>1</id>\n" +
            "\t\t<name>Chrome</name>\n" +
            "\t\t<version>1.0</version>\n" +
            "\t</app>\n" +
            "\t<app>\n" +
            "\t\t<id>2</id>\n" +
            "\t\t<name>Android</name>\n" +
            "\t\t<version>9.0</version>\n" +
            "\t</app>\n" +
            "\t<app>\n" +
            "\t\t<id>3</id>\n" +
            "\t\t<name>Firefox</name>\n" +
            "\t\t<version>2.2</version>\n" +
            "\t</app>\n" +
            "</apps>";

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

        initData();
    }

    private void initData() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundid = soundPool.load(this, R.raw.ding, 1);
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

        android.os.Process.killProcess(android.os.Process.myPid());
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
                LBSBaiduActivity.actionStart(MainActivity.this);
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

        findViewById(R.id.statusbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(StatusBarActivity.newIntent(MainActivity.this));
            }
        });

        findViewById(R.id.alphabet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AlphabetActivity.newIntent(MainActivity.this));
            }
        });

        findViewById(R.id.wakewxscan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //利用Intent打开微信
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
                    intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
                    intent.putExtra("LauncherUI.From.Scaner.Data", Uri.parse("https://www.baidu.com/"));
//                    intent.setData(Uri.parse("https://www.baidu.com/"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction("android.intent.action.VIEW");
                    startActivity(intent);
                } catch (Exception e) {
                    //若无法正常跳转，在此进行错误处理
                    Toast.makeText(MainActivity.this, "无法跳转到微信，请检查您是否安装了微信！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.touchevent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TouchEventActivity.newIntent(MainActivity.this));
            }
        });

        findViewById(R.id.compassimg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CompassImgActivity.newIntent(MainActivity.this));
            }
        });

        findViewById(R.id.soundpool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(soundid, 1f, 1f, 1, 0, 1f);
            }
        });
        findViewById(R.id.serianim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SeriAnimActivity.newIntent(MainActivity.this));
            }
        });
        findViewById(R.id.uploadsuggest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSuggest();
            }
        });
        final Button btn_aes = findViewById(R.id.aesencrypt);
        btn_aes.setOnClickListener(new View.OnClickListener() {

            private String msgCrypt;

            @Override
            public void onClick(View v) {

                if (btn_aes.getText().equals("AES加密")) {
                    btn_aes.setText("AES解密");
                    try {
                        msgCrypt = AESCrypt.encrypt("123", "中国");
                        Toast.makeText(MainActivity.this, msgCrypt, Toast.LENGTH_SHORT).show();
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                } else {
                    btn_aes.setText("AES加密");
                    try {
                        String msg = AESCrypt.decrypt("123", msgCrypt);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        findViewById(R.id.login_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
            }
        });
        findViewById(R.id.bug_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 9 / 0;
            }
        });

        findViewById(R.id.justifytext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JustifyTextActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.choice_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //多选
//                new AlertDialog.Builder(MainActivity.this)
//                        .setMultiChoiceItems(new String[]{"选项一", "选项二"}, new boolean[]{true, false}, new DialogInterface.OnMultiChoiceClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//
//                            }
//                        }).create().show();

                //单选
                selItem = SPUtil.getInt(ConstantSP.KEY_SEL_ITEM);
                new AlertDialog.Builder(MainActivity.this)
                        .setSingleChoiceItems(new String[]{"审判法官", "执行法官"}, selItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selItem = which;
                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                SPUtil.putInt(ConstantSP.KEY_SEL_ITEM, selItem);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null).create().show();
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

        findViewById(R.id.btn_xml_pull).setOnClickListener(new View.OnClickListener() {

            private App app;

            @Override
            public void onClick(View v) {

                try {
                    XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                    xmlPullParser.setInput(new StringReader(xmlStr));
                    int eventType = xmlPullParser.getEventType();
                    ArrayList<App> appList = new ArrayList<>();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String tagName = xmlPullParser.getName();
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if (tagName.equals("app")) {
                                    app = new App();
                                } else if (tagName.equals("id")) {
                                    app.setId(xmlPullParser.nextText());
                                } else if (tagName.equals("name")) {
                                    app.setName(xmlPullParser.nextText());
                                } else if (tagName.equals("version")) {
                                    app.setVersion(xmlPullParser.nextText());
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if(tagName.equals("app")){
                                    appList.add(app);
                                }
                                break;
                        }

                        eventType = xmlPullParser.next();
                    }

                    Toast.makeText(MainActivity.this, Arrays.toString(appList.toArray()), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_xml_sax).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
                    MyHandler myHandler = new MyHandler();
                    XMLReader xmlReader = saxParser.getXMLReader();
                    xmlReader.setContentHandler(myHandler);
                    xmlReader.parse(new InputSource(new StringReader(xmlStr)));
                    Toast.makeText(MainActivity.this, Arrays.toString(myHandler.getAppList().toArray()), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 上传意见反馈
     */
    private void uploadSuggest() {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        MultipartBody multipartBody = builder
                .setType(MultipartBody.FORM)
                .addFormDataPart("data", "{\"ApplicationID\":null,\"Timestamp\":null,\"SecretKey\":null,\"Data\":{\"SystemId\":\"3519377481238839296\",\"Author\":null,\"Phone\":null,\"FeedbackText\":\"网上办公123456\",\"FeedbackVoice\":null,\"DisposeState\":0,\"DisposeState2\":0,\"DisposeState3\":0,\"Remarks\":null,\"CreateDate\":\"0001-01-01T00:00:00\"}}")
                .addFormDataPart("imgcount", "5")
                .addFormDataPart("imgstream1", "img1", RequestBody.create(MediaType.parse("image/jpg"), new File(Environment.getExternalStorageDirectory(), "/temp/test1.jpg")))
                .addFormDataPart("imgstream2", "img2", RequestBody.create(MediaType.parse("image/jpg"), new File(Environment.getExternalStorageDirectory(), "/temp/test2.jpg")))
                .addFormDataPart("imgstream3", "img3", RequestBody.create(MediaType.parse("image/jpg"), new File(Environment.getExternalStorageDirectory(), "/temp/test3.jpg")))
                .addFormDataPart("imgstream4", "img4", RequestBody.create(MediaType.parse("image/jpg"), new File(Environment.getExternalStorageDirectory(), "/temp/test4.jpg")))
                .addFormDataPart("imgstream5", "img5", RequestBody.create(MediaType.parse("image/jpg"), new File(Environment.getExternalStorageDirectory(), "/temp/test5.jpg")))
                .build();

        View view = View.inflate(this, R.layout.view_suggest_progress, null);
        final ProgressBar pb = view.findViewById(R.id.pb);
        final TextView tv = view.findViewById(R.id.tv);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("上传中...")
                .setCancelable(false)
                .setView(view)
                .create();
        alertDialog.show();

        Request request = new Request.Builder()
                .url("http://ewmfk.bjrenrentong.com/api/FeedbackService/FeedbackInfo/appadd")
                .post(new ProgressRequestBody(multipartBody, new ProgressRequestBody.ProgressRequestListener() {
                    @Override
                    public void onProgress(long bytesWritten, long contentLength) {
                        int progress = (int) ((bytesWritten * 100) / contentLength);
                        pb.setProgress(progress);
                        tv.setText(progress + "%");
                    }
                })).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.i(TAG, e.toString());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                });
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
