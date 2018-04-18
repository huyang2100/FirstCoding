package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.bean.App;
import net.sourceforge.simcpux.net.OkHttpMananger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class HttpURLConnectionActivity extends AppCompatActivity {

    private TextView tv;
    private OkHttpClient client = new OkHttpClient();
    private StringBuilder sb = new StringBuilder();
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private File file = null;
    private static final String IMGUR_CLIENT_ID = "123";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private File imgFile;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_urlconnection);

        initView();
        initData();
        initLisenter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    copyFile();
                } else {
                    finish();
                }
                break;
        }
    }

    private void initData() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }


        copyFile();
    }

    private void copyFile() {
        try {
            InputStream is = getResources().openRawResource(R.raw.readme);
            File dir = new File(Environment.getExternalStorageDirectory(), getPackageName());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(dir, "readme.md");
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();


            InputStream imgIs = getResources().openRawResource(R.raw.ic_launcher_round);
            imgFile = new File(dir, "img.png");
            FileOutputStream imgFos = new FileOutputStream(imgFile);
            while ((len = imgIs.read(buffer)) != -1) {
                imgFos.write(buffer, 0, len);
            }
            imgFos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initLisenter() {
        findViewById(R.id.btn_okhttp_manager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpMananger.get("https://www.baidu.com/", new OkHttpMananger.CallBack() {
                    @Override
                    public void onSuccess(String str) {
                        Toast.makeText(HttpURLConnectionActivity.this, str, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onErr(Exception e) {
                        Toast.makeText(HttpURLConnectionActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.btn_okhttp_manager_poststring).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpMananger.postString("https://api.github.com/markdown/raw", new OkHttpMananger.CallBack() {
                    @Override
                    public void onSuccess(String str) {
                        Toast.makeText(HttpURLConnectionActivity.this, str, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onErr(Exception e) {
                        Toast.makeText(HttpURLConnectionActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.btn_xml_pull).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request.Builder().url("http://10.0.2.2:8080/get_data.xml").build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        showResponse(e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            showResponse(parseXMLWithPull(response.body().string()));
                        } else {
                            showResponse(response.code() + "");
                        }
                    }
                });
            }
        });

        findViewById(R.id.btn_json_object).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request.Builder().url("http://10.0.2.2:8080/get_data.json").build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        showResponse(e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            showResponse(parseJSONWithJSONObject(response.body().string()));
                        } else {
                            showResponse(response.code() + "");
                        }
                    }
                });
            }
        });

        findViewById(R.id.btn_json_gson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request.Builder().url("http://10.0.2.2:8080/get_data.json").build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        showResponse(e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            showResponse(parseJSONWithGson(response.body().string()));
                        } else {
                            showResponse("" + response.code());
                        }
                    }
                });
            }
        });

        findViewById(R.id.btn_gson_json).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<App> appArrayList = new ArrayList<>();

                for (int i = 1; i < 5; i++) {
                    App app = new App();
                    app.setId("00" + i);
                    app.setVersion("V1.0" + i);
                    app.setName("Android程序员的自我修养: " + i);
                    appArrayList.add(app);
                }

                String json = gson.toJson(appArrayList);
                showResponse(json);
            }
        });

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
//                            URL url = new URL("https://www.baidu.com/");
                            URL url = new URL("http://app.bjcourt.gov.cn/interface.action?method=get_channel&ver=1.0&uid=211&fayuan=true");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setReadTimeout(8000);
                            conn.setConnectTimeout(8000);
                            int responseCode = conn.getResponseCode();
                            if (responseCode == 200) {
                                InputStream is = conn.getInputStream();
                                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                                String line = null;
                                StringBuilder sb = new StringBuilder();
                                while ((line = br.readLine()) != null) {
                                    sb.append(line);
                                }
                                br.close();
                                is.close();
                                conn.disconnect();

                                showResponse(sb.toString());
                            } else {
                                showResponse("服务器出错: " + responseCode);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        findViewById(R.id.btn_ok_synchronous_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Request request = new Request.Builder().url("https://publicobject.com/helloworld.txt").build();
                        try {
                            Response response = client.newCall(request).execute();
                            if (response.isSuccessful()) {
                                showResponse(response.body().string());
                            } else {
                                showResponse("Server: " + response.code());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        findViewById(R.id.btn_ok_asynchronous_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Uri.parse("http://app.bjcourt.gov.cn/interface.action").buildUpon()
                        .appendQueryParameter("method", "get_channel")
                        .appendQueryParameter("ver", "1.0")
                        .appendQueryParameter("uid", "211")
                        .appendQueryParameter("fayuan", "true")
                        .build().toString();
                Request request = new Request.Builder().url(url).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        showResponse(e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String string = response.body().string();
                            showResponse(string);
                        } else {
                            showResponse("" + response.code());
                        }
                    }
                });
            }
        });

        findViewById(R.id.btn_access_headers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Request request = new Request.Builder().url("https://publicobject.com/helloworld.txt")
                                .header("User-Agent", "Android&OkHttp3")
                                .addHeader("Accept", "application/json; q=0.5")
                                .addHeader("Accept", "application/vnd.github.v3+json")
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                showResponse(e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    Headers headers = response.headers();
                                    Set<String> names = headers.names();
                                    sb.setLength(0);
                                    for (String name : names) {
                                        String value = headers.get(name);
                                        sb.append(name).append("---").append(value).append("\r\n");
                                    }
                                    showResponse(sb.toString());
                                } else {
                                    showResponse("" + response.code());
                                }
                            }
                        });
                    }
                }.start();
            }
        });

        findViewById(R.id.btn_post_string).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        String postBody = ""
                                + "Releases\n"
                                + "--------\n"
                                + "\n"
                                + " * _1.0_ May 6, 2013\n"
                                + " * _1.1_ June 15, 2013\n"
                                + " * _1.2_ August 11, 2013\n";
                        Request request = new Request.Builder().url("https://api.github.com/markdown/raw")
                                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                showResponse(e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    showResponse(response.body().string());
                                } else {
                                    showResponse("" + response.code());
                                }
                            }
                        });
                    }
                }.start();
            }
        });

        findViewById(R.id.btn_post_streaming).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        RequestBody requestBody = new RequestBody() {
                            @Nullable
                            @Override
                            public MediaType contentType() {
                                return MEDIA_TYPE_MARKDOWN;
                            }

                            @Override
                            public void writeTo(BufferedSink sink) throws IOException {
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sink.outputStream()));
                                bw.write("Hello Android!");
                                bw.newLine();
                                bw.write("I am Yang.");
                                bw.newLine();
                                bw.write("I from China,Shanxi province");
                                bw.newLine();
                                bw.write("I am android engineer.");
                                bw.flush();
                                bw.close();
                            }
                        };

                        Request request = new Request.Builder().url("https://api.github.com/markdown/raw")
                                .post(requestBody)
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                showResponse(e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    showResponse(response.body().string());
                                } else {
                                    showResponse("" + response.code());
                                }
                            }
                        });
                    }
                }.start();
            }
        });

        findViewById(R.id.btn_post_afile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        if (file == null) {
                            return;
                        }

                        Request request = new Request.Builder().url("https://api.github.com/markdown/raw")
                                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                showResponse(e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    showResponse(response.body().string());
                                } else {
                                    showResponse("" + response.code());
                                }
                            }
                        });
                    }
                }.start();
            }
        });

        findViewById(R.id.btn_post_formparameters).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        FormBody formBody = new FormBody.Builder()
                                .add("search", "android")
                                .build();
                        Request request = new Request.Builder().url("https://en.wikipedia.org/w/index.php")
                                .post(formBody)
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                showResponse(e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    showResponse(response.body().string());
                                } else {
                                    showResponse("" + response.code());
                                }
                            }
                        });
                    }
                }.start();
            }
        });

        findViewById(R.id.btn_post_multipartparameters).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        MultipartBody multipartBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("title", "FirstCoding Logo")
                                .addFormDataPart("image", "logo-firstcoding.png", RequestBody.create(MEDIA_TYPE_PNG, imgFile))
                                .build();

                        Request request = new Request.Builder().url("https://api.imgur.com/3/image")
                                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                                .post(multipartBody)
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                showResponse(e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    showResponse(response.body().string());
                                } else {
                                    showResponse("" + response.code());
                                }
                            }

                        });
                    }
                }.start();
            }
        });
    }

    private String parseJSONWithGson(String string) {
        List<App> appList = gson.fromJson(string, new TypeToken<List<App>>() {
        }.getType());
        if (appList != null) {
            sb.setLength(0);
            sb.append("gson: ").append("\r\n");
            for (App app : appList) {
                sb.append("id: ").append(app.getId()).append("version: ").append(app.getVersion()).append("name: ").append(app.getName()).append("\r\n");
            }
        }
        return sb.toString();
    }

    private String parseJSONWithJSONObject(String xmlData) {
        try {
            sb.setLength(0);
            JSONArray jsonArray = new JSONArray(xmlData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                sb.append("id: ").append(id).append("||").append("version: ").append(version).append("||").append("name").append(name).append("\r\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String parseXMLWithPull(String content) {
        try {
            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlPullParser.setInput(new StringReader(content));
            int eventType = xmlPullParser.getEventType();
            sb.setLength(0);
            while ((eventType) != XmlPullParser.END_DOCUMENT) {
                String tagName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("id".equals(tagName)) {
                            sb.append("id: ").append(xmlPullParser.nextText()).append(" | ");
                        } else if ("name".equals(tagName)) {
                            sb.append("name: ").append(xmlPullParser.nextText()).append(" | ");
                        } else if ("version".equals(tagName)) {
                            sb.append("version: ").append(xmlPullParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        sb.append("\r\n");
                        break;
                }

                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void showResponse(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(content);
            }
        });
    }

    private void initView() {
        tv = findViewById(R.id.tv);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, HttpURLConnectionActivity.class);
        context.startActivity(intent);
    }
}
